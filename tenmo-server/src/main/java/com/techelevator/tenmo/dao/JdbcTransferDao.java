package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

 private final JdbcTemplate jdbcTemplate;
 private final int SEND_TYPE_ID = 2;
 private final int TRANSFER_STATUS_ID = 2;

 public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
  this.jdbcTemplate = jdbcTemplate;
 }

 @Override
 public List<Transfer> showTransferHistory(int userId) {
  String sql= "SELECT transfer_id, transfer_type_desc, transfer_status_desc, account_to, account_from, amount " +
          "FROM transfer " +
          "JOIN transfer_type ON transfer.transfer_type_id = transfer_type.transfer_type_id " +
          "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id " +
          "JOIN account as af ON transfer.account_from = af.account_id " +
          "JOIN account as at ON transfer.account_to = at.account_id " +
          "WHERE af.user_id = ? OR at.user_id = ?;";
  SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
  List<Transfer> history = new ArrayList<>();
  while (results.next()){
   history.add(mapRowToTransfer(results, userId));
  }
  return history;
 }

 @Override
 public boolean sendTransfer(int fromUserId, int toUserId, BigDecimal amount) {
  String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, " +
          "account_from, account_to, amount) " +
          "VALUES ( ?, ?, (SELECT account_id FROM account WHERE user_id = ?), " +
          "(SELECT account_id FROM account WHERE user_id = ?), ?); ";
  try {
   int output = jdbcTemplate.update(sql, SEND_TYPE_ID, TRANSFER_STATUS_ID, fromUserId, toUserId, amount);
   System.out.println("transfer rows affected: " + output);
  } catch (DataAccessException e) {
   e.printStackTrace();
   return false;
  }
  return true;
 }

 @Override
 public List<Transfer> getTransfersByUserId(int userId) {
  return null;
 }

 public Transfer mapRowToTransfer(SqlRowSet results, int userId){
  Transfer transfer = new Transfer();
  transfer.setTransferId(results.getInt("transfer_id"));
  transfer.setTransferTypeId(results.getInt("transfer_type_id"));
  transfer.setTransferStatusId(results.getInt("transfer_status_id"));
  transfer.setAccountFromUsername(results.getString("account_from"));
  transfer.setAccountToUsername(results.getString("account_to"));
  transfer.setTransferAmount(results.getBigDecimal("amount"));
  return transfer;
 }
}

/* @Override
 public void updateTransfer (Transfer transfer) {
  String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?;";
  jdbcTemplate.update(sql, transfer.getTransferStatusId(), transfer.getTransferId());
 }*/




 /*@Override
 public Transfer createTransfer(Transfer transfer) {
  String sql = "INSERT INTO transfer (transfer_type_id, " +
          "transfer_status_id, account_from, " +
          "account_to, amount) " +
          "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";

  Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
          transfer.getTransferTypeId(),
          transfer.getTransferStatusId(),
          transfer.getAccountFromUsername(),
          transfer.getAccountToUsername(),
          transfer.getTransferAmount());
  return getTransferById(newId);
 }*/





 /*@Override
 public void updateSenderBalance(int transferId, Transfer transfer) {
  String sql = "UPDATE account SET balance = (SELECT SUM (balance - transfer.amount) " +
          "FROM account JOIN Transfer on account.account_id = transfer.account_from WHERE transfer_id = ?) " +
          "WHERE user_id = ?;";
  try{
   jdbcTemplate.update(sql, transferId, transfer.getFromUserId());
  } catch (DataAccessException | ResourceAccessException e) {
   e.printStackTrace();
  }
 }*/