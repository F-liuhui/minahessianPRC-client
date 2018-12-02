package com.cnpc.test.pool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SocketConnectPoolFactory implements PooledObjectFactory<IoSession> {
	private static final Logger log=LoggerFactory.getLogger(SocketConnectPoolFactory.class);
	private String ip;
	private int port;
	
	public SocketConnectPoolFactory(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
	}

	//创建
	@Override
	public PooledObject<IoSession> makeObject() throws Exception {
		log.info("创建池对象");
		IoSession ioSession=ConnectionManagerment.getManagerment().getConnection(ip,port);
		return new DefaultPooledObject<IoSession>(ioSession);
	}

	//销毁
	@Override
	public void destroyObject(PooledObject<IoSession> p) throws Exception {
		log.info("销毁池对象");
		IoSession session=p.getObject();
		session.closeNow();
		session.getService().dispose();
		
	}

	//验证
	@Override
	public boolean validateObject(PooledObject<IoSession> p) {
		log.info("验证池对象");
		IoSession ioSession=p.getObject();
		if(ioSession.isConnected()) {
			return true;
		}else {
			return false;
		}
	}

	//激活
	@Override
	public void activateObject(PooledObject<IoSession> p) throws Exception {
		
	}

	//卸载(钝化)
	@Override
	public void passivateObject(PooledObject<IoSession> p) throws Exception {
		
	}	
}
