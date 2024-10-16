package com.example.pm;

import com.example.pm.Model.AccountManager;
import com.example.pm.Model.EncryptionService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("/com/example/pm/LoginWindowController.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Password Manager");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}