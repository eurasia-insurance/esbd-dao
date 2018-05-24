package tech.lapsa.esbd.beans.dao.resolver;

import java.time.LocalDate;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.lapsa.insurance.elements.InsuranceClassType;

import tech.lapsa.esbd.beans.dao.AService;
import tech.lapsa.esbd.beans.dao.TemporalUtil;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.elements.dict.InsuranceClassTypeService.InsuranceClassTypeServiceLocal;
import tech.lapsa.esbd.dao.resolver.InsuranceClassTypeResolverService;
import tech.lapsa.esbd.dao.resolver.InsuranceClassTypeResolverService.InsuranceClassTypeResolverServiceLocal;
import tech.lapsa.esbd.dao.resolver.InsuranceClassTypeResolverService.InsuranceClassTypeResolverServiceRemote;
import tech.lapsa.esbd.domain.complex.SubjectPersonEntity;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyObjects;

@Stateless(name = InsuranceClassTypeResolverService.BEAN_NAME)
public class InsuranceClassTypeResolverServiceBean
	extends AService<InsuranceClassType>
	implements InsuranceClassTypeResolverServiceLocal, InsuranceClassTypeResolverServiceRemote {

    public InsuranceClassTypeResolverServiceBean() {
	super(InsuranceClassTypeResolverService.class, InsuranceClassType.class);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public InsuranceClassType resolveForSubject(final SubjectPersonEntity individual) throws IllegalArgument, NotFound {
	try {
	    return _resolveForSubject(individual);
	} catch (final IllegalArgumentException e) {
	    throw new IllegalArgument(e);
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public InsuranceClassType resolveForSubject(final SubjectPersonEntity subjectPerson, final LocalDate date)
	    throws NotFound, IllegalArgument {
	try {
	    return _resolveForSubject(subjectPerson, date);
	} catch (final IllegalArgumentException e) {
	    throw new IllegalArgument(e);
	} catch (final RuntimeException e) {
	    logger.WARN.log(e);
	    throw new EJBException(e.getMessage());
	}
    }

    // PRIVATE

    @EJB
    private InsuranceClassTypeServiceLocal classTypes;

    private InsuranceClassType _resolveForSubject(final SubjectPersonEntity individual)
	    throws IllegalArgumentException, NotFound {
	MyObjects.requireNonNull(individual, "individual");
	final LocalDate today = LocalDate.now();
	return _resolveForSubject(individual, today);
    }

    private InsuranceClassType _resolveForSubject(final SubjectPersonEntity subjectPerson, final LocalDate date)
	    throws IllegalArgumentException, NotFound {
	MyObjects.requireNonNull(subjectPerson, "subjectPerson");
	MyObjects.requireNonNull(subjectPerson.getId(), "subjectPerson.id");
	MyObjects.requireNonNull(date, "date");

	final String esbdDate = TemporalUtil.localDateToDate(date);
	final int aClassID;
	try (Connection con = pool.getConnection()) {
	    aClassID = con.getClassId(subjectPerson.getId(), esbdDate, 0);
	} catch (ConnectionException e) {
	    throw new IllegalStateException(e.getMessage());
	}

	if (aClassID == 0)
	    throw new NotFound("WS-call getClassId returned zero value for CLIENT_ID = " + subjectPerson.getId()
		    + " and date = " + esbdDate);
	try {
	    return classTypes.getById(aClassID);
	} catch (final IllegalArgument e) {
	    // it should not happens
	    throw new EJBException(e.getMessage());
	}

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public InsuranceClassType getDefault() {
	return InsuranceClassType.CLASS_3;
    }
}
