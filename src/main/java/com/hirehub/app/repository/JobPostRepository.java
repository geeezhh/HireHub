package com.hirehub.app.repository;

import com.hirehub.app.entity.JobPost;
import com.hirehub.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Long> {

    // All active jobs (for browse page)
    List<JobPost> findByStatusOrderByCreatedAtDesc(String status);

    // Jobs posted by a specific employer
    List<JobPost> findByEmployerOrderByCreatedAtDesc(User employer);

    // Search jobs by keyword in title or description
    @Query("SELECT j FROM JobPost j WHERE j.status = 'ACTIVE' AND " +
           "(LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(j.skills) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(j.location) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<JobPost> searchJobs(@Param("keyword") String keyword);

    // Count active jobs by employer
    long countByEmployerAndStatus(User employer, String status);
}