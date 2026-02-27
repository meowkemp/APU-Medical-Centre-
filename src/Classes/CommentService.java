package Classes;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class CommentService {
    private final Path commentsFile;
    private final Path appointmentsFile;
    private final Path usersFile;

    private static final Pattern PIPE = Pattern.compile("\\|");
    private static final DateTimeFormatter DATE_FMT     = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FMT     = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter TS_FMT       = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // --- constructors ---
    public CommentService(File comments, File appointments, File users) {
        this.commentsFile     = comments.toPath();
        this.appointmentsFile = appointments.toPath();
        this.usersFile        = users.toPath();
    }
    // convenience: try data/ then src/Data/
    public CommentService() {
        this(resolve("data/comments.txt","src/Data/comments.txt"),
             resolve("data/appointments.txt","src/Data/appointments.txt"),
             resolve("data/users.txt","src/Data/users.txt"));
    }
    private static File resolve(String p1, String p2) {
        File f1 = new File(p1);
        return f1.exists() ? f1 : new File(p2);
    }

    /** Loads comments  */
    public List<String> loadComments(DefaultTableModel model) {
        List<String> warnings = new ArrayList<>();
        if (commentsFile == null) {
            warnings.add("comments.txt not found: (null path)");
            return warnings;
        }
        try (BufferedReader br = Files.newBufferedReader(commentsFile, StandardCharsets.UTF_8)) {
            String line; int lineNo = 0;
            while ((line = br.readLine()) != null) {
                lineNo++;
                String raw = line.trim();
                if (raw.isEmpty() || raw.startsWith("#")) continue;

                String[] parts = raw.split("\\|", -1);
                if (parts.length < 6) {
                    warnings.add("Line " + lineNo + ": malformed (expected 6+ fields). Skipped.");
                    continue;
                }

                String commentId  = parts[0].trim();
                String customerId = parts[1].trim();
                String recipient  = parts[2].trim();
                String apptId     = parts[3].trim();

                // legacy: 6 fields (already combined timestamp); newer: 7 fields (date + time)
                String dateTime;
                if (parts.length >= 7) {
                    dateTime = parts[4].trim() + " " + parts[5].trim();
                } else {
                    dateTime = parts[4].trim();
                }
                String comment = parts[parts.length >= 7 ? 6 : 5].trim();

                if (apptId.isEmpty()) {
                    String idOrLine = commentId.isEmpty() ? ("line " + lineNo) : ("ID " + commentId);
                    warnings.add("Skipped comment " + idOrLine + ": missing Appointment ID.");
                    continue;
                }
                recipient = recipient.replaceAll("\\s*,\\s*", ",");
                if (!recipient.isEmpty()) {
                    for (String r : recipient.split(",")) {
                        String rr = r.trim();
                        if (!rr.matches("^[DS]\\d{4}$")) {
                            warnings.add("Line " + lineNo + ": recipient '" + rr +
                                         "' not in expected form (D#### or S####).");
                        }
                    }
                }
                model.addRow(new Object[]{ commentId, customerId, recipient, apptId, dateTime, comment });
            }
        } catch (NoSuchFileException | FileNotFoundException fnf) {
            warnings.add("comments.txt not found: " + commentsFile);
        } catch (IOException ioEx) {
            warnings.add("I/O error reading comments.txt: " + ioEx.getMessage());
        }
        return warnings;
    }

    public DefaultTableModel loadAppointmentsForCustomer(String[] cols, String customerId) {
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        try {
            if (!Files.exists(appointmentsFile)) return model;
            for (String line : Files.readAllLines(appointmentsFile, StandardCharsets.UTF_8)) {
                if (line.isBlank()) continue;
                String[] p = PIPE.split(line, -1);
                if (p.length < 14) continue;

                String custId = p[1].trim();
                String status = p[13].trim();
                if (!custId.equals(customerId)) continue;

                // Show only SCHEDULED/COMPLETED here (match your friend's page)
                if (!(status.equalsIgnoreCase("COMPLETED") || status.equalsIgnoreCase("SCHEDULED")))
                    continue;

                String cancel = (p.length >= 15) ? p[14].trim() : "";

                model.addRow(new Object[]{
                    p[0].trim(), p[1].trim(), p[5].trim(), p[6].trim(),
                    p[7].trim(), p[8].trim(), p[12].trim(), status, cancel
                });
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                "Error reading appointments file: " + e.getMessage(),
                "File Error", JOptionPane.ERROR_MESSAGE);
        }
        return model;
    }

    // ------- Comment creation/saving -------

    public String nextCommentId() {
        int max = 0;
        try {
            if (!Files.exists(commentsFile)) return "CM0001";
            for (String line : Files.readAllLines(commentsFile, StandardCharsets.UTF_8)) {
                if (line.isBlank()) continue;
                String[] p = PIPE.split(line, -1);
                if (p.length > 0 && p[0].startsWith("CM")) {
                    try {
                        int n = Integer.parseInt(p[0].substring(2));
                        if (n > max) max = n;
                    } catch (NumberFormatException ignore) {}
                }
            }
        } catch (IOException ignored) {}
        return String.format("CM%04d", max + 1);
    }

    /** Overload to  pass the specific timestamp */
    public Comment createComment(String fromUserId, String role, String appointmentId, String text,
                                 LocalDateTime createdAt) {
        Comment c = new Comment();
        c.setId(nextCommentId());
        c.setFromUserId(fromUserId);
        c.setText(text);

        if (createdAt == null) createdAt = LocalDateTime.now();
        // store date/time separately in Comment, but we will WRITE as one combined field
        c.setDate(createdAt.toLocalDate());
        c.setTime(createdAt.toLocalTime().withSecond(0).withNano(0));

        String r = (role == null ? "" : role).toUpperCase(Locale.ROOT);
        if ("STAFF".equals(r)) {
            c.setAppointmentId((appointmentId != null && !appointmentId.isBlank()) ? appointmentId : "NA");
            c.setToUserId(pickRandomStaffId());
        } else if ("DOCTOR".equals(r)) {
            c.setAppointmentId(appointmentId);
            c.setToUserId(getDoctorIdFromAppointment(appointmentId));
        } else {
            c.setAppointmentId((appointmentId == null) ? "NA" : appointmentId);
            c.setToUserId("");
        }
        return c;
    }

    /** Uses the current time. */
    public Comment createComment(String fromUserId, String role, String appointmentId, String text) {
        return createComment(fromUserId, role, appointmentId, text, LocalDateTime.now());
    }


    public void saveComment(Comment c) {
        String ts = c.getDate().format(DATE_FMT) + " " + c.getTime().format(TIME_FMT);
        String row = String.join("|",
            nz(c.getId()),
            nz(c.getFromUserId()),
            nz(c.getToUserId()),
            nz(c.getAppointmentId()),
            ts,
            nz(c.getText())
        );
        try {
            Files.createDirectories(commentsFile.getParent());
            boolean addNewline = Files.exists(commentsFile) && Files.size(commentsFile) > 0;
            try (BufferedWriter w = Files.newBufferedWriter(commentsFile, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                if (addNewline) w.newLine();
                w.write(row);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                "Oops! Could not save comment: " + e.getMessage(),
                "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getDoctorIdFromAppointment(String appointmentId) {
        if (appointmentId == null || appointmentId.isBlank()) return null;
        try {
            if (!Files.exists(appointmentsFile)) return null;
            for (String line : Files.readAllLines(appointmentsFile, StandardCharsets.UTF_8)) {
                if (line.isBlank()) continue;
                String[] p = PIPE.split(line, -1);
                if (p.length >= 14 && appointmentId.equals(p[0])) {
                    return p[9].trim(); // doctorId column
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                "Error reading appointments file to fetch doctor ID",
                "File Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    private String pickRandomStaffId() {
        try {
            if (!Files.exists(usersFile)) return "STAFF_UNKNOWN";
            List<String> staff = new ArrayList<>();
            for (String line : Files.readAllLines(usersFile, StandardCharsets.UTF_8)) {
                if (line.isBlank()) continue;
                String[] p = PIPE.split(line, -1);
                if (p.length >= 2 && "STAFF".equalsIgnoreCase(p[1])) {
                    staff.add(p[0].trim());
                }
            }
            if (staff.isEmpty()) return "STAFF_UNKNOWN";
            return staff.get(new Random().nextInt(staff.size()));
        } catch (IOException e) {
            return "STAFF_UNKNOWN";
        }
    }

    private static String nz(String s){ return s==null ? "" : s; }
}
