package Classes;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Feedback {
    private String id;
    private String doctorId;
    private String customerId;     // <-- NEW
    private String appointmentId;
    private String date;           // <-- NEW (yyyy-MM-dd)
    private String time;           // <-- NEW (HH:mm)
    private String notes;

    public Feedback() {}

    public Feedback(String id, String doctorId, String customerId,
                    String appointmentId, String date, String time, String notes) {
        this.id = id; this.doctorId = doctorId; this.customerId = customerId;
        this.appointmentId = appointmentId; this.date = date; this.time = time; this.notes = notes;
    }

    // Getters/Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    private static final Path FILE_PATH = Paths.get("data/feedbacks.txt");

    private static String nz(String s) { return s == null ? "" : s.trim(); }
    private static String clean(String s) {
        if (s == null) return "";
        return s.replace("|","/").replace("\r"," ").replace("\n"," ").trim();
    }

    public String toLine() {
        return String.join("|",
                nz(id), nz(doctorId), nz(customerId), nz(appointmentId),
                nz(date), nz(time), clean(notes));
    }

    public static Feedback fromLine(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length >= 7) {
            return new Feedback(nz(p[0]), nz(p[1]), nz(p[2]), nz(p[3]), nz(p[4]), nz(p[5]), nz(p[6]));
        } else if (p.length == 5) {
            String createdAt = nz(p[3]);
            String date = createdAt; String time = "";
            int sp = createdAt.indexOf(' ');
            if (sp > 0) { date = createdAt.substring(0, sp); time = createdAt.substring(sp + 1); }
            return new Feedback(nz(p[0]), nz(p[1]), "", nz(p[2]), date, time, nz(p[4]));
        } else {
            throw new IllegalArgumentException("Invalid feedback line: " + line);
        }
    }

    public void save() {
        try {
            if (Files.notExists(FILE_PATH)) Files.createFile(FILE_PATH);
            try (BufferedWriter bw = Files.newBufferedWriter(FILE_PATH, StandardCharsets.UTF_8,
                    StandardOpenOption.APPEND)) {
                bw.write(this.toLine());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving feedback", e);
        }
    }

    public static List<Feedback> readAll() {
        if (Files.notExists(FILE_PATH)) return new ArrayList<>();
        try {
            return Files.readAllLines(FILE_PATH, StandardCharsets.UTF_8).stream()
                    .filter(s -> !s.isBlank())
                    .map(Feedback::fromLine)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error reading feedbacks", e);
        }
    }

    public static List<Feedback> byAppointment(String appointmentId) {
        return readAll().stream()
                .filter(f -> f.getAppointmentId().equalsIgnoreCase(appointmentId))
                .collect(Collectors.toList());
    }

    public static String nextId() {
        int max = 0;
        for (Feedback f : readAll()) {
            try { max = Math.max(max, Integer.parseInt(f.getId().substring(2))); }
            catch (Exception ignored) {}
        }
        return String.format("FB%04d", max + 1);
    }
}
