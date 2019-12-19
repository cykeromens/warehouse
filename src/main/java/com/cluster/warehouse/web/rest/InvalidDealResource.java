package com.cluster.warehouse.web.rest;

import com.cluster.warehouse.domain.InvalidDeal;
import com.cluster.warehouse.service.InvalidDealQueryService;
import com.cluster.warehouse.service.InvalidDealService;
import com.cluster.warehouse.service.dto.InvalidDealCriteria;
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
 * REST controller for managing InvalidDeal.
 */
@RestController
@RequestMapping("/api")
public class InvalidDealResource {

    private final Logger log = LoggerFactory.getLogger(InvalidDealResource.class);

    private static final String ENTITY_NAME = "invalidDeal";

    private final InvalidDealService invalidDealService;

    private final InvalidDealQueryService invalidDealQueryService;

    public InvalidDealResource(InvalidDealService invalidDealService, InvalidDealQueryService invalidDealQueryService) {
        this.invalidDealService = invalidDealService;
        this.invalidDealQueryService = invalidDealQueryService;
    }

    /**
     * GET  /invalid-deals : get all the invalidDeals.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of invalidDeals in body
     */
    @GetMapping("/invalid-deals")
    public ResponseEntity<List<InvalidDeal>> getAllInvalidDeals(InvalidDealCriteria criteria, Pageable pageable) {
        log.debug("REST request to get InvalidDeals by criteria: {}", criteria);
        Page<InvalidDeal> page = invalidDealQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/invalid-deals");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /invalid-deals/count : count all the invalidDeals.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the count in body
     */
    @GetMapping("/invalid-deals/count")
    public ResponseEntity<Long> countInvalidDeals(InvalidDealCriteria criteria) {
        log.debug("REST request to count InvalidDeals by criteria: {}", criteria);
        return ResponseEntity.ok().body(invalidDealQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /invalid-deals/:id : get the "id" invalidDeal.
     *
     * @param id the id of the invalidDeal to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invalidDeal, or with status 404 (Not Found)
     */
    @GetMapping("/invalid-deals/{id}")
    public ResponseEntity<InvalidDeal> getInvalidDeal(@PathVariable Long id) {
        log.debug("REST request to get InvalidDeal : {}", id);
        Optional<InvalidDeal> invalidDeal = invalidDealService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invalidDeal);
    }
}
