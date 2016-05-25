package es.esy.chhg.chatapp.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.esy.chhg.chatapp.R;
import es.esy.chhg.chatapp.adapter.ChatAdapter;
import es.esy.chhg.chatapp.data.Chat;
import es.esy.chhg.chatapp.socketclient.SocketClient;
import es.esy.chhg.chatapp.utils.ImageUtil;
import es.esy.chhg.chatapp.utils.NetworkUtil;
import es.esy.chhg.chatapp.utils.PreferencesUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private final int REQUEST_CODE_LOGIN = 1;
    private final int REQUEST_CODE_ATTACH_FILE = 2;

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFabSendMessage;
    private EditText mEditTextMessage;

    private ChatAdapter mChatAdapter;

    private List<Chat> mListChat;

    private SocketClient mSocketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        setupSavedInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_attach:
                doAttachImage();
                return true;

            case R.id.menu_clean_chat:
                mListChat.clear();
                mChatAdapter.notifyDataSetChanged();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initialize() {
        setupToolbar(true, false);
        initializeObjects();
        registerEventClick();
        setupRecyclerView();
        setupAdapter();
        setupEditText();
    }

    private void setupSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            connectSocket();
        } else {
            if (mListChat.size() == 0) {
                mListChat = getListChatApplication();

                mChatAdapter.notifyDataSetChanged();
            }

            if (mSocketClient == null) {
                mSocketClient = getSocketClientApplication();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mSocketClient != null) {
            setSocketClient(mSocketClient);
        }

        if (mListChat != null) {
            updateListChatApplication(mListChat);
        }
    }

    private void initializeObjects() {
        mListChat = new ArrayList<>();
        mFabSendMessage = (FloatingActionButton) findViewById(R.id.fab_send_message);
        mEditTextMessage = (EditText) findViewById(R.id.edit_text_send_message);
    }

    private void registerEventClick() {
        mFabSendMessage.setOnClickListener(this);
    }

    private void setupRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_chat);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
    }

    private void setupAdapter() {
        mChatAdapter = new ChatAdapter(mListChat, getApplicationContext());
        mChatAdapter.setChatAdapterListener(new ChatAdapter.IChatAdapterListener() {
            @Override
            public void onClick(Chat chat) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + chat.getFile().getAbsolutePath()), chat.getMimeType());
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(mChatAdapter);
    }

    private void setupEditText() {
        mEditTextMessage.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mListChat.size() > 0) {
                    mRecyclerView.scrollToPosition(mChatAdapter.getItemCount() - 1);
                }
            }
        });
    }

    private void doAttachImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*|video/*|audio/*");
        try {
            startActivityForResult(intent, REQUEST_CODE_ATTACH_FILE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addTextMessage() {

        String message = mEditTextMessage.getText().toString().trim();

        if (message.length() > 0) {
            Chat chat = new Chat();
            chat.setMessage(message);
            chat.setAction(Chat.Action.SendAll);
            chat.setNameUser(PreferencesUtil.getUserConnected(getApplicationContext()).getUsername());

            addMessage(chat);

            sendMessageSocket(chat);
        }
    }

    private void sendMessageSocket(Chat chat) {
        if (mSocketClient != null) {
            mSocketClient.sendMessage(chat);
        } else {
            alertErrorSocket();
        }
    }

    private void sendFileSocket(Chat chat) {
        if (mSocketClient != null) {
            mSocketClient.sendFile(chat);
        } else {
            alertErrorSocket();
        }
    }

    private void alertErrorSocket() {
        Snackbar.make(mRecyclerView, R.string.error_not_connected_socket, Snackbar.LENGTH_LONG).show();
    }

    private void setupMessage() {
        mEditTextMessage.setText("");
    }

    private void startSocket() {
        mSocketClient = new SocketClient(getApplicationContext()) {
            @Override
            public void getMessage(final Chat chat) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // PAra rodar na thread de UI e poder atualizar a view
                        String messageReplace = chat.getNameUser() + " diz: ";
                        chat.setMessage(chat.getMessage().replace(messageReplace, ""));
                        // Remove a mensagem padrao do server

                        addMessage(chat);
                    }
                });
            }

            @Override
            public void getUsersOnline(Chat chat) {

            }

            @Override
            public void getFile(final Chat chat) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // PAra rodar na thread de UI e poder atualizar a view
                        addMessage(chat);
                    }
                });
            }

            @Override
            public void getUserConnected(final Chat chat) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // PAra rodar na thread de UI e poder atualizar a view
                        chat.setNameUser(getString(R.string.message_server));
                        addMessage(chat);
                    }
                });
            }

            @Override
            public void getUserDisconnected(final Chat chat) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // PAra rodar na thread de UI e poder atualizar a view
                        chat.setNameUser(getString(R.string.message_server));
                        addMessage(chat);
                    }
                });
            }
        };
    }

    private void addMessage(Chat chat) {
        if (PreferencesUtil.isUserConnected(getApplicationContext())) {
            mListChat.add(chat);
            mChatAdapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(mChatAdapter.getItemCount() - 1);

            setupMessage();
        }
    }

    private void startLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_CODE_LOGIN);
    }

    private void connectSocket() {
        if (NetworkUtil.isConnectedInternet(getApplicationContext())) {
            if (PreferencesUtil.isUserConnected(getApplicationContext())) {
                startSocket();
            } else {
                startLogin();
            }
        } else {
            Snackbar.make(mRecyclerView, R.string.text_view_not_connected_network, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_LOGIN:
                    startSocket();
                    break;

                case REQUEST_CODE_ATTACH_FILE:
                    Uri path = data.getData();
                    if (path != null) {

                        Chat chat = new Chat();
                        chat.setAction(Chat.Action.SendAll);
                        chat.setFile(new File(ImageUtil.getPath(getApplicationContext(), path)));
                        chat.setNameUser(PreferencesUtil.getUserConnected(getApplicationContext()).getUsername());

                        addMessage(chat);
                        sendFileSocket(chat);
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mFabSendMessage) {
            addTextMessage();
        }
    }
}