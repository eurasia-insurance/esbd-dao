package tech.lapsa.esbd.beans.dao.elements.dict;

import javax.ejb.Singleton;

import com.lapsa.insurance.elements.IdentityCardType;

import tech.lapsa.esbd.beans.dao.elements.dict.mapping.IdentityCardTypeMapping;
import tech.lapsa.esbd.dao.elements.dict.IdentityCardTypeService;
import tech.lapsa.esbd.dao.elements.dict.IdentityCardTypeService.IdentityCardTypeServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.IdentityCardTypeService.IdentityCardTypeServiceRemote;

@Singleton(name = IdentityCardTypeService.BEAN_NAME)
public class IdentityCardTypeServiceBean
	extends ADictElementsService<IdentityCardType>
	implements IdentityCardTypeServiceLocal, IdentityCardTypeServiceRemote {

    public IdentityCardTypeServiceBean() {
	super(IdentityCardTypeService.class, IdentityCardTypeMapping.getInstance()::forId, IdentityCardType.class);
    }
}
