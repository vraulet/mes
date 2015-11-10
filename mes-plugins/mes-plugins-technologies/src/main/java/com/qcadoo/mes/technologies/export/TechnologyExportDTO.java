package com.qcadoo.mes.technologies.export;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

//@Getter
//@Setter
public class TechnologyExportDTO {

    private List<Map<String, Object>> technologies = new ArrayList<>();

    private List<Map<String, Object>> technologyOperationComponents = new ArrayList<>();

    private List<Map<String, Object>> operations = new ArrayList<>();

    private List<Map<String, Object>> products = new ArrayList<>();

    public List<Map<String, Object>> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<Map<String, Object>> technologies) {
        this.technologies = technologies;
    }

    public List<Map<String, Object>> getTechnologyOperationComponents() {
        return technologyOperationComponents;
    }

    public void setTechnologyOperationComponents(List<Map<String, Object>> technologyOperationComponents) {
        this.technologyOperationComponents = technologyOperationComponents;
    }

    public List<Map<String, Object>> getOperations() {
        return operations;
    }

    public void setOperations(List<Map<String, Object>> operations) {
        this.operations = operations;
    }

    public List<Map<String, Object>> getProducts() {
        return products;
    }

    public void setProducts(List<Map<String, Object>> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "TechnologyExportDTO{" + "technologies=" + technologies + ", technologyOperationComponents=" + technologyOperationComponents + ", operations=" + operations + ", products=" + products + '}';
    }

}
