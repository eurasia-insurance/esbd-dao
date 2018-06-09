package test.resolver;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.junit.Test;

import com.lapsa.insurance.elements.InsuranceClassType;

import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.complex.SubjectPersonEntityService.SubjectPersonEntityServiceLocal;
import tech.lapsa.esbd.dao.resolver.InsuranceClassTypeResolverService.InsuranceClassTypeResolverServiceLocal;
import tech.lapsa.esbd.domain.complex.SubjectPersonEntity;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import test.ArquillianBaseTestCase;

public class InsuranceClassTypeResolverTestCase extends ArquillianBaseTestCase {

    @Inject
    private InsuranceClassTypeResolverServiceLocal service;

    @Inject
    private SubjectPersonEntityServiceLocal persons;

    private static final int VALID_SUBJECT_PERSON_ID = 14_132_412; // Evsyukovs ID
    private static final InsuranceClassType VALID_CLASS_TYPE_FOR_CLIENT = InsuranceClassType.CLASS_13; // Evsyukovs
												       // Class

    @Test
    public void testGetForSubject() throws IllegalArgument {
	try {
	    final SubjectPersonEntity e = persons.getById(VALID_SUBJECT_PERSON_ID);
	    final InsuranceClassType res = service.resolveForSubject(e);
	    assertThat(res, allOf(not(nullValue()), equalTo(VALID_CLASS_TYPE_FOR_CLIENT)));
	} catch (final NotFound e) {
	    fail(e.getMessage());
	}
    }
}
