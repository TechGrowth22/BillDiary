package com.billdiary.dao;

import com.billdiary.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

    public List<Unit> findByUnitIdOrUnitName(Long unitId, String unitName);

    public List<Unit> findByUnitNameIgnoreCase(String unitName);
    @Query("SELECT MAX(unit.unitId) FROM Unit unit")
    Long maxUnitId();
}
