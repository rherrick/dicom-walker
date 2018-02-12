/*
 * dicom-walker_main: org.nrg.xnat.lambdas.dicom.walker.handlers.DicomWalkerRequestHandler
 * XNAT http://www.xnat.org
 * Copyright (c) 2005-2018, Washington University School of Medicine and Howard Hughes Medical Institute
 * All Rights Reserved
 *
 *  Released under the Simplified BSD.
 */

package org.nrg.xnat.lambdas.dicom.walker.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.nrg.xnat.lambdas.dicom.walker.models.Request;
import org.nrg.xnat.lambdas.dicom.walker.models.Response;
import org.nrg.xnat.lambdas.dicom.walker.services.DicomStudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DicomWalkerRequestHandler implements RequestHandler<Request, Response> {
    /**
     * Dependency injection is handled via autowiring!
     */
    @Autowired
    public DicomWalkerRequestHandler(final DicomStudyService service) {
        _service = Objects.requireNonNull(service, "DICOM study service");
    }

    @Override
    public Response handleRequest(final Request request, final Context context) {
        final String responseMessage = "Request message: " + request.getMessage() + ", DICOM study service contains " + _service.getSessionCount() + " sessions";
        return new Response(Response.Status.OK, responseMessage);
    }

    private final DicomStudyService _service;
}
