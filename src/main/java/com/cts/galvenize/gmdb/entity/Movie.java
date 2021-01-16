package com.cts.galvenize.gmdb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
