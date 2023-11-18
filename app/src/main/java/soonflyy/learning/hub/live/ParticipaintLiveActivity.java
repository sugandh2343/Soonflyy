package soonflyy.learning.hub.live;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import soonflyy.learning.hub.R;

public class ParticipaintLiveActivity extends AppCompatActivity {

  String meetingId="",token="",liveId="",liveTopic="",description="",duration="";
  TextView tvDescription,tvDuration,tvTopicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participaint_live);
    }
}