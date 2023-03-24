package com.billdiary.service;


import com.billdiary.config.AppConfig;
import com.billdiary.config.MessageConfig;
import com.billdiary.constant.ErrorConstants;
import com.billdiary.dao.UnitRepository;
import com.billdiary.dto.UnitDto;
import com.billdiary.entity.Unit;
import com.billdiary.exception.DatabaseException;
import com.billdiary.mapper.UnitMapper;
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
    UnitMapper unitMapper;

    @Autowired
    MessageConfig messageConfig;

    @Autowired
    AppConfig appConfig;



    public List<UnitDto> getUnits(int index, int rowsPerPage) {
        logger.info("Entering in getUnits");
        Page<Unit> UnitEntities = unitRepository.findAll(PageRequest.of(index, rowsPerPage));
        logger.info("Exiting in getUnits");
        return unitMapper.unitToUnitDto(UnitEntities.toList());
    }

    public UnitDto getUnitById(Long unitId) throws DatabaseException {

        Unit unit = null;
        UnitDto unitDto = null;
        Optional<Unit> unitEntity=unitRepository.findById(unitId);
        if(unitEntity.isPresent()){
            unit = unitEntity.get();
            unitDto = unitMapper.unitToUnitDto(unit);
        }
        else{
            throw new DatabaseException(ErrorConstants.Err_Code_507, messageConfig.getMessage(ErrorConstants.Err_Code_507, unitId));
        }
        return unitDto;
    }

    public List<UnitDto> saveUnits(List<UnitDto> unitDtos) throws DatabaseException {
        logger.info("Saving the units {}", unitDtos);
        List<Unit>  updatedCustEntities = null;
        List<UnitDto>  updatedCustDtos = null;
        try{
            List<Unit> units = unitMapper.unitDtoToUnit(unitDtos);
            Long maxId = getMaxUnitId();
            units.forEach(unit -> {
                unit.setUnitId(maxId+1);
            });
            updatedCustEntities = unitRepository.saveAll(units);
            updatedCustDtos = unitMapper.unitToUnitDto(updatedCustEntities);
        }catch (Exception e){
            throw new DatabaseException(e.getMessage());
        }

        return updatedCustDtos;
    }

    private Long getMaxUnitId(){
        Long maxId = unitRepository.maxUnitId();
        if(maxId != null){
            return maxId;
        }else{
            return appConfig.getStartingId();
        }
    }

    public UnitDto updateUnit(UnitDto unitDto) throws DatabaseException {

        logger.info("Entering in updateunit");
        Unit updateUnit = null;
        UnitDto updateUnitDto = null;
        Unit unit = unitMapper.unitDtoToUnit(unitDto);
        Optional<Unit> existingUnitOptional = unitRepository.findById(unit.getUnitId());
        if(existingUnitOptional.isPresent()){
            updateUnit = existingUnitOptional.get();
            NullAwareBeanUtils.copyNonNullProperties(unit, updateUnit);
            updateUnit = unitRepository.save(updateUnit);
            updateUnitDto = unitMapper.unitToUnitDto(updateUnit);
        }else{
            throw new DatabaseException(ErrorConstants.Err_Code_507, messageConfig.getMessage(ErrorConstants.Err_Code_507, unit.getUnitId()));
        }
        return updateUnitDto;
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
