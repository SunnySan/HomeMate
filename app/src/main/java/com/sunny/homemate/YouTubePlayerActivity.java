package com.sunny.homemate;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
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

    private boolean isYouTubeInitialized = false;
    private YouTubePlayer myPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");
        //取的intent中的bundle物件
        Bundle bundle =this.getIntent().getExtras();

        String screenSize = bundle.getString("screenSize");
        if (screenSize!=null && screenSize.equals("small")){
            setContentView(R.layout.activity_you_tube_player_small);
        }else{
            setContentView(R.layout.activity_you_tube_player_full);
        }

        /** Initializing YouTube player view **/
        //YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
        //youTubePlayerView.initialize(API_KEY, this);

        //隱藏 status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getActionBar();
        if (actionBar!=null) actionBar.hide();
    }

    @Override
    protected void onPause(){
        System.out.println("onPause");
        super.onPause();
        this.finish();  //直接結束
    }

    @Override
    protected void onResume(){
        System.out.println("onResume");
        super.onResume();
        if (!isYouTubeInitialized){
            System.out.println("onResume initialize YouTube");
            /** Initializing YouTube player view **/
            YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
            youTubePlayerView.initialize(API_KEY, this);
        }else{
            if (myPlayer!=null){
                System.out.println("onResume try to play YouTube");
                playVideo(myPlayer);
            }else{
                System.out.println("onResume player is null");
            }
        }
    }

    @Override
    protected void onDestroy(){
        System.out.println("onDestroy");
        /*
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
        youTubePlayerView.removeAllViews();
        if (myPlayer!=null) myPlayer.release();
        */
        if (myPlayer!=null) myPlayer.release();
        super.onDestroy();
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
        Toast.makeText(this, "Failured to Initialize!", Toast.LENGTH_LONG).show();
        isYouTubeInitialized = false;
        System.out.println(result.toString());
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        isYouTubeInitialized = true;
        /** add listeners to YouTubePlayer instance **/
        player.setPlayerStateChangeListener(playerStateChangeListener);
        player.setPlaybackEventListener(playbackEventListener);

        /** Start buffering **/
        if (!wasRestored) {
            myPlayer = player;
            playVideo(player);
        }
    }

    private void playVideo(YouTubePlayer player){
        //if (player!=null && player.isPlaying()) player.release();
        //取的intent中的bundle物件
        Bundle bundle =this.getIntent().getExtras();

        String videoId = bundle.getString("videoId");
        String screenSize = bundle.getString("screenSize");
        if (screenSize==null || !screenSize.equals("small")){
            //player.setFullscreen(true);   //全螢幕播放
        }
        if (videoId!=null && videoId.length()>1) {  //有傳入 YouTube 的影片ID，直接播放
            //player.cueVideo(VIDEO_ID);    //cueVideo不會自動開始播放，不要用
            //if (player.isPlaying()) player.pause();
            System.out.println("Play youtube: " + videoId);
            player.loadVideo(videoId);
            YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
            youTubePlayerView.setVisibility(View.VISIBLE);
        }else{
            this.finish();  //若未傳入 video id 則關閉 Activity，回上一個畫面
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
