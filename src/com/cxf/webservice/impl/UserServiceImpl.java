package com.cxf.webservice.impl;

import java.util.List;

import javax.jws.WebService;

import com.cxf.dao.UserDao;
import com.cxf.pojo.User;
import com.cxf.webservice.UserService;

@WebService(endpointInterface = "com.cxf.webservice.UserService")
public class UserServiceImpl implements UserService {
	private UserDao userDao;

	public List getAllUsers() {
		return userDao.findAllUser();
	}

	public User getUserById(String userid) {
		return userDao.findUserById(userid);
	}

	public void insert(String userid, String username, String useremail,
			int userage) {
		User user = new User();
		user.setUserage(userage);
		user.setUseremail(useremail);
		user.setUserid(userid);
		user.setUsername(username);
		userDao.insert(user);
		System.out.println("insert successfully!");
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public User getUserById(String userid, String userName) {
		// TODO Auto-generated method stub
		return null;
	}
}