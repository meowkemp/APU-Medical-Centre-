package Classes;

import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class CustHistory implements History {
    private final String appointmentFile = "data/appointments.txt";
    private final String chargeFile      = "data/charges.txt";
    private final String commentFile     = "data/comments.txt";
    private final String feedbackFile    = "data/feedbacks.txt";

    @Override
    public DefaultTableModel viewAppointmentHistory(String[] columnNames, String currentUserId) {
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try (BufferedReader reader = new BufferedReader(new FileReader(appointmentFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String raw = line.trim();
                if (raw.isEmpty() || raw.startsWith("#")) continue;

                String[] data = raw.split("\\|", -1);
                if (data.length >= 15) {
                    String customerId = data[1].trim();
                    String status     = data[13].trim(); // COMPLETED, etc.

                    if (customerId.equals(currentUserId) && status.equalsIgnoreCase("COMPLETED")) {
                        model.addRow(new Object[] {
                            data[0].trim(), // Appointment ID
                            data[1].trim(), // Customer ID
                            data[5].trim(), // Date
                            data[6].trim(), // Time
                            data[7].trim(), // Purpose
                            data[8].trim(), // Doctor (name)
                            data[12].trim(),// Special requirements
                            data[13].trim(),// Status
                            data[14].trim() // Cancellation reason
                        });
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                null,
                "Oops! Something went wrong while accessing the appointments file.\nPlease make sure the file exists",
                "File Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
        return model;
    }

    @Override
    public DefaultTableModel viewChargeHistory(String[] columnNames, String currentUserId) {
        // charges.txt: CHID|CUSTID|APPTID|AMOUNT|DESC|PAID|DOCID
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try (BufferedReader reader = new BufferedReader(new FileReader(chargeFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String raw = line.trim();
                if (raw.isEmpty() || raw.startsWith("#")) continue;

                String[] data = raw.split("\\|", -1);
                if (data.length >= 7) {
                    String customerId = data[1].trim();
                    if (customerId.equals(currentUserId)) {
                        model.addRow(new Object[] {
                            data[0].trim(), // Charge ID
                            data[1].trim(), // Customer ID
                            data[2].trim(), // Appointment ID
                            data[3].trim(), // Amount
                            data[4].trim(), // Description
                            data[5].trim(), // Payment Status
                            data[6].trim()  // Doctor ID
                        });
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                null,
                "Oops! Something went wrong while accessing the charges file.\nPlease make sure the file exists",
                "File Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
        return model;
    }

    @Override
    public DefaultTableModel viewCommentHistory(String[] columnNames, String currentUserId) {
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try (BufferedReader reader = new BufferedReader(new FileReader(commentFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String raw = line.trim();
                if (raw.isEmpty() || raw.startsWith("#")) continue;

                String[] data = raw.split("\\|", -1);
                if (data.length >= 6) {
                    String customerId = data[1].trim();
                    if (!customerId.equals(currentUserId)) continue;

                    String timeCreated; // merged date + time
                    String commentText;

                    if (data.length >= 7) {
                        // id, cust, toUser, appt, date, time, text
                        timeCreated = data[4].trim() + " " + data[5].trim();
                        commentText = data[6].trim();
                    } else {
                        // id, cust, toUser, appt, datetime, text
                        timeCreated = data[4].trim();
                        commentText = data[5].trim();
                    }

                    model.addRow(new Object[] {
                        data[0].trim(), // Comment ID
                        data[1].trim(), // Customer ID
                        data[2].trim(), // Doctor/Staff ID
                        data[3].trim(), // Appointment ID
                        timeCreated,    // yyyy-MM-dd HH:mm
                        commentText     // Comment
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                null,
                "Oops! Something went wrong while accessing the comments file.\nPlease make sure the file exists",
                "File Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
        return model;
    }

    @Override
    public DefaultTableModel viewFeedbackHistory(String[] columnNames, String currentUserId) {
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try (BufferedReader reader = new BufferedReader(new FileReader(feedbackFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String raw = line.trim();
                if (raw.isEmpty() || raw.startsWith("#")) continue;

                String[] data = raw.split("\\|", -1);
                if (data.length >= 7) {
                    String customerId = data[2].trim();
                    if (!customerId.equals(currentUserId)) continue;

                    String timeCreated = data[4].trim() + " " + data[5].trim(); // merge date + time

                    // 6 values to match your 6 headers
                    model.addRow(new Object[] {
                        data[0].trim(), // Feedback ID
                        data[1].trim(), // Doctor ID
                        data[2].trim(), // Customer ID
                        data[3].trim(), // Appointment ID
                        timeCreated,    // Date&Time
                        data[6].trim()  // Notes
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                null,
                "Oops! Something went wrong while accessing the feedbacks file.\nPlease make sure the file exists",
                "File Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
        return model;
    }
}
