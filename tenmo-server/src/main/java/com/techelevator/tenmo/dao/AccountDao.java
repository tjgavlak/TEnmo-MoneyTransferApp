package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface AccountDao {

    List<Account> listAccounts();

    BigDecimal addToBalance(BigDecimal deposit, int accountId);

    BigDecimal subtractFromBalance(BigDecimal withdraw, int accountId);

    BigDecimal getBalance(int userId);

    Account getAccountByUsername(String username);

    Account getAccountByUserId(int accountId);
}



