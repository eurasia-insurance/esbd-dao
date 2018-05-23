package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.beans.dao.entities.complex.converter.SubjectEntityEsbdConverterBean;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.complex.SubjectEntityService;
import tech.lapsa.esbd.dao.entities.complex.SubjectEntityService.SubjectEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.SubjectEntityService.SubjectEntityServiceRemote;
import tech.lapsa.esbd.domain.complex.SubjectEntity;
import tech.lapsa.esbd.jaxws.wsimport.Client;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.kz.taxpayer.TaxpayerNumber;

@Stateless(name = SubjectEntityService.BEAN_NAME)
public class SubjectEntityServiceBean
	extends ASubjectEntityService<SubjectEntity>
	implements SubjectEntityServiceLocal, SubjectEntityServiceRemote {

    // static finals

    private static final BiFunction<Connection, Integer, Client> GET_BY_ID_FUNCTION = (con, id) -> con
	    .getClientByID(id.intValue());

    // constructor

    public SubjectEntityServiceBean() {
	super(SubjectEntityService.class, SubjectEntity.class, GET_BY_ID_FUNCTION);
    }

    // public

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<SubjectEntity> getByIdNumber(final TaxpayerNumber taxpayerNumber) throws IllegalArgument {
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
    public SubjectEntity getFirstByIdNumber(final TaxpayerNumber taxpayerNumber) throws IllegalArgument, NotFound {
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
    private SubjectEntityEsbdConverterBean converter;

    @Override
    protected SubjectEntityEsbdConverterBean getConverter() {
	return converter;
    }

    // private

    private List<SubjectEntity> _getByIdNumber(final TaxpayerNumber taxpayerNumber) throws IllegalArgumentException {
	return _getByIdNumber(taxpayerNumber, true, true);
    }

    private SubjectEntity _getFirstByIdNumber(final TaxpayerNumber taxpayerNumber)
	    throws IllegalArgumentException, NotFound {
	return MyOptionals.of(_getByIdNumber(taxpayerNumber))
		.map(List::stream)
		.flatMap(Stream::findFirst)
		.orElseThrow(MyExceptions.supplier(NotFound::new, "%1$s not found with BIN = %2$s",
			SubjectEntity.class.getSimpleName(), taxpayerNumber));
    }

}
