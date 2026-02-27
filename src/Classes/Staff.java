
package Classes;

public class Staff extends Users {
    public Staff() { }
    public Staff(String id, String name, String email, String phone) {
        super(id, name, email, phone, Role.STAFF);
    }

    @Override
    public void updateProfile(ProfileUpdate p) {
        if (p == null) return;
        if (!isBlank(p.name))     setName(p.name);
        if (!isBlank(p.phone))    setPhone(p.phone);
        if (!isBlank(p.email))    setEmail(p.email);
        if (!isBlank(p.password)) setPassword(p.password);
    }


}
