package com.example.pm;

import com.example.pm.Model.AccountManager;
import com.example.pm.Model.LoginAccount;

public class testMain {
    public static void main(String[] args){
        LoginAccount loginAccount = new LoginAccount.Builder("Test")
                .website("www.test.com")
                .notes("test")
                .username("username")
                .build();

        AccountManager accountManager = new AccountManager();

        accountManager.addAccount(loginAccount);

        accountManager.printAllAccounts();
    }

}
