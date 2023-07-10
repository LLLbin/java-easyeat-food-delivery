package com.lllebin.service;

import com.lllebin.domain.AddressBook;
import com.lllebin.domain.AddressBookExample;
import com.lllebin.mapper.AddressBookMapper;
import com.lllebin.utils.BaseContextUtils;
import com.lllebin.utils.SnowflakeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: AddressBookService
 * Package: com.lllebin.service
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Service
public class AddressBookService {

    @Autowired
    private SnowflakeUtils snowflakeUtils;

    @Autowired
    private AddressBookMapper addressBookMapper;


    public void save(AddressBook addressBook) {
        addressBook.setId(snowflakeUtils.nextId());
        addressBookMapper.insertSelective(addressBook);
    }

    public void updateDefaultById(AddressBook addressBook) {
        // 1. 把用户所有地址 is_default 设为0
        // SQL:update address_book set is_default = 0 where user_id = ?
        AddressBook tmp = new AddressBook();
        tmp.setIsDefault((byte) 0);
        tmp.setUserId(BaseContextUtils.getCurrentId());
        AddressBookExample addressBookExample = new AddressBookExample();
        AddressBookExample.Criteria criteria = addressBookExample.createCriteria();
        criteria.andUserIdEqualTo(tmp.getUserId());
        addressBookMapper.updateByExampleSelective(tmp, addressBookExample);

        // 2. 指定地址 is_default 设为1
        // SQL:update address_book set is_default = 1 where id = ?
        addressBook.setIsDefault((byte) 1);
        addressBookMapper.updateByPrimaryKeySelective(addressBook);
    }

    public AddressBook getById(Long id) {
        return addressBookMapper.selectByPrimaryKey(id);
    }

    public AddressBook getDefault() {
        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBookExample addressBookExample = new AddressBookExample();
        AddressBookExample.Criteria criteria = addressBookExample.createCriteria();
        criteria.andUserIdEqualTo(BaseContextUtils.getCurrentId())
                .andIsDefaultEqualTo((byte) 1);
        List<AddressBook> addressBookList = addressBookMapper.selectByExample(addressBookExample);
        if (addressBookList.size() == 0) {
            return null;
        }
        return addressBookList.get(0);
    }

    public List<AddressBook> getByUserId() {
        //SQL:select * from address_book where user_id = ? order by update_time desc
        AddressBookExample addressBookExample = new AddressBookExample();
        AddressBookExample.Criteria criteria = addressBookExample.createCriteria();
        criteria.andUserIdEqualTo(BaseContextUtils.getCurrentId());
        addressBookExample.setOrderByClause("update_time DESC");

        return addressBookMapper.selectByExample(addressBookExample);
    }

    public void update(AddressBook addressBook) {
        addressBookMapper.updateByPrimaryKeySelective(addressBook);
    }

    public void delete(Long id) {
        addressBookMapper.deleteByPrimaryKey(id);
    }
}
