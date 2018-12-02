package com.cnpc.test.codec;
import com.caucho.hessian.io.HessianOutput;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.io.ByteArrayOutputStream;

public class ByteArrayEncoder  extends ProtocolEncoderAdapter {
    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HessianOutput ho = new HessianOutput(os);
        ho.writeObject(message);
        byte[] bytes=os.toByteArray();
        IoBuffer ioBuffer=IoBuffer.wrap(bytes);
        out.write(ioBuffer);
    }
}
