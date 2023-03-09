package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class TransferService {
    private final String API_BASE_URL = "http://localhost:8080/api/transfer/";
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser userId;
    public TransferService(AuthenticatedUser userId){
        this.userId = userId;
    }

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }



    public void sendTransfer(int senderId, int receiverId, BigDecimal amount){
        HttpEntity entity = makeAuthEntity(userId);
        try{
            ResponseEntity<Void> response = restTemplate.exchange(
                    API_BASE_URL + "send/" + senderId + "/" + receiverId + "/" + amount,
                    HttpMethod.POST, entity, Void.class);
        } catch (RestClientResponseException | ResourceAccessException ex) {
            BasicLogger.log(ex.getMessage());
        }
    }

    public void receiveTransfer(int receiverId, int senderId, BigDecimal amount){
        HttpEntity entity = makeAuthEntity(userId);
        try{
            ResponseEntity<Void> response = restTemplate.exchange(
                    API_BASE_URL + "receive/" + receiverId + "/" + senderId + "/" + amount,
                    HttpMethod.POST, entity, Void.class);
        } catch (RestClientResponseException | ResourceAccessException ex) {
            BasicLogger.log(ex.getMessage());
        }
    }
    HttpEntity<Void> makeAuthEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userId.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }
}
