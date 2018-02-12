/*
 * dicom-walker_main: org.nrg.xnat.lambdas.dicom.walker.services.impl.jpa.JpaDicomStudyService
 * XNAT http://www.xnat.org
 * Copyright (c) 2005-2018, Washington University School of Medicine and Howard Hughes Medical Institute
 * All Rights Reserved
 *
 *  Released under the Simplified BSD.
 */

package org.nrg.xnat.lambdas.dicom.walker.services.impl.jpa;

import lombok.extern.slf4j.Slf4j;
import org.nrg.xnat.lambdas.dicom.walker.entities.Instance;
import org.nrg.xnat.lambdas.dicom.walker.entities.Scan;
import org.nrg.xnat.lambdas.dicom.walker.entities.ScanType;
import org.nrg.xnat.lambdas.dicom.walker.entities.Session;
import org.nrg.xnat.lambdas.dicom.walker.repositories.Instances;
import org.nrg.xnat.lambdas.dicom.walker.repositories.ScanTypes;
import org.nrg.xnat.lambdas.dicom.walker.repositories.Scans;
import org.nrg.xnat.lambdas.dicom.walker.repositories.Sessions;
import org.nrg.xnat.lambdas.dicom.walker.services.DicomStudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class JpaDicomStudyService implements DicomStudyService {
    @Autowired
    public JpaDicomStudyService(final Sessions sessions, final Scans scans, final Instances instances, final ScanTypes scanTypes) {
        _sessions = sessions;
        _scans = scans;
        _instances = instances;
        _scanTypes = scanTypes;
    }

    @Override
    public long getSessionCount() {
        return _sessions.count();
    }

    @Override
    public Session getSessionByStudyInstanceUid(final String studyInstanceUid) {
        return _sessions.findByStudyInstanceUid(studyInstanceUid);
    }

    @Override
    public Session getSessionByAccessionNumber(final String accessionNumber) {
        return _sessions.findByAccessionNumber(accessionNumber);
    }

    @Override
    public List<Session> getSessionsByStudyId(final String studyId) {
        return _sessions.findAllByStudyId(studyId);
    }

    @Override
    public List<Session> getSessionsByDescription(final String description) {
        return _sessions.findAllByDescriptionIsLike(description);
    }

    @Override
    public List<Session> getSessionsByPatientId(final String patientId) {
        return _sessions.findAllByPatientId(patientId);
    }

    @Override
    public List<Session> getSessionsByPatientName(final String patientName) {
        return _sessions.findAllByPatientName(patientName);
    }

    @Override
    public List<Scan> getScans(final Session session) {
        return session.getScans();
    }

    @Override
    public Map<Scan, List<Instance>> getInstances(final Session session) {
        return getScans(session).stream().collect(Collectors.toMap(Function.identity(), this::getInstances));
    }

    @Override
    public List<Instance> getInstances(final Scan scan) {
        return scan.getInstances();
    }

    @Override
    public List<Scan> getScansByModality(final String modality) {
        return _scans.findAllByModality(modality);
    }

    @Override
    public List<Scan> getScansByScanType(final String scanTypeName) {
        final ScanType scanType = _scanTypes.findByName(scanTypeName);
        if (scanType == null) {
            return Collections.emptyList();
        }
        return _scans.findAllByScanType(scanType);
    }

    @Override
    public List<Scan> getScansByScanTypePattern(final String description) {
        final List<ScanType> scanTypes = _scanTypes.findAllByMatchingPatternsContains(description);
        if (scanTypes == null || scanTypes.isEmpty()) {
            return Collections.emptyList();
        }
        return scanTypes.stream().map(_scans::findAllByScanType).flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public Instance getInstance(final String uri) throws URISyntaxException {
        return getInstance(new URI(uri));
    }

    @Override
    public Instance getInstance(final URI uri) {
        return _instances.findByStorageUri(uri);
    }

    private final Sessions  _sessions;
    private final Scans     _scans;
    private final Instances _instances;
    private final ScanTypes _scanTypes;
}
