package com.cts.galvenize.gmdb.entity;

import lombok.Data;

import javax.persistence.Id;

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
}
