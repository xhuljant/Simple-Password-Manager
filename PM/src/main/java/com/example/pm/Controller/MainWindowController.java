package com.example.pm.Controller;

import com.example.pm.Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.smartcardio.Card;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

public class MainWindowController {

    private AccountManager accountManager;
    private Account currentAccount;
    private String masterUsername;
    private String masterPassword;
    private boolean editSelected;

    @FXML private Button copyCardNumberButton;
    @FXML private Button copyExpDateButton;
    @FXML private Button copySecCodeButton;
    @FXML private Button copyPasswordButton;
    @FXML private Button copyUserNameButton;
    @FXML private Button exportButton;

    @FXML public Text itemInformationText;
    @FXML private Button allAccountsButton;
    @FXML private Button loginAccountsButton;
    @FXML private Button cardAccountsButton;
    @FXML private Button noteAccountsButton;
    @FXML private Button passwordGeneratorButton;
    @FXML private TextField searchTextField;

    @FXML private Button saveAccountButton;
    @FXML private Button editAccountButton;
    @FXML private Button deleteAccountButton;
    @FXML private Button cancelEditButton;
    @FXML private Button addAccountButton;

    @FXML private ListView<Account> allAccountsListView;
    @FXML private ListView<Account> loginAccountsListView;
    @FXML private ListView<Account> cardAccountsListView;
    @FXML private ListView<Account> noteAccountsListView;

    @FXML private AnchorPane loginDetailsPane;
    @FXML private AnchorPane cardDetailsPane;
    @FXML private AnchorPane noteDetailsPane;

    //login accounts
    @FXML private TextField accountNameTextField;
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;
    @FXML private TextArea notesTextField;

    //card accounts
    @FXML private TextField cardNameTextField;
    @FXML private TextField cardNumberTextField;
    @FXML private TextField expDateTextField;
    @FXML private TextField secCodeTextField;
    @FXML private TextField cardHolderNameTextField;

    //note accounts
    @FXML private TextField noteNameTextField;
    @FXML private TextArea noteContentTextArea;

    @FXML
    private void initialize() {

        accountManager = new AccountManager();
        setupListViews();
        setupSearchFunctionality();
    }

    private void setupListViews(){
        allAccountsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (editSelected) {
                resetButtonsAndFields();
                editSelected = false;
            }
            if (newSelection != null) {
                showAccountDetails(newSelection);
            }
        });

        loginAccountsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (editSelected) {
                resetButtonsAndFields();
                editSelected = false;
            }
            if (newSelection != null) {
                showAccountDetails(newSelection);
            }
        });

        cardAccountsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (editSelected) {
                resetButtonsAndFields();
                editSelected = false;
            }
            if (newSelection != null) {
                showAccountDetails(newSelection);
            }
        });

        noteAccountsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (editSelected) {
                resetButtonsAndFields();
                editSelected = false;
            }
            if (newSelection != null) {
                showAccountDetails(newSelection);
            }
        });
    }

    private void setupSearchFunctionality() {
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterAccounts(newValue);
        });
    }

    private void filterAccounts(String searchText) {
        ObservableList<Account> filteredAccounts = FXCollections.observableArrayList();
        List<Account> allAccounts = accountManager.getAllAccounts();

        if (searchText == null || searchText.isEmpty()) {
            filteredAccounts.addAll(allAccounts);
        } else {
            for (Account account : allAccounts) {
                if (account.getAccountName().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredAccounts.add(account);
                }
            }
        }
        allAccountsListView.setItems(filteredAccounts);
        loginAccountsListView.setItems(filteredAccounts);
        cardAccountsListView.setItems(filteredAccounts);
        noteAccountsListView.setItems(filteredAccounts);
    }


    public void setAccountManager(AccountManager accountManager, String masterUsername, String masterPassword) {
        this.masterUsername = masterUsername;
        this.masterPassword = masterPassword;
        this.accountManager = accountManager;
        loadAllAccountsList();
        loadLoginAccountsList();
        loadCardAccountsList();
        loadNoteAccountsList();
        showAllAccounts();
    }

    private void loadAllAccountsList() {
        ObservableList<Account> accounts = FXCollections.observableArrayList(accountManager.getAllAccounts());
        allAccountsListView.setItems(accounts);
        allAccountsListView.setCellFactory(param -> new ListCell<Account>() {
            @Override
            protected void updateItem(Account item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getAccountName());
            }
        });
        System.out.println("All accounts list view loaded.");
    }

    private void loadLoginAccountsList() {
        ObservableList<Account> accounts = FXCollections.observableArrayList(accountManager.getAccountsByType(0));
        loginAccountsListView.setItems(accounts);
        loginAccountsListView.setCellFactory(param -> new ListCell<Account>() {
            @Override
            protected void updateItem(Account item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getAccountName());
            }
        });
        System.out.println("Login accounts list view loaded.");
    }

    private void loadCardAccountsList() {
        ObservableList<Account> accounts = FXCollections.observableArrayList(accountManager.getAccountsByType(1));
        cardAccountsListView.setItems(accounts);
        cardAccountsListView.setCellFactory(param -> new ListCell<Account>() {
            @Override
            protected void updateItem(Account item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getAccountName());
            }
        });
        System.out.println("Card accounts list view loaded.");
    }

    private void loadNoteAccountsList() {
        ObservableList<Account> accounts = FXCollections.observableArrayList(accountManager.getAccountsByType(2));
        noteAccountsListView.setItems(accounts);
        noteAccountsListView.setCellFactory(param -> new ListCell<Account>() {
            @Override
            protected void updateItem(Account item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getAccountName());
            }
        });
        System.out.println("Note accounts list view loaded.");
    }

    public void showAccountDetails(Account account) {
        if (account instanceof LoginAccount) {
            showLoginDetails((LoginAccount) account);
        } else if (account instanceof CardAccount) {
            showCardDetails((CardAccount) account);
        } else {
            showNoteDetails((NoteAccount) account);
        }
    }

    //Functions to only show the list user would like to see
    public void showAllAccounts() {
        clearAllSelections();
        hideItemInformation();
        allAccountsListView.setVisible(true);
        loginAccountsListView.setVisible(false);
        noteAccountsListView.setVisible(false);
        cardAccountsListView.setVisible(false);
    }

    public void showLoginAccounts() {
        clearAllSelections();
        hideItemInformation();
        allAccountsListView.setVisible(false);
        loginAccountsListView.setVisible(true);
        noteAccountsListView.setVisible(false);
        cardAccountsListView.setVisible(false);
    }

    public void showNoteAccounts() {
        clearAllSelections();
        hideItemInformation();
        allAccountsListView.setVisible(false);
        loginAccountsListView.setVisible(false);
        noteAccountsListView.setVisible(true);
        cardAccountsListView.setVisible(false);
    }

    public void showCardAccounts() {
        clearAllSelections();
        hideItemInformation();
        allAccountsListView.setVisible(false);
        loginAccountsListView.setVisible(false);
        noteAccountsListView.setVisible(false);
        cardAccountsListView.setVisible(true);
    }

    public void hideItemInformation() {
        loginDetailsPane.setVisible(false);
        cardDetailsPane.setVisible(false);
        noteDetailsPane.setVisible(false);
        itemInformationText.setVisible(false);
        editAccountButton.setVisible(false);
        deleteAccountButton.setVisible(false);
    }

    private void clearAllSelections() {
        allAccountsListView.getSelectionModel().clearSelection();
        loginAccountsListView.getSelectionModel().clearSelection();
        cardAccountsListView.getSelectionModel().clearSelection();
        noteAccountsListView.getSelectionModel().clearSelection();
        resetButtonsAndFields();
        hideItemInformation();
    }

    //Functions to show the fields on the right side of listview that match the object type
    //For example, if a LoginAccount is selected then only fields that account type should be shown
    public void showLoginDetails(LoginAccount account) {
        loginDetailsPane.setVisible(true);
        cardDetailsPane.setVisible(false);
        noteDetailsPane.setVisible(false);

        accountNameTextField.setText(account.getAccountName());
        usernameTextField.setText(account.getUsername());
        passwordTextField.setText(account.getPassword());
        notesTextField.setText(account.getNotes());

        editAccountButton.setVisible(true);
        itemInformationText.setVisible(true);
    }

    public void showCardDetails(CardAccount account) {
        loginDetailsPane.setVisible(false);
        cardDetailsPane.setVisible(true);
        noteDetailsPane.setVisible(false);

        cardNameTextField.setText(account.getAccountName());
        cardNumberTextField.setText(account.getCardNumber());
        cardHolderNameTextField.setText(account.getCardHolderName());
        expDateTextField.setText(account.getCardExpDate());
        secCodeTextField.setText(account.getCardSecCode());

        editAccountButton.setVisible(true);
        itemInformationText.setVisible(true);
    }

    public void showNoteDetails(NoteAccount account) {
        loginDetailsPane.setVisible(false);
        cardDetailsPane.setVisible(false);
        noteDetailsPane.setVisible(true);

        noteNameTextField.setText(account.getAccountName());
        noteContentTextArea.setText(account.getNote());

        editAccountButton.setVisible(true);
        itemInformationText.setVisible(true);

    }

    private void loadAllLists() {
        loadAllAccountsList();
        loadCardAccountsList();
        loadNoteAccountsList();
        loadLoginAccountsList();

        System.out.println("Loaded all lists.");
    }

    private Account getSelectedAccount() {
        if (allAccountsListView.isVisible()) {
            return allAccountsListView.getSelectionModel().getSelectedItem();
        } else if (loginAccountsListView.isVisible()) {
            return loginAccountsListView.getSelectionModel().getSelectedItem();
        } else if (cardAccountsListView.isVisible()) {
            return cardAccountsListView.getSelectionModel().getSelectedItem();
        } else if (noteAccountsListView.isVisible()) {
            return noteAccountsListView.getSelectionModel().getSelectedItem();
        }
        return null;
    }

    private void resetButtonsAndFields() {
        loadAllLists();

        deleteAccountButton.setVisible(false);

        saveAccountButton.setVisible(false);
        editAccountButton.setVisible(false);
        cancelEditButton.setVisible(false);

        accountNameTextField.setEditable(false);
        usernameTextField.setEditable(false);
        passwordTextField.setEditable(false);
        notesTextField.setEditable(false);

        cardNumberTextField.setEditable(false);
        cardNameTextField.setEditable(false);
        cardHolderNameTextField.setEditable(false);
        expDateTextField.setEditable(false);
        secCodeTextField.setEditable(false);

        noteNameTextField.setEditable(false);
        noteContentTextArea.setEditable(false);

        System.out.println("Reset all buttons and fields.");
    }

    @FXML public void handleAllAccountsButton(ActionEvent event) {showAllAccounts();}
    @FXML public void handleLoginAccountsButton(ActionEvent event) {showLoginAccounts();}
    @FXML public void handleCardAccountsButton(ActionEvent event) {showCardAccounts();}
    @FXML public void handleNoteAccountsButton(ActionEvent event) {showNoteAccounts();}

    @FXML
    public void handleExportButton(){
        EncryptionService encryptionService=new EncryptionService();
        File sourceFile= new File("Data/"+encryptionService.generateFileName(masterUsername));

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folder");
        Stage currentStage = (Stage) exportButton.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(currentStage);

        if(selectedDirectory!=null){

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Export");
            alert.setHeaderText("Export File");
            alert.setContentText("Export file to "+selectedDirectory.getAbsolutePath()+" ?");

            alert.showAndWait().ifPresent(response -> {
                if(response==ButtonType.OK){
                    try{
                        File copiedFile=new File(selectedDirectory,sourceFile.getName());
                        Files.copy(sourceFile.toPath(),copiedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("File Exported Successfully.");
                    }catch (Exception e){
                        System.out.println("File Exported Successfully.");
                        e.printStackTrace();
                    }
                }else {
                    System.out.println("File not exported.");
                }
            });
        }
    }

    @FXML
    public void handleEditAccountButton(ActionEvent event) {
        deleteAccountButton.setVisible(true);
        currentAccount = getSelectedAccount();
        editSelected = true;

        if (loginDetailsPane.isVisible()) {
            accountNameTextField.setEditable(true);
            usernameTextField.setEditable(true);
            passwordTextField.setEditable(true);
            notesTextField.setEditable(true);
        }

        if (cardDetailsPane.isVisible()) {
            cardNumberTextField.setEditable(true);
            cardNameTextField.setEditable(true);
            cardHolderNameTextField.setEditable(true);
            expDateTextField.setEditable(true);
            secCodeTextField.setEditable(true);
        }

        if (noteDetailsPane.isVisible()) {
            noteNameTextField.setEditable(true);
            noteContentTextArea.setEditable(true);
        }

        editAccountButton.setVisible(false);
        saveAccountButton.setVisible(true);
        cancelEditButton.setVisible(true);
    }

    @FXML
    public void handleSaveButton(ActionEvent event) throws Exception {

        if (loginDetailsPane.isVisible()) {
            if (accountNameTextField.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Error", "Account name cannot be empty.");
            } else {
                currentAccount = getSelectedAccount();
                LoginAccount newAccount = new LoginAccount.Builder(accountNameTextField.getText())
                        .username(usernameTextField.getText())
                        .password(passwordTextField.getText())
                        .notes(notesTextField.getText())
                        .build();

                if(!accountManager.accountExists(newAccount)){
                    boolean accountAdded = accountManager.addAccount(newAccount);

                    if (accountAdded) {
                        accountManager.removeAccount(currentAccount);
                        accountManager.saveToFile(masterUsername, masterPassword);
                        accountManager = AccountManager.loadFromFile(masterUsername, masterPassword);
                        loadAllLists();
                        System.out.println("Added updated account, removed old account");
                    }else {
                        showAlert(Alert.AlertType.WARNING,"Error","Unable to add account");
                    }
                }else {
                    showAlert(Alert.AlertType.WARNING,"Error","Account already exists.");
                }
            }
        }

        if (cardDetailsPane.isVisible()) {
            if (cardNameTextField.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Error", "Card name cannot be empty.");
            } else {
                CardAccount newAccount = new CardAccount.Builder(cardNameTextField.getText())
                        .cardNumber(cardNumberTextField.getText())
                        .cardExpDate(expDateTextField.getText())
                        .cardSecCode(secCodeTextField.getText())
                        .cardHolderName(cardHolderNameTextField.getText())
                        .build();

                if(!accountManager.accountExists(newAccount)){
                    boolean accountAdded = accountManager.addAccount(newAccount);

                    if(accountAdded){
                        accountManager.removeAccount(currentAccount);
                        currentAccount = newAccount;
                        accountManager.saveToFile(masterUsername, masterPassword);
                        accountManager = AccountManager.loadFromFile(masterUsername, masterPassword);
                        loadAllLists();
                    }else {
                        showAlert(Alert.AlertType.WARNING,"Error","Unable to add account");
                    }
                }else {
                    showAlert(Alert.AlertType.WARNING,"Error","Account already exists.");
                }
            }
        }

        if (noteDetailsPane.isVisible()) {
            if (noteNameTextField.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Error", "Card name cannot be empty.");
                resetButtonsAndFields();
            } else {
                NoteAccount newAccount = new NoteAccount.Builder(noteNameTextField.getText())
                        .noteContent(noteContentTextArea.getText())
                        .build();

                if(!accountManager.accountExists(newAccount)){
                    boolean accountAdded = accountManager.addAccount(newAccount);

                    if(accountAdded){
                        accountManager.removeAccount(currentAccount);
                        currentAccount = newAccount;
                        accountManager.saveToFile(masterUsername, masterPassword);
                        accountManager = AccountManager.loadFromFile(masterUsername, masterPassword);
                        loadAllLists();
                    }else{
                        showAlert(Alert.AlertType.WARNING,"Error","Unable to add account");
                    }
                }else {
                    showAlert(Alert.AlertType.WARNING,"Error","Account already exists.");
                }
            }
        }
        resetButtonsAndFields();
    }

    @FXML
    public void handleCancelButton(ActionEvent event) {
        showAccountDetails(currentAccount);
        resetButtonsAndFields();
        editAccountButton.setVisible(true);
        System.out.println("Cancel button selected.");
    }

    @FXML
    public void handleDeleteButton(ActionEvent event) throws Exception {

        if (getSelectedAccount() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Account");
            alert.setHeaderText("Confirm deleting account.");
            alert.setContentText("Are you sure you want to delete account? This cannot be undone.");

            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("Delete");
            Optional<ButtonType> choice = alert.showAndWait();

            if (choice.isPresent() && choice.get() == ButtonType.OK) {
                boolean accountRemoved = accountManager.removeAccount(getSelectedAccount());

                if (accountRemoved) {
                    accountManager.saveToFile(masterUsername, masterPassword);
                    accountManager = AccountManager.loadFromFile(masterUsername, masterPassword);
                    resetButtonsAndFields();

                    System.out.println("Account Removed.");

                    hideItemInformation();

                } else {
                    showAlert(Alert.AlertType.WARNING, "Error", "Unable to remove account.");
                }
            }
        }
    }

    @FXML
    public void handleAddAccountButton() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pm/AddAccountController.fxml"));
        Parent root = loader.load();

        AddAccountController addAccountController = loader.getController();
        addAccountController.setAccountManager(accountManager, masterUsername, masterPassword);

        Stage addAccountStage = new Stage();
        addAccountStage.setTitle("Add Account");
        addAccountStage.initModality(Modality.APPLICATION_MODAL);
        addAccountStage.initOwner(addAccountButton.getScene().getWindow());
        addAccountStage.setResizable(false);
        addAccountStage.setScene(new Scene(root));
        addAccountStage.showAndWait();

        System.out.println("Add account button selected");

        loadAllLists();
    }

    @FXML
    public void handleGeneratePasswordButton(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader((getClass().getResource("/com/example/pm/GeneratePasswordController.fxml")));
            Parent root = loader.load();

            Stage generatePasswordStage = new Stage();
            generatePasswordStage.setTitle("Password Generator");
            generatePasswordStage.initModality(Modality.APPLICATION_MODAL);
            generatePasswordStage.initOwner(addAccountButton.getScene().getWindow());
            generatePasswordStage.setScene(new Scene(root));
            generatePasswordStage.setResizable(false);
            generatePasswordStage.showAndWait();
        } catch (Exception exception) {
            exception.printStackTrace();
            showAlert(Alert.AlertType.WARNING, "Error", "Unable to open password generator.");
        }
    }

    @FXML public void handleCopyUsernameButton(){
        final Clipboard clipboard=Clipboard.getSystemClipboard();
        final ClipboardContent text=new ClipboardContent();
        text.putString(usernameTextField.getText());
        clipboard.setContent(text);
    }
    @FXML public void handleCopyPasswordButton(){
        final Clipboard clipboard=Clipboard.getSystemClipboard();
        final ClipboardContent text=new ClipboardContent();
        text.putString(passwordTextField.getText());
        clipboard.setContent(text);
    }
    @FXML public void handleCopyCardNumberButton(){
        final Clipboard clipboard=Clipboard.getSystemClipboard();
        final ClipboardContent text=new ClipboardContent();
        text.putString(cardNumberTextField.getText());
        clipboard.setContent(text);
    }
    @FXML public void handleCopyExpDateButton(){
        final Clipboard clipboard=Clipboard.getSystemClipboard();
        final ClipboardContent text=new ClipboardContent();
        text.putString(expDateTextField.getText());
        clipboard.setContent(text);
    }
    @FXML public void handleCopySecCodeButton(){
        final Clipboard clipboard=Clipboard.getSystemClipboard();
        final ClipboardContent text=new ClipboardContent();
        text.putString(secCodeTextField.getText());
        clipboard.setContent(text);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
