package com.huaban.thrift;

import org.apache.thrift.transport.TSocket;

public interface ConnectionProvider {
    public TSocket getConnection();
    public void returnConn(TSocket socket);
    public void close();
}
