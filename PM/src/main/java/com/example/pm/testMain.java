package com.example.pm;

import com.example.pm.Model.*;
import javafx.scene.control.Alert;

import java.util.Locale;

public class testMain {

    public static void main(String[] args) throws Exception {

        LoginAccount l1=new LoginAccount.Builder("Facebook").username("xhuljant").notes("test").build();
        System.out.println(l1.toString());

    }

}
