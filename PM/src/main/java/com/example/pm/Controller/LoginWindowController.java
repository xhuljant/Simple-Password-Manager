package com.example.pm.Controller;

import com.example.pm.Model.Account;
import com.example.pm.Model.AccountManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;


import java.io.IOException;
import java.nio.file.Paths;
import java.util.Locale;

public class LoginWindowController {
    @FXML private TextField userNameField;
    @FXML private TextField passwordField;
    @FXML private TextField retypePasswordField;
    @FXML private Button loginButton;
    @FXML private Button createAccountButton;

    @FXML
    private void initialize() {
        // This method is called automatically after the FXML file is loaded
        System.out.println("LoginWindowController initialized");
    }
    @FXML
    private void handleLoginButton(ActionEvent event) throws Exception {

        String username = "xhuljan";
        String password = "qwertyui1!";

        /* uncomment once testing is done, remove above code
        String username = userNameField.getText();
        String password = passwordField.getText();
         */

        if(AccountManager.fileExistsForUsername(username.toLowerCase(Locale.ROOT))){
            if(AccountManager.verifyAccess(username,password))
                openMainWindow(username,password);
            else
                showAlert(Alert.AlertType.WARNING,"","Incorrect passowrd.");
        }else {
            showAlert(Alert.AlertType.WARNING,"","Username not found.");
        }
    }

    @FXML
    private void handleCreateAccountButton(ActionEvent event) throws Exception {
        openCreateAccountWindow();
    }

    @FXML
    private void openMainWindow(String username, String password) throws Exception {
        try{
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/pm/MainWindowController.fxml"));
            Parent root=loader.load();

            MainWindowController mainWindowController=loader.getController();
            AccountManager accountManager=AccountManager.loadFromFile(username,password);

            mainWindowController.setAccountManager(accountManager,username,password);

            Stage mainStage=new Stage();
            mainStage.setTitle("Passoword Manager");
            mainStage.setScene(new Scene(root));
            mainStage.setResizable(false);
            mainStage.show();

            //close previous window
            Window window = userNameField.getScene().getWindow();
            window.hide();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(e.toString());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void openCreateAccountWindow() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pm/CreateAccountController.fxml"));
            Parent root = loader.load();

            CreateAccountController createAccountController = loader.getController();

            Stage createAccountStage = new Stage();
            createAccountStage.setTitle("Create New Account");
            createAccountStage.setScene(new Scene(root));
            createAccountStage.setResizable(false);

            Window window = userNameField.getScene().getWindow();
            window.hide();

            createAccountStage.show();
        }catch (Exception e){
            showAlert(Alert.AlertType.WARNING,"Error",e.toString());
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
