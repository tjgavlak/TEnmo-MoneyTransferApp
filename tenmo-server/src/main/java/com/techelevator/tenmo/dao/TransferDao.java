package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    List<Transfer> showTransferHistory(int userId);

    boolean sendTransfer(int fromUserId, int toUserId, BigDecimal amount);

    List<Transfer> getTransfersByUserId(int userId);

}
