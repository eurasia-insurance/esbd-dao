package test.elements.dict;

import javax.inject.Inject;

import com.lapsa.insurance.elements.IdentityCardType;

import tech.lapsa.esbd.beans.dao.elements.dict.mapping.IdentityCardTypeMapping;
import tech.lapsa.esbd.dao.IElementsService;
import tech.lapsa.esbd.dao.elements.dict.IdentityCardTypeService.IdentityCardTypeServiceLocal;

public class IdentityCardTypeServiceTestCase extends AMappedElementTestCase<IdentityCardType> {

    @Inject
    private IdentityCardTypeServiceLocal service;

    public IdentityCardTypeServiceTestCase() {
	super(IdentityCardType.class, IdentityCardTypeMapping.getInstance(), "DOCUMENTS_TYPES", 99999);
    }

    @Override
    IElementsService<IdentityCardType> service() {
	return service;
    }
}