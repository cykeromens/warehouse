package com.cluster.warehouse.service;

import com.cluster.warehouse.domain.Deal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * Service Interface for managing Deal.
 */
public interface DealService {

    /**
     * Save a deal.
     *
     * @param deal the entity to save
     * @return the persisted entity
     */
    Deal save(Deal deal);

    /**
     * Get all the deals.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Deal> findAll(Pageable pageable);

    /**
     * Get the "id" deal.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Deal> findOne(Long id);

    /**
     * Save a file to path.
     *
     * @param file the entity to save to directory
     */
    void uploadFile(MultipartFile file);

    /**
     * Check if file has been uploaded.
     *
     * @param fileName the entity to save to directory
     */
    boolean exists(String fileName);
}
