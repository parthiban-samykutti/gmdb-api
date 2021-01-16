package com.cts.galvenize.gmdb.controller;

import com.cts.galvenize.gmdb.service.MovieService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class MovieControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;
    @Test
    @DisplayName("findAllMovies - Null value")
    public void testFindAllMoviesWithNullValue() throws Exception {
        when(movieService.findAllMovies()).thenReturn(null);
        MvcResult mvcResult= mockMvc.perform(get("/api/gmdb/movies"))
                .andExpect(status().isOk()).andReturn();
        assertThat(mvcResult.getResponse().getContentAsString()).isEmpty();
        verify(movieService, times(1)).findAllMovies();
    }

    @Test
    @DisplayName("findAllMovies - empty list")
    public void testFindAllMoviesWithEmptyList() throws Exception {
        mockMvc.perform(get("/api/gmdb/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(0));
    }

}
