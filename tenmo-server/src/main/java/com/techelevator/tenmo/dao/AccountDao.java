package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    List<Account> getAllAccounts();

    BigDecimal getBalance(int userId);

    Account getAccountById(int id);

    Account getAccountByUserId(int id);
}

/*    public Account addMoney(int id, BigDecimal amount);

    public Account subtractMoney(int id, BigDecimal amount);*/

//    public boolean checkValidTransfer(int id, BigDecimal amount);

