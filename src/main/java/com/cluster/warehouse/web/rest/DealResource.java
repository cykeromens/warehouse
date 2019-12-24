package com.cluster.warehouse.web.rest;

import com.cluster.warehouse.domain.Deal;
import com.cluster.warehouse.service.DealService;
import com.cluster.warehouse.web.rest.errors.BadRequestAlertException;
import com.cluster.warehouse.web.rest.errors.InternalServerErrorException;
import com.cluster.warehouse.web.rest.util.HeaderUtil;
import com.cluster.warehouse.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing Deal.
 */
@RestController
@RequestMapping("/api")
public class DealResource {

    private final Logger log = LoggerFactory.getLogger(DealResource.class);

    private static final String ENTITY_NAME = "deal";

    private final DealService dealService;


    public DealResource(DealService dealService) {
        this.dealService = dealService;
    }


    /**
     * POST  /deals uploads file to a path.
     *
     * @param file the file to upload
     * @return the ResponseEntity with status 200 (Ok) and with body the new fileLoader, or with status 400 (Bad Request) if the fileLoader has already an ID
     * @throws InternalServerErrorException if the Location URI syntax is incorrect
     */
    @PostMapping("/deals")
    public ResponseEntity<Map<String, Integer>> uploadDeal(@RequestPart("file") MultipartFile file) throws InternalServerErrorException {
        log.debug("REST request to save file  : {}", file);
        if (file.getOriginalFilename() == null) {
            throw new BadRequestAlertException("File cannot be empty!", ENTITY_NAME, "idexists");
        }
        if (dealService.exists(file.getOriginalFilename())) {
            throw new BadRequestAlertException("Duplicates not allowed! File already uploaded.", ENTITY_NAME, "duplicate");
        }
        Map<String, Integer> result = dealService.uploadFile(file);
        if (result.containsKey("error")) {
			throw new BadRequestAlertException("Could not read file records", file.getName(), "path");
        }
		if (result.containsKey("violations")) {
			return ResponseEntity.ok()
					.headers(HeaderUtil.createFailureAlert("deal", "violations",
							"Could not upload file"))
					.body(result);
        }
        return ResponseEntity.ok()
                .headers(HeaderUtil.createFileUploadAlert(result, file.getName()))
                .body(result);
    }

    /**
     * GET  /deals : get all the deals.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of deals in body
     */
    @GetMapping("/deals")
    public ResponseEntity<List<Deal>> getAllDeals(Pageable pageable) {
        log.debug("REST request to get a page of Deals");
        Page<Deal> page = dealService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/deals");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /deals/:id : get the "id" deal.
     *
     * @param id the id of the deal to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the deal, or with status 404 (Not Found)
     */
    @GetMapping("/deals/{id}")
    public ResponseEntity<Deal> getDeal(@PathVariable String id) {
        log.debug("REST request to get Deal : {}", id);
        Optional<Deal> deal = dealService.findOne(id);
        return deal.map(deal1 -> ResponseEntity.ok().body(deal1)).orElseGet(() -> ResponseEntity.notFound().build());

    }

    /**
     * {@code SEARCH  /_search/deals?query=:query} : search for the Deal corresponding
     * to the query.
     *
     * @param query    the query of the approvalLog search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/deals")
    public ResponseEntity<List<Deal>> searchApprovalLogs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ApprovalLogs for query {}", query);
        Page<Deal> page = dealService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/deals");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
