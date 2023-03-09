package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

 @Autowired
 private final JdbcTemplate jdbcTemplate;
 public JdbcTransferDao(DataSource dataSource) {
  this.jdbcTemplate = new JdbcTemplate(dataSource);
 }

 @Override
 public List<Transfer> getAllTransfers() {
   List<Transfer> transferList = new ArrayList<>();
   String sql = "SELECT * FROM transfer;";
   SqlRowSet result = jdbcTemplate.queryForRowSet(sql);
   while (result.next()){
    Transfer transfer = mapRowToTransfer(result);
   }
  return transferList;
 }

 @Override
 public List<Transfer> getAllTransfersByAccountId(int id) {
  List<Transfer> transferList = new ArrayList<>();
  String sql = "SELECT * FROM transfer WHERE account_from = ?;";
  SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
  while(result.next()) {
   Transfer transfer = mapRowToTransfer(result);
   transferList.add(transfer);
  }
  return transferList;
 }

 @Override
 public Transfer getTransferById(int id) {
  String sql = "SELECT * FROM transfer WHERE transfer_id = ?;";
  SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
  if(result.next()){
   return mapRowToTransfer(result);
  }
  return null;
 }

 @Override
 public Transfer createTransfer(Transfer transfer) {
  String sql = "INSERT INTO transfer (transfer_type_id, " +
          "transfer_status_id, account_from, " +
          "account_to, amount) " +
          "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";

  Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
          transfer.getTransferTypeId(),
          transfer.getTransferStatusId(),
          transfer.getAccountFrom(),
          transfer.getAccountTo(),
          transfer.getTransferAmount());
  return getTransferById(newId);
 }

 @Override
 public void sendTransfer(int sender, int receiver, BigDecimal amount) {

 }

 @Override
 public void receiveTransfer(int sender, int receiver, BigDecimal amount) {

 }

 /*@Override
 public void deleteTransfer(int id) {
  String sql = "DELETE FROM transfer WHERE transfer_id = ?;";
  jdbcTemplate.update(sql,id);
 }*/

 public Transfer mapRowToTransfer(SqlRowSet results){
  Transfer transfer = new Transfer();
  transfer.setId(results.getInt("transfer_id"));
  transfer.setTransferTypeId(results.getInt("transfer_type_id"));
  transfer.setTransferStatusId(results.getInt("transfer_status_id"));
  transfer.setAccountFrom(results.getInt("account_from"));
  transfer.setAccountTo(results.getInt("account_to"));
  transfer.setTransferAmount(results.getBigDecimal("amount"));
  return transfer;
 }
}
