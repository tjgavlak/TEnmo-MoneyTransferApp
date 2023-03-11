package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final JdbcTransferDao transferDao;
    private final JdbcAccountDao accountDao;


    public TransferController(JdbcTransferDao transfer, JdbcAccountDao accountDao) {
        this.transferDao = transfer;
        this.accountDao = accountDao;
    }

    /*@ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path= "", method = RequestMethod.POST)
    public void createTransfer(@RequestBody Transfer transfer ) {
        transferDao.sendMoney(transfer);
    }*/


    @GetMapping(path = "")
    public List<Transfer> getAllTransfers(Principal user){
        Account account = accountDao.getAccountByUsername(user.getName());
        return transferDao.getAllTransfersByUserId(account.getUserId());
    }

    @GetMapping(path = "/users")
    public List<Account> listAllAccounts() {
        return accountDao.getAllAccounts();
    }

    @PostMapping(path = "")
    public void sendTransfer(@RequestBody Transfer transfer, Principal fromUser) {
        Account fromAccount = accountDao.getAccountByUsername(fromUser.getName());
        int fromId = fromAccount.getUserId();
        int id = transfer.getAccountTo();
        BigDecimal amount = transfer.getTransferAmount();
        System.out.println(transfer.toString());
        BigDecimal fromAccountBalance = accountDao.getBalance(fromAccount.getUserId());
        if (fromAccountBalance.compareTo(amount) <= 0) {
            System.out.println("You're too poor to send this much.");
        }
        if (fromId == id){
            System.out.println("No, no, no.");
        } else {
            transferDao.sendMoney(fromId,id, amount);
            accountDao.addToBalance(amount,id);
            accountDao.subtractFromBalance(amount, fromId);
            System.out.println("transfer complete");
        }

    }
}
