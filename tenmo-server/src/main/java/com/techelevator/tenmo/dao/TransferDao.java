package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public interface TransferDao {
List<Transfer> getAllTransfers();

List<Transfer> getAllTransfersByUserId(int id);

Transfer getTransferById(int id);

Transfer createTransfer(Transfer transfer);

//void deleteTransfer(int id);

void sendTransfer(int sender, int receiver, BigDecimal amount);

void receiveTransfer(int sender, int receiver, BigDecimal amount);
}
