package com.example.pm.Controller;

import com.example.pm.Model.AccountManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Locale;
import java.util.regex.Pattern;

public class CreateAccountController {

    @FXML
    private TextField userNameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField retypedPasswordField;

    @FXML
    public void initialize() {
        System.out.println("CreateAccountController initialized");
    }

    @FXML
    public void handleCreateAccountButton(ActionEvent e) throws Exception {
        try {
            String username = userNameField.getText();
            String password = passwordField.getText();
            String retypedPassword = retypedPasswordField.getText();

            if (username.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Username or Passowrd fields cannot be blank.");
                return;
            }
            if (AccountManager.fileExistsForUsername(username)) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Account with this username already exists. Select another username, or try to log in.");
                return;
            }

            if (!checkPasswordStrength(password)) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Password does not meet minimum requirements. " +
                        "Your password must be at least 8 characters long and include at least one letter, one number, and one special character.");
                return;
            }

            if (!password.equals(retypedPassword)) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Passwords do not match.");
                return;
            }

            AccountManager accountManager = new AccountManager();
            accountManager.saveToFile(username, password);

            Alert accountCreatedAlert = new Alert(Alert.AlertType.INFORMATION);
            accountCreatedAlert.setTitle("Account Created");
            accountCreatedAlert.setHeaderText(null);
            accountCreatedAlert.setContentText("Account Created");

            accountCreatedAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/LoginWindowController.fxml"));
                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    Stage createAccountStage = (Stage) userNameField.getScene().getWindow();
                    createAccountStage.close();
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "An error occurred: " + ex.getMessage());
        }
    }

    @FXML
    public void handleGeneratePasswordButton(ActionEvent e) throws IOException {
        try {

            FXMLLoader loader = new FXMLLoader((getClass().getResource("/FXML/GeneratePasswordController.fxml")));
            Parent root = loader.load();

            Stage generatePasswordStage = new Stage();
            generatePasswordStage.setTitle("Password Generator");
            generatePasswordStage.initModality(Modality.APPLICATION_MODAL);
            generatePasswordStage.initOwner(userNameField.getScene().getWindow());
            generatePasswordStage.setScene(new Scene(root));
            generatePasswordStage.setResizable(false);
            generatePasswordStage.showAndWait();
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }


    private boolean checkPasswordStrength(String password){
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=])[A-Za-z\\d!@#$%^&*()_+\\-=]{8,}$";
        return Pattern.compile(regex).matcher(password).matches();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

