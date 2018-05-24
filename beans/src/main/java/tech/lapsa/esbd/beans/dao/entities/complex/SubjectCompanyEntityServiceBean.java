package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.function.BiFunction;
import java.util.function.Function;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import tech.lapsa.esbd.beans.dao.entities.complex.converter.SubjectCompanyEntityConverterBean;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.dao.entities.complex.SubjectCompanyEntityService;
import tech.lapsa.esbd.dao.entities.complex.SubjectCompanyEntityService.SubjectCompanyEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.SubjectCompanyEntityService.SubjectCompanyEntityServiceRemote;
import tech.lapsa.esbd.domain.complex.SubjectCompanyEntity;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfClient;
import tech.lapsa.esbd.jaxws.wsimport.Client;
import tech.lapsa.kz.taxpayer.TaxpayerNumber;

@Stateless(name = SubjectCompanyEntityService.BEAN_NAME)
public class SubjectCompanyEntityServiceBean
	extends ASubjectEntityService<SubjectCompanyEntity>
	implements SubjectCompanyEntityServiceLocal, SubjectCompanyEntityServiceRemote {

    // static finals

    private static final BiFunction<Connection, Integer, Client> GET_BY_ID_FUNCTION = (con, id) -> {
	final Client source = con.getClientByID(id.intValue());
	if (source == null)
	    return null;
	final boolean isLegal = source.getNaturalPersonBool() == 0;
	if (!isLegal)
	    return null;
	return source;
    };

    // constructor

    public SubjectCompanyEntityServiceBean() {
	super(SubjectCompanyEntityService.class, SubjectCompanyEntity.class, GET_BY_ID_FUNCTION);
    }

    // private & protected

    @Override
    protected Function<Connection, ArrayOfClient> criteriaByIdNumber(final TaxpayerNumber idNumber) {
	assert idNumber != null;
	return con -> {
	    final Client search = new Client();
	    search.setIIN(idNumber.getNumber());
	    search.setNaturalPersonBool(0);
	    return con.getClientsByKeyFields(search);
	};
    }

    // injected

    @EJB
    private SubjectCompanyEntityConverterBean converter;

    @Override
    protected SubjectCompanyEntityConverterBean getConverter() {
	return converter;
    }
}
