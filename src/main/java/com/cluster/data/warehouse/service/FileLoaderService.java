package com.cluster.data.warehouse.service;

import com.cluster.data.warehouse.web.rest.errors.FileStorageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;

/**
 * Service Interface for managing FileLoader.
 */
public interface FileLoaderService {

    /**
     * Save a file to path.
     *
     * @param file the entity to save to directory
     */
    void uploadFile(MultipartFile file) throws FileStorageException;


    /**
     * Save file contents on DB.
     *
     * @param path to the file directory
     */
    void extractToDB(Path path, String fileName) throws FileStorageException;
}
