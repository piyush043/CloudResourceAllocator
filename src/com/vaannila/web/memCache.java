package com.vaannila.web;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

/**
 *
 * @author Stringpool
 */
public class memCache {

    //Define how much time the get() method will wait for data
    public final static int WAITTIME = 5;
    
    //Spymemcached 
    MemcachedClient memcacheClient;
    Future<Object> f = null;
    Object obj;

    public memCache() {
        try {
            /*Add your MemCached Server IP and Port NO, you can add more than one MemCached Server
            Eg. AddrUtil.getAddresses("server1:11211 server2:11211")
            */
            memcacheClient = new MemcachedClient(
                    AddrUtil.getAddresses("localhost:8081"));
        } catch (IOException ex) {
            Logger.getLogger(memCache.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    //method to add data in MemCached Server
    public OperationFuture<Boolean> addInMemCache(String key, int expTime, Object value) {
        OperationFuture<Boolean> status = memcacheClient.set(key, expTime, value);
        return status;

    }

      //method to retrieve data from MemCached Server
    public Object getFromMemCache(String key) {
        
        //Used for asynchronous request to get value
        f = memcacheClient.asyncGet(key);
        try {
            obj = f.get(WAITTIME, TimeUnit.SECONDS);
            
        } catch (TimeoutException e) {
            f.cancel(false);
            Logger.getLogger(memCache.class.getName()).log(Level.SEVERE, null, e);
        } catch (InterruptedException ex) {
            Logger.getLogger(memCache.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(memCache.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj;
    }

    
    //Close MemCached Client
    public void closeMemCacheClient() {
        memcacheClient.shutdown();
    }
}
