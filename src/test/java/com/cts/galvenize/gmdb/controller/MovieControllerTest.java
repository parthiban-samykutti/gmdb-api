package com.cts.galvenize.gmdb.controller;

import com.cts.galvenize.gmdb.entity.Movie;
import com.cts.galvenize.gmdb.model.MovieUpdateRequest;
import com.cts.galvenize.gmdb.service.MovieService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class MovieControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    /**
     * As a user, I should see a list of movies when I visit GMDB.
     * <p>
     * When I visit GMDB
     * Then I can see a list of all movies.
     *
     * @throws Exception
     */
    @Test
    @DisplayName("findAllMovies - Null value")
    public void testFindAllMoviesWithNullValue() throws Exception {
        when(movieService.findAllMovies()).thenReturn(null);
        MvcResult mvcResult = mockMvc.perform(get("/api/gmdb/movies"))
                .andExpect(status().isOk()).andReturn();
        assertThat(mvcResult.getResponse().getContentAsString()).isEmpty();
        verify(movieService, times(1)).findAllMovies();
    }

    /**
     * As a user, I should see a list of movies when I visit GMDB.
     * <p>
     * When I visit GMDB
     * Then I can see a list of all movies.
     *
     * @throws Exception
     */
    @Test
    @DisplayName("findAllMovies - empty list")
    public void testFindAllMoviesWithEmptyList() throws Exception {
        mockMvc.perform(get("/api/gmdb/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(0));
    }

    /**
     * As a user, I should see a list of movies when I visit GMDB.
     * <p>
     * When I visit GMDB
     * Then I can see a list of all movies.
     *
     * @throws Exception
     */
    @Test
    @DisplayName("findAllMovies - One value")
    public void testFindAllMoviesWithOneValue() throws Exception {
        when(movieService.findAllMovies()).thenReturn(buildSingleMovieList());
        mockMvc.perform(get("/api/gmdb/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].title").value("The Avengers"))
                .andExpect(jsonPath("$[0].director").value("Joss Whedon"))
                .andExpect(jsonPath("$[0].actors").value("Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth"))
                .andExpect(jsonPath("$[0].release").value("2012"))
                .andExpect(jsonPath("$[0].description").value("Earth's mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army from enslaving humanity."))
                .andExpect(jsonPath("$[0].rating").doesNotExist());
        verify(movieService, times(1)).findAllMovies();
    }

    /**
     * As a user, I should see a list of movies when I visit GMDB.
     * <p>
     * When I visit GMDB
     * Then I can see a list of all movies.
     *
     * @throws Exception
     */
    @Test
    @DisplayName("findAllMovies - Multiple value")
    public void testFindAllMoviesWithMultipleValue() throws Exception {
        when(movieService.findAllMovies()).thenReturn(buildMultipleMovieList());
        mockMvc.perform(get("/api/gmdb/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(7))
                .andExpect(jsonPath("$[0].title").value("The Avengers"))
                .andExpect(jsonPath("$[0].director").value("Joss Whedon"))
                .andExpect(jsonPath("$[0].actors").value("Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth"))
                .andExpect(jsonPath("$[0].release").value("2012"))
                .andExpect(jsonPath("$[0].description").value("Earth's mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army from enslaving humanity."))
                .andExpect(jsonPath("$[0].rating").doesNotExist());
        verify(movieService, times(1)).findAllMovies();
    }

    /**
     * As a user, I can browse each movie so I can learn all the details.
     * <p>
     * Rule: Movie details include title, director, actors, release year, description and star rating.
     * <p>
     * Given an existing movie
     * When I visit that title
     * Then I can see all the movie details.
     *
     * @throws Exception
     */
    @Test
    @DisplayName("findMovieByTitle - existing movie")
    public void testFindMovieByTitleWithExistingMovie() throws Exception {
        Movie movie = buildSingleMovieList().get(0);
        when(movieService.findMovieByTitle(any())).thenReturn(movie);
        mockMvc.perform(get("/api/gmdb/movies/title/{title}", "The Avengers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value(movie.getTitle()))
                .andExpect(jsonPath("director").value(movie.getDirector()))
                .andExpect(jsonPath("actors").value(movie.getActors()))
                .andExpect(jsonPath("release").value(movie.getRelease()))
                .andExpect(jsonPath("description").value(movie.getDescription()))
                .andExpect(jsonPath("rating").doesNotExist());
        verify(movieService, times(1)).findMovieByTitle(any());
    }

    /**
     * As a user, I can browse each movie so I can learn all the details.
     * <p>
     * Rule: Movie details include title, director, actors, release year, description and star rating.
     * <p>
     * Given a non-existing movie
     * When I visit that title
     * Then I receive a friendly message that it doesn't exist.
     *
     * @throws Exception
     */
    @Test
    @DisplayName("findMovieByTitle - non-existing movie")
    public void testFindMovieByTitleWithNonExistingMovie() throws Exception {
        when(movieService.findMovieByTitle(any())).thenReturn(null);
        mockMvc.perform(get("/api/gmdb/movies/title/{title}", "WonderWomen"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Movie doesn't exist"));
        verify(movieService, times(1)).findMovieByTitle(any());
    }

    /**
     * As a user, I can give a star rating to a movie so that I can share my experiences with others.
     * <p>
     * Given an existing movie
     * When I submit a 5 star rating
     * Then I can see it in the movie details.
     *
     * @throws Exception
     */
    @Test
    @DisplayName("updateRatingByTitle - existing movie (no star exists, but add one new 5Star)")
    public void testUpdateRatingByTitleRetrun5Star() throws Exception {
        Movie movie = buildSingleMovieList().get(0);
        movie.setRating("5");
        when(movieService.updateRatingByTitle(any(), any())).thenReturn(movie);
        mockMvc.perform(put("/api/gmdb/movies/title/{title}/rating/{rating}", "The Avengers", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value(movie.getTitle()))
                .andExpect(jsonPath("director").value(movie.getDirector()))
                .andExpect(jsonPath("actors").value(movie.getActors()))
                .andExpect(jsonPath("release").value(movie.getRelease()))
                .andExpect(jsonPath("description").value(movie.getDescription()))
                .andExpect(jsonPath("rating").value("5"));
        verify(movieService, times(1)).updateRatingByTitle(any(), any());
    }

    /**
     * As a user, I can give a star rating to a movie so that I can share my experiences with others.
     * <p>
     * Given an existing movie
     * When I submit a 5 star rating
     * Then I can see it in the movie details.
     *
     * @throws Exception
     */
    @Test
    @DisplayName("updateRatingByTitle - existing movie (one 5 star exist, add one new 3Star)")
    public void testUpdateRatingByTitleReturn4Star() throws Exception {
        Movie movie = buildSingleMovieList().get(0);
        movie.setRating("4");
        when(movieService.updateRatingByTitle(any(), any())).thenReturn(movie);
        mockMvc.perform(put("/api/gmdb/movies/title/{title}/rating/{rating}", "The Avengers", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value(movie.getTitle()))
                .andExpect(jsonPath("director").value(movie.getDirector()))
                .andExpect(jsonPath("actors").value(movie.getActors()))
                .andExpect(jsonPath("release").value(movie.getRelease()))
                .andExpect(jsonPath("description").value(movie.getDescription()))
                .andExpect(jsonPath("rating").value("4"));
        verify(movieService, times(1)).updateRatingByTitle(any(), any());
    }

    /**
     * As a user, I can review a movie so that I can share my thoughts about it.
     * <p>
     * Given an existing movie
     * When I submit a star rating and text review
     * Then I can see my contribution on the movie details.
     *
     * @throws Exception
     */
    @Test
    @DisplayName("updateReviewAndRatingByTitle - existing movie")
    public void testUpdateReviewAndRatingByTitle() throws Exception {
        Movie movie = buildSingleMovieList().get(0);
        movie.setRating("5");
        MovieUpdateRequest updateRequest = MovieUpdateRequest.builder()
                .review("Good movie to watch")
                .rating("5")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        when(movieService.updateRatingAndReviewByTitle(any(), any())).thenReturn(movie);
        mockMvc.perform(put("/api/gmdb/movies/title/{title}", "The Avengers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value(movie.getTitle()))
                .andExpect(jsonPath("director").value(movie.getDirector()))
                .andExpect(jsonPath("actors").value(movie.getActors()))
                .andExpect(jsonPath("release").value(movie.getRelease()))
                .andExpect(jsonPath("description").value(movie.getDescription()))
                .andExpect(jsonPath("rating").value(movie.getRating()))
                .andExpect(jsonPath("review").value(movie.getReview()));
        verify(movieService, times(1)).updateRatingAndReviewByTitle(any(), any());
    }

    /**
     * As a user, I can review a movie so that I can share my thoughts about it.
     * <p>
     * <p>
     * Given an existing movie
     * When I submit a text review without a star rating
     * Then I receive a friendly message that a star rating is required.
     *
     * @throws Exception
     */
    @Test
    @DisplayName("updateReviewAndRatingByTitle - existing movie, but without Rating")
    public void testUpdateReviewAndRatingByTitleWithoutRating() throws Exception {
        Movie movie = buildSingleMovieList().get(0);
        movie.setRating("5");
        MovieUpdateRequest updateRequest = MovieUpdateRequest.builder()
                .review("Good movie to watch")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        when(movieService.updateRatingAndReviewByTitle(any(), any())).thenReturn(movie);
        mockMvc.perform(put("/api/gmdb/movies/title/{title}", "The Avengers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("A star rating is required"));
        verify(movieService, times(0)).updateRatingAndReviewByTitle(any(), any());
    }

    /**
     * Builds a Movie list with multiple movie from a json file.
     *
     * @return List<Movie>
     * @throws IOException
     */
    private List<Movie> buildMultipleMovieList() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(MovieControllerTest.class.getClassLoader().getResourceAsStream("movies.json"), new TypeReference<ArrayList<Movie>>() {
        });
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
