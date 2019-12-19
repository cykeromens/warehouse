package com.cluster.warehouse.service.impl;

import com.cluster.warehouse.domain.RecordCount;
import com.cluster.warehouse.repository.RecordCountRepository;
import com.cluster.warehouse.service.RecordCountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing RecordCount.
 */
@Service
@Transactional
public class RecordCountServiceImpl implements RecordCountService {

    private final Logger log = LoggerFactory.getLogger(RecordCountServiceImpl.class);

    private final RecordCountRepository recordCountRepository;

    public RecordCountServiceImpl(RecordCountRepository recordCountRepository) {
        this.recordCountRepository = recordCountRepository;
    }

    /**
     * Save a recordCount.
     *
     * @param recordCount the entity to save
     * @return the persisted entity
     */
    @Override
    public RecordCount save(RecordCount recordCount) {
        log.debug("Request to save RecordCount : {}", recordCount);
        return recordCountRepository.save(recordCount);
    }

    /**
     * Get all the recordCounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RecordCount> findAll(Pageable pageable) {
        log.debug("Request to get all RecordCounts");
        return recordCountRepository.findAll(pageable);
    }


    /**
     * Get one recordCount by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RecordCount> findOne(Long id) {
        log.debug("Request to get RecordCount : {}", id);
        return recordCountRepository.findById(id);
    }
}
