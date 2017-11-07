package com.edu118.customer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.edu118.customer.db.DBConnect;
import com.edu118.customer.domain.Customer;

public class CustomerDao {
	protected String findByPhone = "select * from t_customer where cellphone=?";
	protected String insertCus = "insert into t_customer values(?,?,?,?,?,?,?)";
	protected String seleceAllCus = "select * from t_customer";
	protected String deleteByID = "delete from t_customer where cid=?";
	protected String updateByID = "update t_customer set cname=?,gender=?,birthday=?,cellphone=?,email=?,description=? where cid=?";
	protected String queryByCondition = "select * from t_customer where (cname like ? or ? is null) and "
			+ "(gender like ? or ? is null) and (cellphone like ? or ? is null) and (email like ? or ? is null)";
	public void insertCus(Customer customer) {
		// 向数据库中添加顾客信息
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(insertCus);
			pstmt.setObject(1, UUID.randomUUID().toString().replaceAll("-", ""));
			pstmt.setObject(2, customer.getCname());
			pstmt.setObject(3, customer.getGender());
			pstmt.setObject(4, customer.getBirthday());
			pstmt.setObject(5, customer.getCellphone());
			pstmt.setObject(6, customer.getEmail());
			pstmt.setObject(7, customer.getDescription());
			pstmt.executeUpdate();
		}catch (SQLException e) {
			throw new RuntimeException(e);
		}finally{
			DBConnect.closeDB(pstmt, conn, rs);
		}
		
	}
	//通过电话号码查询客户
	public Customer findCusByPhone(String cellphone) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(findByPhone);
			pstmt.setObject(1, cellphone);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return new Customer(rs.getString("cid"), rs.getString("cname"), rs.getString("gender"),
						rs.getString("birthday"), rs.getString("cellphone"), rs.getString("email"),
						rs.getString("description"));

			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally{
			DBConnect.closeDB(pstmt, conn, rs);
		}
		return null;
	}
	//查找所有的客户信息,返回结果集
	public List<Customer> selectAllCus() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
//		List<Customer> customers = new ArrayList<Customer>();
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(seleceAllCus);
			rs = pstmt.executeQuery();
			List<Customer> customers = new ArrayList<Customer>();
			while(rs.next()){
				customers.add(new Customer(rs.getString(1),rs.getString(2),rs.getString(3),
						rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7)));
			}
			return customers;
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			DBConnect.closeDB(pstmt, conn, rs);
		}
	}
	//通过ID删除该条记录
	public int deleteByID(String cid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(deleteByID);
			pstmt.setObject(1, cid);
			return pstmt.executeUpdate();
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			DBConnect.closeDB(pstmt, conn, rs);
		}
	}
	public int updateByID(String cid, Customer newCustomer) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(updateByID);
			pstmt.setObject(1, newCustomer.getCname());
			pstmt.setObject(2, newCustomer.getGender());
			pstmt.setObject(3, newCustomer.getBirthday());
			pstmt.setObject(4, newCustomer.getCellphone());
			pstmt.setObject(5, newCustomer.getEmail());
			pstmt.setObject(6, newCustomer.getDescription());
			pstmt.setObject(7, cid);
			
			return pstmt.executeUpdate();
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			DBConnect.closeDB(pstmt, conn, rs);
		}
	}
	public List<Customer> queryCusByCondition(Customer queryCondition) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(queryByCondition);
			pstmt.setObject(1, "%"+queryCondition.getCname()+"%");
			pstmt.setObject(2, queryCondition.getCname());
			pstmt.setObject(3, "%"+queryCondition.getGender()+"%");
			pstmt.setObject(4, queryCondition.getGender());
			pstmt.setObject(5, "%"+queryCondition.getCellphone()+"%");
			pstmt.setObject(6, queryCondition.getCellphone());
			pstmt.setObject(7, "%"+queryCondition.getEmail()+"%");
			pstmt.setObject(8, queryCondition.getEmail());
			rs = pstmt.executeQuery();
			List<Customer> customers = new ArrayList<Customer>();
			while(rs.next()){
				customers.add(new Customer(rs.getString(1),rs.getString(2),rs.getString(3),
						rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7)));
			}
			return customers;
		
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			DBConnect.closeDB(pstmt, conn, rs);
		}
	}

}
