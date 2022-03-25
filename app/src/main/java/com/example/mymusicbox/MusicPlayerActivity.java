package com.example.mymusicbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MusicPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    TextView songName, sangerName, RightTime, LeftTime;
    SeekBar playSeekBar;//, volumeSeekBar;
    ImageButton playButton;
    MediaPlayer mediaPlayer = new MediaPlayer();
    Animation animation;
    CircleImageView circleImageView;
    Boolean startBefore =true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

             SetUpUI();

       // VolumeSeekBarFun();
         playSeekBarFun();
         SetRightTime();
        }





    void SetUpUI() {
        songName = findViewById(R.id.the_song);
        sangerName = findViewById(R.id.sanger_name);

        RightTime = findViewById(R.id.rightTime);
        LeftTime = findViewById(R.id.leftTime);

        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(this);

        playSeekBar = findViewById(R.id.seekBarPlay);
        // volumeSeekBar = findViewById(R.id.seekBarVolume);
           mediaPlayer = MediaPlayer.create(this, R.raw.mylifeisgoingon);

        circleImageView=findViewById(R.id.mainImage);
        animation= AnimationUtils.loadAnimation(this,R.anim.rotaterotate);
    }

  /*  void VolumeSeekBarFun() {
        volumeSeekBar.setMax(100);
        volumeSeekBar.setProgress(50);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    float vol=seekBar.getProgress()/100f;

                    mediaPlayer.setVolume(vol,vol);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
*/
    void playSeekBarFun(){

        playSeekBar.setMax(mediaPlayer.getDuration());
        playSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }

                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("mm:ss");
                int currant=mediaPlayer.getCurrentPosition();
                LeftTime.setText(simpleDateFormat.format(new Date(currant)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });





        final Handler mHandler = new Handler();
        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    int mCurrentPosition = mediaPlayer.getCurrentPosition();
                    playSeekBar.setProgress(mCurrentPosition);
                    if (playSeekBar.getProgress()==mediaPlayer.getDuration()){
                        playSeekBar.setProgress(0);
                        LeftTime.setText( "00:00");
                        playButton.setImageResource(R.drawable.ic_play);
                    }
                }
                mHandler.postDelayed(this, 1000);
            }
        });
    }


    public void SetRightTime(){
        int time=mediaPlayer.getDuration();
        String ElapsedTime="";

        int minutes=time/1000/60;
        int seconds=time/1000%60;

        ElapsedTime +=minutes+":";

        if (seconds<10)
            ElapsedTime +="0";
        else
            ElapsedTime+=seconds;

        RightTime.setText(ElapsedTime);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playButton:
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    playButton.setImageResource(R.drawable.ic_play);
                    circleImageView.clearAnimation();
                }
                else {
                    mediaPlayer.start();
                    playButton.setImageResource(R.drawable.ic_baseline_pause_24);
                    circleImageView.startAnimation(animation);
                    //  playButton.setBackgroundResource();
                }
                break;

            case R.id.nextButton:
                if(startBefore){
                    startBefore=false;
                    mediaPlayer.stop();
                    mediaPlayer=MediaPlayer.create(this, R.raw.bella_ciao);
                    songName.setText("Bella Ciao");
                    sangerName.setText("la Casa de Papel_ Money Heist");
                    circleImageView.clearAnimation();
                    circleImageView.setImageResource(R.drawable.bella_ciao);
                    circleImageView.startAnimation(animation);
                    playButton.setImageResource(R.drawable.ic_baseline_pause_24);
                    SetRightTime();
                    mediaPlayer.start();
                }
                else
                    Toast.makeText(this, "No More Go previous", Toast.LENGTH_SHORT).show();
                break;


            case R.id.previousButton:
                if (startBefore==false){
                    startBefore=true;
                    mediaPlayer.stop();
                    mediaPlayer=MediaPlayer.create(this, R.raw.mylifeisgoingon);
                    songName.setText("My Life Is Going On");
                    sangerName.setText("Cecilia");

                    circleImageView.clearAnimation();
                    circleImageView.setImageResource(R.drawable.idontcareatall);
                    circleImageView.startAnimation(animation);
                    playButton.setImageResource(R.drawable.ic_baseline_pause_24);
                    SetRightTime();
                    mediaPlayer.start();
                }
                else
                    Toast.makeText(this, "No More Go Next", Toast.LENGTH_SHORT).show();

                break;
        }
    }


}