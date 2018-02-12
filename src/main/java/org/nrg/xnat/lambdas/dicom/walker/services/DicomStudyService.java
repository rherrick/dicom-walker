/*
 * dicom-walker_main: org.nrg.xnat.lambdas.dicom.walker.services.DicomStudyService
 * XNAT http://www.xnat.org
 * Copyright (c) 2005-2018, Washington University School of Medicine and Howard Hughes Medical Institute
 * All Rights Reserved
 *
 *  Released under the Simplified BSD.
 */

package org.nrg.xnat.lambdas.dicom.walker.services;

import org.nrg.xnat.lambdas.dicom.walker.entities.Instance;
import org.nrg.xnat.lambdas.dicom.walker.entities.Scan;
import org.nrg.xnat.lambdas.dicom.walker.entities.Session;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface DicomStudyService {
    long getSessionCount();

    Session getSessionByStudyInstanceUid(final String studyInstanceUid);

    Session getSessionByAccessionNumber(final String accessionNumber);

    List<Session> getSessionsByStudyId(final String studyId);

    List<Session> getSessionsByDescription(final String description);

    List<Session> getSessionsByPatientId(final String patientId);

    List<Session> getSessionsByPatientName(final String patientName);

    List<Scan> getScans(final Session session);

    /**
     * Gets all instances contained in the specified session.
     *
     * @param session The session to retrieve.
     *
     * @return All DICOM instances in the specified session.
     */
    Map<Scan, List<Instance>> getInstances(final Session session);

    /**
     * Gets all instances contained in the specified scan.
     *
     * @param scan The scan to retrieve.
     *
     * @return All DICOM instances in the specified scan.
     */
    List<Instance> getInstances(final Scan scan);

    List<Scan> getScansByModality(String modality);

    List<Scan> getScansByScanType(String scanTypeName);

    List<Scan> getScansByScanTypePattern(String description);

    Instance getInstance(String uri) throws URISyntaxException;

    Instance getInstance(URI uri);
}
