package Classes;

import java.io.*;
import java.util.*;

public class UserManager {
    private final List<Users> users = new ArrayList<>();
    private Users currentUser;

    // file location for users.txt
    private final File usersFile;

    public UserManager() {
        this.usersFile = resolveUsersPath();
        if (this.usersFile == null) {
            System.err.println("users.txt not found (tried src/Data/users.txt and data/users.txt)");
        }
        loadUsers();
    }

    // =========================
    // AUTH (add back login, logout)
    // =========================
 
    public Users login(String email, String password) {
        if (email == null || password == null) return null;
        String emailLower = email.trim().toLowerCase(Locale.ROOT);

        for (Users u : users) {
            if (u.getEmail() != null && u.getEmail().trim().toLowerCase(Locale.ROOT).equals(emailLower)) {
                String pw = u.getPassword();
                if (pw != null && pw.equals(password)) {
                    currentUser = u;
                    return u;
                } else {
                    return null; // email found, wrong password
                }
            }
        }
        return null; // email not found
    }

    public void logout() { currentUser = null; }

    public Users getCurrentUser() { return currentUser; }
    public void setCurrentUser(Users u) { currentUser = u; } // if you already have a login flow elsewhere

    // =========================
    // READ ALL (BufferedReader)
    // =========================
    public final void loadUsers() {
        users.clear();
        if (usersFile == null || !usersFile.exists()) return;

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(usersFile));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] f = line.split("\\|", -1); // keep empty columns
                if (f.length < 10) continue;        // we expect 10 columns

                String id    = f[0];
                String role  = f[1];
                String name  = f[2];
                String email = f[3];
                String pass  = f[4];
                String phone = f[5];
                String spec  = f[6];
                String room  = f[7];
                String ic    = f[8];
                String addr  = f[9];

                // build the right object
                if ("CUSTOMER".equalsIgnoreCase(role)) {
                    users.add(new Customer(id, name, email, phone, ic, pass, addr));
                } else if ("DOCTOR".equalsIgnoreCase(role)) {
                    Doctor d = new Doctor(id, name, email, phone, spec, room);
                    d.setPassword(pass);
                    users.add(d);
                } else if ("STAFF".equalsIgnoreCase(role)) {
                    Staff s = new Staff(id, name, email, phone);
                    s.setPassword(pass);
                    users.add(s);
                }
                else if ("MANAGER".equalsIgnoreCase(role)) {
                    Manager m = new Manager(id, name, email, phone);
                    m.setPassword(pass);
                    users.add(m);
                }
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) try { br.close(); } catch (IOException ignored) {}
        }
    }

    // =========================
    // SAVE ALL (BufferedWriter)
    // =========================
    public void saveUsers() {
        if (usersFile == null) return;

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(usersFile));
            for (Users u : users) {
                String[] col = new String[10];
                Arrays.fill(col, "");

                col[0] = safe(u.getId());
                col[1] = u.getRole().name();
                col[2] = safe(u.getName());
                col[3] = safe(u.getEmail());
                col[4] = safe(u.getPassword());
                col[5] = safe(u.getPhone());

                if (u instanceof Doctor) {
                    Doctor d = (Doctor) u;
                    col[6] = safe(d.getSpeciality());
                    col[7] = safe(d.getRoomNo());
                } else if (u instanceof Customer) {
                    Customer c = (Customer) u;
                    col[8] = safe(c.getCustomerIc());
                    col[9] = safe(c.getAddress());
                }
                // Staff leaves 6..9 empty

                bw.write(String.join("|", col));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) try { bw.close(); } catch (IOException ignored) {}
        }
    }

    private static String safe(String s) {
        return (s == null) ? "" : s.replace("\n", " ").replace("\r", " ");
    }

    // =========================
    // EDIT PROFILE (polymorphic)
    // =========================
    /** For your CustomerEditProfileGUI "Save" button. */
    public boolean editCurrentCustomer(String email, String password, String phone, String address) {
        if (!(currentUser instanceof Customer)) return false;
        ProfileUpdate p = new ProfileUpdate();
        p.email = email;
        p.password = password;
        p.phone = phone;
        p.address = address;

        currentUser.updateProfile(p); // OVERRIDING happens here (Customer impl)
        saveUsers();                  // persist to users.txt
        return true;
    }

    /** Generic by ID: works for doctor/staff/customer screens too. */
    public boolean editProfileById(String userId, ProfileUpdate p) {
        Users u = findById(userId);
        if (u == null) return false;
        u.updateProfile(p); // calls the correct override
        saveUsers();
        return true;
    }

    // =========================
    // FINDERS / LISTS
    // =========================
    public Users findById(String id) {
        if (id == null) return null;
        for (Users u : users) if (id.equals(u.getId())) return u;
        return null;
    }

    public List<Users> getAllUsers() {
        return Collections.unmodifiableList(users);
    }

    public List<Doctor> getDoctors() {
        List<Doctor> out = new ArrayList<Doctor>();
        for (Users u : users) if (u.getRole() == Role.DOCTOR) out.add((Doctor) u);
        return Collections.unmodifiableList(out);
    }

    public Optional<Doctor> findDoctorByName(String name) {
        if (name == null) return Optional.empty();
        for (Users u : users) {
            if (u.getRole() == Role.DOCTOR && name.equalsIgnoreCase(u.getName())) {
                return Optional.of((Doctor) u);
            }
        }
        return Optional.empty();
    }

    /** Find by label "Name (Speciality)" produced by Doctor.getDisplayLabel(). */
    public Optional<Doctor> findDoctorByDisplayLabel(String label) {
        if (label == null) return Optional.empty();
        int idx = label.indexOf(" (");
        String nameOnly = (idx > 0) ? label.substring(0, idx) : label;
        return findDoctorByName(nameOnly.trim());
    }

    // =========================
    // PATH RESOLUTION
    // =========================
    private File resolveUsersPath() {
        File p1 = new File("src/Data/users.txt");
        if (p1.exists()) return p1;
        File p2 = new File("data/users.txt");
        if (p2.exists()) return p2;
        return null;
    }
}
