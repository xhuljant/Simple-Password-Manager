package com.example.pm.Model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AccountManager manages different types of account types
 * Handles operations for adding, finding, and removing accounts
 * Also handles reading and writing to file
 * AccountMap object is written encrypted and written to file after data is changed
 */

public class AccountManager{
    /**
     * Accounts are indexed as follows
     * 0=Login Account
     * 1=Card Account
     * 2=Note Account
     */
    private static final String DATA_DIRECTORY="Data/"; //combined with EncryptionService.generateFileName() to give location for accountMap to be saved
    private Map<Integer,List<Account>> accountMap;

    /**
     * Constructor takes no parameters
     * When new AccountManager object is created, a empty accountMap map is populated with different account types
     * Can be expanded in the future if further account types are needed
     */
    public AccountManager(){
        accountMap=new HashMap<>();
        for(int i=0;i<3;i++){ //Initializing new lists for each account type, currently only 3 account types, can be expanded in the future
            accountMap.put(i,new ArrayList<>());
        }
    }

    /**
     * @param account to be added to accountMap of all accounts
     * @return false if account already exists, return true if account is added to accountMap
     */
    public boolean addAccount(Account account){
        if(accountExists(account))
            return false;

        //add try catch in case account is not added after accountExists is called
        List<Account> accountList=new ArrayList<>(accountMap.get(account.getAccountType()));
        accountList.add(account);
        accountList.sort(Comparator.comparing(Account::getAccountName, String.CASE_INSENSITIVE_ORDER));
        accountMap.put(account.getAccountType(),accountList);
        return true;
    }

    /**
     * Creates list of accountType from account passed in, traverses list for matching account and removes if account is found
     * Ex if a LoginAccount is pasased in, all LoginAccounts from accountMap will be passed to temp list and traversed to see if there is a matching account
     * @param account to be removed,
     * @return true if acount is removed, false if not
     */
    public boolean removeAccount(Account account){
        List<Account> typeList = accountMap.get(account.getAccountType());
        boolean removed = typeList.removeIf(a -> a.compareTo(account) == 0);
        if (removed) {
            accountMap.put(account.getAccountType(), typeList);
        }
        return removed;
    }

    /**
     * @return list of all accounts in accountmap
     */
    public List<Account> getAllAccounts(){
        return accountMap.values().stream()
                .flatMap(List::stream)
                .sorted(Comparator.comparing(Account::getAccountName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    /**
     * To be used for filtering based on account type
     * @param accountType Index above shows what account type belongs to what index
     * @return list of accounts of a certain type, ex return all LoginAccounts or all CardAccounts
     */
    public List<Account> getAccountsByType(int accountType) {
        return new ArrayList<>(accountMap.get(accountType));
    }

    public void printAllAccounts(){
        List<Account> allAccounts = getAllAccounts();
        for(Account account:allAccounts){
            System.out.println(account.toString());
        }
    }

    /**
     * Compares account passed in to accounts already in accountMap, compares Account Name, Username, and Password.
     * Notes are not compared, if an account already exists you can add notes to that specific account
     * @param account passes in account to look for
     * @return true if a identical account is found, false if not
     */
    public boolean accountExists(Account account){
        List<Account> accountList = accountMap.get(account.getAccountType()); //ge temp list of all accounts of accountType to iterate through

        for(Account comparedAccount:accountList){
            if(account.compareTo(comparedAccount)==0)
                return true;
        }

        return false;
    }

    /**
     * Checks if there is already an account created matching one entered by user
     * Uses EncryptionService to create encrypted filename and compare it to other files already in Data directory
     * @param username to encrypt and compare with
     * @return true if file matching passed in username is found, false if not
     */
    public static boolean fileExistsForUsername(String username) {
        username=username.toLowerCase(Locale.ROOT);
        EncryptionService encryptionService = new EncryptionService();
        String fileName = encryptionService.generateFileName(username);
        Path filePath = Paths.get(DATA_DIRECTORY, fileName);
        return Files.exists(filePath);
    }

    /**
     * Serializes accountMap, converts it to a bytes tream so it can be reversed once file is read back
     * Encrypts serialized data to users master password, can only be decrypted with said password, password cannot be changed once set
     * Data will be lost if user cannot supply password
     * Generates a unique file name
     * Checks to see if Data directory exists, creates one if one does not already exist
     * Writes encrypted byte stream to a file
     * @param username used to generate unique filename to save data
     * @param password used as key to encrypt data
     * @throws Exception
     */
    public void saveToFile(String username, String password) throws Exception {
        EncryptionService encryptionService = new EncryptionService();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(accountMap);
        oos.flush();
        byte[] serializedData = bos.toByteArray();

        String encryptedData = encryptionService.encrypt(Base64.getEncoder().encodeToString(serializedData), password);

        String fileName = encryptionService.generateFileName(username);

        Path directoryPath = Paths.get(DATA_DIRECTORY);
        Files.createDirectories(directoryPath);

        Path filePath = directoryPath.resolve(fileName);
        Files.write(filePath, encryptedData.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Looks for file in data recovery matching username entered by user
     * If file is found, it then reads data to string to be deserialized and loaded on to a temp Map<></> with decrypted with password provided
     * Temp Map is assigned to a new AccountManager obj
     * @param username used to verify file exists
     * @param password used to verify user has access to file
     * @return new Account Manager obj is returned
     * @throws Exception
     */
    // New method to load encrypted accountMap from file
    public static AccountManager loadFromFile(String username, String password) throws Exception {
        if(verifyAccess(username,password)){
            try{
                EncryptionService encryptionService=new EncryptionService();

                // Generate file name
                String fileName = encryptionService.generateFileName(username);

                // Read encrypted data from file
                String encryptedData;
                try (BufferedReader reader = new BufferedReader(new FileReader(DATA_DIRECTORY+fileName))) {
                    encryptedData = reader.readLine();
                }

                String decryptedData = encryptionService.decrypt(encryptedData, password);

                byte[] serializedData = Base64.getDecoder().decode(decryptedData);
                ByteArrayInputStream bis = new ByteArrayInputStream(serializedData);
                ObjectInputStream ois = new ObjectInputStream(bis);
                Map<Integer, List<Account>> loadedMap = (Map<Integer, List<Account>>) ois.readObject();

                AccountManager manager = new AccountManager();
                manager.accountMap = loadedMap;
                return manager;
            }catch (Exception e){
                System.out.println("Error:"+e.getMessage());
                return null;
            }
        }else {
            System.out.println("Access denied, incorrect password");
            return null;
        }
    }

    /**
     * Checks if file exists for username entered by user
     * Verifies user has access to file with password entered
     * @param username
     * @param password
     * @return true if user has access, false if user does not
     */
    public static boolean verifyAccess(String username, String password) {
        try {
            if (!fileExistsForUsername(username)) {
                System.out.println("No data file found for the username: " + username);
                return false;
            }

            EncryptionService encryptionService = new EncryptionService();
            String fileName = encryptionService.generateFileName(username);
            Path filePath = Paths.get(DATA_DIRECTORY, fileName);

            String encryptedData;
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
                encryptedData = reader.readLine(); //Assumes the entire encrypted data is on one line
            }

            String decryptedData = encryptionService.decrypt(encryptedData, password);

            // If no exceptions were thrown, the password is correct and access is verified
            System.out.println("Access verified for username: " + username);
            return true;
        } catch (IOException e) {
            System.out.println("Error accessing the data file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Failed to decrypt data. Possible incorrect password: " + e.getMessage());
        }
        return false;
    }

    /**
     * Verifies password passed in has access to file
     * Reads in encrypted data to String encryptedData
     * Encrypts encryptedData with newPassword and saves file with username passed in
     * @param username used to verify access to file and to write file
     * @param currentPassword used to verify access
     * @param newPassword used to encrypt data
     * @return true or false if new file is saved or false if access is denied or file is not written
     */
    public boolean changePassword(String username, String currentPassword, String newPassword){
        try{
            if (!verifyAccess(username, currentPassword)) {
                System.out.println("Current password is incorrect");
                return false;
            }

            EncryptionService encryptionService = new EncryptionService();
            String fileName = encryptionService.generateFileName(username);
            Path filePath = Paths.get(DATA_DIRECTORY,fileName);
            String encryptedData;

            BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()));
            encryptedData=reader.readLine();


            String newEncryptedData = encryptionService.changePassword(encryptedData,currentPassword,newPassword);
            Files.write(filePath, newEncryptedData.getBytes(StandardCharsets.UTF_8));
            System.out.println("Password changed.");

            return true;

        }catch (Exception e){
            e.getMessage();
            System.out.println("Unable to change password.");
            return false;
        }
    }
}