package com.cluster.warehouse.service.impl;

import com.cluster.warehouse.domain.Summary;
import com.cluster.warehouse.repository.SummaryRepository;
import com.cluster.warehouse.service.SummaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Summary.
 */
@Service
@Transactional
public class SummaryServiceImpl implements SummaryService {

    private final Logger log = LoggerFactory.getLogger(SummaryServiceImpl.class);

    private final SummaryRepository summaryRepository;

    public SummaryServiceImpl(SummaryRepository summaryRepository) {
        this.summaryRepository = summaryRepository;
    }

    /**
     * Save a summary.
     *
     * @param summary the entity to save
     * @return the persisted entity
     */
    @Override
    public Summary save(Summary summary) {
        log.debug("Request to save Summary : {}", summary);
        return summaryRepository.save(summary);
    }

    /**
     * Get all the summaries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Summary> findAll(Pageable pageable) {
        log.debug("Request to get all Summaries");
        return summaryRepository.findAll(pageable);
    }


    /**
     * Get one summary by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Summary> findOne(Long id) {
        log.debug("Request to get Summary : {}", id);
        return summaryRepository.findById(id);
    }
}
