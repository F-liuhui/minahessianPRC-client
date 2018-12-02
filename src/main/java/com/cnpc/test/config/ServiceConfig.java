package com.cnpc.test.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.cnpc.test.minaclient.MinaHessiaProxyFactory;
import com.cnpc.test.service.TestService;

@Configuration
public class ServiceConfig {
	@Bean
	public TestService getTestService() {
		return MinaHessiaProxyFactory.setService(TestService.class,"localhost",9090);
	}
}
