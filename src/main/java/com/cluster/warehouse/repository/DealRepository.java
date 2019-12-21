package com.cluster.warehouse.repository;

import com.cluster.warehouse.domain.Deal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Deal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DealRepository extends MongoRepository<Deal, String> {

	Page<Deal> findBySourceContaining(String fileName, Pageable pageable);
}
