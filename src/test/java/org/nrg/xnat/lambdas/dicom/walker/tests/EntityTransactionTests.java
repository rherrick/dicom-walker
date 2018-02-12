/*
 * dicom-walker_test: org.nrg.xnat.lambdas.dicom.walker.tests.EntityTransactionTests
 * XNAT http://www.xnat.org
 * Copyright (c) 2005-2018, Washington University School of Medicine and Howard Hughes Medical Institute
 * All Rights Reserved
 *
 *  Released under the Simplified BSD.
 */

package org.nrg.xnat.lambdas.dicom.walker.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nrg.xnat.lambdas.dicom.walker.entities.Instance;
import org.nrg.xnat.lambdas.dicom.walker.entities.Scan;
import org.nrg.xnat.lambdas.dicom.walker.entities.ScanType;
import org.nrg.xnat.lambdas.dicom.walker.entities.Session;
import org.nrg.xnat.lambdas.dicom.walker.repositories.Instances;
import org.nrg.xnat.lambdas.dicom.walker.repositories.ScanTypes;
import org.nrg.xnat.lambdas.dicom.walker.repositories.Scans;
import org.nrg.xnat.lambdas.dicom.walker.repositories.Sessions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = OrmConfig.class)
@Transactional
public class EntityTransactionTests {
    @Test
    public void testStartup() {
        assertTrue(true);
    }

    @Test
    public void createCompleteSession() {
        initializeScanTypes();
        final List<Session> sessions = getSessions();
        _sessions.save(sessions);

        final Session sessionByStudyInstanceUid1 = _sessions.findByStudyInstanceUid(getStudyInstanceUid(1));
        assertNotNull(sessionByStudyInstanceUid1);
        assertEquals(getAccessionNumber(1), sessionByStudyInstanceUid1.getAccessionNumber());

        final List<Scan> allScans = _scans.findAll();
        assertNotNull(allScans);
        assertEquals(100, allScans.size());

        final List<Scan> session1Scans = sessionByStudyInstanceUid1.getScans();
        assertNotNull(session1Scans);
        assertEquals(10, session1Scans.size());

        final List<Instance> allInstances = _instances.findAll();
        assertNotNull(allInstances);
        assertEquals(1000, allInstances.size());

        final List<Instance> session1Scan1Instances = sessionByStudyInstanceUid1.getScans().get(0).getInstances();
        assertNotNull(session1Scan1Instances);
        assertEquals(10, session1Scans.size());
    }

    private List<Session> getSessions() {
        return IntStream.rangeClosed(1, 10).mapToObj(this::getSession).collect(Collectors.toList());
    }

    private Session getSession(final int sessionId) {
        final Session session = Session.builder()
                                       .accessionNumber(getAccessionNumber(sessionId))
                                       .studyId(getStudyId(sessionId))
                                       .studyInstanceUid(getStudyInstanceUid(sessionId))
                                       .scans(getScans(sessionId))
                                       .build();
        session.getScans().forEach(scan -> scan.setSession(session));
        return session;
    }

    private List<Scan> getScans(final int sessionId) {
        return IntStream.rangeClosed(1, 10).mapToObj(scanId -> getScan(sessionId, scanId)).collect(Collectors.toList());
    }

    private Scan getScan(final int sessionId, final int scanId) {
        final int cycle = scanId % 4;
        final Scan scan = Scan.builder()
                              .scanNumber(scanId)
                              .seriesInstanceUid("1.1.1.1." + sessionId + "." + scanId)
                              .scanType(_scanTypes.findByName(getScanTypeName(cycle)))
                              .modality(MODALITIES.get(cycle))
                              .sopClassUid(SOP_CLASS_UIDS.get(cycle))
                              .instances(getInstances(sessionId, scanId))
                              .build();
        scan.getInstances().forEach(instance -> instance.setScan(scan));
        return scan;
    }

    private List<Instance> getInstances(final int sessionId, final int scanId) {
        return IntStream.rangeClosed(1, 10).mapToObj(instanceId -> getInstance(sessionId, scanId, instanceId)).collect(Collectors.toList());
    }

    private Instance getInstance(final int sessionId, final int scanId, final int instanceId) {
        try {
            return Instance.builder()
                           .instanceNumber(instanceId)
                           .storageUri(getStorageUri(sessionId, scanId, instanceId))
                           .attributes(getAttributes(sessionId, scanId, instanceId))
                           .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    // private JsonNode getAttributes(final int sessionId, final int scanId, final int instanceId) {
    private String getAttributes(final int sessionId, final int scanId, final int instanceId) {
        final String studyInstanceUid  = getStudyInstanceUid(sessionId);
        final String seriesInstanceUid = studyInstanceUid + "." + scanId;
        final int    cycle             = scanId % 4;

        final ObjectNode parent = _objectMapper.createObjectNode();
        parent.put("accessionNumber", getAccessionNumber(sessionId));
        parent.put("studyId", getStudyId(sessionId));
        parent.put("studyInstanceUid", studyInstanceUid);
        parent.put("scanNumber", instanceId);
        parent.put("seriesInstanceUid", seriesInstanceUid);
        parent.put("scanType", getScanTypeName(cycle));
        parent.put("modality", MODALITIES.get(cycle));
        parent.put("sopClassUid", SOP_CLASS_UIDS.get(cycle));
        parent.put("instanceNumber", instanceId);
        // return parent;
        return parent.asText();
    }

    private void initializeScanTypes() {
        _scanTypes.save(IntStream.rangeClosed(1, 4).mapToObj(this::getScanType).collect(Collectors.toList()));
    }

    private ScanType getScanType(final int index) {
        return ScanType.builder().name(getScanTypeName(index)).matchingPatterns(Arrays.asList("description " + index, "type " + index)).build();
    }

    @NotNull
    private static String getStudyId(final int index) {
        return "Session " + index;
    }

    @NotNull
    private static String getStudyInstanceUid(final int index) {
        return "1.1.1.1." + index;
    }

    @NotNull
    private static String getAccessionNumber(final int session) {
        return "" + session;
    }

    @NotNull
    private static String getScanTypeName(final int cycle) {
        return "Scan Type " + cycle;
    }

    @NotNull
    private URI getStorageUri(final int session, final int scan, final int instance) throws URISyntaxException {
        return new URI("http://place/stuff/" + session + "/" + scan + "/" + instance);
    }

    private static final List<String> MODALITIES     = Arrays.asList("MR", "PT", "CT", "SR");
    private static final List<String> SOP_CLASS_UIDS = Arrays.asList("1.1.1", "1.1.2", "1.1.3", "1.1.4");

    @Autowired
    private ObjectMapper _objectMapper;

    @Autowired
    private Sessions _sessions;

    @Autowired
    private Scans _scans;

    @Autowired
    private Instances _instances;

    @Autowired
    private ScanTypes _scanTypes;
}
