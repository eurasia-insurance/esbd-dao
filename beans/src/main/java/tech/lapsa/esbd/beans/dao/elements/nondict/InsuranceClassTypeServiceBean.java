package tech.lapsa.esbd.beans.dao.elements.nondict;

import java.time.LocalDate;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.lapsa.insurance.elements.InsuranceClassType;

import tech.lapsa.esbd.beans.dao.TemporalUtil;
import tech.lapsa.esbd.beans.dao.elements.dict.mapping.InsuranceClassTypeMapping;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.connection.ConnectionPool;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.elements.nondict.InsuranceClassTypeService;
import tech.lapsa.esbd.dao.elements.nondict.InsuranceClassTypeService.InsuranceClassTypeServiceLocal;
import tech.lapsa.esbd.dao.elements.nondict.InsuranceClassTypeService.InsuranceClassTypeServiceRemote;
import tech.lapsa.esbd.dao.entities.complex.SubjectPersonEntity;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.java.commons.logging.MyLogger;

@Stateless(name = InsuranceClassTypeService.BEAN_NAME)
public class InsuranceClassTypeServiceBean
	implements InsuranceClassTypeServiceLocal, InsuranceClassTypeServiceRemote {

    private final MyLogger logger = MyLogger.newBuilder() //
	    .withNameOf(InsuranceClassTypeService.class) //
	    .build();

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public InsuranceClassType getById(final Integer id) throws NotFound, IllegalArgument {
	try {
	    return _getById(id);
	} catch (final IllegalArgumentException e) {
	    throw new IllegalArgument(e);
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public InsuranceClassType getByCode(final String code) throws IllegalArgument, NotFound {
	try {
	    return _getByCode(code);
	} catch (final IllegalArgumentException e) {
	    throw new IllegalArgument(e);
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public InsuranceClassType getForSubject(final SubjectPersonEntity individual) throws IllegalArgument, NotFound {
	try {
	    return _getForSubject(individual);
	} catch (final IllegalArgumentException e) {
	    throw new IllegalArgument(e);
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public InsuranceClassType getForSubject(final SubjectPersonEntity subjectPerson, final LocalDate date)
	    throws NotFound, IllegalArgument {
	try {
	    return _getForSubject(subjectPerson, date);
	} catch (final IllegalArgumentException e) {
	    throw new IllegalArgument(e);
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    // PRIVATE

    @EJB
    private ConnectionPool pool;

    private InsuranceClassType _getById(final Integer id) throws IllegalArgumentException, NotFound {
	MyNumbers.requireNonZero(id, "id");
	try (Connection con = pool.getConnection()) {
	    final String classCode = con.getClassText(id);
	    if (classCode == null || classCode.trim().equals(""))
		throw new NotFound(InsuranceClassType.class.getSimpleName() + " not found with ID = '" + id + "'");
	    return _getByCode(classCode);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
    }

    private InsuranceClassType _getByCode(final String code) throws IllegalArgumentException, NotFound {
	MyStrings.requireNonEmpty(code, "code");
	final InsuranceClassType result = InsuranceClassTypeMapping.getInstance().forId(code);
	if (result == null)
	    throw new NotFound(InsuranceClassType.class.getSimpleName() + " not found with CODE = '" + code + "'");
	return result;
    }

    private InsuranceClassType _getForSubject(final SubjectPersonEntity individual)
	    throws IllegalArgumentException, NotFound {
	MyObjects.requireNonNull(individual, "individual");
	final LocalDate today = LocalDate.now();
	return _getForSubject(individual, today);
    }

    private InsuranceClassType _getForSubject(final SubjectPersonEntity subjectPerson, final LocalDate date)
	    throws IllegalArgumentException, NotFound {
	MyObjects.requireNonNull(subjectPerson, "subjectPerson");
	MyObjects.requireNonNull(subjectPerson.getId(), "subjectPerson.id");
	MyObjects.requireNonNull(date, "date");
	try (Connection con = pool.getConnection()) {
	    final String esbdDate = TemporalUtil.localDateToDate(date);
	    final int aClassID = con.getClassId(subjectPerson.getId(), esbdDate, 0);
	    if (aClassID == 0)
		throw new NotFound("WS-call getClassId returned zero value for CLIENT_ID = " + subjectPerson.getId()
			+ " and date = " + esbdDate);
	    try {
		return getById(aClassID);
	    } catch (final IllegalArgument e) {
		// it should not happens
		throw new EJBException(e.getMessage());
	    }
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public InsuranceClassType getDefault() {
	return InsuranceClassType.CLASS_3;
    }
}
