package Classes;

public interface Billable {
    Charge createCharge(Appointment appt, double amount, String description);
    
}
