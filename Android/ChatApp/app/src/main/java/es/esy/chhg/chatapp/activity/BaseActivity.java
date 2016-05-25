package es.esy.chhg.chatapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.List;

import es.esy.chhg.chatapp.R;
import es.esy.chhg.chatapp.application.ChatApplication;
import es.esy.chhg.chatapp.data.Chat;
import es.esy.chhg.chatapp.socketclient.SocketClient;

public class BaseActivity extends AppCompatActivity {
    private boolean mIsResumed;

    @Override
    protected void onPause() {
        super.onPause();

        mIsResumed = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        mIsResumed = true;
    }

    protected boolean isBaseActivityResumed() {
        return mIsResumed;
    }

    protected boolean hasWriteExternalStoragePermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    protected void requestWriteExternalStoragePermission(int requestCode) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
    }

    protected boolean shouldShowRequestPermissionRationaleWriteExternalStoragePermission() {
        return ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    protected void openPermissionSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(intent);
    }

    protected void setupToolbar(boolean navigationDrawer, boolean navigationUp) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(navigationUp);
            actionBar.setHomeButtonEnabled(navigationDrawer);
        }
    }

    protected void removeWorkerFragment(String tag) {
        Fragment task = getSupportFragmentManager().findFragmentByTag(tag);
        if (task != null) {
            getSupportFragmentManager().beginTransaction().remove(task).commitAllowingStateLoss();
        }
    }

    private ChatApplication getChatApplication() {
        ChatApplication chatApplication = (ChatApplication) getApplication();
        return chatApplication;
    }

    protected void updateListChatApplication(List<Chat> listChat) {
        getChatApplication().updateListChat(listChat);
    }

    protected List<Chat> getListChatApplication() {
        return getChatApplication().getListChat();
    }

    protected void setSocketClient(SocketClient socketClient) {
        getChatApplication().setSocketClient(socketClient);
    }

    protected SocketClient getSocketClientApplication() {
        return getChatApplication().getSocketClient();
    }
}