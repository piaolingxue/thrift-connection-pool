package com.huaban.thrift;


import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool.Config;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericConnectionProvider implements ConnectionProvider {
    public static final Logger logger = LoggerFactory.getLogger(GenericConnectionProvider.class);
    /** 服务的IP地址 */
    private String serviceIP;
    /** 服务的端口 */
    private int servicePort;
    /** 连接超时配置 */
    private int connectionTimeout;
    /** 可以从缓存池中分配对象的最大数量 */
    private int maxActive = GenericObjectPool.DEFAULT_MAX_ACTIVE;
    /** 缓存池中最大空闲对象数量 */
    private int maxIdle = GenericObjectPool.DEFAULT_MAX_IDLE;
    /** 缓存池中最小空闲对象数量 */
    private int minIdle = GenericObjectPool.DEFAULT_MIN_IDLE;
    /** 阻塞的最大数量 */
    private long maxWait = GenericObjectPool.DEFAULT_MAX_WAIT;
    /** 从缓存池中分配对象，是否执行PoolableObjectFactory.validateObject方法 */
    private boolean testOnBorrow = GenericObjectPool.DEFAULT_TEST_ON_BORROW;
    private boolean testOnReturn = GenericObjectPool.DEFAULT_TEST_ON_RETURN;
    private boolean testWhileIdle = GenericObjectPool.DEFAULT_TEST_WHILE_IDLE;
    /** 对象缓存池 */
    private ObjectPool<TTransport> objectPool = null;

    public GenericConnectionProvider(String serviceIP, int servicePort, int connectionTimeout) throws Exception {
        this.serviceIP = serviceIP;
        this.servicePort = servicePort;
        this.connectionTimeout = connectionTimeout;
        
        Config config = new Config();
        config.maxActive = maxActive;
        config.maxIdle = maxIdle;
        config.minIdle = minIdle;
        config.maxWait = maxWait;
        config.testOnBorrow = testOnBorrow;
        config.testOnReturn = testOnReturn;
        config.testWhileIdle = testWhileIdle;

        ThriftPoolableObjectFactory thriftPoolableObjectFactory =
                new ThriftPoolableObjectFactory(serviceIP, servicePort, connectionTimeout);

        objectPool = new GenericObjectPool<TTransport>(thriftPoolableObjectFactory, config);
    }

    @Override
    public void close() {
        try {
            objectPool.close();
        } catch (Exception e) {
            throw new RuntimeException("erorr destroy()", e);
        }
    }

    @Override
    public TSocket getConnection() {
        try {
            return (TSocket) objectPool.borrowObject();
        } catch (Exception e) {
            throw new RuntimeException("error getConnection()", e);
        }
    }

    @Override
    public void returnConn(TSocket socket) {
        try {
            objectPool.returnObject(socket);
        } catch (Exception e) {
            throw new RuntimeException("error returnCon()", e);
        }
    }

    public ObjectPool<TTransport> getObjectPool() {
        return objectPool;
    }

    public void setObjectPool(ObjectPool<TTransport> objectPool) {
        this.objectPool = objectPool;
    }

    public String getServiceIP() {
        return serviceIP;
    }

    public int getServicePort() {
        return servicePort;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public long getMaxWait() {
        return maxWait;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }
    
    
}
