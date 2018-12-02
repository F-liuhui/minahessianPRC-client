package com.cnpc.test.pool;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;
@Component
public class ConnectorCloseLitener implements ApplicationListener<ContextClosedEvent>{

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		ConnectionManagerment.getManagerment().closeConnector();
	}

}
