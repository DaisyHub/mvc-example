package com.edu118.customer.dao;

import java.sql.SQLException;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.edu118.customer.domain.Customer;
import com.edu118.util.JdbcUtils;

public class CustomerDao {
	protected String findByPhone = "select * from t_customer where cellphone=?";
	protected String insertCus = "insert into t_customer values(?,?,?,?,?,?,?)";
	protected String seleceAllCus = "select * from t_customer";
	protected String deleteByID = "delete from t_customer where cid=?";
	protected String updateByID = "update t_customer set cname=?,gender=?,birthday=?,cellphone=?,email=?,description=? where cid=?";
	protected String queryByCondition = "select * from t_customer where (cname like ? or ? is null) and "
			+ "(gender like ? or ? is null) and (cellphone like ? or ? is null) and (email like ? or ? is null)";

	public void insertCus(Customer customer) {
		// �����ݿ�����ӹ˿���Ϣ
		QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
		Object[] params = { customer.getCid(), customer.getCname(), customer.getGender(), customer.getBirthday(),
				customer.getCellphone(), customer.getEmail(), customer.getDescription() };
		try {
			qr.update(insertCus, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	// ͨ���绰�����ѯ�ͻ�
	public Customer findCusByPhone(String cellphone) {
		QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
		
		try {
			Customer customer = qr.query(findByPhone, new BeanHandler<Customer>(Customer.class), cellphone);
			return customer;
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	// �������еĿͻ���Ϣ,���ؽ����
	public List<Customer> selectAllCus() {
		QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
		try {
			List<Customer> list = qr.query(seleceAllCus, new BeanListHandler<Customer>(Customer.class));
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// ͨ��IDɾ��������¼
	public int deleteByID(String cid) {
		QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
		try {
			return qr.update(deleteByID, cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// ͨ��ID����
	public int updateByID(String cid, Customer newCustomer) {
		QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
		Object[] params = { newCustomer.getCname(), newCustomer.getGender(), newCustomer.getBirthday(),
				newCustomer.getCellphone(), newCustomer.getEmail(), newCustomer.getDescription(), cid };
		try {
			return qr.update(updateByID, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	//������ѯ
	public List<Customer> queryCusByCondition(Customer queryCondition) {
		QueryRunner qr = new QueryRunner(JdbcUtils.getDataSource());
		Object[] params = {
				"%" + queryCondition.getCname() + "%",queryCondition.getCname(),
				"%" + queryCondition.getGender() + "%",queryCondition.getGender(),
				"%" + queryCondition.getCellphone() + "%",queryCondition.getCellphone(),
				"%" + queryCondition.getEmail() + "%",queryCondition.getEmail()
		};
		
		try {
			List<Customer> list = qr.query(queryByCondition, new BeanListHandler<Customer>(Customer.class), params);
			return list;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}

}
