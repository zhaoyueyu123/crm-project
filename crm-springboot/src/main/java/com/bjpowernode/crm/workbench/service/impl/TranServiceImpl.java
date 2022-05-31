package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.mapper.CustomerMapper;
import com.bjpowernode.crm.workbench.mapper.TranMapper;
import com.bjpowernode.crm.workbench.service.TranService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("tranService")
public class TranServiceImpl implements TranService {

    @Resource
    private TranMapper tranMapper;
    @Resource
    private CustomerMapper customerMapper;
    @Override
    public List<Tran> queryTranAll() {
        return tranMapper.selectTranAll();
    }

    @Override
    public void saveCreateTran(Map<String, Object> map) {
        User user=(User)map.get(Contants.SESSION_USER);
        String customerName=(String)map.get("customerName");
        //根据name精确查询客户
        Customer customer=customerMapper.selectCustomerByName(customerName);
        //如果客户不存在,则新建客户
        if(customer==null){
            customer = new Customer();
            customer.setOwner(user.getId());
            customer.setName(customerName);
            customer.setId(UUIDUtils.getUUID());
            customer.setCreateTime(DateUtils.formateDateTime(new Date()));
            customer.setCreateBy(user.getId());
            customerMapper.insertCustomer(customer);
        }
        //保存创建交易
        Tran tran=new Tran();
        tran.setStage((String)map.get("stage"));
        tran.setOwner((String)map.get("owner"));
        tran.setNextContactTime((String)map.get("nextContactTime"));
        tran.setName((String)map.get("name"));
        tran.setMoney((String)map.get("money"));
        tran.setId(UUIDUtils.getUUID());
        tran.setExpectedDate((String)map.get("expectedDate"));
        tran.setCustomerId(customer.getId());
        tran.setContactSummary((String)map.get("contactSummary"));
        tran.setCreateTime(DateUtils.formateDateTime(new Date()));
        tran.setCreateBy(user.getId());
        tran.setContactsId((String)map.get("contactsId"));
        tran.setActivityId((String)map.get("activityId"));
        tran.setDescription((String)map.get("description"));
        tran.setSource((String)map.get("source"));
        tran.setType((String)map.get("type"));
        tranMapper.insertTran(tran);
    }
}
