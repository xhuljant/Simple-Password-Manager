package com.example.pm.Model;


import java.io.Serializable;

/**
 * Subclass of Account, meant to hold Login Accounts with a accountName, userName, password, and notes.
 * Notes can be left blank empty, notes are not considered when objects are compared to one another
 * Implements comparable and serializable to compare objects adn serialize so they can be written to file
 */
public class LoginAccount extends Account implements Comparable<Account>, Serializable {
    private static final long serialVersionUID = 1L;
    private String userName;
    private String password;
    private String notes;

    /**
     * Constructor to be used when user enters a accountname but wants to set username, password, and notes later
     * @param accountName and int 0 sent to superclass
     */
    public LoginAccount(String accountName) {
        super(accountName,0);
    }

    public LoginAccount(String accountName,String userName, String password, String notes){
        super(accountName,0);
        this.userName=userName;
        this.password=password;
        this.notes=notes;
    }

    public String getUserName(){return this.userName;}
    public String getNotes() {return this.notes;}
    public String getPassword(){return this.password;}
    @Override
    public String getAccountName() {return super.getAccountName();}
    @Override
    public int getAccountType() {return super.getAccountType();}

    @Override
    public void setAccountName(String accountName) {super.setAccountName(accountName);}
    public void setPassword(String password) {this.password = password;}
    public void setUserName(String userName) {this.userName = userName;}
    public void setNotes(String notes) {this.notes = notes;}


    @Override
    public String toString() {
        return getAccountName()+"\n"+
                getUserName()+"\n"+
                getPassword()+"\n"+
                getNotes()+"\n";
    }

    /**
     * Compares one Account object to another
     * First compares account type to make sure they are not different account types, then proceeds to compare variables
     * Notes are not compared
     * @param other the object to be compared.
     * @return 0 if equal, other number if not
     */
    @Override
    public int compareTo(Account other) {
        LoginAccount otherLoginAccount=(LoginAccount)other;

        if(this.getAccountType()!=otherLoginAccount.getAccountType())
            return Integer.compare(this.getAccountType(),otherLoginAccount.getAccountType());

        if(this.getAccountName().compareToIgnoreCase(otherLoginAccount.getAccountName())!=0)
            return this.getAccountName().compareToIgnoreCase(otherLoginAccount.getAccountName());

        if(this.getUserName().compareToIgnoreCase(otherLoginAccount.getUserName())!=0)
            return this.getUserName().compareToIgnoreCase(otherLoginAccount.getUserName());

        if(this.getPassword().compareTo(otherLoginAccount.getPassword())!=0)
            return this.getPassword().compareTo(otherLoginAccount.getPassword());

        return 0;
    }
}
