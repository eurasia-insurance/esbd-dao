package tech.lapsa.esbd.beans.dao.entities.complex;

import javax.ejb.EJB;

import tech.lapsa.esbd.connection.ConnectionPool;
import tech.lapsa.esbd.dao.entities.AEntity;

public abstract class AComplexEntitiesService<T extends AEntity, E> {

    @EJB
    ConnectionPool pool;

    abstract T conversion(E source);
}
