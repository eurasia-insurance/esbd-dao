package tech.lapsa.esbd.beans.dao.entities.complex;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import tech.lapsa.esbd.beans.dao.entities.complex.converter.SubjectEntityEsbdConverterBean;
import tech.lapsa.esbd.dao.entities.complex.SubjectEntityService;
import tech.lapsa.esbd.dao.entities.complex.SubjectEntityService.SubjectEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.SubjectEntityService.SubjectEntityServiceRemote;
import tech.lapsa.esbd.domain.complex.SubjectEntity;
import tech.lapsa.esbd.jaxws.wsimport.Client;

@Stateless(name = SubjectEntityService.BEAN_NAME)
public class SubjectEntityServiceBean
	extends ASubjectEntityService<SubjectEntity>
	implements SubjectEntityServiceLocal, SubjectEntityServiceRemote {

    // static finals

    private static final ESBDEntityLookupFunction<Client> ESBD_LOOKUP_FUNCTION = (con, id) -> con
	    .getClientByID(id.intValue());

    // constructor

    public SubjectEntityServiceBean() {
	super(SubjectEntityService.class, SubjectEntity.class, ESBD_LOOKUP_FUNCTION, ClientType.BOTH);
    }

    // injected

    @EJB
    private SubjectEntityEsbdConverterBean converter;

    @Override
    protected SubjectEntityEsbdConverterBean getConverter() {
	return converter;
    }

}
