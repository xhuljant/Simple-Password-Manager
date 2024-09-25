package com.example.pm;

import com.example.pm.Model.*;
import javafx.scene.control.Alert;

import java.util.Locale;

public class testMain {

    public static void main(String[] args) throws Exception {

        String cardNumber="123312332331233";
        String secCode="1232";
        String expDate="12444";

        try {
            long x=Long.parseLong(cardNumber);
            Integer.parseInt(secCode);
            Integer.parseInt(expDate);

            if( (cardNumber.length()>=13&&cardNumber.length()<=17) && expDate.length()==4 && (secCode.length()<=4&&secCode.length()>=3)){
                System.out.println(true);
            }

            System.out.println("Nothing happened");


        }catch (Exception exception){
            exception.printStackTrace();
            System.out.println(false);
        }
    }

}
