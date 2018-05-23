package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.List;
import java.util.function.BiFunction;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import tech.lapsa.esbd.beans.dao.AOndemandComplexEntitiesService;
import tech.lapsa.esbd.beans.dao.entities.complex.converter.InsuranceAgentEntityEsbdConverterBean;
import tech.lapsa.esbd.connection.Connection;
import tech.lapsa.esbd.dao.entities.complex.InsuranceAgentEntityService;
import tech.lapsa.esbd.dao.entities.complex.InsuranceAgentEntityService.InsuranceAgentEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.InsuranceAgentEntityService.InsuranceAgentEntityServiceRemote;
import tech.lapsa.esbd.domain.complex.InsuranceAgentEntity;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfMIDDLEMAN;
import tech.lapsa.esbd.jaxws.wsimport.MIDDLEMAN;

@Stateless(name = InsuranceAgentEntityService.BEAN_NAME)
public class InsuranceAgentEntityServiceBean
	extends AOndemandComplexEntitiesService<InsuranceAgentEntity, MIDDLEMAN>
	implements InsuranceAgentEntityServiceLocal, InsuranceAgentEntityServiceRemote {

    // static finals

    private static final BiFunction<Connection, Integer, List<MIDDLEMAN>> GET_BY_ID_FUNCTION = (con, id) -> {
	final MIDDLEMAN search = new MIDDLEMAN();
	search.setMIDDLEMANID(id.intValue());
	final ArrayOfMIDDLEMAN models = con.getMiddlemenByKeyFields(search);
	return models.getMIDDLEMAN();
    };

    // constructor

    public InsuranceAgentEntityServiceBean() {
	super(InsuranceAgentEntityService.class, InsuranceAgentEntity.class, GET_BY_ID_FUNCTION);
    }

    // injected

    @EJB
    private InsuranceAgentEntityEsbdConverterBean converter;

    @Override
    protected InsuranceAgentEntityEsbdConverterBean getConverter() {
	return converter;
    }
}
