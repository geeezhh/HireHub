package com.hirehub.app.showcase;

import java.util.List;

/**
 * Curated tech employers with public career portals (not scraped; URLs maintained in code).
 * {@link #matchKeyword} is used to relate HireHub {@code JobPost} rows to this card when employer
 * company name contains the keyword (case-insensitive).
 */
public record CompanyShowcase(
        String name,
        String industry,
        String tagline,
        String careersUrl,
        String linkedinJobsUrl,
        List<String> sampleJobTitles,
        String matchKeyword,
        String redditDiscussUrl,
        String googleSearchJobsUrl) {
}
