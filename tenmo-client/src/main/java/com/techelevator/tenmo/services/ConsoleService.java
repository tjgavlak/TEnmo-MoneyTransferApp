package com.techelevator.tenmo.services;


import com.techelevator.tenmo.App;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;


public class ConsoleService {

    public static String authToken;
    private final String BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleService(String url) {
        this.BASE_URL = url;
    }

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printTransferDetails(Transfer chosenTransfer) {
        System.out.println("------------------------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("------------------------------------------------------------");
        System.out.println("ID: " + chosenTransfer.getTransferId());
        System.out.println("From: " + chosenTransfer.getAccountFrom());
        System.out.println("To: " + chosenTransfer.getAccountTo());
        System.out.println("Type: " + chosenTransfer.getTransferTypeId());
        System.out.println("Status: " + chosenTransfer.getTransferStatusId());
        System.out.println("Amount: $" + chosenTransfer.getAmount());
    }

    public void printTransferHistory(Transfer[] transfers) {
        System.out.println("-------------------------------------------");
        System.out.println("Transfer History");
        System.out.println("\nID          From/To             Amount");
        System.out.println("-------------------------------------------");
        for (Transfer transfer : transfers) {
            System.out.println();
        }
    }

    public void printUsers(List<User> users) {
        if (users != null) {
            System.out.println("-------------------------------------------");
            System.out.println("Users");
            System.out.println("ID          Name");
            System.out.println("-------------------------------------------");
            for (User user : users) {
                System.out.println(user.getId() + "        " + user.getUsername());
            }
            System.out.println("---------\n");
        }
    }



        /*String currentTo = "";
        String currentFrom = "";

        for (Transfer transfer : transfers) {
            System.out.print(transfer.getTransferId() + "        ");
            if (transfer.getAccountFrom() == accountId) {
                currentTo = restTemplate.exchange(BASE_URL + "users/account/" + transfer.getAccountTo(), HttpMethod.GET, makeAuthEntity(),  String.class).getBody();
                System.out.print("To: " + currentTo);
            }
            else {
                currentFrom = restTemplate.exchange(BASE_URL + "users/account/" + transfer.getAccountFrom(), HttpMethod.GET, makeAuthEntity(),  String.class).getBody();
                System.out.print("From: " + currentFrom);
            }
            System.out.println("    \t$" + transfer.getAmount());
        }
        System.out.println("---------");*/


    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }


}
