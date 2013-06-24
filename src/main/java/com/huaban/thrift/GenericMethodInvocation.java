package com.huaban.thrift;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;

public class GenericMethodInvocation implements MethodInvocation {
    private Method method;
    private Object[] args;
    private Object me;
    private AccessibleObject accessibleObject;
    
    
    public GenericMethodInvocation(Object obj, Method method, Object[] args) {
        this.me = obj;
        this.method = method;
        this.args = args;
    }
    
    public GenericMethodInvocation(Object obj, Method method, Object[] args, AccessibleObject accessibleObject) {
        this(obj, method, args);
        this.accessibleObject = accessibleObject;	
    }
    

    @Override
    public Object[] getArguments() {
        return args;
    }

    @Override
    public Object proceed() throws Throwable {
        return this.method.invoke(me, args);
    }

    @Override
    public Object getThis() {
        return me;
    }

    @Override
    public AccessibleObject getStaticPart() {
        return this.accessibleObject;
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
