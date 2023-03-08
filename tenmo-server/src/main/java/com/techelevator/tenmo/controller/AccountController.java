package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping(path = "/api/account")
public class AccountController {
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Account getAccountById(int id){


    }
}
