package Classes;

import javax.swing.table.DefaultTableModel;

public interface History {
    DefaultTableModel viewAppointmentHistory(String[] columnNames, String currentUserId);
    DefaultTableModel viewChargeHistory(String[] columnNames, String currentUserId);
    DefaultTableModel viewCommentHistory(String[] columnNames, String currentUserId);
    DefaultTableModel viewFeedbackHistory(String[] columnNames, String currentUserId);
}
