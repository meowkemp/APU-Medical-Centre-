package Classes;

import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class FeedbackService {
    private final File file;
    // 7 fields: FB|Doc|Cust|Appt|Date|Time|Notes
    public FeedbackService(File file) { this.file = file; }

    public void loadFeedbacks(DefaultTableModel model, String doctorId) {
        model.setRowCount(0);
        if (file == null || !file.exists()) return;

        String wanted = (doctorId == null) ? null : doctorId.trim();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] a = line.split("\\|", -1);

                String id, doc, appt, dateTime, notes;

                if (a.length >= 7) {
                    id   = a[0].trim();
                    doc  = a[1].trim();
                    appt = a[3].trim();
                    dateTime = (a[4] + " " + a[5]).trim();
                    notes = a[6].trim();
                } else if (a.length == 5) { // legacy
                    id   = a[0].trim();
                    doc  = a[1].trim();
                    appt = a[2].trim();
                    dateTime = a[3].trim();
                    notes = a[4].trim();
                } else {
                    continue; // bad line
                }

                if (wanted != null && !wanted.isEmpty() && !doc.equalsIgnoreCase(wanted)) continue;

                // Table columns: Feedback ID | Doctor ID | Appointment ID | Date & Time | Feedback Given
                model.addRow(new Object[]{ id, doc, appt, dateTime, notes });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAll(DefaultTableModel model) {
        loadFeedbacks(model, null);
    }
}
