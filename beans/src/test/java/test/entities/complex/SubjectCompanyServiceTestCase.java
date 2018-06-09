package test.entities.complex;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.junit.Test;

import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.complex.SubjectCompanyEntityService.SubjectCompanyEntityServiceLocal;
import tech.lapsa.esbd.domain.complex.SubjectCompanyEntity;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.kz.taxpayer.TaxpayerNumber;
import test.ArquillianBaseTestCase;

public class SubjectCompanyServiceTestCase extends ArquillianBaseTestCase {

    @Inject
    private SubjectCompanyEntityServiceLocal service;

    private static final int[] VALID_IDS = new int[] { 1, 2 };

    @Test
    public void testGetById() throws IllegalArgument {
	try {
	    for (final int valid : VALID_IDS) {
		final SubjectCompanyEntity res = service.getById(valid);
		assertThat(res, not(nullValue()));
	    }
	} catch (final NotFound e) {
	    fail(e.getMessage());
	}
    }

    private static final int[] INVALID_IDS = new int[] { 100, -1 };

    @Test
    public void testGetById_NotFound() throws IllegalArgument {
	for (final int invalid : INVALID_IDS)
	    try {
		service.getById(invalid);
		fail("Not found exception Expected");
	    } catch (final NotFound e) {
	    }
    }

    public static final TaxpayerNumber[] VALID_BINS = new TaxpayerNumber[] {
	    TaxpayerNumber.of("930840000071") };

    @Test
    public void testGetByBIN() throws IllegalArgument {
	try {
	    for (final TaxpayerNumber valid : VALID_BINS) {
		final SubjectCompanyEntity res = service.getFirstByIdNumber(valid);
		assertThat(res, not(nullValue()));
	    }
	} catch (final NotFound e) {
	    fail(e.getMessage());
	}
    }

    public static final TaxpayerNumber[] INVALID_BINS = new TaxpayerNumber[] {
	    TaxpayerNumber.of("581114350286") };

    @Test
    public void testGetByBIN_NotFound() throws IllegalArgument {
	for (final TaxpayerNumber invalid : INVALID_BINS)
	    try {
		service.getFirstByIdNumber(invalid);
		fail("Not found exception Expected");
	    } catch (final NotFound e) {
	    }
    }
}
