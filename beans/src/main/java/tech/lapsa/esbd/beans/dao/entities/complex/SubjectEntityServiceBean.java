package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.function.BiFunction;
import java.util.function.Function;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import tech.lapsa.esbd.beans.dao.entities.complex.converter.SubjectEntityEsbdConverterBean;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.dao.entities.complex.SubjectEntityService;
import tech.lapsa.esbd.dao.entities.complex.SubjectEntityService.SubjectEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.SubjectEntityService.SubjectEntityServiceRemote;
import tech.lapsa.esbd.domain.complex.SubjectEntity;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfClient;
import tech.lapsa.esbd.jaxws.wsimport.Client;
import tech.lapsa.kz.taxpayer.TaxpayerNumber;

@Stateless(name = SubjectEntityService.BEAN_NAME)
public class SubjectEntityServiceBean
	extends ASubjectEntityService<SubjectEntity>
	implements SubjectEntityServiceLocal, SubjectEntityServiceRemote {

    // static finals

    private static final BiFunction<Connection, Integer, Client> GET_BY_ID_FUNCTION = (con, id) -> con
	    .getClientByID(id.intValue());

    // constructor

    public SubjectEntityServiceBean() {
	super(SubjectEntityService.class, SubjectEntity.class, GET_BY_ID_FUNCTION);
    }

    // private & protected

    @Override
    protected Function<Connection, ArrayOfClient> criteriaByIdNumber(final TaxpayerNumber idNumber) {
	return con -> {
	    final Client search = new Client();
	    search.setIIN(idNumber.getNumber());
	    return con.getClientsByKeyFields(search);
	};
    }

    // injected

    @EJB
    private SubjectEntityEsbdConverterBean converter;

    @Override
    protected SubjectEntityEsbdConverterBean getConverter() {
	return converter;
    }
}
