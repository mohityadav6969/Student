package com.example.easychat.utils;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;

public class FirebaseUtil {

    public static String currentUserId() {
        return FirebaseAuth.getInstance().getUid();
    }

    public static boolean isLoggedIn() {
        return currentUserId() != null;
    }

    public static DocumentReference currentUserDetails() {
        if (!isLoggedIn()) {
            throw new IllegalStateException("User is not logged in");
        }
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

    public static CollectionReference allUserCollectionReference() {
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static DocumentReference getChatroomReference(String chatroomId) {
        if (chatroomId == null) {
            throw new IllegalArgumentException("Chatroom ID cannot be null");
        }
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
    }

    public static CollectionReference getChatroomMessageReference(String chatroomId) {
        return getChatroomReference(chatroomId).collection("chats");
    }

    public static String getChatroomId(String userId1, String userId2) {
        if (userId1 == null || userId2 == null) {
            throw new IllegalArgumentException("User IDs cannot be null");
        }
        return userId1.hashCode() < userId2.hashCode() ? userId1 + "_" + userId2 : userId2 + "_" + userId1;
    }

    public static CollectionReference allChatroomCollectionReference() {
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    public static DocumentReference getOtherUserFromChatroom(List<String> userIds) {
        if (userIds == null || userIds.size() < 2) {
            throw new IllegalArgumentException("User IDs list is invalid");
        }
        String otherUserId = userIds.get(0).equals(currentUserId()) ? userIds.get(1) : userIds.get(0);
        return allUserCollectionReference().document(otherUserId);
    }

    public static String timestampToString(Timestamp timestamp) {
        String format = new SimpleDateFormat("HH:mm").format(timestamp.toDate());
        return format;
    }

    public static void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    public static StorageReference getCurrentProfilePicStorageRef() {
        if (!isLoggedIn()) {
            throw new IllegalStateException("User is not logged in");
        }
        return FirebaseStorage.getInstance().getReference().child("profile_pic").child(currentUserId());
    }

    public static StorageReference getOtherProfilePicStorageRef(String otherUserId) {
        if (otherUserId == null) {
            throw new IllegalArgumentException("Other user ID cannot be null");
        }
        return FirebaseStorage.getInstance().getReference().child("profile_pic").child(otherUserId);
    }
}
