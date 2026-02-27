package Classes;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Domain model for Appointment + static helpers to load table data
 * (merged with Manager/Appointment's file-reading method).
 */
public abstract class Appointment {
    
    private static final String DEFAULT_DATA_PATH = "data/appointments.txt";
    private static final String DEFAULT_REGEX_DELIM = "\\|";

    private String appointmentId;
    private String customerId;
    private String customerIc;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private LocalDate date;
    private LocalTime time;
    private String purposeOfVisit;
    private String preferredDoctor;
    private String doctorId;
    private String paymentMethod;
    private String paymentArrangement;
    private String specialRequest;
    private AppointmentStatus appointmentStatus;
    private String cancellationReason;

    public Appointment(
            String appointmentId,
            String customerId,
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
            String specialRequest,
            AppointmentStatus appointmentStatus,
            String cancellationReason
    ) {
        this.appointmentId      = appointmentId;
        this.customerId         = customerId;
        this.customerIc         = customerIc;
        this.customerName       = customerName;
        this.customerPhone      = customerPhone;
        this.customerEmail      = customerEmail;
        this.date               = date;
        this.time               = time;
        this.purposeOfVisit     = purposeOfVisit;
        this.preferredDoctor    = preferredDoctor;
        this.doctorId           = doctorId;
        this.paymentMethod      = paymentMethod;
        this.paymentArrangement = paymentArrangement;
        this.specialRequest     = specialRequest;
        this.appointmentStatus  = appointmentStatus;
        this.cancellationReason = cancellationReason;
    }

    public Appointment() {
        // default
    }

    // ===== getters =====
    public String getAppointmentId() { return appointmentId; }
    public String getCustomerId() { return customerId; }
    public String getCustomerIc() { return customerIc; }
    public String getCustomerName() { return customerName; }
    public String getCustomerPhone() { return customerPhone; }
    public String getCustomerEmail() { return customerEmail; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public String getPurposeOfVisit() { return purposeOfVisit; }
    public String getPreferredDoctor() { return preferredDoctor; }
    public String getDoctorId() { return doctorId; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getPaymentArrangement() { return paymentArrangement; }
    public String getSpecialRequest() { return specialRequest; }
    public AppointmentStatus getAppointmentStatus() { return appointmentStatus; }
    public String getCancellationReason() { return cancellationReason; }

    // ===== setters =====
    public void setAppointmentId(String v) { this.appointmentId = v; }
    public void setCustomerId(String v) { this.customerId = v; }
    public void setCustomerIc(String v) { this.customerIc = v; }
    public void setCustomerName(String v) { this.customerName = v; }
    public void setCustomerPhone(String v) { this.customerPhone = v; }
    public void setCustomerEmail(String v) { this.customerEmail = v; }
    public void setDate(LocalDate v) { this.date = v; }
    public void setTime(LocalTime v) { this.time = v; }
    public void setPurposeOfVisit(String v) { this.purposeOfVisit = v; }
    public void setPreferredDoctor(String v) { this.preferredDoctor = v; }
    public void setDoctorId(String v) { this.doctorId = v; }
    public void setPaymentMethod(String v) { this.paymentMethod = v; }
    public void setPaymentArrangement(String v) { this.paymentArrangement = v; }
    public void setSpecialRequest(String v) { this.specialRequest = v; }
    public void setAppointmentStatus(AppointmentStatus v) { this.appointmentStatus = v; }
    public void setCancellationReason(String v) { this.cancellationReason = v; }

    // Role-specific behavior (left abstract as in your original)
    public abstract boolean bookAppointment();

    // =========================
    // MERGED STATIC UTILITIES
    // =========================

    public static DefaultTableModel viewAllAppointmentDetails(String[] columnNames) {
        return viewAllAppointmentDetails(columnNames, new File(DEFAULT_DATA_PATH), DEFAULT_REGEX_DELIM);
    }

    public static DefaultTableModel viewAllAppointmentDetails(String[] columnNames, File file, String regexDelim) {
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(regexDelim);

                // Expecting at least 13 fields based on the Manager mapping
                if (data.length >= 13) {
                    Object[] row = new Object[] {
                        data[0],   // Customer ID
                        data[1],   // Customer Name
                        data[2],   // Customer Phone
                        data[3],   // Email
                        data[4],   // Customer IC
                        data[5],   // Date
                        data[6],   // Time
                        data[7],   // Purpose of Visit
                        data[8],   // Doctor ID
                        data[9],   // Doctor Name
                        data[10],  // Payment Method
                        data[11],  // Cancellation Reason
                        data[12],  // Appointment Status
                    };
                    model.addRow(row);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                null,
                "Oops! Something went wrong while accessing the Appointment file.\n" +
                "Please make sure the file exists.",
                "File Error",
                JOptionPane.ERROR_MESSAGE
            );
        }

        return model;
    }
}
