package tech.lapsa.esbd.beans.dao.elements;

import javax.ejb.Singleton;

import com.lapsa.insurance.elements.Sex;

import tech.lapsa.esbd.beans.dao.elements.mapping.SexMapping;
import tech.lapsa.esbd.dao.elements.GenderService;
import tech.lapsa.esbd.dao.elements.GenderService.GenderServiceLocal;
import tech.lapsa.esbd.dao.elements.GenderService.GenderServiceRemote;

@Singleton(name = GenderService.BEAN_NAME)
public class GenderServiceBean extends AElementsService<Sex> implements GenderServiceLocal, GenderServiceRemote {

	public GenderServiceBean() {
		super(GenderService.class, SexMapping.getInstance()::forId, Sex.class);
	}
}
