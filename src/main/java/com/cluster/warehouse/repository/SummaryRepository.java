package com.cluster.warehouse.repository;

import com.cluster.warehouse.domain.Summary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Summary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SummaryRepository extends JpaRepository<Summary, Long> {

}
