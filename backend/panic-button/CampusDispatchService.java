package backend.panicbutton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Microservicio Backend: Despacho y Telemetría de Emergencias del Campus ITTOL.
 * Gestiona el enrutamiento de alertas prioritarias en tiempo real y el enlace con seguridad.
 */
public class CampusDispatchService {
    public static class EmergencyEvent {
        public String alertId;
        public String studentId;
        public String studentCode;
        public double latitude;
        public double longitude;
        public String campusZone;
        public String level;
        public boolean silentMode;
        public String status;
        public Date timestamp;
    }

    private final Map<String, EmergencyEvent> activeEvents = new ConcurrentHashMap<>();

    public EmergencyEvent dispatchAlert(String studentId, String studentCode, double lat, double lng,
                                       String zone, String level, boolean silent) {
        EmergencyEvent event = new EmergencyEvent();
        event.alertId = "ALERTA-ITTOL-" + System.currentTimeMillis();
        event.studentId = studentId;
        event.studentCode = studentCode;
        event.latitude = lat;
        event.longitude = lng;
        event.campusZone = zone;
        event.level = level;
        event.silentMode = silent;
        event.status = "DISPATCHED_TO_PATROL";
        event.timestamp = new Date();

        activeEvents.put(event.alertId, event);
        System.out.println("🚨 [BACKEND DISPATCH] Alerta en campus ITTOL despachada a Caseta Sur: " + event.alertId);
        return event;
    }

    public boolean resolveAlert(String alertId, String resolutionNotes) {
        EmergencyEvent event = activeEvents.get(alertId);
        if (event != null) {
            event.status = "RESOLVED";
            return true;
        }
        return false;
    }

    public EmergencyEvent getEvent(String alertId) {
        return activeEvents.get(alertId);
    }

    public boolean updateStatus(String alertId, String newStatus) {
        EmergencyEvent event = activeEvents.get(alertId);
        if (event != null) {
            event.status = newStatus;
            return true;
        }
        return false;
    }

    public List<EmergencyEvent> getActiveEvents() {
        return new ArrayList<>(activeEvents.values());
    }
}

