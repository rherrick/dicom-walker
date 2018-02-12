/*
 * dicom-walker_main: org.nrg.xnat.lambdas.dicom.walker.DicomWalkerRequestHandler
 * XNAT http://www.xnat.org
 * Copyright (c) 2005-2018, Washington University School of Medicine and Howard Hughes Medical Institute
 * All Rights Reserved
 *
 *  Released under the Simplified BSD.
 */

package org.nrg.xnat.lambdas.dicom.walker;

import me.ccampo.spring.aws.lambda.SpringRequestHandler;
import org.nrg.xnat.lambdas.dicom.walker.models.Request;
import org.nrg.xnat.lambdas.dicom.walker.models.Response;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DicomWalkerRequestHandler extends SpringRequestHandler<Request, Response> {
    @Override
    public ApplicationContext getApplicationContext() {
        return context;
    }

    /**
     * Here we create the Spring {@link ApplicationContext} that will
     * be used throughout our application.
     */
    private static final ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
}
