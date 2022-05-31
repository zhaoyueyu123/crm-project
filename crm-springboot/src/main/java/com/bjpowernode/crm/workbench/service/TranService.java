package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranService {
    List<Tran> queryTranAll();

    void saveCreateTran(Map<String,Object> map);
}
