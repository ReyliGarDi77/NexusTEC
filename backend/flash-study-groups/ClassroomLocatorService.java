package backend.studygroups;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Microservicio Backend: Localizador de Aulas y Coordinador de Mesas de Estudio Flash en Campus ITTOL.
 */
public class ClassroomLocatorService {
    public static class StudyTable {
        public String tableId;
        public String subject;
        public String topic;
        public String hostStudent;
        public int capacity;
        public List<String> currentAttendees = new ArrayList<>();
        public String locationIttol;
        public Date scheduledTime;
    }

    private final Map<String, StudyTable> tables = new ConcurrentHashMap<>();

    public ClassroomLocatorService() {
        seedInitialTables();
    }

    private void seedInitialTables() {
        StudyTable t1 = new StudyTable();
        t1.tableId = "TABLE-LC1-01";
        t1.subject = "Arquitectura de Computadoras";
        t1.topic = "Microinstrucciones y pipeline examen departamental";
        t1.hostStudent = "Reyli García Díaz";
        t1.capacity = 5;
        t1.currentAttendees.add("21280942");
        t1.currentAttendees.add("21280455");
        t1.locationIttol = "Edificio F · Cubículo 2";
        t1.scheduledTime = new Date();
        tables.put(t1.tableId, t1);
    }

    public List<StudyTable> getOpenTables(String careerFilter) {
        return new ArrayList<>(tables.values());
    }

    public boolean joinTable(String tableId, String studentCode) {
        StudyTable table = tables.get(tableId);
        if (table != null && table.currentAttendees.size() < table.capacity) {
            table.currentAttendees.add(studentCode);
            return true;
        }
        return false;
    }
}

