package com.example.pm.Model;


import java.io.Serializable;
import java.util.Comparator;

/**
 * Subclass of Account, meant to hold Login Accounts with a accountName, username, password, and notes.
 * Implements comparable and serializable to compare objects and serialize so they can be written to file
 */
public class LoginAccount extends Account implements Comparable<Account>, Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String notes;

    private LoginAccount(Builder builder) {
        super(builder.accountName, 0);
        this.username = builder.username;
        this.password = builder.password;
        this.notes = builder.notes;
    }

    public static class Builder {
        private final String accountName;
        private String username = "";
        private String password = "";
        private String notes = "";

        public Builder(String accountName) {
            this.accountName = accountName;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public LoginAccount build() {
            return new LoginAccount(this);
        }
    }

    public String getUsername(){
        if(this.username!=null)
            return this.username;
        else
            return "";
    }

    public String getNotes() {
        if(this.notes!=null)
            return this.notes;
        else
            return "";
    }

    public String getPassword(){
        if(this.password!=null)
            return this.password;
        else
            return "";
    }

    @Override
    public String getAccountName() {return super.getAccountName();}

    @Override
    public int getAccountType() {return super.getAccountType();}

    @Override
    public void setAccountName(String accountName) {super.setAccountName(accountName);}
    public void setPassword(String password) {this.password = password;}
    public void setUsername(String username) {this.username = username;}
    public void setNotes(String notes) {this.notes = notes;}

    @Override
    public String toString() {
        return getAccountName()+"\n"+
                getUsername()+"\n"+
                getPassword()+"\n"+
                getNotes()+"\n";
    }

    /**
     * Compares one Account object to another
     * First compares account type to make sure they are not different account types, then proceeds to compare variables
     * @param other the object to be compared.
     * @return 0 if equal, other number if not
     */

    @Override
    public int compareTo(Account other){
        if(!(other instanceof Account))
            return getClass().getName().compareTo(other.getClass().getName());

        LoginAccount otherAccount = (LoginAccount) other;

        return Comparator.comparing(LoginAccount::getAccountType)
                .thenComparing(LoginAccount::getAccountName,String.CASE_INSENSITIVE_ORDER)
                .thenComparing(LoginAccount::getUsername,String.CASE_INSENSITIVE_ORDER)
                .thenComparing(LoginAccount::getPassword)
                .thenComparing(LoginAccount::getNotes,String.CASE_INSENSITIVE_ORDER)
                .compare(this,otherAccount);
    }
}
