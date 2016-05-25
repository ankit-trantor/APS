package es.esy.chhg.chatapp.adapter.viewholder;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import es.esy.chhg.chatapp.R;
import es.esy.chhg.chatapp.asynctasks.LoadSoundInfoAsyncTask;
import es.esy.chhg.chatapp.data.Chat;

public class ChatFileSendViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageViewSend;
    public TextView textViewTimeImageSend;
    public FrameLayout frameLayoutImageSend;
    public View viewDivider;
    public TextView textViewTimeVideoOrAudio;

    private LoadSoundInfoAsyncTask mLoadSoundInfoAsyncTask;

    public ChatFileSendViewHolder(View itemView) {
        super(itemView);
        imageViewSend = (ImageView) itemView.findViewById(R.id.image_view_image_send);
        textViewTimeImageSend = (TextView) itemView.findViewById(R.id.text_view_time_image_send);
        textViewTimeVideoOrAudio = (TextView) itemView.findViewById(R.id.text_view_time_video_audio);
        frameLayoutImageSend = (FrameLayout) itemView.findViewById(R.id.frame_layout_image_send);
        viewDivider = itemView.findViewById(R.id.view_divider);
    }

    public void loadSoundInfo(Chat chat) {
        if (mLoadSoundInfoAsyncTask != null) {
            mLoadSoundInfoAsyncTask.cancel(false);
        }
        mLoadSoundInfoAsyncTask = new LoadSoundInfoAsyncTask(textViewTimeVideoOrAudio, chat);
        mLoadSoundInfoAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}