package Classes;


public enum AppointmentStatus {
    /** Created (by customer or staff), not yet assigned to a doctor */
    PENDING,

    /** Doctor is assigned (usually by staff) */
    ASSIGNED,

    /** Consultation done */
    COMPLETED,

    /** Cancelled by staff or customer */
    CANCELLED;

    /*final state? */
    public boolean isTerminal() {
        return this == COMPLETED || this == CANCELLED;
    }

    /** Basic transition validation to avoid illegal jumps. */
    public boolean canTransitionTo(AppointmentStatus next) {
        if (this == next) return true;
        switch (this) {
            case PENDING:
                // pending -> assigned | cancelled
                return next == ASSIGNED || next == CANCELLED;
            case ASSIGNED:
                // assigned -> completed | cancelled
                return next == COMPLETED || next == CANCELLED;
            case COMPLETED:
            case CANCELLED:
                // terminal
                return false;
            default:
                return false;
        }
    }
}


