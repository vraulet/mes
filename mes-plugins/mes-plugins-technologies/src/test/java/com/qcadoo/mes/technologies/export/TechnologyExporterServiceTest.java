package com.qcadoo.mes.technologies.export;

import com.qcadoo.model.api.DataDefinition;
import com.qcadoo.model.api.DataDefinitionService;
import com.qcadoo.model.internal.DefaultEntity;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class TechnologyExporterServiceTest {

    private TechnologyExporterService technologyExporterService;

    private TechnologyImporterService technologyImporterService;

    @Before
    public void init() {
//        MockitoAnnotations.initMocks(this);
        technologyExporterService = new TechnologyExporterService();

        TechnologyExporterRepository technologyExporterRepository = Mockito.mock(TechnologyExporterRepository.class);;
        technologyExporterService.technologyExporterRepository = technologyExporterRepository;

        when(technologyExporterRepository.getTechnologies(any())).thenReturn(getTechnologies());
        when(technologyExporterRepository.getTechnologyOperationComponentByTechnology(any())).thenReturn(getTechnologyOperationComponents());
        when(technologyExporterRepository.getOperationsByIds(any())).thenReturn(getOperations());
        when(technologyExporterRepository.getProductById(anyLong())).thenReturn(getProduct());

        // --
        technologyImporterService = new TechnologyImporterService();
        DataDefinition dataDefinition = Mockito.mock(DataDefinition.class);
        DataDefinitionService dataDefinitionService = Mockito.mock(DataDefinitionService.class);
        when(dataDefinitionService.get(anyString(), anyString())).thenReturn(dataDefinition);
        technologyImporterService.dataDefinitionService = dataDefinitionService;

        DefaultEntity defaultEntity = new DefaultEntity(dataDefinition);
        when(dataDefinition.create()).thenReturn(defaultEntity);

//        EntityService entityService = mock(EntityService.class);
//        technologyImporterService.entityService = entityService;
    }

    @Test
    public void xTest() {

        String json = technologyExporterService.exportTechnologies(327L);

        technologyImporterService.importTechnologies(json);

    }

    private List<Map<String, Object>> getTechnologies() {
        String values = "327;\"412002-002\";\"Tech. WAFLE ŚREDNIE MULTIGRAIN 108G SANUTRI FRANCE (412002) - 2013.8\";27;;t;f;\"Nowy film ID 215020.\";\"02accepted\";\"\";\"\";\"\";\"412002 PF\";;\"\";;f;f;t;\"\";;;;;;;;\"\";;;\"\";;;;;;\"03forEach\";f;f;f;t;f;f;t;t;0";
        String keys = "\"id;number;name;product_id;technologygroup_id;externalsynchronized;master;description;state;recipeimportstatus;recipeimportmsg;qualitycontrolinstruction;formula;minimalquantity;qualitycontroltype;unitsamplingnr;active;technologybatchrequired;isstandardgoodfoodtechnology;range;division_id;componentslocation_id;componentsoutputlocation_id;productsinputlocation_id;isdivisionlocation;isdivisioninputlocation;isdivisionoutputlocation;technologytype;technologyprototype_id;productionline_id;productionflow;productsflowlocation_id;automaticmoveforintermediate;automaticmoveforfinal;graphicsaccepted;constructionandtechnologyaccepted;typeofproductionrecording;justone;generateproductionrecordnumberfromordernumber;allowtoclose;registerquantityoutproduct;autocloseorder;registerpiecework;registerquantityinproduct;registerproductiontime;entityversion\"";
        Map<String, Object> technology = parseObject(keys, values);

        return Arrays.asList(technology);
    }

    /*
     SELECT string_agg(column_name, ';') FROM information_schema.columns WHERE table_schema = 'public' AND table_name   = 'technologies_technologyoperationcomponent'
     */
    private Map<String, Object> parseObject(String keysString, String valuesString) {
        Map<String, Object> map = new HashMap<>();

        String[] keys = keysString.split(";");
        String[] values = valuesString.split(";");

        for (int i = 0; i < keys.length; i++) {
            map.put(trim(keys[i]), trim(values[i]));
        }

        return map;
    }

    private String trim(String txt) {
        if (txt.startsWith("\"")) {
            txt = txt.substring(1);
        }
        if (txt.endsWith("\"")) {
            txt = txt.substring(0, txt.length() - 1);
        }

        return txt;
    }

    private List<Map<String, Object>> getTechnologyOperationComponents() {
        String values = "1758;327;95;;\"operation\";1;\"1.\";;\"\";\"\";f;f;60;f;;\"szt\";\"\";f;\"\";0.00000;\"01all\";f;1.00000;0;f;0.00000;;4.00000;1.00000;;1;f;f;60;0.00000;21.56000;f;\"01workstations\";;1;\"2015-03-09 12:05:58.138\";\"2015-03-31 09:21:43.621\";\"aantolak\";\"kwierzchucka\";1759;;2;f;0.00000;;;0";
        String keys = "\"id;technology_id;operation_id;parent_id;entitytype;priority;nodenumber;referencetechnology_id;comment;attachment;areproductquantitiesdivisible;istjdivisible;tpz;hidedescriptioninworkplans;laborworktime;productioninonecycleunit;nextoperationafterproducedquantityunit;hidetechnologyandorderinworkplans;imageurlinworkplan;nextoperationafterproducedquantity;nextoperationafterproducedtype;hidedetailsinworkplans;machineutilization;timenextoperation;dontprintinputproductsinworkplans;pieceworkcost;machineworktime;productioninonecycle;laborutilization;duration;numberofoperations;qualitycontrolrequired;dontprintoutputproductsinworkplans;tj;machinehourlycost;laborhourlycost;issubcontracting;assignedtooperation;workstationtype_id;quantityofworkstations;createdate;updatedate;createuser;updateuser;techopercomptimecalculation_id;hascorrections;division_id;showinproductdata;productdatanumber;productionlinechange;productionline_id;entityversion\"";

        Map<String, Object> technologyOperationComponent = parseObject(keys, values);

        return Arrays.asList(technologyOperationComponent);
    }

    private List<Map<String, Object>> getOperations() {
        String values = "95;\"SK-CI-GÓ-WZ\";\"SKIS CIĘCIE GÓRNYCH WZMOCNIEŃ / REINFORCEMENT CUTTING\";\"\";;\"\";f;f;;\"\";f;0.00000;2.00000;f;21.56000;1;1.00000;\"01all\";f;\"\";0.00000;60;0.00000;1.00000;0;f;f;0;\"szt\";t;f;\"01workstations\";1;2;f;0.00000;;0";
        String keys = "\"id;number;name;comment;workstationtype_id;attachment;areproductquantitiesdivisible;istjdivisible;operationgroup_id;imageurlinworkplan;hidedescriptioninworkplans;pieceworkcost;productioninonecycle;hidedetailsinworkplans;laborhourlycost;numberofoperations;laborutilization;nextoperationafterproducedtype;dontprintoutputproductsinworkplans;nextoperationafterproducedquantityunit;machinehourlycost;tj;nextoperationafterproducedquantity;machineutilization;timenextoperation;dontprintinputproductsinworkplans;hidetechnologyandorderinworkplans;tpz;productioninonecycleunit;active;issubcontracting;assignedtooperation;quantityofworkstations;division_id;showinproductdata;productdatanumber;productionline_id;entityversion\"";

        Map<String, Object> operation = parseObject(keys, values);

        return Arrays.asList(operation);
    }

    private Map<String, Object> getProduct() {
        String values = "27;\"412002\";\"WAFLE ŚREDNIE MULTIGRAIN 108G SANUTRI FRANCE\";\"01component\";\"3175681026827\";\"\";\"szt.\";\"432\";\"\";1042;\"31.1\";\"01particularProduct\";12;;1.00000;0.00000;;t;\"SRMG.12.SFGE-15W\";0.00000;\"\";0.86000;\"Konwencjonalne\";t;;t;\"2013-08-22 12:06:01\";\"2015-06-10 16:15:08.262\";\"admin\";\"admin\";1;\"\";\"\";f;;\"\";f;\"\";\"\";\"\";\"\";\"\";;f;f;0.10800;f;15;f;;;;;;;;;\"\";\"\";f;;;0;;\"\"";
        String keys = "\"id;number;name;globaltypeofmaterial;ean;category;unit;externalnumber;description;parent_id;nodenumber;entitytype;durabilityinmonths;averageoffercost;costfornumber;lastpurchasecost;lastoffercost;isglutenproduct;symbol;averagecost;goodsgroup;nominalcost;bio;isdoublepallet;technologygroup_id;active;createdate;updatedate;createuser;updateuser;quantityofextrusioningredient;norm;actualversion;hasnutritionelements;quantityfornutritions;quantityfornutritionsunit;doublequantityfordoublepallet;size;uppershelf;lowershelf;upperform;lowerform;showinproductdata;usedquantitycontrol;automaticusedquantity;nominalweight;countusedquantityforfullpallets;quantityinpackage;synchronize;capacitynorm;downform_id;upform_id;downshelve_id;upshelve_id;costnormsgenerator_id;producer_id;machinepart;drawingnumber;catalognumber;isproductiondate;fabric;fabricgrammage;entityversion;ispallet;additionalunit\"";

        Map<String, Object> product = parseObject(keys, values);

        return product;
    }
}
