package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.beans.dao.entities.AOndemandLoadedEntitiesService.AOndemandComplexViaSingleEntityService;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.complex.ISubjectEntitiesService.ISubjectEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.ISubjectEntitiesService.ISubjectEntityServiceRemote;
import tech.lapsa.esbd.domain.complex.SubjectEntity;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfClient;
import tech.lapsa.esbd.jaxws.wsimport.Client;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.kz.taxpayer.TaxpayerNumber;

public abstract class ASubjectEntityService<T extends SubjectEntity>
	extends AOndemandComplexViaSingleEntityService<T, Client, ArrayOfClient>
	implements ISubjectEntityServiceLocal<T>, ISubjectEntityServiceRemote<T> {

    // static finals

    private static final ESBDEntityStoreFunction<Client> ESBD_STORE_FUNCTION = (con, e) -> con.setClient(e);

    private static final Function<ArrayOfClient, List<Client>> INTERMEDIATE_TO_LIST_FUNCTION = ArrayOfClient::getClient;
    private static final Comparator<? super Client> CLIENT_BY_ID_COMPARATOR = (x1, x2) -> Integer.compare(x1.getID(),
	    x2.getID());

    // finals

    protected final ClientType clientType;

    // constructor

    protected ASubjectEntityService(final Class<?> serviceClazz,
	    final Class<T> domainClass,
	    final ESBDEntityLookupFunction<Client> getSingleById,
	    final ClientType clientType) {
	super(serviceClazz, domainClass, INTERMEDIATE_TO_LIST_FUNCTION, getSingleById, ESBD_STORE_FUNCTION);
	assert clientType != null;
	this.clientType = clientType;
    }

    // public

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<T> getByIdNumber(final TaxpayerNumber idNumber) throws IllegalArgument {
	MyObjects.requireNonNull(IllegalArgument::new, idNumber, "idNumber");
	return cacheControl.put(domainClazz, manyFromStream(criteriaByIdNumber(idNumber)));
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public T getFirstByIdNumber(final TaxpayerNumber idNumber) throws IllegalArgument, NotFound {
	MyObjects.requireNonNull(IllegalArgument::new, idNumber, "idNumber");
	return cacheControl.put(domainClazz, firstFromStream(criteriaByIdNumber(idNumber)));
    }

    // private & protected

    protected enum ClientType {
	PERSON(new int[] { 1 }), COMPANY(new int[] { 0 }), BOTH(new int[] { 0, 1 });

	private final int[] bools;

	private ClientType(final int[] bools) {
	    this.bools = bools;
	}
    }

    protected Function<Connection, Stream<Client>> criteriaByIdNumber(final TaxpayerNumber idNumber) {
	return con -> {
	    final Client search = new Client();
	    search.setIIN(idNumber.getNumber());

	    final int[] residents = new int[] { 0, 1 };

	    final Builder<Client> builder = Stream.builder();

	    for (int person : clientType.bools)
		for (int resident : residents) {
		    search.setRESIDENTBOOL(resident);
		    search.setNaturalPersonBool(person);
		    final ArrayOfClient intermediateArray = con.getClientsByKeyFields(search);
		    MyOptionals.of(intermediateArray)
			    .map(INTERMEDIATE_TO_LIST_FUNCTION)
			    .map(List::stream)
			    .orElseGet(Stream::empty)
			    .forEach(builder::accept);
		}
	    return builder.build()
		    .sorted(CLIENT_BY_ID_COMPARATOR);
	};
    }
}
