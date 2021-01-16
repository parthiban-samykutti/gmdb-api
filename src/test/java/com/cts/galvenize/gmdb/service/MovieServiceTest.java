package com.cts.galvenize.gmdb.service;

import com.cts.galvenize.gmdb.controller.MovieControllerTest;
import com.cts.galvenize.gmdb.entity.Movie;
import com.cts.galvenize.gmdb.entity.Rating;
import com.cts.galvenize.gmdb.model.MovieUpdateRequest;
import com.cts.galvenize.gmdb.repository.MovieRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {MovieService.class})
public class MovieServiceTest {

    @Autowired
    private MovieService movieService;

    @MockBean
    private MovieRepository movieRepository;

    @Test
    @DisplayName("findAllMovies - Find all movies")
    public void testFindAllMovies() throws IOException {
        List<Movie> movieList= buildMultipleMovieList();
        when(movieRepository.findAll()).thenReturn(movieList);
        List<Movie> actualMoviesList= movieService.findAllMovies();
        assertThat(actualMoviesList).isSameAs(movieList);
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findMovieByTitle - Find an existing movie by title")
    public void testFindMovieByTitle() throws IOException {
        Movie movie = buildSingleMovieList().get(0);
        when(movieRepository.findById(any())).thenReturn(Optional.of(movie));
        Movie actualMovie= movieService.findMovieByTitle("The Avengers");
        assertThat(actualMovie).isSameAs(movie);
        verify(movieRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("findMovieByTitle - Find movie by title with non existing title")
    public void testFindMovieByTitleNonExistingMovie() throws IOException {
        when(movieRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        Movie actualMovie= movieService.findMovieByTitle("The Avengers");
        assertThat(actualMovie).isNull();
        verify(movieRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("UpdateRatingByTitle - Update the rating of the existing movie")
    public void testUpdateRatingByTitle() throws IOException {
        Movie movie = buildSingleMovieList().get(0);
        Rating rating = new Rating();
        movie.setRatingPattern(rating);
        when(movieRepository.findById(any())).thenReturn(Optional.of(movie));
        when(movieRepository.save(any())).thenReturn(movie);
        Movie actualMovie= movieService.updateRatingByTitle("The Avengers", "5");
        assertThat(actualMovie.getRating()).isEqualTo("5");
        verify(movieRepository, times(1)).findById(any());
        verify(movieRepository, times(1)).save(any());
    }

    /**
     * Test case to check the movie rating for a movie with one 3 star and giving a 5 star review
     * Expected rating is 4 star review
     * @throws IOException
     */
    @Test
    @DisplayName("UpdateRatingByTitle - Update the rating of the existing movie with existing rating")
    public void testUpdateRatingByTitleWIthExistingRating() throws IOException {
        Movie movie = buildSingleMovieList().get(0);
        Rating rating = new Rating();
        rating.setThreeStar(1);
        movie.setRatingPattern(rating);
        when(movieRepository.findById(any())).thenReturn(Optional.of(movie));
        when(movieRepository.save(any())).thenReturn(movie);
        Movie actualMovie= movieService.updateRatingByTitle("The Avengers", "5");
        assertThat(actualMovie.getRating()).isEqualTo("4");
        verify(movieRepository, times(1)).findById(any());
        verify(movieRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("updateRatingAndReviewByTitle - Update the rating and text review of the existing movie")
    public void testupdateRatingAndReviewByTitle() throws IOException {
        Movie movie = buildSingleMovieList().get(0);
        Rating rating = new Rating();
        movie.setRatingPattern(rating);
        MovieUpdateRequest updateRequest = MovieUpdateRequest.builder()
                .review("Good movie to watch")
                .rating("5")
                .build();
        when(movieRepository.findById(any())).thenReturn(Optional.of(movie));
        when(movieRepository.save(any())).thenReturn(movie);
        Movie actualMovie= movieService.updateRatingAndReviewByTitle("The Avengers", updateRequest);
        assertThat(actualMovie.getRating()).isEqualTo("5");
        assertThat(actualMovie.getReview()).isEqualTo(updateRequest.getReview());
        verify(movieRepository, times(1)).findById(any());
        verify(movieRepository, times(1)).save(any());
    }

    /**
     * Builds a Movie list with multiple movie from a json file.
     *
     * @return List<Movie>
     * @throws IOException
     */
    private List<Movie> buildMultipleMovieList() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Movie> movieList = objectMapper.readValue(MovieControllerTest.class.getClassLoader().getResourceAsStream("movies.json"), new TypeReference<ArrayList<Movie>>() {
        });
        return movieList;
    }

    /**
     * Builds a Movie list with single movie from a json file.
     *
     * @return List<Movie>
     * @throws IOException
     */
    private List<Movie> buildSingleMovieList() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Movie movie = objectMapper.readValue(MovieControllerTest.class.getClassLoader().getResourceAsStream("single-movie.json"), Movie.class);
        List<Movie> movieList = new ArrayList<>();
        movieList.add(movie);
        return movieList;
    }
}
