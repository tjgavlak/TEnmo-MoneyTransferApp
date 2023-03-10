package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.util.Objects;

public class User {

    private int id;
    private String username;
    private BigDecimal accountBalance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof User) {
            User otherUser = (User) other;
            return otherUser.getId() == id
                    && otherUser.getUsername().equals(username);
        } else {
            return false;
        }
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    public String selectionPrint(){
        return id + " " + username;
    }
}
