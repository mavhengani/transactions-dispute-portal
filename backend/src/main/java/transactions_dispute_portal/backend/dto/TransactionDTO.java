package transactions_dispute_portal.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDTO {

    private Long id;

    private String merchantName;

    private BigDecimal amount;

    private String currency;

    private String transactionType;

    private String status;

    private String referenceNumber;

    private String paymentMethod;

    private String location;

    private Boolean disputed;

    private String disputeReason;

    private LocalDateTime transactionDate;

    // MASKED CARD NUMBER ONLY
    private String maskedCardNumber;

    public TransactionDTO() {
    }

    public TransactionDTO(
            Long id,
            String merchantName,
            BigDecimal amount,
            String currency,
            String transactionType,
            String status,
            String referenceNumber,
            String paymentMethod,
            String location,
            Boolean disputed,
            String disputeReason,
            LocalDateTime transactionDate,
            String maskedCardNumber
    ) {

        this.id = id;
        this.merchantName = merchantName;
        this.amount = amount;
        this.currency = currency;
        this.transactionType = transactionType;
        this.status = status;
        this.referenceNumber = referenceNumber;
        this.paymentMethod = paymentMethod;
        this.location = location;
        this.disputed = disputed;
        this.disputeReason = disputeReason;
        this.transactionDate = transactionDate;
        this.maskedCardNumber = maskedCardNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getDisputed() {
        return disputed;
    }

    public void setDisputed(Boolean disputed) {
        this.disputed = disputed;
    }

    public String getDisputeReason() {
        return disputeReason;
    }

    public void setDisputeReason(String disputeReason) {
        this.disputeReason = disputeReason;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(
            LocalDateTime transactionDate
    ) {
        this.transactionDate = transactionDate;
    }

    public String getMaskedCardNumber() {
        return maskedCardNumber;
    }

    public void setMaskedCardNumber(
            String maskedCardNumber
    ) {
        this.maskedCardNumber = maskedCardNumber;
    }
}