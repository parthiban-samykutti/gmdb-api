package com.cts.galvenize.gmdb.service;

import com.cts.galvenize.gmdb.entity.Movie;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class MovieService {

    public List<Movie> findAllMovies() {
        return new ArrayList<>();
    }
}
