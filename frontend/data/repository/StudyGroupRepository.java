package data.repository;

import data.model.StudyGroup;
import data.remote.ApiResponse;
import data.remote.NexusApiService;
import data.remote.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Repositorio de datos para el módulo Flash Study Groups.
 */
public class StudyGroupRepository {
    private static volatile StudyGroupRepository instance;
    private final NexusApiService apiService;
    private final Map<String, StudyGroup> groupsCache = new ConcurrentHashMap<>();

    public StudyGroupRepository() {
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    public static StudyGroupRepository getInstance() {
        if (instance == null) {
            synchronized (StudyGroupRepository.class) {
                if (instance == null) {
                    instance = new StudyGroupRepository();
                }
            }
        }
        return instance;
    }

    public List<StudyGroup> getOpenGroups(String subjectFilter) {
        if (apiService != null) {
            ApiResponse<List<StudyGroup>> response = apiService.getOpenStudyGroups(subjectFilter);
            if (response != null && response.isSuccess() && response.getData() != null) {
                for (StudyGroup g : response.getData()) {
                    groupsCache.put(g.getGroupId(), g);
                }
                return response.getData();
            }
        }
        List<StudyGroup> results = new ArrayList<>();
        for (StudyGroup g : groupsCache.values()) {
            if (subjectFilter == null || subjectFilter.isEmpty() || 
                g.getSubjectName().toLowerCase().contains(subjectFilter.toLowerCase())) {
                results.add(g);
            }
        }
        return results;
    }

    public StudyGroup createGroup(StudyGroup group) {
        if (group == null) return null;
        if (group.getGroupId() == null || group.getGroupId().isEmpty()) {
            group.setGroupId("group_" + System.currentTimeMillis());
        }
        groupsCache.put(group.getGroupId(), group);

        if (apiService != null) {
            ApiResponse<StudyGroup> response = apiService.createStudyGroup(group);
            if (response != null && response.isSuccess() && response.getData() != null) {
                groupsCache.put(response.getData().getGroupId(), response.getData());
                return response.getData();
            }
        }
        return group;
    }

    public boolean joinGroup(String groupId, String studentId) {
        StudyGroup group = groupsCache.get(groupId);
        if (group != null) {
            boolean added = group.addMember(studentId);
            if (added && apiService != null) {
                apiService.joinStudyGroup(groupId, studentId);
            }
            return added;
        }
        return false;
    }

    public StudyGroup getGroupById(String groupId) {
        return groupsCache.get(groupId);
    }
}
