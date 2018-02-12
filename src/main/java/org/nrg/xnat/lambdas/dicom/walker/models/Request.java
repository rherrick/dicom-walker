/*
 * dicom-walker_main: org.nrg.xnat.lambdas.dicom.walker.models.Request
 * XNAT http://www.xnat.org
 * Copyright (c) 2005-2018, Washington University School of Medicine and Howard Hughes Medical Institute
 * All Rights Reserved
 *
 *  Released under the Simplified BSD.
 */

package org.nrg.xnat.lambdas.dicom.walker.models;

public class Request {
    public Request() {

    }

    public Request(final String message) {
        _message = message;
    }

    public String getMessage() {
        return _message;
    }

    public Request setMessage(final String message) {
        _message = message;
        return this;
    }

    private String _message;
}
