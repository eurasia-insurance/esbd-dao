package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.beans.dao.entities.complex.converter.AEsbdAttributeConverter.EsbdConversionException;
import tech.lapsa.esbd.beans.dao.entities.complex.converter.InsuranceAgentEntityEsbdConverterBean;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.complex.InsuranceAgentEntityService;
import tech.lapsa.esbd.dao.entities.complex.InsuranceAgentEntityService.InsuranceAgentEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.InsuranceAgentEntityService.InsuranceAgentEntityServiceRemote;
import tech.lapsa.esbd.domain.complex.InsuranceAgentEntity;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfMIDDLEMAN;
import tech.lapsa.esbd.jaxws.wsimport.MIDDLEMAN;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyCollections;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.logging.MyLogger;

@Stateless(name = InsuranceAgentEntityService.BEAN_NAME)
public class InsuranceAgentEntityServiceBean
	extends AComplexEntitiesService<InsuranceAgentEntity, MIDDLEMAN>
	implements InsuranceAgentEntityServiceLocal, InsuranceAgentEntityServiceRemote {

    private final MyLogger logger = MyLogger.newBuilder() //
	    .withNameOf(InsuranceAgentEntityService.class) //
	    .build();

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public InsuranceAgentEntity getById(final Integer id) throws NotFound, IllegalArgument {
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

    // PRIVATE

    private InsuranceAgentEntity _getById(final Integer id) throws IllegalArgumentException, NotFound {
	MyNumbers.requireNonZero(id, "id");
	final ArrayOfMIDDLEMAN models;
	try (Connection con = pool.getConnection()) {
	    final MIDDLEMAN search = new MIDDLEMAN();
	    search.setMIDDLEMANID(id.intValue());
	    models = con.getMiddlemenByKeyFields(search);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}

	final List<MIDDLEMAN> list = MyOptionals.of(models) //
		.map(ArrayOfMIDDLEMAN::getMIDDLEMAN) //
		.filter(MyCollections::nonEmpty)
		.orElseThrow(MyExceptions.supplier(NotFound::new, "%1$s not found with ID = '%2$s'",
			InsuranceAgentEntity.class.getSimpleName(), id));
	final MIDDLEMAN source = Util.requireSingle(list, InsuranceAgentEntity.class, "ID", id);
	return conversion(source);
    }

    // converter

    @EJB
    private InsuranceAgentEntityEsbdConverterBean converter;

    @Override
    InsuranceAgentEntity conversion(MIDDLEMAN source) {
	try {
	    return converter.convertToEntityAttribute(source);
	} catch (EsbdConversionException e) {
	    throw Util.esbdConversionExceptionToEJBException(e);
	}
    }
}
