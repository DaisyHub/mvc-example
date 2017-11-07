package com.edu118.customer.service;

import java.util.List;

import com.edu118.customer.dao.CustomerDao;
import com.edu118.customer.domain.Customer;

public class CustomerService {

	private CustomerDao customerDao = new CustomerDao();

	public void addCustomer(Customer customer) {
		/*
		 * ͨ��Dao�еķ���findCusByPhone()�ж����ݿ��Ƿ��иù˿ͣ����У�����Ըù˿���ע��
		 * ��û�У�����insertCus�����ݿ����������
		 */
		Customer customer2 = findCustomerByPhone(customer.getCellphone());
		if(customer2 != null){
			//�иÿͻ����������
			throw new RuntimeException("���ֻ����Ѿ���ע�ᣬ�����������");
		}else{
			customerDao.insertCus(customer);
		}
	}

	public List<Customer> findAllCustomer() {
		//��ResultSet����List����
		
		return customerDao.selectAllCus();
	}

	public void deleteCustomer(String cid) {
		if(customerDao.deleteByID(cid) == 0){
			throw new RuntimeException("����˺�̨̫ǿ������ɾ��");
		}
	}
	//����������������
	public Customer findCustomerByPhone(String cellphone){
		return customerDao.findCusByPhone(cellphone);
	}

	public void updateCustomer(String cid, Customer newCustomer) {
		//
		if(customerDao.updateByID(cid,newCustomer) == 0){
			throw new RuntimeException("̫��̵��ˣ�����Ϊ��ִ�и��²���");
		}
	}

	public List<Customer> queryCustomer(Customer queryCondition) {
		List<Customer> customers =  customerDao.queryCusByCondition(queryCondition);
		if(customers.isEmpty()) throw new RuntimeException("û������Ҫ�ҵ���");
		return customers;
	}
}
