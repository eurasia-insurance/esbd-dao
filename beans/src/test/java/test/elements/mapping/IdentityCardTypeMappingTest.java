package test.elements.mapping;

import com.lapsa.insurance.elements.IdentityCardType;

import tech.lapsa.esbd.beans.dao.elements.mapping.IdentityCardTypeMapping;

public class IdentityCardTypeMappingTest extends AMappingTest<IdentityCardType> {

    private static final String DICT = "DOCUMENTS_TYPES";

    public IdentityCardTypeMappingTest() {
	super(IdentityCardTypeMapping.getInstance(), DICT, IdentityCardType.class);
    }
}
