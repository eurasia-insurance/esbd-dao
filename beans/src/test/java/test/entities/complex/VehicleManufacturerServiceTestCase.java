package test.entities.complex;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static test.entities.complex.TestConstants.*;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.VehicleManufacturerEntity;
import tech.lapsa.esbd.dao.entities.VehicleManufacturerEntityService.VehicleManufacturerEntityServiceLocal;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import test.ArquillianBaseTestCase;

public class VehicleManufacturerServiceTestCase extends ArquillianBaseTestCase {

    @Inject
    private VehicleManufacturerEntityServiceLocal service;

    @Test
    public void testGetById() throws IllegalArgument {
	try {
	    final VehicleManufacturerEntity res = service.getById(VALID_VEHICLE_MANUFACTURER_ID);
	    assertThat(res, not(nullValue()));
	} catch (final NotFound e) {
	    fail(e.getMessage());
	}
    }

    @Test(expected = NotFound.class)
    public void testGetById_NotFound() throws NotFound, IllegalArgument {
	service.getById(ININVALID_VEHICLE_MANUFACTURER_ID);
    }

    @Test
    public void testGetByName() throws IllegalArgument {
	final List<VehicleManufacturerEntity> list = service.getByName("INFINITI FX");
	assertThat(list, allOf(not(nullValue()), not(empty())));
	for (final VehicleManufacturerEntity e : list)
	    assertThat(e, not(nullValue()));
    }

    @Test
    public void testGetByName_empty() throws IllegalArgument {
	final List<VehicleManufacturerEntity> list = service.getByName(INVALID_VEHICLE_MANUFACTURER_NAME);
	// TODO DEBUG : Need to debug
	assertThat(list, allOf(not(nullValue()), not(empty())));
	for (final VehicleManufacturerEntity e : list)
	    assertThat(e, not(nullValue()));
    }

}
