package com.cnpc.test.handler;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.mina.core.future.ConnectFuture;

import com.cnpc.test.entity.ExchangeEntity;
import com.cnpc.test.pool.ConnectionManagerment;
import com.cnpc.test.utils.SocketUtil;

public class ProxyHandler implements InvocationHandler{
	private String classFullName;
	private String Ip;
	private int serverPort;
	GenericObjectPool<ConnectFuture> pool= null;
	public ProxyHandler(String classFullName,String Ip,int serverPort) {
		super();
		this.classFullName = classFullName;
		this.Ip = Ip;
		this.serverPort = serverPort;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		ExchangeEntity exchangeEntity=new ExchangeEntity();
		exchangeEntity.setArgs(args);
		exchangeEntity.setClazz(Class.forName(classFullName));
		exchangeEntity.setMethodName(method.getName());    
		return SocketUtil.doRead(exchangeEntity,ConnectionManagerment.getManagerment().getPool(Ip,serverPort));
	}
}
