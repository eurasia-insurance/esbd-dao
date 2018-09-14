package tech.lapsa.esbd.beans.dao.entities.complex;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import tech.lapsa.esbd.beans.dao.entities.complex.converter.SubjectCompanyEntityConverterBean;
import tech.lapsa.esbd.dao.entities.complex.SubjectCompanyEntityService;
import tech.lapsa.esbd.dao.entities.complex.SubjectCompanyEntityService.SubjectCompanyEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.SubjectCompanyEntityService.SubjectCompanyEntityServiceRemote;
import tech.lapsa.esbd.domain.complex.SubjectCompanyEntity;
import tech.lapsa.esbd.jaxws.wsimport.Client;

@Stateless(name = SubjectCompanyEntityService.BEAN_NAME)
public class SubjectCompanyEntityServiceBean
	extends ASubjectEntityService<SubjectCompanyEntity>
	implements SubjectCompanyEntityServiceLocal, SubjectCompanyEntityServiceRemote {

    // static finals

    private static final ESBDEntityLookupFunction<Client> ESBD_LOOKUP_FUNCTION = (con, id) -> {
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
	super(SubjectCompanyEntityService.class, SubjectCompanyEntity.class, ESBD_LOOKUP_FUNCTION, ClientType.COMPANY);
    }

    // injected

    @EJB
    private SubjectCompanyEntityConverterBean converter;

    @Override
    protected SubjectCompanyEntityConverterBean getConverter() {
	return converter;
    }
}
