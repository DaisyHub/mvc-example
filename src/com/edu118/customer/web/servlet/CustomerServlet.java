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
		//����������ֵ��װ��һ��������
		Map<String, String[]> map = request.getParameterMap();
		Customer customer = CommonUtils.getBean(map, Customer.class);
		customer.setCid(UUID.randomUUID().toString().replace("-", ""));
		//���ȶԱ����м���,����������Ϣ����map
		Map<String, String> error = new HashMap<String, String>();

		if(customer.getCname().length()<3 || customer.getCname().length()>15){
			error.put("cname", "���ֳ���Ҫ��3-15���ַ�֮��");
		}
		if(customer.getCellphone().length()<8 || customer.getCellphone().length()>11){
			error.put("cellphone", "�绰�������8-11������֮��");
		}else if(!phoneisDigit(customer.getCellphone())){
			error.put("cellphone", "�绰������붼������");
		}
		
		boolean matches = Pattern.compile("\\w+@\\w+.\\w+").matcher(customer.getEmail()).matches();
		if(!matches){
			error.put("email", "�����ʽ��̫�ԣ����ٿ���");
		}
			
			
		request.setAttribute("customer", customer);	
		if(error.isEmpty()){
			//����ע���Ҫ��
			try{
				customerService.addCustomer(customer);
				request.setAttribute("message", "��ӳɹ�");
				return "f:/msg.jsp";
			}catch(Exception e){
				request.setAttribute("hasCustomer", e.getMessage());
				return "f:/add.jsp";
			}
			
		}else{
			//��������Ϣ���Ե�add.jsp			
			request.setAttribute("error", error);
			return "f:/add.jsp";
		}
	}
	
	//�ж��ַ����Ƿ�������
	private static boolean phoneisDigit(String str){
		char[] charArray = str.toCharArray();
		for(char ch : charArray){
			if(!Character.isDigit(ch)){
				return false;
			}
		}
		return true;
	}
	//ͨ��idɾ���ͻ���Ϣ
	public String delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cid = request.getParameter("cid");
		try{
			customerService.deleteCustomer(cid);
			request.setAttribute("message", "ɾ���ɹ�");
			return "f:/msg.jsp";
		}catch(Exception e){
			request.setAttribute("message", e.getMessage());
			return "f:/msg.jsp";
		}
	}
	
	//�޸Ŀͻ���Ϣ
	public String update(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, String[]> map = request.getParameterMap();
		Customer newCustomer = CommonUtils.getBean(map, Customer.class);
		//�ж�һ����Ҫ�޸ĵ�cellphone�Ƿ������ݿ� �����ͻ��� �Ѿ����ڣ������ڣ�����ִ���޸Ĳ���
		
		Customer hasPhone = customerService.findCustomerByPhone(newCustomer.getCellphone());
		if(hasPhone != null && !hasPhone.getCname().equals(newCustomer.getCname())){	
			//�Ѿ����ڸ��ֻ��ţ������޸�
			request.setAttribute("newCustomer", newCustomer);
			request.setAttribute("message", "�Ѿ����ڸ��ֻ��ţ������޸�");
			return "f:/msg.jsp";
		}else{
			//ͨ��cid�ҵ��ù˿ͣ�Ȼ�����������ֵ��cid�������ã�
			String cid = request.getParameter("cid");	
			try{
				customerService.updateCustomer(cid,newCustomer);
				request.setAttribute("message", "����ɹ�");
				return "f:/msg.jsp";
			}catch(Exception e){
				//û�гɹ�ִ�и��²���
				request.setAttribute("newCustomer", newCustomer);
				request.setAttribute("message", e.getMessage());
				return "f:/edit.jsp";
			}
		}	
	}
	
	//�г����пͻ���Ϣ
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Customer> customers = customerService.findAllCustomer();
		request.setAttribute("customers", customers);		
		return "f:/list.jsp";
	}
	//ģ����ѯ������/�Ա�/�ֻ�/����
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
	//�ҵ�Ҫ���Ե���Ϣ
	public String sendObjectToEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cellphone = request.getParameter("cellphone");
		String cid = request.getParameter("cid");
//		System.out.println(cid);
		Customer customer = customerService.findCustomerByPhone(cellphone);
		//���û�����Ϣ
		System.out.println(customer);
		request.setAttribute("customer", customer);
		
		//����������cid��edit.jsp,edit.jsp��id����update������
		request.setAttribute("cid", cid);
		return "f:/edit.jsp";
	}

}
