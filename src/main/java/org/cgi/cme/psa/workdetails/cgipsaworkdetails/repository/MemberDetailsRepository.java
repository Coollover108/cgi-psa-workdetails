package org.cgi.cme.psa.workdetails.cgipsaworkdetails.repository;

import org.cgi.cme.psa.workdetails.cgipsaworkdetails.entity.MemberDetails;
import org.cgi.cme.psa.workdetails.cgipsaworkdetails.entity.WorkDaysDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MemberDetailsRepository extends JpaRepository<MemberDetails, Integer>{
  
  @Transactional(readOnly = true)
  MemberDetails findByMemberId(final int memberId);
 }
