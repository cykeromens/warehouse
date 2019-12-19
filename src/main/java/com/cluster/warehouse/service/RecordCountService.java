package com.cluster.warehouse.service;

import com.cluster.warehouse.domain.RecordCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing RecordCount.
 */
public interface RecordCountService {

    /**
     * Save a recordCount.
     *
     * @param recordCount the entity to save
     * @return the persisted entity
     */
    RecordCount save(RecordCount recordCount);

    /**
     * Get all the recordCounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RecordCount> findAll(Pageable pageable);


    /**
     * Get the "id" recordCount.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RecordCount> findOne(Long id);

}
