package org.linx.apick.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatePassword {

    public static boolean validate(String password) {
        if (password.length() < 8 || password.length() > 12) {
            return false;
        }
        // Verifica se a senha corresponde aos parâmetros
        Pattern upperCase = Pattern.compile("[A-Z]");
        Pattern lowerCase = Pattern.compile("[a-z]");
        Pattern specialChar = Pattern.compile("[!@#$%^&*()-_+=<>?/{}\\[\\]]");
        Pattern numChar = Pattern.compile("[0-9]");

        Matcher upperCaseMatcher = upperCase.matcher(password);
        Matcher lowerCaseMatcher = lowerCase.matcher(password);
        Matcher specialCharMatcher = specialChar.matcher(password);
        Matcher numCharMatcher = numChar.matcher(password);

        return upperCaseMatcher.find() && lowerCaseMatcher.find()
                && specialCharMatcher.find() && numCharMatcher.find();
    }
}
