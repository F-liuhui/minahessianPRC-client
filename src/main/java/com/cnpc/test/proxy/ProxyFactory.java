package com.cnpc.test.proxy;
import java.lang.reflect.Proxy;

import com.cnpc.test.handler.ProxyHandler;

public class ProxyFactory {
	@SuppressWarnings("unchecked")
	public static <T> T getProxy(Class<T> clazz,String Ip,int serverPort) {
		return (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {clazz},new ProxyHandler(clazz.getName(),Ip,serverPort));
		
	}
}
