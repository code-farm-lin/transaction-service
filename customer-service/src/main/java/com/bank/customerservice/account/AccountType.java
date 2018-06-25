package com.bank.customerservice.account;

public enum AccountType {
    SAVING_ACCOUNT("Saving Account"),
    CURRENT_ACCOUNT("Current Account");

    private String name;

    private AccountType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
