package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.beans.dao.entities.complex.converter.SubjectPersonEntityConverterBean;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.complex.SubjectPersonEntityService;
import tech.lapsa.esbd.dao.entities.complex.SubjectPersonEntityService.SubjectPersonEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.SubjectPersonEntityService.SubjectPersonEntityServiceRemote;
import tech.lapsa.esbd.domain.complex.SubjectPersonEntity;
import tech.lapsa.esbd.jaxws.wsimport.Client;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.kz.taxpayer.TaxpayerNumber;

@Stateless(name = SubjectPersonEntityService.BEAN_NAME)
public class SubjectPersonEntityServiceBean
	extends ASubjectEntityService<SubjectPersonEntity>
	implements SubjectPersonEntityServiceLocal, SubjectPersonEntityServiceRemote {

    // static finals

    private static final BiFunction<Connection, Integer, Client> GET_BY_ID_FUNCTION = (con, id) -> {
	final Client source = con.getClientByID(id.intValue());
	if (source == null)
	    return null;
	final boolean isPerson = source.getNaturalPersonBool() == 1;
	if (!isPerson)
	    return null;
	return source;
    };

    // constructor

    public SubjectPersonEntityServiceBean() {
	super(SubjectPersonEntityService.class, SubjectPersonEntity.class, GET_BY_ID_FUNCTION);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<SubjectPersonEntity> getByIdNumber(final TaxpayerNumber taxpayerNumber) throws IllegalArgument {
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
    public SubjectPersonEntity getFirstByIdNumber(final TaxpayerNumber taxpayerNumber)
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

    // injected

    @EJB
    private SubjectPersonEntityConverterBean converter;

    @Override
    protected SubjectPersonEntityConverterBean getConverter() {
	return converter;
    }

    // private

    private List<SubjectPersonEntity> _getByIdNumber(final TaxpayerNumber taxpayerNumber)
	    throws IllegalArgumentException {
	return _getByIdNumber(taxpayerNumber, true, false);
    }

    private SubjectPersonEntity _getFirstByIdNumber(final TaxpayerNumber taxpayerNumber)
	    throws IllegalArgumentException, NotFound {
	return MyOptionals.of(_getByIdNumber(taxpayerNumber))
		.map(List::stream)
		.flatMap(Stream::findFirst)
		.orElseThrow(MyExceptions.supplier(NotFound::new, "%1$s not found with IIN = %2$s",
			SubjectPersonEntity.class.getSimpleName(), taxpayerNumber));
    }
}
