package com.example.mymovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.mymovies.data.MainViewModel;
import com.example.mymovies.data.Movie;
import com.example.mymovies.utils.JSONUtils;
import com.example.mymovies.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPosters;
    private MovieAdapter adapter;
    private Switch switchSort;
    private TextView textViewMoteRated;
    private TextView textViewMostPopular;

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switchSort = findViewById(R.id.switchSort);
        viewModel = new ViewModelProvider(this,null).get(MainViewModel.class);
        textViewMostPopular = findViewById(R.id.textViewPopularity);
        textViewMoteRated = findViewById(R.id.textViewTopRated);
        recyclerViewPosters = findViewById(R.id.recyclerViewPosters);
        recyclerViewPosters.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new MovieAdapter();
        recyclerViewPosters.setAdapter(adapter);
        switchSort.setChecked(true);
        switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setMethodOfSort(isChecked);
            }
        });
        switchSort.setChecked(false);
        adapter.setOnPosterClickListener(new MovieAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                Movie movie = adapter.getMovies().get(position);
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("id",movie.getId());
                startActivity(intent);
            }
        });
        adapter.setOnReachEndListener(new MovieAdapter.OnReachEndListener() {
            @Override
            public void onReachEnd() {

            }
        });
        LiveData<List<Movie>> moviesFromLiveData = viewModel.getMovies();
        moviesFromLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                adapter.setMovies(movies);
            }
        });
    }

    private void downloadData(int methodOfSort,int page){
        JSONObject jsonObject = NetworkUtils.getJsonFromNetwork(methodOfSort,1);
        ArrayList<Movie> movies = JSONUtils.getMoviesFromJSON(jsonObject);
        if(movies != null && !movies.isEmpty()){
            viewModel.deleteAllMovies();
            for(Movie movie : movies){
                viewModel.insertMovie(movie);
            }
        }
    }

    private void setMethodOfSort(boolean isTopRated){
        int methodOfSort;
        if (isTopRated){
            textViewMoteRated.setTextColor(getResources().getColor(R.color.design_default_color_error));
            textViewMostPopular.setTextColor(getResources().getColor(R.color.black));
            methodOfSort = NetworkUtils.TOP_RATED;
        }else{
            textViewMoteRated.setTextColor(getResources().getColor(R.color.black));
            textViewMostPopular.setTextColor(getResources().getColor(R.color.design_default_color_error));
            methodOfSort = NetworkUtils.POPULARITY;
        }
        downloadData(methodOfSort,1);
    }

    public void onClickMostPopular(View view) {
        setMethodOfSort(false);
        switchSort.setChecked(false);
    }

    public void onClickMoreRated(View view) {
        setMethodOfSort(true);
        switchSort.setChecked(true);
    }
}