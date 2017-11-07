package com.edu118.customer.web.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.edu118.customer.domain.Customer;
import com.edu118.customer.service.CustomerService;
import com.edu118.user.web.servlet.BaseServlet;
import com.edu118.utils.CommonUtils;

public class CustomerServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	private CustomerService customerService = new CustomerService();

	public String add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//将表单属性与值封装到一个对象中
		Map<String, String[]> map = request.getParameterMap();
		Customer customer = CommonUtils.getBean(map, Customer.class);
		customer.setCid(UUID.randomUUID().toString().replace("-", ""));
		//首先对表单进行检验,并将错误信息放入map
		Map<String, String> error = new HashMap<String, String>();

		if(customer.getCname().length()<3 || customer.getCname().length()>15){
			error.put("cname", "名字长度要在3-15个字符之间");
		}
		if(customer.getCellphone().length()<8 || customer.getCellphone().length()>11){
			error.put("cellphone", "电话号码得在8-11个数字之间");
		}else if(!phoneisDigit(customer.getCellphone())){
			error.put("cellphone", "电话号码必须都是数字");
		}
		
		boolean matches = Pattern.compile("\\w+@\\w+.\\w+").matcher(customer.getEmail()).matches();
		if(!matches){
			error.put("email", "邮箱格式不太对，你再看看");
		}
			
			
		request.setAttribute("customer", customer);	
		if(error.isEmpty()){
			//符合注册的要求
			try{
				customerService.addCustomer(customer);
				request.setAttribute("message", "添加成功");
				return "f:/msg.jsp";
			}catch(Exception e){
				request.setAttribute("hasCustomer", e.getMessage());
				return "f:/add.jsp";
			}
			
		}else{
			//将错误信息回显到add.jsp			
			request.setAttribute("error", error);
			return "f:/add.jsp";
		}
	}
	
	//判断字符串是否都是数字
	private static boolean phoneisDigit(String str){
		char[] charArray = str.toCharArray();
		for(char ch : charArray){
			if(!Character.isDigit(ch)){
				return false;
			}
		}
		return true;
	}
	//通过id删除客户信息
	public String delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cid = request.getParameter("cid");
		try{
			customerService.deleteCustomer(cid);
			request.setAttribute("message", "删除成功");
			return "f:/msg.jsp";
		}catch(Exception e){
			request.setAttribute("message", e.getMessage());
			return "f:/msg.jsp";
		}
	}
	
	//修改客户信息
	public String update(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, String[]> map = request.getParameterMap();
		Customer newCustomer = CommonUtils.getBean(map, Customer.class);
		//判断一下想要修改的cellphone是否在数据库 其它客户中 已经存在，若存在，则不能执行修改操作
		
		Customer hasPhone = customerService.findCustomerByPhone(newCustomer.getCellphone());
		if(hasPhone != null && !hasPhone.getCname().equals(newCustomer.getCname())){	
			//已经存在该手机号，不能修改
			request.setAttribute("newCustomer", newCustomer);
			request.setAttribute("message", "已经存在该手机号，不能修改");
			return "f:/msg.jsp";
		}else{
			//通过cid找到该顾客，然后更新其它的值，cid从哪里获得？
			String cid = request.getParameter("cid");	
			try{
				customerService.updateCustomer(cid,newCustomer);
				request.setAttribute("message", "改造成功");
				return "f:/msg.jsp";
			}catch(Exception e){
				//没有成功执行更新操作
				request.setAttribute("newCustomer", newCustomer);
				request.setAttribute("message", e.getMessage());
				return "f:/edit.jsp";
			}
		}	
	}
	
	//列出所有客户信息
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Customer> customers = customerService.findAllCustomer();
		request.setAttribute("customers", customers);		
		return "f:/list.jsp";
	}
	//模糊查询：名称/性别/手机/邮箱
	public String query(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, String[]> map = request.getParameterMap();
		Customer queryCondition = CommonUtils.getBean(map, Customer.class);
		try{
			List<Customer> customers = customerService.queryCustomer(queryCondition);
			request.setAttribute("customers", customers);		
			return "f:/list.jsp";
		}catch(Exception e){
			request.setAttribute("message", e.getMessage());
			return "f:/msg.jsp";
		}
		
	}
	//找到要回显的信息
	public String sendObjectToEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cellphone = request.getParameter("cellphone");
		String cid = request.getParameter("cid");
//		System.out.println(cid);
		Customer customer = customerService.findCustomerByPhone(cellphone);
		//设置回显信息
		System.out.println(customer);
		request.setAttribute("customer", customer);
		
		//从这里设置cid给edit.jsp,edit.jsp将id传给update做条件
		request.setAttribute("cid", cid);
		return "f:/edit.jsp";
	}

}
