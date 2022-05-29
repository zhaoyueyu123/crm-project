package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.mapper.TranMapper;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service("tranService")
public class TranServiceImpl implements TranService {

    @Resource
    private TranMapper tranMapper;
    @Override
    public List<Tran> queryTranAll() {
        return tranMapper.selectTranAll();
    }
}
