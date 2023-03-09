package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Balance;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    private final String API_BASE_URL = "http://localhost:8080/api/account/";
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser userId;
    public AccountService(AuthenticatedUser userId){
        this.userId = userId;
    }

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Balance getBalance(AuthenticatedUser authenticatedUser) {
        HttpEntity entity = makeAuthEntity(authenticatedUser);
        Balance balance = null;
        try {
            balance = restTemplate.exchange(API_BASE_URL + "/balance", HttpMethod.GET, entity, Balance.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException ex) {
            BasicLogger.log(ex.getMessage());
        }
        return balance;
    }

    public Account getAccountByUserId(AuthenticatedUser authenticatedUser, int userId) {
        Account account = null;
        HttpEntity entity = makeAuthEntity(authenticatedUser);
        try {
            account = restTemplate.exchange(API_BASE_URL + "user/" + userId, HttpMethod.GET, entity, Account.class).getBody();

        } catch (RestClientResponseException | ResourceAccessException ex) {
            BasicLogger.log(ex.getMessage());
        }
        return account;
    }





    HttpEntity<Void> makeAuthEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userId.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

   /* private HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(account, headers);
    }*/
}
