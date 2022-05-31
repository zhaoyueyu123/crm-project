package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.mapper.CustomerMapper;
import com.bjpowernode.crm.workbench.service.CustomerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerMapper customerMapper;
    @Override
    public List<String> queryAllCustomerNameByName(String customerName) {
        return customerMapper.selectAllCustomerNameByName(customerName);
    }
}
