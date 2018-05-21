package test.entities.complex;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.junit.Test;

import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.complex.SubjectPersonEntity;
import tech.lapsa.esbd.dao.entities.complex.SubjectPersonEntityService.SubjectPersonEntityServiceLocal;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.kz.taxpayer.TaxpayerNumber;
import test.ArquillianBaseTestCase;

public class SubjectPersonServiceTestCase extends ArquillianBaseTestCase {

    @Inject
    private SubjectPersonEntityServiceLocal service;

    private static final int[] VALID_IDS = new int[] { 100, 14132412 };

    @Test
    public void testGetById() throws IllegalArgument {
	try {
	    for (final int valid : VALID_IDS) {
		final SubjectPersonEntity res = service.getById(valid);
		assertThat(res, not(nullValue()));
	    }
	} catch (final NotFound e) {
	    fail(e.getMessage());
	}
    }

    private static final int[] INVALID_IDS = new int[] { 1, 2, -1 };

    @Test
    public void testGetById_NotFound() throws IllegalArgument {
	for (final int invalid : INVALID_IDS)
	    try {
		service.getById(invalid);
		fail("Not found exception Expected");
	    } catch (final NotFound e) {
	    }
    }

    private static final TaxpayerNumber[] VALID_IINS = new TaxpayerNumber[] {
	    TaxpayerNumber.of("581114350286"), TaxpayerNumber.of("870622300359") };

    @Test
    public void testGetByIIN() throws IllegalArgument {
	try {
	    for (final TaxpayerNumber valid : VALID_IINS) {
		final SubjectPersonEntity res = service.getFirstByIdNumber(valid);
		assertThat(res, not(nullValue()));
	    }
	} catch (final NotFound e) {
	    fail(e.getMessage());
	}
    }

    private static final TaxpayerNumber[] INVALID_IINS = new TaxpayerNumber[] {
	    TaxpayerNumber.of("930840000071") };

    @Test
    public void testGetByIIN_NotFound() throws IllegalArgument {
	for (final TaxpayerNumber invalid : INVALID_IINS)
	    try {
		service.getFirstByIdNumber(invalid);
		fail("Not found exception Expected");
	    } catch (final NotFound e) {
	    }
    }

}
