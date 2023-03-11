package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;

import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import java.math.BigDecimal;
import java.util.List;

import static com.techelevator.tenmo.services.AccountService.authToken;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private User user;
    private AuthenticatedUser currentUser;
    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);
    private final static int TYPE_SEND = 2;
    private final static int APPROVED_STATUS = 2;


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
        Transfer[] transferList = transferService.getTransferHistory(currentUser.getToken());
        consoleService.printTransferListHeader(transferList);
        for (Transfer transfer : transferList) {
            System.out.println(transfer);
        }
    }

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	} //Optional

	private void sendBucks() { //TODO Fix NullPointerException
		// TODO Auto-generated method stub
        List<Account> accounts = accountService.listAccounts(currentUser.getToken());
        for (int i = 0; i < accounts.size(); i++) {
            System.out.println(accounts.get(i));
        }
        int toId = consoleService.promptForInt("Select userId from above list: ");
        BigDecimal transferAmount = consoleService.promptForBigDecimal("Enter amount to transfer: ");
        Transfer transfer = new Transfer();
        transfer.setAmount(transferAmount);
        transfer.setAccountToUsername(String.valueOf(toId));
        transfer.setAccountFromUsername(currentUser.getUser().getUsername());
        transferService.sendMoney(currentUser.getToken(), transfer);
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	} //Optional
}
