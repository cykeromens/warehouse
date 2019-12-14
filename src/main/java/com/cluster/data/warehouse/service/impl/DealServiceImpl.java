package com.cluster.data.warehouse.service.impl;

import com.cluster.data.warehouse.domain.Deal;
import com.cluster.data.warehouse.repository.DealRepository;
import com.cluster.data.warehouse.service.DealService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Deal.
 */
@Service
@Transactional
public class DealServiceImpl implements DealService {

    private final Logger log = LoggerFactory.getLogger(DealServiceImpl.class);

    private final DealRepository dealRepository;

    public DealServiceImpl(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    /**
     * Save a deal.
     *
     * @param deal the entity to save
     * @return the persisted entity
     */
    @Override
    public Deal save(Deal deal) {
        log.debug("Request to save Deal : {}", deal);
        return dealRepository.save(deal);
    }

    /**
     * Get all the deals.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Deal> findAll(Pageable pageable) {
        log.debug("Request to get all Deals");
        return dealRepository.findAll(pageable);
    }


    /**
     * Get one deal by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Deal> findOne(Long id) {
        log.debug("Request to get Deal : {}", id);
        return dealRepository.findById(id);
    }

    /**
     * Delete the deal by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Deal : {}", id);
        dealRepository.deleteById(id);
    }
}
