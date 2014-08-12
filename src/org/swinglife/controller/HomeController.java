package org.swinglife.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.spring.context.SpringContextUtil;
import com.spring.context.test.TestSpringContextUtil;

@Controller
public class HomeController {

	/***
	 * ��ҳ ������/page/home.jspҳ��
	 * 
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index() {
		// ����ģ�͸���ͼ��������Ⱦҳ�档����ָ��Ҫ���ص�ҳ��Ϊhomeҳ��
		TestSpringContextUtil test = (TestSpringContextUtil) SpringContextUtil
				.getBean("test");
		test.test();
		ModelAndView mav = new ModelAndView("logon");
		return mav;
	}

	@RequestMapping("go")
	public ModelAndView getBasic() {
		// ����ģ�͸���ͼ��������Ⱦҳ�档����ָ��Ҫ���ص�ҳ��Ϊhomeҳ��
		ModelAndView mav = new ModelAndView("test");
		return mav;
	}
}
