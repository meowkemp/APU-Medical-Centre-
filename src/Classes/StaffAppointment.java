package Classes;

import java.time.LocalDate;
import java.time.LocalTime;

public class StaffAppointment extends Appointment {

    private final String staffId;

    public StaffAppointment(
            String appointmentId,
            String customerId,
            String staffId,
            String customerIc,
            String customerName,
            String customerPhone,
            String customerEmail,
            LocalDate date,
            LocalTime time,
            String purposeOfVisit,
            String preferredDoctor,
            String doctorId,
            String paymentMethod,
            String paymentArrangement,
            String specialRequest
    ) {
        super(
            appointmentId,
            customerId,
            customerIc,
            customerName,
            customerPhone,
            customerEmail,
            date,
            time,
            purposeOfVisit,
            preferredDoctor,
            doctorId,
            paymentMethod,
            paymentArrangement,
            specialRequest,
            AppointmentStatus.PENDING,
            null
        );
        this.staffId = staffId;
    }

    public String getStaffId() { return staffId; }

    @Override
    public boolean bookAppointment() {
        if (getCustomerId() == null || getCustomerId().isBlank()) return false;
        if (staffId == null || staffId.isBlank()) return false;
        if (getDate() == null || getTime() == null) return false;
        if (getPurposeOfVisit() == null || getPurposeOfVisit().isBlank()) return false;

        // auto status
        if (getDoctorId() != null && !getDoctorId().isBlank()) {
            setAppointmentStatus(AppointmentStatus.ASSIGNED);
        } else {
            setAppointmentStatus(AppointmentStatus.PENDING);
        }
        return true;
    }
}
