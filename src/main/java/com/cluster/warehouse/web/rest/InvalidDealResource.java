package com.cluster.warehouse.web.rest;

import com.cluster.warehouse.domain.InvalidDeal;
import com.cluster.warehouse.service.InvalidDealService;
import com.cluster.warehouse.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing InvalidDeal.
 */
@RestController
@RequestMapping("/api")
public class InvalidDealResource {

    private final Logger log = LoggerFactory.getLogger(InvalidDealResource.class);

    private static final String ENTITY_NAME = "invalidDeal";

    private final InvalidDealService invalidDealService;

    public InvalidDealResource(InvalidDealService invalidDealService) {
        this.invalidDealService = invalidDealService;
    }

    /**
     * GET  /invalid-deals : get all the invalidDeals.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of invalidDeals in body
     */
    @GetMapping("/invalid-deals")
    public ResponseEntity<List<InvalidDeal>> getAllInvalidDeals(Pageable pageable) {
        log.debug("REST request to get a page of InvalidDeals");
        Page<InvalidDeal> page = invalidDealService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/invalid-deals");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /invalid-deals/:id : get the "id" invalidDeal.
     *
     * @param id the id of the invalidDeal to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invalidDeal, or with status 404 (Not Found)
     */
    @GetMapping("/invalid-deals/{id}")
    public ResponseEntity<InvalidDeal> getInvalidDeal(@PathVariable String id) {
        log.debug("REST request to get InvalidDeal : {}", id);
        Optional<InvalidDeal> invalidDeal = invalidDealService.findOne(id);
        return invalidDeal.map(invalidDeal1 -> ResponseEntity.ok().body(invalidDeal1)).orElseGet(() -> ResponseEntity.notFound().build());

    }

    /**
     * {@code SEARCH  /_search/invalid-deals?query=:query} : search for the Deal corresponding
     * to the query.
     *
     * @param query    the query of the approvalLog search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/invalid-deals")
    public ResponseEntity<List<InvalidDeal>> searchApprovalLogs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of invalid-deals for query {}", query);
        Page<InvalidDeal> page = invalidDealService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/invalid-deals");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
