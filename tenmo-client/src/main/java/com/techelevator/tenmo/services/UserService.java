package com.techelevator.tenmo.services;

import com.techelevator.tenmo.App;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserService {
    public static String authToken;
    private final String BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();

    public UserService(String url) {
        this.BASE_URL = url;
    }

    public List<User> listAllUsers(int idToRemove) {
        User[] users = null;
        users = restTemplate.exchange(BASE_URL + "users/", HttpMethod.GET, makeAuthEntity(authToken), User[].class).getBody();

        List<User> usersWithoutId = new ArrayList<>();
        for (int i = 0; i < Objects.requireNonNull(users).length; i++) {
            if (users[i].getId() != idToRemove) {
                usersWithoutId.add(users[i]);
            }
        }
        return usersWithoutId;
    }
    private HttpEntity makeAuthEntity(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
