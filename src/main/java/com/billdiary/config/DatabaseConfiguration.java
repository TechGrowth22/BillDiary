package com.billdiary.config;


import com.billdiary.dao.UnitRepository;
import com.billdiary.entity.Unit;
import com.billdiary.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DatabaseConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfiguration.class);

    @Autowired
    UnitRepository unitRepository;


    @Value("#{${units}}")
    private Map<Long,String> units;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("Starting Initialization events");
        createUnits();

    }

    private void createUnits() {
        try{
            List<Unit> unitList = new ArrayList<>();
            for(Map.Entry<Long,String> entry: units.entrySet()){
                Unit unit = new Unit();
                unit.setUnitId(entry.getKey());
                unit.setUnitName(entry.getValue());
                unitList.add(unit);
            }
            unitRepository.saveAll(unitList);
        }catch (Exception e){
            logger.error("Units initialization failed", e);
        }

    }

}
