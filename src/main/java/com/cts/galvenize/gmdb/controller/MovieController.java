package com.cts.galvenize.gmdb.controller;

import com.cts.galvenize.gmdb.entity.Movie;
import com.cts.galvenize.gmdb.exception.InvalidValueException;
import com.cts.galvenize.gmdb.exception.MovieNotFoundException;
import com.cts.galvenize.gmdb.model.MovieUpdateRequest;
import com.cts.galvenize.gmdb.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handler methods to handle the Movie API requests.
 */
@RestController
@RequestMapping("/api/gmdb/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;

    /**
     * Find all the available movies.
     *
     * @return movie list
     */
    @GetMapping
    public List findAllMovies() {
        return movieService.findAllMovies();
    }

    /**
     * Find the {@link Movie} by title.
     *
     * @param movieTitle
     * @return matched {@link Movie}
     * @throws MovieNotFoundException
     */
    @GetMapping("/title/{title}")
    public Movie findMovieByTitle(final @PathVariable("title") String movieTitle) throws MovieNotFoundException {
        Movie movie = movieService.findMovieByTitle(movieTitle);
        if (movie == null) {
            throw new MovieNotFoundException("Movie doesn't exist");
        }
        return movie;
    }

    /**
     * Updates the Movie rating.
     *
     * @param movieTitle
     * @param movieRating
     * @return updated {@link Movie}
     */
    @PutMapping("/title/{title}/rating/{rating}")
    public Movie updateRatingByTitle(final @PathVariable("title") String movieTitle, final @PathVariable("rating") String movieRating) {
        return movieService.updateRatingByTitle(movieTitle, movieRating);
    }
    /**
     * Updates the Movie rating.
     *
     * @param movieTitle
     * @param movieUpdateRequest
     * @return updated {@link Movie}
     */
    @PutMapping("/title/{title}")
    public Movie updateRatingAndReviewByTitle(final @PathVariable("title") String movieTitle, @RequestBody MovieUpdateRequest movieUpdateRequest) throws InvalidValueException {
        if(movieUpdateRequest.getRating() == null){
            throw new InvalidValueException("A star rating is required");
        }
        return movieService.updateRatingAndReviewByTitle(movieTitle, movieUpdateRequest);
    }
}
