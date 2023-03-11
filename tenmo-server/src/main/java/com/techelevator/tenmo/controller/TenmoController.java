package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class TenmoController {
    @Autowired
    private final AccountDao accountDao;
    @Autowired
    private final TransferDao transferDao;

    public TenmoController(AccountDao accountDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/balance")
    public BigDecimal checkBalance(Principal user) {
        Account account = accountDao.getAccountByUsername(user.getName());
        return accountDao.getBalance(account.getUserId());
    }

    @PostMapping(path = "/transfer")
    public void sendTransfer(@RequestBody Transfer transfer, Principal fromUser) {
        Account fromAccount = accountDao.getAccountByUsername(fromUser.getName());
        int fromId = fromAccount.getUserId();
        int id = Integer.parseInt(transfer.getAccountToUsername());
        BigDecimal amount = transfer.getTransferAmount();
        System.out.println(transfer.toString());
        BigDecimal fromAccountBalance = accountDao.getBalance(fromAccount.getUserId());
        if (fromAccountBalance.compareTo(amount) <= 0) {
            System.out.println("You don't have enough funds to send.");
        }
        if (fromId == id){
            System.out.println("No no no.");
        } else {
            transferDao.sendTransfer(fromId, id, amount);
            accountDao.addToBalance(amount, id);
            accountDao.subtractFromBalance(amount, fromId);
            System.out.println("Transfer Complete");
        }

    }

    @GetMapping(path= "/users")
    public List<Account> getAllAccounts() {
        return accountDao.getAllAccounts();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/transfer")
    public List<Transfer> listTransfers(Principal user){
        Account account = accountDao.getAccountByUsername(user.getName());
        return transferDao.getTransfersByUserId(account.getUserId());
    }
}
