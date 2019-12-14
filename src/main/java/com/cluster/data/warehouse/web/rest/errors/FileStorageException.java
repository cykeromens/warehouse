package com.cluster.data.warehouse.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;

public class FileStorageException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;
    private String msg;

    public FileStorageException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}