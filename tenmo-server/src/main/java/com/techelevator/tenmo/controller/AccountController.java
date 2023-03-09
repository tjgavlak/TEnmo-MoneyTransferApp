package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {

    @Autowired
    AccountDao accountDao;
    @Autowired
    TransferDao transferDao;
    @Autowired
    UserDao userDao;

    @RequestMapping(path = "/accounts", method = RequestMethod.GET)
    public List<Account> listAllAccounts() {
        return accountDao.getAllAccounts();
    }

    @RequestMapping(path = "/accounts/{id}", method = RequestMethod.GET)
    public Account getAccountById(@PathVariable("id") int id) {
        return accountDao.getAccountById(id);
    }

    @RequestMapping(path = "/user/{id}", method = RequestMethod.GET)
    public Account getAccountByUserId(@PathVariable("id") int id) {
        return accountDao.getAccountByUserId(id);
    }

    /*@ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Account createAccount(@Valid @RequestBody Account account) {
        return accountDao.createAccount(account);
    }*/

    /*@ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/account/{id}", method = RequestMethod.DELETE)
    public void deleteAccount(@PathVariable("id") int id) {
        accountDao.deleteAccount(id);
    }*/

    @RequestMapping(path = "/account/{id}/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable int id) {
        return accountDao.getAccountById(id).getBalance();
    }
}
