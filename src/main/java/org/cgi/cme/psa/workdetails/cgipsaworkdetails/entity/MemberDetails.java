package org.cgi.cme.psa.workdetails.cgipsaworkdetails.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity(name= "psaMemberDetails")
public class MemberDetails {
  
  @Id 
  @Column(name="Id")
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private int id;
  
  @NotNull
  @Column(name="Member_Id", unique = true)
  private int memberId;
  
  @Embedded
  @Column(name="Member_Name")
  private MemberName memberName;
  
  @NotBlank
  @Column(name="Member_EMail_Id")
  private String memberEmailId;
  
  @Column(name="Member_Contact_Number")
  private String mobileNumber;
  
  @NotBlank
  @Column(name="ReportingManager_EmailId")
  private String memberReportingManagerEmail;
  
  @Column(name="No_Of_Working_Days")
  private int noOfWorkingDays;
  
  @Embedded
  @Column(name="WorkDays_Details")
  private WorkDaysDetails workDaysDetails;
  
  @NotBlank
  @Column(name="Email_ID_To_Send_Report")
  private String emailIdToSendReport;
  }
