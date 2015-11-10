package com.qcadoo.mes.technologies.export;

import com.qcadoo.mes.basic.constants.BasicConstants;
import com.qcadoo.mes.basic.constants.ProductFields;
import com.qcadoo.mes.technologies.constants.TechnologiesConstants;
import com.qcadoo.mes.technologies.constants.TechnologyFields;
import com.qcadoo.model.api.DataDefinition;
import com.qcadoo.model.api.DataDefinitionService;
import com.qcadoo.model.api.Entity;
import com.qcadoo.model.api.FieldDefinition;
import com.qcadoo.model.api.search.SearchRestrictions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TechnologyImporterService {

    @Autowired
    DataDefinitionService dataDefinitionService;

//    @Autowired
//    private ExpressionService expressionService;
    private List<String> log;

    private Map<String, Entity> importedProducts;

    public void importTechnologies(String json) {
        TechnologyExportDTO technologiesExportDTO = fromJsonString(json);

        init();

        technologiesExportDTO = importProducts(technologiesExportDTO);

        for (Map<String, Object> technologyExportDTO : technologiesExportDTO.getTechnologies()) {
            technologiesExportDTO = tryImportTechnology(technologiesExportDTO, technologyExportDTO);
        }

        displayLog();
    }

    private TechnologyExportDTO fromJsonString(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enableDefaultTyping();
            objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

            return objectMapper.readValue(json, TechnologyExportDTO.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Entity importTechnologyOperationComponents(Entity technology, List<Map<String, Object>> technologyOperationComponentsMap) {
        List<Entity> technologyOperationComponents = new ArrayList<>();
        DataDefinition technologyOperationComponentDD = getTechnologyOperationComponentDD();

        for (Map<String, Object> technologyOperationComponentMap : technologyOperationComponentsMap) {
            technologyOperationComponents.add(convertToEntity(technologyOperationComponentDD, technologyOperationComponentMap));
        }

        technology.setField(TechnologyFields.OPERATION_COMPONENTS, technologyOperationComponents);
        return technology;
    }

    private DataDefinition getTechnologyDD() {
        return dataDefinitionService.get(TechnologiesConstants.PLUGIN_IDENTIFIER, TechnologiesConstants.MODEL_TECHNOLOGY);
    }

    private DataDefinition getTechnologyOperationComponentDD() {
        return dataDefinitionService.get(TechnologiesConstants.PLUGIN_IDENTIFIER, TechnologiesConstants.MODEL_TECHNOLOGY_OPERATION_COMPONENT);
    }

    private DataDefinition getProductDD() {
        return dataDefinitionService.get(BasicConstants.PLUGIN_IDENTIFIER, BasicConstants.MODEL_PRODUCT);
    }

    private Entity convertToEntity(DataDefinition dataDefinition, Map<String, Object> map) {
        Entity entity = dataDefinition.create();

        if (dataDefinition.isActivable()) {
            entity.setActive(Boolean.getBoolean(map.get("active").toString()));
        }

        for (Map.Entry<String, FieldDefinition> fieldDefinitionEntry : dataDefinition.getFields().entrySet()) {
            if (fieldDefinitionEntry.getValue().isPersistent()) {
                entity.setField(fieldDefinitionEntry.getKey(), map.get(fieldDefinitionEntry.getKey()));
            }
        }

//            for (Map.Entry<String, FieldDefinition> fieldDefinitionEntry : dataDefinition.getFields().entrySet()) {
//                if (fieldDefinitionEntry.getValue().getExpression() != null) {
//                    entity.setField(fieldDefinitionEntry.getKey(), expressionService.getValue(entity, fieldDefinitionEntry.getValue().getExpression(), Locale.ENGLISH));
//                }
//            }
        return entity;
    }

    private TechnologyExportDTO importProducts(TechnologyExportDTO technologyExportDTO) {
        for (Map<String, Object> productMap : technologyExportDTO.getProducts()) {
            tryImportProduct(productMap);
        }

        return technologyExportDTO;
    }

    private void log(Entity entity) {
        log.add(String.format("Import of %s. Result: %b. Number: %s", entity.getDataDefinition().getName(), entity.isValid(), entity.getField("number")));
        if (!entity.isValid()) {
            entity.getErrors().values().forEach(error -> {
                log.add(error.getMessage());
            });
            entity.getGlobalErrors().forEach(error -> {
                log.add(error.getMessage());
            });
        }
    }

    private void displayLog() {
        log.forEach(System.out::println);
    }

    private void init() {
        log = new ArrayList<>();
        importedProducts = new HashMap<>();
    }

    private TechnologyExportDTO tryImportTechnology(TechnologyExportDTO technologiesExportDTO, Map<String, Object> technologyExportDTO) {
        String technologyNumber = technologyExportDTO.get("number").toString();
        Entity technologyEntity = getTechnologyDD().find().add(SearchRestrictions.eq(TechnologyFields.NUMBER, technologyNumber)).setMaxResults(1).uniqueResult();
        if (technologyEntity == null) {
            technologiesExportDTO = importTechnology(technologiesExportDTO, technologyExportDTO);
        } else {
            log.add(String.format("Technology: '%s' already exists.", technologyNumber));
        }

        return technologiesExportDTO;
    }

    private TechnologyExportDTO importTechnology(TechnologyExportDTO technologiesExportDTO, Map<String, Object> technologyExportDTO) {
        DataDefinition technologyDD = getTechnologyDD();
        Entity technology = convertToEntity(technologyDD, technologyExportDTO);
        technology = importTechnologyOperationComponents(technology, technologiesExportDTO.getTechnologyOperationComponents());
        technology.setField(TechnologyFields.PRODUCT, importedProducts.get(technologyExportDTO.get("product_id").toString()));
        technology = technologyDD.save(technology);
        log(technology);

        return technologiesExportDTO;
    }

    private void tryImportProduct(Map<String, Object> productMap) {
        String productNumber = productMap.get("number").toString();
        Entity productEntity = getProductDD().find().add(SearchRestrictions.eq(ProductFields.NUMBER, productNumber)).setMaxResults(1).uniqueResult();
        if (productEntity == null) {
            importProduct(productMap);
        } else {
            log.add(String.format("Product: '%s' already exists.", productNumber));
        }
    }

    private void importProduct(Map<String, Object> productMap) {
        DataDefinition productDD = getProductDD();
        Entity product = convertToEntity(productDD, productMap);
        product = productDD.save(product);
        importedProducts.put(product.getStringField(ProductFields.NUMBER), product);
        log(product);
    }
}
