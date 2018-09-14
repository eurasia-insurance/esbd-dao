package test.elements.dict;

import javax.inject.Inject;

import com.lapsa.insurance.elements.IdentityCardType;

import tech.lapsa.esbd.beans.dao.elements.mapping.IdentityCardTypeMapping;
import tech.lapsa.esbd.dao.elements.dict.IdentityCardTypeService;
import tech.lapsa.esbd.dao.elements.dict.IdentityCardTypeService.IdentityCardTypeServiceLocal;

public class IdentityCardTypeServiceTestCase extends AMappedElementDictionaryTestCase<IdentityCardType> {

    @Inject
    private IdentityCardTypeServiceLocal service;

    public IdentityCardTypeServiceTestCase() {
	super(IdentityCardType.class, IdentityCardTypeMapping.getInstance(), "DOCUMENTS_TYPES", 99999);
    }

    @Override
    protected IdentityCardTypeService service() {
	return service;
    }
}