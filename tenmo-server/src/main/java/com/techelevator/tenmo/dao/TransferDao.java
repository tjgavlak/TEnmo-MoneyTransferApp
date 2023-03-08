package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.ArrayList;
import java.util.List;

public interface TransferDao {
List<Transfer> getAllTransfers();

List<Transfer> getAllTransfersByAccountId(int id);

Transfer getTransferById(int id);

Transfer createTransfer(Transfer transfer);

void deleteTransfer(int id);



}
