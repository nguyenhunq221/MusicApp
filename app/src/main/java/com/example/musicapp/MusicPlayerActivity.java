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

public class MusicPlayerActivity extends ListSongActivity {

      private ActivityMainBinding binding;
      Animation animation;
      MusicController mMusicController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mMusicController = new MusicController(this, new MusicController.MusicSource() {
            @Override
            public Song getAtIndex(int index) {
                return songArrayList.get(index);
            }

            @Override
            public int getSize() {
                return songArrayList.size();
            }

            @Override
            public void onDonePrepare() {
                binding.skTime.setMax(mMusicController.getDuration());
                String duration = milisecondsToString(mMusicController.getDuration());
                binding.txtDuration.setText(duration);
            }
        });

//      getSupportActionBar().hide(); //hide the actionbar(ten app)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int songIndex = getIntent().getIntExtra("song_index", 0);
        Song song = songArrayList.get(songIndex);
        animation= AnimationUtils.loadAnimation(this, R.anim.rotate);

        binding.txtSong.setText((song.getTitle()));
        binding.txtSinger.setText(song.getArtist());

        mMusicController.playSongAt(songIndex);
        binding.imgcd.startAnimation(animation);
        binding.btnplay.setBackgroundResource(R.drawable.ic_baseline_pause_24);

        mMusicController.setLooping(true);
        mMusicController.seekTo(0);



        binding.btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMusicController.isPlaying()){
                    mMusicController.pause();
                    binding.btnplay.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                    binding.imgcd.clearAnimation();

                }
                else{
                    mMusicController.start();
                    binding.btnplay.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                    binding.imgcd.startAnimation(animation);


                }
            }
        });

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMusicController.playNext();
                Song s = songArrayList.get(mMusicController.getCurrentIndex());
                binding.txtSong.setText((s.getTitle()));
                binding.txtSinger.setText(s.getArtist());
                binding.imgcd.clearAnimation();
                binding.imgcd.startAnimation(animation);
                mMusicController.setLooping(true);
            }

        });

        binding.btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMusicController.playPrev();
                Song s = songArrayList.get(mMusicController.getCurrentIndex());
                binding.txtSong.setText((s.getTitle()));
                binding.txtSinger.setText(s.getArtist());
                binding.imgcd.clearAnimation();
                binding.imgcd.startAnimation(animation);
                mMusicController.setLooping(true);
            }
        });

        binding.skTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                    mMusicController.seekTo(binding.skTime.getProgress());
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mMusicController != null ){
                    if ((mMusicController.isPlaying())){
                        try{
                                final double current      =  mMusicController.getCurrentPosition();
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
            if(mMusicController.isPlaying()){
                mMusicController.pause();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}