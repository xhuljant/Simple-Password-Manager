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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeleteAccountController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button deleteAccountButton;

    @FXML
    public void handleDeleteAccountButton(){
        if(usernameField.getText().isEmpty() || passwordField.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR,"Error","Username or password cannot be empty.");
            return;
        }

        if(!AccountManager.verifyAccess(usernameField.getText(),passwordField.getText())){
            showAlert(Alert.AlertType.ERROR,"Error","Unable to verify username and password.");
            return;
        }

        Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDelete.setTitle("Confirm Delete");
        confirmDelete.setHeaderText("Confirm Delete");
        confirmDelete.setContentText("Are you sure you want to delete file? This cannot be undone.");

        Optional<ButtonType> result = confirmDelete.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK){
            try{
                EncryptionService encryptionService=new EncryptionService();

                Path dataDir = AccountManager.getAppDataDirectory().resolve("Data");
                Path accountFile = dataDir.resolve(encryptionService.generateFileName(usernameField.getText()));

                if(Files.deleteIfExists(accountFile)){
                    Alert accountDeleted = new Alert(Alert.AlertType.INFORMATION);
                    accountDeleted.setTitle("Account Deleted");
                    accountDeleted.setHeaderText(null);
                    accountDeleted.setContentText("Account deleted.");

                    accountDeleted.showAndWait().ifPresent(response -> {
                        if(response == ButtonType.OK){
                            Window currentWindow = usernameField.getScene().getWindow();
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
            }catch (Exception e){
                showAlert(Alert.AlertType.ERROR,"Error","Unable to delete file.");
                System.err.println(e.getMessage());
            }
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
