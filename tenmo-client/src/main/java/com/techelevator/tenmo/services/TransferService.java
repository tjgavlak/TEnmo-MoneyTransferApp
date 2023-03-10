package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class TransferService {
    private final String BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();
    public static String authToken;

    public TransferService(String url) {
        this.BASE_URL = url;
    }


    public void sendMoney(Transfer transfer){
        try{
            restTemplate.postForObject(BASE_URL + "send/", makeTransferEntity(transfer), Void.class);
        } catch (RestClientResponseException | ResourceAccessException ex) {
            BasicLogger.log(ex.getMessage());
        }
    }

    public Transfer[] userTransfers(User user) {
        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(BASE_URL + "transfer", HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException ex) {
            BasicLogger.log(ex.getMessage());
        }
        return transfers;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

        HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }
}

/*public void receiveMoney(int receiverId, int senderId, BigDecimal amount){
        HttpEntity entity = makeAuthEntity(userId);
        try{
            ResponseEntity<Void> response = restTemplate.exchange(
                    BASE_URL + "receive/" + receiverId + "/" + senderId + "/" + amount,
                    HttpMethod.POST, entity, Void.class);
        } catch (RestClientResponseException | ResourceAccessException ex) {
            BasicLogger.log(ex.getMessage());
        }
    }*/
