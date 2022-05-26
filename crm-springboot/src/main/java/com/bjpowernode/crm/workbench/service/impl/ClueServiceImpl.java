package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.mapper.*;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("clueService")
public class ClueServiceImpl implements ClueService {

    @Resource
    private ClueMapper clueMapper;
    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private ContactsMapper contactsMapper;
    @Resource
    private ClueRemarkMapper clueRemarkMapper;
    @Resource
    private CustomerRemarkMapper customerRemarkMapper;
    @Override
    public int saveCreateClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public List<Clue> queryClueByConditionForPage(Map<String, Object> map) {
        return clueMapper.selectClueByConditionForPage(map);
    }

    @Override
    public int queryCountOfClueByCondition(Map<String, Object> map) {
        return clueMapper.selectCountOfClueByCondition(map);
    }

    @Override
    public Clue queryClueForDetailById(String id) {
        return clueMapper.selectClueForDetailById(id);
    }

    @Override
    public void saveConvert(Map<String, Object> map) {
        //根据id查询线索信息
        User user=(User) map.get(Contants.SESSION_USER);
        String clueId = (String)map.get("clueId");
        Clue clue=clueMapper.selectClueById(clueId);
        //把该线索中有关公司的信息转换到客户表中
        Customer customer=new Customer();
        customer.setAddress(clue.getAddress());
        customer.setContactSummary(clue.getContactSummary());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateUtils.formateDateTime(new Date()));
        customer.setDescription(clue.getDescription());
        customer.setId(UUIDUtils.getUUID());
        customer.setName(clue.getCompany());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setOwner(user.getId());
        customer.setPhone(clue.getPhone());
        customer.setWebsite(clue.getWebsite());
        customerMapper.insertCustomer(customer);
        //把该线索中有关个人的信息转换到联系人表中
        Contacts contacts = new Contacts();
        contacts.setAddress(clue.getAddress());
        contacts.setAppellation(clue.getAppellation());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DateUtils.formateDateTime(new Date()));
        contacts.setCustomerId(customer.getId());
        contacts.setDescription(clue.getDescription());
        contacts.setEmail(clue.getEmail());
        contacts.setFullname(clue.getFullname());
        contacts.setId(UUIDUtils.getUUID());
        contacts.setJob(clue.getJob());
        contacts.setMphone(clue.getMphone());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setOwner(user.getId());
        contacts.setSource(clue.getSource());
        contactsMapper.insertContacts(contacts);
        //根据clueId查询该线索下的所有备注
        List<ClueRemark> clueRemarksList = clueRemarkMapper.selectClueRemarkByClueId(clueId);
        //如果该线索下有备注，把该线索下的所有备注转换到联系人备注表中一份
        if(clueRemarksList!=null&&clueRemarksList.size()>0){
            //遍历clueRemarksList，封装客户备注
            CustomerRemark customerRemark=null;
            List<CustomerRemark> customerRemarkList=new ArrayList<>();
            for(ClueRemark cr:clueRemarksList){
                customerRemark = new CustomerRemark();
                customerRemark.setCreateBy(cr.getCreateBy());
                customerRemark.setCreateTime(cr.getCreateTime());
                customerRemark.setCustomerId(customer.getId());
                customerRemark.setEditBy(cr.getEditBy());
                customerRemark.setEditFlag(cr.getEditFlag());
                customerRemark.setId(UUIDUtils.getUUID());
                customerRemark.setNoteContent(cr.getNoteContent());
                customerRemarkList.add(customerRemark);
            }
            customerRemarkMapper.insertCustomerRemarkByList(customerRemarkList);

        }
    }
}
