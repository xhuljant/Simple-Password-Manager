package com.example.pm.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Generates a random password that matches criteria sent in as parameter by user
 * 2 Strings are created, one is the password and the other is a String holding all available characters that can be in the password
 * The first 4 characters are added from each required group of characters to ensure each required character appears at least once
 * The password is then appended with characters from availableChars until password is the length of passwordLength
 * The password is then loaded into a list of characters so that Collections.shuffle can be called to shuffle all characters to ensure password randomness
 * List is converted back into a String builder object, and then returned as a string by calling .toString
 */
public class PasswordGenerator {
    private static final Random random = new Random();

    private static final String LOWER_CASE_CHARACTERS="abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER_CASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()_+-=[]{}|;:,.<>?";
    private static final String NUMBERS = "0123456789";

    public static String generatePassword(boolean lowerCaseLetters,boolean upperCaseLetters,boolean specialCharacters,boolean numbers,int passwordLength){
        StringBuilder availableChars=new StringBuilder();
        StringBuilder password=new StringBuilder();

        if(lowerCaseLetters){
            availableChars.append(LOWER_CASE_CHARACTERS);
            password.append(LOWER_CASE_CHARACTERS.charAt(random.nextInt(LOWER_CASE_CHARACTERS.length())));
        }

        if(upperCaseLetters){
            availableChars.append(UPPER_CASE_CHARACTERS);
            password.append(UPPER_CASE_CHARACTERS.charAt(random.nextInt(UPPER_CASE_CHARACTERS.length())));
        }

        if(specialCharacters){
            availableChars.append(SPECIAL_CHARACTERS);
            password.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));
        }

        if(numbers){
            availableChars.append(NUMBERS);
            password.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        }

        for(int i=password.length();i<passwordLength;i++){
            password.append(availableChars.charAt(random.nextInt(availableChars.length())));
        }

        List<Character> passwordCharacters=new ArrayList<>();
        for(char c:password.toString().toCharArray()){
            passwordCharacters.add(c);
        }

        Collections.shuffle(passwordCharacters);

        StringBuilder finalPassword=new StringBuilder();

        for(Character c:passwordCharacters){
            finalPassword.append(c);
        }

        return finalPassword.toString();
    }
}
