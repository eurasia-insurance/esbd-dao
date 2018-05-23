package tech.lapsa.esbd.beans.dao.entities.dict;

import javax.ejb.Singleton;
import javax.ejb.Startup;

import tech.lapsa.esbd.beans.dao.ADictEntitiesService;
import tech.lapsa.esbd.dao.entities.dict.BranchEntityService;
import tech.lapsa.esbd.dao.entities.dict.BranchEntityService.BranchEntityServiceLocal;
import tech.lapsa.esbd.dao.entities.dict.BranchEntityService.BranchEntityServiceRemote;
import tech.lapsa.esbd.domain.dict.BranchEntity;
import tech.lapsa.esbd.domain.dict.BranchEntity.BranchEntityBuilder;

@Singleton(name = BranchEntityService.BEAN_NAME)
@Startup
public class BranchEntityServiceBean
	extends ADictEntitiesService<BranchEntity, BranchEntityBuilder>
	implements BranchEntityServiceLocal, BranchEntityServiceRemote {

    // static finals

    private static final String DICT_NAME = "BRANCHES";

    // constructor

    public BranchEntityServiceBean() {
	super(null, BranchEntity.class, DICT_NAME, BranchEntity::builder);
    }
}
