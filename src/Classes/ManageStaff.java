package Classes;

import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.regex.Pattern;

public class ManageStaff {
    private static final String USERS_FILE = "data/users.txt";
    private static final Pattern STAFF_ID_RX = Pattern.compile("^S(\\d{4})$"); // e.g. S0001

    public DefaultTableModel viewAllStaffDetails(String[] columns) {
        DefaultTableModel m = new DefaultTableModel();
        m.setColumnIdentifiers(columns);
        try {
            Path p = Paths.get(USERS_FILE);
            if (!Files.exists(p)) return m;
            for (String line : Files.readAllLines(p, StandardCharsets.UTF_8)) {
                if (line.isBlank()) continue;
                String[] a = line.split("\\|", -1);
                if (a.length >= 5 && "STAFF".equalsIgnoreCase(a[1].trim())) {
                    String id    = a[0].trim();
                    String name  = a[2].trim();
                    String email = a[3].trim();
                    String phone = a.length > 5 ? a[5].trim() : "";
                    m.addRow(new Object[]{id, name, email, "STAFF",phone});
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return m;
    }

    /** Compute next staff id (S0001, S0002, …) by scanning users.txt */
    public String nextStaffId() {
        int max = 0;
        try {
            Path p = Paths.get(USERS_FILE);
            if (!Files.exists(p)) return formatId(1);
            for (String line : Files.readAllLines(p, StandardCharsets.UTF_8)) {
                if (line.isBlank()) continue;
                String[] a = line.split("\\|", -1);
                if (a.length >= 2 && "STAFF".equalsIgnoreCase(a[1].trim())) {
                    var m = STAFF_ID_RX.matcher(a[0].trim());
                    if (m.matches()) {
                        int n = Integer.parseInt(m.group(1));
                        if (n > max) max = n;
                    }
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return formatId(max + 1);
    }

    public String peekNextStaffId() { return nextStaffId(); }
    private String formatId(int n)  { return String.format("S%04d", n); }

    /** Add a staff row, auto-generate the ID and return it (null on failure). */
    public String addStaffAutoReturnId(String name, String email, String phone) {
        String id = nextStaffId();
        if (appendStaffRow(id, name, email, phone)) return id;
        return null;
    }

    private boolean appendStaffRow(String id, String name, String email, String phone) {
        String password = "password123"; // default
        String row = String.join("|",
            id, "STAFF", name, email, password,
            phone == null ? "" : phone,
            "", "", "", ""); // keep remaining columns blank
        try (BufferedWriter w = Files.newBufferedWriter(
                Paths.get(USERS_FILE), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            if (Files.size(Paths.get(USERS_FILE)) > 0) w.newLine();
            w.write(row);
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    /** Update staff line (keeps password and any extra columns intact). */
    public boolean updateStaff(String id, String name, String email, String phone) {
        try {
            Path path = Paths.get(USERS_FILE);
            var all = Files.exists(path)
                    ? Files.readAllLines(path, StandardCharsets.UTF_8)
                    : new java.util.ArrayList<String>();
            boolean changed = false;
            for (int i = 0; i < all.size(); i++) {
                String[] a = all.get(i).split("\\|", -1);
                if (a.length >= 5 && "STAFF".equalsIgnoreCase(a[1].trim()) && a[0].trim().equals(id)) {
                    String pw = a[4];
                    String c6 = a.length > 6 ? a[6] : "";
                    String c7 = a.length > 7 ? a[7] : "";
                    String c8 = a.length > 8 ? a[8] : "";
                    String c9 = a.length > 9 ? a[9] : "";
                    all.set(i, String.join("|",
                            id, "STAFF", name, email, pw,
                            phone == null ? "" : phone,
                            c6, c7, c8, c9));
                    changed = true;
                    break;
                }
            }
            if (changed) Files.write(path, all, StandardCharsets.UTF_8);
            return changed;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    /** Delete staff by id from users.txt */
    public boolean deleteStaffById(String id) {
        try {
            Path path = Paths.get(USERS_FILE);
            var all = Files.exists(path)
                    ? Files.readAllLines(path, StandardCharsets.UTF_8)
                    : new java.util.ArrayList<String>();
            int before = all.size();
            all.removeIf(line -> {
                String[] a = line.split("\\|", -1);
                return a.length >= 2 && "STAFF".equalsIgnoreCase(a[1].trim()) && a[0].trim().equals(id);
            });
            if (all.size() != before) {
                Files.write(path, all, StandardCharsets.UTF_8);
                return true;
            }
            return false;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    /** Helper to populate the phone field when a row is selected. */
    public String getPhoneByStaffId(String id) {
        try {
            Path p = Paths.get(USERS_FILE);
            if (!Files.exists(p)) return "";
            for (String line : Files.readAllLines(p, StandardCharsets.UTF_8)) {
                String[] a = line.split("\\|", -1);
                if (a.length >= 6 && "STAFF".equalsIgnoreCase(a[1].trim()) && a[0].trim().equals(id)) {
                    return a[5];
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return "";
    }
}
