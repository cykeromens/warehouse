package com.cluster.warehouse.service;

import com.cluster.warehouse.domain.InvalidDeal;
import com.cluster.warehouse.domain.InvalidDeal_;
import com.cluster.warehouse.repository.InvalidDealRepository;
import com.cluster.warehouse.service.dto.InvalidDealCriteria;
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
 * Service for executing complex queries for InvalidDeal entities in the database.
 * The main input is a {@link InvalidDealCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InvalidDeal} or a {@link Page} of {@link InvalidDeal} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvalidDealQueryService extends QueryService<InvalidDeal> {

    private final Logger log = LoggerFactory.getLogger(InvalidDealQueryService.class);

    private final InvalidDealRepository invalidDealRepository;

    public InvalidDealQueryService(InvalidDealRepository invalidDealRepository) {
        this.invalidDealRepository = invalidDealRepository;
    }

    /**
     * Return a {@link List} of {@link InvalidDeal} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InvalidDeal> findByCriteria(InvalidDealCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InvalidDeal> specification = createSpecification(criteria);
        return invalidDealRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link InvalidDeal} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InvalidDeal> findByCriteria(InvalidDealCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InvalidDeal> specification = createSpecification(criteria);
        return invalidDealRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InvalidDealCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InvalidDeal> specification = createSpecification(criteria);
        return invalidDealRepository.count(specification);
    }

    /**
     * Function to convert InvalidDealCriteria to a {@link Specification}
     */
    private Specification<InvalidDeal> createSpecification(InvalidDealCriteria criteria) {
        Specification<InvalidDeal> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), InvalidDeal_.id));
            }
            if (criteria.getTagId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTagId(), InvalidDeal_.tagId));
            }
            if (criteria.getFromIsoCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFromIsoCode(), InvalidDeal_.fromIsoCode));
            }
            if (criteria.getToIsoCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getToIsoCode(), InvalidDeal_.toIsoCode));
            }
            if (criteria.getTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTime(), InvalidDeal_.time));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), InvalidDeal_.amount));
            }
            if (criteria.getSource() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSource(), InvalidDeal_.source));
            }
            if (criteria.getSourceFormat() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSourceFormat(), InvalidDeal_.sourceFormat));
            }
            if (criteria.getReason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReason(), InvalidDeal_.reason));
            }
        }
        return specification;
    }
}
