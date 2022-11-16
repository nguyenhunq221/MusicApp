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

import com.example.musicapp.databinding.ActivityMainBinding;

import java.io.IOException;

public class MusicPlayerActivity extends AppCompatActivity {
      private ActivityMainBinding binding;
      MediaPlayer musicPlayer;
      Animation animation;
      int position =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//      getSupportActionBar().hide(); //hide the actionbar(ten app)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Song song =(Song) getIntent().getSerializableExtra("song");

        animation= AnimationUtils.loadAnimation(this, R.anim.rotate);

        binding.txtSong.setText((song.getTitle()));
        binding.txtSinger.setText(song.getArtist());

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
        binding.txtDuration.setText(duration);

        binding.btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(musicPlayer.isPlaying()){
                    musicPlayer.pause();
                    binding.btnplay.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                    binding.imgcd.clearAnimation();

                }
                else{
                    musicPlayer.start();
                    binding.btnplay.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                    binding.imgcd.startAnimation(animation);


                }
            }
        });

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }

        });

        binding.skTime.setMax(musicPlayer.getDuration());
        binding.skTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                    musicPlayer.seekTo(binding.skTime.getProgress());
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (musicPlayer != null ){
                    if ((musicPlayer.isPlaying())){
                        try{
                                final double current      =  musicPlayer.getCurrentPosition();
                                final  String elapsedTime =  milisecondsToString((int) current);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.txtTime.setText(elapsedTime);
                                        binding.skTime.setProgress((int) current);
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



}