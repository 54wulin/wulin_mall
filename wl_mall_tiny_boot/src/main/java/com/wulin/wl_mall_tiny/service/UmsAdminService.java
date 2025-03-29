package com.wulin.wl_mall_tiny.service;

import com.wulin.wl_mall_tiny.domain.AdminUserDetails;
/*
* 后台用于管理service
* */
public interface UmsAdminService {

    AdminUserDetails getAdminByUsername(String username);

    String login(String usernname , String password);


}
