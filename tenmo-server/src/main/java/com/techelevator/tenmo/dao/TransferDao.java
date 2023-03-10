package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public interface TransferDao {

    int sendTransfer(Transfer transfer);

    void updateSenderBalance(int transferId, Transfer transfer);

    void updateReceiverBalance(int transferId, Transfer transfer);

    void updateTransfer (Transfer transfer);

    List<Transfer> getUserTransfers(User user);

}
