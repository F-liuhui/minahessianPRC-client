package com.cnpc.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.cnpc.test.pool.ConnectionManagerment;

@Configuration
public class PoolConfig {
    
	@Bean
	public ConnectionManagerment getConnectionManagerment() throws Exception {
	    ConnectionManagerment.getManagerment().getPool("localhost",9090);
		return ConnectionManagerment.getManagerment();
	}
}
