package soonflyy.learning.hub.live;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import soonflyy.learning.hub.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import live.videosdk.rtc.android.Meeting;
import live.videosdk.rtc.android.Participant;
import live.videosdk.rtc.android.VideoView;
import live.videosdk.rtc.android.listeners.MeetingEventListener;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.PeerViewHolder> {

    private int containerHeight;

    // creating a empty list which will store all participants
    private final List<Participant> participants = new ArrayList<>();
    public ParticipantAdapter(Meeting meeting) {
        // adding the local participant(You) to the list
       // participants.add(meeting.getLocalParticipant());

        // adding Meeting Event listener to get the participant join/leave event in the meeting.
        meeting.addEventListener(new MeetingEventListener() {
            @Override
            public void onParticipantJoined(Participant participant) {
                // add participant to the list
                participants.add(participant);
                notifyItemInserted(participants.size() - 1);


            }

            @Override
            public void onParticipantLeft(Participant participant) {
                int pos = -1;
                for (int i = 0; i < participants.size(); i++) {
                    if (participants.get(i).getId().equals(participant.getId())) {
                        pos = i;
                        break;
                    }
                }
                // remove participant from the list
                participants.remove(participant);

                if (pos >= 0) {
                    notifyItemRemoved(pos);
                }

            }
        });
    }
    @NonNull
    @Override
    public PeerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        containerHeight = parent.getHeight();
        return new PeerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_student_list, parent, false));//item_remote_peer
    }

    @Override
    public void onBindViewHolder(@NonNull PeerViewHolder holder, int position) {
        Participant participant = participants.get(position);
//
//        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
//        layoutParams.height = containerHeight / 3;
//        holder.itemView.setLayoutParams(layoutParams);

        holder.tvName.setText(participant.getDisplayName());

        // adding the initial video stream for the participant into the 'VideoView'
//        for (Map.Entry<String, Stream> entry : participant.getStreams().entrySet()) {
//            Stream stream = entry.getValue();
//            if (stream.getKind().equalsIgnoreCase("video")) {
//                holder.participantView.setVisibility(View.VISIBLE);
//                VideoTrack videoTrack = (VideoTrack) stream.getTrack();
//                holder.participantView.addTrack(videoTrack);
//                break;
//            }
//        }
//        // add Listener to the participant which will update start or stop the video stream of that participant
//        participant.addEventListener(new ParticipantEventListener() {
//            @Override
//            public void onStreamEnabled(Stream stream) {
//                if (stream.getKind().equalsIgnoreCase("video")) {
//                    holder.participantView.setVisibility(View.VISIBLE);
//                    VideoTrack videoTrack = (VideoTrack) stream.getTrack();
//                    holder.participantView.addTrack(videoTrack);
//                }
//            }
//
//            @Override
//            public void onStreamDisabled(Stream stream) {
//                if (stream.getKind().equalsIgnoreCase("video")) {
//                    holder.participantView.removeTrack();
//                    holder.participantView.setVisibility(View.GONE);
//                }
//            }
//        });

        //--change color
        switch (position%2){
            case 0:
                holder.linParent.setBackgroundColor(ContextCompat
                        .getColor(holder.linParent.getContext(), R.color.white_smoke));
                break;
            case 1:
                holder.linParent.setBackgroundColor(ContextCompat
                        .getColor(holder.linParent.getContext(),R.color.white));
                break;
        }

        holder.ivMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                participant.disableMic();
            }
        });
        //profile with diffrent color
        holder.tvProfileText.setText(participant.getDisplayName().substring(0,1).toUpperCase(Locale.ROOT));
        Context context=holder.tvProfileText.getContext();
        if (position+1<=4){
            switch (position+1){
                case 1:
                    holder.tvProfileText.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.red));
                    break;
                case 2:
                    holder.tvProfileText.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.graient2));
                    break;
                case 3:
                    holder.tvProfileText.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.green));
                    break;
                case 4:
                    holder.tvProfileText.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.yellow));
                    break;
            }
        }else{
            switch ((position+1)%4){
                case 0:
                    holder.tvProfileText.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.red));
                    break;
                case 1:
                    holder.tvProfileText.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.graient2));
                    break;
                case 2:
                    holder.tvProfileText.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.green));
                    break;
                case 3:
                    holder.tvProfileText.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.yellow));
                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return participants.size();
    }
    public int getParticipaintsCount(){
        return participants.size();
    }

    static class PeerViewHolder extends RecyclerView.ViewHolder {
        // 'VideoView' to show Video Stream
        public VideoView participantView;
        LinearLayout linParent;
        public TextView tvName,tvProfileText;
        public View itemView;
        ImageView ivMic;

        PeerViewHolder(@NonNull View view) {
            super(view);
            itemView = view;
            tvName = view.findViewById(R.id.tv_name);
            tvProfileText = view.findViewById(R.id.tv_profile_text);
            linParent = view.findViewById(R.id.lin_parent);
            ivMic=view.findViewById(R.id.iv_mic);

        }
    }

}