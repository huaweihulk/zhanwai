package com.tts.zhanwai.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanUtils implements ApplicationContextAware {
	private ApplicationContext applicationContext;

	public Object getBean(String beanName) {
		// if (!beanName.endsWith(".class")) {
		// beanName += ".class";
		// }
		return applicationContext.getBean(beanName);
	}

	public <T> Object getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = arg0;
	}
}
