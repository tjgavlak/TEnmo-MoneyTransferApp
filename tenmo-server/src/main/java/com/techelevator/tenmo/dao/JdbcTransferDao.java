package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

 private final JdbcTemplate jdbcTemplate;
 public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
  this.jdbcTemplate = jdbcTemplate;
 }

 @Override
 public List<Transfer> getUserTransfers(User user) {
   List<Transfer> transferList = new ArrayList<>();
   String sql = "SELECT transfer_id, transfer.transfer_type_id, transfer_type_desc, transfer.transfer_status_id, " +
           "z.username AS to_username, y.username AS from_username," +
           "f.user_id AS from_user_id, transfer_status_desc, x.user_id AS to_user_id, amount " +
           "FROM transfer JOIN transfer_type ON transfer_type.transfer_type_id = transfer.transfer_type_id " +
           "JOIN transfer_status ON transfer_status.transfer_status_id = transfer.transfer_type_id "+
           "JOIN account x ON x.account_id = transfer.account_to " +
           "JOIN account f ON f.account_id = transfer.account_from " +
           "JOIN tenmo_user z ON z.user_id = x.user_id " +
           "JOIN tenmo_user y ON y.user_id = f.user_id" +
           "WHERE f.user_id = ? OR x.user_id = ?;";
   try {
    SqlRowSet result = jdbcTemplate.queryForRowSet(sql, user.getId(), user.getId());
    while (result.next()){
     transferList.add(mapRowToTransfer(result));
    }
   } catch (DataAccessException e) {
    e.printStackTrace();
   }
  return transferList;
 }

 @Override
 public void updateTransfer (Transfer transfer) {
  String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?;";
  jdbcTemplate.update(sql, transfer.getTransferStatusId(), transfer.getTransferId());
 }




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


 @Override
 public int sendTransfer(Transfer transfer) {
  if (transfer.getFromUserId() == transfer.getToUserId()) {
   System.out.println("Error: You can not send money to yourself");
   return 0;
  }
  int newId = 0;
  String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) \" +\n" +
          "VALUES (?, ?, (SELECT account_id FROM account WHERE user_id = ?), (SELECT account_id FROM account WHERE user_id = ?), ?) RETURNING transfer_id";
  try {
   newId = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getFromUserId(), transfer.getToUserId(), transfer.getTransferAmount());
  } catch (DataAccessException | ResourceAccessException e) {
   e.printStackTrace();
  }
  return newId;
 }

 @Override
 public void updateSenderBalance(int transferId, Transfer transfer) {
  String sql = "UPDATE account SET balance = (SELECT SUM (balance - transfer.amount) " +
          "FROM account JOIN Transfer on account.account_id = transfer.account_from WHERE transfer_id = ?) " +
          "WHERE user_id = ?;";
  try{
   jdbcTemplate.update(sql, transferId, transfer.getFromUserId());
  } catch (DataAccessException | ResourceAccessException e) {
   e.printStackTrace();
  }
 }




 public Transfer mapRowToTransfer(SqlRowSet results){
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
