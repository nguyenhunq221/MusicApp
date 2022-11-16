package com.example.musicapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SongAdapter extends ArrayAdapter <Song>{


//    private int resource;
    public SongAdapter(@NonNull Context context, @NonNull List<Song> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song,null);

        TextView txtTitle = convertView.findViewById(R.id.txtTitle);
        TextView txtArtist = convertView.findViewById(R.id.txtArtist);
        Song song = getItem(position);
        txtArtist.setText(song.getArtist());
        txtTitle.setText(song.getTitle());

        return convertView;
    }
}
