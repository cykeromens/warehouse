package com.cluster.warehouse.service.impl;

import com.cluster.warehouse.domain.InvalidDeal;
import com.cluster.warehouse.repository.InvalidDealRepository;
import com.cluster.warehouse.service.InvalidDealService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service Implementation for managing InvalidDeal.
 */
@Service
public class InvalidDealServiceImpl implements InvalidDealService {

    private final Logger log = LoggerFactory.getLogger(InvalidDealServiceImpl.class);

    private final InvalidDealRepository invalidDealRepository;

    public InvalidDealServiceImpl(InvalidDealRepository invalidDealRepository) {
        this.invalidDealRepository = invalidDealRepository;
    }

    /**
     * Save a invalidDeal.
     *
     * @param invalidDeal the entity to save
     * @return the persisted entity
     */
    @Override
    public InvalidDeal save(InvalidDeal invalidDeal) {
        log.debug("Request to save InvalidDeal : {}", invalidDeal);
        return invalidDealRepository.save(invalidDeal);
    }

    /**
     * Get all the invalidDeals.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<InvalidDeal> findAll(Pageable pageable) {
        log.debug("Request to get all InvalidDeals");
        return invalidDealRepository.findAll(pageable);
    }


    /**
     * Get one invalidDeal by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<InvalidDeal> findOne(String id) {
        log.debug("Request to get InvalidDeal : {}", id);
        return invalidDealRepository.findById(id);
    }

    /**
     * Search for the Deal corresponding to the query.
     *
     * @param query    the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<InvalidDeal> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Deals for source file {}", query);
        return invalidDealRepository.findBySourceContaining(query, pageable);
    }
}
