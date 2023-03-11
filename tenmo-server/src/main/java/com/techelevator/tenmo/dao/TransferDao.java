package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    boolean sendMoney(int fromUserId, int toUserId, BigDecimal amount);

    List<Transfer> getAllTransfers();

    List<Transfer> getAllTransfersByUserId(int accountId);

}
