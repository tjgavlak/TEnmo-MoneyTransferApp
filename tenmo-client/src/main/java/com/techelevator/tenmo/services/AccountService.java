package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
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

    public BigDecimal getBalance() {
        BigDecimal balance = new BigDecimal(0);
        try {
            balance = restTemplate.exchange(API_BASE_URL + "/balance" + userId.getUser().getId(), HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException ex) {
            BasicLogger.log(ex.getMessage());
        }
        return balance;
    }

    public Account getAccountByUserId(int userId) {
        Account account = null;

        try {
            ResponseEntity<Account> response = restTemplate.exchange(
                    API_BASE_URL + "user/" + userId,
                    HttpMethod.GET, makeAuthEntity(), Account.class);

            account = response.getBody();

        } catch (RestClientResponseException | ResourceAccessException ex) {
            BasicLogger.log(ex.getMessage());
        }
        return account;
    }

    public void sendTransfer(int senderId, int receiverId, BigDecimal amount){
        try{
            ResponseEntity<Void> response = restTemplate.exchange(
                    API_BASE_URL + "send/" + senderId + "/" + receiverId + "/" + amount,
                    HttpMethod.POST, makeAuthEntity(), Void.class);
        } catch (RestClientResponseException | ResourceAccessException ex) {
            BasicLogger.log(ex.getMessage());
        }
    }

    public void receiveTransfer(int receiverId, int senderId, BigDecimal amount){
        try{
            ResponseEntity<Void> response = restTemplate.exchange(
                    API_BASE_URL + "receive/" + receiverId + "/" + senderId + "/" + amount,
                    HttpMethod.POST, makeAuthEntity(), Void.class);
        } catch (RestClientResponseException | ResourceAccessException ex) {
            BasicLogger.log(ex.getMessage());
        }
    }



    HttpEntity<Void> makeAuthEntity() {
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
