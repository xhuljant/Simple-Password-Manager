package com.example.pm.Controller;

import com.example.pm.Model.AccountManager;
import com.example.pm.Model.EncryptionService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ImportWindowController {
    @FXML public Button importButton;
    @FXML private Button fileChooserButton;
    @FXML private TextField fileLocation;
    @FXML private PasswordField passwordTextField;
    @FXML private PasswordField usernameTextField;


    @FXML
    private void initialize(){
        System.out.println("ImportWindowController Initialized.");
    }

    @FXML
    private void handleFileBrowseButton() throws IOException {
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("Select File");
        Stage currentStage = (Stage) fileChooserButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(currentStage);
        if(selectedFile!=null) {
            fileLocation.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void handleImportButton(){
        EncryptionService encryptionService = new EncryptionService();
        File selectedFile = new File(fileLocation.getText());

        if(fileLocation.getText().isEmpty() || usernameTextField.getText().isEmpty() || passwordTextField.getText().isEmpty() ) {
            showAlert(Alert.AlertType.WARNING, "Error", "Please fill in all fields.");
            return;
        }

        if(selectedFile.getName().equals(encryptionService.generateFileName(usernameTextField.getText()))){
            String encryptedData;

            try (BufferedReader reader = new BufferedReader(new FileReader(fileLocation.getText()))) {
                encryptedData = reader.readLine();

                encryptionService.decrypt(encryptedData,passwordTextField.getText());

                File destinationFile=new File("Data/"+encryptionService.generateFileName(usernameTextField.getText()));

                if(destinationFile.exists()){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm Export");
                    alert.setHeaderText("Export File");
                    alert.setContentText("File already exists in this directory, would you like to replace the file?");

                    alert.showAndWait().ifPresent(response -> {
                        if(response== ButtonType.OK){
                            try {
                                Files.copy(selectedFile.toPath(),destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                importButton.getScene().getWindow().hide();
                                System.out.println("File imported.");
                            } catch (IOException e) {
                                showAlert(Alert.AlertType.WARNING,"Error","File was not imported successfully.");
                                e.printStackTrace();
                            }
                        }else{
                            System.out.println("File not imported.");
                        }
                    });

                }else {
                    Files.copy(selectedFile.toPath(),destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    importButton.getScene().getWindow().hide();
                    System.out.println("File imported.");
                }
            }catch (Exception e){
                System.out.println("Incorrect Password.");
                showAlert(Alert.AlertType.WARNING,"Error","Incorrect Password.");
            }
        }else {
            System.out.println("Incorrect Username.");
            showAlert(Alert.AlertType.WARNING,"Error","Incorrect Username.");
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
