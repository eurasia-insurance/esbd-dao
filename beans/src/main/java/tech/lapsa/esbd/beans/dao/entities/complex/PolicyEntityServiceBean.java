package tech.lapsa.esbd.beans.dao.entities.complex;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import tech.lapsa.esbd.beans.dao.AOndemandComplexEntitiesService;
import tech.lapsa.esbd.beans.dao.entities.complex.converter.PolicyEntityEsbdConverterBean;
import tech.lapsa.esbd.connection.Connection;
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
	extends AOndemandComplexEntitiesService<PolicyEntity, Policy, ArrayOfPolicy>
	implements PolicyEntityServiceLocal, PolicyEntityServiceRemote {

    // static finals

    private static final BiFunction<Connection, Integer, Policy> GET_BY_ID_FUNCTION = (con, id) -> con
	    .getPolicyByID(id);
    private static final Function<ArrayOfPolicy, List<Policy>> GET_LIST_FUNCTION = ArrayOfPolicy::getPolicy;

    // constructor

    public PolicyEntityServiceBean() {
	super(PolicyEntityService.class, PolicyEntity.class, GET_LIST_FUNCTION, false, null, GET_BY_ID_FUNCTION);
    }

    // public

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public PolicyEntity getByNumber(final String number) throws NotFound, IllegalArgument {
	MyStrings.requireNonEmpty(IllegalArgument::new, number, "number");
	return getOneFromSingle(con -> con.getPolicyByGlobalID(number));
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<PolicyEntity> getByInternalNumber(final String internalNumber) throws IllegalArgument {
	MyStrings.requireNonEmpty(IllegalArgument::new, internalNumber, "internalNumber");
	return getManyFromIntermediate(con -> con.getPoliciesByNumber(internalNumber));
    }

    // injected

    @EJB
    private PolicyEntityEsbdConverterBean converter;

    @Override
    protected PolicyEntityEsbdConverterBean getConverter() {
	return converter;
    }
}
