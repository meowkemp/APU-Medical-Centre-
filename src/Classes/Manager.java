
package Classes;

public class Manager extends Users {

    public Manager() { }

    // matches how UserManager constructs it:
    public Manager(String id, String name, String email, String phone) {
        super(id, name, email, phone, Role.MANAGER);
    }

    @Override
    public void updateProfile(ProfileUpdate p) {
        if (p == null) return;
        if (p.name != null)  setName(p.name);
        if (p.phone != null) setPhone(p.phone);
    }
    private final String ManagerData = "src/Data/Managers.txt";
    
     @Override
    public String getDisplayLabel() {
        return getName() + " (Manager)";
    }
}
