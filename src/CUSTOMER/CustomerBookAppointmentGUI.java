/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package CUSTOMER;

import Classes.*;
import Start.LoginGUI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;

public class CustomerBookAppointmentGUI extends javax.swing.JFrame {
    private final ButtonGroup paymentArr = new ButtonGroup();
    private final UserManager userManager;
    private final Classes.Customer currentCustomer;

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(CustomerBookAppointmentGUI.class.getName());
            
    public CustomerBookAppointmentGUI(UserManager userManager, Classes.Customer currentCustomer) {
        initComponents();
        setLocationRelativeTo(null);

        this.userManager = (userManager != null) ? userManager : new UserManager();
        this.currentCustomer = currentCustomer;

        paymentArr.add(rbCustUpfront);
        paymentArr.add(rbCustAfterConsult);
        rbCustAfterConsult.setSelected(true);

        loadCustomerDetails();
        loadDoctors();
        javax.swing.SwingUtilities.invokeLater(this::setupDateSpinners);
    }

    public CustomerBookAppointmentGUI(UserManager userManager) {
        this(userManager,
            (userManager != null && userManager.getCurrentUser() instanceof Classes.Customer)
                ? (Classes.Customer) userManager.getCurrentUser()
                : null
        );
    }

    public CustomerBookAppointmentGUI() { this(new UserManager()); }

    /* ---------------- helpers ---------------- */

    private static String nz(String s) { return (s == null) ? "" : s; }

    private static String extractNameFromLabel(String label) {
        if (label == null) return "";
        int idx = label.indexOf(" (");
        return (idx > 0) ? label.substring(0, idx) : label;
    }

    private void setupDateSpinners() {
        spnMonth.setModel(new SpinnerNumberModel(1, 1, 12, 1));
        spnDay.setModel(new SpinnerNumberModel(1, 1, 31, 1));
        setCurrentDate();

        spnMonth.addChangeListener(e -> {
            updateMaxDaysForMonth();
            validateNotPastDate();
        });
        spnDay.addChangeListener(e -> validateNotPastDate());
    }

    private void setCurrentDate() {
        Calendar c = Calendar.getInstance();
        spnMonth.setValue(c.get(Calendar.MONTH) + 1);
        spnDay.setValue(c.get(Calendar.DAY_OF_MONTH));
    }

    private int getMaxDaysForMonth(int month) {
        return switch (month) {
            case 2 -> 29;
            case 4, 6, 9, 11 -> 30;
            default -> 31;
        };
    }

    private void updateMaxDaysForMonth() {
        int month = (Integer) spnMonth.getValue();
        int maxDays = getMaxDaysForMonth(month);

        SpinnerNumberModel dayModel = (SpinnerNumberModel) spnDay.getModel();
        int currentDay = (Integer) dayModel.getValue();

        if (currentDay > maxDays) dayModel.setValue(maxDays);
        dayModel.setMaximum(maxDays);
    }

    private void validateNotPastDate() {
        Calendar today = Calendar.getInstance();
        int currentMonth = today.get(Calendar.MONTH) + 1;
        int currentDay = today.get(Calendar.DAY_OF_MONTH);

        int selectedMonth = (Integer) spnMonth.getValue();
        int selectedDay = (Integer) spnDay.getValue();

        if (selectedMonth < currentMonth ||
           (selectedMonth == currentMonth && selectedDay < currentDay)) {
            JOptionPane.showMessageDialog(this, "You cannot select a past date",
                    "Invalid Date", JOptionPane.WARNING_MESSAGE);
            setCurrentDate();
        }
    }

    private void loadDoctors() {
        cbCustPreferredDoc.removeAllItems();
        try {
            List<Users> all = userManager.getAllUsers();   // <-- make sure UserManager has this
            int count = 0;
            for (Users u : all) {
                if (u.getRole() == Role.DOCTOR) {
                    Doctor d = (Doctor) u;
                    cbCustPreferredDoc.addItem(d.getDisplayLabel()); // "Dr. Lee (PHYSICIAN)"
                    count++;
                }
            }
            cbCustPreferredDoc.setEnabled(count > 0);
            btnBook.setEnabled(count > 0);
            if (count == 0) cbCustPreferredDoc.addItem("No doctors available");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading doctors: " + e.getMessage(),
                    "Load Error", JOptionPane.ERROR_MESSAGE);
            cbCustPreferredDoc.addItem("No doctors available");
            cbCustPreferredDoc.setEnabled(false);
            btnBook.setEnabled(false);
        }
    }

    private void loadCustomerDetails() {
        try {
            Users u = (currentCustomer != null)
                    ? currentCustomer
                    : (userManager != null ? userManager.getCurrentUser() : null);

            if (u != null && u.getRole() == Role.CUSTOMER) {
                Customer c = (Customer) u;
                txtCustId.setText(nz(c.getId()));
                txtCustName.setText(nz(c.getName()));
                txtCustPhone.setText(nz(c.getPhone()));
                txtCustEmail.setText(nz(c.getEmail()));
                txtCustIc.setText(nz(c.getCustomerIc()));

                txtCustId.setEditable(false);
                txtCustName.setEditable(false);
                txtCustPhone.setEditable(false);
                txtCustEmail.setEditable(false);
                txtCustIc.setEditable(false);
            } else {
                txtCustId.setEditable(false);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading customer details: " + e.getMessage(),
                    "Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnBookApp = new javax.swing.JButton();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        btnBookApp1 = new javax.swing.JButton();
        btnCheckApp = new javax.swing.JButton();
        btnShareComment = new javax.swing.JButton();
        btnViewHistory = new javax.swing.JButton();
        btnEditProf = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCustId = new javax.swing.JTextField();
        txtCustName = new javax.swing.JTextField();
        txtCustPhone = new javax.swing.JTextField();
        txtCustEmail = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cbCustPreferredDoc = new javax.swing.JComboBox<>();
        cbCustTimeOfApp = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        rbCustUpfront = new javax.swing.JRadioButton();
        rbCustAfterConsult = new javax.swing.JRadioButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        cbCustPaymentMethod = new javax.swing.JComboBox<>();
        btnBook = new javax.swing.JButton();
        txtCustSpecialRequirements = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtCustPurposeOfVisit = new javax.swing.JTextArea();
        btnBack = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        txtCustIc = new javax.swing.JTextField();
        spnMonth = new javax.swing.JSpinner();
        spnDay = new javax.swing.JSpinner();

        btnBookApp.setText("Book Appointment");
        btnBookApp.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jButton1.setText("Logout");
        jButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnBookApp1.setText("Book Appointment");
        btnBookApp1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnBookApp1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBookApp1ActionPerformed(evt);
            }
        });

        btnCheckApp.setText("Check Appointments");
        btnCheckApp.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCheckApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckAppActionPerformed(evt);
            }
        });

        btnShareComment.setText("Share Comment");
        btnShareComment.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnShareComment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShareCommentActionPerformed(evt);
            }
        });

        btnViewHistory.setText("View History");
        btnViewHistory.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnViewHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewHistoryActionPerformed(evt);
            }
        });

        btnEditProf.setText("Edit Profile");
        btnEditProf.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnEditProf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditProfActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(btnBookApp1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
            .addComponent(btnCheckApp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnShareComment, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
            .addComponent(btnViewHistory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnEditProf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addGap(116, 116, 116)
                .addComponent(btnBookApp1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCheckApp, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnShareComment, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnViewHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnEditProf, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Book Appointment");

        jLabel2.setText("Customer ID:");

        jLabel3.setText("Name:");

        jLabel4.setText("Phone Number:");

        jLabel5.setText("Email:");

        txtCustId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustIdActionPerformed(evt);
            }
        });

        txtCustName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustNameActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Date of Appointment:");

        jLabel7.setText("Time of Appointment:");

        jLabel8.setText("Purpose of Visit:");

        jLabel9.setText("Payment Arrangement:");

        jLabel10.setText("Preferred Doctor:");

        cbCustPreferredDoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCustPreferredDocActionPerformed(evt);
            }
        });

        cbCustTimeOfApp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "9.00 AM", "9.30 AM", "10.00 AM", "10.30 AM", "11.00 AM", "11.30 AM", "12.00 PM", "12.30 PM", "1.00 PM", "1.30 PM", "2.00 PM", "2.30 PM", "3.00 PM", "3.30 PM", "4.00 PM", "4.30 PM", "5.00 PM", "5.30 PM", "6.00 PM" }));
        cbCustTimeOfApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCustTimeOfAppActionPerformed(evt);
            }
        });

        jLabel11.setText("Month:");

        jLabel12.setText("Day:");

        rbCustUpfront.setText("Upfront");

        rbCustAfterConsult.setText("After consultation");

        jLabel13.setText("Special Requirements:");

        jLabel14.setText("Payment Method:");

        cbCustPaymentMethod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "Credit/Debit Card", "Bank Transfer", "eWallet" }));

        btnBook.setText("Book");
        btnBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBookActionPerformed(evt);
            }
        });

        txtCustPurposeOfVisit.setColumns(20);
        txtCustPurposeOfVisit.setRows(5);
        jScrollPane2.setViewportView(txtCustPurposeOfVisit);

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        jLabel15.setText("Customer IC:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCustSpecialRequirements, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbCustPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(rbCustUpfront, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rbCustAfterConsult))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel15)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnBook))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addGap(0, 18, Short.MAX_VALUE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel4)
                                                    .addComponent(jLabel2)
                                                    .addComponent(jLabel3)
                                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(txtCustId)
                                                    .addComponent(txtCustName)
                                                    .addComponent(txtCustPhone)
                                                    .addComponent(txtCustEmail)
                                                    .addComponent(txtCustIc))))
                                        .addGap(33, 33, 33)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel6)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel7)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cbCustTimeOfApp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel10)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cbCustPreferredDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(spnMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel12)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(spnDay, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(40, 40, 40))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnBack)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(cbCustPreferredDoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(txtCustId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtCustName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(jLabel12)
                        .addComponent(spnMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(spnDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(txtCustPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(cbCustTimeOfApp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(txtCustEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtCustIc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(167, 167, 167)
                        .addComponent(btnBook))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel14)
                                    .addComponent(cbCustPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(14, 14, 14)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbCustUpfront)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbCustAfterConsult)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCustSpecialRequirements, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEditProfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditProfActionPerformed
        new CustomerEditProfileGUI(userManager).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnEditProfActionPerformed

    private void txtCustNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustNameActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        new CustomerHomePageGUI(userManager).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.YES_OPTION){
            userManager.logout();
            this.dispose();
            new LoginGUI().setVisible(true);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBookActionPerformed
        try {
            // time
            String timeString = (String) cbCustTimeOfApp.getSelectedItem();
            if (timeString == null || timeString.isBlank()) {
                JOptionPane.showMessageDialog(this, "Please select a time.", "Missing Time",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            DateTimeFormatter tf = DateTimeFormatter.ofPattern("h.mm a", Locale.ENGLISH);
            LocalTime appointmentTime = LocalTime.parse(timeString, tf);

            // date
            int month = (Integer) spnMonth.getValue();
            int day   = (Integer) spnDay.getValue();
            int year  = LocalDate.now().getYear();
            LocalDate appointmentDate = LocalDate.of(year, month, day);

            // payment arrangement
            String paymentArrangement =
                    rbCustUpfront.isSelected() ? "Upfront" :
                    rbCustAfterConsult.isSelected() ? "After consultation" : "";
            if (paymentArrangement.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please choose a payment arrangement.",
                        "Missing Payment Arrangement", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // doctor name -> id
            String label = (String) cbCustPreferredDoc.getSelectedItem();
            String doctorName = extractNameFromLabel(label);
            String doctorId = null;
            for (Users u : userManager.getAllUsers()) {
                if (u.getRole() == Role.DOCTOR &&
                    u.getName() != null &&
                    u.getName().equalsIgnoreCase(doctorName)) {
                    doctorId = u.getId();
                    break;
                }
            }
            if (doctorId == null) {
                JOptionPane.showMessageDialog(this,
                        "Unable to resolve the selected doctor. Please try again.",
                        "Doctor Lookup Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // build & save
            CustAppointmentManager appt = new CustAppointmentManager(
                null,
                txtCustId.getText().trim(),
                txtCustIc.getText().trim(),
                txtCustName.getText().trim(),
                txtCustPhone.getText().trim(),
                txtCustEmail.getText().trim(),
                appointmentDate,
                appointmentTime,
                txtCustPurposeOfVisit.getText().trim(),
                doctorName,
                doctorId,
                (String) cbCustPaymentMethod.getSelectedItem(),
                paymentArrangement,
                txtCustSpecialRequirements.getText().trim(),
                AppointmentStatus.PENDING,
                "NA"
            );

            if (appt.bookAppointment()) {
                JOptionPane.showMessageDialog(this, "Appointment booked successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to book appointment.",
                        "Booking Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error while booking appointment: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            logger.log(java.util.logging.Level.SEVERE, "Book appointment failed", ex);

        }
    

    
  
    }//GEN-LAST:event_btnBookActionPerformed

    private void txtCustIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustIdActionPerformed
        
    }//GEN-LAST:event_txtCustIdActionPerformed

    private void cbCustTimeOfAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCustTimeOfAppActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbCustTimeOfAppActionPerformed

    private void cbCustPreferredDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCustPreferredDocActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbCustPreferredDocActionPerformed

    private void btnCheckAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckAppActionPerformed
        new CustomerCheckAppointmentGUI(userManager).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnCheckAppActionPerformed

    private void btnShareCommentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShareCommentActionPerformed
        new CustomerCommentGUI(userManager).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnShareCommentActionPerformed

    private void btnBookApp1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBookApp1ActionPerformed
        new CUSTOMER.CustomerBookAppointmentGUI(userManager, currentCustomer).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnBookApp1ActionPerformed

    private void btnViewHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewHistoryActionPerformed
        new CustomerHistoryGUI(userManager).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnViewHistoryActionPerformed

    /**
     * @param args the command line arguments
     */

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new CustomerBookAppointmentGUI().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnBook;
    private javax.swing.JButton btnBookApp;
    private javax.swing.JButton btnBookApp1;
    private javax.swing.JButton btnCheckApp;
    private javax.swing.JButton btnEditProf;
    private javax.swing.JButton btnShareComment;
    private javax.swing.JButton btnViewHistory;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JComboBox<String> cbCustPaymentMethod;
    private javax.swing.JComboBox<String> cbCustPreferredDoc;
    private javax.swing.JComboBox<String> cbCustTimeOfApp;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JRadioButton rbCustAfterConsult;
    private javax.swing.JRadioButton rbCustUpfront;
    private javax.swing.JSpinner spnDay;
    private javax.swing.JSpinner spnMonth;
    private javax.swing.JTextField txtCustEmail;
    private javax.swing.JTextField txtCustIc;
    private javax.swing.JTextField txtCustId;
    private javax.swing.JTextField txtCustName;
    private javax.swing.JTextField txtCustPhone;
    private javax.swing.JTextArea txtCustPurposeOfVisit;
    private javax.swing.JTextField txtCustSpecialRequirements;
    // End of variables declaration//GEN-END:variables
}
