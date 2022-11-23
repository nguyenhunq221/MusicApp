package com.example.musicapp;

import android.content.ContentUris;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class MusicController {
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private int mCurrentIndex;
    private boolean mIsPreparing;
    private MusicSource mMusicSource;


    public void setMusicSource(MusicSource source){
        mMusicSource = source;
    }

    public void setLooping(boolean b) {
        mMediaPlayer.setLooping(b);
    }

    public void seekTo(int i) {
        mMediaPlayer.seekTo(i);
    }

    public int getCurrentPosition(){
        return mMediaPlayer.getCurrentPosition();
    }

    public interface MusicSource {
        Song getAtIndex(int index);
        int getSize();
        void onDonePrepare();
    }

    public MusicController(Context context, MusicSource musicSource) {
        mContext = context;
        mMusicSource = musicSource;

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                mIsPreparing = false;
                musicSource.onDonePrepare();
            }
        });

        mCurrentIndex = -1;
        mIsPreparing = false;
    }

    public int getDuration(){
       return mMediaPlayer.getDuration();
    }


    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    public boolean isPlaying(){
        return mMediaPlayer.isPlaying();
    }

    public void playNext() {
        if(mMusicSource.getSize() != 0) {
            if (mCurrentIndex < mMusicSource.getSize() - 1) {
                mCurrentIndex++;
            } else {
                mCurrentIndex = 0;
            }
            playSongAt(mCurrentIndex);
        }
    }

    public void playPrev() {
        if(mMusicSource.getSize() != 0) {
            if (mCurrentIndex > 0) {
                mCurrentIndex--;
            } else {
                mCurrentIndex = mMusicSource.getSize() - 1;
            }
            playSongAt(mCurrentIndex);
        }
    }

    public void pause() {
        mMediaPlayer.pause();
    }

    public void start() {
        mMediaPlayer.start();
    }

    public void playSongAt(int index) {
        mMediaPlayer.reset();
        Uri trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mMusicSource.getAtIndex(index).getId());
        try {
            mMediaPlayer.setDataSource(mContext, trackUri);
            mCurrentIndex = index;
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error starting data source", e);
        }
        mMediaPlayer.prepareAsync();
        mIsPreparing = true;
    }
}
