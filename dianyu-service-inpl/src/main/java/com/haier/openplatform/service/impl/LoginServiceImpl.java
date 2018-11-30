package com.haier.openplatform.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haier.openplatform.dao.LoginDao;
import com.haier.openplatform.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService{

	private Log log = LogFactory.getLog(LoginServiceImpl.class);
	@Autowired
	private LoginDao loginDao;
	
	@Override
	public int findByName() {
		// TODO Auto-generated method stub
		try{
			int loginName =1;
			System.out.print(loginName);
		}catch(Exception e){
			log.error(e.getMessage(),e.fillInStackTrace());
		}
		
		return 1;
	}

}
