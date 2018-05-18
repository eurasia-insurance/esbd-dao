package tech.lapsa.esbd.beans.dao.entities;

import javax.ejb.EJB;

import tech.lapsa.esbd.connection.ConnectionPool;
import tech.lapsa.esbd.dao.Domain;

public abstract class AEntityServiceBeanTemplate<T extends Domain, E> {

    @EJB
    ConnectionPool pool;

    abstract T conversion(E source);
}
