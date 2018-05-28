package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.List;
import java.util.function.Function;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import tech.lapsa.esbd.beans.dao.entities.AOndemandLoadedEntitiesService.AOndemandComplexViaIntermediateArrayService;
import tech.lapsa.esbd.beans.dao.entities.complex.converter.InsuranceAgentEntityEsbdConverterBean;
import tech.lapsa.esbd.dao.entities.complex.InsuranceAgentEntityService;
import tech.lapsa.esbd.dao.entities.complex.InsuranceAgentEntityService.InsuranceAgentEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.InsuranceAgentEntityService.InsuranceAgentEntityServiceRemote;
import tech.lapsa.esbd.domain.complex.InsuranceAgentEntity;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfMIDDLEMAN;
import tech.lapsa.esbd.jaxws.wsimport.MIDDLEMAN;

@Stateless(name = InsuranceAgentEntityService.BEAN_NAME)
public class InsuranceAgentEntityServiceBean
	extends AOndemandComplexViaIntermediateArrayService<InsuranceAgentEntity, MIDDLEMAN, ArrayOfMIDDLEMAN>
	implements InsuranceAgentEntityServiceLocal, InsuranceAgentEntityServiceRemote {

    // static finals

    private static final ESBDEntityLookupFunction<ArrayOfMIDDLEMAN> ESBD_LOOKUP_FUNCTION = (con, id) -> {
	final MIDDLEMAN param = new MIDDLEMAN();
	param.setMIDDLEMANID(id.intValue());
	return con.getMiddlemenByKeyFields(param);
    };

    private static final Function<ArrayOfMIDDLEMAN, List<MIDDLEMAN>> INTERMEDIATE_TO_LIST_FUNCTION = ArrayOfMIDDLEMAN::getMIDDLEMAN;

    // constructor

    protected InsuranceAgentEntityServiceBean() {
	super(InsuranceAgentEntityService.class, InsuranceAgentEntity.class, INTERMEDIATE_TO_LIST_FUNCTION, ESBD_LOOKUP_FUNCTION);
    }

    // injected

    @EJB
    private InsuranceAgentEntityEsbdConverterBean converter;

    @Override
    protected InsuranceAgentEntityEsbdConverterBean getConverter() {
	return converter;
    }
}
