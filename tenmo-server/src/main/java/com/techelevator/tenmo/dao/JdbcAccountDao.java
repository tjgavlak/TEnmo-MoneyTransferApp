package com.techelevator.tenmo.dao;
import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> account = new ArrayList<>();
        String sql = "SELECT * FROM account;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql);
        while(result.next()){
            Account accountResult = mapRowToAccount(result);
            account.add(accountResult);
        }
        return account;
    }

    @Override
    public BigDecimal getBalance(int userId) {
        String sql = "SELECT balance FROM account WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        BigDecimal balance = null;

        if (results.next()) {
            balance = results.getBigDecimal("balance");
        }
        return balance;
    }

    @Override
    public Account getAccountById(int id) {
        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
        if(result.next()){
            return mapRowToAccount(result);
        }
        return null;
    }

    @Override
    public Account getAccountByUserId(int id) {
        String sql = "SELECT * FROM account WHERE user_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
        if (result.next()){
            return mapRowToAccount(result);
        }
        return null;
    }

    public Account mapRowToAccount(SqlRowSet result){
        Account account = new Account();
        account.setId(result.getInt("account_id"));
        account.setUserId(result.getInt("user_id"));
        account.setBalance(result.getBigDecimal("balance"));
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


