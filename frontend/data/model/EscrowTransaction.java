package data.model;

import java.io.Serializable;
import java.util.Date;

/**
 * POJO para transacciones bajo custodia (Escrow) de Créditos Halcón.
 */
public class EscrowTransaction implements Serializable {
    public enum TransactionStatus {
        PENDING_DEPOSIT,
        IN_ESCROW_HOLD,
        RELEASED,
        DISPUTED,
        REFUNDED
    }

    private String transactionId;
    private String relatedSwapId;
    private String payerStudentId;
    private String payeeStudentId;
    private double creditsAmount;
    private TransactionStatus status;
    private String securityHash;
    private String disputeReason;
    private Date createdAt;
    private Date releasedAt;

    public EscrowTransaction() {
        this.status = TransactionStatus.PENDING_DEPOSIT;
        this.createdAt = new Date();
    }

    public EscrowTransaction(String transactionId, String relatedSwapId,
                             String payerStudentId, String payeeStudentId,
                             double creditsAmount, TransactionStatus status) {
        this.transactionId = transactionId;
        this.relatedSwapId = relatedSwapId;
        this.payerStudentId = payerStudentId;
        this.payeeStudentId = payeeStudentId;
        this.creditsAmount = creditsAmount;
        this.status = status != null ? status : TransactionStatus.PENDING_DEPOSIT;
        this.createdAt = new Date();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getRelatedSwapId() {
        return relatedSwapId;
    }

    public void setRelatedSwapId(String relatedSwapId) {
        this.relatedSwapId = relatedSwapId;
    }

    public String getPayerStudentId() {
        return payerStudentId;
    }

    public void setPayerStudentId(String payerStudentId) {
        this.payerStudentId = payerStudentId;
    }

    public String getPayeeStudentId() {
        return payeeStudentId;
    }

    public void setPayeeStudentId(String payeeStudentId) {
        this.payeeStudentId = payeeStudentId;
    }

    public double getCreditsAmount() {
        return creditsAmount;
    }

    public void setCreditsAmount(double creditsAmount) {
        this.creditsAmount = creditsAmount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getSecurityHash() {
        return securityHash;
    }

    public void setSecurityHash(String securityHash) {
        this.securityHash = securityHash;
    }

    public String getDisputeReason() {
        return disputeReason;
    }

    public void setDisputeReason(String disputeReason) {
        this.disputeReason = disputeReason;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getReleasedAt() {
        return releasedAt;
    }

    public void setReleasedAt(Date releasedAt) {
        this.releasedAt = releasedAt;
    }

    @Override
    public String toString() {
        return "EscrowTransaction{" +
                "transactionId='" + transactionId + '\'' +
                ", payerStudentId='" + payerStudentId + '\'' +
                ", payeeStudentId='" + payeeStudentId + '\'' +
                ", creditsAmount=" + creditsAmount +
                ", status=" + status +
                '}';
    }
}

