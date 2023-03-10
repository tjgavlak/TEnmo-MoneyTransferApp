package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/transfer")
public class TransferController {
   private final TransferDao transferDao;
    private final UserDao userDao;
    private final static int TYPE_SEND = 2;
    private final static int APPROVED_STATUS = 2;
    private final static int PENDING_STATUS = 1;


    public TransferController(UserDao userDao, TransferDao transferDao) {
        this.userDao = userDao;
        this.transferDao = transferDao;
    }

    @RequestMapping(path = "/send/", method = RequestMethod.POST)
    public void sendTransfer(@RequestBody Transfer transfer) {
        int transferId = transferDao.sendTransfer(transfer);
        transferDao.updateSenderBalance(transferId, transfer);
        transferDao.updateReceiverBalance(transferId, transfer);
    }

    @RequestMapping(path = "/transfer/history/", method = RequestMethod.GET)
    public List<Transfer> userTransfers(Principal principal) {
        String username = principal.getName();
        User user = userDao.findByUsername(username);
        return transferDao.userTransfers(user);
    }

    @RequestMapping(path = "", method = RequestMethod.PUT)
    public void updateTransfer(@RequestBody Transfer transfer) {
        transferDao.updateTransfer(transfer);
        User payingUser = userDao.findByUsername(transfer.getAccountFromUsername());
        if (transfer.getTransferStatusId() == APPROVED_STATUS && transfer.getTransferAmount().compareTo(payingUser.getBalance()) <= 0){
            transferDao.updateSenderBalance(transfer.getTransferId(), transfer);
            transferDao.updateReceiverBalance(transfer.getTransferId(), transfer);
        } else {
            transfer.setTransferStatusId(PENDING_STATUS);
        }
    }

}
