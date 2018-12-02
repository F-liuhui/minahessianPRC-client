package com.cnpc.test.minaclient;

import com.cnpc.test.proxy.ProxyFactory;
public class MinaHessiaProxyFactory {
	public static <T> T setService(Class<T> clazz,String Ip,int serverPort) {
		return ProxyFactory.getProxy(clazz,Ip,serverPort);
	}
}
