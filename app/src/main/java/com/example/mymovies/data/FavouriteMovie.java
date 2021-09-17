package com.example.mymovies.data;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "favourite_movies")
public class FavouriteMovie extends Movie {

    public FavouriteMovie(int id, int vote_count, String title, String original_title, String overview, String posterPath, String bigPosterPath, String backdropPath, double vote_average, String releaseDate) {
        super(id, vote_count, title, original_title, overview, posterPath, bigPosterPath, backdropPath, vote_average, releaseDate);
    }

    @Ignore
    public FavouriteMovie(Movie movie){
        super(movie.getId(),movie.getVote_count(),movie.getTitle(),movie.getOriginal_title(),movie.getOverview(),movie.getPosterPath(),movie.getBigPosterPath(),movie.getBackdropPath(),movie.getVote_average(),movie.getReleaseDate());
    }

}
