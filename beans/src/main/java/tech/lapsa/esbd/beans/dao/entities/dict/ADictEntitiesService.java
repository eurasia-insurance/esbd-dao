package tech.lapsa.esbd.beans.dao;

import java.util.function.Supplier;

import tech.lapsa.esbd.beans.dao.entities.complex.converter.AEsbdAttributeConverter;
import tech.lapsa.esbd.dao.IDictEntitiesService;
import tech.lapsa.esbd.domain.dict.ADictEntity;
import tech.lapsa.esbd.domain.dict.ADictEntity.ADictEntityBuilder;
import tech.lapsa.esbd.jaxws.wsimport.Item;
import tech.lapsa.java.commons.function.MyOptionals;

public abstract class ADictEntitiesService<DOMAIN extends ADictEntity, BT extends ADictEntityBuilder<DOMAIN, BT>>
	extends APreloadedComplexEntitiesService<DOMAIN, Item>
	implements IDictEntitiesService<DOMAIN> {

    // finals

    private final Supplier<BT> newBuilderSupplier;

    // constructor

    protected ADictEntitiesService(final Class<?> serviceClazz,
	    final Class<DOMAIN> domainClass,
	    final String dictionaryName,
	    final Supplier<BT> newBuilderSupplier) {
	super(serviceClazz, domainClass, con -> con.getItems(dictionaryName).getItem(), Item::getID);
	assert dictionaryName != null && !dictionaryName.isEmpty();
	assert newBuilderSupplier != null;
	this.newBuilderSupplier = newBuilderSupplier;
    }

    // converter

    private final AEsbdAttributeConverter<DOMAIN, Item> converter = new AEsbdAttributeConverter<DOMAIN, Item>() {

	@Override
	public Item convertToEsbdValue(DOMAIN source) throws EsbdConversionException {
	    // TODO Auto-generated method stub
	    return null;
	}

	@Override
	public DOMAIN convertToEntityAttribute(Item source) throws EsbdConversionException {
	    final BT builder = newBuilderSupplier.get();

	    MyOptionals.of(source.getID())
		    .ifPresent(builder::withId);

	    MyOptionals.of(source.getCode())
		    .ifPresent(builder::withCode);

	    MyOptionals.of(source.getName())
		    .ifPresent(builder::withName);

	    return builder.build();
	}
    };

    @Override
    protected AEsbdAttributeConverter<DOMAIN, Item> getConverter() {
	return converter;
    }

}
