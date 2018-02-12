/*
 * dicom-walker_main: org.nrg.xnat.lambdas.dicom.walker.models.Response
 * XNAT http://www.xnat.org
 * Copyright (c) 2005-2018, Washington University School of Medicine and Howard Hughes Medical Institute
 * All Rights Reserved
 *
 *  Released under the Simplified BSD.
 */

package org.nrg.xnat.lambdas.dicom.walker.models;

public class Response {
    public Response() {

    }

    public Response(final Status status, final String message) {
        _status = status;
        _message = message;
    }

    public enum Status {
        OK, ERROR;

    }

    public String getMessage() {
        return _message;
    }

    public Response setMessage(final String message) {
        _message = message;
        return this;
    }

    public Status getStatus() {
        return _status;
    }

    public void setStatus(final Status status) {
        _status = status;
    }

    private String _message;
    private Status _status;
}
