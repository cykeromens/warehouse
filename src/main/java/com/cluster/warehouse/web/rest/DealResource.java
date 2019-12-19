package com.cluster.warehouse.web.rest;

import com.cluster.warehouse.domain.Deal;
import com.cluster.warehouse.service.DealQueryService;
import com.cluster.warehouse.service.DealService;
import com.cluster.warehouse.service.FileProcessorService;
import com.cluster.warehouse.service.dto.DealCriteria;
import com.cluster.warehouse.web.rest.errors.BadRequestAlertException;
import com.cluster.warehouse.web.rest.errors.FileExistAlertException;
import com.cluster.warehouse.web.rest.util.HeaderUtil;
import com.cluster.warehouse.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
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

    @Autowired
    private FileProcessorService fileProcessorService;

    private final DealQueryService dealQueryService;

    public DealResource(DealService dealService, DealQueryService dealQueryService) {
        this.dealService = dealService;
        this.dealQueryService = dealQueryService;
    }

    /**
     * POST  /deals uploads file to a path.
     *
     * @param file the file to upload
     * @return the ResponseEntity with status 200 (Ok) and with body the new fileLoader, or with status 400 (Bad Request) if the fileLoader has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/deals")
    public ResponseEntity<String> createFileLoader(@RequestPart("file") MultipartFile file) throws FileExistAlertException {
        log.debug("REST request to save file  : {}", file);
        if (file.getOriginalFilename() == null) {
            throw new BadRequestAlertException("File cannot be empty!", ENTITY_NAME, "idexists");
        }
        if (dealService.exists(file.getOriginalFilename())) {
            throw new BadRequestAlertException("Duplicates not allowed! File already uploaded.", ENTITY_NAME, "duplicate");
        }
        dealService.uploadFile(file);

//        return null;//ResponseEntity.created(new URI("/api/file-loaders/" + result.getId()))
//                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
//                .body(result);
//        redirectAttributes.addFlashAttribute("message",
//                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return ResponseEntity.ok().build();
    }

    /**
     * PUT  /deals : Updates an existing deal.
     *
     * @param deal the deal to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated deal,
     * or with status 400 (Bad Request) if the deal is not valid,
     * or with status 500 (Internal Server Error) if the deal couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/deals")
    public ResponseEntity<Deal> updateDeal(@Valid @RequestBody Deal deal) throws URISyntaxException {
        log.debug("REST request to update Deal : {}", deal);
        if (deal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Deal result = dealService.save(deal);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, deal.getId().toString()))
            .body(result);
    }

    /**
     * GET  /deals : get all the deals.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of deals in body
     */
    @GetMapping("/deals")
    public ResponseEntity<List<Deal>> getAllDeals(DealCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Deals by criteria: {}", criteria);
        Page<Deal> page = dealQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/deals");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /deals/count : count all the deals.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/deals/count")
    public ResponseEntity<Long> countDeals(DealCriteria criteria) {
        log.debug("REST request to count Deals by criteria: {}", criteria);
        return ResponseEntity.ok().body(dealQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /deals/:id : get the "id" deal.
     *
     * @param id the id of the deal to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the deal, or with status 404 (Not Found)
     */
    @GetMapping("/deals/{id}")
    public ResponseEntity<Deal> getDeal(@PathVariable Long id) {
        log.debug("REST request to get Deal : {}", id);
        Optional<Deal> deal = dealService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deal);
    }
}
