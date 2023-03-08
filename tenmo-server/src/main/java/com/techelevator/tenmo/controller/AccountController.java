package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private TransferDao transferDao;

    @RequestMapping(path = "/accounts", method = RequestMethod.GET)
    public List<Account> listAllAccounts() {
        return accountDao.getAllAccounts();
    }

    @RequestMapping(path = "/accounts/{id}", method = RequestMethod.GET)
    public Account getAccountById(@PathVariable("id") int id){
        return accountDao.getAccountById(id);
    }

    @RequestMapping(path = "/user/{id}", method  = RequestMethod.GET)
    public Account getAccountByUserId(@PathVariable("id") int id){
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

    @RequestMapping(path = "/account/user/{id}/balance", method = RequestMethod.GET)
    public BigDecimal getBalanceByUserId(@PathVariable("id") int id){
        return accountDao.getBalanceByUserId(id);
    }

    @RequestMapping(path = "/account/{id}/balance", method = RequestMethod.GET)
    public BigDecimal getBalanceByAccountId(@PathVariable("id") int id){
        return accountDao.getBalanceByAccountId(id);
    }
}
