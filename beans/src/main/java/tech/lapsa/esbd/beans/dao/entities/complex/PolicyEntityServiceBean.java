package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.beans.dao.AOndemandComplexEntitiesService;
import tech.lapsa.esbd.beans.dao.entities.complex.converter.PolicyEntityEsbdConverterBean;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.complex.PolicyEntityService;
import tech.lapsa.esbd.dao.entities.complex.PolicyEntityService.PolicyEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.PolicyEntityService.PolicyEntityServiceRemote;
import tech.lapsa.esbd.domain.complex.PolicyEntity;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfPolicy;
import tech.lapsa.esbd.jaxws.wsimport.Policy;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyCollectors;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.function.MyStrings;

@Stateless(name = PolicyEntityService.BEAN_NAME)
public class PolicyEntityServiceBean
	extends AOndemandComplexEntitiesService<PolicyEntity, Policy>
	implements PolicyEntityServiceLocal, PolicyEntityServiceRemote {

    // static finals

    private static final BiFunction<Connection, Integer, List<Policy>> GET_BY_ID_FUNCTION = (con, id) -> {
	final Policy source = con.getPolicyByID(id);
	return MyObjects.nullOrGet(source, Arrays::asList);
    };

    // constructor

    public PolicyEntityServiceBean() {
	super(PolicyEntityService.class, PolicyEntity.class, GET_BY_ID_FUNCTION);
    }

    // public

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public PolicyEntity getByNumber(final String number) throws NotFound, IllegalArgument {
	try {
	    return _getByNumber(number);
	} catch (final IllegalArgumentException e) {
	    throw new IllegalArgument(e);
	} catch (final EJBException e) {
	    throw e;
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<PolicyEntity> getByInternalNumber(final String internalNumber) throws IllegalArgument {
	try {
	    return _getByInternalNumber(internalNumber);
	} catch (final IllegalArgumentException e) {
	    throw new IllegalArgument(e);
	} catch (final EJBException e) {
	    throw e;
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    // injected

    @EJB
    private PolicyEntityEsbdConverterBean converter;

    @Override
    protected PolicyEntityEsbdConverterBean getConverter() {
	return converter;
    }

    // private

    private PolicyEntity _getByNumber(final String number) throws IllegalArgumentException, NotFound {
	MyStrings.requireNonEmpty(number, "number");

	final Policy source;
	try (Connection con = pool.getConnection()) {
	    source = con.getPolicyByGlobalID(number);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
	if (MyObjects.isNull(source))
	    throw new NotFound(PolicyEntity.class.getSimpleName() + " not found with NUMBER = '" + number + "'");

	return conversion(source);
    }

    private List<PolicyEntity> _getByInternalNumber(final String internalNumber) throws IllegalArgumentException {
	MyStrings.requireNonEmpty(internalNumber, "internalNumber");

	final ArrayOfPolicy policies;
	try (Connection con = pool.getConnection()) {
	    policies = con.getPoliciesByNumber(internalNumber);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}

	return MyOptionals.of(policies) //
		.map(ArrayOfPolicy::getPolicy) //
		.map(List::stream) //
		.orElseGet(Stream::empty) //
		.map(this::conversion) //
		.collect(MyCollectors.unmodifiableList());
    }
}
