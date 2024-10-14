package com.example.pm;

import com.example.pm.Model.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.smartcardio.Card;
import javax.swing.*;
import java.io.File;
import java.util.Locale;

import static javafx.application.Application.launch;

public class testMain extends Application {

    private String selectedFilePath = "";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("File Picker App");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // Add file filters if needed
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Button button = new Button("Select File");
        button.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                selectedFilePath = selectedFile.getAbsolutePath();
                System.out.println("Selected file: " + selectedFilePath);
            }
        });

        VBox vBox = new VBox(10, button);
        Scene scene = new Scene(vBox, 300, 200);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        CardAccount c1 = new CardAccount.Builder("aemx")
                .cardHolderName("Xhuljan")
                .cardNumber("1231231")
                .cardExpDate("1223")
                .cardSecCode("123")
                .build();

        CardAccount c2 = new CardAccount.Builder("aemx")
                .cardHolderName("Xhuljan")
                .cardNumber("1231231")
                .cardExpDate("1223")
                .cardSecCode("123")
                .build();

        int accountAdded=c1.compareTo(c2);
        if(accountAdded==0)
            System.out.println("same account");
        else
            System.out.println("diff account");

    }
}
