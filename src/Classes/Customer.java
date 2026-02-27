package Classes;

public class Customer extends Users {

    private String customerIc;
    private String address;
    private boolean active = true;

    public Customer() {
        super();
        setRole(Role.CUSTOMER);
    }

    public Customer(String id, String name, String email, String phone,
                    String ic, String password, String address) {
        super(id, name, email, phone, Role.CUSTOMER);
        setPassword(password);
        this.customerIc = ic;
        this.address = address;
    }

    public String getCustomerIc() { return customerIc; }
    public void   setCustomerIc(String ic) { this.customerIc = ic; }

    public String getAddress() { return address; }
    public void   setAddress(String address) { this.address = address; }

    public boolean isActive() { return active; }
    public void    setActive(boolean active) { this.active = active; }

    @Override
    public void updateProfile(ProfileUpdate p) {
        if (p == null) return;
        if (!isBlank(p.name))     setName(p.name);
        if (!isBlank(p.phone))    setPhone(p.phone);
        if (!isBlank(p.email))    setEmail(p.email);
        if (!isBlank(p.password)) setPassword(p.password);
        if (!isBlank(p.ic))       setCustomerIc(p.ic);
        if (!isBlank(p.address))  setAddress(p.address);
    }

   
}
