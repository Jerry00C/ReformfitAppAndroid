package com.example.reformfitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.livechatinc.inappchat.ChatWindowConfiguration;
import com.livechatinc.inappchat.ChatWindowView;

import java.util.HashMap;

public class LiveChat extends AppCompatActivity {
    ChatWindowConfiguration configuration;

    ChatWindowView fullScreenChatWindow;

    ChatWindowView emmbeddedChatWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_chat);


        HashMap<String, String> customParamsMap = null;
        configuration = new ChatWindowConfiguration(
                "your_licence_number",
                "group_id",
                "Visitor name",
                "visitor@email.com",
                customParamsMap
        );

        startFullScreenChat();


        ChatWindowView chatWindowView = new ChatWindowView(LiveChat.this);
        emmbeddedChatWindow.findViewById(R.id.embedded_chat_window);


    }

    public void startFullScreenChat() {
        if (fullScreenChatWindow == null) {
            fullScreenChatWindow = ChatWindowView.createAndAttachChatWindowInstance(LiveChat.this);
            fullScreenChatWindow.setUpWindow(configuration);
            fullScreenChatWindow.setUpListener((ChatWindowView.ChatWindowEventsListener) getApplicationContext());
            fullScreenChatWindow.initialize();
        }
        fullScreenChatWindow.showChatWindow();
    }

    public void startEmmbeddedChat(View view) {
        if (!emmbeddedChatWindow.isInitialized()) {
            emmbeddedChatWindow.setUpWindow(configuration);
            emmbeddedChatWindow.setUpListener((ChatWindowView.ChatWindowEventsListener) getApplicationContext());
            emmbeddedChatWindow.initialize();
        }
        emmbeddedChatWindow.showChatWindow();
    }


}