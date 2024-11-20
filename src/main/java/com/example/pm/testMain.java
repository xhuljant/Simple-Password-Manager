package com.example.pm;

import com.example.pm.Model.Account;
import com.example.pm.Model.AccountManager;
import com.example.pm.Model.Folder;
import com.example.pm.Model.LoginAccount;

import java.util.*;

public class testMain {
    public static void main(String[] args) throws Exception {
        LoginAccount loginAccount = new LoginAccount.Builder("Test")
                .website("www.test.com")
                .notes("test")
                .username("username")
                .build();

        AccountManager accountManager = new AccountManager();

        accountManager.addAccount(loginAccount);

        accountManager.printAllAccounts();

        accountManager.createFolder("testfolder");
        accountManager.createFolder("testfolder2");

        accountManager.addAccountToFolder(loginAccount,"testfolder");

        List<Account> list = accountManager.getAccountsInFolder("testfolder");

        //System.out.println("Items in list:"+list.toString());

        try {
            accountManager.saveToFile("test","test");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        AccountManager accountManager1 = AccountManager.loadFromFile("test","test");


        List<Account> list2 = accountManager1.getAccountsInFolder("testfolder");

        //System.out.println("Items in list:"+list2.toString());




        List<String> folderNames = new ArrayList<>();

        for(Map.Entry<String, Folder> entry : accountManager.getFolderMap().entrySet()){
            folderNames.add(entry.getKey());
        }

        Collections.sort(folderNames,String.CASE_INSENSITIVE_ORDER);

        //System.out.println(accountManager.getFolderMap().keySet());

    }

}
