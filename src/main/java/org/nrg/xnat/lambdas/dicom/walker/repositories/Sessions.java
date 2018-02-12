/*
 * dicom-walker_main: org.nrg.xnat.lambdas.dicom.walker.repositories.Sessions
 * XNAT http://www.xnat.org
 * Copyright (c) 2005-2018, Washington University School of Medicine and Howard Hughes Medical Institute
 * All Rights Reserved
 *
 *  Released under the Simplified BSD.
 */

package org.nrg.xnat.lambdas.dicom.walker.repositories;

import org.nrg.xnat.lambdas.dicom.walker.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Sessions extends JpaRepository<Session, String> {
    Session findByStudyInstanceUid(final String studyInstanceUid);

    Session findByAccessionNumber(final String accessionNumber);

    List<Session> findAllByPatientId(final String patientId);

    List<Session> findAllByPatientName(final String patientName);

    List<Session> findAllByPatientNameIsLike(final String patientName);

    List<Session> findAllByPatientIdAndPatientName(final String patientId, final String patientName);

    List<Session> findAllByStudyId(final String description);

    List<Session> findAllByStudyIdIsLike(final String description);

    List<Session> findAllByStudyIdIsNotLike(final String description);

    List<Session> findAllByDescription(final String description);

    List<Session> findAllByDescriptionIsLike(final String description);

    List<Session> findAllByDescriptionIsNotLike(final String description);
}
