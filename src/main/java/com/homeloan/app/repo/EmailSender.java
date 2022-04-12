package com.homeloan.app.repo;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

public interface EmailSender {

    void sendOnApprove(String to, String email) throws MessagingException, UnsupportedEncodingException;

    void sendOnReject(String email, String buildResetEmail) throws MessagingException, UnsupportedEncodingException;

}