package com.lz.football_management.dao;

import com.lz.football_management.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    //通过账号密码来查询信息
    User findByUsername(String username);



}
