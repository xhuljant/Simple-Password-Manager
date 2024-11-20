package com.example.pm.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class CreateFolderController {
    @FXML private TextField folderNameTextField;
    @FXML private Button saveButton;

    private Consumer<String> folderNameCallback;

    public void setFolderNameCallback(Consumer<String> callback){
        this.folderNameCallback=callback;
    }

    public void handleSaveButton(){
        String folderName=folderNameTextField.getText();
        if(folderName!=null && !folderName.trim().isEmpty()){
            folderNameCallback.accept(folderName);
            ((Stage) folderNameTextField.getScene().getWindow()).close();
        }
    }
}
