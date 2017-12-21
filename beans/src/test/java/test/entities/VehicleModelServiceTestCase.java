package test.entities;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static test.entities.TestConstants.*;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import tech.lapsa.insurance.esbd.NotFound;
import tech.lapsa.insurance.esbd.entities.VehicleManufacturerEntity;
import tech.lapsa.insurance.esbd.entities.VehicleModelEntity;
import tech.lapsa.insurance.esbd.entities.VehicleModelEntityService.VehicleModelEntityServiceLocal;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import test.ArquillianBaseTestCase;

public class VehicleModelServiceTestCase extends ArquillianBaseTestCase {

    @Inject
    private VehicleModelEntityServiceLocal service;

    @Test
    public void testGetById() throws IllegalArgument {
	try {
	    final VehicleModelEntity res = service.getById(VALID_VEHICLE_MODEL_ID);
	    assertThat(res, not(nullValue()));
	} catch (final NotFound e) {
	    fail(e.getMessage());
	}
    }

    @Test(expected = NotFound.class)
    public void testGetById_NotFound() throws NotFound, IllegalArgument {
	service.getById(ININVALID_VEHICLE_MODEL_ID);
    }

    @Test
    public void testGetByName() throws IllegalArgument {
	final List<VehicleModelEntity> list = service.getByName(VALID_VEHICLE_MODEL_NAME);
	assertThat(list, allOf(not(nullValue()), not(empty())));
	for (final VehicleModelEntity e : list)
	    assertThat(e, not(nullValue()));
    }

    @Test
    public void testGetByManufacturer() throws NotFound, IllegalArgument {
	final VehicleManufacturerEntity validManufacturer = new VehicleManufacturerEntity();
	validManufacturer.setId(VALID_VEHICLE_MANUFACTURER_ID);
	final List<VehicleModelEntity> list = service.getByManufacturer(validManufacturer);
	assertThat(list, allOf(not(nullValue()), not(empty())));
	for (final VehicleModelEntity e : list)
	    assertThat(e, not(nullValue()));
    }
}
