package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferType {
    @JsonProperty("transferTypeID")
    private long  transferTypeId;
    private String transferTypeDesc;

    public TransferType(long  transferTypeId, String transferTypeDesc) {
        this.transferTypeId = transferTypeId;
        this.transferTypeDesc = transferTypeDesc;
    }

    public long  getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(long  transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public String getTransferTypeDesc() {
        return transferTypeDesc;
    }

    public void setTransferTypeDesc(String transferTypeDesc) {
        this.transferTypeDesc = transferTypeDesc;
    }




    @Override
    public String toString() {
        return "TransferType{" +
                "transferTypeId=" + transferTypeId +
                ", transferTypeDesc='" + transferTypeDesc + '\'' +
                '}';
    }
}
