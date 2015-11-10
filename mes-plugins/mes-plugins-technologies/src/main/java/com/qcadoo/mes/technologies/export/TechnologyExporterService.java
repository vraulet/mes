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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TechnologyExporterService {

    @Autowired
    TechnologyExporterRepository technologyExporterRepository;

    public String exportTechnologies(Long... ids) {
        if (ids == null || ids.length == 0) {
            // TODO
            return null;
        }

        List<Map<String, Object>> technologies = technologyExporterRepository.getTechnologies(ids);

        TechnologyExportDTO technologyExportDTO = new TechnologyExportDTO();
        technologies.forEach(technology -> {
            exportTechnology(technologyExportDTO, technology);
        });

        System.out.println(toJsonString(technologyExportDTO));

        return toJsonString(technologyExportDTO);
    }

    private String toJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enableDefaultTyping();
            objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

            return objectMapper.writeValueAsString(obj);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private TechnologyExportDTO exportTechnology(TechnologyExportDTO technologyExportDTO, Map<String, Object> technology) {
        technologyExportDTO.getTechnologies().add(technology);

        technologyExportDTO = exportTechnologyOperationComponent(technologyExportDTO, Long.parseLong(technology.get("id").toString()));
        
        technologyExportDTO = exportProduct(technologyExportDTO, technology);

        return technologyExportDTO;
    }

    private TechnologyExportDTO exportTechnologyOperationComponent(TechnologyExportDTO technologyExportDTO, Long technologyId) {
        List<Map<String, Object>> technologyOperationComponents = technologyExporterRepository.getTechnologyOperationComponentByTechnology(technologyId);

        technologyExportDTO.getTechnologyOperationComponents().addAll(technologyOperationComponents);

        List<Long> operationIds = technologyOperationComponents.stream().map(technologyOperationComponent -> {
            return Long.parseLong(technologyOperationComponent.get("operation_id").toString());
        }).collect(Collectors.toList());

        technologyExportDTO = exportOperations(technologyExportDTO, operationIds);

        return technologyExportDTO;
    }

    // co na wejściu? może plik? stream? string?
    public void importEchnologies(Object technologyDTO) {

    }

    private TechnologyExportDTO exportOperations(TechnologyExportDTO technologyExportDTO, List<Long> operationIds) {
        List<Map<String, Object>> operations = technologyExporterRepository.getOperationsByIds(operationIds);

        technologyExportDTO.setOperations(operations);

        return technologyExportDTO;
    }

    private TechnologyExportDTO exportProduct(TechnologyExportDTO technologyExportDTO, Map<String, Object> technology) {
        Long productId = Long.parseLong(technology.get("product_id").toString());
        Map<String, Object> product = technologyExporterRepository.getProductById(productId);        
        technology.put("product_id", product.get("number"));
        
        technologyExportDTO.getProducts().add(product);
        
        return technologyExportDTO;
    }

}
