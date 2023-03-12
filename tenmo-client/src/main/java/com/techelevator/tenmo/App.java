package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;

import com.techelevator.tenmo.services.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import static com.techelevator.tenmo.services.AccountService.authToken;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private User user;
    private AuthenticatedUser currentUser;
    private final ConsoleService consoleService = new ConsoleService(API_BASE_URL);
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);
    /*private final UserService userService = new UserService(API_BASE_URL);*/
    private final static int TYPE_SEND = 2;
    private final static int APPROVED_STATUS = 2;
    private final RestTemplate restTemplate = new RestTemplate();
    public static int ID = 0;


    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        authToken = currentUser.getToken();
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        System.out.println("Your account balance is: " + "$" + accountService.getBalance(authToken));
    }

	private void viewTransferHistory() { //TODO Fix NullPointerException
		// TODO Auto-generated method stub
        /*int accountId = 0;

        accountId = restTemplate.exchange(API_BASE_URL + "accounts/" + ID, HttpMethod.GET, makeAuthEntity(authToken), int.class).getBody();

        Transfer[] transfers = null;
        transfers = transferService.getTransfersByAccountId(accountId);
        console.printTransfers(transfers, accountId);

        int transferID = -1;
        try {
            transferID = console.getUserInputInteger("Please enter transfer ID to view details (0 to cancel)");
            console.printTransferDetailByID(transferID, transfers);
        } catch (Exception e){
            System.out.println("Invalid transfer ID!");
        }*/
        /*Transfer[] transferList = transferService.getTransferHistory(currentUser.getToken());
        consoleService.printTransferHistory(transferList);
        for (Transfer transfer : transferList) {
            System.out.println(transfer);
        }*/
    }

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	} //Optional

	private void sendBucks() { //TODO Fix Users = 0 and Names = null
		// TODO Auto-generated method stub
        Transfer transfer = new Transfer();
        Scanner scanner = new Scanner(System.in);

        transfer.setTransferTypeId(TYPE_SEND);
        transfer.setTransferStatusId(APPROVED_STATUS);

        boolean goodInput = false;

        while(!goodInput) {
            consoleService.printUsers(accountService.listUsers(currentUser.getToken()));
            System.out.println("Enter the ID of the user you would like to send to: ");
            int toId = Integer.parseInt(scanner.nextLine());
            transfer.setAccountTo(toId);
            System.out.println("Enter amount to send: ");
            try {
                double sendAmount = Double.parseDouble(scanner.nextLine());
                BigDecimal sendAmountBD = BigDecimal.valueOf(sendAmount);
                transfer.setAmount(sendAmountBD);
                Transfer newTransfer = transferService.sendMoney(authToken, transfer);
                System.out.println();
                System.out.println("Transfer Successful.");
                System.out.println(newTransfer.toString());
            } catch(Exception e) {
                System.out.println(e); //TODO Currently NullPointerException
            }
        }
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	} //Optional


    private HttpEntity makeAuthEntity(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
