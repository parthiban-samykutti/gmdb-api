package com.cts.galvenize.gmdb.controller;

import com.cts.galvenize.gmdb.service.MovieService;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
