package com.designpattern.proxy.impl;

import org.apache.log4j.Logger;

import com.designpattern.proxy.UserDao;

public class UserDaoImpl implements UserDao {

	Logger logger = Logger.getLogger(UserDaoImpl.class);

	public void save() {
		logger.info("user saved");
	}
}
