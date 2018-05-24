package test.elements;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.junit.Test;

import com.lapsa.insurance.elements.InsuranceClassType;

import tech.lapsa.esbd.beans.dao.elements.dict.mapping.InsuranceClassTypeMapping;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.connection.ConnectionPool;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.elements.dict.InsuranceClassTypeService;
import tech.lapsa.esbd.dao.elements.dict.InsuranceClassTypeService.InsuranceClassTypeServiceLocal;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyStrings;

public class InsuranceClassTypeServiceTestCase extends AMappedElementTestCase<InsuranceClassType> {

    @Inject
    private InsuranceClassTypeServiceLocal service;

    public InsuranceClassTypeServiceTestCase() {
	super(InsuranceClassType.class, InsuranceClassTypeMapping.getInstance(), 99999);
    }

    @Override
    protected InsuranceClassTypeService service() {
	return service;
    }

    @Inject
    protected ConnectionPool pool;

    @Test
    @Override
    public void testEveryElementValueMappedToDictionary() throws ConnectionException {

	try (Connection con = pool.getConnection()) {
	    for (int i = -1000; i <= 1000; i++) {
		InsuranceClassType origin;
		InsuranceClassType resolved;

		try {
		    origin = service.getById(i);
		} catch (IllegalArgument e) {
		    throw e.getRuntime();
		} catch (NotFound e) {
		    origin = null;
		}

		final String code = con.getClassText(i);
		if (MyStrings.nonEmpty(code)) {
		    final String enumName = "CLASS_" + code;
		    try {
			resolved = InsuranceClassType.valueOf(enumName);
		    } catch (IllegalArgumentException e) {
			resolved = null;
		    }
		} else
		    resolved = null;

		assertThat(resolved, equalTo(origin));
	    }
	}
    }

}
