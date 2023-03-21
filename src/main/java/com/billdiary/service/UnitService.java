package com.billdiary.service;


import com.billdiary.config.AppConfig;
import com.billdiary.config.MessageConfig;
import com.billdiary.constant.ErrorConstants;
import com.billdiary.dao.UnitRepository;
import com.billdiary.entity.Unit;
import com.billdiary.exception.DatabaseException;
import com.billdiary.service.utility.NullAwareBeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnitService {

    final static Logger logger = LoggerFactory.getLogger(UnitService.class);

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    MessageConfig messageConfig;

    @Autowired
    AppConfig appConfig;



    public List<Unit> getUnits(int index, int rowsPerPage) {
        logger.info("Entering in getUnits");
        Page<Unit> UnitEntities = unitRepository.findAll(PageRequest.of(index, rowsPerPage));
        logger.info("Exiting in getUnits");
        return UnitEntities.toList();
    }

    public Unit getUnitById(Long unitId) throws DatabaseException {

        Unit unit = null;
        Optional<Unit> unitEntity=unitRepository.findById(unitId);
        if(unitEntity.isPresent()){
            unit = unitEntity.get();
        }
        else{
            throw new DatabaseException(ErrorConstants.Err_Code_507, messageConfig.getMessage(ErrorConstants.Err_Code_507, unitId));
        }
        return unit;
    }

    public List<Unit> saveUnits(List<Unit> units) {
        logger.info("Saving the units {}", units);
        Long maxId = getMaxUnitId();
        units.forEach(unit -> {
            unit.setUnitId(maxId+1);
        });
        List<Unit>  updatedCustEntities = unitRepository.saveAll(units);
        return updatedCustEntities;
    }

    private Long getMaxUnitId(){
        Long maxId = unitRepository.maxUnitId();
        if(maxId != null){
            return maxId;
        }else{
            return appConfig.getStartingId();
        }
    }

    public Unit updateUnit(Unit unit) throws DatabaseException {

        logger.info("Entering in updateunit");
        Unit updateUnit = null;
        Optional<Unit> existingUnitOptional = unitRepository.findById(unit.getUnitId());
        if(existingUnitOptional.isPresent()){
            updateUnit = existingUnitOptional.get();
            NullAwareBeanUtils.copyNonNullProperties(unit, updateUnit);
            unitRepository.save(updateUnit);
        }else{
            throw new DatabaseException(ErrorConstants.Err_Code_507, messageConfig.getMessage(ErrorConstants.Err_Code_507, unit.getUnitId()));
        }
        return updateUnit;
    }

    public boolean deleteUnit(Long unitId) throws DatabaseException {

        if(isUnitAlreadyExists(unitId)){
            logger.info("Deleting the unit {}", unitId);
            unitRepository.deleteById(unitId);
        }
        return true;
    }

    private boolean isUnitAlreadyExists(Long unitId) throws DatabaseException {
        Optional<Unit> unitEntity=unitRepository.findById(unitId);
        if(unitEntity.isPresent()){
            return true;
        }
        else{
            throw new DatabaseException(ErrorConstants.Err_Code_507, messageConfig.getMessage(ErrorConstants.Err_Code_507,unitId));
        }
    }
}
