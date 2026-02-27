package Classes;

public class Doctor extends Users implements Billable {
    private String speciality;
    private String roomNo;

    public Doctor() { super(); setRole(Role.DOCTOR); }
    public Doctor(String id, String name, String email, String phone,
                  String speciality, String roomNo) {
        super(id, name, email, phone, Role.DOCTOR);
        this.speciality = speciality;
        this.roomNo = roomNo;
    }

    public String getSpeciality() { return speciality; }
    public void setSpeciality(String v) { this.speciality = v; }
    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String v) { this.roomNo = v; }

    @Override
    public void updateProfile(ProfileUpdate p) {
        if (p == null) return;
        if (!Users.isBlank(p.name))        setName(p.name);
        if (!Users.isBlank(p.phone))       setPhone(p.phone);
        if (!Users.isBlank(p.email))       setEmail(p.email);
        if (!Users.isBlank(p.password))    setPassword(p.password);
        if (!Users.isBlank(p.speciality))  setSpeciality(p.speciality);
        if (!Users.isBlank(p.roomNo))      setRoomNo(p.roomNo);
    }

    @Override
    public Charge createCharge(Appointment appt, double amount, String description) {
    if (appt == null || appt.getAppointmentId() == null || appt.getAppointmentId().isBlank()) {
        throw new IllegalArgumentException("Appointment ID is required");
    }
    if (amount < 0) {
        throw new IllegalArgumentException("Amount must be non-negative");
    }

    java.nio.file.Path file = java.nio.file.Paths.get("data/charges.txt");
        try {
            // make sure folder/file exist
            java.nio.file.Files.createDirectories(file.getParent());
            if (!java.nio.file.Files.exists(file)) java.nio.file.Files.createFile(file);

            // find next charge id: CH0001, CH0002, ...
            int max = 0;
            for (String line : java.nio.file.Files.readAllLines(file, java.nio.charset.StandardCharsets.UTF_8)) {
                if (line == null || line.isBlank()) continue;
                String[] p = line.split("\\|", -1);
                if (p.length > 0 && p[0].startsWith("CH")) {
                    try { max = Math.max(max, Integer.parseInt(p[0].substring(2))); }
                    catch (NumberFormatException ignored) {}
                }
            }
            String newId = String.format("CH%04d", max + 1);

            // sanitize description for pipe-delimited file
            String desc = (description == null ? "" : description.replace("|", "/").replace("\n", " ").trim());

            // row format expected by your reports:
            // id|apptId|amount|description|paid|doctorId
            String row = String.join("|",
                    newId,
                    appt.getAppointmentId(),
                    String.format(java.util.Locale.US, "%.2f", amount),
                    desc,
                    "false",
                    this.getId()
            );

            try (java.io.BufferedWriter w = java.nio.file.Files.newBufferedWriter(
                    file, java.nio.charset.StandardCharsets.UTF_8,
                    java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.APPEND)) {
                if (java.nio.file.Files.size(file) > 0) w.newLine();
                w.write(row);
            }

            // build and return a Charge object
            Charge c = new Charge();
            c.setId(newId);
            c.setAppointmentId(appt.getAppointmentId());
            c.setAmount(amount);
            c.setDescription(desc);
            c.setPaid(false);
            c.setEnteredByDoctorId(this.getId());
            return c;

        } catch (java.io.IOException ioe) {
            // keep Billable signature clean; surface as runtime so your UI catch(Exception) shows it
            throw new RuntimeException("Unable to write data/charges.txt", ioe);
        }
    }

    @Override
    public String getDisplayLabel() {
        return getName() + (speciality == null || speciality.isEmpty() ? "" : " (" + speciality + ")");
    }
}
