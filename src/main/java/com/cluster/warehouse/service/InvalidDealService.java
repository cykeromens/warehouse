package com.cluster.warehouse.service;

import com.cluster.warehouse.domain.InvalidDeal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing InvalidDeal.
 */
public interface InvalidDealService {

    /**
     * Save a invalidDeal.
     *
     * @param invalidDeal the entity to save
     * @return the persisted entity
     */
    InvalidDeal save(InvalidDeal invalidDeal);

    /**
     * Get all the invalidDeals.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<InvalidDeal> findAll(Pageable pageable);


    /**
     * Get the "id" invalidDeal.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<InvalidDeal> findOne(String id);

    /**
     * Search for the InvalidDeal corresponding to the query.
     *
     * @param query    the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InvalidDeal> search(String query, Pageable pageable);

}
