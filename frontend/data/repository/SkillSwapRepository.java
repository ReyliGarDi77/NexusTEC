package data.repository;

import data.model.SkillSwap;
import data.remote.ApiResponse;
import data.remote.NexusApiService;
import data.remote.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Repositorio de datos para el módulo Skill-Swap.
 * Implementa el patrón Repository para abstraer fuentes de datos locales y remotas.
 */
public class SkillSwapRepository {
    private static volatile SkillSwapRepository instance;
    private final NexusApiService apiService;
    private final Map<String, SkillSwap> localCache = new ConcurrentHashMap<>();

    public SkillSwapRepository() {
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    public static SkillSwapRepository getInstance() {
        if (instance == null) {
            synchronized (SkillSwapRepository.class) {
                if (instance == null) {
                    instance = new SkillSwapRepository();
                }
            }
        }
        return instance;
    }

    public List<SkillSwap> getAvailableSwaps(String category, String keyword) {
        if (apiService != null) {
            ApiResponse<List<SkillSwap>> response = apiService.getAvailableSkillSwaps(category, keyword);
            if (response != null && response.isSuccess() && response.getData() != null) {
                for (SkillSwap swap : response.getData()) {
                    localCache.put(swap.getSwapId(), swap);
                }
                return response.getData();
            }
        }
        return new ArrayList<>(localCache.values());
    }

    public SkillSwap getSwapById(String swapId) {
        if (localCache.containsKey(swapId)) {
            return localCache.get(swapId);
        }
        if (apiService != null) {
            ApiResponse<SkillSwap> response = apiService.getSkillSwapById(swapId);
            if (response != null && response.isSuccess()) {
                SkillSwap swap = response.getData();
                if (swap != null) {
                    localCache.put(swap.getSwapId(), swap);
                    return swap;
                }
            }
        }
        return null;
    }

    public SkillSwap createSwap(SkillSwap skillSwap) {
        if (skillSwap == null) return null;
        if (skillSwap.getSwapId() == null || skillSwap.getSwapId().isEmpty()) {
            skillSwap.setSwapId("swap_" + System.currentTimeMillis());
        }
        localCache.put(skillSwap.getSwapId(), skillSwap);

        if (apiService != null) {
            ApiResponse<SkillSwap> response = apiService.createSkillSwap(skillSwap);
            if (response != null && response.isSuccess() && response.getData() != null) {
                localCache.put(response.getData().getSwapId(), response.getData());
                return response.getData();
            }
        }
        return skillSwap;
    }

    public boolean updateStatus(String swapId, SkillSwap.SwapStatus newStatus) {
        SkillSwap swap = localCache.get(swapId);
        if (swap != null) {
            swap.setStatus(newStatus);
            if (apiService != null) {
                apiService.updateSkillSwapStatus(swapId, newStatus.name());
            }
            return true;
        }
        return false;
    }

    public void clearCache() {
        localCache.clear();
    }
}

