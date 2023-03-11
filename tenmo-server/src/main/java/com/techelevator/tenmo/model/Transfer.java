package com.techelevator.tenmo.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class Transfer {
    @Min(value = 1, message = "Transfer ID should Be a Positive Number.")
    private int transferId;

    @Min(value = 1, message = "Transfer Type ID Should Be a Positive Number.")
    private int transferTypeId;

    @Min(value = 1, message = "Transfer Status ID Should Be a Positive Number.")
    private int transferStatusId;

    @Min(value = 1, message = "Account From Should Be a Positive Number.")
    private int accountFrom;

    @Min(value = 1, message = "Account To Should Be a Positive Number.")
    private int accountTo;

    @Positive(message = "The Amount Transferring Must Be > 0.")
    private BigDecimal transferAmount;

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }
}
