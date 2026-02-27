package Classes;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.FileReader;

public class DoctorHistory implements History {
    

    private final String apptFile   = "data/appointments.txt";
    private final String chargeFile = "data/charges.txt";
    private final String commentFile= "data/comments.txt";
    private final String fbFile     = "data/feedbacks.txt";

    private static boolean blank(String s){ return s==null || s.isBlank(); }

    @Override
    public DefaultTableModel viewAppointmentHistory(String[] cols, String doctorId) {
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        try (BufferedReader br = new BufferedReader(new FileReader(apptFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String raw = line.trim();
                if (raw.isEmpty() || raw.startsWith("#")) continue;
                String[] p = raw.split("\\|", -1);
                if (p.length < 14) continue;

                String docId   = p[9].trim();
                String status  = p[13].trim();  // PENDING/ASSIGNED/COMPLETED/CANCELLED
                if (!docId.equalsIgnoreCase(doctorId)) continue;

                m.addRow(new Object[] {
                    safe(p,0), safe(p,1), safe(p,5), safe(p,6),
                    safe(p,7), safe(p,8), safe(p,12), status, safe(p,14)
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading appointments.txt", "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return m;
    }

    @Override
    public DefaultTableModel viewChargeHistory(String[] cols, String doctorId) {
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        try (BufferedReader br = new BufferedReader(new FileReader(chargeFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String raw = line.trim();
                if (raw.isEmpty() || raw.startsWith("#")) continue;

                String[] p = raw.split("\\|", -1);
                String chId, apptId, amount, desc, paid, docId;

                if (p.length >= 7) {
                    chId   = safe(p,0);
                    apptId = safe(p,2);
                    amount = safe(p,3);
                    desc   = safe(p,4);
                    paid   = safe(p,5);
                    docId  = safe(p,6);
                } else if (p.length >= 6) {
                    chId   = safe(p,0);
                    apptId = safe(p,1);
                    amount = safe(p,2);
                    desc   = safe(p,3);
                    paid   = safe(p,4);
                    docId  = safe(p,5);
                } else {
                    continue; // malformed
                }

                if (!docId.equalsIgnoreCase(doctorId)) continue;
                String status = normalizePaid(paid); // "PAID"/"UNPAID"

                m.addRow(new Object[]{ chId, apptId, amount, desc, status, docId });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading charges.txt", "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return m;
    }
    private static String normalizePaid(String paid) {
        if (paid == null) return "";
        String s = paid.trim();
        if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("paid") || s.equalsIgnoreCase("yes")) return "PAID";
        if (s.equalsIgnoreCase("false") || s.equalsIgnoreCase("unpaid") || s.equalsIgnoreCase("no")) return "UNPAID";
        return s;
    }

    @Override
    public DefaultTableModel viewCommentHistory(String[] cols, String doctorId) {
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        try (BufferedReader br = new BufferedReader(new FileReader(commentFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String raw = line.trim();
                if (raw.isEmpty() || raw.startsWith("#")) continue;
                String[] p = raw.split("\\|", -1);
                if (p.length < 6) continue;

                String recipient = p[2].trim();
                if (!recipient.equalsIgnoreCase(doctorId)) continue;

                String dateTime, text;
                if (p.length >= 7) {            // new format: DATE | TIME
                    dateTime = safe(p,4) + " " + safe(p,5);
                    text     = safe(p,6);
                } else {                         // legacy merged timestamp
                    dateTime = safe(p,4);
                    text     = safe(p,5);
                }
                m.addRow(new Object[] {
                    safe(p,0), safe(p,1), recipient, safe(p,3), dateTime, text
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading comments.txt", "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return m;
    }

    @Override
    public DefaultTableModel viewFeedbackHistory(String[] cols, String doctorId) {
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        try (BufferedReader br = new BufferedReader(new FileReader(fbFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String raw = line.trim();
                if (raw.isEmpty() || raw.startsWith("#")) continue;

                String[] p = raw.split("\\|", -1);
                String fbId, docId, custId = "", apptId, dateTime, notes;

                if (p.length >= 7) {
                    // FBID|DOCID|CUSTID|APPTID|DATE|TIME|NOTES
                    fbId   = safe(p,0);
                    docId  = safe(p,1);
                    custId = safe(p,2);
                    apptId = safe(p,3);
                    dateTime = safe(p,4) + " " + safe(p,5);
                    notes  = safe(p,6);
                } else if (p.length >= 5) {
                    // FBID|DOCID|APPTID|YYYY-MM-DD HH:mm|NOTES
                    fbId   = safe(p,0);
                    docId  = safe(p,1);
                    apptId = safe(p,2);
                    dateTime = safe(p,3);
                    notes  = safe(p,4);
                } else {
                    continue; 
                }

                if (!docId.equalsIgnoreCase(doctorId)) continue;

                m.addRow(new Object[]{ fbId, docId, apptId, dateTime, notes });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading feedbacks.txt", "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return m;
    }

    private static String safe(String[] a, int i){ return (i>=0 && i<a.length && !blank(a[i])) ? a[i].trim() : ""; }
}
