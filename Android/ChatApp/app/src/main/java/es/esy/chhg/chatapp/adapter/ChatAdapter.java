package es.esy.chhg.chatapp.adapter;

import android.content.Context;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.esy.chhg.chatapp.R;
import es.esy.chhg.chatapp.adapter.viewholder.ChatFileReceivedViewHolder;
import es.esy.chhg.chatapp.adapter.viewholder.ChatFileSendViewHolder;
import es.esy.chhg.chatapp.adapter.viewholder.ChatReceivedViewHolder;
import es.esy.chhg.chatapp.adapter.viewholder.ChatSendViewHolder;
import es.esy.chhg.chatapp.data.Chat;
import es.esy.chhg.chatapp.picasso.PicassoUtil;
import es.esy.chhg.chatapp.picasso.VideoRequestHandler;
import es.esy.chhg.chatapp.utils.DateUtil;
import es.esy.chhg.chatapp.utils.ImageUtil;
import es.esy.chhg.chatapp.utils.PreferencesUtil;
import es.esy.chhg.chatapp.utils.SoundUtil;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface IChatAdapterListener {
        void onClick(Chat chat);
    }

    private final int VIEW_TYPE_MESSAGE_SEND = 0;
    private final int VIEW_TYPE_MESSAGE_RECEIVED = 1;
    private final int VIEW_TYPE_FILE_SEND = 2;
    private final int VIEW_TYPE_FILE_RECEIVED = 3;

    private List<Chat> mListChat;
    private Context mContext;

    private Picasso mVideoLoader; // Para exibir uma foto do vídeo

    private IChatAdapterListener mChatAdapterListener;

    public ChatAdapter(List<Chat> listChat, Context context) {
        mListChat = listChat;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {

        if (mListChat.get(position).getFile() != null) {
            // arquivo
            if (mListChat.get(position).getNameUser().equals(PreferencesUtil.getUserConnected(mContext).getUsername())) {
                return VIEW_TYPE_FILE_SEND;
            } else {
                return VIEW_TYPE_FILE_RECEIVED;
            }
        } else if (mListChat.get(position).getNameUser().equals(PreferencesUtil.getUserConnected(mContext).getUsername())) {
            return VIEW_TYPE_MESSAGE_SEND;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public int getItemCount() {
        return mListChat != null ? mListChat.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view;
        switch (viewType) {
            case VIEW_TYPE_MESSAGE_SEND:

                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_send_chat, parent, false);

                viewHolder = new ChatSendViewHolder(view);
                break;

            case VIEW_TYPE_MESSAGE_RECEIVED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_received_chat, parent, false);

                viewHolder = new ChatReceivedViewHolder(view);
                break;

            case VIEW_TYPE_FILE_SEND:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_file_send_chat, parent, false);
                ChatFileSendViewHolder chatFileSendViewHolder = new ChatFileSendViewHolder(view);

                chatFileSendViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mChatAdapterListener != null) {
                            Chat chat = (Chat) v.getTag();
                            mChatAdapterListener.onClick(chat);
                        }
                    }
                });

                viewHolder = chatFileSendViewHolder;
                break;

            case VIEW_TYPE_FILE_RECEIVED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_file_received_chat, parent, false);

                ChatFileReceivedViewHolder chatFileReceivedViewHolder = new ChatFileReceivedViewHolder(view);

                chatFileReceivedViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mChatAdapterListener != null) {
                            Chat chat = (Chat) v.getTag();
                            mChatAdapterListener.onClick(chat);
                        }
                    }
                });

                viewHolder = new ChatFileReceivedViewHolder(view);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Chat chat;
        switch (getItemViewType(position)) {
            case VIEW_TYPE_MESSAGE_SEND:
                chat = mListChat.get(position);
                ChatSendViewHolder chatSendViewHolder = (ChatSendViewHolder) holder;

                chatSendViewHolder.textViewTextMessageSend.setText(
                        Html.fromHtml(chat.getMessage() + " &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;")
                );


                chatSendViewHolder.textViewTimeMessageSend.setText(DateUtil.getTime(chat.getTimeMessage(), chatSendViewHolder.itemView.getContext()));

                setupSpacingBetweenViewsSend(chatSendViewHolder, position);

                chatSendViewHolder.itemView.setTag(chat);

                break;

            case VIEW_TYPE_MESSAGE_RECEIVED:

                chat = mListChat.get(position);
                ChatReceivedViewHolder chatReceivedViewHolder = (ChatReceivedViewHolder) holder;

                chatReceivedViewHolder.textViewMessageReceived.setText(
                        Html.fromHtml(chat.getMessage() + " &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;")
                );

                if (chat.getNameUser() == null) {
                    chat.setNameUser(mContext.getString(R.string.message_server));
                }

                chatReceivedViewHolder.textViewNameUSer.setText(chat.getNameUser());
                chatReceivedViewHolder.textViewTimeMessageReceived.setText(DateUtil.getTime(chat.getTimeMessage(), chatReceivedViewHolder.itemView.getContext()));

                setupSpacingBetweenViewsReceived(chatReceivedViewHolder, position);

                chatReceivedViewHolder.itemView.setTag(chat);

                break;

            case VIEW_TYPE_FILE_SEND:
                chat = mListChat.get(position);
                ChatFileSendViewHolder chatFileSendViewHolder = (ChatFileSendViewHolder) holder;

                int width = 0;
                int height = 0;

                if (chat.isImage()) {
                    chatFileSendViewHolder.textViewTimeVideoOrAudio.setVisibility(View.GONE);

                    if (ImageUtil.getPhotoOrientation(Uri.parse(chat.getFile().getAbsolutePath())) == ExifInterface.ORIENTATION_NORMAL) { // Retrato
                        width = (int) chatFileSendViewHolder.itemView.getResources().getDimension(R.dimen.width_image_view);
                        height = (int) chatFileSendViewHolder.itemView.getResources().getDimension(R.dimen.height_image_view_retrato);
                    } else {
                        // Paisagem
                        width = (int) chatFileSendViewHolder.itemView.getResources().getDimension(R.dimen.width_image_view);
                        height = (int) chatFileSendViewHolder.itemView.getResources().getDimension(R.dimen.height_image_view_paisagem);
                    }

                    picassoCropTopWhitRadius(
                            chatFileSendViewHolder.itemView.getContext(),
                            chatFileSendViewHolder.imageViewSend,
                            chat.getFile().getAbsolutePath(),
                            width,
                            height
                    );

                } else if (chat.isVideo()) {
                    if (mVideoLoader == null) {
                        mVideoLoader = new Picasso.Builder(((ChatFileSendViewHolder) holder).imageViewSend.getContext())
                                .addRequestHandler(new VideoRequestHandler()).build();
                    }

                    chatFileSendViewHolder.textViewTimeVideoOrAudio.setText("--:--");
                    chatFileSendViewHolder.textViewTimeVideoOrAudio.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_video_size, 0, 0, 0);
                    chatFileSendViewHolder.textViewTimeVideoOrAudio.setVisibility(View.VISIBLE);

                    if (chat.getSoundTime() == 0) {
                        chatFileSendViewHolder.loadSoundInfo(chat);
                    } else {
                        chatFileSendViewHolder.textViewTimeVideoOrAudio.setText(SoundUtil.getFormated(chat.getSoundTime()));
                    }

                    width = (int) chatFileSendViewHolder.itemView.getResources().getDimension(R.dimen.width_image_view);
                    height = (int) chatFileSendViewHolder.itemView.getResources().getDimension(R.dimen.height_image_view_retrato);

                    String fileVideoProtocol = "video:" + chat.getFile().getAbsolutePath().replace("file://", ""); // Protocolo

                    picassoCropTopWhitRadius(
                            mVideoLoader,
                            chatFileSendViewHolder.imageViewSend,
                            fileVideoProtocol,
                            width,
                            height
                    );
                } else if (chat.isAudio()) {

                    width = (int) chatFileSendViewHolder.itemView.getResources().getDimension(R.dimen.width_image_view);
                    height = (int) chatFileSendViewHolder.itemView.getResources().getDimension(R.dimen.height_image_view_paisagem);

                    PicassoUtil.picassoCropTopAndRadiusResId(
                            chatFileSendViewHolder.itemView.getContext(),
                            chatFileSendViewHolder.imageViewSend,
                            R.drawable.ic_audio,
                            width,
                            height,
                            R.drawable.ic_audio
                    );

                } else {
                    chatFileSendViewHolder.textViewTimeVideoOrAudio.setVisibility(View.GONE);

                    width = (int) chatFileSendViewHolder.itemView.getResources().getDimension(R.dimen.width_image_view);
                    height = (int) chatFileSendViewHolder.itemView.getResources().getDimension(R.dimen.height_image_view_retrato);

                    PicassoUtil.picassoCropTopAndRadiusResId(
                            chatFileSendViewHolder.itemView.getContext(),
                            chatFileSendViewHolder.imageViewSend,
                            R.drawable.ic_file_default,
                            width,
                            height,
                            R.drawable.ic_file_default
                    );
                }

                chatFileSendViewHolder.textViewTimeImageSend.setText(
                        DateUtil.getTime(chat.getTimeMessage(), chatFileSendViewHolder.itemView.getContext())
                );

                setupSpacingBetweenViewsFileSend(chatFileSendViewHolder, position);
                chatFileSendViewHolder.itemView.setTag(chat);
                break;

            case VIEW_TYPE_FILE_RECEIVED:
                chat = mListChat.get(position);

                ChatFileReceivedViewHolder chatFileReceivedViewHolder = (ChatFileReceivedViewHolder) holder;

                if (chat.isImage()) {
                    chatFileReceivedViewHolder.textViewTimeVideoOrAudio.setVisibility(View.GONE);

                    width = (int) chatFileReceivedViewHolder.itemView.getResources().getDimension(R.dimen.width_image_view);
                    height = (int) chatFileReceivedViewHolder.itemView.getResources().getDimension(R.dimen.height_image_view_retrato);

                    picassoCropTopWhitRadius(
                            chatFileReceivedViewHolder.itemView.getContext(),
                            chatFileReceivedViewHolder.imageViewReceived,
                            chat.getFile().getAbsolutePath(),
                            width,
                            height
                    );

                } else if (chat.isVideo()) {
                    if (mVideoLoader == null) {
                        mVideoLoader = new Picasso.Builder(((ChatFileReceivedViewHolder) holder).imageViewReceived.getContext())
                                .addRequestHandler(new VideoRequestHandler()).build();
                    }

                    chatFileReceivedViewHolder.textViewTimeVideoOrAudio.setText("--:--");
                    chatFileReceivedViewHolder.textViewTimeVideoOrAudio.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_video_size, 0, 0, 0);
                    chatFileReceivedViewHolder.textViewTimeVideoOrAudio.setVisibility(View.VISIBLE);

                    if (chat.getSoundTime() == 0) {
                        chatFileReceivedViewHolder.loadSoundInfo(chat);
                    } else {
                        chatFileReceivedViewHolder.textViewTimeVideoOrAudio.setText(SoundUtil.getFormated(chat.getSoundTime()));
                    }

                    width = (int) chatFileReceivedViewHolder.itemView.getResources().getDimension(R.dimen.width_image_view);
                    height = (int) chatFileReceivedViewHolder.itemView.getResources().getDimension(R.dimen.height_image_view_retrato);

                    String fileVideoProtocol = "video:" + chat.getFile().getAbsolutePath().replace("file://", ""); // Protocolo

                    picassoCropTopWhitRadius(
                            mVideoLoader,
                            chatFileReceivedViewHolder.imageViewReceived,
                            fileVideoProtocol,
                            width,
                            height
                    );
                } else if (chat.isAudio()) {

                    width = (int) chatFileReceivedViewHolder.itemView.getResources().getDimension(R.dimen.width_image_view);
                    height = (int) chatFileReceivedViewHolder.itemView.getResources().getDimension(R.dimen.height_image_view_paisagem);

                    PicassoUtil.picassoCropTopAndRadiusResId(
                            chatFileReceivedViewHolder.itemView.getContext(),
                            chatFileReceivedViewHolder.imageViewReceived,
                            R.drawable.ic_audio,
                            width,
                            height,
                            R.drawable.ic_audio
                    );

                } else {
                    chatFileReceivedViewHolder.textViewTimeVideoOrAudio.setVisibility(View.GONE);

                    width = (int) chatFileReceivedViewHolder.itemView.getResources().getDimension(R.dimen.width_image_view);
                    height = (int) chatFileReceivedViewHolder.itemView.getResources().getDimension(R.dimen.height_image_view_retrato);

                    PicassoUtil.picassoCropTopAndRadiusResId(
                            chatFileReceivedViewHolder.itemView.getContext(),
                            chatFileReceivedViewHolder.imageViewReceived,
                            R.drawable.ic_file_default,
                            width,
                            height,
                            R.drawable.ic_file_default
                    );
                }

                chatFileReceivedViewHolder.textViewTimeImageReceived.setText(
                        DateUtil.getTime(chat.getTimeMessage(), chatFileReceivedViewHolder.itemView.getContext())
                );

                if (chat.getNameUser() == null) {
                    chat.setNameUser(mContext.getString(R.string.message_server));
                }

                chatFileReceivedViewHolder.textViewNameUser.setText(chat.getNameUser());

                setupSpacingBetweenViewsFileReceived(chatFileReceivedViewHolder, position);

                chatFileReceivedViewHolder.itemView.setTag(chat);
                break;
        }
    }

    public void setChatAdapterListener(IChatAdapterListener chatAdapterListener) {
        mChatAdapterListener = chatAdapterListener;
    }

    private void setupSpacingBetweenViewsSend(ChatSendViewHolder chatSendViewHolder, int position) {

        if (position == 0) {
            chatSendViewHolder.viewDivider.setVisibility(View.GONE);
            chatSendViewHolder.frameLayout.setBackgroundResource(R.drawable.balloon_send_ponta);
        } else {
            if (mListChat.get(--position).getNameUser().equals(PreferencesUtil.getUserConnected(chatSendViewHolder.itemView.getContext()).getUsername())) {
                chatSendViewHolder.viewDivider.setVisibility(View.GONE);
                chatSendViewHolder.frameLayout.setBackgroundResource(R.drawable.balloon_send_normal);
            } else {
                chatSendViewHolder.viewDivider.setVisibility(View.VISIBLE);
                chatSendViewHolder.frameLayout.setBackgroundResource(R.drawable.balloon_send_ponta);
            }
        }
    }

    private void setupSpacingBetweenViewsReceived(ChatReceivedViewHolder chatReceivedViewHolder, int position) {
        if (position == 0) {
            chatReceivedViewHolder.viewDivider.setVisibility(View.GONE);
            chatReceivedViewHolder.frameLayout.setBackgroundResource(R.drawable.balloon_received_ponta);
            chatReceivedViewHolder.textViewNameUSer.setVisibility(View.VISIBLE);
        } else {

            Chat chatCurrent = mListChat.get(position);
            Chat chatBack = mListChat.get(--position);

            if (chatCurrent.getNameUser().equals(chatBack.getNameUser())) {
                chatReceivedViewHolder.frameLayout.setBackgroundResource(R.drawable.balloon_received_normal);
                chatReceivedViewHolder.viewDivider.setVisibility(View.GONE);
                chatReceivedViewHolder.textViewNameUSer.setVisibility(View.GONE);
            } else {
                chatReceivedViewHolder.viewDivider.setVisibility(View.VISIBLE);
                chatReceivedViewHolder.frameLayout.setBackgroundResource(R.drawable.balloon_received_ponta);
                chatReceivedViewHolder.textViewNameUSer.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setupSpacingBetweenViewsFileSend(ChatFileSendViewHolder chatFileSendViewHolder, int position) {

        if (position == 0) {
            chatFileSendViewHolder.viewDivider.setVisibility(View.GONE);
            chatFileSendViewHolder.frameLayoutImageSend.setBackgroundResource(R.drawable.balloon_send_ponta);
        } else {
            if (mListChat.get(--position).getNameUser().equals(PreferencesUtil.getUserConnected(chatFileSendViewHolder.itemView.getContext()).getUsername())) {
                chatFileSendViewHolder.viewDivider.setVisibility(View.GONE);
                chatFileSendViewHolder.frameLayoutImageSend.setBackgroundResource(R.drawable.balloon_send_normal);
            } else {
                chatFileSendViewHolder.viewDivider.setVisibility(View.VISIBLE);
                chatFileSendViewHolder.frameLayoutImageSend.setBackgroundResource(R.drawable.balloon_send_ponta);
            }
        }
    }

    private void setupSpacingBetweenViewsFileReceived(ChatFileReceivedViewHolder chatFileReceivedViewHolder, int position) {
        if (position == 0) {
            chatFileReceivedViewHolder.viewDivider.setVisibility(View.GONE);
            chatFileReceivedViewHolder.frameLayoutImageReceived.setBackgroundResource(R.drawable.balloon_received_ponta);
            chatFileReceivedViewHolder.textViewNameUser.setVisibility(View.VISIBLE);
        } else {

            Chat chatCurrent = mListChat.get(position);
            Chat chatBack = mListChat.get(--position);

            if (chatCurrent.getNameUser().equals(chatBack.getNameUser())) {
                chatFileReceivedViewHolder.frameLayoutImageReceived.setBackgroundResource(R.drawable.balloon_received_normal);
                chatFileReceivedViewHolder.viewDivider.setVisibility(View.GONE);
                chatFileReceivedViewHolder.textViewNameUser.setVisibility(View.GONE);
            } else {
                chatFileReceivedViewHolder.viewDivider.setVisibility(View.VISIBLE);
                chatFileReceivedViewHolder.frameLayoutImageReceived.setBackgroundResource(R.drawable.balloon_received_ponta);
                chatFileReceivedViewHolder.textViewNameUser.setVisibility(View.VISIBLE);
            }
        }
    }

    private void picassoCropTopWhitRadius(Context context, ImageView imageView, String path, int width, int height) {
        PicassoUtil.picassoCropTopAndRadius(
                context,
                imageView,
                "file://" + path,
                width,
                height,
                R.drawable.ic_send); // TODO - passar o ícone correto
    }

    private void picassoCropTopWhitRadius(Picasso picasso, ImageView imageView, String path, int width, int height) {
        PicassoUtil.picassoCropTopAndRadius(
                picasso,
                imageView.getContext(),
                imageView,
                path,
                width,
                height,
                R.drawable.ic_send
        );// TODO - passar o ícone corret
    }

    public void updateListAndNotify(List<Chat> listChat) {
        mListChat = listChat;
        notifyDataSetChanged();
    }
}