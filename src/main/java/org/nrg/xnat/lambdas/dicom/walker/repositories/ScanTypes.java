/*
 * dicom-walker_main: org.nrg.xnat.lambdas.dicom.walker.repositories.ScanTypes
 * XNAT http://www.xnat.org
 * Copyright (c) 2005-2018, Washington University School of Medicine and Howard Hughes Medical Institute
 * All Rights Reserved
 *
 *  Released under the Simplified BSD.
 */

package org.nrg.xnat.lambdas.dicom.walker.repositories;

import org.nrg.xnat.lambdas.dicom.walker.entities.ScanType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScanTypes extends JpaRepository<ScanType, String> {
    ScanType findByName(final String name);

    List<ScanType> findAllByNameIsLike(final String name);

    List<ScanType> findAllByNameIsNotLike(final String name);

    // List<ScanType> findAllByNameMatchesRegex(final String regex);

    List<ScanType> findAllByMatchingPatternsContains(final String matchingPattern);
}
