package com.viettel.etc.repositories.impl;

import com.viettel.etc.dto.CastWardDto;
import com.viettel.etc.repositories.CastWardRepository;
import com.viettel.etc.utils.Constants;
import com.viettel.etc.xlibrary.core.repositories.CommonDataBaseRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class CastWardRepositoryImpl extends CommonDataBaseRepository implements CastWardRepository {
    @Override
    public Object getAddress(String provinceCode, String districtCode, String wardCode) {
        if(provinceCode == null){
            return null;
        }
        HashMap<String, Object> hmapParams = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT w.ward_code as wardCode, w.name as wardName, " +
                "d.district_code as districtCode, d.name as districtName, " +
                "p.province_code as provinceCode, p.name as provinceName " +
                "FROM cats_provinces p " +
                "LEFT JOIN (" +
                "SELECT district_code, name, province_code FROM cats_districts " );
        if(districtCode==null){
            sql.append("WHERE 1<>1 ");
        }
        sql.append(") d ON d.province_code=p.province_code " +
                "LEFT JOIN (" +
                "select ward_code, name, district_code from cats_wards " );
        if(wardCode==null){
            sql.append("WHERE 1<>1 ");
        }
        sql.append(") w ON w.district_code=d.district_code " +
                "WHERE p.province_code=:provinceCode ");
        hmapParams.put("provinceCode",provinceCode);
        if(districtCode!=null){
            sql.append("AND d.district_code=:districtCode ");
            hmapParams.put("districtCode",districtCode);
        }
        if(wardCode!=null){
            sql.append("AND w.ward_code=:wardCode ");
            hmapParams.put("wardCode",wardCode);
        }

        return getFirstData(sql, hmapParams, CastWardDto.class);
    }
}
