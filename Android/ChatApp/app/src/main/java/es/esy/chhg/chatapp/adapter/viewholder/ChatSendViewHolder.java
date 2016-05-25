package es.esy.chhg.chatapp.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import es.esy.chhg.chatapp.R;

public class ChatSendViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewTextMessageSend;
    public TextView textViewTimeMessageSend;
    public View viewDivider;
    public FrameLayout frameLayout;
    public ImageView imageView;

    public ChatSendViewHolder(View itemView) {
        super(itemView);

        textViewTextMessageSend = (TextView) itemView.findViewById(R.id.text_view_message_send);
        textViewTimeMessageSend = (TextView) itemView.findViewById(R.id.text_view_time_message_send);
        viewDivider = itemView.findViewById(R.id.view_divider);
        frameLayout = (FrameLayout) itemView.findViewById(R.id.frame_layout_send);
    }
}