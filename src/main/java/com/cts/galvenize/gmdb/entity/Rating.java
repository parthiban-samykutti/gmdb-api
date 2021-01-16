package com.cts.galvenize.gmdb.entity;

import lombok.Data;

import javax.persistence.Id;

@Data
public class Rating {

   @Id
   private String title;
   private int oneStar;
   private int twoStar;
   private int threeStar;
   private int fourStar;
   private int fiveStar;

}
