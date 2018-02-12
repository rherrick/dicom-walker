/*
 * dicom-walker_main: org.nrg.xnat.lambdas.dicom.walker.repositories.Instances
 * XNAT http://www.xnat.org
 * Copyright (c) 2005-2018, Washington University School of Medicine and Howard Hughes Medical Institute
 * All Rights Reserved
 *
 *  Released under the Simplified BSD.
 */

package org.nrg.xnat.lambdas.dicom.walker.repositories;

import org.nrg.xnat.lambdas.dicom.walker.entities.Instance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.net.URI;

public interface Instances extends JpaRepository<Instance, String> {
    // Instance findByScanAndInstanceNumber(final Scan scan, final int instanceNumber);

    Instance findByStorageUri(final URI storageUri);

    // List<Instance> findAllByScan(final Scan scan);
}
