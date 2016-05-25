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

public class ChatFileReceivedViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageViewReceived;
    public TextView textViewTimeImageReceived;
    public FrameLayout frameLayoutImageReceived;
    public View viewDivider;
    public TextView textViewNameUser;
    public TextView textViewTimeVideoOrAudio;

    private LoadSoundInfoAsyncTask mLoadSoundInfoAsyncTask;

    public ChatFileReceivedViewHolder(View itemView) {
        super(itemView);
        imageViewReceived = (ImageView) itemView.findViewById(R.id.image_view_image_received);
        textViewTimeImageReceived = (TextView) itemView.findViewById(R.id.text_view_time_image_received);
        frameLayoutImageReceived = (FrameLayout) itemView.findViewById(R.id.frame_layout_image_received);
        viewDivider = itemView.findViewById(R.id.view_divider);
        textViewNameUser = (TextView) itemView.findViewById(R.id.text_view_name_user);
        textViewTimeVideoOrAudio = (TextView) itemView.findViewById(R.id.text_view_time_video_audio);
    }

    public void loadSoundInfo(Chat chat) {
        if (mLoadSoundInfoAsyncTask != null) {
            mLoadSoundInfoAsyncTask.cancel(false);
        }
        mLoadSoundInfoAsyncTask = new LoadSoundInfoAsyncTask(textViewTimeVideoOrAudio, chat);
        mLoadSoundInfoAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}