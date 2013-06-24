/**
 * 
 */
package com.huaban.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import com.huaban.thrift.CalcService.Iface;

/**
 * @author matrix
 *
 */
public class CalcServer {
  public static Iface handler = new Iface() {
    
    @Override
    public void ping() throws TException {
        System.out.println("ping.");
    }
    
    @Override
    public int add(int a, int b) throws TException {
        return a + b;
    }
  };  

  public static CalcService.Processor<Iface> processor;

  public static void main(String [] args) {
    try {
      processor = new CalcService.Processor<Iface>(handler);

      Runnable simple = new Runnable() {
        public void run() { 
          simple(processor);
        }
      };      

      new Thread(simple).start();
    } catch (Exception x) {
      x.printStackTrace();
    }
  }

  public static void simple(CalcService.Processor<Iface> processor) {
    try {
      TServerTransport serverTransport = new TServerSocket(9090);
      TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));

      // Use this for a multithreaded server
      // TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

      System.out.println("Starting the simple server...");
      server.serve();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
