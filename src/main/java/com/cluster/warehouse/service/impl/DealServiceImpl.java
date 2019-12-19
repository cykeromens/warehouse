package com.cluster.warehouse.service.impl;

import com.cluster.warehouse.domain.Deal;
import com.cluster.warehouse.repository.DealRepository;
import com.cluster.warehouse.service.DealService;
import com.cluster.warehouse.service.dealpool.ForkJoinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;

/**
 * Service Implementation for managing Deal.
 */
@Service
@Transactional
public class DealServiceImpl implements DealService {

    private final Logger log = LoggerFactory.getLogger(DealServiceImpl.class);

    @Value("${app.upload.delimiter}")
    private final static String DELIMITER = ";";
    @Value("${app.upload.dir}")
    private String uploadDir = "./";
    @Value("${app.thread.count}")
    private static final int CONCURRENCY_COUNT = 10;



    private final DealRepository dealRepository;
    private final ForkJoinService forkJoinService;

    public DealServiceImpl(DealRepository dealRepository, ForkJoinService forkJoinService) {
        this.dealRepository = dealRepository;
        this.forkJoinService = forkJoinService;
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
     * Save a file.
     *
     * @param file the entity to save
     */
    public void uploadFile(MultipartFile file) throws RuntimeException {
        log.debug("Request to upload file to path: {}", uploadDir);
        File dir = new File(uploadDir);
        String fileName = file.getOriginalFilename();
        Path location = Paths.get(uploadDir + File.separator + StringUtils.cleanPath(
                Objects.requireNonNull(fileName)));
        try {
            boolean dirExists = dir.exists() || dir.mkdir();
            if (dirExists) {
                log.debug("Coping file to directory");
                long copy = Files.copy(file.getInputStream(), location, StandardCopyOption.REPLACE_EXISTING);
                if (copy > 0) {
                    log.debug("File copied, forkjoin processing");
                    forkJoinService.fileToDatabase(location);
                    log.debug("Finished processing file.");
                }
            }
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("Duplicate records! "
                    + " Some of this file record contains already existing record.");
        } catch (Exception e) {
            log.error("Batch update failed: ", e.getMessage());
            e.printStackTrace();
//                throw new RuntimeException("Could not store file " + fileName
//                        + " records . Please try again!");
        }

    }

    @Override
    public boolean exists(String fileName) {
        Path location = Paths.get(uploadDir + File.separator + StringUtils.cleanPath(
                Objects.requireNonNull(fileName)));
        return location.toFile().exists();
    }

}
