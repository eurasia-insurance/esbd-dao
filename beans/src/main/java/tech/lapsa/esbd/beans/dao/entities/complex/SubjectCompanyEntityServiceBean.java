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

import tech.lapsa.esbd.beans.dao.entities.complex.converter.SubjectCompanyEntityConverterBean;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.complex.SubjectCompanyEntityService;
import tech.lapsa.esbd.dao.entities.complex.SubjectCompanyEntityService.SubjectCompanyEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.SubjectCompanyEntityService.SubjectCompanyEntityServiceRemote;
import tech.lapsa.esbd.domain.complex.SubjectCompanyEntity;
import tech.lapsa.esbd.jaxws.wsimport.Client;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.kz.taxpayer.TaxpayerNumber;

@Stateless(name = SubjectCompanyEntityService.BEAN_NAME)
public class SubjectCompanyEntityServiceBean
	extends ASubjectEntityService<SubjectCompanyEntity>
	implements SubjectCompanyEntityServiceLocal, SubjectCompanyEntityServiceRemote {

    // static finals

    private static final BiFunction<Connection, Integer, List<Client>> GET_BY_ID_FUNCTION = (con, id) -> {
	final Client source = con.getClientByID(id.intValue());
	if (source == null)
	    return null;
	final boolean isLegal = source.getNaturalPersonBool() == 0;
	if (!isLegal)
	    return null;
	return Arrays.asList(source);
    };

    // constructor

    public SubjectCompanyEntityServiceBean() {
	super(SubjectCompanyEntityService.class, SubjectCompanyEntity.class, GET_BY_ID_FUNCTION);
    }

    // public

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

    // injected

    @EJB
    private SubjectCompanyEntityConverterBean converter;

    @Override
    protected SubjectCompanyEntityConverterBean getConverter() {
	return converter;
    }

    // private

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
}
