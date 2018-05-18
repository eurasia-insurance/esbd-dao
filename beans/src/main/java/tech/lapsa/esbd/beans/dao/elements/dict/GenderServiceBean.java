package tech.lapsa.esbd.beans.dao.elements.dict;

import javax.ejb.Singleton;

import com.lapsa.insurance.elements.Sex;

import tech.lapsa.esbd.beans.dao.elements.dict.mapping.SexMapping;
import tech.lapsa.esbd.dao.elements.dict.GenderService;
import tech.lapsa.esbd.dao.elements.dict.GenderService.GenderServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.GenderService.GenderServiceRemote;

@Singleton(name = GenderService.BEAN_NAME)
public class GenderServiceBean
	extends ADictElementsService<Sex>
	implements GenderServiceLocal, GenderServiceRemote {

    public GenderServiceBean() {
	super(GenderService.class, SexMapping.getInstance()::forId, Sex.class);
    }
}
