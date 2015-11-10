/**
 * ***************************************************************************
 * Copyright (c) 2010 Qcadoo Limited Project: Qcadoo MES Version: 1.4
 * <p>
 * This file is part of Qcadoo.
 * <p>
 * Qcadoo is free software; you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA ***************************************************************************
 */
package com.qcadoo.mes.technologies.export;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TechnologyExporterRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    List<Map<String, Object>> getTechnologies(Long[] idsArray) {
        String ids = Arrays.asList(idsArray).stream().map(String::valueOf).collect(Collectors.joining(","));
        return jdbcTemplate
                .queryForList("SELECT * FROM technologies_technology WHERE id IN("+ids+")", ImmutableMap.of());
    }

    List<Map<String, Object>> getTechnologyOperationComponentByTechnology(Long technologyId) {
        return jdbcTemplate
                .queryForList("SELECT * FROM technologies_technologyoperationcomponent WHERE technology_id = :id",
                        ImmutableMap.of("id", technologyId));
    }

    List<Map<String, Object>> getOperationsByIds(List<Long> operationIds) {
        String ids = operationIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        return jdbcTemplate.queryForList("SELECT * FROM technologies_operation  WHERE id in("+ids+")", ImmutableMap.of());
    }

    Map<String, Object> getProductById(long productId) {
        return jdbcTemplate.queryForMap("SELECT * FROM basic_product WHERE id = :id", ImmutableMap.of("id", productId));
    }

}
