package com.thejatech.whatsapp.repository;

import com.thejatech.whatsapp.model.Submission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubmissionRepository extends MongoRepository<Submission, String> {
    Optional<Submission> findByCouponCode(String couponCode);
}
