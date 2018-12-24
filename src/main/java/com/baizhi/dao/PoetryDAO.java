package com.baizhi.dao;

import com.baizhi.entity.Poetry;

import java.util.List;

public interface PoetryDAO {

    //查询全部
    public List<Poetry> findAll();
    //查询总条数
    public int count();
}
