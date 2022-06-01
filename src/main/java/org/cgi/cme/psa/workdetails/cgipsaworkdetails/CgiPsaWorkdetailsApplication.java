package org.cgi.cme.psa.workdetails.cgipsaworkdetails;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CgiPsaWorkdetailsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CgiPsaWorkdetailsApplication.class, args);
	}

}
