package com.pagepal.capstone.services;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.pagepal.capstone.dtos.firebase.FirebaseMessageData;

import java.util.List;

public interface FirebaseMessagingService {
    String sendNotification(FirebaseMessageData data, String token) throws FirebaseMessagingException;
    String sendMultipleNotifications(FirebaseMessageData data, List<String> tokens) throws FirebaseMessagingException;
}
