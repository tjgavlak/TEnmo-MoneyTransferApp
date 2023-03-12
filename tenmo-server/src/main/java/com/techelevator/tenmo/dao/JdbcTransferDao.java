package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

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
 public List<Transfer> listTransfers() {
  String sql = "SELECT * FROM transfer;";
  SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

  List<Transfer> transfers = new ArrayList<>();
  while(results.next()) {
   Transfer transfer = mapRowToTransfer(results);
   transfers.add(transfer);
  }
  return transfers;
 }

 @Override
 public List<Transfer> getAllTransfersByUserId(int accountId)  {
  List<Transfer> myTransfers = new ArrayList<>();
  String sql= "SELECT * FROM transfer WHERE account_from = (SELECT account_id FROM account WHERE user_id = ?) " +
          "OR account_to = (SELECT account_id FROM account WHERE user_id = ?); ";
  SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
  while(results.next()) {
   myTransfers.add(mapRowToTransfer(results));
  }
  return myTransfers;
 }

 @Override
 public boolean sendMoney(int fromUserId, int toUserId, BigDecimal amount) {
  String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, " +
          "account_from, account_to, amount) " +
          "VALUES ( 2, 2, (SELECT account_id FROM account WHERE user_id = ?), " +
          "(SELECT account_id FROM account WHERE user_id = ?), ?); ";
  try {
   int output = jdbcTemplate.update(sql, SEND_TYPE_ID, TRANSFER_STATUS_ID, fromUserId, toUserId, amount);
   System.out.println("transfer rows affected: " + output);
  } catch (DataAccessException | ResourceAccessException e) {
   e.printStackTrace();
   return false;
  }
  return true;


 }

 public Transfer mapRowToTransfer(SqlRowSet results){
  Transfer transfer = new Transfer();
  transfer.setTransferId(results.getInt("transfer_id"));
  transfer.setTransferTypeId(results.getInt("transfer_type_id"));
  transfer.setTransferStatusId(results.getInt("transfer_status_id"));
  transfer.setAccountFrom(results.getInt("account_from"));
  transfer.setAccountTo(results.getInt("account_to"));
  transfer.setTransferAmount(results.getBigDecimal("amount"));
  return transfer;
 }
}