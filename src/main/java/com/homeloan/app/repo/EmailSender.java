package com.homeloan.app.repo;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

public interface EmailSender {

    void sendEmail(String to, String email,String subject, String from) throws MessagingException, UnsupportedEncodingException;

  
}