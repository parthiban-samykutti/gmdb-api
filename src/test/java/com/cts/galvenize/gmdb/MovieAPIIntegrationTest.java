package com.cts.galvenize.gmdb;

import com.cts.galvenize.gmdb.entity.Movie;
import com.cts.galvenize.gmdb.entity.Rating;
import com.cts.galvenize.gmdb.model.MovieUpdateRequest;
import com.cts.galvenize.gmdb.repository.MovieRepository;
import com.cts.galvenize.gmdb.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MovieAPIIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    @DisplayName("findAllMovies - With No Movie")
    public void testFindAllMoviesWithNoMovie() throws Exception {
        mockMvc.perform(get("/api/gmdb/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(0));
    }

    @Test
    @DisplayName("findAllMovies - One value")
    public void testFindAllMoviesWithOneValue() throws Exception {
        createAvengerMovie(null);
        mockMvc.perform(get("/api/gmdb/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].title").value("The Avengers"))
                .andExpect(jsonPath("$[0].director").value("Joss Whedon"))
                .andExpect(jsonPath("$[0].actors").value("Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth"))
                .andExpect(jsonPath("$[0].release").value("2012"))
                .andExpect(jsonPath("$[0].description").value("Earth's mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army from enslaving humanity."))
                .andExpect(jsonPath("$[0].rating").doesNotExist());
    }

    @Test
    @DisplayName("findAllMovies - Multiple movies")
    public void testFindAllMoviesWithMultipleValue() throws Exception {
        createAvengerMovie(null);
        createSuperManMovie();
        mockMvc.perform(get("/api/gmdb/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(2))
                .andExpect(jsonPath("$[0].title").value("The Avengers"))
                .andExpect(jsonPath("$[0].director").value("Joss Whedon"))
                .andExpect(jsonPath("$[0].actors").value("Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth"))
                .andExpect(jsonPath("$[0].release").value("2012"))
                .andExpect(jsonPath("$[0].description").value("Earth's mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army from enslaving humanity."))
                .andExpect(jsonPath("$[0].rating").doesNotExist());
    }

    @Test
    @DisplayName("findMovieByTitle - existing movie")
    public void testFindMovieByTitleWithExistingMovie() throws Exception {
        Movie movie = createAvengerMovie(null);
        mockMvc.perform(get("/api/gmdb/movies/title/{title}", "The Avengers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value(movie.getTitle()))
                .andExpect(jsonPath("director").value(movie.getDirector()))
                .andExpect(jsonPath("actors").value(movie.getActors()))
                .andExpect(jsonPath("release").value(movie.getRelease()))
                .andExpect(jsonPath("description").value(movie.getDescription()))
                .andExpect(jsonPath("rating").doesNotExist());
    }

    @Test
    @DisplayName("findMovieByTitle - non-existing movie")
    public void testFindMovieByTitleWithNonExistingMovie() throws Exception {
        mockMvc.perform(get("/api/gmdb/movies/title/{title}", "WonderWomen"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Movie doesn't exist"));
    }

    @Test
    @DisplayName("updateRatingByTitle - existing movie (no star exists, but add one new 5Star)")
    public void testUpdateRatingByTitleRetrun5Star() throws Exception {
        Movie movie = createAvengerMovie(null);
        mockMvc.perform(put("/api/gmdb/movies/title/{title}/rating/{rating}", "The Avengers", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value(movie.getTitle()))
                .andExpect(jsonPath("director").value(movie.getDirector()))
                .andExpect(jsonPath("actors").value(movie.getActors()))
                .andExpect(jsonPath("release").value(movie.getRelease()))
                .andExpect(jsonPath("description").value(movie.getDescription()))
                .andExpect(jsonPath("rating").value("5"));
    }

    @Test
    @DisplayName("updateRatingByTitle - existing movie (one 5 star exist, add one new 3Star)")
    public void testUpdateRatingByTitleReturn4Star() throws Exception {
        Rating rating = Rating.builder()
                .fiveStar(1)
                .title("The Avengers")
                .build();
        Movie movie = createAvengerMovie(rating);
        mockMvc.perform(put("/api/gmdb/movies/title/{title}/rating/{rating}", "The Avengers", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value(movie.getTitle()))
                .andExpect(jsonPath("director").value(movie.getDirector()))
                .andExpect(jsonPath("actors").value(movie.getActors()))
                .andExpect(jsonPath("release").value(movie.getRelease()))
                .andExpect(jsonPath("description").value(movie.getDescription()))
                .andExpect(jsonPath("rating").value("4"));
    }

    @Test
    @DisplayName("updateReviewAndRatingByTitle - existing movie")
    public void testUpdateReviewAndRatingByTitle() throws Exception {
        Movie movie = createAvengerMovie(null);
        MovieUpdateRequest updateRequest = MovieUpdateRequest.builder()
                .review("Good movie to watch")
                .rating("5")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
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
    }

    @Test
    @DisplayName("updateReviewAndRatingByTitle - existing movie, but without Rating")
    public void testUpdateReviewAndRatingByTitleWithoutRating() throws Exception {
        createAvengerMovie(null);
        MovieUpdateRequest updateRequest = MovieUpdateRequest.builder()
                .review("Good movie to watch")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put("/api/gmdb/movies/title/{title}", "The Avengers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("A star rating is required"));

    }

    /**
     * Uitlity method to create SuperMan movie without rating in DB
     */
    private void createSuperManMovie() {
        Movie movie = Movie.builder()
                .title("Superman Returns")
                .actors("Brandon Routh, Kate Bosworth, Kevin Spacey, James Marsden")
                .director("Bryan Singer")
                .release("2006")
                .description("Superman returns to Earth after spending five years in space examining his homeworld Krypton. But he finds things have changed while he was gone, and he must once again prove himself important to the world.")
                .build();
        movieRepository.save(movie);
    }

    /**
     * Uitlity method to create SuperMan movie with/without rating in DB
     */
    private Movie createAvengerMovie(Rating rating) {
        Movie movie = Movie.builder()
                .title("The Avengers")
                .actors("Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth")
                .director("Joss Whedon")
                .release("2012")
                .ratingPattern(rating)
                .description("Earth's mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army from enslaving humanity.")
                .build();
        return movieRepository.save(movie);
    }
}
