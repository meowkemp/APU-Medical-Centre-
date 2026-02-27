package Classes;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

/**
 * Generates PDFs using PDFBox from your txt data sources.
 * Files are assumed to be pipe-delimited.
 */
public class ReportGenerator {

    // ----- DATA FILES -----
    private static final String CHARGES_FILE     = "data/charges.txt";
    private static final String APPTS_FILE       = "data/appointments.txt";
    private static final String COMMENTS_FILE    = "data/comments.txt";

    // ----- small helpers -----
    private static File ensureParentDirs(String path) throws IOException {
        File f = new File(path);
        File p = f.getParentFile();
        if (p != null && !p.exists() && !p.mkdirs()) {
            throw new IOException("Could not create folder: " + p.getAbsolutePath());
        }
        return f;
    }

    private static void tryOpen(File pdf) {
        try {
            if (Desktop.isDesktopSupported()) Desktop.getDesktop().open(pdf);
        } catch (Exception ignored) {}
    }

    private static String safe(String s) {
        if (s == null) return "";
        // Replace a few common Unicode chars not encodable by Type1 fonts
        String t = s.replace('\u2019','\'')
                    .replace('\u2018','\'')
                    .replace('\u201C','"')
                    .replace('\u201D','"')
                    .replace('\u2013','-')
                    .replace('\u2014','-');
        StringBuilder b = new StringBuilder(t.length());
        for (char c : t.toCharArray()) {
            if (c >= 32 && c <= 126) b.append(c);
            else b.append('?');
        }
        return b.toString();
    }

    private static List<String> wrap(String text, int maxLen) {
        List<String> lines = new ArrayList<>();
        String t = safe(text);
        if (t.length() <= maxLen) { lines.add(t); return lines; }
        String[] words = t.split("\\s+");
        StringBuilder line = new StringBuilder();
        for (String w : words) {
            if ((line.length() + 1 + w.length()) > maxLen) {
                if (line.length() > 0) lines.add(line.toString());
                line.setLength(0);
                // if a single word is too long, hard-cut it
                while (w.length() > maxLen) {
                    lines.add(w.substring(0, maxLen));
                    w = w.substring(maxLen);
                }
                line.append(w);
            } else {
                if (line.length() > 0) line.append(' ');
                line.append(w);
            }
        }
        if (line.length() > 0) lines.add(line.toString());
        return lines;
    }

    // ====== DATA MODELS ======
    private static class ChargeRec {
        String id, apptId, desc, doctorId;
        double amount;
        boolean paid;
    }

    private static class ApptRec {
        String id, customerId, email, date, time, purpose, doctorName, doctorId, status;
    }

    private static class CommentRec {
        String id, customerId, recipientId, apptId, dateTime, text;
    }

    // ====== LOADERS ======
    private static List<ChargeRec> loadCharges() throws IOException {
        List<ChargeRec> list = new ArrayList<>();
        if (!Files.exists(Paths.get(CHARGES_FILE))) return list;
        for (String line : Files.readAllLines(Paths.get(CHARGES_FILE), StandardCharsets.UTF_8)) {
            if (line == null || line.isBlank()) continue;
            String[] p = line.split("\\|", -1);
            if (p.length < 6) continue;
            ChargeRec r = new ChargeRec();
            r.id       = p[0].trim();
            r.apptId   = p[1].trim();
            try { r.amount = Double.parseDouble(p[2].trim()); } catch (Exception e) { r.amount = 0d; }
            r.desc     = p[3].trim();
            r.paid     = Boolean.parseBoolean(p[4].trim());
            r.doctorId = p[5].trim();
            list.add(r);
        }
        return list;
    }

    private static List<ApptRec> loadAppointments() throws IOException {
        List<ApptRec> list = new ArrayList<>();
        if (!Files.exists(Paths.get(APPTS_FILE))) return list;
        for (String line : Files.readAllLines(Paths.get(APPTS_FILE), StandardCharsets.UTF_8)) {
            if (line == null || line.isBlank()) continue;
            String[] p = line.split("\\|", -1);
            // A0001|C0002|IC|Phone|Email|Date|Time|Purpose|DoctorName|DoctorId|PayMethod|PayTiming|SpecialReq|Status
            if (p.length < 14) continue;
            ApptRec r = new ApptRec();
            r.id         = p[0].trim();
            r.customerId = p[1].trim();
            r.email      = p[4].trim();
            r.date       = p[5].trim();
            r.time       = p[6].trim();
            r.purpose    = p[7].trim();
            r.doctorName = p[8].trim();
            r.doctorId   = p[9].trim();
            r.status     = p[13].trim();
            list.add(r);
        }
        return list;
    }

    private static List<CommentRec> loadComments() throws IOException {
        List<CommentRec> list = new ArrayList<>();
        if (!Files.exists(Paths.get(COMMENTS_FILE))) return list;
        for (String line : Files.readAllLines(Paths.get(COMMENTS_FILE), StandardCharsets.UTF_8)) {
            if (line == null || line.isBlank()) continue;
            String[] p = line.split("\\|", -1);
            if (p.length < 6) continue;
            CommentRec r = new CommentRec();
            r.id         = p[0].trim();
            r.customerId = p[1].trim();
            r.recipientId= p[2].trim();
            r.apptId     = p[3].trim();
            r.dateTime   = p[4].trim();
            r.text       = p[5].trim();
            list.add(r);
        }
        return list;
    }

    // ====== REPORTS ======

    /** Financial / Charges report (uses charges.txt). Kept method name for your existing button. */
    public static void generateInvoice(String filePath) throws IOException {
        List<ChargeRec> rows = loadCharges();

        double total = 0, totalPaid = 0, totalUnpaid = 0;
        Map<String, Double> byDoctor = new TreeMap<>();
        for (ChargeRec r : rows) {
            total += r.amount;
            if (r.paid) totalPaid += r.amount; else totalUnpaid += r.amount;
            byDoctor.merge(r.doctorId, r.amount, Double::sum);
        }

        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(new PDRectangle(PDRectangle.LETTER.getWidth(), PDRectangle.LETTER.getHeight()));
        doc.addPage(page);
        PDPageContentStream cs = new PDPageContentStream(doc, page);

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Header
        cs.beginText();
        cs.setFont(PDType1Font.TIMES_BOLD, 16);
        cs.newLineAtOffset(200, 740);
        cs.showText("Financial Report (Charges)");
        cs.endText();

        final float addrX = 450f, addrY = 742f, leading = 14f;
        cs.beginText();
        cs.setFont(PDType1Font.TIMES_ROMAN, 10);
        cs.newLineAtOffset(addrX, addrY);
        String[] addr = {
            "Jalan Teknologi 5,",
            "Taman Teknologi Malaysia,",
            "57000 Kuala Lumpur,",
            "Wilayah Persekutuan Kuala Lumpur"
        };
        for (String s : addr) { cs.showText(s); cs.newLineAtOffset(0, -leading); }
        cs.showText("Generated: " + today); // directly under the address
        cs.endText();

        // Compute a safe Y below header block for the separator and table
        float headerBottomY = addrY - leading * (addr.length + 1); // last baseline used above
        float sepY = headerBottomY - 8f;

        // Separator line (moved down to avoid clashing with header)
        cs.setStrokingColor(Color.BLACK);
        cs.setLineWidth(1f);
        cs.moveTo(30, sepY);
        cs.lineTo(582, sepY);
        cs.stroke();

        // Table starts comfortably below the separator
        float y = sepY - 20f, rowH = 16f;
        float xId=40, xAppt=90, xDoc=160, xAmt=230, xPaid=285, xDesc=330;

        // headers
        cs.beginText(); cs.setFont(PDType1Font.TIMES_BOLD, 10);
        cs.newLineAtOffset(xId,   y); cs.showText("Charge ID"); cs.endText();
        cs.beginText(); cs.newLineAtOffset(xAppt, y); cs.showText("Appt");      cs.endText();
        cs.beginText(); cs.newLineAtOffset(xDoc,  y); cs.showText("Doctor");    cs.endText();
        cs.beginText(); cs.newLineAtOffset(xAmt,  y); cs.showText("Amount");    cs.endText();
        cs.beginText(); cs.newLineAtOffset(xPaid, y); cs.showText("Paid");      cs.endText();
        cs.beginText(); cs.newLineAtOffset(xDesc, y); cs.showText("Description");cs.endText();

        y -= 8;

        cs.setFont(PDType1Font.TIMES_ROMAN, 10);
        for (ChargeRec r : rows) {
            y -= rowH;
            cs.beginText(); cs.newLineAtOffset(xId,   y); cs.showText(safe(r.id));                         cs.endText();
            cs.beginText(); cs.newLineAtOffset(xAppt, y); cs.showText(safe(r.apptId));                     cs.endText();
            cs.beginText(); cs.newLineAtOffset(xDoc,  y); cs.showText(safe(r.doctorId));                   cs.endText();
            cs.beginText(); cs.newLineAtOffset(xAmt,  y); cs.showText(String.format("%.2f", r.amount));    cs.endText();
            cs.beginText(); cs.newLineAtOffset(xPaid, y); cs.showText(r.paid ? "YES" : "NO");              cs.endText();

            // wrap description to 45 chars
            List<String> descLines = wrap(r.desc, 45);
            cs.beginText(); cs.newLineAtOffset(xDesc, y);
            cs.showText(descLines.get(0)); cs.endText();
            // extra wrapped lines
            for (int i=1;i<descLines.size();i++) {
                y -= rowH;
                cs.beginText(); cs.newLineAtOffset(xDesc, y); cs.showText(descLines.get(i)); cs.endText();
            }
            if (y < 60) break; // (simple overflow guard for one page)
        }

        // Summary
        y -= 20;
        cs.moveTo(30, y); cs.lineTo(582, y); cs.stroke();
        y -= 16;

        cs.beginText(); cs.setFont(PDType1Font.TIMES_BOLD, 12);
        cs.newLineAtOffset(40, y); cs.showText("Summary"); cs.endText();

        y -= 16;
        cs.beginText(); cs.setFont(PDType1Font.TIMES_ROMAN, 10);
        cs.newLineAtOffset(40, y); cs.showText("Total charges : " + String.format("%.2f", total)); cs.endText();
        y -= 14;
        cs.beginText(); cs.newLineAtOffset(40, y); cs.showText("Paid          : " + String.format("%.2f", totalPaid)); cs.endText();
        y -= 14;
        cs.beginText(); cs.newLineAtOffset(40, y); cs.showText("Unpaid        : " + String.format("%.2f", totalUnpaid)); cs.endText();

        y -= 20;
        cs.beginText(); cs.setFont(PDType1Font.TIMES_BOLD, 11);
        cs.newLineAtOffset(40, y); cs.showText("Amount by Doctor"); cs.endText();
        cs.setFont(PDType1Font.TIMES_ROMAN, 10);
        for (Map.Entry<String,Double> e : byDoctor.entrySet()) {
            y -= 14; if (y < 40) break;
            cs.beginText(); cs.newLineAtOffset(40, y);
            cs.showText(e.getKey() + " : " + String.format("%.2f", e.getValue()));
            cs.endText();
        }

        cs.close();
        File out = ensureParentDirs(filePath);
        doc.save(out);
        doc.close();
        tryOpen(out);
    }

    /** Appointment report (uses appointments.txt). */
    public static void generateAppointmentReport(String filePath) throws IOException {
        List<ApptRec> appts = loadAppointments();

        // counts by doctor & by status
        Map<String,Integer> byDoctor = new TreeMap<>();
        Map<String,Integer> byStatus = new TreeMap<>();
        for (ApptRec a : appts) {
            byDoctor.merge(a.doctorName + " (" + a.doctorId + ")", 1, Integer::sum);
            byStatus.merge(a.status, 1, Integer::sum);
        }

        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(new PDRectangle(PDRectangle.LETTER.getWidth(), PDRectangle.LETTER.getHeight()));
        doc.addPage(page);
        PDPageContentStream cs = new PDPageContentStream(doc, page);

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Header
        cs.beginText(); cs.setFont(PDType1Font.TIMES_BOLD, 16);
        cs.newLineAtOffset(200, 740); cs.showText("Appointment Report Analysis"); cs.endText();

        // Address + Generated stacked
        final float addrX = 450f, addrY = 742f, leading = 14f;
        cs.beginText(); cs.setFont(PDType1Font.TIMES_ROMAN, 10);
        cs.newLineAtOffset(addrX, addrY);
        String[] addr = {
            "Jalan Teknologi 5,",
            "Taman Teknologi Malaysia,",
            "57000 Kuala Lumpur,",
            "Wilayah Persekutuan Kuala Lumpur"
        };
        for (String s : addr) { cs.showText(s); cs.newLineAtOffset(0, -leading); }
        cs.showText("Generated: " + today);
        cs.endText();

        float headerBottomY = addrY - leading * (addr.length + 1);
        float sepY = headerBottomY - 8f;

        // Separator
        cs.setStrokingColor(Color.BLACK); cs.setLineWidth(1f);
        cs.moveTo(30, sepY); cs.lineTo(582, sepY); cs.stroke();

        // Table top
        float y = sepY - 20f, rowH = 16f;
        float xDt=40, xTm=110, xId=160, xPatient=220, xDoc=320, xStatus=520;

        // table headers
        cs.beginText(); cs.setFont(PDType1Font.TIMES_BOLD, 10);
        cs.newLineAtOffset(xDt, y); cs.showText("Date"); cs.endText();
        cs.beginText(); cs.newLineAtOffset(xTm, y); cs.showText("Time"); cs.endText();
        cs.beginText(); cs.newLineAtOffset(xId, y); cs.showText("Appt"); cs.endText();
        cs.beginText(); cs.newLineAtOffset(xPatient, y); cs.showText("Patient Email"); cs.endText();
        cs.beginText(); cs.newLineAtOffset(xDoc, y); cs.showText("Doctor"); cs.endText();
        cs.beginText(); cs.newLineAtOffset(xStatus, y); cs.showText("Status"); cs.endText();

        y -= 8;
        cs.setFont(PDType1Font.TIMES_ROMAN, 10);
        for (ApptRec a : appts) {
            y -= rowH; if (y < 60) break;
            cs.beginText(); cs.newLineAtOffset(xDt, y); cs.showText(safe(a.date)); cs.endText();
            cs.beginText(); cs.newLineAtOffset(xTm, y); cs.showText(safe(a.time)); cs.endText();
            cs.beginText(); cs.newLineAtOffset(xId, y); cs.showText(safe(a.id)); cs.endText();
            cs.beginText(); cs.newLineAtOffset(xPatient, y); cs.showText(safe(a.email)); cs.endText();
            cs.beginText(); cs.newLineAtOffset(xDoc, y); cs.showText(safe(a.doctorName)); cs.endText();
            cs.beginText(); cs.newLineAtOffset(xStatus, y); cs.showText(safe(a.status)); cs.endText();
        }

        // summary
        y -= 20;
        cs.moveTo(30, y); cs.lineTo(582, y); cs.stroke();
        y -= 16;

        cs.beginText(); cs.setFont(PDType1Font.TIMES_BOLD, 12);
        cs.newLineAtOffset(40, y); cs.showText("Summary"); cs.endText();

        float leftX  = 40f;
        float rightX = 300f;

        // headings
        float colTop = y - 16;
        cs.beginText(); cs.setFont(PDType1Font.TIMES_BOLD, 11);
        cs.newLineAtOffset(leftX,  colTop);  cs.showText("By Doctor"); cs.endText();
        cs.beginText(); cs.setFont(PDType1Font.TIMES_BOLD, 11);
        cs.newLineAtOffset(rightX, colTop);  cs.showText("By Status"); cs.endText();

        // print rows in parallel so they align
        List<Map.Entry<String,Integer>> left  = new ArrayList<>(byDoctor.entrySet());
        List<Map.Entry<String,Integer>> right = new ArrayList<>(byStatus.entrySet());
        int maxRows = Math.max(left.size(), right.size());

        cs.setFont(PDType1Font.TIMES_ROMAN, 10);
        for (int i = 0; i < maxRows; i++) {
            float yRow = colTop - rowH * (i + 1);
            if (yRow < 40) break;

            if (i < left.size()) {
                var e = left.get(i);
                cs.beginText(); cs.newLineAtOffset(leftX, yRow);
                cs.showText(safe(e.getKey()) + " : " + e.getValue()); cs.endText();
            }
            if (i < right.size()) {
                var e = right.get(i);
                cs.beginText(); cs.newLineAtOffset(rightX, yRow);
                cs.showText(safe(e.getKey()) + " : " + e.getValue()); cs.endText();
            }
        }
        cs.close();                                   // finish drawing
        File out = ensureParentDirs(filePath);        // make sure folder exists
        doc.save(out);                                // write the PDF file
        doc.close();                                  // free resources
        tryOpen(out);                                 // (optional) open it
    }

    /** Feedback/Comments report (uses comments.txt). */
    public static void generateFeedbackReport(String filePath) throws IOException {
        List<CommentRec> comments = loadComments();

        int toDoctors = 0, toStaff = 0;
        for (CommentRec c : comments) {
            if (c.recipientId.startsWith("D")) toDoctors++;
            else if (c.recipientId.startsWith("S")) toStaff++;
        }

        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(new PDRectangle(PDRectangle.LETTER.getWidth(), PDRectangle.LETTER.getHeight()));
        doc.addPage(page);
        PDPageContentStream cs = new PDPageContentStream(doc, page);

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Header
        cs.beginText(); cs.setFont(PDType1Font.TIMES_BOLD, 16);
        cs.newLineAtOffset(220, 740); cs.showText("Hospital Feedback Report"); cs.endText();

        // Address + Generated stacked
        final float addrX = 450f, addrY = 742f, leading = 14f;
        cs.beginText(); cs.setFont(PDType1Font.TIMES_ROMAN, 10);
        cs.newLineAtOffset(addrX, addrY);
        String[] addr = {
            "Jalan Teknologi 5,",
            "Taman Teknologi Malaysia,",
            "57000 Kuala Lumpur,",
            "Wilayah Persekutuan Kuala Lumpur"
        };
        for (String s : addr) { cs.showText(s); cs.newLineAtOffset(0, -leading); }
        cs.showText("Generated: " + today);
        cs.endText();

        float headerBottomY = addrY - leading * (addr.length + 1);
        float sepY = headerBottomY - 8f;

        // Separator
        cs.setStrokingColor(Color.BLACK); cs.setLineWidth(1f);
        cs.moveTo(30, sepY); cs.lineTo(582, sepY); cs.stroke();

        // Table top
        float y = sepY - 20f, rowH = 14f;
        float xDT=40, xCust=140, xTo=210, xAppt=300, xTxt=360;

        // table headers
        cs.beginText(); cs.setFont(PDType1Font.TIMES_BOLD, 10);
        cs.newLineAtOffset(xDT, y); cs.showText("Date & Time"); cs.endText();
        cs.beginText(); cs.newLineAtOffset(xCust, y); cs.showText("Customer"); cs.endText();
        cs.beginText(); cs.newLineAtOffset(xTo, y); cs.showText("To"); cs.endText();
        cs.beginText(); cs.newLineAtOffset(xAppt, y); cs.showText("Appt"); cs.endText();
        cs.beginText(); cs.newLineAtOffset(xTxt, y); cs.showText("Comment"); cs.endText();

        y -= 8;
        cs.setFont(PDType1Font.TIMES_ROMAN, 10);
        for (CommentRec c : comments) {
            List<String> lines = wrap(c.text, 50);
            y -= rowH; if (y < 60) break;

            cs.beginText(); cs.newLineAtOffset(xDT, y);   cs.showText(safe(c.dateTime));   cs.endText();
            cs.beginText(); cs.newLineAtOffset(xCust, y); cs.showText(safe(c.customerId)); cs.endText();
            cs.beginText(); cs.newLineAtOffset(xTo, y);   cs.showText(safe(c.recipientId));cs.endText();
            cs.beginText(); cs.newLineAtOffset(xAppt, y); cs.showText(safe(c.apptId));     cs.endText();
            cs.beginText(); cs.newLineAtOffset(xTxt, y);  cs.showText(lines.get(0));       cs.endText();

            for (int i=1;i<lines.size();i++) {
                y -= rowH; if (y < 60) break;
                cs.beginText(); cs.newLineAtOffset(xTxt, y); cs.showText(lines.get(i)); cs.endText();
            }
        }

        // Summary
        y -= 20;
        cs.moveTo(30, y); cs.lineTo(582, y); cs.stroke();
        y -= 16;

        cs.beginText(); cs.setFont(PDType1Font.TIMES_BOLD, 12);
        cs.newLineAtOffset(40, y); cs.showText("Summary"); cs.endText();

        y -= 16;
        cs.beginText(); cs.setFont(PDType1Font.TIMES_ROMAN, 10);
        cs.newLineAtOffset(40, y); cs.showText("Total Feedback: " + comments.size()); cs.endText();
        y -= 14;
        cs.beginText(); cs.newLineAtOffset(40, y); cs.showText("To Doctors: " + toDoctors); cs.endText();
        y -= 14;
        cs.beginText(); cs.newLineAtOffset(40, y); cs.showText("To Staff   : " + toStaff); cs.endText();

        cs.close();
        File out = ensureParentDirs(filePath);
        doc.save(out);
        doc.close();
        tryOpen(out);
    }
}
