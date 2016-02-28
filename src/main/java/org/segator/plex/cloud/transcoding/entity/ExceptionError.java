/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.segator.plex.cloud.transcoding.entity;

/**
 *
 * @author isaac_000
 */
public class ExceptionError extends IJSON {

    private Integer error;
    private String message;

    public ExceptionError(Integer error, String message) {
        this.error = error;
        this.message = message;
        this.setResult("KO");
    }

    public Integer getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
