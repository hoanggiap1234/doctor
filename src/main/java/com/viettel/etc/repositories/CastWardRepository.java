package com.viettel.etc.repositories;

public interface CastWardRepository {
    Object getAddress(String provinceCode, String districtCode, String wardCode);
}
