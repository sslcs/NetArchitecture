package com.sslcs.uniform.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.sslcs.uniform.R;
import com.sslcs.uniform.databinding.ItemMovieBinding;
import com.sslcs.uniform.net.bean.Movie;

import java.util.ArrayList;

public class MovieAdapter extends ArrayAdapter<Movie> {
    private ArrayList<Movie> movies;
    private LayoutInflater inflater;

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        super(context, R.layout.item_movie);
        this.movies = movies;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return movies == null ? 0 : movies.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemMovieBinding binding;
        if (convertView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.item_movie, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (ItemMovieBinding) convertView.getTag();
        }

        Movie movie = movies.get(position);
        binding.tvTitle.setText(movie.title);
        binding.tvRate.setText("评分：" + movie.rating.average);
        return convertView;
    }
}
