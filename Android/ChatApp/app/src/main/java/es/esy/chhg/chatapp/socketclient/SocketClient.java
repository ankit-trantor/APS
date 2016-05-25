package es.esy.chhg.chatapp.socketclient;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import es.esy.chhg.chatapp.constants.Constants;
import es.esy.chhg.chatapp.data.Chat;
import es.esy.chhg.chatapp.data.User;
import es.esy.chhg.chatapp.utils.PreferencesUtil;

public abstract class SocketClient extends Thread {
    private final String PATH_SAVED_FILES = "chat_files";

    private Socket mSocket;
    private ObjectOutputStream mObjectOutputStream;

    private User mUser;

    private Context mContext;

    public SocketClient(Context context) {
        mUser = PreferencesUtil.getUserConnected(context);
        mContext = context;


        start(); // inicia a thread
    }

    @Override
    public void run() {
        try {
            mSocket = new Socket(
                    Constants.SocketConstants.ADDRESS_IP,
                    Constants.SocketConstants.PORT
            );

            mObjectOutputStream = new ObjectOutputStream(mSocket.getOutputStream());

            new Thread(new ListenerSocket()).start();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void send(Chat chat) {
        try {
            chat.setTimeMessage(System.currentTimeMillis());
            chat.setNameUser(mUser.getUsername());
            mObjectOutputStream.writeObject(chat);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendMessage(Chat chat) {
        send(chat);
    }

    public void sendFile(Chat chat) {
        new Thread(new SendFile(chat)).start();
    }

    public void connectedChat() {
        Chat chat = new Chat();

        chat.setAction(Chat.Action.Connect);

        send(chat);
    }

    public void disconnectChat() {
        Chat chat = new Chat();

        chat.setAction(Chat.Action.Disconnect);

        sendMessage(chat);
    }

    private void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private class ListenerSocket implements Runnable {

        private ObjectInputStream mObjectInputStream;

        public ListenerSocket() throws IOException {
            mObjectInputStream = new ObjectInputStream(mSocket.getInputStream());

            connectedChat();
            // Envia que o fulano entrou no chat
        }

        @Override
        public void run() {
            try {
                Chat chat = null;

                while ((chat = (Chat) mObjectInputStream.readObject()) != null) {

                    switch (chat.getAction()) {
                        case Connect:
                            getUserConnected(chat);
                            break;

                        case Disconnect:
                            getUserDisconnected(chat);
                            break;

                        case SendOne:
                            getMessage(chat);

                            break;

                        case UsersOnline:
                            getUsersOnline(chat);

                            break;

                        case File:
                            new Thread(new SaveFile(chat)).start();
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private class SaveFile implements Runnable {

            private Chat mChat;

            public SaveFile(Chat chat) {
                mChat = chat;
            }

            @Override
            public void run() {
                saveFile();
            }

            private void saveFile() {
                FileOutputStream fileOutputStream = null;
                File fileSave;
                try {
                    File fileRoot = new File(mContext.getExternalFilesDir(null), PATH_SAVED_FILES + "/");

                    if (fileRoot.exists() || fileRoot.mkdirs()) {

                        fileSave = new File(fileRoot.getAbsolutePath() + "/" + mChat.getMessage());

                        fileOutputStream = new FileOutputStream(fileSave);
                        fileOutputStream.write(mChat.getFileByte());

                        mChat.setFile(fileSave);

                        getFile(mChat);
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    closeStream(fileOutputStream);
                }
            }
        }
    }

    private class SendFile implements Runnable {

        private Chat mChat;

        public SendFile(Chat chat) {
            mChat = chat;

            mChat.setMessage(chat.getFile().getName()); // Vai sempre enviar o nome do arquivo
        }

        @Override
        public void run() {
            convertFileToByte();
        }

        private void convertFileToByte() {
            FileInputStream fileInputStream = null;
            ByteArrayOutputStream byteArrayOutputStream = null;
            try {
                fileInputStream = new FileInputStream(mChat.getFile());
                byteArrayOutputStream = new ByteArrayOutputStream();

                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];

                int size = 0;
                while ((size = fileInputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, size);
                }

                mChat.setFileByte(byteArrayOutputStream.toByteArray());

                send(mChat);

            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                closeStream(fileInputStream);
                closeStream(byteArrayOutputStream);
            }
        }
    }

    //Metodos abstratos que serão dados que vão vir do server
    public abstract void getMessage(Chat chat);

    public abstract void getUsersOnline(Chat chat);

    public abstract void getFile(Chat chat);

    public abstract void getUserConnected(Chat chat);

    public abstract void getUserDisconnected(Chat chat);
}