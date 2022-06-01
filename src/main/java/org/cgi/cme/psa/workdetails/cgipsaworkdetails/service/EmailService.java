package org.cgi.cme.psa.workdetails.cgipsaworkdetails.service;

import java.io.File;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class EmailService {
  
  @Autowired
  private JavaMailSender javaMailSender;
  
  /**
   * Method: sendSimpleEmail
   * @param fromEmail type String
   * @param toEmail type String
   * @param body type String
   * @param subject type String
   * 
   * Description: This method sends an email to each employee to request them to update their work details from portal
   */
  public void sendSimpleEmail(String fromEmail,
                              String toEmail,
                              String body,
                              String subject) {
    log.info("Sending email for updating working days details to member /t :"+toEmail);
    SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
    simpleMailMessage.setFrom(fromEmail);
    simpleMailMessage.setTo(toEmail);
    simpleMailMessage.setText(body);
    simpleMailMessage.setSubject(subject);
    log.info("Email has been sent to member /t :"+toEmail);
    javaMailSender.send(simpleMailMessage);
    
  }
  
  /**
   * Method: sendEmailwithReportAttachment
   * @param fromEmail type String
   * @param toEmail type String
   * @param emailCC type List<String>
   * @param body type String
   * @param subject type String
   * @param attachment type String
   * 
   * @Description: This method will send an email to PSA work hours allocation manager with attachment of all reporting employees based on working days numbers
   */
  public void sendEmailwithReportAttachment(String fromEmail,
                                            String toEmail,
                                            List<String> emailCC,
                                            String body,
                                            String subject,
                                            String attachment) {
    
    MimeMessage mimeMessage=javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper mimeMessageHelper= new MimeMessageHelper(mimeMessage, true);
      String[] arr = emailCC.stream().toArray(String[] ::new);
      
      log.info("Sending email to PSA work hours allocation manager /t :"+toEmail);
      mimeMessageHelper.setFrom(fromEmail);
      mimeMessageHelper.setTo(toEmail);
      mimeMessageHelper.setCc(arr);
      mimeMessageHelper.setText(body);
      mimeMessageHelper.setSubject(subject);
      
      FileSystemResource fileSystem= new FileSystemResource(new File(attachment));
      mimeMessageHelper.addAttachment(fileSystem.getFilename(), fileSystem);
      
      log.info("Email has been sent to PSA work hours allocation manager /t :"+toEmail);
      javaMailSender.send(mimeMessage);
      
    } catch (MessagingException exception) {
      log.error("Encountered error while constructing mime message"+exception.getMessage());
      exception.printStackTrace();
    }
  }

}
