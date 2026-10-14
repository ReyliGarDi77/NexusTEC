package data.model;

import java.io.Serializable;
import java.util.Date;

/**
 * POJO para representar un intercambio de habilidades académicas o técnicas (Skill-Swap).
 */
public class SkillSwap implements Serializable {
    public enum SwapStatus {
        DRAFT,
        PENDING,
        ACCEPTED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }

    private String swapId;
    private String requesterStudentId;
    private String providerStudentId;
    private String offeredSkill;
    private String requestedSkill;
    private String category;
    private String description;
    private int estimatedHours;
    private double agreedHalconCredits;
    private SwapStatus status;
    private Date scheduledDate;
    private String meetingLocation;
    private Date createdAt;

    public SkillSwap() {
        this.status = SwapStatus.PENDING;
        this.createdAt = new Date();
    }

    public SkillSwap(String swapId, String requesterStudentId, String providerStudentId,
                     String offeredSkill, String requestedSkill, String category,
                     String description, int estimatedHours, double agreedHalconCredits,
                     SwapStatus status, Date scheduledDate, String meetingLocation) {
        this.swapId = swapId;
        this.requesterStudentId = requesterStudentId;
        this.providerStudentId = providerStudentId;
        this.offeredSkill = offeredSkill;
        this.requestedSkill = requestedSkill;
        this.category = category;
        this.description = description;
        this.estimatedHours = estimatedHours;
        this.agreedHalconCredits = agreedHalconCredits;
        this.status = status != null ? status : SwapStatus.PENDING;
        this.scheduledDate = scheduledDate;
        this.meetingLocation = meetingLocation;
        this.createdAt = new Date();
    }

    public String getSwapId() {
        return swapId;
    }

    public void setSwapId(String swapId) {
        this.swapId = swapId;
    }

    public String getRequesterStudentId() {
        return requesterStudentId;
    }

    public void setRequesterStudentId(String requesterStudentId) {
        this.requesterStudentId = requesterStudentId;
    }

    public String getProviderStudentId() {
        return providerStudentId;
    }

    public void setProviderStudentId(String providerStudentId) {
        this.providerStudentId = providerStudentId;
    }

    public String getOfferedSkill() {
        return offeredSkill;
    }

    public void setOfferedSkill(String offeredSkill) {
        this.offeredSkill = offeredSkill;
    }

    public String getRequestedSkill() {
        return requestedSkill;
    }

    public void setRequestedSkill(String requestedSkill) {
        this.requestedSkill = requestedSkill;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(int estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public double getAgreedHalconCredits() {
        return agreedHalconCredits;
    }

    public void setAgreedHalconCredits(double agreedHalconCredits) {
        this.agreedHalconCredits = agreedHalconCredits;
    }

    public SwapStatus getStatus() {
        return status;
    }

    public void setStatus(SwapStatus status) {
        this.status = status;
    }

    public Date getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getMeetingLocation() {
        return meetingLocation;
    }

    public void setMeetingLocation(String meetingLocation) {
        this.meetingLocation = meetingLocation;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "SkillSwap{" +
                "swapId='" + swapId + '\'' +
                ", offeredSkill='" + offeredSkill + '\'' +
                ", requestedSkill='" + requestedSkill + '\'' +
                ", status=" + status +
                ", agreedHalconCredits=" + agreedHalconCredits +
                '}';
    }
}

