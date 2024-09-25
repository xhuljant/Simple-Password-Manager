package com.example.pm.Model;


import java.io.Serializable;

public class NoteAccount extends Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private String note;

    public NoteAccount(String accountName){
        super(accountName,2);
    }

    public NoteAccount(String accountName,String note){
        super(accountName,2);
        this.note=note;
    }

    public String getNote() {
        return note;
    }
    public void addToNote(String note){
        this.note=note+"\n"+note;
    }
    @Override
    public int getAccountType() {
        return super.getAccountType();
    }
    @Override
    public String getAccountName() {
        return super.getAccountName();
    }
    @Override
    public void setAccountName(String accountName) {
        super.setAccountName(accountName);
    }
    public void setNote(String note) {
        this.note = note;
    }
    @Override
    public String toString() {return getAccountName()+"\n"+ this.note;}

    @Override
    public int compareTo(Account other) {
        NoteAccount otherNoteAccount=(NoteAccount) other;

        if(this.getAccountName().compareToIgnoreCase(otherNoteAccount.getAccountName())!=0)
            return this.getAccountName().compareToIgnoreCase(otherNoteAccount.getAccountName());

        if(this.getNote().compareTo(otherNoteAccount.getNote())!=0)
            return this.getNote().compareTo(otherNoteAccount.getNote());

        return 0;
    }
}
