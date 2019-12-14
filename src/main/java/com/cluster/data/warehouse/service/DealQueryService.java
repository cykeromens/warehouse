package com.cluster.data.warehouse.service;

import com.cluster.data.warehouse.domain.Deal;
import com.cluster.data.warehouse.domain.Deal_;
import com.cluster.data.warehouse.repository.DealRepository;
import com.cluster.data.warehouse.service.dto.DealCriteria;
import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for executing complex queries for Deal entities in the database.
 * The main input is a {@link DealCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Deal} or a {@link Page} of {@link Deal} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DealQueryService extends QueryService<Deal> {

    private final Logger log = LoggerFactory.getLogger(DealQueryService.class);

    private final DealRepository dealRepository;

    public DealQueryService(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    /**
     * Return a {@link List} of {@link Deal} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Deal> findByCriteria(DealCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Deal> specification = createSpecification(criteria);
        return dealRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Deal} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Deal> findByCriteria(DealCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Deal> specification = createSpecification(criteria);
        return dealRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DealCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Deal> specification = createSpecification(criteria);
        return dealRepository.count(specification);
    }

    /**
     * Function to convert DealCriteria to a {@link Specification}
     */
    private Specification<Deal> createSpecification(DealCriteria criteria) {
        Specification<Deal> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Deal_.id));
            }
            if (criteria.getTagId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTagId(), Deal_.tagId));
            }
            if (criteria.getFromIsoCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFromIsoCode(), Deal_.fromIsoCode));
            }
            if (criteria.getToIsoCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getToIsoCode(), Deal_.toIsoCode));
            }
            if (criteria.getTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTime(), Deal_.time));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Deal_.amount));
            }
            if (criteria.getSource() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSource(), Deal_.source));
            }
            if (criteria.getSourceFormat() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSourceFormat(), Deal_.sourceFormat));
            }
        }
        return specification;
    }
}
