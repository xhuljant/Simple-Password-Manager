package com.example.pm.Controller;

import com.example.pm.Model.EncryptionService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class SettingsWindowController {
    @FXML private Text ExportDataFile;
    @FXML private Text DeleteAccount;
    @FXML private Button changeUsernameButton;
    @FXML private Button changeMasterPasswordButton;
    @FXML private Button deleteAccountButton;

    private String username;

    public void handleChangeMasterPasswordButton(){
        try {
            FXMLLoader loader = new FXMLLoader((getClass().getResource("/com/example/pm/ChangePasswordController.fxml")));
            Parent root = loader.load();

            Stage generatePasswordStage = new Stage();
            generatePasswordStage.setTitle("Change Password");
            generatePasswordStage.initModality(Modality.APPLICATION_MODAL);
            generatePasswordStage.initOwner(changeUsernameButton.getScene().getWindow());
            generatePasswordStage.setScene(new Scene(root));
            generatePasswordStage.setResizable(false);
            generatePasswordStage.showAndWait();
        } catch (Exception exception) {
            exception.printStackTrace();
            showAlert(Alert.AlertType.WARNING, "Error", "Unable change password.");
        }
    }

    @FXML
    public void handleChangeUsernameButton(){
        try {
            FXMLLoader loader = new FXMLLoader((getClass().getResource("/com/example/pm/ChangeUsernameController.fxml")));
            Parent root = loader.load();

            Stage generatePasswordStage = new Stage();
            generatePasswordStage.setTitle("Change Username");
            generatePasswordStage.initModality(Modality.APPLICATION_MODAL);
            generatePasswordStage.initOwner(changeUsernameButton.getScene().getWindow());
            generatePasswordStage.setScene(new Scene(root));
            generatePasswordStage.setResizable(false);
            generatePasswordStage.showAndWait();
        } catch (Exception exception) {
            exception.printStackTrace();
            showAlert(Alert.AlertType.WARNING, "Error", "Unable to change username.");
        }
    }

    @FXML
    public void handleDeleteAccountButton() {
        try {
            FXMLLoader loader = new FXMLLoader((getClass().getResource("/com/example/pm/DeleteAccountController.fxml")));
            Parent root = loader.load();

            Stage generatePasswordStage = new Stage();
            generatePasswordStage.setTitle("Delete File");
            generatePasswordStage.initModality(Modality.APPLICATION_MODAL);
            generatePasswordStage.initOwner(changeUsernameButton.getScene().getWindow());
            generatePasswordStage.setScene(new Scene(root));
            generatePasswordStage.setResizable(false);
            generatePasswordStage.showAndWait();
        } catch (Exception exception) {
            exception.printStackTrace();
            showAlert(Alert.AlertType.WARNING, "Error", "Unable to delete file");
        }
    }

    @FXML
    public void handleExportButton(){
        EncryptionService encryptionService=new EncryptionService();
        File sourceFile= new File("Data/"+encryptionService.generateFileName(username));

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folder");
        Stage currentStage = (Stage) DeleteAccount.getScene().getWindow();
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


    public void setUsername(String username){
        this.username=username;
    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
