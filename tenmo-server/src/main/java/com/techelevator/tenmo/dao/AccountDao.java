package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    List<Account> getAllAccounts();

    BigDecimal addToBalance(BigDecimal deposit, int userId);

    BigDecimal subtractFromBalance(BigDecimal withdraw, int userId);

    BigDecimal getBalance(int userId);

    Account getAccountByUsername(String username);

    Account getAccountByUserId(int id);
}



