package test.entities.complex;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.junit.Test;

import com.lapsa.insurance.elements.SubjectType;

import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.complex.SubjectCompanyEntity;
import tech.lapsa.esbd.dao.entities.complex.SubjectEntity;
import tech.lapsa.esbd.dao.entities.complex.SubjectEntityService.SubjectEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.SubjectPersonEntity;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.kz.taxpayer.TaxpayerNumber;
import test.ArquillianBaseTestCase;

public class SubjectServiceTestCase extends ArquillianBaseTestCase {

    @Inject
    private SubjectEntityServiceLocal service;

    private static final int[] VALID_IDS = new int[] { 1, 100 };
    private static final SubjectType[] VALID_TYPES = new SubjectType[] {
	    SubjectType.COMPANY,
	    SubjectType.PERSON };
    private static final Class<?>[] VALID_CLASSES = new Class<?>[] {
	    SubjectCompanyEntity.class,
	    SubjectPersonEntity.class };

    @Test
    public void testGetById() throws IllegalArgument {
	try {
	    for (int i = 0; i < VALID_IDS.length; i++) {
		final int validSubjectId = VALID_IDS[i];
		final SubjectType validSubjectType = VALID_TYPES[i];
		final Class<?> validSubjectClass = VALID_CLASSES[i];
		final SubjectEntity res = service.getById(validSubjectId);
		assertThat(res, allOf(not(nullValue()), instanceOf(validSubjectClass)));
		assertThat(res.getSubjectType(), allOf(not(nullValue()), is(validSubjectType)));
	    }
	} catch (final NotFound e) {
	    fail(e.getMessage());
	}
    }

    private static final int INVALID_ID = -1;

    @Test(expected = NotFound.class)
    public void testGetById_NotFound() throws NotFound, IllegalArgument {
	service.getById(INVALID_ID);
    }

    private static final TaxpayerNumber[] VALID_SUBJECT_ID_NUMBERS = new TaxpayerNumber[] {
	    TaxpayerNumber.of("930840000071"),
	    TaxpayerNumber.of("581114350286") };

    @Test
    public void testGetByIDNumber() throws IllegalArgument {
	try {
	    for (int i = 0; i < VALID_SUBJECT_ID_NUMBERS.length; i++) {
		final TaxpayerNumber subjecdIdNumber = VALID_SUBJECT_ID_NUMBERS[i];
		final SubjectType validSubjectType = VALID_TYPES[i];
		final Class<?> validSubjectClass = VALID_CLASSES[i];
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

    private static final TaxpayerNumber INVALID_SUBJECT_ID_NUMBER = TaxpayerNumber.of("800225000001");

    @Test(expected = NotFound.class)
    public void testGetByIDNumber_NotFound() throws NotFound, IllegalArgument {
	service.getFirstByIdNumber(INVALID_SUBJECT_ID_NUMBER);
    }

}
