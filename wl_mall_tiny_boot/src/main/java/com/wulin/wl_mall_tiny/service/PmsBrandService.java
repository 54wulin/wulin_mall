package com.wulin.wl_mall_tiny.service;



import com.wulin.wl_mall_tiny.mbg.model.PmsBrand;

import java.util.List;

public interface PmsBrandService {
    List<PmsBrand> listAllBrand();////

    int createBrand(PmsBrand brand);

    int opdateBrand(Long id, PmsBrand brand);

    int deleteBrand(Long id);

    List<PmsBrand> listBrand(int pageNum, int pageSize);

    PmsBrand getBrand(Long id);
}
