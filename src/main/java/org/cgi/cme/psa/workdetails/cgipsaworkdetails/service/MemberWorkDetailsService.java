package org.cgi.cme.psa.workdetails.cgipsaworkdetails.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.cgi.cme.psa.workdetails.cgipsaworkdetails.configuration.UserExcelExporter;
import org.cgi.cme.psa.workdetails.cgipsaworkdetails.dto.MemberDetailsDTO;
import org.cgi.cme.psa.workdetails.cgipsaworkdetails.entity.MemberDetails;
import org.cgi.cme.psa.workdetails.cgipsaworkdetails.entity.WorkDaysDetails;
import org.cgi.cme.psa.workdetails.cgipsaworkdetails.repository.MemberDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MemberWorkDetailsService {
  
  @Autowired
  private MemberDetailsRepository memberDetailsRepository;
  
  @Autowired
  private EmailService emailService;
  
  @Autowired
  private UserExcelExporter userExcelExporter;
  
  @Value("${cgi.psa.email.from}")
  private String emailFrom;
  
  @Value("${cgi.psa.email.subject}")
  private String emailSubject;
  
  @Value("${cgi.psa.email.body}")
  private String emailBody;  
  
  @Value("${cgi.psa.work.hours.allocation.manager.email.subject}")
  private String psaEmailSubject;
  
  @Value("${cgi.psa.work.hours.allocation.manager.email.body}")
  private String psaEmailBody; 
  

  /**
   * Method: getMemberDetail
   * @param memberId type int
   * @return memberDetails type MemberDetailsDTO
   * 
   * Description: This method retrieved data from table for given member ID
   */
  public MemberDetailsDTO getMemberDetail(final int memberId) {
    MemberDetailsDTO memberDetailsDTO= new MemberDetailsDTO();
    log.info("Fetching details of member for given input member ID \t:"+memberId);
    MemberDetails memberDetail=memberDetailsRepository.findByMemberId(memberId);
    log.info("Retrieved result for given meber ID \t:"+memberDetail.toString());
    memberDetailsDTO = memberDetailsDTO.fromEntity(memberDetail);
    return memberDetailsDTO;
  }



  /**
   * Method : addNewMemberDetail
   * @param memberDetailsDTO type MemberDetailsDTO
   * @return memberDetailsDTOResponse type MemberDetailsDTO
   * 
   * Description: This method is used to save new members record in database table
   */
  public MemberDetailsDTO addNewMemberDetail(final MemberDetailsDTO memberDetailsDTO) {
    log.info("Adding new record for new member \t:"+memberDetailsDTO.toString());
    MemberDetailsDTO memberDetailsDTOResponse= new MemberDetailsDTO();
    final MemberDetails memberDetails=memberDetailsDTO.toEntity(memberDetailsDTO);
    final MemberDetails memberDetailsResponse=memberDetailsRepository.save(memberDetails);
    memberDetailsDTOResponse = memberDetailsDTOResponse.fromEntity(memberDetailsResponse);
    log.info("Record Saved to database table \t:"+memberDetailsDTOResponse.toString());
    return memberDetailsDTOResponse;
  }



  /**
   * Method: getAllMemberDetails
   * @return memberDetailsDTOList type List<MemberDetailsDTO> 
   * 
   * Description: This method will fetch all members details.
   */
  public List<MemberDetailsDTO> getAllMemberDetails() {
    List<MemberDetailsDTO> memberDetailsDTOList = new ArrayList<>();
    
    log.info("Fetching details for all members \t:");
    List<MemberDetails> memberDetailList = memberDetailsRepository.findAll(Sort.by("memberReportingManagerEmail").ascending());
    log.info("Retrieved result for all members from database \t:" + memberDetailList.toString());
    
    for (MemberDetails memberDetails : memberDetailList) {
      MemberDetailsDTO memberDetailsDTO = new MemberDetailsDTO();
      memberDetailsDTOList.add(memberDetailsDTO.fromEntity(memberDetails));
    }
    return memberDetailsDTOList;
  }



  /**
   * Method: updateMemberDetail
   * @param memberDetailsDTO type MemberDetailsDTO
   * @return memberDetailsDTOResponse type MemberDetailsDTO
   * 
   * Description: This method is used to update members details like contact number, reporting manager email ID.
   */
  public MemberDetailsDTO updateMemberDetail(MemberDetailsDTO memberDetailsDTO) {
    log.info("Updating members information details for member ID \t:"+memberDetailsDTO.getMemberId());
    MemberDetailsDTO memberDetailsDTOResponse= new MemberDetailsDTO();
    MemberDetails memberDetail=memberDetailsRepository.findByMemberId(memberDetailsDTO.getMemberId());
    memberDetail.setMobileNumber(memberDetailsDTO.getMobileNumber());
    memberDetail.setMemberReportingManagerEmail(memberDetailsDTO.getMemberReportingManagerEmail());
    memberDetail.setEmailIdToSendReport(memberDetailsDTO.getEmailIdToSendReport());
    final MemberDetails memberDetailsResponse=memberDetailsRepository.save(memberDetail);
    memberDetailsDTOResponse = memberDetailsDTOResponse.fromEntity(memberDetailsResponse);
    log.info("Updated members information details record \t:"+memberDetailsDTOResponse.toString());
    return memberDetailsDTOResponse;
  }



  /**
   * Method: updateMemberWorkingDaysInfo
   * @param memberDetailsDTO type MemberDetailsDTO
   * @return memberDetailsDTOResponse type MemberDetailsDTO
   * 
   * Description: This method will update number of working days and respective days status like working or leave
   */
  public MemberDetailsDTO updateMemberWorkingDaysInfo(MemberDetailsDTO memberDetailsDTO) {
    log.info("Updating members information details for member ID \t:"+memberDetailsDTO.getMemberId());
    MemberDetailsDTO memberDetailsDTOResponse= new MemberDetailsDTO();
    MemberDetails memberDetail=memberDetailsRepository.findByMemberId(memberDetailsDTO.getMemberId());
    
    if(null!=memberDetail) {
      memberDetail.setWorkDaysDetails(memberDetailsDTO.getWorkDaysDetails());
      memberDetail.setNoOfWorkingDays(memberDetailsDTO.getNoOfWorkingDays());
      MemberDetails memberDetailResponse=memberDetailsRepository.save(memberDetail);
      memberDetailsDTOResponse=memberDetailsDTOResponse.fromEntity(memberDetailResponse);
    }else {
      memberDetailsDTOResponse=new MemberDetailsDTO();
    }
    
    return memberDetailsDTOResponse;
  }
  
  
  /**
   * Method: sendEmailRequestToUpdateWorkDetails
   * 
   * Description: This method is used to send an email to member on every wednesday to update working days details
   */
  
  @Scheduled(cron="0 0 10 ? * WED")
  public String sendEmailRequestToUpdateWorkDetails() {
    
    List<MemberDetails> memberDetailList=memberDetailsRepository.findAll(Sort.by("memberReportingManagerEmail").ascending());
    
    log.info("Sending email to members to update their working days details");
    memberDetailList.parallelStream()
                    .forEach(member ->emailService.sendSimpleEmail(emailFrom,
                                                                   member.getMemberEmailId(), 
                                                                   emailBody,
                                                                   emailSubject));
    log.info("Applying value reset on days details");
    memberDetailList.parallelStream()
                    .forEach(member -> resetWorkDayDetail(member));
    
    return "Sending email to members and updating table to reset values is done";
  }
  
  /**
   * Method: resetWorkDayDetail
   * @param memberDetail type MemberDetails
   * 
   * Description: This method will set working days details to default values
   */
  private void resetWorkDayDetail(MemberDetails memberDetail) {
    memberDetail.getWorkDaysDetails().setSunday("Holiday");
    memberDetail.getWorkDaysDetails().setMonday("");
    memberDetail.getWorkDaysDetails().setTuesday("");
    memberDetail.getWorkDaysDetails().setWednesday("");
    memberDetail.getWorkDaysDetails().setThursday("");
    memberDetail.getWorkDaysDetails().setFriday("");
    memberDetail.getWorkDaysDetails().setSaturday("Holiday");
    memberDetailsRepository.save(memberDetail);
  }


  /**
   * Method: generatePSAWorkDaysReport
   * @return message type String
   * 
   * Description: This method is responsible for fetching all members details with working days and creates excel report 
   *              and sends respective work hour allocation manager and copies all managers in CC
   */
  //@Scheduled(cron="0 */2 * ? * *")
  @Scheduled(cron="0 0 8 ? * THU")
  public String generatePSAWorkDaysReport() {
    List<MemberDetailsDTO> memberDetailsDTOList = new ArrayList<>();

    log.info("Fetching details for all members to generate report \t:");
    List<MemberDetails> memberDetailList =  memberDetailsRepository.findAll(Sort.by("memberReportingManagerEmail").ascending());
    log.info("Retrieved result for all members from database for report generation\t:" + memberDetailList.toString());

    for (MemberDetails memberDetails : memberDetailList) {
      MemberDetailsDTO memberDetailsDTO = new MemberDetailsDTO();
      memberDetailsDTOList.add(memberDetailsDTO.fromEntity(memberDetails));
    }
    
    List<String> reportingEmailIdList=memberDetailsDTOList.parallelStream()
                                                          .map(member-> member.getEmailIdToSendReport())
                                                          .distinct()
                                                          .sorted()
                                                          .collect(Collectors.toUnmodifiableList());
    
    for(String reportingEmailId: reportingEmailIdList) {
      
     List<MemberDetailsDTO> memberListOnReportToEmailId=memberDetailsDTOList.parallelStream()
                                                                            .filter(member ->member.getEmailIdToSendReport().equalsIgnoreCase(reportingEmailId))
                                                                            .collect(Collectors.toUnmodifiableList());
    
     List<String> managerList=memberListOnReportToEmailId.parallelStream()
                                                          .map(object -> object.getMemberReportingManagerEmail())
                                                          .distinct()
                                                          .collect(Collectors.toUnmodifiableList());
     
     String fileName=userExcelExporter.createExcelSheet(memberListOnReportToEmailId);
     
     if(null!=fileName) {
       sendEmailWithReportAttachment(reportingEmailId,managerList, fileName);
     }
    
    }
    return "Sent an email with generated report to work hours allocation manager";
  }



  /**
   * Method : sendEmailWithReportAttachment
   * @param reportingEmailId type String
   * @param managerList type List<String>
   * @param fileName type String
   * 
   * Description: This method is used to send an email to PSA work hours allocation manager
   */
  private void sendEmailWithReportAttachment(String reportingEmailId, List<String> managerList, String fileName) {

    emailService.sendEmailwithReportAttachment( emailFrom, reportingEmailId, managerList, psaEmailBody,  psaEmailSubject, fileName);
  }
  
}
