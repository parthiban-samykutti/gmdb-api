package com.cts.galvenize.gmdb.repository;

import com.cts.galvenize.gmdb.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, String> {
}
