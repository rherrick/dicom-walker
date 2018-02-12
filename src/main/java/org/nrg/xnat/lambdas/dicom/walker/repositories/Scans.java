/*
 * dicom-walker_main: org.nrg.xnat.lambdas.dicom.walker.repositories.Scans
 * XNAT http://www.xnat.org
 * Copyright (c) 2005-2018, Washington University School of Medicine and Howard Hughes Medical Institute
 * All Rights Reserved
 *
 *  Released under the Simplified BSD.
 */

package org.nrg.xnat.lambdas.dicom.walker.repositories;

import org.nrg.xnat.lambdas.dicom.walker.entities.Scan;
import org.nrg.xnat.lambdas.dicom.walker.entities.ScanType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Scans extends JpaRepository<Scan, String> {
    Scan findBySeriesInstanceUid(final String seriesInstanceUid);

    // Scan findBySessionAndScanNumber(final Session session, final int number);

    // List<Scan> findAllBySession(final Session session);

    List<Scan> findAllByScanType(final ScanType scanType);

    // List<Scan> findAllBySessionAndScanType(final Session session, final ScanType scanType);

    List<Scan> findAllByModality(final String modality);

    // List<Scan> findAllBySessionAndModality(final Session session, final String modality);

    List<Scan> findAllBySopClassUid(final String sopClassUid);

    // List<Scan> findAllBySessionAndSopClassUid(final Session session, final String sopClassUid);
}
