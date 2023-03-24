package com.billdiary.mapper;


import com.billdiary.dto.UnitDto;
import com.billdiary.entity.Unit;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UnitMapper {

    public abstract Unit unitDtoToUnit(UnitDto unitDto);

    public abstract UnitDto unitToUnitDto(Unit unit);

    public abstract List<Unit> unitDtoToUnit(List<UnitDto> unitDtos);

    public abstract List<UnitDto> unitToUnitDto(List<Unit> units);
}
