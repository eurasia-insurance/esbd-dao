package tech.lapsa.esbd.beans.dao;

import javax.ejb.EJB;

import tech.lapsa.esbd.connection.ConnectionPool;
import tech.lapsa.java.commons.logging.MyLogger;

public abstract class AService<T> {

    protected final MyLogger logger;
    protected final Class<T> domainClazz;

    // constructor

    protected AService(final Class<?> serviceClazz,
	    final Class<T> domainClazz) {
	assert serviceClazz != null;
	assert domainClazz != null;
	this.logger = MyLogger.newBuilder() //
		.withNameOf(serviceClazz) //
		.build();
	this.domainClazz = domainClazz;
    }

    // injected

    @EJB
    protected ConnectionPool pool;
}
