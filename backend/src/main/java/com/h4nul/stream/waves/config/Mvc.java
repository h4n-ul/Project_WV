// package com.h4nul.stream.waves.config;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class Mvc implements WebMvcConfigurer {

// 	private BeforeActionInterceptor beforeActionInterceptor;
// 	private NeedLoginInterceptor needLoginInterceptor;
// 	private NeedLogoutInterceptor needLogoutInterceptor;

// 	public MyWebMvcConfigurer(BeforeActionInterceptor beforeActionInterceptor,
// 			NeedLoginInterceptor needLoginInterceptor, NeedLogoutInterceptor needLogoutInterceptor) {
// 		this.beforeActionInterceptor = beforeActionInterceptor;
// 		this.needLoginInterceptor = needLoginInterceptor;
// 		this.needLogoutInterceptor = needLogoutInterceptor;
// 	}

// 	@Override
// 	public void addInterceptors(InterceptorRegistry registry) {
// 		registry.addInterceptor(beforeActionInterceptor).addPathPatterns("/**").excludePathPatterns("/resource/**");

// 		registry.addInterceptor(needLoginInterceptor).addPathPatterns("/usr/article/write").addPathPatterns("/usr/article/doWrite")
// 				.addPathPatterns("/usr/article/modify").addPathPatterns("/usr/article/doModify")
// 				.addPathPatterns("/usr/article/doDelete").addPathPatterns("/usr/member/doLogout");

// 		registry.addInterceptor(needLogoutInterceptor).addPathPatterns("/usr/member/doJoin")
// 				.addPathPatterns("/usr/member/login").addPathPatterns("/usr/member/doLogin");
// 	}

// }
