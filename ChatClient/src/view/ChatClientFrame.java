package view;

import socketclient.SocketClient;
import es.esy.chhg.chatapp.data.Chat;
import es.esy.chhg.chatapp.data.Chat.Action;
import es.esy.chhg.chatapp.data.User;
import java.io.File;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

public class ChatClientFrame extends javax.swing.JFrame {

    private socketclient.SocketClient mClientService;
    private User mUser;

    public ChatClientFrame() {
        initComponents();
        initialize();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        panelActionBar = new javax.swing.JPanel();
        buttonAttach = new javax.swing.JButton();
        panelAreaMessage = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textAreaDisplayMessages = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        textAreaInputMessage = new javax.swing.JTextArea();
        buttonSendMessage = new javax.swing.JButton();
        panelUsersOnlines = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        listUsersOnlines = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelActionBar.setBackground(new java.awt.Color(206, 206, 206));

        buttonAttach.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ic_attach.png"))); // NOI18N
        buttonAttach.setBorderPainted(false);
        buttonAttach.setContentAreaFilled(false);
        buttonAttach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAttachActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelActionBarLayout = new javax.swing.GroupLayout(panelActionBar);
        panelActionBar.setLayout(panelActionBarLayout);
        panelActionBarLayout.setHorizontalGroup(
            panelActionBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelActionBarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonAttach, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelActionBarLayout.setVerticalGroup(
            panelActionBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelActionBarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonAttach, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelAreaMessage.setBackground(new java.awt.Color(206, 206, 206));

        textAreaDisplayMessages.setColumns(20);
        textAreaDisplayMessages.setRows(5);
        jScrollPane1.setViewportView(textAreaDisplayMessages);

        textAreaInputMessage.setColumns(20);
        textAreaInputMessage.setRows(5);
        jScrollPane2.setViewportView(textAreaInputMessage);

        buttonSendMessage.setBackground(new java.awt.Color(153, 153, 153));
        buttonSendMessage.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        buttonSendMessage.setText("Enviar");
        buttonSendMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSendMessageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAreaMessageLayout = new javax.swing.GroupLayout(panelAreaMessage);
        panelAreaMessage.setLayout(panelAreaMessageLayout);
        panelAreaMessageLayout.setHorizontalGroup(
            panelAreaMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAreaMessageLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAreaMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(panelAreaMessageLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buttonSendMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelAreaMessageLayout.setVerticalGroup(
            panelAreaMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAreaMessageLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelAreaMessageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAreaMessageLayout.createSequentialGroup()
                        .addComponent(buttonSendMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAreaMessageLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        panelUsersOnlines.setBackground(new java.awt.Color(206, 206, 206));

        jScrollPane3.setViewportView(listUsersOnlines);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setText("Usuários onlines");

        javax.swing.GroupLayout panelUsersOnlinesLayout = new javax.swing.GroupLayout(panelUsersOnlines);
        panelUsersOnlines.setLayout(panelUsersOnlinesLayout);
        panelUsersOnlinesLayout.setHorizontalGroup(
            panelUsersOnlinesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUsersOnlinesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(panelUsersOnlinesLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jLabel1)
                .addContainerGap(53, Short.MAX_VALUE))
        );
        panelUsersOnlinesLayout.setVerticalGroup(
            panelUsersOnlinesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelUsersOnlinesLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelAreaMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelActionBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addComponent(panelUsersOnlines, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelActionBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelAreaMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(panelUsersOnlines, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonAttachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAttachActionPerformed
        JFileChooser fileChooser = new JFileChooser();

        int click = fileChooser.showOpenDialog(this);

        if (click == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            Chat chat = new Chat();
            chat.setFile(file);

            if (listUsersOnlines.getSelectedIndex() > -1) {
                chat.setNameReserved((String) listUsersOnlines.getSelectedValue());
                chat.setAction(Action.SendOne);
                listUsersOnlines.clearSelection();
                // Envia o arquivo apenas para uma pessoa
            } else {
                chat.setAction(Action.SendAll);
                // Envia o arquivo para todos
            }

            mClientService.sendFile(chat);

            textAreaDisplayMessages.append("Você enviou o arquivo: " + file.getName() + "\n");
        }
    }//GEN-LAST:event_buttonAttachActionPerformed

    private void buttonSendMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSendMessageActionPerformed

        sendMessage();
    }//GEN-LAST:event_buttonSendMessageActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAttach;
    private javax.swing.JButton buttonSendMessage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList listUsersOnlines;
    private javax.swing.JPanel panelActionBar;
    private javax.swing.JPanel panelAreaMessage;
    private javax.swing.JPanel panelUsersOnlines;
    private javax.swing.JTextArea textAreaDisplayMessages;
    private javax.swing.JTextArea textAreaInputMessage;
    // End of variables declaration//GEN-END:variables

    private void initialize() {
        this.setTitle("Chat");
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        setupUser();

        setupChat();
    }

    private void setupUser() {
        // TODO - não esquecer de criar o login para pegar o user ou pegar só o nome mesmo
        mUser = new User();
        mUser.setUsername("A_" + System.currentTimeMillis());
    }

    private void setupChat() {
        mClientService = new SocketClient(mUser) {

            @Override
            public void getMessage(Chat chat) {
                receive(chat);
            }

            @Override
            public void getUsersOnline(Chat chat) {
                refreshOnlines(chat);
            }

            @Override
            public void getFile(Chat chat) {
                receive(chat);
            }
        };
    }

    private void sendMessage() {
        String message = textAreaInputMessage.getText().trim();

        Chat chat = new Chat();

        if (listUsersOnlines.getSelectedIndex() > -1) {
            chat.setNameReserved((String) listUsersOnlines.getSelectedValue());
            chat.setAction(Action.SendOne);
            listUsersOnlines.clearSelection();
            // Envia a mensagem apenas para uma pessoa
        } else {
            chat.setAction(Action.SendAll);
            // Envia a mensagem para todos
        }

        if (!message.isEmpty()) {
            chat.setNameUser(mUser.getUsername());
            chat.setMessage(message);

            textAreaDisplayMessages.append("Você disse: " + message + "\n");

            mClientService.sendMessage(chat);
        }

        textAreaInputMessage.setText("");
    }

    private void receive(Chat chat) {
        textAreaDisplayMessages.append(chat.getMessage() + "\n");
    }

    private void refreshOnlines(Chat chat) {
        System.out.println(chat.getUsersOnlines().toString());

        Set<String> names = chat.getUsersOnlines();

        names.remove(chat.getNameUser()); // Deleta o cara que está no chat

        String[] array = (String[]) names.toArray(new String[names.size()]);

        listUsersOnlines.setListData(array);
        listUsersOnlines.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listUsersOnlines.setLayoutOrientation(JList.VERTICAL);
    }
}