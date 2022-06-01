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
public class MemberName {

  private String firstName;
  private String middleName;
  private String lastName;
  
}
