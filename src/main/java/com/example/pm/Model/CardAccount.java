package com.example.pm.Model;


import java.io.Serializable;
import java.util.Comparator;

/**
 * Represents a card-based account that extends the base Account class.
 * This class implements Comparable for sorting and Serializable for object persistence.
 */

public class CardAccount extends Account implements Comparable<Account>, Serializable {
    private static final long serialVersionUID = 1L;
    private String cardNumber;
    private String cardExpDate;
    private String cardSecCode;
    private String cardHolderName;

    /**
     * Builder pattern used to create object with optional parameters.
     * Allows for flexible object creation.
     * @param builder
     */
    private CardAccount(Builder builder){
        super(builder.accountName,1);
        this.cardNumber=builder.cardNumber;
        this.cardSecCode=builder.cardSecCode;
        this.cardExpDate=builder.cardExpDate;
        this.cardHolderName=builder.cardHolderName;
    }

    /**
     * Values of variables is first to empty string so null is not returned if value is not passed in to object.
     * If value is passed in, empty string is replaced by variable value passed in.
     */
    public static class Builder{
        private final String accountName;
        private String cardNumber="";
        private String cardExpDate="";
        private String cardSecCode="";
        private String cardHolderName="";

        public Builder(String accountName) {
            this.accountName = accountName;
        }

        public Builder cardNumber(String cardNumber){
            this.cardNumber=cardNumber;
            return this;
        }

        public Builder cardExpDate(String cardExpDate){
            this.cardExpDate=cardExpDate;
            return this;
        }

        public Builder cardSecCode(String cardSecCode){
            this.cardSecCode=cardSecCode;
            return this;
        }

        public Builder cardHolderName(String cardHolderName){
            this.cardHolderName=cardHolderName;
            return this;
        }

        public CardAccount build(){
            return new CardAccount(this);
        }
    }

    @Override
    public String getAccountName() {return super.getAccountName();}

    public String getCardExpDate() {
        if(this.cardExpDate!=null)
            return cardExpDate;
        else
            return "";
    }

    public String getCardHolderName() {
        if(this.cardHolderName!=null)
            return cardHolderName;
        else
            return "";
    }

    public String getCardNumber() {
        if(this.cardNumber!=null)
            return cardNumber;
        else
            return "";
    }
    public String getCardSecCode() {
        if(this.cardSecCode!=null)
            return cardSecCode;
        else
            return "";
    }

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

    /**
     * Compares Account objects in order of :
     * 1.Account Type
     * 2.Card Holder Name
     * 3 Account Name
     * 4.Card Number
     * 5.Exp Date
     * 6.Sec Code
     * @param other the object to be compared.
     * @return if this < other, zero if equal, positive if this > other.
     */
    @Override
    public int compareTo(Account other){
        if(!(other instanceof Account))
            return getClass().getName().compareTo(other.getClass().getName());

        CardAccount otherCardAccount=(CardAccount) other;

        return Comparator.comparing(CardAccount::getAccountType)
                .thenComparing(CardAccount::getCardHolderName,String.CASE_INSENSITIVE_ORDER)
                .thenComparing(CardAccount::getAccountName,String.CASE_INSENSITIVE_ORDER)
                .thenComparing(CardAccount::getCardNumber,String.CASE_INSENSITIVE_ORDER)
                .thenComparing(CardAccount::getCardExpDate,String.CASE_INSENSITIVE_ORDER)
                .thenComparing(CardAccount::getCardSecCode,String.CASE_INSENSITIVE_ORDER)
                .compare(this,otherCardAccount);
    }

}
