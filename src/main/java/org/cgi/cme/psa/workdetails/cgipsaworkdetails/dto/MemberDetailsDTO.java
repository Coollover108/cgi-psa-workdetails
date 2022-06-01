package org.cgi.cme.psa.workdetails.cgipsaworkdetails.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.cgi.cme.psa.workdetails.cgipsaworkdetails.entity.MemberDetails;
import org.cgi.cme.psa.workdetails.cgipsaworkdetails.entity.MemberName;
import org.cgi.cme.psa.workdetails.cgipsaworkdetails.entity.WorkDaysDetails;
import org.springframework.beans.BeanUtils;
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
public class MemberDetailsDTO {
  
  @NotNull
  private int memberId;
  
  private MemberName memberName;
  
  @NotBlank
  private String memberEmailId;
  
  private String mobileNumber;
  
  @NotBlank
  private String memberReportingManagerEmail;
  
  private int noOfWorkingDays;
  
  private WorkDaysDetails workDaysDetails;
  
  @NotBlank
  private String emailIdToSendReport;

  public MemberDetailsDTO fromEntity(MemberDetails memberDetails) {
    BeanUtils.copyProperties(memberDetails, this);
    return this;
  }

  public MemberDetails toEntity(MemberDetailsDTO memberDetailsDTO) {
    MemberDetails memberDetails = new MemberDetails();
    BeanUtils.copyProperties(memberDetailsDTO, memberDetails);
    return memberDetails;
  }
}
