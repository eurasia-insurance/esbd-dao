package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.beans.dao.AOndemandComplexEntitiesService.AOndemandComplexIdBySingleService;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.complex.ISubjectEntitiesService.ISubjectEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.ISubjectEntitiesService.ISubjectEntityServiceRemote;
import tech.lapsa.esbd.domain.complex.SubjectEntity;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfClient;
import tech.lapsa.esbd.jaxws.wsimport.Client;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.kz.taxpayer.TaxpayerNumber;

public abstract class ASubjectEntityService<T extends SubjectEntity>
	extends AOndemandComplexIdBySingleService<T, Client, ArrayOfClient>
	implements ISubjectEntityServiceLocal<T>, ISubjectEntityServiceRemote<T> {

    private static final Function<ArrayOfClient, List<Client>> GET_LIST_FUNCTION = ArrayOfClient::getClient;

    // constructor

    protected ASubjectEntityService(final Class<?> serviceClazz,
	    final Class<T> domainClass,
	    final BiFunction<Connection, Integer, Client> getSingleById) {
	super(serviceClazz, domainClass, GET_LIST_FUNCTION, getSingleById);
    }

    // public

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<T> getByIdNumber(final TaxpayerNumber idNumber) throws IllegalArgument {
	MyObjects.requireNonNull(IllegalArgument::new, idNumber, "idNumber");
	return manyFromIntermediateArray(criteriaByIdNumber(idNumber));
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public T getFirstByIdNumber(final TaxpayerNumber idNumber) throws IllegalArgument, NotFound {
	MyObjects.requireNonNull(IllegalArgument::new, idNumber, "idNumber");
	return firstFromIntermediateArray(criteriaByIdNumber(idNumber));
    }

    // private & protected

    protected abstract Function<Connection, ArrayOfClient> criteriaByIdNumber(TaxpayerNumber idNumber);
}
