package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class AccountService {
    public static String authToken;
    private final String BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();

    public AccountService(String url) {
        this.BASE_URL = url;
    }

    private HttpEntity makeAuthEntity(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    public BigDecimal getBalance(String authToken) {
        BigDecimal balance = null;
        try {
            balance = restTemplate.exchange(BASE_URL + "account/balance", HttpMethod.GET, makeAuthEntity(authToken), BigDecimal.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException ex) {
            BasicLogger.log(ex.getMessage());
        }
        return balance;
    }


    public User[] listUsers(String authToken) {
        User[] users = null;
        try {
            users = restTemplate.exchange(BASE_URL + "users", HttpMethod.GET, makeAuthEntity(authToken), User[].class).getBody();
        } catch (ResourceAccessException | RestClientResponseException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }
}

