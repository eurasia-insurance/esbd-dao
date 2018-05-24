package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.function.BiFunction;
import java.util.function.Function;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import tech.lapsa.esbd.beans.dao.entities.complex.converter.SubjectPersonEntityConverterBean;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.dao.entities.complex.SubjectPersonEntityService;
import tech.lapsa.esbd.dao.entities.complex.SubjectPersonEntityService.SubjectPersonEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.SubjectPersonEntityService.SubjectPersonEntityServiceRemote;
import tech.lapsa.esbd.domain.complex.SubjectPersonEntity;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfClient;
import tech.lapsa.esbd.jaxws.wsimport.Client;
import tech.lapsa.kz.taxpayer.TaxpayerNumber;

@Stateless(name = SubjectPersonEntityService.BEAN_NAME)
public class SubjectPersonEntityServiceBean
	extends ASubjectEntityService<SubjectPersonEntity>
	implements SubjectPersonEntityServiceLocal, SubjectPersonEntityServiceRemote {

    // static finals

    private static final BiFunction<Connection, Integer, Client> GET_BY_ID_FUNCTION = (con, id) -> {
	final Client source = con.getClientByID(id.intValue());
	if (source == null)
	    return null;
	final boolean isPerson = source.getNaturalPersonBool() == 1;
	if (!isPerson)
	    return null;
	return source;
    };

    // constructor

    public SubjectPersonEntityServiceBean() {
	super(SubjectPersonEntityService.class, SubjectPersonEntity.class, GET_BY_ID_FUNCTION);
    }

    // private & protected

    @Override
    protected Function<Connection, ArrayOfClient> criteriaByIdNumber(final TaxpayerNumber idNumber) {
	assert idNumber != null;
	return con -> {
	    final Client search = new Client();
	    search.setIIN(idNumber.getNumber());
	    search.setNaturalPersonBool(1);
	    return con.getClientsByKeyFields(search);
	};
    }

    // injected

    @EJB
    private SubjectPersonEntityConverterBean converter;

    @Override
    protected SubjectPersonEntityConverterBean getConverter() {
	return converter;
    }
}
