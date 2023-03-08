package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/transfer")
public class TransferController {
    @Autowired
   private TransferDao transferDao;

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers(){
        return transferDao.getAllTransfers();
    }
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable ("id") int id){
        return transferDao.getTransferById(id);
    }
    @RequestMapping(path = "/send/{sender}/{receiver}/{amount}", method = RequestMethod.POST)
    public void sendTransfer(@PathVariable ("sender") int sender, @PathVariable ("receiver") int receiver, @PathVariable ("amount") BigDecimal amount){
        transferDao.sendTransfer(sender, receiver, amount);
        Transfer newTransfer = new Transfer();
        newTransfer.setAccountFrom(sender);
        newTransfer.setAccountTo(receiver);
        newTransfer.setTransferAmount(amount);
        newTransfer.setTransferTypeId(2);
        newTransfer.setTransferStatusId(2);
        transferDao.createTransfer(newTransfer);
    }
    @RequestMapping(path = "/receive/{receiver}/{sender}/{amount}", method = RequestMethod.POST)
    public void receiveTransfer(@PathVariable ("receiver") int receiver, @PathVariable ("sender") int sender, @PathVariable ("amount") BigDecimal amount){
        transferDao.receiveTransfer(receiver, sender, amount);
        Transfer newTransfer = new Transfer();
        newTransfer.setAccountFrom(receiver);
        newTransfer.setAccountTo(sender);
        newTransfer.setTransferAmount(amount);
        newTransfer.setTransferTypeId(1);
        newTransfer.setTransferStatusId(1);
        transferDao.createTransfer(newTransfer);
    }

}
