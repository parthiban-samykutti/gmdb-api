package com.cts.galvenize.gmdb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Data
public class Movie {
    @Id
    private String title;
    private String director;
    private String actors;
    private String release;
    private String description;
    private String rating;
    private String review;
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private Rating ratingPattern;
}
