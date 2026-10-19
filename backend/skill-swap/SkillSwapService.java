package backend.skillswap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Microservicio Backend: Skill-Swap para el campus ITTOL.
 * Gestiona el catálogo de asesorías técnicas y emparejamiento de habilidades entre estudiantes.
 */
public class SkillSwapService {
    public static class SwapRecord {
        public String id;
        public String requesterCode;
        public String providerCode;
        public String offeredSkill;
        public String requestedSkill;
        public String careerCategory;
        public double halconCredits;
        public String status;
        public String campusLocation;
        public Date timestamp;
    }

    private final Map<String, SwapRecord> registry = new ConcurrentHashMap<>();

    public SkillSwapService() {
        seedInitialRecords();
    }

    private void seedInitialRecords() {
        SwapRecord s1 = new SwapRecord();
        s1.id = "SWAP-ITTOL-001";
        s1.requesterCode = "21280942";
        s1.offeredSkill = "Estructura de Datos en Java & Algoritmos";
        s1.requestedSkill = "Cálculo Integral & Ecuaciones Diferenciales";
        s1.careerCategory = "Ing. Sistemas";
        s1.halconCredits = 25.0;
        s1.status = "PENDING";
        s1.campusLocation = "Biblioteca Central ITTOL · Cubículo 3";
        s1.timestamp = new Date();
        registry.put(s1.id, s1);
    }

    public List<SwapRecord> listSwaps(String categoryFilter) {
        if (categoryFilter == null || categoryFilter.isEmpty() || "Todos".equalsIgnoreCase(categoryFilter)) {
            return new ArrayList<>(registry.values());
        }
        List<SwapRecord> filtered = new ArrayList<>();
        for (SwapRecord record : registry.values()) {
            if (record.careerCategory.toLowerCase().contains(categoryFilter.toLowerCase())) {
                filtered.add(record);
            }
        }
        return filtered;
    }

    public SwapRecord createSwap(SwapRecord record) {
        if (record.id == null || record.id.isEmpty()) {
            record.id = "SWAP-ITTOL-" + System.currentTimeMillis();
        }
        record.timestamp = new Date();
        registry.put(record.id, record);
        return record;
    }

    public SwapRecord getSwap(String id) {
        return registry.get(id);
    }

    public boolean updateStatus(String id, String newStatus) {
        SwapRecord record = registry.get(id);
        if (record != null) {
            record.status = newStatus;
            return true;
        }
        return false;
    }
}

