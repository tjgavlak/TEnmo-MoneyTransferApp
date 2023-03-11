package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import java.util.List;


public class TransferService {
    private final String BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();
    public static String authToken;

    public TransferService(String url) {
        this.BASE_URL = url;
    }

    public Transfer[] getTransferHistory(String authToken) {
        Transfer[] transfers = null;
        try {
            transfers = restTemplate.exchange(BASE_URL + "transfer/history", HttpMethod.GET, makeAuthEntity(authToken), Transfer[].class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public boolean sendMoney(String authToken, Transfer newTransfer){
        boolean returnedTransfer = false;
        try{
            restTemplate.exchange(BASE_URL + "/send", HttpMethod.POST, makeTransferEntity(newTransfer, authToken), Void.class).getBody();
            returnedTransfer = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return returnedTransfer;
    }

    private HttpEntity<Void> makeAuthEntity(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

        HttpEntity<Transfer> makeTransferEntity(Transfer transfer, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }
}