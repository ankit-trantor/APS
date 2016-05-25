package es.esy.chhg.chatapp.application;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import es.esy.chhg.chatapp.data.Chat;
import es.esy.chhg.chatapp.socketclient.SocketClient;

public class ChatApplication extends Application {
    private List<Chat> mListChat;
    private SocketClient mSocketClient;

    public void updateListChat(List<Chat> listChat) {
        if (listChat != null) {
            mListChat = new ArrayList<>(listChat);
        }
    }

    public List<Chat> getListChat() {
        if (mListChat == null) {
            mListChat = new ArrayList<>();
        }

        return mListChat;
    }

    public SocketClient getSocketClient() {
        return mSocketClient;
    }

    public void setSocketClient(SocketClient socketClient) {
        mSocketClient = socketClient;
    }
}