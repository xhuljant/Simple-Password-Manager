package com.example.pm.Model;


import java.io.Serializable;

/**
 * Subclass of Account
 * Holds mains cardNumber cardExpDate cardSecCode cardHolderName as strings, data should be validated before its passed in to create obj
 *
 */
public class CardAccount extends Account implements Comparable<Account>, Serializable {
    private static final long serialVersionUID = 1L;
    private String cardNumber;
    private String cardExpDate;
    private String cardSecCode;
    private String cardHolderName;

    public CardAccount(String accountName){
        super(accountName,1);
    }

    public CardAccount(String accountName, String cardNumber, String cardExpDate, String cardSecCode, String cardHolderName){
        super(accountName,1);
        this.cardNumber=cardNumber;
        this.cardExpDate=cardExpDate;
        this.cardSecCode=cardSecCode;
        this.cardHolderName=cardHolderName;
    }

    @Override
    public String getAccountName() {return super.getAccountName();}
    public String getCardExpDate() {return cardExpDate;}
    public String getCardHolderName() {return cardHolderName;}
    public String getCardNumber() {return cardNumber;}
    public String getCardSecCode() {return cardSecCode;}
    @Override
    public int getAccountType() {return super.getAccountType();}

    public void setCardNumber(String newCardNumber){this.cardNumber=newCardNumber;}
    public void setCardExpDate(String newExpDate){this.cardExpDate=newExpDate;}
    public void setCardSecCode(String newSecCode){this.cardSecCode=newSecCode;}
    public void setCardHolderName(String newCardHolderName){this.cardHolderName=newCardHolderName;}
    public void setAccountName(String newAccountName){super.setAccountName(newAccountName);}

    public String toString(){
        return getAccountName()+"\n"+
                getCardHolderName()+"\n"+
                getCardNumber()+"\n"+
                getCardExpDate()+"\n"+
                getCardSecCode()+"\n";
    }

    @Override
    public int compareTo(Account other) {
        CardAccount otherCardAccount=(CardAccount)other;

        if(this.getAccountType()!=otherCardAccount.getAccountType())
            return Integer.compare(this.getAccountType(),otherCardAccount.getAccountType());

        if(this.getAccountName().compareToIgnoreCase(otherCardAccount.getAccountName())!=0)
            return this.getAccountName().compareToIgnoreCase(otherCardAccount.getAccountName());

        if(this.getCardHolderName().compareToIgnoreCase(otherCardAccount.cardHolderName)!=0)
            return this.getCardHolderName().compareToIgnoreCase(otherCardAccount.cardHolderName);

        if(this.getCardNumber().compareToIgnoreCase(otherCardAccount.getCardNumber())!=0)
            return this.getCardNumber().compareToIgnoreCase(otherCardAccount.getCardNumber());

        if(this.getCardSecCode().compareToIgnoreCase(otherCardAccount.getCardSecCode())!=0)
            return this.getCardSecCode().compareToIgnoreCase(otherCardAccount.getCardSecCode());

        if(this.getCardExpDate().compareToIgnoreCase(otherCardAccount.cardExpDate)!=0)
            return this.getCardExpDate().compareToIgnoreCase(otherCardAccount.cardExpDate);

        return 0;
    }
}
