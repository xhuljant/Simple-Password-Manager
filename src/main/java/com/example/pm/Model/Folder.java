package com.example.pm.Model;

import java.io.Serializable;
import java.util.*;

public class Folder implements Comparable<Folder>, Serializable {

    private static final long serialVersionUID = 1L;

    private String folderName;
    private Set<Account> accounts;

    public Folder(String folderName){
        this.folderName = folderName;
        this.accounts = new HashSet<>();
    }

    public String getFolderName(){return this.folderName;}
    public List<Account> getAccounts(){return new ArrayList<>(accounts);}

    public boolean addAccount(Account account){
        return accounts.add(account);
    }

    public boolean removeAccount(Account account){
        return accounts.remove(account);
    }

    @Override
    public int compareTo(Folder other) {
        if (other == null) {
            return 1;
        }
        return String.CASE_INSENSITIVE_ORDER.compare(this.folderName, other.folderName);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Folder folder = (Folder) other;
        return Objects.equals(folderName, folder.folderName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.folderName );
    }
}
