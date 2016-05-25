package es.esy.chhg.chatapp.asynctasks;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import es.esy.chhg.chatapp.data.Chat;
import es.esy.chhg.chatapp.utils.SoundUtil;

public class LoadSoundInfoAsyncTask extends AsyncTask<Void, Void, Integer> {
    private static HashMap<String, Integer> mLoadedSoundInfoCache;
    private WeakReference<TextView> mTextViewReference;
    private WeakReference<Chat> mChatReference;

    public LoadSoundInfoAsyncTask(TextView textView, Chat chat) {
        mTextViewReference = new WeakReference<>(textView);
        mChatReference = new WeakReference<>(chat);
    }

    public static String getFormatedCachedSoundInfo(File file) {
        return mLoadedSoundInfoCache != null ? SoundUtil.getFormated(mLoadedSoundInfoCache.get(file.getAbsolutePath())) : null;
    }

    private TextView getTextView() {
        return mTextViewReference != null ? mTextViewReference.get() : null;
    }

    private Chat getChat() {
        return mChatReference != null ? mChatReference.get() : null;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        Chat chat = getChat();
        int duration = 0;
        MediaPlayer mp = new MediaPlayer();
        FileInputStream fs = null;
        FileDescriptor fd;
        if (chat != null) {
            try {
                fs = new FileInputStream(chat.getFile());
                fd = fs.getFD();
                mp.setDataSource(fd);
                mp.prepare();
                duration = mp.getDuration();
                mp.release();

                if (mLoadedSoundInfoCache == null) {
                    mLoadedSoundInfoCache = new HashMap<>();
                }
                mLoadedSoundInfoCache.put(chat.getFile().getAbsolutePath(), duration);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fs != null) {
                    try {
                        fs.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return duration;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        TextView textView = getTextView();
        Chat chat = getChat();
        String formatted = null;

        if (integer != null && !isCancelled()) {
            formatted = SoundUtil.getFormated(integer);
            if (textView != null) {
                textView.setText(formatted);
            }
            if (chat != null) {
                chat.setSoundTime(integer);
            }
        }
    }

    @Override
    protected void onPreExecute() {
        TextView textView = getTextView();
        if (textView != null) {
            textView.setText("00:00");
        }
    }
}