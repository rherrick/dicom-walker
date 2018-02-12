/*
 * dicom-walker_main: org.nrg.xnat.lambdas.dicom.walker.entities.ScanType
 * XNAT http://www.xnat.org
 * Copyright (c) 2005-2018, Washington University School of Medicine and Howard Hughes Medical Institute
 * All Rights Reserved
 *
 *  Released under the Simplified BSD.
 */

package org.nrg.xnat.lambdas.dicom.walker.entities;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@Cacheable
public class ScanType extends BaseEntity {
    @Override
    public String toString() {
        return "Scan type " + getName() + " (" + StringUtils.defaultIfBlank(getDescription(), "") + ")";
    }

    @NotNull
    @NaturalId
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull
    @ElementCollection
    private List<String> matchingPatterns;

    private String description;
}
