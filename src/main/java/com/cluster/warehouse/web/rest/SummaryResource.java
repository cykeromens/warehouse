package com.cluster.warehouse.web.rest;

import com.cluster.warehouse.domain.Summary;
import com.cluster.warehouse.service.SummaryService;
import com.cluster.warehouse.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Summary.
 */
@RestController
@RequestMapping("/api")
public class SummaryResource {

    private final Logger log = LoggerFactory.getLogger(SummaryResource.class);

    private static final String ENTITY_NAME = "summary";

    private final SummaryService summaryService;

    public SummaryResource(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    /**
     * GET  /summaries : get all the summaries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of summaries in body
     */
    @GetMapping("/summaries")
    public ResponseEntity<List<Summary>> getAllSummaries(Pageable pageable) {
        log.debug("REST request to get a page of Summaries");
        Page<Summary> page = summaryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/summaries");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /summaries/:id : get the "id" summary.
     *
     * @param id the id of the summary to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the summary, or with status 404 (Not Found)
     */
    @GetMapping("/summaries/{id}")
    public ResponseEntity<Summary> getSummary(@PathVariable Long id) {
        log.debug("REST request to get Summary : {}", id);
        Optional<Summary> summary = summaryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(summary);
    }
}
