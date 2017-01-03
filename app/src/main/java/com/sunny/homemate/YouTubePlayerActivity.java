package com.sunny.homemate;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayerView;

import static com.sunny.homemate.Config.GC_YOUTUBE_API_KEY;

public class YouTubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    public static final String API_KEY = GC_YOUTUBE_API_KEY;

    //http://youtu.be/<VIDEO_ID>
    public static final String VIDEO_ID = "jcRBtTtP9f8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //取的intent中的bundle物件
        Bundle bundle =this.getIntent().getExtras();

        String screenSize = bundle.getString("screenSize");
        if (screenSize!=null && screenSize.equals("small")){
            setContentView(R.layout.activity_you_tube_player_small);
        }else{
            setContentView(R.layout.activity_you_tube_player_full);
        }

        /** Initializing YouTube player view **/
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
        youTubePlayerView.initialize(API_KEY, this);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
        Toast.makeText(this, "Failured to Initialize!", Toast.LENGTH_LONG).show();
        System.out.println(result.toString());
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        /** add listeners to YouTubePlayer instance **/
        player.setPlayerStateChangeListener(playerStateChangeListener);
        player.setPlaybackEventListener(playbackEventListener);

        /** Start buffering **/
        if (!wasRestored) {
            //取的intent中的bundle物件
            Bundle bundle =this.getIntent().getExtras();

            String videoId = bundle.getString("videoId");
            String screenSize = bundle.getString("screenSize");
            if (screenSize==null || !screenSize.equals("small")){
                player.setFullscreen(true);   //全螢幕播放
            }
            if (videoId!=null && videoId.length()>1) {  //有傳入 YouTube 的影片ID，直接播放
                //player.cueVideo(VIDEO_ID);    //cueVideo不會自動開始播放，不要用
                player.loadVideo(videoId);
            }
        }
    }

    private PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {

        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
        }

        @Override
        public void onPlaying() {
        }

        @Override
        public void onSeekTo(int arg0) {
        }

        @Override
        public void onStopped() {
        }

    };

    private PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {

        @Override
        public void onAdStarted() {
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {
        }

        @Override
        public void onLoaded(String arg0) {
        }

        @Override
        public void onLoading() {
        }

        @Override
        public void onVideoEnded() {
            YouTubePlayerActivity.this.finish();    //結束 Activity，回到前一個 Activity
        }

        @Override
        public void onVideoStarted() {
        }
    };
}
