package com.huaban.thrift;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ThriftPoolableObjectFactory implements PoolableObjectFactory<TTransport> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private String serviceIP;
    private int servicePort;
    private int timeOut;

    /**
     * 
     * @param serviceIP
     * @param servicePort
     * @param timeOut
     */
    public ThriftPoolableObjectFactory(String serviceIP, int servicePort, int timeOut) {
        this.serviceIP = serviceIP;
        this.servicePort = servicePort;
        this.timeOut = timeOut;
    }

    @Override
    public void destroyObject(TTransport socket) throws Exception {
        if (socket.isOpen()) {
            socket.close();
        }
    }

    /**
     * 
     */
    @Override
    public TTransport makeObject() throws Exception {
        try {
            TTransport transport = new TSocket(this.serviceIP, this.servicePort, this.timeOut);
            transport.open();
            return transport;
        } catch (Exception e) {
            logger.error("error ThriftPoolableObjectFactory()", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean validateObject(TTransport arg0) {
        try {
            TSocket thriftSocket = (TSocket) arg0;
            if (thriftSocket.isOpen()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void passivateObject(TTransport arg0) throws Exception {
        // DO NOTHING
    }

    @Override
    public void activateObject(TTransport arg0) throws Exception {
        // DO NOTHING
    }

    public String getServiceIP() {
        return serviceIP;
    }

    public void setServiceIP(String serviceIP) {
        this.serviceIP = serviceIP;
    }

    public int getServicePort() {
        return servicePort;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }
}
