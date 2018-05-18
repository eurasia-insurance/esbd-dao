package test.entities.complex;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static test.entities.complex.TestConstants.*;

import javax.inject.Inject;

import org.junit.Test;

import com.lapsa.insurance.elements.SubjectType;

import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.SubjectEntity;
import tech.lapsa.esbd.dao.entities.SubjectEntityService.SubjectEntityServiceLocal;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.kz.taxpayer.TaxpayerNumber;
import test.ArquillianBaseTestCase;

public class SubjectServiceTestCase extends ArquillianBaseTestCase {

    @Inject
    private SubjectEntityServiceLocal service;

    @Test
    public void testGetById() throws IllegalArgument {
	try {
	    for (int i = 0; i < VALID_SUBJECT_IDS.length; i++) {
		final int validSubjectId = VALID_SUBJECT_IDS[i];
		final SubjectType validSubjectType = VALID_SUBJECT_TYPES[i];
		final Class<?> validSubjectClass = VALID_SUBJECT_CLASSES[i];
		final SubjectEntity res = service.getById(validSubjectId);
		assertThat(res, allOf(not(nullValue()), instanceOf(validSubjectClass)));
		assertThat(res.getSubjectType(), allOf(not(nullValue()), is(validSubjectType)));
	    }
	} catch (final NotFound e) {
	    fail(e.getMessage());
	}
    }

    @Test(expected = NotFound.class)
    public void testGetById_NotFound() throws NotFound, IllegalArgument {
	service.getById(INVALID_SUBJECT_ID);
    }

    public static final TaxpayerNumber[] VALID_SUBJECT_ID_NUMBERS = new TaxpayerNumber[] {
	    TaxpayerNumber.of("930840000071"),
	    TaxpayerNumber.of("581114350286") };

    @Test
    public void testGetByIDNumber() throws IllegalArgument {
	try {
	    for (int i = 0; i < VALID_SUBJECT_ID_NUMBERS.length; i++) {
		final TaxpayerNumber subjecdIdNumber = VALID_SUBJECT_ID_NUMBERS[i];
		final SubjectType validSubjectType = VALID_SUBJECT_TYPES[i];
		final Class<?> validSubjectClass = VALID_SUBJECT_CLASSES[i];
		final SubjectEntity res = service.getFirstByIdNumber(subjecdIdNumber);
		assertThat(res, allOf(not(nullValue()), instanceOf(validSubjectClass)));
		assertThat(res.getSubjectType(), allOf(not(nullValue()),
			is(validSubjectType)));
		assertThat(res.getIdNumber(), allOf(not(nullValue()), is(subjecdIdNumber)));
	    }
	} catch (final NotFound e) {
	    fail(e.getMessage());
	}
    }

    public static final TaxpayerNumber INVALID_SUBJECT_ID_NUMBER = TaxpayerNumber.of("800225000001");

    @Test(expected = NotFound.class)
    public void testGetByIDNumber_NotFound() throws NotFound, IllegalArgument {
	service.getFirstByIdNumber(INVALID_SUBJECT_ID_NUMBER);
    }

}
