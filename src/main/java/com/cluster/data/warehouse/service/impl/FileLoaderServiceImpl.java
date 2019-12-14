package com.cluster.data.warehouse.service.impl;

import com.cluster.data.warehouse.domain.Deal;
import com.cluster.data.warehouse.repository.DealRepository;
import com.cluster.data.warehouse.service.DealService;
import com.cluster.data.warehouse.service.FileLoaderService;
import com.cluster.data.warehouse.web.rest.errors.FileStorageException;
import liquibase.util.file.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.nio.file.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Service Implementation for managing FileLoader.
 */
@Service
@Transactional
public class FileLoaderServiceImpl implements FileLoaderService {

    private final Logger log = LoggerFactory.getLogger(FileLoaderServiceImpl.class);

    @Value("${app.upload.new:${user.home}}")
    private final static String DELIMITER = ";";
    @Value("${app.upload.new:${user.home}}")
    private String uploadDir;

    private DealRepository dealRepository;

    public FileLoaderServiceImpl(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    /**
     * Save a file.
     *
     * @param file the entity to save
     */
    public void uploadFile(MultipartFile file) throws FileStorageException {
        log.debug("Request to save file : {}", file);

        Path location = Paths.get(uploadDir + File.separator + StringUtils.cleanPath(
                Objects.requireNonNull(file.getOriginalFilename())));
        if(!new File(uploadDir).exists() && !new File(uploadDir).mkdirs()) {
            throw new FileStorageException("Directory could not be found or created!");
        }

        try {
            if(location.toFile().exists()){
                throw new FileStorageException("File already exists!");
            }
            long copy = Files.copy(file.getInputStream(), location, StandardCopyOption.REPLACE_EXISTING);
            if(copy > 0){
                extractToDB(location, file.getOriginalFilename());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileStorageException("Could not store file " + file.getOriginalFilename()
                    + ". Please try again!");
        }
    }

    @Override
    public void extractToDB(Path path, String fileName) throws FileStorageException {
        try {
            String row;
            int i =0;
            BufferedReader csvReader = new BufferedReader(new FileReader(path.toString()));
            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(DELIMITER);
                long datum5 = Long.parseLong(data[5]);
                ZonedDateTime dateTime = Timestamp.from(Instant.ofEpochSecond(datum5)).toLocalDateTime().atZone(ZoneId.of("+01:00"));
                Deal deal = new Deal()
                        .tagId(data[0])
                        .fromIsoCode(data[1])
                        .fromCountry(data[2])
                        .toIsoCode(data[3])
                        .toCountry(data[4])
                        .time(dateTime)
                        .amount(new BigDecimal(data[6]))
                        .source(fileName)
                        .sourceFormat(FilenameUtils.getExtension(fileName));
                dealRepository.saveAndFlush(deal);
            }
            csvReader.close();
        }catch (Exception e) {
            e.printStackTrace();
            throw new FileStorageException("Could not store file " + fileName
                    + ". Please try again!");
        }
    }

}
