package com.hirehub.app.repository;

import com.hirehub.app.entity.JobApplication;
import com.hirehub.app.entity.JobPost;
import com.hirehub.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    // All applications by a seeker (for seeker dashboard)
    List<JobApplication> findBySeekerOrderByAppliedAtDesc(User seeker);

    // All applications for a job (for employer to review)
    List<JobApplication> findByJobPostOrderByAppliedAtDesc(JobPost jobPost);

    // All applications for all jobs by an employer
    List<JobApplication> findByJobPost_EmployerOrderByAppliedAtDesc(User employer);

    // Check if seeker already applied to a job
    boolean existsBySeekerAndJobPost(User seeker, JobPost jobPost);

    // Count applications by status for a seeker
    long countBySeekerAndStatus(User seeker, String status);

    // Count total applicants for a job post
    long countByJobPost(JobPost jobPost);

    // Get shortlisted applicants for an employer
    List<JobApplication> findByJobPost_EmployerAndStatus(User employer, String status);
}