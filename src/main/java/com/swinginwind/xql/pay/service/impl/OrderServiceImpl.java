package com.swinginwind.xql.pay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swinginwind.xql.pay.entity.BaseOrder;
import com.swinginwind.xql.pay.mapper.BaseOrderMapper;
import com.swinginwind.xql.pay.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private BaseOrderMapper orderMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return orderMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(BaseOrder record) {
		return orderMapper.insert(record);
	}

	@Override
	public int insertSelective(BaseOrder record) {
		return orderMapper.insertSelective(record);
	}

	@Override
	public BaseOrder selectByPrimaryKey(Integer id) {
		return orderMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(BaseOrder record) {
		return orderMapper.updateByPrimaryKey(record);
	}

	@Override
	public int updateByPrimaryKeyWithBLOBs(BaseOrder record) {
		return orderMapper.updateByPrimaryKeyWithBLOBs(record);
	}

	@Override
	public int updateByPrimaryKey(BaseOrder record) {
		return orderMapper.updateByPrimaryKey(record);
	}

}
