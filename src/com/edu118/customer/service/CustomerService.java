package com.edu118.customer.service;

import java.util.List;

import com.edu118.customer.dao.CustomerDao;
import com.edu118.customer.domain.Customer;

public class CustomerService {

	private CustomerDao customerDao = new CustomerDao();

	public void addCustomer(Customer customer) {
		/*
		 * 通过Dao中的方法findCusByPhone()判断数据库是否有该顾客，若有，则会显该顾客已注册
		 * 若没有，调用insertCus向数据库中添加数据
		 */
		Customer customer2 = findCustomerByPhone(customer.getCellphone());
		if(customer2 != null){
			//有该客户，不再添加
			throw new RuntimeException("该手机号已经被注册，不能再添加了");
		}else{
			customerDao.insertCus(customer);
		}
	}

	public List<Customer> findAllCustomer() {
		//将ResultSet放入List集合
		
		return customerDao.selectAllCus();
	}

	public void deleteCustomer(String cid) {
		if(customerDao.deleteByID(cid) == 0){
			throw new RuntimeException("这个人后台太强，不能删除");
		}
	}
	//将这个方法单抽出来
	public Customer findCustomerByPhone(String cellphone){
		return customerDao.findCusByPhone(cellphone);
	}

	public void updateCustomer(String cid, Customer newCustomer) {
		//
		if(customerDao.updateByID(cid,newCustomer) == 0){
			throw new RuntimeException("太顽固的人，不能为他执行更新操作");
		}
	}

	public List<Customer> queryCustomer(Customer queryCondition) {
		List<Customer> customers =  customerDao.queryCusByCondition(queryCondition);
		if(customers.isEmpty()) throw new RuntimeException("没有你想要找的人");
		return customers;
	}
}
