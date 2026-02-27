package Classes;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
public class Charge {
    // ===== Fields =====
    private String id;                  // CH0001...
    private String appointmentId;       // A0002...
    private double amount;              // 80.0
    private String description;         // "Consultation"
    private boolean paid;               // true/false
    private String enteredByDoctorId;   

    // ===== Constructors =====
    public Charge() { }

    public Charge(String id, String appointmentId, double amount,
                  String description, boolean paid, String enteredByDoctorId) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.description = description;
        this.paid = paid;
        this.enteredByDoctorId = enteredByDoctorId;
    }

    // ===== Getters/Setters =====
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }
    public String getEnteredByDoctorId() { return enteredByDoctorId; }
    public void setEnteredByDoctorId(String enteredByDoctorId) { this.enteredByDoctorId = enteredByDoctorId; }

    // ===== File utilities =====
    private static final Path FILE_PATH = Paths.get("data/charges.txt");

    /** Format: id|appointmentId|amount|description|paid|enteredByDoctorId */
    public String toLine() {
        String desc = (description == null) ? "" : description.replace("\n", " ");
        String id0  = (id == null) ? "" : id;
        String a0   = (appointmentId == null) ? "" : appointmentId;
        String amt0 = String.valueOf(amount);
        String p0   = String.valueOf(paid);
        String d0   = (enteredByDoctorId == null) ? "" : enteredByDoctorId;
        return String.join("|", id0, a0, amt0, desc, p0, d0);
    }

    public static Charge fromLine(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 6) throw new IllegalArgumentException("Invalid charge line: " + line);
        String id = p[0].trim();
        String appt = p[1].trim();
        double amt = Double.parseDouble(p[2].trim());
        String desc = p[3].trim();
        boolean paid = Boolean.parseBoolean(p[4].trim());
        String doc = p[5].trim();
        return new Charge(id, appt, amt, desc, paid, doc);
    }

    public void save() {
        try {
            if (Files.notExists(FILE_PATH)) Files.createFile(FILE_PATH);
            try (BufferedWriter bw = Files.newBufferedWriter(
                    FILE_PATH, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
                bw.write(this.toLine());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving charge", e);
        }
    }

    /** Read all charges. */
    public static List<Charge> readAll() {
        if (Files.notExists(FILE_PATH)) return new ArrayList<>();
        try {
            return Files.readAllLines(FILE_PATH, StandardCharsets.UTF_8).stream()
                    .filter(s -> !s.isBlank())
                    .map(Charge::fromLine)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error reading charges", e);
        }
    }

    /** Get charges for a specific appointment. */
    public static List<Charge> byAppointment(String appointmentId) {
        return readAll().stream()
                .filter(c -> c.getAppointmentId().equalsIgnoreCase(appointmentId))
                .collect(Collectors.toList());
    }

    /** Generate the next charge ID (CH000X). */
    public static String nextId() {
        int max = 0;
        for (Charge c : readAll()) {
            try {
                max = Math.max(max, Integer.parseInt(c.getId().substring(2)));
            } catch (Exception ignored) {}
        }
        return String.format("CH%04d", max + 1);
    }

    

}

