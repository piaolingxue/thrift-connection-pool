/**
 * 
 */
package com.huaban.thrift;

import static org.junit.Assert.*;

import junit.framework.TestCase;
import org.junit.Test;


/**
 * @author matrix
 * 
 */
public class ConnectionPoolClientTest extends TestCase {
    ConnectionProvider connProvider;
    ConnectionManager connManager;
    ConnectionPoolClient client;

    @Override
    protected void setUp() throws Exception {
        connProvider = new GenericConnectionProvider("127.0.0.1", 9090, 1000);
        connManager = new ConnectionManager();
        connManager.setConnectionProvider(connProvider);
        client = new ConnectionPoolClient(connManager);
    }

    @Override
    protected void tearDown() throws Exception {
        connProvider.close();
    }

    @Test
    public void test_echo() throws Throwable {
        client.ping();
    }
    
    @Test
    public void test_segment() throws Throwable {
        System.out.println(String.format("%d+%d=%d", 3, 2, client.add(3, 2)));
    }

}
