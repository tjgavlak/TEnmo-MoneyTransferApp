package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import java.math.BigDecimal;
import java.util.List;

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
        AccountService.authToken = currentUser.getToken();
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
        // TODO Auto-generated method stub
        System.out.println("Your account balance is: " + "$" + accountService.getBalance());
    }

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
        Transfer[] transfers = transferService.userTransfers(user);
        for(Transfer transfer : transfers) {
            System.out.println(transfer);
        }
        /*User activeUser = setActiveUser();
        List<Transfer> transfers = List.of(transferService.userTransfers(activeUser));
        consoleService.printTransferListHeader();
        for(Transfer transfer : transfers) {
            if (transfer.getFromUserId() == activeUser.getId()) {
                List<User> users  = List.of(accountService.getUsers());
                User receivingUser = users.stream().filter(user -> user.getId() == transfer.getToUserId()).findAny().orElse(null);
                System.out.println(transfer.sendPrint(receivingUser.getUsername()));
            } else if (transfer.getToUserId() == activeUser.getId()) {
                List<User> users = List.of(accountService.getUsers());
                User sendingUser = users.stream().filter(user -> user.getId() == transfer.getFromUserId()).findAny().orElse(null);
                System.out.println(transfer.receivePrint(sendingUser.getUsername()));
            }*/

        boolean running = true;
        while(running){
            int choice = consoleService.promptForInt("Please enter transfer ID to view details or (0) to cancel: ");
            if (choice == 0) {
                running = false;
            } else {
                System.out.println("Please enter a Transfer ID from the list.");
            }
        }
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		// TODO Auto-generated method stub
        User activeUser = setActiveUser();
        List<User> users = List.of(accountService.getUsers());
        int choice = chooseFromAvailableUsers();
        Transfer transfer = sendMoneyTransfer(activeUser);
        User receivingUser = users.stream().filter(user -> user.getId() == choice).findAny().orElse(null);
	}

    private void receivingUser(User receivingUser, User sendingUser, Transfer transfer) {
        if(receivingUser != null){
            transfer.setToUserId(receivingUser.getId());
            boolean running = true;
            while(running){
                transfer.setAmount(consoleService.promptForBigDecimal("Please enter in a dollar amount: "));
                if(sendingUser.getAccountBalance().compareTo(transfer.getAmount()) >= 0 && transfer.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                    transferService.sendMoney(transfer);
                    running = false;
                } else if (transfer.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Please enter a valid number greater than zero.");
                } else {
                    System.out.println("Insufficient funds for entered amount\n");
                }
            }
        } else {
            System.out.println("Please enter a valid User ID");
            sendBucks();
        }
    }

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}

    public Transfer sendMoneyTransfer(User user){
        Transfer transfer = new Transfer();
        transfer.setTransferTypeId(TYPE_SEND);
        transfer.setTransferStatusId(APPROVED_STATUS);
        transfer.setFromUserId(user.getId());
        return transfer;
    }

    public int chooseFromAvailableUsers(){
        return consoleService.userIdForSendMoney(accountService.getUsers());
    }

    private User setActiveUser() {
        return accountService.getUser(currentUser.getUser().getUsername());
    }

}
