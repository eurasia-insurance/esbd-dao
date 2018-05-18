package test.elements;

import javax.inject.Inject;

import com.lapsa.insurance.elements.IdentityCardType;

import tech.lapsa.esbd.beans.dao.elements.mapping.IdentityCardTypeMapping;
import tech.lapsa.esbd.dao.elements.ElementsService;
import tech.lapsa.esbd.dao.elements.IdentityCardTypeService.IdentityCardTypeServiceLocal;

public class IdentityCardTypeServiceTestCase extends AMappedElementIntTestCase<IdentityCardType> {

    @Inject
    private IdentityCardTypeServiceLocal service;

    public IdentityCardTypeServiceTestCase() {
	super(IdentityCardType.class, IdentityCardTypeMapping.getInstance(), "DOCUMENTS_TYPES", 99999);
    }

    @Override
    ElementsService<IdentityCardType> service() {
	return service;
    }
}