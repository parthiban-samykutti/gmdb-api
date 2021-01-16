package com.cts.galvenize.gmdb.service;

import com.cts.galvenize.gmdb.entity.Movie;
import com.cts.galvenize.gmdb.model.MovieUpdateRequest;
import com.cts.galvenize.gmdb.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> findAllMovies() {

        return movieRepository.findAll();
    }

    public Movie findMovieByTitle(String movieTitle) {

        return movieRepository.findById(movieTitle).get();
    }

    public Movie updateRatingByTitle(String movieTitle, String movieRating) {
        return null;
    }

    public Movie updateRatingAndReviewByTitle(String movieTitle, MovieUpdateRequest movieUpdateRequest) {
        return null;
    }
}
