package tech.lapsa.esbd.beans.dao.entities;

import java.util.List;
import java.util.stream.Stream;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.beans.dao.entities.EsbdAttributeConverter.EsbdConversionException;
import tech.lapsa.esbd.beans.dao.entities.converter.SubjectCompanyEntityConverterBean;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.SubjectCompanyEntity;
import tech.lapsa.esbd.dao.entities.SubjectCompanyEntityService;
import tech.lapsa.esbd.dao.entities.SubjectCompanyEntityService.SubjectCompanyEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.SubjectCompanyEntityService.SubjectCompanyEntityServiceRemote;
import tech.lapsa.esbd.dao.entities.SubjectPersonEntity;
import tech.lapsa.esbd.jaxws.wsimport.Client;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.logging.MyLogger;
import tech.lapsa.kz.taxpayer.TaxpayerNumber;

@Stateless(name = SubjectCompanyEntityService.BEAN_NAME)
public class SubjectCompanyEntityServiceBean
	extends ASubjectEntityService<SubjectCompanyEntity>
	implements SubjectCompanyEntityServiceLocal, SubjectCompanyEntityServiceRemote {

    private final MyLogger logger = MyLogger.newBuilder() //
	    .withNameOf(SubjectCompanyEntityService.class) //
	    .build();

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public SubjectCompanyEntity getById(final Integer id) throws NotFound, IllegalArgument {
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
    public List<SubjectCompanyEntity> getByIdNumber(final TaxpayerNumber taxpayerNumber) throws IllegalArgument {
	try {
	    return _getByIdNumber(taxpayerNumber);
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
    public SubjectCompanyEntity getFirstByIdNumber(final TaxpayerNumber taxpayerNumber)
	    throws IllegalArgument, NotFound {
	try {
	    return _getFirstByIdNumber(taxpayerNumber);
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

    private SubjectCompanyEntity _getById(final Integer id) throws IllegalArgumentException, NotFound {
	MyNumbers.requireNonZero(id, "id");
	try (Connection con = pool.getConnection()) {
	    final Client source = con.getClientByID(id.intValue());
	    if (source == null)
		throw new NotFound(SubjectCompanyEntity.class.getSimpleName() + " not found with ID = '" + id + "'");
	    final boolean isLegal = source.getNaturalPersonBool() == 0;
	    if (!isLegal)
		throw new NotFound(SubjectCompanyEntity.class.getSimpleName() + " not found with ID = '" + id
			+ "'. It was a " + SubjectPersonEntity.class.getName());
	    return conversion(source);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
    }

    private SubjectCompanyEntity _getFirstByIdNumber(final TaxpayerNumber taxpayerNumber)
	    throws IllegalArgumentException, NotFound {
	return MyOptionals.of(_getByIdNumber(taxpayerNumber))
		.map(List::stream)
		.flatMap(Stream::findFirst)
		.orElseThrow(MyExceptions.supplier(NotFound::new, "%1$s not found with BIN = %2$s",
			SubjectCompanyEntity.class.getSimpleName(), taxpayerNumber));
    }

    private List<SubjectCompanyEntity> _getByIdNumber(final TaxpayerNumber taxpayerNumber)
	    throws IllegalArgumentException {
	MyObjects.requireNonNull(taxpayerNumber, "taxpayerNumber"); //
	TaxpayerNumber.requireValid(taxpayerNumber);
	return _getByIdNumber(taxpayerNumber, false, true);
    }

    // converter

    @EJB
    private SubjectCompanyEntityConverterBean converter;

    @Override
    SubjectCompanyEntity conversion(final Client source) {
	try {
	    return converter.convertToEntityAttribute(source);
	} catch (EsbdConversionException e) {
	    throw Util.esbdConversionExceptionToEJBException(e);
	}
    }
}
