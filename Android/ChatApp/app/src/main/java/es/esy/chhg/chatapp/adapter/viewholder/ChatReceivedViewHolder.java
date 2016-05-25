package es.esy.chhg.chatapp.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import es.esy.chhg.chatapp.R;

public class ChatReceivedViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewMessageReceived;
    public TextView textViewTimeMessageReceived;
    public TextView textViewNameUSer;
    public View viewDivider;
    public FrameLayout frameLayout;

    public ChatReceivedViewHolder(View itemView) {
        super(itemView);

        textViewMessageReceived = (TextView) itemView.findViewById(R.id.text_view_text_message_received);
        textViewTimeMessageReceived = (TextView) itemView.findViewById(R.id.text_view_time_message_received);
        textViewNameUSer = (TextView) itemView.findViewById(R.id.text_view_name_user);
        viewDivider = itemView.findViewById(R.id.view_divider);
        frameLayout = (FrameLayout) itemView.findViewById(R.id.frame_layout_received);
    }
}