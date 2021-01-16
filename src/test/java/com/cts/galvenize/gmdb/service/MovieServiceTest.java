package com.cts.galvenize.gmdb.service;

import com.cts.galvenize.gmdb.controller.MovieControllerTest;
import com.cts.galvenize.gmdb.entity.Movie;
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
