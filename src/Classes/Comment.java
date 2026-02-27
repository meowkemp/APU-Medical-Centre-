package Classes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Comment {
    private String id;
    private String fromUserId;
    private String toUserId;
    private String appointmentId;
    private LocalDate date;     // <-- add
    private LocalTime time;     // <-- add
    private String text;

    public Comment() { }

    public Comment(String id,
                   String fromUserId,
                   String toUserId,
                   String appointmentId,
                   LocalDate date,
                   LocalTime time,
                   String text) {
        this.id = id;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.appointmentId = appointmentId;
        this.date = date;
        this.time = time;
        this.text = text;
    }

    // getters
    public String getId() { return id; }
    public String getFromUserId() { return fromUserId; }
    public String getToUserId() { return toUserId; }
    public String getAppointmentId() { return appointmentId; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public String getText() { return text; }

    // setters
    public void setId(String v) { id = v; }
    public void setFromUserId(String v) { fromUserId = v; }
    public void setToUserId(String v) { toUserId = v; }
    public void setAppointmentId(String v) { appointmentId = v; }
    public void setDate(LocalDate v) { date = v; }
    public void setTime(LocalTime v) { time = v; }
    public void setText(String v) { text = v; }

    // Optional: keep a createdAt-like string for UI convenience
    public String getCreatedAt() {
        if (date == null || time == null) return "";
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " +
               time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
