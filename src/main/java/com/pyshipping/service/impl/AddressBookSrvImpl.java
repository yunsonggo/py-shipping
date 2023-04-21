package com.pyshipping.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pyshipping.mapper.AddressBookMapper;
import com.pyshipping.model.AddressBook;
import com.pyshipping.service.AddressBookSrv;
import org.springframework.stereotype.Service;

@Service
public class AddressBookSrvImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookSrv {
}
