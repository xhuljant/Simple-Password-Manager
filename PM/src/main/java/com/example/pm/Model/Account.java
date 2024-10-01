package com.example.pm.Model;

import java.io.Serializable;

/**
 * Represents Account object, is extended by specific account types such as LoginAccount or CardAccount
 * Implements comparable for sorting and searching and serializable for writing obj to file
 */
public class Account implements Comparable<Account>, Serializable{
    /**
     * Accounts in accountType are indexed as follows
     * 0=Login Account
     * 1=Card Account
     * 2=Note Account
     */
    private static final long serialVersionUID = 1L;
    private String accountName;
    private int accountType;

    /**
     * @param accountName passed in from subclass
     * @param accountType passed in from subclass depending on class type, for example LoginAccount will always pass 0 to superclass
     */
    public Account(String accountName, int accountType){
        this.accountName=accountName;
        this.accountType=accountType;
    }

    public int getAccountType() {return accountType;}
    public String getAccountName() {
        if(this.accountName!=null)
            return accountName;
        else
            return "";
    }
    public void setAccountName(String accountName) {this.accountName = accountName;}
    @Override
    public String toString() {return this.accountName;}

    /**
     * Compares two Account objects to see if theyre the same
     * @param other the object to be compared.
     * @return 0 if equal, negative or positive int if not equal
     * Comapres Name and Account type only
     */
    @Override
    public int compareTo(Account other) {
        if(this.accountType!=other.accountType){
            return Integer.compare(this.accountType,other.accountType);
        }
        return this.accountName.compareToIgnoreCase(other.getAccountName());
    }
}