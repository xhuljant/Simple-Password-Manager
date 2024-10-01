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

    private CardAccount(Builder builder){
        super(builder.accountName,1);
        this.cardNumber=builder.cardNumber;
        this.cardSecCode=builder.cardSecCode;
        this.cardExpDate=builder.cardExpDate;
        this.cardHolderName=builder.cardHolderName;
    }

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
