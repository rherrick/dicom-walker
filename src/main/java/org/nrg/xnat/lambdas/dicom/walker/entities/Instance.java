/*
 * dicom-walker_main: org.nrg.xnat.lambdas.dicom.walker.entities.Instance
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
import java.net.URI;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@Cacheable
@Table(indexes = {@Index(columnList = "attributes"), @Index(columnList = "storageUri")})
public class Instance extends BaseEntity {
    @Override
    public String toString() {
        return // getScan() != null ?
                // getScan().toString() + " " + getInstanceNumber() + " (" + getStorageUri() + ")" :
                "Instance " + getInstanceNumber() + " (" + getStorageUri() + ")";
    }

    @ManyToOne
    private Scan scan;

    @NotNull
    private int instanceNumber;

    @NotNull
    @NaturalId
    @Column(nullable = false, unique = true)
    private URI storageUri;

    // @Type(type = "com.marvinformatics.hibernate.json.JsonUserType")
    // private JsonNode attributes;
    private String attributes;
}
