package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class MusicPlayerActivity extends AppCompatActivity {
    ImageView imgcd;
    Button btnPlay;
    TextView txtTime,txtDuration,txtSong,txtSinger;
    SeekBar skTime;
    MediaPlayer musicPlayer;
    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getSupportActionBar().hide(); //hide the actionbar(ten app)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Song song =(Song) getIntent().getSerializableExtra("song");

        Anhxa();

        animation= AnimationUtils.loadAnimation(this, R.anim.rotate);

        txtSong.setText((song.getTitle()));
        txtSinger.setText(song.getArtist());

        musicPlayer = new MediaPlayer();
        try {
            musicPlayer.setDataSource(song.getPath());
            musicPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        musicPlayer.setLooping(true); //lap lai
        musicPlayer.seekTo(0); // nhay den gia tri bat dau lap lai

        String duration = milisecondsToString(musicPlayer.getDuration());
        txtDuration.setText(duration);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(musicPlayer.isPlaying()){
                    musicPlayer.pause();
                    btnPlay.setBackgroundResource(R.drawable.play);
                    imgcd.clearAnimation();

                }
                else{
                    musicPlayer.start();
                    btnPlay.setBackgroundResource(R.drawable.pause);
                    imgcd.startAnimation(animation);


                }
            }
        });

        skTime.setMax(musicPlayer.getDuration());
        skTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                    musicPlayer.seekTo(skTime.getProgress());
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (musicPlayer != null ){
                    if ((musicPlayer.isPlaying())){
                        try{
                                final double current = musicPlayer.getCurrentPosition();
                                final  String elapsedTime=milisecondsToString((int) current);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        txtTime.setText(elapsedTime);
                                        skTime.setProgress((int) current);
                                    }
                                });
                                Thread.sleep(1000);

                        }catch (InterruptedException e){

                        }
                    }
                }
            }
        }).start();
    }

    public String milisecondsToString(int time){
        String elapsedTime = "";
        int minutes = time/1000/60;
        int seconds = time /1000 %60;
        elapsedTime = minutes + ":";
        if(seconds < 10){
            elapsedTime += "0";
        }
        elapsedTime += seconds;
        return  elapsedTime;
    }

    public  void Anhxa(){
        btnPlay =       (Button) findViewById(R.id.btnplay);
        txtDuration =   (TextView) findViewById(R.id.txt_duration);
        txtTime =       (TextView) findViewById(R.id.txt_time);
        skTime =        (SeekBar) findViewById(R.id.sk_time);
        txtSong=        (TextView) findViewById(R.id.txt_song);
        txtSinger=        (TextView) findViewById(R.id.txt_singer);
        imgcd = (ImageView) findViewById(R.id.imgcd);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home);{
            finish();
            if(musicPlayer.isPlaying()){
                musicPlayer.stop();
            }
        }
        return super.onOptionsItemSelected(item);
    }

//    public void startAnimation(View view, int animId){
//        Animation animation= AnimationUtils.loadAnimation(this, animId);
//        view.startAnimation(animation);
//    }


}