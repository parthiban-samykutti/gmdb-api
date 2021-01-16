package com.cts.galvenize.gmdb.controller;

import com.cts.galvenize.gmdb.entity.Movie;
import com.cts.galvenize.gmdb.exception.MovieNotFoundException;
import com.cts.galvenize.gmdb.service.MovieService;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/gmdb/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping
    public List findAllMovies(){
        return movieService.findAllMovies();
    }

    @GetMapping("/title/{title}")
    public Movie findMovieByTitle(final @PathVariable("title") String movieTitle) throws MovieNotFoundException {
        Movie movie = movieService.findMovieByTitle(movieTitle);
        if(movie == null){
            throw new MovieNotFoundException("Movie doesn't exist");
        }
        return movie;
    }

    @PutMapping("/title/{title}/rating/{rating}")
    public Movie updateRatingByTitle(final @PathVariable("title") String movieTitle, final @PathVariable("rating") String movieRating){
        return movieService.updateRatingByTitle( movieTitle, movieRating);
    }
}
