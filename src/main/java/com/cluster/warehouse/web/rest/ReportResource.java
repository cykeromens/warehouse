package com.cluster.warehouse.web.rest;

import com.cluster.warehouse.domain.Report;
import com.cluster.warehouse.service.ReportService;
import com.cluster.warehouse.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Report.
 */
@RestController
@RequestMapping("/api")
public class ReportResource {

	private final Logger log = LoggerFactory.getLogger(ReportResource.class);

	private static final String ENTITY_NAME = "report";

	private final ReportService reportService;

	public ReportResource(ReportService reportService) {
		this.reportService = reportService;
	}

	/**
	 * GET  /reports : get all the reports.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of reports in body
	 */
	@GetMapping("/reports")
	public ResponseEntity<List<Report>> getAllReports(Pageable pageable) {
		log.debug("REST request to get a page of Reports");
		Page<Report> page = reportService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reports");
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * GET  /reports/:id : get the "id" report.
	 *
	 * @param id the id of the report to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the report, or with status 404 (Not Found)
	 */
	@GetMapping("/reports/{id}")
	public ResponseEntity<Report> getReport(@PathVariable String id) {
		log.debug("REST request to get Report : {}", id);
		Optional<Report> report = reportService.findOne(id);
		return report.map(report1 -> ResponseEntity.ok().body(report1)).orElseGet(() -> ResponseEntity.notFound().build());
	}
}
