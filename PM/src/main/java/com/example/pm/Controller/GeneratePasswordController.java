package com.example.pm.Controller;

import com.example.pm.Model.PasswordGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;

public class GeneratePasswordController {
    @FXML private TextField passwordField;
    @FXML private CheckBox upperCaseLetters;
    @FXML private CheckBox numberCharacters;
    @FXML private CheckBox specialCharacters;
    @FXML private CheckBox lowerCaseLetters;
    @FXML private Button copyButton;
    @FXML private Button generateButton;
    @FXML private Slider passwordSizeSlider;
    @FXML private Button closeButton;

    @FXML
    public void initialize(){
        String password= PasswordGenerator.generatePassword(lowerCaseLetters.isSelected(),upperCaseLetters.isSelected(),specialCharacters.isSelected(),numberCharacters.isSelected(),(int)passwordSizeSlider.getValue());
        passwordField.setText(password);
    }

    @FXML
    public void handleGenerateButton(ActionEvent e){
        int passwordLength=(int)passwordSizeSlider.getValue();
        if(!lowerCaseLetters.isSelected()&!upperCaseLetters.isSelected()&!specialCharacters.isSelected()&!numberCharacters.isSelected()){
            showAlert(Alert.AlertType.WARNING,"Error","At least one character type is needed. Please select one and try again.");
        }
        String password= PasswordGenerator.generatePassword(lowerCaseLetters.isSelected(),upperCaseLetters.isSelected(),specialCharacters.isSelected(),numberCharacters.isSelected(),(int)passwordSizeSlider.getValue());
        passwordField.setText(password);
    }

    @FXML
    public void handleCopyButton(){
        final Clipboard clipboard=Clipboard.getSystemClipboard();
        final ClipboardContent text=new ClipboardContent();
        text.putString(passwordField.getText());
        clipboard.setContent(text);
    }

    @FXML
    public void handleCloseButton(){
        passwordField.getScene().getWindow().hide();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
