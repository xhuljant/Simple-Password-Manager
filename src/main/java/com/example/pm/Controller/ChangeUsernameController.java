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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class ChangeUsernameController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField newUsernameField;
    @FXML private Button changeUsernameButton;
    private static final String DATA_DIRECTORY="Data/";


    @FXML
    public void handleChangeUsernameButton(){
        if(usernameField.getText().isEmpty() || passwordField.getText().isEmpty() || newUsernameField.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR,"Error","Make sure all fields are not empty and try again.");
            return;
        }

        boolean verifiedAccess = AccountManager.verifyAccess(usernameField.getText(),passwordField.getText());

        if(verifiedAccess){
            try{
                EncryptionService encryptionService =  new EncryptionService();
                Path dataDir = AccountManager.getAppDataDirectory().resolve("data");

                Path originalFile = dataDir.resolve(encryptionService.generateFileName(usernameField.getText()));
                Path newFile = dataDir.resolve(encryptionService.generateFileName(newUsernameField.getText()));

                if(!Files.exists(originalFile)){
                    showAlert(Alert.AlertType.ERROR,"Error","No file found matching username entered.");
                    return;
                }

                if(Files.exists(newFile)){
                    showAlert(Alert.AlertType.ERROR,"Error","Username is being used by another account, please login to existing account and delete file if you would like to use this username.");
                    return;
                }

                Files.move(originalFile,newFile, StandardCopyOption.REPLACE_EXISTING);

                Alert passwordChangedAlert = new Alert(Alert.AlertType.INFORMATION);
                passwordChangedAlert.setTitle("Username Changed");
                passwordChangedAlert.setHeaderText(null);
                passwordChangedAlert.setContentText("Username changed, please login again with new username.");

                passwordChangedAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
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
                System.out.println("File renamed.");
            }catch (FileNotFoundException e){
                System.out.println(e.getMessage());
            }catch (FileAlreadyExistsException e){
                System.out.println(e.getMessage());
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }else {
            showAlert(Alert.AlertType.ERROR,"Error","Incorrect username or password.");
            return;
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
