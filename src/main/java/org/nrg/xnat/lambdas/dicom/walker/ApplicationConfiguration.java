/*
 * dicom-walker_main: org.nrg.xnat.lambdas.dicom.walker.ApplicationConfiguration
 * XNAT http://www.xnat.org
 * Copyright (c) 2005-2018, Washington University School of Medicine and Howard Hughes Medical Institute
 * All Rights Reserved
 *
 *  Released under the Simplified BSD.
 */

package org.nrg.xnat.lambdas.dicom.walker;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@Slf4j
public class ApplicationConfiguration {
    @Bean
    public HikariDataSource dataSource() {
        final HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(Driver.class.getName());
        return dataSource;
    }
}
