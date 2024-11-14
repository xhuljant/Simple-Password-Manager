package com.example.pm.Controller;

import com.example.pm.Model.AccountManager;
import com.example.pm.Model.EncryptionService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ChangePasswordController {

    @FXML private TextField userNameField;
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField retypedNewPasswordField;
    @FXML private Button changePasswordButton;

    private AccountManager accountManager;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=])[A-Za-z\\d!@#$%^&*()_+\\-=]{8,}$");


    @FXML
    public void initialize(){
        accountManager=new AccountManager();
    }

    @FXML public void handleChangePasswordButton() throws Exception {
       if(userNameField.getText().isEmpty() || currentPasswordField.getText().isEmpty() || newPasswordField.getText().isEmpty() || retypedNewPasswordField.getText().isEmpty()){
           showAlert(Alert.AlertType.ERROR,"Error","Make sure all fields are not empty and try again.");
           return;
       }

       if(!AccountManager.fileExistsForUsername(userNameField.getText())){
            showAlert(Alert.AlertType.ERROR,"Error","No file found for username.");
            return;
        }

       if(!AccountManager.verifyAccess(userNameField.getText(),currentPasswordField.getText())){
           showAlert(Alert.AlertType.ERROR,"Error","Incorrect username or password");
           return;
       }

       if(!newPasswordField.getText().equals(retypedNewPasswordField.getText())){
           showAlert(Alert.AlertType.ERROR,"Error","New passwords do not match.");
           return;
       }

       if(!PASSWORD_PATTERN.matcher(newPasswordField.getText()).matches()){
           showAlert(Alert.AlertType.ERROR,"Error","New password does not match requirements.");
           return;
       }

       try{
           accountManager = AccountManager.loadFromFile(userNameField.getText(),currentPasswordField.getText());
           if(accountManager.changePassword(userNameField.getText(),currentPasswordField.getText(),newPasswordField.getText())){

               Alert passwordChangedAlert = new Alert(Alert.AlertType.INFORMATION);
               passwordChangedAlert.setTitle("Password Changed");
               passwordChangedAlert.setHeaderText(null);
               passwordChangedAlert.setContentText("Password changed, please login again with new password.");

               passwordChangedAlert.showAndWait().ifPresent(response -> {
                   if (response == ButtonType.OK) {
                       Window currentWindow = userNameField.getScene().getWindow();
                       List<Window> windows = new ArrayList<>(Window.getWindows());
                       for (Window window : windows) {
                           if (window instanceof Stage && window != currentWindow) {
                               ((Stage) window).close();
                           }
                       }

                       FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/LoginWindowController.fxml"));
                       Parent root = null;
                       try {
                           root = loader.load();
                       } catch (IOException ex) {
                           throw new RuntimeException(ex);
                       }

                       Stage loginStage = new Stage();
                       loginStage.setTitle("Password Manager");
                       loginStage.setScene(new Scene(root));

                       ((Stage) currentWindow).close();

                       loginStage.show();
                   }
               });
           }

           System.out.println("Password changed.");
       }catch (Exception e){
           System.out.println("Unable to change password.");
           System.out.println(e.getMessage());
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
