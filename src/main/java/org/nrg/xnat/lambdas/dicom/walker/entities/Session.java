/*
 * dicom-walker_main: org.nrg.xnat.lambdas.dicom.walker.entities.Session
 * XNAT http://www.xnat.org
 * Copyright (c) 2005-2018, Washington University School of Medicine and Howard Hughes Medical Institute
 * All Rights Reserved
 *
 *  Released under the Simplified BSD.
 */

package org.nrg.xnat.lambdas.dicom.walker.entities;

import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@Cacheable
@Table(indexes = {@Index(name = "studyId", columnList = "studyId"),
                  @Index(name = "patient", columnList = "patientId, patientName")})
public class Session extends BaseEntity {
    @Override
    public String toString() {
        return "Session " + studyId + " (" + studyInstanceUid + ")";
    }

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Singular
    private List<Scan> scans;

    @NotNull
    @NaturalId
    @Column(nullable = false, unique = true)
    private String studyInstanceUid;

    @NotNull
    @NaturalId
    @Column(nullable = false, unique = true)
    private String accessionNumber;

    @NotNull
    @Column(nullable = false)
    private String studyId;

    private String description;

    private String patientId;

    private String patientName;
}
