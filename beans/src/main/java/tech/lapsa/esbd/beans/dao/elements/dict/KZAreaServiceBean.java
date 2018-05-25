package tech.lapsa.esbd.beans.dao.elements.dict;

import javax.ejb.Singleton;

import com.lapsa.kz.country.KZArea;

import tech.lapsa.esbd.beans.dao.elements.mapping.KZAreaMapping;
import tech.lapsa.esbd.dao.elements.dict.KZAreaService;
import tech.lapsa.esbd.dao.elements.dict.KZAreaService.KZAreaServiceLocal;
import tech.lapsa.esbd.dao.elements.dict.KZAreaService.KZAreaServiceRemote;

@Singleton(name = KZAreaService.BEAN_NAME)
public class KZAreaServiceBean
	extends ADictElementsService<KZArea>
	implements KZAreaServiceLocal, KZAreaServiceRemote {

    // constructor

    protected KZAreaServiceBean() {
	super(KZAreaService.class, KZArea.class, KZAreaMapping.getInstance()::forId);
    }
}
