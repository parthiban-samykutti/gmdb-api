package com.cts.galvenize.gmdb.service;

import com.cts.galvenize.gmdb.entity.Movie;
import com.cts.galvenize.gmdb.entity.Rating;
import com.cts.galvenize.gmdb.model.MovieUpdateRequest;
import com.cts.galvenize.gmdb.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> findAllMovies() {

        return movieRepository.findAll();
    }

    public Movie findMovieByTitle(String movieTitle) {
        return movieRepository.findById(movieTitle).orElseGet(()->null);
    }

    public Movie updateRatingByTitle(String movieTitle, String movieRating) {
        Movie movie = getMovie(movieTitle, movieRating);
        Movie savedMovie = movieRepository.save(movie);

        return savedMovie;
    }

    private Movie getMovie(String movieTitle, String movieRating) {
        Movie movie = findMovieByTitle(movieTitle);
        Rating rating = movie.getRatingPattern();
        switch (movieRating){
            case "1":
                rating.setOneStar(rating.getOneStar()+1);
                break;
            case "2":
                rating.setTwoStar(rating.getTwoStar()+1);
                break;
            case "3":
                rating.setThreeStar(rating.getThreeStar()+1);
                break;
            case "4":
                rating.setFourStar(rating.getFourStar()+1);
                break;
            case "5":
                rating.setFiveStar(rating.getFiveStar()+1);
                break;
        }
        movie.setRating(String.valueOf(calculateRating(rating)));
        return movie;
    }

    private int calculateRating(Rating rating) {
        int oneStar = rating.getOneStar();
        int twoStar = rating.getTwoStar();
        int threeStar = rating.getThreeStar();
        int fourStar = rating.getFourStar();
        int fiveStar = rating.getFiveStar();
        int starCount = 1*oneStar + 2*twoStar + 3*threeStar + 4*fourStar + 5*fiveStar;
        int divCount = 0;
        if(oneStar > 0)
            divCount ++;
        if(twoStar > 0)
            divCount ++;
        if(threeStar > 0)
            divCount ++;
        if(fourStar > 0)
            divCount ++;
        if(fiveStar > 0)
            divCount ++;

        return Math.round(starCount/divCount);
    }

    public Movie updateRatingAndReviewByTitle(String movieTitle, MovieUpdateRequest movieUpdateRequest) {
        Movie movie = getMovie(movieTitle, movieUpdateRequest.getRating());
        movie.setReview(movieUpdateRequest.getReview());
        return movieRepository.save(movie);
    }
}
