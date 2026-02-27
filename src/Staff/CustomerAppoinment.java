package Staff;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import Classes.UserManager;
import Classes.Staff;
import Start.LoginGUI;


public class CustomerAppoinment extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(CustomerAppoinment.class.getName());

    // ===== files & users.txt indices =====
    private static final Path APPT_FILE  = Paths.get("data/appointments.txt");
    private static final Path USERS_FILE = Paths.get("data/users.txt");
    private static final String NEW_ITEM_PREFIX = "New (";


    // users.txt columns: id|role|name|email|password|phone|specialty|room|ic|address
    private static final int U_ID=0, U_ROLE=1, U_NAME=2, U_EMAIL=3, U_PHONE=5, U_SPEC=6, U_IC=8;

    // caches
    private final Map<String, String[]> customers = new HashMap<>();         
    private final Map<String, String> doctorIdByName  = new HashMap<>();     
    private final Map<String, Integer> rowIndexByApptId = new HashMap<>();

    private final Map<String, String> doctorSpecByName= new HashMap<>();     
    private final UserManager userManager;
    private final Staff loggedInStaff;
    // appointment.txt column indices (14 cols)
    private static final int C_APPT_ID=0, C_CUST_ID=1, C_CUST_IC=2, C_PHONE=3, C_EMAIL=4,
            C_DATE=5, C_TIME=6, C_PURPOSE=7, C_PREFDOC=8, C_DOCID=9, C_PAY_METH=10,
            C_PAY_ARR=11, C_SPECIAL=12, C_STATUS=13,C_CANCEL_REASON = 14;

    // track which appointment row is loaded (for in-place status editing)
    private int currentRowIndex = -1;

    public CustomerAppoinment(UserManager userManager, Staff staff) {
        this.userManager = userManager;
        this.loggedInStaff = staff; 
        initComponents();
        setLocationRelativeTo(null);

        // read-only identity fields (auto-filled from users.txt when Customer ID changes)
        jTextPane3.setEditable(false); // name
        jTextPane4.setEditable(false); // phone
        jTextPane5.setEditable(false); // email
        jTextPane6.setEditable(false); // ic

        // these are editable for a new appointment
        jTextPane7.setEditable(true);  // date
        jTextArea1.setEditable(true);  // purpose
        jTextPane8.setEditable(true);  // special

        ensureFileExists();

        populateDoctorCombo();
        populateCustomerCombo();

        wireActions();
        
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton8 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jFrame1 = new javax.swing.JFrame();
        jPanel2 = new javax.swing.JPanel();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        jPanel1 = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane3 = new javax.swing.JTextPane();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextPane4 = new javax.swing.JTextPane();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextPane5 = new javax.swing.JTextPane();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextPane6 = new javax.swing.JTextPane();
        jLabel10 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextPane7 = new javax.swing.JTextPane();
        jLabel12 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTextPane8 = new javax.swing.JTextPane();
        jLabel16 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        custCB = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        appIDCB = new javax.swing.JComboBox<>();

        jButton8.setLabel("Edit Profile");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton2.setText("Customer Appoinment");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Manage Customer ");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Assign Doctor");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setLabel("View History");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Payment ");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setLabel("Log Out");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Manage Customer");

        jFrame1.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jButton11.setText("Customer Appoinment");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setText("Manage Customer ");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton16.setText("Payment ");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton17.setLabel("Log Out");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton18.setLabel("Edit Profile");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton16, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                    .addComponent(jButton18, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(jButton17)
                    .addContainerGap(102, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(179, 179, 179)
                .addComponent(jButton11)
                .addGap(18, 18, 18)
                .addComponent(jButton16)
                .addGap(18, 18, 18)
                .addComponent(jButton18)
                .addContainerGap(216, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(139, 139, 139)
                    .addComponent(jButton12)
                    .addContainerGap(338, Short.MAX_VALUE)))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(jButton17)
                    .addContainerGap(461, Short.MAX_VALUE)))
        );

        jButton19.setLabel("Back");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("Customer Appoinment");

        jLabel5.setText("Customer ID:");

        jScrollPane2.setViewportView(jTextPane2);

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrame1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jFrame1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton19))
                    .addGroup(jFrame1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jFrame1Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jScrollPane2))
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(39, 39, 39))
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jFrame1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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

        jButton9.getAccessibleContext().setAccessibleName("btncustomerappoinment");
        jButton10.getAccessibleContext().setAccessibleName("btnmanagecustomer");
        jButton13.getAccessibleContext().setAccessibleName("btnpayment");
        jButton14.getAccessibleContext().setAccessibleName("btnlogout");
        jButton15.getAccessibleContext().setAccessibleName("btneditprofile");
        jButton15.getAccessibleContext().setAccessibleDescription("");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("Customer Appoinment");

        jLabel3.setText("Customer ID:");

        jScrollPane3.setViewportView(jTextPane3);

        jLabel6.setText("Name:");

        jLabel7.setText("Phone Number:");

        jScrollPane4.setViewportView(jTextPane4);

        jLabel8.setText("Email:");

        jScrollPane5.setViewportView(jTextPane5);

        jLabel9.setText("Customer IC:");

        jScrollPane6.setViewportView(jTextPane6);

        jLabel10.setText("Preferred Doctor:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel11.setText("Date of Appoinment:");

        jScrollPane7.setViewportView(jTextPane7);

        jLabel12.setText("Time of Appoinment:");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "9.00am", "9.30am", "10.00am", "10.30am", "11.00am", "11.30am", "12.00pm", "12.30pm", "13.00pm", "13.30pm", "14.00pm", "14.30pm", "15.00pm", "15.30pm", "16.00pm", "16.30pm", "17.00pm", "17.30pm", "18.00pm", " " }));

        jLabel13.setText("Purpose of Visit:");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane8.setViewportView(jTextArea1);

        jLabel14.setText("Payment Method:");

        jLabel15.setText("Special Requirements:");

        jScrollPane9.setViewportView(jTextPane8);

        jLabel16.setText("Status:");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PENDING", "ASSIGNED", "COMPLETED", "CANCELLED" }));
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });

        jButton20.setText("Add");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jButton21.setText("Clear");

        custCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bank Transfer", "Credit/Debit", "Cash", "eWallet" }));

        jLabel17.setText("App ID:");

        appIDCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane9)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jScrollPane5))
                                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                            .addComponent(jButton20)
                                            .addGap(49, 49, 49))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(24, 24, 24)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jScrollPane3)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(appIDCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(0, 0, Short.MAX_VALUE)))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(1, 1, 1)))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                            .addComponent(jScrollPane6))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24)
                                .addComponent(custCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton21))
                                .addGap(0, 50, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(custCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(appIDCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton20)
                    .addComponent(jButton21))
                .addContainerGap(88, Short.MAX_VALUE))
        );

        jLabel2.getAccessibleContext().setAccessibleName("lblcustomerappoinment");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        new CustomerAppoinment(userManager, loggedInStaff).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        new ManageCustomer(userManager, loggedInStaff).setVisible(true);

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

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        
    }//GEN-LAST:event_jButton20ActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> appIDCB;
    private javax.swing.JComboBox<String> custCB;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextPane jTextPane2;
    private javax.swing.JTextPane jTextPane3;
    private javax.swing.JTextPane jTextPane4;
    private javax.swing.JTextPane jTextPane5;
    private javax.swing.JTextPane jTextPane6;
    private javax.swing.JTextPane jTextPane7;
    private javax.swing.JTextPane jTextPane8;
    // End of variables declaration//GEN-END:variables

        /* ===================== wiring ===================== */
    private void wireActions() {
    // Status change only applies when an existing row is loaded
        jComboBox4.addActionListener(evt -> {
            if (currentRowIndex < 0) return;
            String newStatus = String.valueOf(jComboBox4.getSelectedItem());
            try {
                List<String> rows = readAllRows();
                if (currentRowIndex >= rows.size()) return;

                String[] f = splitRow(rows.get(currentRowIndex));
                f = ensureCols(f, C_STATUS + 1);
                f[C_STATUS] = (newStatus == null) ? "" : newStatus.trim();

                rows.set(currentRowIndex, joinRow(f));
                writeAllRows(rows);
                JOptionPane.showMessageDialog(this, "Status updated to: " + f[C_STATUS]);
            } catch (Exception ex) {
                logger.log(java.util.logging.Level.SEVERE, "Failed to update status", ex);
                JOptionPane.showMessageDialog(this, "Failed to update status:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Buttons
        jButton20.addActionListener(evt -> addAppointmentFromForm()); // Add
        jButton21.addActionListener(evt -> clearForm());              // Clear

        // When customer changes → fill identity + rebuild App ID list
        custCB.addActionListener(e -> {
            fillCustomerFromSelection();     // keep identity filled
            clearAppointmentFields();        // only clear appointment bits
            rebuildAppIdListForSelectedCustomer();
        });

        // When App ID changes → either go to "New" mode or load a row
        appIDCB.addActionListener(e -> onAppIdChanged());
    }

    /* ===================== file helpers ===================== */
    private void ensureFileExists() {
        try {
            Files.createDirectories(APPT_FILE.getParent());
            if (!Files.exists(APPT_FILE)) Files.createFile(APPT_FILE);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot create " + APPT_FILE.toAbsolutePath(), e);
        }
    }

    private List<String> readAllRows() {
        try {
            if (!Files.exists(APPT_FILE)) return new ArrayList<>();
            return Files.readAllLines(APPT_FILE, StandardCharsets.UTF_8);
        } catch (IOException e) { throw new UncheckedIOException(e); }
    }

    private void writeAllRows(List<String> rows) {
        try (BufferedWriter bw = Files.newBufferedWriter(APPT_FILE, StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)) {
            for (String r : rows) { bw.write(r == null ? "" : r); bw.newLine(); }
        } catch (IOException e) { throw new UncheckedIOException(e); }
    }

    private String[] splitRow(String row) { return row.split("\\|", -1); }
    private String   joinRow(String[] f)   { return String.join("|", f); }
    private String[] ensureCols(String[] f, int min) {
        if (f.length >= min) return f;
        String[] g = Arrays.copyOf(f, min);
        for (int i = f.length; i < min; i++) g[i] = "";
        return g;
    }
    private String getSafely(String[] f, int idx) {
        return (idx >= 0 && idx < f.length && f[idx] != null) ? f[idx] : "";
    }

    /* ===================== data loading ===================== */
    private void populateDoctorCombo() {
        doctorIdByName.clear();
        doctorSpecByName.clear();
        jComboBox1.removeAllItems();

        try {
            if (!Files.exists(USERS_FILE)) return;
            for (String line : Files.readAllLines(USERS_FILE, StandardCharsets.UTF_8)) {
                if (line.isBlank()) continue;
                String[] p = line.split("\\|", -1); // id|role|name|email|pwd|phone|spec|room|ic|addr
                if (p.length > U_SPEC && "DOCTOR".equalsIgnoreCase(p[U_ROLE].trim())) {
                    String id   = p[U_ID].trim();
                    String name = p[U_NAME].trim();
                    String spec = p[U_SPEC].trim();
                    doctorIdByName.put(name, id);
                    doctorSpecByName.put(name, spec);
                    jComboBox1.addItem(name);
                }
            }
        } catch (IOException ignore) {}

        // Render as "Dr. Lee — PHYSICIAN" while keeping the selected value = doctor's name
        jComboBox1.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                String name = (value == null) ? "" : value.toString();
                String spec = doctorSpecByName.getOrDefault(name, "");
                setText(spec.isEmpty() ? name : name + " — " + spec);
                return this;
            }
        });
    }

    private void populateCustomerCombo() {
        customers.clear();
        custCB.removeAllItems();
        try {
            if (!Files.exists(USERS_FILE)) return;
            for (String line : Files.readAllLines(USERS_FILE, StandardCharsets.UTF_8)) {
                if (line.isBlank()) continue;
                String[] p = line.split("\\|", -1); // id|role|name|email|pwd|phone|...|ic|addr
                if (p.length > U_IC && "CUSTOMER".equalsIgnoreCase(p[U_ROLE].trim())) {
                    customers.put(p[U_ID].trim(), p);
                    custCB.addItem(p[U_ID].trim());
                }
            }
        } catch (IOException ignore) {}

        if (custCB.getItemCount() > 0) custCB.setSelectedIndex(0);
        fillCustomerFromSelection();
        rebuildAppIdListForSelectedCustomer();  // <— IMPORTANT
    }


    private void fillCustomerFromSelection() {
        Object sel = custCB.getSelectedItem();
        if (sel == null) { clearCustomerFields(); return; }
        String[] p = customers.get(sel.toString());
        if (p == null) { clearCustomerFields(); return; }
        jTextPane3.setText(p[U_NAME].trim());
        jTextPane4.setText(p[U_PHONE].trim());
        jTextPane5.setText(p[U_EMAIL].trim());
        jTextPane6.setText(p[U_IC].trim());
    }

    private void clearCustomerFields() {
        jTextPane3.setText("");
        jTextPane4.setText("");
        jTextPane5.setText("");
        jTextPane6.setText("");
    }

    /* ===================== helpers ===================== */
    private String nextAppointmentId() {
        int max = 0;
        for (String line : readAllRows()) {
            if (line == null || line.isBlank()) continue;
            String[] p = splitRow(line);
            if (p.length > 0 && p[0].startsWith("A")) {
                try { max = Math.max(max, Integer.parseInt(p[0].substring(1))); }
                catch (NumberFormatException ignore) {}
            }
        }
        return String.format("A%04d", max + 1);
    }

    private String lookupDoctorId(String doctorName) {
        return doctorIdByName.getOrDefault(doctorName == null ? "" : doctorName.trim(), "");
    }

    private String normalizeTimeTo24(String uiTime) {
        if (uiTime == null || uiTime.isBlank()) return "";
        String t = uiTime.trim().toLowerCase().replace(" ", "").replace('.', ':');
        boolean isAM = t.endsWith("am");
        boolean isPM = t.endsWith("pm");
        if (isAM || isPM) t = t.substring(0, t.length() - 2);
        String[] parts = t.split(":");
        int h = Integer.parseInt(parts[0]);
        int m = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
        if (isAM) { if (h == 12) h = 0; }
        else if (isPM) { if (h < 12) h += 12; }
        return String.format("%02d:%02d", h, m);
    }

    private String normalizePaymentMethod(String s) {
        if (s == null) return "";
        String t = s.trim().toLowerCase();
        if (t.contains("cash")) return "Cash";
        if (t.contains("wallet")) return "eWallet";
        if (t.contains("credit") || t.contains("debit")) return "Credit/Debit Card";
        if (t.contains("transfer")) return "Bank Transfer";
        return s.trim();
    }

    private String defaultPaymentArrangement(String payMethod) {
        if (payMethod == null) return "After consultation";
        switch (payMethod) {
            case "Credit/Debit Card":
            case "eWallet":      return "Upfront";
            case "Cash":
            case "Bank Transfer":
            default:             return "After consultation";
        }
    }
    
    private void rebuildAppIdListForSelectedCustomer() {
        appIDCB.removeAllItems();
        rowIndexByApptId.clear();

        String nextId = nextAppointmentId();
        appIDCB.addItem(NEW_ITEM_PREFIX + nextId + ")");  // first choice = “New (A00xx)”

        Object sel = custCB.getSelectedItem();
        String custId = (sel == null) ? "" : sel.toString().trim();
        if (custId.isEmpty()) return;

        List<String> rows = readAllRows();
        for (int i = 0; i < rows.size(); i++) {
            String r = rows.get(i);
            if (r == null || r.isBlank()) continue;
            String[] f = splitRow(r);
            if (f.length >= C_STATUS+1 && custId.equalsIgnoreCase(getSafely(f, C_CUST_ID))) {
                String apptId = getSafely(f, C_APPT_ID);
                if (!apptId.isBlank()) {
                    appIDCB.addItem(apptId);
                    rowIndexByApptId.put(apptId, i);
                }
            }
        }
        appIDCB.setSelectedIndex(0);  // default to “New (…)”
        currentRowIndex = -1;
        clearAppointmentFields();
    }

    private void onAppIdChanged() {
        Object sel = appIDCB.getSelectedItem();
        if (sel == null) return;
        String text = sel.toString();

        if (text.startsWith(NEW_ITEM_PREFIX)) {
        currentRowIndex = -1;
        clearAppointmentFields();        // not clearIdentityFields()
        jComboBox4.setSelectedItem("PENDING");
        return;
    }

        Integer idx = rowIndexByApptId.get(text);
        if (idx == null) { currentRowIndex = -1; return; }

        List<String> rows = readAllRows();
        if (idx < 0 || idx >= rows.size()) { currentRowIndex = -1; return; }

        currentRowIndex = idx;
        String[] f = splitRow(rows.get(idx));
        f = ensureCols(f, C_CANCEL_REASON + 1);
        fillFormFromRow(f);
        String status = getSafely(f, C_STATUS);
        jComboBox4.setSelectedItem(status.isBlank() ? "PENDING" : status);
    }

    /* ===================== add new appointment ===================== */
    private void addAppointmentFromForm() {
        String custId = (custCB.getSelectedItem()==null) ? "" : custCB.getSelectedItem().toString().trim();
        String custIC = jTextPane6.getText().trim();
        String phone  = jTextPane4.getText().trim();
        String email  = jTextPane5.getText().trim();
        String date   = jTextPane7.getText().trim();                       // yyyy-MM-dd
        String time24 = normalizeTimeTo24(String.valueOf(jComboBox2.getSelectedItem()));
        String purpose= jTextArea1.getText().trim();

        String prefDoc = String.valueOf(jComboBox1.getSelectedItem()).trim();
        String docId   = lookupDoctorId(prefDoc);
        String payMeth = normalizePaymentMethod(String.valueOf(jComboBox3.getSelectedItem()));
        String payArr  = defaultPaymentArrangement(payMeth);
        String special = jTextPane8.getText().trim();
        String status  = String.valueOf(jComboBox4.getSelectedItem());
        if (status == null || status.isBlank()) status = "PENDING";

        if (custId.isBlank() || custIC.isBlank() || phone.isBlank() || email.isBlank()
            || date.isBlank() || time24.isBlank() || purpose.isBlank() || prefDoc.isBlank()) {
            JOptionPane.showMessageDialog(this,
                "Fill required fields: Customer, IC, Phone, Email, Date, Time, Purpose, Doctor.");
            return;
        }
        if (docId.isBlank()) {
            JOptionPane.showMessageDialog(this, "Selected doctor not found in users.txt.");
            return;
        }

        String specialOrNA = special.isBlank() ? "NA" : special;

        List<String> rows = readAllRows();

        Object appSel = appIDCB.getSelectedItem();
        boolean isNew = (appSel == null) || appSel.toString().startsWith(NEW_ITEM_PREFIX) || currentRowIndex < 0;

        if (isNew) {
            String id = nextAppointmentId();
            String row = String.join("|",
                id, custId, custIC, phone, email, date, time24, purpose,
                prefDoc, docId, payMeth, payArr, specialOrNA, status, "NA"   // 15th col
            );

            try (BufferedWriter w = Files.newBufferedWriter(APPT_FILE, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                if (Files.size(APPT_FILE) > 0) w.newLine();
                w.write(row);
                JOptionPane.showMessageDialog(this, "Appointment added: " + id);
            } catch (IOException ex) {
                logger.log(java.util.logging.Level.SEVERE, "Add failed", ex);
                JOptionPane.showMessageDialog(this, "Failed to add:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            try {
                if (currentRowIndex >= rows.size()) { JOptionPane.showMessageDialog(this, "Row index out of range."); return; }
                String[] f = splitRow(rows.get(currentRowIndex));
                f = ensureCols(f, C_CANCEL_REASON + 1);

                f[C_CUST_ID]   = custId;
                f[C_CUST_IC]   = custIC;
                f[C_PHONE]     = phone;
                f[C_EMAIL]     = email;
                f[C_DATE]      = date;
                f[C_TIME]      = time24;
                f[C_PURPOSE]   = purpose;
                f[C_PREFDOC]   = prefDoc;
                f[C_DOCID]     = docId;
                f[C_PAY_METH]  = payMeth;
                f[C_PAY_ARR]   = payArr;
                f[C_SPECIAL]   = specialOrNA;
                f[C_STATUS]    = status;
                if (getSafely(f, C_CANCEL_REASON).isBlank()) f[C_CANCEL_REASON] = "NA";

                rows.set(currentRowIndex, joinRow(f));
                writeAllRows(rows);
                JOptionPane.showMessageDialog(this, "Appointment updated: " + getSafely(f, C_APPT_ID));
            } catch (Exception ex) {
                logger.log(java.util.logging.Level.SEVERE, "Update failed", ex);
                JOptionPane.showMessageDialog(this, "Failed to update:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Refresh App ID list so the new/updated item appears
        rebuildAppIdListForSelectedCustomer();
    }

    /* ===================== push a row to the UI ===================== */
    private void fillFormFromRow(String[] f) {
        f = ensureCols(f, C_CANCEL_REASON + 1);
        jTextPane4.setText(getSafely(f, C_PHONE));
        jTextPane5.setText(getSafely(f, C_EMAIL));
        jTextPane6.setText(getSafely(f, C_CUST_IC));

        jTextPane7.setText(getSafely(f, C_DATE));

        // time
        String time = getSafely(f, C_TIME);
        boolean found = false;
        for (int i = 0; i < jComboBox2.getItemCount(); i++) {
            if (time.equalsIgnoreCase(String.valueOf(jComboBox2.getItemAt(i)))) { found = true; break; }
        }
        if (!found && time != null && !time.isBlank()) jComboBox2.insertItemAt(time, 0);
        jComboBox2.setSelectedItem(time);

        // doctor
        String preferredDoctor = getSafely(f, C_PREFDOC);
        boolean docFound = false;
        for (int i = 0; i < jComboBox1.getItemCount(); i++) {
            if (preferredDoctor.equalsIgnoreCase(String.valueOf(jComboBox1.getItemAt(i)))) { docFound = true; break; }
        }
        if (!docFound && !preferredDoctor.isBlank()) jComboBox1.insertItemAt(preferredDoctor, 0);
        jComboBox1.setSelectedItem(preferredDoctor);

        jTextArea1.setText(getSafely(f, C_PURPOSE));
        jTextPane8.setText(getSafely(f, C_SPECIAL));
    }

    /* ===================== clear form ===================== */
    private void clearIdentityFields() {
        jTextPane3.setText(""); // name
        jTextPane4.setText(""); // phone
        jTextPane5.setText(""); // email
        jTextPane6.setText(""); // ic
    }

    private void clearAppointmentFields() {
        jTextPane7.setText("");            
        if (jComboBox2.getItemCount() > 0)
        jComboBox2.setSelectedIndex(0); 
        jTextArea1.setText("");           
        jTextPane8.setText("");            
        if (jComboBox1.getItemCount() > 0) 
        jComboBox1.setSelectedIndex(0); 
        jComboBox4.setSelectedItem("PENDING");                             
    }

    private void clearForm() {
        clearIdentityFields();
        clearAppointmentFields();
    }
}
