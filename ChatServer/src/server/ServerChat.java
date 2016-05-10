package server;

import data.Chat;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServerChat {

    private ServerSocket mServerSocket;
    private Socket mSocket;
    private Map<String, ObjectOutputStream> mUsersOnlines = new HashMap<>();
    // Vai armazenar o nome e ObjectOutputStream dos users para enviar messagens
    // Quando precisar

    public ServerChat() {
        try {
            mServerSocket = new ServerSocket(constants.Constants.SocketConstants.PORT);

            System.out.println("O servidor está executando...");

            while (true) {
                mSocket = mServerSocket.accept();

                //Alguém conectou. Já inicia a thread para esse user que
                // Ficará enviando/recebendo mensagens
                new Thread(new ListenerSocket()).start();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private class ListenerSocket implements Runnable {

        private ObjectOutputStream mObjectOutputStream;
        private ObjectInputStream mObjectInputStream;

        public ListenerSocket() {
            try {

                mObjectOutputStream = new ObjectOutputStream(mSocket.getOutputStream());
                mObjectInputStream = new ObjectInputStream(mSocket.getInputStream());

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            Chat chat = null;
            try {
                while ((chat = (Chat) mObjectInputStream.readObject()) != null) {

                    switch (chat.getAction()) {
                        case Connect:
                            System.out.println(chat.getNameUser() + " entrou na sala");
                            chat.setMessage(chat.getNameUser() + " entrou na sala");

                            // Envia para todos que o fulano tal entrou na sala
                            sendAll(chat);

                            mUsersOnlines.put(chat.getNameUser(), mObjectOutputStream);

                            sendOnlines(); // Atualiza a lista de users online nos clientes
                            break;

                        case Disconnect:
                            chat.setMessage(chat.getNameUser() + " desconectou");

                            disconnect(chat);

                            sendOnlines(); // Atualiza a lista de users online nos clientes
                            break;

                        case SendOne:
                            if (chat.getFile() == null) {
                                chat.setMessage(chat.getNameUser() + " diz: " + chat.getMessage());
                            } else {
                                chat.setAction(Chat.Action.File);
                                chat.setMessage(chat.getNameUser() + " enviou um arquivo");
                            }

                            // Envia o chat apenas para o nameReserved (1 pessoa)
                            sendOne(chat);
                            break;

                        case SendAll:
                            if (chat.getFile() == null) {
                                chat.setMessage(chat.getNameUser() + " diz: " + chat.getMessage());
                                chat.setAction(Chat.Action.SendOne);
                            } else {
                                chat.setAction(Chat.Action.File);
                                chat.setMessage(chat.getNameUser() + " enviou um arquivo");
                            }

                            sendAll(chat);
                            break;
                    }
                }
            } catch (IOException ex) {
                Chat chatError = new Chat();
                chatError.setNameUser(chat.getNameUser());
                chatError.setMessage(chatError.getNameUser() + " desconectou");

                disconnect(chatError);

                sendOnlines(); // Atualiza a lista de onlines
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void disconnect(Chat message) {
        mUsersOnlines.remove(message.getNameUser());

        message.setAction(Chat.Action.SendOne);

        sendAll(message);

        System.out.println("O usuário  " + message.getNameUser() + " saiu da sala...");
    }

    private void sendOne(Chat chat) {
        for (Map.Entry<String, ObjectOutputStream> usersOnlines : mUsersOnlines.entrySet()) {
            if (usersOnlines.getKey().equals(chat.getNameReserved())) {
                try {
                    usersOnlines.getValue().writeObject(chat);
                    break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void sendAll(Chat chat) {
        for (Map.Entry<String, ObjectOutputStream> usersOnlines : mUsersOnlines.entrySet()) {
            if (!usersOnlines.getKey().equals(chat.getNameUser())) {
                try {
                    usersOnlines.getValue().writeObject(chat);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void sendOnlines() {
        // Envia para todos os clientes o nome dos usuários onlines

        Set<String> setNameUsersOnline = new HashSet<>();

        for (Map.Entry<String, ObjectOutputStream> usersOnline : mUsersOnlines.entrySet()) {
            // Pega o nome de todos conectados
            setNameUsersOnline.add(usersOnline.getKey());
        }

        Chat chat = new Chat();
        chat.setAction(Chat.Action.UsersOnline);
        chat.setUsersOnlines(setNameUsersOnline);

        for (Map.Entry<String, ObjectOutputStream> usersOnline : mUsersOnlines.entrySet()) {
            chat.setNameUser(usersOnline.getKey());
            try {
                usersOnline.getValue().writeObject(chat);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}