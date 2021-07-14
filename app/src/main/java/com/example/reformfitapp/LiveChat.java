package com.example.reformfitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.livechatinc.inappchat.ChatWindowConfiguration;
import com.livechatinc.inappchat.ChatWindowErrorType;
import com.livechatinc.inappchat.ChatWindowView;
import com.livechatinc.inappchat.models.NewMessageModel;

import java.util.HashMap;

public class LiveChat extends AppCompatActivity {
    ChatWindowConfiguration configuration;

    ChatWindowView fullScreenChatWindow;

    ChatWindowView emmbeddedChatWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_chat);



        startFullScreenChat();




    }

    public void startFullScreenChat() {
        String visitorName = "";
        String visitorEmail = "";
        if(((GlobalVariableApplication) getApplication()).getLogIn()){

            MindbodyClientResponseModel mindbodyClientResponseModel = ((GlobalVariableApplication) getApplication()).getMindbodyClientResponseModel();
            visitorName = mindbodyClientResponseModel.getFirstName();
            visitorEmail = mindbodyClientResponseModel.getEmail();
        }

        HashMap<String, String> customParamsMap = null;
        ChatWindowConfiguration configuration = new ChatWindowConfiguration(
                "12951837",
                "",
                visitorName,
                visitorEmail,
                customParamsMap
        );
        if (fullScreenChatWindow == null) {
            ChatWindowView fullScreenChatWindow = ChatWindowView.createAndAttachChatWindowInstance(LiveChat.this);
            fullScreenChatWindow.setUpWindow(configuration);
            fullScreenChatWindow.onBackPressed();
            fullScreenChatWindow.setUpListener(new ChatWindowView.ChatWindowEventsListener() {
                @Override
                public void onChatWindowVisibilityChanged(boolean visible) {

                }

                @Override
                public void onNewMessage(NewMessageModel message, boolean windowVisible) {

                }

                @Override
                public void onStartFilePickerActivity(Intent intent, int requestCode) {

                }

                @Override
                public boolean onError(ChatWindowErrorType errorType, int errorCode, String errorDescription) {
                    return false;
                }

                @Override
                public boolean handleUri(Uri uri) {
                    return false;
                }
            });
            fullScreenChatWindow.initialize();
        }
        fullScreenChatWindow.showChatWindow();
    }

    public void startEmmbeddedChat(View view) {
        if (!emmbeddedChatWindow.isInitialized()) {
            emmbeddedChatWindow.setUpWindow(configuration);
            emmbeddedChatWindow.setUpListener(new ChatWindowView.ChatWindowEventsListener() {
                @Override
                public void onChatWindowVisibilityChanged(boolean visible) {

                }

                @Override
                public void onNewMessage(NewMessageModel message, boolean windowVisible) {

                }

                @Override
                public void onStartFilePickerActivity(Intent intent, int requestCode) {

                }

                @Override
                public boolean onError(ChatWindowErrorType errorType, int errorCode, String errorDescription) {
                    return false;
                }

                @Override
                public boolean handleUri(Uri uri) {
                    return false;
                }
            });
            emmbeddedChatWindow.initialize();
        }
        emmbeddedChatWindow.showChatWindow();
    }


}