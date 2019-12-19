package com.cluster.warehouse.web.rest;

import com.cluster.warehouse.domain.RecordCount;
import com.cluster.warehouse.service.RecordCountService;
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
 * REST controller for managing RecordCount.
 */
@RestController
@RequestMapping("/api")
public class RecordCountResource {

    private final Logger log = LoggerFactory.getLogger(RecordCountResource.class);

    private static final String ENTITY_NAME = "recordCount";

    private final RecordCountService recordCountService;

    public RecordCountResource(RecordCountService recordCountService) {
        this.recordCountService = recordCountService;
    }

    /**
     * GET  /record-counts : get all the recordCounts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of recordCounts in body
     */
    @GetMapping("/record-counts")
    public ResponseEntity<List<RecordCount>> getAllRecordCounts(Pageable pageable) {
        log.debug("REST request to get a page of RecordCounts");
        Page<RecordCount> page = recordCountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/record-counts");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /record-counts/:id : get the "id" recordCount.
     *
     * @param id the id of the recordCount to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the recordCount, or with status 404 (Not Found)
     */
    @GetMapping("/record-counts/{id}")
    public ResponseEntity<RecordCount> getRecordCount(@PathVariable Long id) {
        log.debug("REST request to get RecordCount : {}", id);
        Optional<RecordCount> recordCount = recordCountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recordCount);
    }
}
