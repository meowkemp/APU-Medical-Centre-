package Classes;

import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Doctors {
    private static final Path USERS_PATH = Paths.get("data/users.txt");
    private static final Pattern DOC_ID_RX = Pattern.compile("^D(\\d{4})$"); // D0001

    public DefaultTableModel viewAllDoctorDetails(String[] columns) {
        DefaultTableModel m = new DefaultTableModel();
        m.setColumnIdentifiers(columns);

        if (!Files.exists(USERS_PATH)) return m;

        try {
            for (String line : Files.readAllLines(USERS_PATH, StandardCharsets.UTF_8)) {
                if (line == null || line.isBlank()) continue;
                String[] a = line.split("\\|", -1);
                if (a.length >= 2 && "DOCTOR".equalsIgnoreCase(a[1].trim())) {
                    String id    = safe(a, 0);
                    String name  = safe(a, 2);
                    String email = safe(a, 3);
                    String phone = safe(a, 5);
                    String spec  = safe(a, 6);
                    String room  = safe(a, 7);

                    // Pack in the order expected by UI
                    if (columns.length >= 7) {
                        m.addRow(new Object[]{ id, name, email, "DOCTOR", phone, spec, room });
                    } else if (columns.length == 6) {
                        m.addRow(new Object[]{ id, name, email, "DOCTOR", phone, spec });
                    } else if (columns.length == 5) {
                        m.addRow(new Object[]{ id, name, email, "DOCTOR", phone });
                    } else {
                        m.addRow(new Object[]{ id, name, email, "DOCTOR" });
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return m;
    }

    // ----- ID generation ------------------------------------------------------
    /** Peek the next doctor ID (D0001, D0002, …). */
    public String peekNextDoctorId() {
        int max = 0;
        if (!Files.exists(USERS_PATH)) return formatId(1);

        try {
            for (String line : Files.readAllLines(USERS_PATH, StandardCharsets.UTF_8)) {
                if (line == null || line.isBlank()) continue;
                String[] a = line.split("\\|", -1);
                if (a.length >= 2 && "DOCTOR".equalsIgnoreCase(a[1].trim())) {
                    Matcher m = DOC_ID_RX.matcher(safe(a, 0));
                    if (m.matches()) {
                        int n = Integer.parseInt(m.group(1));
                        if (n > max) max = n;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return formatId(max + 1);
    }

    private String formatId(int n) { return String.format("D%04d", n); }

    // ----- Create -------------------------------------------------------------
    /** Add a doctor with auto ID; returns new ID or null on failure. */
    public String addDoctorAutoReturnId(String name, String email, String phone, String speciality, String room) {
        String id = peekNextDoctorId();
        boolean ok = appendDoctorRow(id, name, email, phone, speciality, room);
        return ok ? id : null;
    }

    private boolean appendDoctorRow(String id, String name, String email, String phone, String speciality, String room) {
        final String password = "password123"; 

        String row = String.join("|",
                nz(id), "DOCTOR",
                nz(name), nz(email), password,
                nz(phone), nz(speciality), nz(room), "", ""
        );

        try {
            Files.createDirectories(USERS_PATH.getParent());
            boolean needNewline = Files.exists(USERS_PATH) && Files.size(USERS_PATH) > 0;

            try (BufferedWriter w = Files.newBufferedWriter(
                    USERS_PATH, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                if (needNewline) w.newLine();
                w.write(row);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ----- Update -------------------------------------------------------------
    public boolean updateDoctor(String id, String name, String email, String phone, String speciality, String room) {
        if (!Files.exists(USERS_PATH)) return false;

        try {
            List<String> all = new ArrayList<>(Files.readAllLines(USERS_PATH, StandardCharsets.UTF_8));
            boolean changed = false;

            for (int i = 0; i < all.size(); i++) {
                String[] a = all.get(i).split("\\|", -1);
                if (a.length >= 2 && "DOCTOR".equalsIgnoreCase(safe(a, 1)) && safe(a, 0).equals(id)) {
                    // Ensure array has at least 10 cells
                    a = grow(a, 10);

                    String pw  = a[4];
                    String c8  = a[8];
                    String c9  = a[9];

                    String newLine = String.join("|",
                            nz(id), "DOCTOR",
                            nz(name), nz(email),
                            nz(pw),
                            nz(phone),
                            nz(speciality),
                            nz(room),
                            nz(c8), nz(c9)
                    );
                    all.set(i, newLine);
                    changed = true;
                    break;
                }
            }

            if (changed) {
                Files.write(USERS_PATH, all, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
            }
            return changed;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ----- Delete -------------------------------------------------------------
    /** Delete a doctor row by ID. */
    public boolean deleteDoctorById(String id) {
        if (!Files.exists(USERS_PATH)) return false;

        try {
            List<String> all = new ArrayList<>(Files.readAllLines(USERS_PATH, StandardCharsets.UTF_8));
            int before = all.size();

            all.removeIf(line -> {
                String[] a = line.split("\\|", -1);
                return a.length >= 2 && "DOCTOR".equalsIgnoreCase(safe(a, 1)) && safe(a, 0).equals(id);
            });

            if (all.size() != before) {
                Files.write(USERS_PATH, all, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ----- UI field fill-ins -------------------------------------
    public String getPhoneByDoctorId(String id)       { return getCellById(id, 5); }
    public String getSpecialityByDoctorId(String id)  { return getCellById(id, 6); }
    public String getRoomByDoctorId(String id)        { return getCellById(id, 7); } // NEW

    private String getCellById(String id, int idx) {
        if (!Files.exists(USERS_PATH)) return "";
        try {
            for (String line : Files.readAllLines(USERS_PATH, StandardCharsets.UTF_8)) {
                if (line == null || line.isBlank()) continue;
                String[] a = line.split("\\|", -1);
                if (a.length > idx && "DOCTOR".equalsIgnoreCase(safe(a, 1)) && safe(a, 0).equals(id)) {
                    return a[idx].trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /** return empty string as the way it is without showing any missing value error*/
    private static String safe(String[] a, int i) { return (i < a.length && a[i] != null) ? a[i].trim() : ""; }
    private static String nz(String s) { return s == null ? "" : s; } //If string is null, it will return null to prevent NullPointerException

    /** This method is used to safely leave the user text field empty. Can avoid array error*/
    private static String[] grow(String[] a, int targetLen) {
        if (a.length >= targetLen) return a;
        String[] b = new String[targetLen];
        System.arraycopy(a, 0, b, 0, a.length);
        for (int i = a.length; i < targetLen; i++) b[i] = "";
        return b;
    }
}
