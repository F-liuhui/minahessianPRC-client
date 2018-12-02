package com.cnpc.test.pool;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnpc.test.codec.ByteArrayCodecFactory;

public class ConnectionManagerment{
	private  static final List<NioSocketConnector> connectors= new ArrayList<NioSocketConnector>();
	private static ConnectionManagerment connectionManagerment=null;
	private static final Logger log=LoggerFactory.getLogger(ConnectionManagerment.class);
	GenericObjectPool<IoSession> pool=null;
	protected void closeConnector() {
		for(NioSocketConnector cn:connectors) {
			log.info("关闭连接");
			cn.dispose();
		}
	}
	private ConnectionManagerment() {
		super();
	}
    
	public static ConnectionManagerment getManagerment() {
		if(connectionManagerment==null) {
			synchronized(ConnectionManagerment.class){
				if(connectionManagerment==null) {
					connectionManagerment=new ConnectionManagerment();
				}
			}
		}
	    return connectionManagerment;
	}
	
	protected IoSession getConnection(String Ip,int serverPort) throws Exception {
		        NioSocketConnector connector=new NioSocketConnector();
				// 添加解码器
				connector.getFilterChain().addLast("codec",new ProtocolCodecFilter(new ByteArrayCodecFactory()));
				//获取配置
		        SocketSessionConfig cfg = connector.getSessionConfig();
		        //设置同步
		        cfg.setUseReadOperation(true);
		        //a设置默认访问地址
		        connector.setDefaultRemoteAddress(new InetSocketAddress(Ip,serverPort));// 设置默认访问地址
		        
		        IoSession iosession=null;
		        
		        //a如果五次都连接不成功则抛出异常
		        for(int i=0;i<5;i++) {
		             try {
		            	ConnectFuture future = connector.connect();
		                future.awaitUninterruptibly(); // 等待连接创建成功10秒
	                	IoSession session =future.getSession();
	                	if(session.isConnected()) {
	                		connectors.add(connector);
	             		    System.out.println(connectors.size());
	                		log.info("连接服务端127.0.0.1"+ ":"+9090 + "[成功],时间:"+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	                		return session;
	                	}
	                }catch (RuntimeIoException e) {
	            	 log.warn("连接服务端127.0.0.1:"+ 9090+ "失败,尝试重连第,"+(i+1)+ "次连接，时间:"
	                                   + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
	                                   + "异常内容:"+ e.getMessage());
	            	 if(i==4) {
		            		log.error((i+1)+"次连接均失败，停止连接尝试"+e);
		            		throw new Exception("连接SOCKET服务异常,请检查SOCKET端口、IP是否正确,网络是否正常："+e);
		             }
	                 Thread.sleep(1000);//a 连接失败后,重连间隔1s
	             }
	        }
		  return iosession;
	}
	public  GenericObjectPool<IoSession> getPool(String Ip,int port) throws Exception{
		if(pool==null) {
			synchronized(this){
				if(pool==null) {
					log.info("创建新的pool");
					//a 对象工厂
					SocketConnectPoolFactory sp=new SocketConnectPoolFactory(Ip,port);
					
					//a 资源池配置对象
					GenericObjectPoolConfig<IoSession> poolConfig = new GenericObjectPoolConfig<IoSession>();
					
					//a 防止对象泄露,客户端从池里面取出去后，若长时间不归还池中，则抛弃并destory
					//AbandonedConfig abandonedConfig=new AbandonedConfig();
					//a 则把抛弃对象时，打印相关的日志（调用日志）
					//abandonedConfig.setLogAbandoned(true);
					
					
					//a 最大空闲数
					poolConfig.setMaxIdle(10);
					//a 整个池最大对象数
					poolConfig.setMaxTotal(15);
					//a 最小空闲数
					poolConfig.setMinIdle(5);
					//a 当没有空闲连接时，获取一个对象的最大等待时间。如果这个值小于0，则永不超时，一直等待，直到有空闲对象到来。如果大于0，则等待maxWaitMillis长时间，如果还没有空闲对象，将抛出NoSuchElementException异常。默认值是-1；可以根据需要自己调整，单位是毫秒。
					poolConfig.setMaxWaitMillis(-1);
					
					// a在从对象池获取对象时是否检测对象有效，true是；默认值是false
					poolConfig.setTestOnBorrow(true);
					// a在向对象池中归还对象时是否检测对象有效，true是，默认值是false
					poolConfig.setTestOnReturn(true);
					// a在检测空闲对象线程检测到对象不需要移除时，是否检测对象的有效性。true是，默认值是false
					poolConfig.setTestWhileIdle(true);
					//a 当对象池没有空闲对象时，新的获取对象的请求是否阻塞。true阻塞。默认值是true
					poolConfig.setBlockWhenExhausted(true);
					
					//a检测空闲对象线程每次检测的空闲对象的数量。默认值是3；如果这个值小于0,则每次检测的空闲对象数量等于当前空闲对象数量除以这个值的绝对值，并对结果向上取整
					poolConfig.setNumTestsPerEvictionRun(-1);
					//a 对象最小的空闲时间。如果为小于等于0，则取Long的最大值，如果大于0，当空闲的时间大于这个值时，执行移除这个对象操作。默认值是1000L * 60L * 30L;即30分钟。这个参数是强制性的，只要空闲时间超过这个值，就会移除。
					poolConfig.setMinEvictableIdleTimeMillis(-1);
					//a 对象最小的空闲时间，如果小于等于0，则取Long的最大值，如果大于0，当对象的空闲时间超过这个值，并且当前空闲对象的数量大于最小空闲数量(minIdle)时，执行移除操作。这个和上面的minEvictableIdleTimeMillis的区别是，它会保留最小的空闲对象数量。而上面的不会，是强制性移除的。默认值是-1；
					poolConfig.setSoftMinEvictableIdleTimeMillis(2000);
					//a 空闲对象检测线程的执行周期，即多长时候执行一次空闲对象检测。单位是毫秒数。如果小于等于0，则不执行检测线程。默认值是-1;
					poolConfig.setTimeBetweenEvictionRunsMillis(-1);
					
					GenericObjectPool<IoSession> futruePool=new GenericObjectPool<IoSession>(sp,poolConfig);
					for(int i=0;i<6;i++) {
						futruePool.addObject();
					}
					pool=futruePool;
					log.info("pool结束创建结束");
				}
			}
		}
		return pool;
   }
}
