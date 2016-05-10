package clientservice;

import data.Chat;
import data.User;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ClientService {

    private Socket mSocket;
    private ObjectOutputStream mObjectOutputStream;

    private User mUser;

    public ClientService(User user) {
        mUser = user;

        try {
            mSocket = new Socket(
                    constants.Constants.SocketConstants.ADDRESS_IP,
                    constants.Constants.SocketConstants.PORT
            );

            mObjectOutputStream = new ObjectOutputStream(mSocket.getOutputStream());

            new Thread(new ListenerSocket()).start();

        } catch (IOException ex) {
            Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMessageOrFile(Chat chat) {
        try {
            chat.setNameUser(mUser.getUsername());
            chat.setTimeMessage(System.currentTimeMillis());
            mObjectOutputStream.writeObject(chat);
        } catch (IOException ex) {
            Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void connectedChat() {
        Chat chat = new Chat();

        chat.setAction(Chat.Action.Connect);

        sendMessageOrFile(chat);
    }

    public void disconnectChat() {
        Chat chat = new Chat();

        chat.setAction(Chat.Action.Disconnect);

        sendMessageOrFile(chat);
    }

    private void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
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

                            break;

                        case Disconnect:

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
            } catch (IOException ex) {
                Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private class SaveFile implements Runnable {

            private Chat mChat;

            public SaveFile(Chat chat) {
                mChat = chat;
            }

            @Override
            public void run() {
                saveFile(mChat.getFile());
            }

            private void saveFile(File file) {
                FileInputStream fileInputStream = null;
                FileOutputStream fileOutputStream = null;
                FileChannel fileChannelInput = null;
                FileChannel fileChannelOutput = null;
                try {
                    fileInputStream = new FileInputStream(file);
                    fileOutputStream = new FileOutputStream("C:\\a\\" + file.getName());
                    // CRIAR UMA PASTA SE NAO TIVER

                    fileChannelInput = fileInputStream.getChannel();
                    fileChannelOutput = fileOutputStream.getChannel();

                    long size = fileChannelInput.size();

                    fileChannelInput.transferTo(0, size, fileChannelOutput);
                    
                    fileOutputStream.flush();

                    getFile(mChat);

                } catch (IOException ex) {
                    Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    closeStream(fileInputStream);
                    closeStream(fileOutputStream);
                    closeStream(fileChannelInput);
                    closeStream(fileChannelOutput);
                }
            }
        }
    }

    //Metodos abstratos que serão dados que vão vir do server
    public abstract void getMessage(Chat chat);

    public abstract void getUsersOnline(Chat chat);

    public abstract void getFile(Chat chat);
}