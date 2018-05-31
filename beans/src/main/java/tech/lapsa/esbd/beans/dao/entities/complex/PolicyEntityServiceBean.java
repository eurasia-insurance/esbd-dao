package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.List;
import java.util.function.Function;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.beans.dao.entities.AOndemandLoadedEntitiesService.AOndemandComplexViaSingleEntityService;
import tech.lapsa.esbd.beans.dao.entities.complex.converter.PolicyEntityEsbdConverterBean;
import tech.lapsa.esbd.dao.NotFound;
import tech.lapsa.esbd.dao.entities.complex.PolicyEntityService;
import tech.lapsa.esbd.dao.entities.complex.PolicyEntityService.PolicyEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.complex.PolicyEntityService.PolicyEntityServiceRemote;
import tech.lapsa.esbd.domain.complex.PolicyEntity;
import tech.lapsa.esbd.jaxws.wsimport.ArrayOfPolicy;
import tech.lapsa.esbd.jaxws.wsimport.Policy;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.function.MyStrings;

@Stateless(name = PolicyEntityService.BEAN_NAME)
public class PolicyEntityServiceBean
	extends AOndemandComplexViaSingleEntityService<PolicyEntity, Policy, ArrayOfPolicy>
	implements PolicyEntityServiceLocal, PolicyEntityServiceRemote {

    // static finals

    private static final ESBDEntityLookupFunction<Policy> ESBD_LOOKUP_FUNCTION = (con, id) -> con.getPolicyByID(id);
    private static final ESBDEntityStoreFunction<Policy> ESBD_STORE_FUNCTION = (con, e) -> con.setPolicy(e);

    private static final Function<ArrayOfPolicy, List<Policy>> INTERMEDIATE_TO_LIST_FUNCTION = ArrayOfPolicy::getPolicy;

    // constructor

    public PolicyEntityServiceBean() {
	super(PolicyEntityService.class,
		PolicyEntity.class,
		INTERMEDIATE_TO_LIST_FUNCTION,
		ESBD_LOOKUP_FUNCTION,
		ESBD_STORE_FUNCTION);
    }

    // public

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public PolicyEntity getByNumber(final String number) throws NotFound, IllegalArgument {
	MyStrings.requireNonEmpty(IllegalArgument::new, number, "number");
	return cacheControl.put(domainClazz, singleFromSingle(con -> con.getPolicyByGlobalID(number)));
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<PolicyEntity> getByInternalNumber(final String internalNumber) throws IllegalArgument {
	MyStrings.requireNonEmpty(IllegalArgument::new, internalNumber, "internalNumber");
	return cacheControl.put(domainClazz,
		manyFromIntermediateArray(con -> con.getPoliciesByNumber(internalNumber)));
    }

    // injected

    @EJB
    private PolicyEntityEsbdConverterBean converter;

    @Override
    protected PolicyEntityEsbdConverterBean getConverter() {
	return converter;
    }
}
