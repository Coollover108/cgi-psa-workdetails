package org.cgi.cme.psa.workdetails.cgipsaworkdetails.controller;

import java.util.List;
import org.cgi.cme.psa.workdetails.cgipsaworkdetails.dto.MemberDetailsDTO;
import org.cgi.cme.psa.workdetails.cgipsaworkdetails.service.MemberWorkDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/cme_psa_app")
public class MemberWorkDetailsController {
  
  @Autowired
  private MemberWorkDetailsService memberWorkDetailsService;
  
  
  /**
   * Method: getMemberDetail
   * @param memberId type int 
   * @return ResponseEntity type MemberDetailsDTO
   * 
   * Description: This method will allow user to fetch details of specific member PSA based of member ID.
   */
  @GetMapping("/getMemberDetail")
  private ResponseEntity<MemberDetailsDTO> getMemberDetail(@RequestParam int memberId) {
   // Demo for failure.....
    log.info("Retrieving member detais for give member ID"+memberId);
    MemberDetailsDTO memberDetailsDTO=memberWorkDetailsService.getMemberDetail(memberId);
    log.info("Retrieved member detais for give member ID"+memberId);
    return memberDetailsDTO == null 
                               ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                               : new ResponseEntity<>(memberDetailsDTO, HttpStatus.OK);
  }
  
  
  /**
   * Method: addNewMember
   * @param memberDetailsDTO type MemberDetailsDTO
   * @return memberDetailsDTO type MemberDetailsDTO
   * 
   * Description: This method will add new members record to DB
   */
  @PostMapping("/addNewMember")
  private ResponseEntity<MemberDetailsDTO> addNewMember(@RequestBody MemberDetailsDTO memberDetailsDTO) {
    log.info("Adding new member to member details table"+memberDetailsDTO.toString());
    MemberDetailsDTO memberDetailsDTOResponse=memberWorkDetailsService.addNewMemberDetail(memberDetailsDTO);
    return memberDetailsDTOResponse == null 
                               ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                               : new ResponseEntity<>(memberDetailsDTOResponse, HttpStatus.OK);
  }
  
 
  /**
   * Method: getAllMemberDetails
   * @return memberDetailsDTOList type ResponseEntity<List<MemberDetailsDTO>>
   * 
   * Description: This method is used to fetch details for all employees
   */
  @GetMapping("/getAllMemberDetail")
  private ResponseEntity<List<MemberDetailsDTO>> getAllMemberDetails() {
    log.info("Retrieving member detais for all members");
    List<MemberDetailsDTO> memberDetailsDTOList=memberWorkDetailsService.getAllMemberDetails();
    return memberDetailsDTOList == null 
                               ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                               : new ResponseEntity<>(memberDetailsDTOList, HttpStatus.OK);
  }
  
  /**
   * Method: updateMemberInfo
   * @param memberDetailsDTO type MemberDetailsDTO
   * @return ResponseEntity<MemberDetailsDTO>
   * 
   * Description: This method is used to update Members information like Contact number and Reporting manager email Id
   */
  @PutMapping("/updateMemberInfo")
  private ResponseEntity<MemberDetailsDTO> updateMemberInfo(@RequestBody MemberDetailsDTO memberDetailsDTO) {
    log.info("Update member details for employee like contact number or reporting manager"+memberDetailsDTO.toString());
    MemberDetailsDTO memberDetailsDTOResponse=memberWorkDetailsService.updateMemberDetail(memberDetailsDTO);
    return memberDetailsDTOResponse == null 
                               ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                               : new ResponseEntity<>(memberDetailsDTOResponse, HttpStatus.OK);
    }
  
  
  /**
   * Method: updateMemberWorkingDaysInfo
   * @param memberDetailsDTO type MemberDetailsDTO
   * @return ResponseEntity<MemberDetailsDTO>
   * 
   * Description: This method is used to update Members work days details.
   */
  @PatchMapping("/updateMemberWorkingDaysInfo")
  private ResponseEntity<MemberDetailsDTO> updateMemberWorkingDaysInfo(@RequestBody MemberDetailsDTO memberDetailsDTO) {
    log.info("Update member details for  Members work days details"+memberDetailsDTO.toString());
    MemberDetailsDTO memberDetailsDTOResponse=memberWorkDetailsService.updateMemberWorkingDaysInfo(memberDetailsDTO);
    return memberDetailsDTOResponse == null 
                               ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                               : new ResponseEntity<>(memberDetailsDTOResponse, HttpStatus.OK);
    }
  
  
  /**
   * Method: sendEmailToMembers
   * @return response type String
   * 
   * Description: This method will send an Email to each member and reset value of work days to default.
   */
  @PostMapping("/triggerEmailToMembers")
  private String sendEmailToMembers() {
    log.info("Inside method : sendEmailToMembers()");
    String response =memberWorkDetailsService.sendEmailRequestToUpdateWorkDetails();
    log.info("Response received Inside method : sendEmailToMembers()"+response);
    return response;
  }
  
  
  
  @PostMapping("/triggerReportCreation")
  private String generatePSAWorkDaysReport() {
    log.info("Inside method : generatePSAWorkDaysReport()");
    String response =memberWorkDetailsService.generatePSAWorkDaysReport();
    log.info("Response received Inside method : generatePSAWorkDaysReport()"+response);
    return response;
  }
  
  
  
}
