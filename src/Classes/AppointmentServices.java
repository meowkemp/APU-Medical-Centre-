package Classes;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class AppointmentServices {

    private final Path file;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");
    private static final Pattern PIPE_SPLIT = Pattern.compile("\\|");

    public AppointmentServices(File f) {
        this.file = f.toPath();
    }

    /* -------- ID generation like your Staff/Manager -------- */
    public String peekNextAppointmentId() {
        int max = 0;
        try {
            if (!Files.exists(file)) return "A0001";
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                if (line.isBlank()) continue;
                String[] p = PIPE_SPLIT.split(line, -1);
                if (p.length > 0 && p[0].startsWith("A")) {
                    int n = Integer.parseInt(p[0].substring(1));
                    if (n > max) max = n;
                }
            }
        } catch (IOException ignored) {}
        return String.format("A%04d", max + 1);
    }

    /* -------- Add and return new ID -------- */
    public String addAppointmentReturnId(
            String customerId,
            String customerIc,
            String customerName,
            String customerPhone,
            String customerEmail,
            LocalDate date,
            LocalTime time,
            String purposeOfVisit,
            String preferredDoctorName,
            String doctorId,
            String paymentMethod,
            String paymentArrangement,
            String specialRequest
    ) {
        String newId = peekNextAppointmentId();
        String status = (doctorId != null && !doctorId.isBlank()) ? "ASSIGNED" : "PENDING";
        String sr = (specialRequest == null || specialRequest.isBlank()) ? "NA" : specialRequest;

        // 14-column legacy format (no cancellation reason)
        String row = String.join("|",
                newId,
                nz(customerId),
                nz(customerIc),
                nz(customerPhone),
                nz(customerEmail),
                date.format(DATE_FMT),
                time.format(TIME_FMT),
                nz(purposeOfVisit),
                nz(preferredDoctorName),
                nz(doctorId),
                nz(paymentMethod),
                nz(paymentArrangement),
                sr,
                status,
                "NA"
        );

        try {
            Files.createDirectories(file.getParent());
            boolean addNewline = Files.exists(file) && Files.size(file) > 0;
            try (BufferedWriter w = Files.newBufferedWriter(file, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                if (addNewline) w.newLine();
                w.write(row);
            }
            return newId;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String nz(String s) { return s == null ? "" : s; }

    /* -------- Load for your 9-col table (handles 14 or 15 cols) -------- */
    public void loadAppointmentsNEW(DefaultTableModel model) {
        model.setRowCount(0);
        int added = 0;

        try {
            if (!Files.exists(file)) return;

            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                if (line.isBlank()) continue;

                String[] p = line.split("\\|", -1);  // see fix #1
                if (p.length < 14) continue;         // need at least legacy layout

                String apptId  = p[0].trim();
                String custId  = p[1].trim();
                String date    = p[5].trim();
                String time    = p[6].trim();
                String purpose = p[7].trim();
                String doctor  = p[8].trim();

                String special = p[12].trim();
                String status  = p[13].trim();
                String cancel  = (p.length >= 15) ? p[14].trim() : ""; // new col optional

                model.addRow(new Object[]{ apptId, custId, date, time, purpose, doctor, special, status, cancel });
                added++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Appointments loaded: " + added);
    }

    /* -------- Filter by doctorId (uses column 9) -------- */
    public void loadAppointmentsForDoctor(DefaultTableModel model, String doctorKey) {
        model.setRowCount(0);
        String key = doctorKey == null ? "" : doctorKey.trim();

        try (java.io.BufferedReader br = java.nio.file.Files.newBufferedReader(
                new File("data/appointments.txt").toPath(),
                java.nio.charset.StandardCharsets.UTF_8)) {

            String ln;
            while ((ln = br.readLine()) != null) {
                if (ln.isBlank()) continue;
                String[] f = ln.split("\\|", -1); // keep empty trailing cols!
                if (f.length < 14) continue;

                String docName = f[8].trim();  // "Dr. Lee"
                String docId   = f[9].trim();  // "D0001"

                // match by id OR by name (more forgiving)
                if (!key.equalsIgnoreCase(docId) && !key.equalsIgnoreCase(docName)) continue;

                String id       = f[0].trim();
                String customer = f[1].trim();
                String date     = f[5].trim();
                String time     = f[6].trim();
                String reason   = f[7].trim();
                String status   = f[13].trim();

                model.addRow(new Object[]{
                    id, customer, docName, date + " " + time, reason, status
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null,
                "Failed to load appointments:\n" + ex.getMessage(),
                "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        System.out.println("rows loaded = " + model.getRowCount());
    }




    
}

