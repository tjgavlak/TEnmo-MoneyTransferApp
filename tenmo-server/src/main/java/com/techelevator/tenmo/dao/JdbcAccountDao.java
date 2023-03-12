package com.techelevator.tenmo.dao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Account> listAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT user_id, account_id FROM account;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()){
            accounts.add(mapRowToAccountNoBalance(results));
        }
        return accounts;
    }

    @Override
    public BigDecimal getBalance(int userId) {
        BigDecimal balance = BigDecimal.valueOf(0);
        String sql = "SELECT balance FROM account WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            balance = mapRowToAccount(results).getBalance();
        }
        return balance;
    }

    @Override
    public Account getAccountByUsername(String username) {
        Account account = new Account();
        String sql = "SELECT a.* " +
                "FROM account a " +
                "JOIN tenmo_user t USING (user_id) " +
                "WHERE t.username = ?; ";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, username);
        if(result.next()){
            account = mapRowToAccount(result);
        }
        return account;
    }

    @Override
    public Account getAccountByUserId(int accountId) {
        Account account = new Account();
        String sql = "SELECT * FROM account WHERE user_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);
        if (result.next()){
            account = mapRowToAccount(result);
        }
        return account;
    }

    @Override
    public BigDecimal addToBalance(BigDecimal deposit, int accountId) {
        Account account = getAccountByUserId(accountId);
        String sql = "UPDATE account " +
                "SET balance = balance + ? " +
                "WHERE user_id = ?; ";
        try {
            int output = jdbcTemplate.update(sql, deposit, accountId);
            System.out.println("deposit rows affected: " + output);
        } catch (DataAccessException | ResourceAccessException e){
            e.printStackTrace();
            System.out.println("Unable to update balance");
        }
        return account.getBalance();
    }

    @Override
    public BigDecimal subtractFromBalance(BigDecimal withdraw, int userId) {
        Account account = getAccountByUserId(userId);
        String sql = "UPDATE account " +
                "SET balance = balance - ? " +
                "WHERE user_id = ?; ";
        try {
            int output = jdbcTemplate.update(sql, withdraw, userId);
            System.out.println("withdraw rows affected: " + output);
        } catch (DataAccessException e){
            e.printStackTrace();
            System.out.println("unable to update balance");
        }
        return account.getBalance();
    }

    public Account mapRowToAccount(SqlRowSet result){
        Account account = new Account();
        account.setAccountId(result.getInt("account_id"));
        account.setUserId(result.getInt("user_id"));
        account.setBalance(result.getBigDecimal("balance"));
        return account;
    }

    public Account mapRowToAccountNoBalance(SqlRowSet result){
        Account account = new Account();
        account.setAccountId(result.getInt("account_id"));
        account.setUserId(result.getInt("user_id"));
        return account;
    }
}

    /*@Override
    public Account createAccount(Account account) {
        String sql = "INSERT INTO account (user_id, balance) " +
                "VALUES (?, ?) RETURNING account_id";

        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                account.getUserId(),
                account.getBalance());
        return getAccountByUserId(newId);
    }*/


    /*@Override
    public void deleteAccount(int id) {
        String sql = "DELETE FROM account WHERE account_id = ?;";
        jdbcTemplate.update(sql, id);
    }*/

    /*@Override
    public Account addMoney(int id, BigDecimal amount) {
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, amount, id);
        return getAccountById(id);
    }*/
    /*@Override
    public boolean checkValidTransfer(String user, BigDecimal amount){
        if (amount.compareTo(getBalance(user)) > 0){
            return false;
        }else {
            return true;
        }
}*/
    /*@Override
    public Account subtractMoney(int id, BigDecimal amount) {

        if (checkValidTransfer(id, amount)) {
            String sql = "UPDATE account SET balance = (balance - ?) WHERE user_id = ?;";
            jdbcTemplate.update(sql, amount, id);

        } else {
            System.out.println("Transaction could not be completed, not enough funds in your account.");

        } return getAccountById(id);
    }*/


