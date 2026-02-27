package Classes;

public abstract class Users {
    private String id, name, email, password, phone;
    private Role role;

    public Users() {}
    public Users(String id, String name, String email, String phone, Role role) {
        this.id=id; this.name=name; this.email=email; this.phone=phone; this.role=role;
    }

    // getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPhone() { return phone; }
    public Role getRole() { return role; }
    public String getDisplayLabel() { return name; }

    // setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setRole(Role role) { this.role = role; }

    // ===== OVERRIDING
    
    public abstract void updateProfile(ProfileUpdate p);

    // ===== OVERLOADING
    public void updateProfile(String newName) {
        if (isBlank(newName)) return;
        updateProfile(ProfileUpdate.nameOnly(newName));
    }
    public void updateProfile(String newName, String newPhone) {
        if (isBlank(newName) && isBlank(newPhone)) return;
        updateProfile(ProfileUpdate.namePhone(newName, newPhone));
    }

    
    protected static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
