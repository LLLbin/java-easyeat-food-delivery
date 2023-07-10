package com.lllebin.controller;

import com.lllebin.domain.AddressBook;
import com.lllebin.response.CommonResponse;
import com.lllebin.service.AddressBookService;
import com.lllebin.utils.BaseContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: AddressBookController
 * Package: com.lllebin.controller
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增
     */
    @PostMapping
    public CommonResponse<AddressBook> save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContextUtils.getCurrentId());
        log.info("新增地址，addressBook:{}", addressBook);
        addressBookService.save(addressBook);
        return CommonResponse.success(addressBook);
    }

    /**
     * 设置默认地址
     */
    @PutMapping("/default")
    public CommonResponse<AddressBook> updateDefault(@RequestBody AddressBook addressBook) {
        log.info("设置默认地址，addressBook:{}", addressBook);
        addressBookService.updateDefaultById(addressBook);
        return CommonResponse.success(addressBook);
    }

    /**
     * 根据id查询地址
     */
    @GetMapping("/{id}")
    public CommonResponse<AddressBook> get(@PathVariable Long id) {
        log.info("根据Id查寻地址，{}", id);
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return CommonResponse.success(addressBook);
        } else {
            return CommonResponse.error("没有找到");
        }
    }

    /**
     * 查询默认地址
     */
    @GetMapping("/default")
    public CommonResponse<AddressBook> getDefault() {
        log.info("查寻默认地址");
        AddressBook addressBook = addressBookService.getDefault();
        if (addressBook == null) {
            return CommonResponse.error("没有找到");
        } else {
            return CommonResponse.success(addressBook);
        }
    }

    /**
     * 查询指定用户的全部地址
     */
    @GetMapping("/list")
    public CommonResponse<List<AddressBook>> list(AddressBook addressBook) {
        log.info("查询指定用户所有地址，addressBook:{}", addressBook);
        List<AddressBook> addressBookList = addressBookService.getByUserId();
        return CommonResponse.success(addressBookList);
    }

    /**
     * 更新指定用户地址
     */
    @PutMapping
    public CommonResponse<String> update(@RequestBody AddressBook addressBook) {
        log.info("更新地址，addressBook:{}", addressBook);
        addressBookService.update(addressBook);
        return CommonResponse.success("修改成功");
    }

    /**
     * 删除指定用户地址
     */
    @DeleteMapping
    public CommonResponse<String> delete(Long id) {
        log.info("删除指定地址，addressBook:{}", id);
        addressBookService.delete(id);
        return CommonResponse.success("删除成功");
    }
}
