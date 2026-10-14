package data.model;

import java.io.Serializable;
import java.util.Date;

/**
 * POJO para representar una Alerta de Emergencia activada mediante el Botón de Pánico.
 */
public class PanicAlert implements Serializable {
    public enum AlertLevel {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }

    public enum AlertStatus {
        TRIGGERED,
        DISPATCHED,
        ACKNOWLEDGED,
        RESPONDING,
        RESOLVED,
        FALSE_ALARM
    }

    private String alertId;
    private String studentId;
    private String studentName;
    private String studentCode;
    private double latitude;
    private double longitude;
    private String campusBuildingZone;
    private AlertLevel level;
    private AlertStatus status;
    private boolean silentMode;
    private String emergencyType; // "MEDICA", "SEGURIDAD", "ACOSO", "OTRO"
    private String notes;
    private Date triggeredAt;
    private Date resolvedAt;

    public PanicAlert() {
        this.level = AlertLevel.HIGH;
        this.status = AlertStatus.TRIGGERED;
        this.triggeredAt = new Date();
    }

    public PanicAlert(String alertId, String studentId, String studentName,
                      String studentCode, double latitude, double longitude,
                      String campusBuildingZone, AlertLevel level, String emergencyType) {
        this.alertId = alertId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentCode = studentCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.campusBuildingZone = campusBuildingZone;
        this.level = level != null ? level : AlertLevel.HIGH;
        this.status = AlertStatus.TRIGGERED;
        this.emergencyType = emergencyType;
        this.triggeredAt = new Date();
    }

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCampusBuildingZone() {
        return campusBuildingZone;
    }

    public void setCampusBuildingZone(String campusBuildingZone) {
        this.campusBuildingZone = campusBuildingZone;
    }

    public AlertLevel getLevel() {
        return level;
    }

    public void setLevel(AlertLevel level) {
        this.level = level;
    }

    public AlertStatus getStatus() {
        return status;
    }

    public void setStatus(AlertStatus status) {
        this.status = status;
    }

    public boolean isSilentMode() {
        return silentMode;
    }

    public void setSilentMode(boolean silentMode) {
        this.silentMode = silentMode;
    }

    public String getEmergencyType() {
        return emergencyType;
    }

    public void setEmergencyType(String emergencyType) {
        this.emergencyType = emergencyType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getTriggeredAt() {
        return triggeredAt;
    }

    public void setTriggeredAt(Date triggeredAt) {
        this.triggeredAt = triggeredAt;
    }

    public Date getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(Date resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    @Override
    public String toString() {
        return "PanicAlert{" +
                "alertId='" + alertId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", campusBuildingZone='" + campusBuildingZone + '\'' +
                ", level=" + level +
                ", status=" + status +
                ", emergencyType='" + emergencyType + '\'' +
                '}';
    }
}

