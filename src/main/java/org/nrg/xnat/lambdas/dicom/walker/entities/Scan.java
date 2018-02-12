/*
 * dicom-walker_main: org.nrg.xnat.lambdas.dicom.walker.entities.Scan
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
@Table(indexes = {@Index(name = "modality", columnList = "modality"),
                  @Index(name = "sopClassUid", columnList = "sopClassUid")})
public class Scan extends BaseEntity {
    @Override
    public String toString() {
        return // getSession() != null ?
               // getSession().toString() + " scan " + getScanNumber() + " (" + description + ")" :
               "Scan " + getScanNumber() + " (" + description + ")";
    }

    @ManyToOne
    private Session session;

    @OneToMany(mappedBy = "scan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Singular
    private List<Instance> instances;

    @NotNull
    private int scanNumber;

    @NotNull
    @NaturalId
    @Column(unique = true)
    private String seriesInstanceUid;

    @ManyToOne
    private ScanType scanType;

    @NotNull
    private String description;

    @NotNull
    private String modality;

    @NotNull
    private String sopClassUid;
}
