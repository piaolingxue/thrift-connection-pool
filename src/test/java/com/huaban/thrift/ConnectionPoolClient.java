package com.huaban.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

import com.huaban.thrift.CalcService.Client;
import com.huaban.thrift.CalcService.Iface;


public class ConnectionPoolClient implements Iface {
    GenericMethodInvocation ping;
    GenericMethodInvocation add;
    ConnectionManager connManager;
    
    class CalcServiceClient implements Iface {
        
        public CalcServiceClient(ConnectionManager manager) {
            
        }

        @Override
        public void ping() throws TException {
            TProtocol protocol = new TBinaryProtocol(connManager.getSocket());
            Client client = new Client(protocol);
            client.ping();
        }

        @Override
        public int add(int a, int b) throws TException {
            TProtocol protocol = new TBinaryProtocol(connManager.getSocket());
            Client client = new Client(protocol);
            return client.add(a, b);
        }
        
    }

    public ConnectionPoolClient(ConnectionManager connManager) {
        this.connManager = connManager;
        CalcServiceClient client = new ConnectionPoolClient.CalcServiceClient(connManager);
        try {
            ping = new GenericMethodInvocation(client, client.getClass().getMethod("ping"), null);
            add = new GenericMethodInvocation(client, client.getClass().getMethod("add", int.class, int.class), null);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    

    @Override
    public void ping() {
        try {
            connManager.invoke(ping);
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Override
    public int add(int a, int b) throws TException {
        try {
            add.setArgs(new Object[] {a, b});
            return (Integer) connManager.invoke(add);
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

}
