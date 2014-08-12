package org.swinglife.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.spring.context.SpringContextUtil;
import com.spring.context.test.TestSpringContextUtil;

@Controller
public class HomeController {

	/***
	 * 首页 返回至/page/home.jsp页面
	 * 
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index() {
		// 创建模型跟视图，用于渲染页面。并且指定要返回的页面为home页面
		TestSpringContextUtil test = (TestSpringContextUtil) SpringContextUtil
				.getBean("test");
		test.test();
		ModelAndView mav = new ModelAndView("logon");
		return mav;
	}

	@RequestMapping("go")
	public ModelAndView getBasic() {
		// 创建模型跟视图，用于渲染页面。并且指定要返回的页面为home页面
		ModelAndView mav = new ModelAndView("test");
		return mav;
	}
}
