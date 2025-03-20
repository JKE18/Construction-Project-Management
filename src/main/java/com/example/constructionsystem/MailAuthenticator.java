package com.example.constructionsystem;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailAuthenticator extends Authenticator {
    private static final String EMAIL_FROM = System.getenv("SMTP_EMAIL");
    private static final String EMAIL_PASSWORD = System.getenv("SMTP_PASSWORD");
    @Override
    protected PasswordAuthentication getPasswordAuthentication(){
        if (EMAIL_FROM == null || EMAIL_PASSWORD == null) {
            throw new IllegalStateException("Brak ustawionych zmiennych środowiskowych SMTP_EMAIL i SMTP_PASSWORD!");
        }
        return new PasswordAuthentication(EMAIL_FROM, EMAIL_PASSWORD);
    }
}
