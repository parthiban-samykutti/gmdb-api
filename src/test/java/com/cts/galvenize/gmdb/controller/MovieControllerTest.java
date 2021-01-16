package com.cts.galvenize.gmdb.controller;

import com.cts.galvenize.gmdb.entity.Movie;
import com.cts.galvenize.gmdb.service.MovieService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
