package Staff;
import Classes.Staff;
import Classes.UserManager;
import Start.LoginGUI;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class Payment extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(Payment.class.getName());

    // ----- file paths -----
    private static final Path APPTS_FILE        = Paths.get("data/appointments.txt");
    private static final Path CHARGES_FILE      = Paths.get("data/charges.txt");
    private static final Path RECEIPTS_TXT      = Paths.get("data/receipts.txt");
    private static final Path RECEIPTS_PDF_DIR  = Paths.get("data/receipts");
    private final UserManager userManager;   // <—
    private final Staff loggedInStaff;  

    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    // table model for charges
    private DefaultTableModel chargesModel;

    // cache of appointment info
    private final Map<String, ApptInfo> apptMap = new HashMap<>();

    public Payment(UserManager userManager,Staff staff) {
        this.userManager = userManager;
        this.loggedInStaff = staff;
        initComponents();
        setLocationRelativeTo(null);
        postInit();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel11 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextPane7 = new javax.swing.JTextPane();
        jLabel12 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane3 = new javax.swing.JTextPane();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextPane8 = new javax.swing.JTextPane();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();

        jLabel11.setText("Date of Appoinment:");

        jScrollPane7.setViewportView(jTextPane7);

        jLabel12.setText("Time of Appoinment:");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "9.00am", "9.30am", "10.00am", "10.30am", "11.00am", "11.30am", "12.00pm", "12.30pm", "13.00pm", "13.30pm", "14.00pm", "14.30pm", "15.00pm", "15.30pm", "16.00pm", "16.30pm", "17.00pm", "17.30pm", "18.00pm", " " }));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jButton9.setText("Customer Appoinment");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("Manage Customer ");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton13.setText("Payment ");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setLabel("Log Out");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setLabel("Edit Profile");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                    .addComponent(jButton15, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(84, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(179, 179, 179)
                .addComponent(jButton9)
                .addGap(18, 18, 18)
                .addComponent(jButton13)
                .addGap(18, 18, 18)
                .addComponent(jButton15)
                .addContainerGap(216, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(139, 139, 139)
                    .addComponent(jButton10)
                    .addContainerGap(338, Short.MAX_VALUE)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(jButton14)
                    .addContainerGap(462, Short.MAX_VALUE)))
        );

        jButton9.getAccessibleContext().setAccessibleName("btncustomerapplication");
        jButton10.getAccessibleContext().setAccessibleName("btnmanagecustomer");
        jButton13.getAccessibleContext().setAccessibleName("btnpayment");
        jButton14.getAccessibleContext().setAccessibleName("btnlogout");
        jButton15.getAccessibleContext().setAccessibleName("btneditprofile");

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel3.setText("Customer ID:");

        jLabel4.setText("Appoinment ID:");

        jScrollPane2.setViewportView(jTextPane2);

        jLabel5.setText("Doctor Name:");

        jScrollPane3.setViewportView(jTextPane3);

        jLabel13.setText("Date of Appoinment:");

        jScrollPane8.setViewportView(jTextPane8);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(65, 65, 65))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jLabel3.getAccessibleContext().setAccessibleName("lblcustomerID");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("Payment");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Charges ID", "Description", "Amount Due"
            }
        ));
        jScrollPane4.setViewportView(jTable1);
        jTable1.getAccessibleContext().setAccessibleName("tblcharges");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setText("Charges");

        jButton12.setText("Print Receipt");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton16.setText("Collect Payment");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 16, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButton16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton12)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton12)
                    .addComponent(jButton16))
                .addGap(27, 27, 27))
        );

        jLabel7.getAccessibleContext().setAccessibleName("lblcharges");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        new CustomerAppoinment(userManager, loggedInStaff).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        new ManageCustomer().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        new Payment(userManager, loggedInStaff).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        new LoginGUI().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        new editprofile(userManager, loggedInStaff).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        int rows = jTable1.getRowCount();
        if (rows > 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Payment collected.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Please choose a correct Appointment ID (no charges loaded).",
                    "No Charges",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }//GEN-LAST:event_jButton16ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextPane jTextPane2;
    private javax.swing.JTextPane jTextPane3;
    private javax.swing.JTextPane jTextPane7;
    private javax.swing.JTextPane jTextPane8;
    // End of variables declaration//GEN-END:variables

private void postInit() {
    chargesModel = (DefaultTableModel) jTable1.getModel();
    ensureFile(APPTS_FILE);
    ensureFile(CHARGES_FILE);
    ensureFile(RECEIPTS_TXT);
    ensureDir(RECEIPTS_PDF_DIR);

    // Fill appointment IDs and cache their info
    loadAppointmentsToCombo();

    // When an appointment is chosen -> fill fields + load charges
    jComboBox1.addActionListener(e -> onAppointmentSelected());

    // Print (export PDF + mark paid)
    jButton12.addActionListener(e -> onPrintAndPdf());

    // Header fields are derived; keep them read-only
    jTextPane2.setEditable(false);
    jTextPane3.setEditable(false);
    jTextPane8.setEditable(false);
}

/* ---------- Helpers for file ---------- */
private void ensureFile(Path p) {
    try {
        if (p.getParent() != null && Files.notExists(p.getParent())) {
            Files.createDirectories(p.getParent());
        }
        if (Files.notExists(p)) Files.createFile(p);
    } catch (IOException ex) {
        logger.log(java.util.logging.Level.SEVERE, "Cannot create file: " + p, ex);
        JOptionPane.showMessageDialog(this, "Cannot create file:\n" + p, "I/O Error", JOptionPane.ERROR_MESSAGE);
    }
}
private void ensureDir(Path dir) {
    try {
        if (dir != null && Files.notExists(dir)) Files.createDirectories(dir);
    } catch (IOException ex) {
        logger.log(java.util.logging.Level.SEVERE, "Cannot create dir: " + dir, ex);
    }
}
private static String money(double v) { return new DecimalFormat("#,##0.00").format(v); }

/* ---------- Appointment loading ---------- */
private void loadAppointmentsToCombo() {
    apptMap.clear();
    jComboBox1.removeAllItems();
    try {
        for (String line : Files.readAllLines(APPTS_FILE, StandardCharsets.UTF_8)) {
            if (line.isBlank()) continue;
            // A0001|C0002|IC|phone|email|date|time|purpose|Dr Name|DoctorId|...
            String[] f = line.split("\\|", -1);
            if (f.length < 9) continue;
            String apptId = f[0].trim();
            String custId = f[1].trim();
            String date   = f[5].trim();
            String time   = f[6].trim();
            String drName = f[8].trim();
            apptMap.put(apptId, new ApptInfo(apptId, custId, date, time, drName));
            jComboBox1.addItem(apptId);
        }
    } catch (IOException e) {
        logger.log(java.util.logging.Level.SEVERE, "read appointments failed", e);
        JOptionPane.showMessageDialog(this, "Failed to read appointments.txt", "I/O Error", JOptionPane.ERROR_MESSAGE);
    }
    if (jComboBox1.getItemCount() > 0) {
        jComboBox1.setSelectedIndex(0);
        onAppointmentSelected();
    }
}

private void onAppointmentSelected() {
    String apptId = (String) jComboBox1.getSelectedItem();
    if (apptId == null) return;
    ApptInfo a = apptMap.get(apptId);
    if (a == null) return;

    // Fill header
    jTextPane2.setText(a.customerId);
    jTextPane3.setText(a.doctorName);
    jTextPane8.setText(a.date); // (time intentionally ignored)

    // Load unpaid charges for the appointment into table
    loadChargesForAppointment(apptId);
}

private void loadChargesForAppointment(String apptId) {
    chargesModel.setRowCount(0);
    try {
        for (String line : Files.readAllLines(CHARGES_FILE, StandardCharsets.UTF_8)) {
            if (line.isBlank()) continue;
            String[] f = line.split("\\|", -1);
            if (f.length < 6) continue;
            if (!apptId.equalsIgnoreCase(f[1].trim())) continue;
            if (Boolean.parseBoolean(f[4].trim())) continue; // skip paid
            String chargeId = f[0].trim();
            String desc     = f[3].trim();
            String amt      = money(parseDoubleSafe(f[2]));
            chargesModel.addRow(new Object[]{ chargeId, desc, amt });
        }
    } catch (IOException e) {
        logger.log(java.util.logging.Level.SEVERE, "read charges failed", e);
        JOptionPane.showMessageDialog(this, "Failed to read charges.txt", "I/O Error", JOptionPane.ERROR_MESSAGE);
    }
}

private double parseDoubleSafe(String s) {
    try { return Double.parseDouble(s); } catch (Exception e) { return 0.0; }
}

/* ---------- Print flow: read unpaid -> print UI -> PDF -> mark paid ---------- */
private void onPrintAndPdf() {
    String apptId = (String) jComboBox1.getSelectedItem();
    if (apptId == null || apptId.isBlank()) {
        JOptionPane.showMessageDialog(this, "Please select an Appointment ID.");
        return;
    }
    ApptInfo a = apptMap.get(apptId);

    // Always re-read unpaid charges from file (authoritative)
    List<ChargeRow> rows = loadUnpaidForApptFromFile(apptId);
    if (rows.isEmpty()) {
        int ok = JOptionPane.showConfirmDialog(this,
                "No unpaid charges for " + apptId + ". Print a zero-total receipt anyway?",
                "No Unpaid Charges", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;
    }

    // 1) Print exactly what user sees
    printComponentExact(this.getContentPane());

    // 2) Export PDF
    try {
        exportReceiptPdfFromRows(apptId, a, rows);
    } catch (ClassNotFoundException cnf) {
        JOptionPane.showMessageDialog(this,
                "PDFBox library not found. Printed successfully, but PDF export was skipped.",
                "PDF Export", JOptionPane.INFORMATION_MESSAGE);
    } catch (IOException ioe) {
        JOptionPane.showMessageDialog(this, "Failed to save PDF: " + ioe.getMessage(),
                "PDF Export Error", JOptionPane.ERROR_MESSAGE);
    }

    // 3) Mark paid & append receipt (only if there were unpaid items)
    try {
        Set<String> ids = new HashSet<>();
        double total = 0.0;
        for (ChargeRow r : rows) { ids.add(r.id); total += r.amount; }
        if (!ids.isEmpty()) {
            markChargesPaid(ids);
            appendReceiptLine(apptId, total, ids);
        }
        // Refresh table to reflect new status
        loadChargesForAppointment(apptId);
        if (!ids.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Receipt generated. Total paid: RM " + money(total));
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Failed to update payment status:\n" + e.getMessage(),
                "I/O Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void printComponentExact(java.awt.Component comp) {
    PrinterJob job = PrinterJob.getPrinterJob();
    job.setJobName("Receipt");
    job.setPrintable((graphics, pageFormat, pageIndex) -> {
        if (pageIndex > 0) return java.awt.print.Printable.NO_SUCH_PAGE;
        java.awt.Graphics2D g2 = (java.awt.Graphics2D) graphics;
        double ix = pageFormat.getImageableX(), iy = pageFormat.getImageableY();
        double iw = pageFormat.getImageableWidth(), ih = pageFormat.getImageableHeight();
        double pw = comp.getWidth(), ph = comp.getHeight();
        g2.translate(ix, iy);
        double scale = Math.min(iw / pw, ih / ph);
        if (scale < 1.0) g2.scale(scale, scale);
        javax.swing.RepaintManager mgr = javax.swing.RepaintManager.currentManager(comp);
        boolean db = mgr.isDoubleBufferingEnabled();
        mgr.setDoubleBufferingEnabled(false);
        comp.printAll(g2);
        mgr.setDoubleBufferingEnabled(db);
        return java.awt.print.Printable.PAGE_EXISTS;
    });
    if (job.printDialog()) {
        try { job.print(); } catch (PrinterException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Print failed", ex);
            JOptionPane.showMessageDialog(this, "Printing failed:\n" + ex.getMessage(),
                    "Print Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

/* ---------- File & PDF helpers ---------- */
private static class ChargeRow {
    String id, desc; double amount;
    ChargeRow(String id, String desc, double amount) { this.id=id; this.desc=desc; this.amount=amount; }
}

private List<ChargeRow> loadUnpaidForApptFromFile(String apptId) {
    List<ChargeRow> rows = new ArrayList<>();
    try {
        for (String line : Files.readAllLines(CHARGES_FILE, StandardCharsets.UTF_8)) {
            if (line.isBlank()) continue;
            String[] f = line.split("\\|", -1);
            if (f.length < 6) continue;
            if (!apptId.equalsIgnoreCase(f[1].trim())) continue;
            if (Boolean.parseBoolean(f[4].trim())) continue;
            rows.add(new ChargeRow(f[0].trim(), f[3].trim(), parseDoubleSafe(f[2].trim())));
        }
    } catch (IOException ex) {
        logger.log(java.util.logging.Level.SEVERE, "Failed reading charges", ex);
        JOptionPane.showMessageDialog(this, "Failed to read charges.txt", "I/O Error", JOptionPane.ERROR_MESSAGE);
    }
    return rows;
}

private void markChargesPaid(Set<String> chargeIds) throws IOException {
    if (chargeIds == null || chargeIds.isEmpty()) return;
    List<String> in = Files.readAllLines(CHARGES_FILE, StandardCharsets.UTF_8);
    List<String> out = new ArrayList<>(in.size());
    for (String line : in) {
        if (line.isBlank()) { out.add(line); continue; }
        String[] f = line.split("\\|", -1);
        if (f.length < 6) { out.add(line); continue; }
        if (chargeIds.contains(f[0].trim())) {
            f[4] = "true"; // mark paid
            out.add(String.join("|", f));
        } else out.add(line);
    }
    Files.write(CHARGES_FILE, out, StandardCharsets.UTF_8,
            StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
}

private void appendReceiptLine(String apptId, double total, Set<String> chargeIds) {
    String receiptId = "R" + LocalDateTime.now().format(TS);
    String receiptLine = String.join("|",
            receiptId, apptId, String.valueOf(total),
            LocalDateTime.now().toString(), String.join(",", chargeIds));
    try {
        Files.write(RECEIPTS_TXT, List.of(receiptLine),
                StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    } catch (IOException e) {
        logger.log(java.util.logging.Level.WARNING, "Failed to write receipts.txt", e);
    }
}

private void exportReceiptPdfFromRows(String apptId, ApptInfo a, List<ChargeRow> rows)
        throws IOException, ClassNotFoundException {

    // Ensure PDFBox is on the classpath
    Class.forName("org.apache.pdfbox.pdmodel.PDDocument");

    double total = 0.0;
    for (ChargeRow r : rows) total += r.amount;

    String filename = "receipt_" + (apptId == null || apptId.isEmpty() ? "NA" : apptId)
            + "_" + LocalDateTime.now().format(TS) + ".pdf";
    Path out = RECEIPTS_PDF_DIR.resolve(filename);

    org.apache.pdfbox.pdmodel.PDDocument doc = new org.apache.pdfbox.pdmodel.PDDocument();
    org.apache.pdfbox.pdmodel.PDPage page = new org.apache.pdfbox.pdmodel.PDPage();
    doc.addPage(page);

    try (org.apache.pdfbox.pdmodel.PDPageContentStream cs =
                 new org.apache.pdfbox.pdmodel.PDPageContentStream(doc, page)) {
        var font = org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;
        float y = 750, margin = 50, leading = 16;

        cs.beginText();
        cs.setFont(font, 14);
        cs.newLineAtOffset(margin, y);
        cs.showText("Clinic Receipt");
        cs.newLineAtOffset(0, -leading);
        cs.setFont(font, 11);
        cs.showText("Appointment: " + (apptId == null ? "" : apptId));
        cs.newLineAtOffset(0, -leading);
        cs.showText("Customer ID: " + (a == null ? "" : a.customerId));
        cs.newLineAtOffset(0, -leading);
        cs.showText("Doctor: " + (a == null ? "" : a.doctorName));
        cs.newLineAtOffset(0, -leading);
        cs.showText("Date: " + (a == null ? "" : a.date));
        cs.newLineAtOffset(0, -leading * 2);

        cs.showText("Charges:");
        cs.newLineAtOffset(0, -leading);
        for (ChargeRow r : rows) {
            cs.showText(r.id + "  |  " + r.desc + "  |  RM " + money(r.amount));
            cs.newLineAtOffset(0, -leading);
        }

        cs.newLineAtOffset(0, -leading);
        cs.setFont(font, 12);
        cs.showText("TOTAL: RM " + money(total));
        cs.endText();
    }

    doc.save(out.toFile());
    doc.close();

    JOptionPane.showMessageDialog(this, "PDF saved in " + RECEIPTS_PDF_DIR.toString());
}

/* ---------- tiny struct ---------- */
private static class ApptInfo {
    String id, customerId, date, time, doctorName;
    ApptInfo(String id, String customerId, String date, String time, String doctorName) {
        this.id = id; this.customerId = customerId; this.date = date; this.time = time; this.doctorName = doctorName;
    }
}
}