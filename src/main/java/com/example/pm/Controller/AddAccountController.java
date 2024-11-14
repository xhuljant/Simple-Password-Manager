package com.example.pm.Controller;


import com.example.pm.Model.AccountManager;
import com.example.pm.Model.CardAccount;
import com.example.pm.Model.LoginAccount;
import com.example.pm.Model.NoteAccount;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AddAccountController {

    private AccountManager accountManager;
    private String masterPassword;
    private String masterUsername;

    @FXML public VBox loginAccountVBox;
    @FXML public MenuItem loginAccount;
    @FXML public MenuItem cardAccount;
    @FXML public MenuItem noteAccount;
    @FXML public Button deleteButton;
    @FXML private ChoiceBox<String> accountTypeChoiceBox;

    @FXML private AnchorPane loginAccountPane;
    @FXML private VBox logginAccountVBox;
    @FXML private Text accountNameText;
    @FXML private TextField accountNameTextField;
    @FXML private TextArea loginNoteArea;
    @FXML private Text loginNoteText;
    @FXML private Text usernameText;
    @FXML private TextField usernameTextField;
    @FXML private Text passwordText;
    @FXML private TextField passwordTextField;

    @FXML private Button cancelButton;
    @FXML private Button generatorButton;
    @FXML private Button saveButton;

    @FXML private AnchorPane cardAccountPane;
    @FXML private Text cardHolderNameText;
    @FXML private TextField cardHolderNameTextField;
    @FXML private Text cardNameTextArea;
    @FXML private TextField cardNameTextField;
    @FXML private Text cardNumberTextArea;
    @FXML private TextField cardNumberTextField;
    @FXML private Text expDateText;
    @FXML private TextField expDateTextField;
    @FXML private Text secCodeText;
    @FXML private TextField secCodeTextField;

    @FXML private AnchorPane noteAccountPane;
    @FXML private Text noteText;
    @FXML private TextArea noteTextArea;
    @FXML public Text noteNameText;
    @FXML public TextField noteNameTextField;



    @FXML
    public void initialize(){
        accountTypeChoiceBox.setValue("Login");
    }

    public void setAccountManager(AccountManager accountManager,String masterUsername,String masterPassword){
        this.accountManager=accountManager;
        this.masterPassword=masterPassword;
        this.masterUsername=masterUsername;
    }

    @FXML
    public void handleGeneratorButton(ActionEvent event){
        try {
            generatorButton.setDisable(true);

            FXMLLoader loader = new FXMLLoader((getClass().getResource("/FXML/GeneratePasswordController.fxml")));
            Parent root = loader.load();

            Stage generatePasswordStage = new Stage();
            generatePasswordStage.setTitle("Password Generator");
            //generatePasswordStage.initModality(Modality.APPLICATION_MODAL);
            //generatePasswordStage.initOwner(generatorButton.getScene().getWindow());
            generatePasswordStage.setScene(new Scene(root));
            generatePasswordStage.setResizable(false);
            generatePasswordStage.showAndWait();

            generatorButton.setDisable(false);

        }catch (Exception exception){
            exception.printStackTrace();
            showAlert(Alert.AlertType.WARNING,"Error","Unable to open password generator.");
        }
    }

    @FXML
    public void handleSaveButton(ActionEvent event) throws Exception {
        String choice=accountTypeChoiceBox.getValue();

        if(choice.equalsIgnoreCase("card")){
            if(!cardNameTextField.getText().isEmpty()){
                CardAccount newAccount=new CardAccount.Builder(cardNameTextField.getText())
                        .cardNumber(cardNumberTextField.getText())
                        .cardExpDate(expDateTextField.getText())
                        .cardSecCode(secCodeTextField.getText())
                        .cardHolderName(cardHolderNameTextField.getText())
                        .build();

                boolean accountAdded = accountManager.addAccount(newAccount);
                if (accountAdded) {
                    accountManager.saveToFile(masterUsername, masterPassword);
                    System.out.println("Card Account Added");
                }else {
                    showAlert(Alert.AlertType.WARNING,"Error","Account already exists.");
                }
                generatorButton.getScene().getWindow().hide();
            }else{
                showAlert(Alert.AlertType.WARNING,"Error","Please enter card name before saving.");
            }
        }

        if(choice.equalsIgnoreCase("login")){
            if(!accountNameTextField.getText().isEmpty()){
                LoginAccount newAccount=new LoginAccount.Builder(accountNameTextField.getText())
                        .username(usernameTextField.getText())
                        .password(passwordTextField.getText())
                        .notes(loginNoteArea.getText())
                        .build();
                boolean accountAdded = accountManager.addAccount(newAccount);
                if (accountAdded) {
                    accountManager.saveToFile(masterUsername, masterPassword);
                    System.out.println("Login Account Added");
                }else {
                    showAlert(Alert.AlertType.WARNING,"Error","Account already exists.");

                }
                generatorButton.getScene().getWindow().hide();
            }else {
                showAlert(Alert.AlertType.WARNING, "Error", "Please enter account name before saving.");
            }
        }

        if(choice.equalsIgnoreCase("note")) {
            if (!noteNameTextField.getText().isEmpty()) {
                NoteAccount newAccount=new NoteAccount.Builder(noteNameTextField.getText())
                        .noteContent(noteTextArea.getText())
                        .build();
                boolean accountAdded = accountManager.addAccount(newAccount);
                if (accountAdded) {
                    accountManager.saveToFile(masterUsername, masterPassword);
                    System.out.println("Note Account Added");
                }else {
                    showAlert(Alert.AlertType.WARNING,"Error","Account already exists.");
                }
                generatorButton.getScene().getWindow().hide();
            } else {
                showAlert(Alert.AlertType.WARNING, "Error", "Please enter note name before saving.");
            }
        }
    }

    @FXML
    public void handleCancelButton(ActionEvent event){
        generatorButton.getScene().getWindow().hide();
    }

    @FXML
    public void handleChoiceBox(ActionEvent event){

        String choice=accountTypeChoiceBox.getValue();

        if(choice.equalsIgnoreCase("card")){
            generatorButton.setVisible(false);
            loginAccountPane.setVisible(false);
            noteAccountPane.setVisible(false);
            cardAccountPane.setVisible(true);
        }

        if(choice.equalsIgnoreCase("login")){
            generatorButton.setVisible(true);
            loginAccountPane.setVisible(true);
            noteAccountPane.setVisible(false);
            cardAccountPane.setVisible(false);
        }

        if(choice.equalsIgnoreCase("note")){
            generatorButton.setVisible(false);
            loginAccountPane.setVisible(false);
            noteAccountPane.setVisible(true);
            cardAccountPane.setVisible(false);
        }

    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
