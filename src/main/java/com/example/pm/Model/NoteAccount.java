package com.example.pm.Model;


import java.io.Serializable;
import java.util.Comparator;

public class NoteAccount extends Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private String note;

    private NoteAccount(Builder builder){
        super(builder.noteName,2);
        this.note=builder.noteContent;
    }

    public static class Builder{
        private final String noteName;
        private String noteContent="";

        public Builder(String noteName){
            this.noteName=noteName;
        }

        public Builder noteContent(String noteContent){
            this.noteContent=noteContent;
            return this;
        }

        public NoteAccount build(){
            return new NoteAccount(this);
        }
    }

    public String getNote() {
        if(this.note!=null)
            return note;
        else
            return "";
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
    public int compareTo(Account other){
        if(!(other instanceof Account))
            return getClass().getName().compareTo(other.getClass().getName());

        NoteAccount otherAccount = (NoteAccount) other;

        return Comparator.comparing(NoteAccount::getAccountType)
                .thenComparing(NoteAccount::getAccountName,String.CASE_INSENSITIVE_ORDER)
                .thenComparing(NoteAccount::getNote,String.CASE_INSENSITIVE_ORDER)
                .compare(this,otherAccount);
    }

}
