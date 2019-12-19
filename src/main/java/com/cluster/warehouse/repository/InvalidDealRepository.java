package com.cluster.warehouse.repository;

import com.cluster.warehouse.domain.InvalidDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the InvalidDeal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvalidDealRepository extends JpaRepository<InvalidDeal, Long>, JpaSpecificationExecutor<InvalidDeal> {

}
