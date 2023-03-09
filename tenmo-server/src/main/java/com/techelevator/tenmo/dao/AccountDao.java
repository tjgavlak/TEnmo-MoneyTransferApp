package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    public List<Account> getAllAccounts();

    public BigDecimal getBalance(int userId);

    public Account getAccountById(int id);

    public Account getAccountByUserId(int id);
}

/*    public Account addMoney(int id, BigDecimal amount);

    public Account subtractMoney(int id, BigDecimal amount);*/

//    public boolean checkValidTransfer(int id, BigDecimal amount);

