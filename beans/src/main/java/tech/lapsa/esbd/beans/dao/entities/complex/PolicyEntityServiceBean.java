package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.List;
import java.util.stream.Stream;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.beans.dao.entities.complex.converter.PolicyEntityEsbdConverterBean;
import tech.lapsa.esbd.beans.dao.entities.complex.converter.AEsbdAttributeConverter.EsbdConversionException;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.complex.PolicyEntity;
import tech.lapsa.esbd.dao.entities.complex.PolicyEntityService;
import tech.lapsa.esbd.dao.entities.complex.PolicyEntityService.PolicyEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.PolicyEntityService.PolicyEntityServiceRemote;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfPolicy;
import tech.lapsa.esbd.jaxws.wsimport.Policy;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyCollectors;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.java.commons.logging.MyLogger;

@Stateless(name = PolicyEntityService.BEAN_NAME)
public class PolicyEntityServiceBean
	extends AComplexEntitiesService<PolicyEntity, Policy>
	implements PolicyEntityServiceLocal, PolicyEntityServiceRemote {

    private final MyLogger logger = MyLogger.newBuilder() //
	    .withNameOf(PolicyEntityService.class) //
	    .build();

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public PolicyEntity getById(final Integer id) throws NotFound, IllegalArgument {
	try {
	    return _getById(id);
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

    // PRIVATE

    private PolicyEntity _getById(final Integer id) throws IllegalArgumentException, NotFound {
	MyNumbers.requireNonZero(id, "id");
	final Policy source;
	try (Connection con = pool.getConnection()) {
	    source = con.getPolicyByID(id);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
	if (source == null)
	    throw new NotFound(PolicyEntity.class.getSimpleName() + " not found with ID = '" + id + "'");

	return conversion(source);
    }

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

    // converter

    @EJB
    private PolicyEntityEsbdConverterBean converter;

    @Override
    PolicyEntity conversion(Policy source) {
	try {
	    return converter.convertToEntityAttribute(source);
	} catch (EsbdConversionException e) {
	    throw Util.esbdConversionExceptionToEJBException(e);
	}
    }
}
