package com.cluster.warehouse.service;

import com.cluster.warehouse.domain.Summary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Summary.
 */
public interface SummaryService {

    /**
     * Save a summary.
     *
     * @param summary the entity to save
     * @return the persisted entity
     */
    Summary save(Summary summary);

    /**
     * Get all the summaries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Summary> findAll(Pageable pageable);


    /**
     * Get the "id" summary.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Summary> findOne(Long id);
}
