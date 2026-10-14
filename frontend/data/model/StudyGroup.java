package data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * POJO para representar un Grupo de Estudio Flash colaborativo.
 */
public class StudyGroup implements Serializable {
    public enum GroupStatus {
        OPEN,
        FULL,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }

    private String groupId;
    private String subjectName;
    private String topicDescription;
    private String creatorStudentId;
    private String creatorStudentName;
    private List<String> memberStudentIds;
    private int maxCapacity;
    private String meetingLocation; // Ej: "Biblioteca Cubículo 4", "Edificio F Sala 2"
    private Date scheduledTime;
    private int durationMinutes;
    private GroupStatus status;
    private Date createdAt;

    public StudyGroup() {
        this.memberStudentIds = new ArrayList<>();
        this.status = GroupStatus.OPEN;
        this.createdAt = new Date();
    }

    public StudyGroup(String groupId, String subjectName, String topicDescription,
                      String creatorStudentId, String creatorStudentName,
                      int maxCapacity, String meetingLocation, Date scheduledTime,
                      int durationMinutes) {
        this.groupId = groupId;
        this.subjectName = subjectName;
        this.topicDescription = topicDescription;
        this.creatorStudentId = creatorStudentId;
        this.creatorStudentName = creatorStudentName;
        this.memberStudentIds = new ArrayList<>();
        this.memberStudentIds.add(creatorStudentId);
        this.maxCapacity = maxCapacity;
        this.meetingLocation = meetingLocation;
        this.scheduledTime = scheduledTime;
        this.durationMinutes = durationMinutes;
        this.status = GroupStatus.OPEN;
        this.createdAt = new Date();
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }

    public String getCreatorStudentId() {
        return creatorStudentId;
    }

    public void setCreatorStudentId(String creatorStudentId) {
        this.creatorStudentId = creatorStudentId;
    }

    public String getCreatorStudentName() {
        return creatorStudentName;
    }

    public void setCreatorStudentName(String creatorStudentName) {
        this.creatorStudentName = creatorStudentName;
    }

    public List<String> getMemberStudentIds() {
        return memberStudentIds;
    }

    public void setMemberStudentIds(List<String> memberStudentIds) {
        this.memberStudentIds = memberStudentIds;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getMeetingLocation() {
        return meetingLocation;
    }

    public void setMeetingLocation(String meetingLocation) {
        this.meetingLocation = meetingLocation;
    }

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public GroupStatus getStatus() {
        return status;
    }

    public void setStatus(GroupStatus status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean addMember(String studentId) {
        if (memberStudentIds.size() < maxCapacity && !memberStudentIds.contains(studentId)) {
            memberStudentIds.add(studentId);
            if (memberStudentIds.size() >= maxCapacity) {
                this.status = GroupStatus.FULL;
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "StudyGroup{" +
                "groupId='" + groupId + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", maxCapacity=" + maxCapacity +
                ", currentMembers=" + (memberStudentIds != null ? memberStudentIds.size() : 0) +
                ", status=" + status +
                '}';
    }
}
