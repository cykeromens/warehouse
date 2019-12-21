package com.cluster.warehouse.service;

import com.cluster.warehouse.domain.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Report.
 */
public interface ReportService {

	/**
	 * Save a report.
	 *
	 * @param report the entity to save
	 * @return the persisted entity
	 */
	Report save(Report report);

	/**
	 * Get all the reports.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	Page<Report> findAll(Pageable pageable);

	/**
	 * Distinct FromIso count
	 */
	List<Report> generateReport();

	/**
	 * Get the "id" report.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	Optional<Report> findOne(String id);
}
