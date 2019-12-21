package com.cluster.warehouse.repository;

import com.cluster.warehouse.domain.InvalidDeal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the InvalidDeal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvalidDealRepository extends MongoRepository<InvalidDeal, String> {

	Page<InvalidDeal> findBySourceContaining(String fileName, Pageable pageable);
}
