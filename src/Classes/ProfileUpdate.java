package Classes;

public class ProfileUpdate {
    public String name;
    public String phone;
    public String email;
    public String password;

    // role-specific:
    public String address;     // Customer
    public String ic;          // Customer
    public String speciality;  // Doctor
    public String roomNo;      // Doctor

    public ProfileUpdate() {}

    public static ProfileUpdate nameOnly(String name) {
        ProfileUpdate p = new ProfileUpdate();
        p.name = name;
        return p;
    }
    public static ProfileUpdate namePhone(String name, String phone) {
        ProfileUpdate p = new ProfileUpdate();
        p.name = name; p.phone = phone;
        return p;
    }
}
