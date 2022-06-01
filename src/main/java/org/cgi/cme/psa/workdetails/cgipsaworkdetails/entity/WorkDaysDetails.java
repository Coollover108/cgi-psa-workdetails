package org.cgi.cme.psa.workdetails.cgipsaworkdetails.entity;

import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@ToString
@Builder
public class WorkDaysDetails {
  
  private String sunday;
  
  private String monday;
  
  private String tuesday;
  
  private String wednesday;
  
  private String thursday;
  
  private String friday;
  
  private String saturday;
 }
