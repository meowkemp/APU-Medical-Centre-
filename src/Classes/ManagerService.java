package Classes;

import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class ManagerService {

    private static final String USERS_FILE = "data/users.txt"; // adjust path
    private static final Pattern MGR_ID_RX = Pattern.compile("^M(\\d{4})$"); // e.g. M0001

    public DefaultTableModel viewAllManagerDetails(String[] columns) {
        DefaultTableModel m = new DefaultTableModel();
        m.setColumnIdentifiers(columns);
        try {
            Path pth = Paths.get(USERS_FILE);
            if (!Files.exists(pth)) return m;

            for (String line : Files.readAllLines(pth, StandardCharsets.UTF_8)) {
                if (line.isBlank()) continue;
                String[] p = line.split("\\|",-1);
                if (p.length >= 5 && "MANAGER".equalsIgnoreCase(p[1].trim())) {
                    String id    = p[0].trim();
                    String name  = p[2].trim();
                    String email = p[3].trim();
                    String phone = p.length > 5 ? p[5].trim() : "";   // <-- FIX: index 5, not 4
                    m.addRow(new Object[]{ id, name, email, "MANAGER", phone });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return m;
    }

    /** Compute next manager ID by checking the users.txt for MANAGER rows with IDs like M0001. */
    public String nextManagerId() {
        int max = 0;
        try {
            if (!Files.exists(Paths.get(USERS_FILE))) return formatId(1);
            for (String line : Files.readAllLines(Paths.get(USERS_FILE))) {
                if (line.isBlank()) continue;
                String[] p = line.split("\\|",-1);
                if (p.length >= 2 && "MANAGER".equalsIgnoreCase(p[1].trim())) {
                    String id = p[0].trim();
                    Matcher m = MGR_ID_RX.matcher(id);
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

    /** Peek display for UI. Same as nextManagerId(). */
    public String peekNextManagerId() { return nextManagerId(); }

    private String formatId(int n) { return String.format("M%04d", n); }



    public String addManagerAutoReturnId(String name, String email, String phone) {
        String id = nextManagerId();
        boolean ok = appendManagerRow(id, name, email, phone);
        return ok ? id : null;
    }

    
   
    private boolean appendManagerRow(String id, String name, String email, String phone) {
        
        String password = "password123";
        String row = String.join("|",
            id, "MANAGER", name, email, password, phone, "", "", "", "");
        try (BufferedWriter w = Files.newBufferedWriter(
                Paths.get(USERS_FILE), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            if (Files.size(Paths.get(USERS_FILE)) > 0) w.newLine();
            w.write(row);
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    public boolean updateManager(String id, String name, String email, String phone) {
        try {
            Path pth = Paths.get(USERS_FILE);
            if (!Files.exists(pth)) return false;

            List<String> all = Files.readAllLines(pth, StandardCharsets.UTF_8);
            boolean changed = false;

            for (int i = 0; i < all.size(); i++) {
                String[] a = all.get(i).split("\\|", -1);
                if (a.length >= 2 && "MANAGER".equalsIgnoreCase(a[1].trim()) && a[0].trim().equals(id)) {
                    // keep existing password and tail cells
                    String pw = a.length > 4 ? a[4] : "";
                    String c6 = a.length > 6 ? a[6] : "";
                    String c7 = a.length > 7 ? a[7] : "";
                    String c8 = a.length > 8 ? a[8] : "";
                    String c9 = a.length > 9 ? a[9] : "";

                    String newline = String.join("|",
                            id, "MANAGER",
                            nz(name), nz(email), nz(pw),
                            nz(phone), nz(c6), nz(c7), nz(c8), nz(c9));

                    all.set(i, newline);
                    changed = true;
                    break;
                }
            }
            if (changed) Files.write(pth, all, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
            return changed;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private static String nz(String s) { return s == null ? "" : s; }

    public static boolean deleteManager(String id) {
        try {
            List<String> all = Files.readAllLines(Paths.get(USERS_FILE));
            int before = all.size();
            all.removeIf(line -> {
                String[] p = line.split("\\|",-1);
                return p.length >= 2 && "MANAGER".equalsIgnoreCase(p[1].trim()) && p[0].trim().equals(id);
            });
            if (all.size() != before) {
                Files.write(Paths.get(USERS_FILE), all, StandardCharsets.UTF_8);
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace(); return false;
        }
    }
}
