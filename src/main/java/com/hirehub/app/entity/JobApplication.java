package com.hirehub.app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ── Who applied ───────────────────────────────────────────
    @ManyToOne
    @JoinColumn(name = "seeker_id", nullable = false)
    private User seeker;

    // ── Which job ─────────────────────────────────────────────
    @ManyToOne
    @JoinColumn(name = "job_post_id", nullable = false)
    private JobPost jobPost;

    // ── Status: APPLIED, REVIEWING, SHORTLISTED, REJECTED ────
    private String status = "APPLIED";

    @Column(updatable = false)
    private LocalDateTime appliedAt;

    @PrePersist
    public void prePersist() {
        this.appliedAt = LocalDateTime.now();
        if (this.status == null) this.status = "APPLIED";
    }

    // ── Getters & Setters ─────────────────────────────────────

    public Long getId() { return id; }

    public User getSeeker() { return seeker; }
    public void setSeeker(User seeker) { this.seeker = seeker; }

    public JobPost getJobPost() { return jobPost; }
    public void setJobPost(JobPost jobPost) { this.jobPost = jobPost; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getAppliedAt() { return appliedAt; }
}