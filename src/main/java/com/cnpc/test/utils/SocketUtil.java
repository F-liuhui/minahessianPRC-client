package com.cnpc.test.utils;
import java.util.concurrent.TimeUnit;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketUtil {
	private static final Logger log=LoggerFactory.getLogger(SocketUtil.class);
    public static Object doRead(Object out,GenericObjectPool<IoSession> pool) throws Exception {
    	Object obj=null;
    	IoSession session=pool.borrowObject();
		try {
			// a现在已实现了连接，接下来就是发送-接收-断开了
			session.write(out).awaitUninterruptibly();
	        // 接收
	        ReadFuture readFuture = session.read();
	        //同步读取，最多等待15秒,如果15秒还没有响应则抛出异常
	        if (readFuture.awaitUninterruptibly(15,TimeUnit.SECONDS)) {
	            obj = readFuture.getMessage();
	            if(obj instanceof Exception) {
	            	log.error("读取数据错误");
	            	throw (Exception)obj;
	            }
	        }else {
	        	log.error("读取数据超时");
	        	throw new Exception("读取超时");
	        } 
		}catch(Exception e) {
            throw new Exception(e);
		}finally {
			pool.returnObject(session);
		}
        return obj;
    }
}
