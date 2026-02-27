
package Classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class CustAppointmentManager extends Appointment{
    private final String appointmentFile = "data/appointments.txt";
    private final String usersFile = "data/users.txt";
    
    private static final java.util.regex.Pattern CUST_ID_RX =
        java.util.regex.Pattern.compile("^C(\\d{4})$");
    
    public CustAppointmentManager(String appointmentId, String customerId, String customerIc, String customerName, String customerPhone, String customerEmail, LocalDate date, LocalTime time, String purposeOfVisit, String preferredDoctor, String doctorId, String paymentMethod, String paymentArrangement, String specialRequest, AppointmentStatus appointmentStatus, String cancellationReason){
        super(appointmentId, customerId, customerIc, customerName, customerPhone, customerEmail, date, time, purposeOfVisit, preferredDoctor, doctorId, paymentMethod, paymentArrangement, specialRequest, appointmentStatus, cancellationReason);    
    }
    
    public CustAppointmentManager(){
        super(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
    
    //Check if any fields empty
    private boolean validateAppointment(){
        return !(getCustomerId().isEmpty() || getCustomerIc().isEmpty() || getCustomerPhone().isEmpty() || getCustomerEmail().isEmpty()
                || getDate() == null || getTime() == null || getPurposeOfVisit().isEmpty() || getPreferredDoctor().isEmpty()
                || getPaymentMethod().isEmpty() || getPaymentArrangement().isEmpty());
    }
    
    @Override
    public boolean bookAppointment(){
        try{
            if(!validateAppointment()){
                return false;
            }

            String newId = generateNewAppointmentId();
            setAppointmentId(newId);


            String record = buildRecord();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(appointmentFile, true))) {
                writer.write(record);
                writer.newLine();
            }
            return true;
        } catch(Exception e){
            JOptionPane.showMessageDialog(null,
                "Oops! Something went wrong while accessing the appointments file.\nPlease make sure the file exists",
                "File Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    //Build format for text file
    private String buildRecord(){
        return getAppointmentId() + "|" + getCustomerId() + "|" + getCustomerIc() + "|" + getCustomerPhone() + "|" + getCustomerEmail() + "|"
                + getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "|" + getTime().toString() + "|" +
                getPurposeOfVisit() + "|" + getPreferredDoctor() + "|" + (getDoctorId() != null ? getDoctorId() : "") + "|" +
                getPaymentMethod() + "|" + getPaymentArrangement() + "|" + (getSpecialRequest() == null || getSpecialRequest().isEmpty() ? "NA" : getSpecialRequest()) + "|"
                + getAppointmentStatus() + "|" + ((getCancellationReason() == null || getCancellationReason().isEmpty()) ? "NA" : getCancellationReason());
    }
    
    //Generate Appointment ID
    private String generateNewAppointmentId() throws IOException{
        int maxId = 0;
        File file = new File(appointmentFile);
        if(!file.exists()){
            return "A0001";
        }
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split("\\|");
                if(parts.length > 0 && parts[0].startsWith("A")){
                    int id = Integer.parseInt(parts[0].substring(1));
                    if(id > maxId) maxId = id;
                }
            }
        }
        return String.format("A%04d", maxId + 1);
    }
    
    //Find doctor Id based on name
    public String getDoctorId(String doctorName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(usersFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                // Adjust indexes based on your Users.txt structure:
                // Example: parts[0] = doctorId, parts[1] = role, parts[2] = doctorName, parts[3] = specialism
                if (parts.length >= 3 && parts[1].equalsIgnoreCase("DOCTOR")) {
                    String fileDoctorName = parts[2].trim();
                    if (fileDoctorName.equalsIgnoreCase(doctorName.trim())) {
                        return parts[0]; // doctorId
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Oops! Something went wrong while accessing the comments file.\n" +
                        "Please make sure the file exists", "File Error", JOptionPane.ERROR_MESSAGE);
        }
        return null; // not found
        }

        //Load Appointments
        public DefaultTableModel checkAppointment(String[] columnNames, String currentUserId){
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try (BufferedReader reader = new BufferedReader(new FileReader(appointmentFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|", -1);
                if (data.length < 14) continue;

                String customerId = data[1];
                String status = data[13];

                if (customerId.equals(currentUserId) &&
                   (status.equalsIgnoreCase("PENDING")
                 || status.equalsIgnoreCase("CANCELLED")
                 || status.equalsIgnoreCase("SCHEDULED")
                 || status.equalsIgnoreCase("ASSIGNED"))) {

                    String cancel = (data.length >= 15) ? data[14] : "NA";

                    model.addRow(new Object[]{
                        data[0], // appt id
                        data[1], // cust id
                        data[5], // date
                        data[6], // time
                        data[7], // purpose
                        data[8], // doctor
                        data[12],// special
                        data[13],// status
                        cancel   // cancellation reason
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Oops! Something went wrong while accessing the appointments file.\nPlease make sure the file exists",
                "File Error", JOptionPane.ERROR_MESSAGE);
        }

        return model;
    }
    /** Compute next customer ID by scanning users.txt for CUSTOMER rows. */
    public String nextCustomerId() {
        int max = 0;
        File f = new File(usersFile);
        if (!f.exists()) return formatCustomerId(1);
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = line.split("\\|", -1);   // id|role|name|email|password|phone|||ic|address
                if (p.length >= 2 && "CUSTOMER".equalsIgnoreCase(p[1].trim())) {
                    String id = p[0].trim();
                    java.util.regex.Matcher m = CUST_ID_RX.matcher(id);
                    if (m.matches()) {
                        int n = Integer.parseInt(m.group(1));
                        if (n > max) max = n;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return formatCustomerId(max + 1);
    }

    /** Non-mutating peek for UI display (same as nextCustomerId). */
    public String peekNextCustomerId() { return nextCustomerId(); }

    private String formatCustomerId(int n) { return String.format("C%04d", n); }
    
}
