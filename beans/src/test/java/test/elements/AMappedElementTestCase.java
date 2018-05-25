package test.elements;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import tech.lapsa.esbd.beans.dao.elements.mapping.AMapping;
import tech.lapsa.esbd.connection.ConnectionException;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.elements.IElementsService;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import test.ArquillianBaseTestCase;

public abstract class AMappedElementTestCase<T extends Enum<T>> extends ArquillianBaseTestCase {

    protected final Class<T> clazz;
    protected final AMapping<Integer, T> mapper;
    protected final int invalidId;

    protected abstract IElementsService<T> service();

    public AMappedElementTestCase(final Class<T> clazz, AMapping<Integer, T> mapper, int invalidId) {
	this.clazz = clazz;
	this.mapper = mapper;
	this.invalidId = invalidId;
    }

    @Test
    public void testGetById() throws IllegalArgument {
	for (final int id : mapper.getAllIds())
	    try {
		final T res = service().getById(id);
		assertThat(res, allOf(not(nullValue()), equalTo(mapper.forId(id))));
	    } catch (final NotFound e) {
		fail(e.getMessage());
	    }
    }

    @Test(expected = NotFound.class)
    public void testGetById_NotFound() throws NotFound, IllegalArgument {
	service().getById(invalidId);
    }

    public abstract void testEveryElementValueMappedToDictionary() throws ConnectionException;
}
