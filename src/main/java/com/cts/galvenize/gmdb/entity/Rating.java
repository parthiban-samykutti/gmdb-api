package com.cts.galvenize.gmdb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rating {

   @Id
   private String title;
   private int oneStar;
   private int twoStar;
   private int threeStar;
   private int fourStar;
   private int fiveStar;

}
