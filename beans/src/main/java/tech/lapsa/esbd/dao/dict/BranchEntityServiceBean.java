package tech.lapsa.esbd.dao.dict;

import javax.ejb.Singleton;

import tech.lapsa.esbd.dao.dict.BranchEntityService.BranchEntityServiceLocal;
import tech.lapsa.esbd.dao.dict.BranchEntityService.BranchEntityServiceRemote;

@Singleton(name = BranchEntityService.BEAN_NAME)
public class BranchEntityServiceBean
	extends ADictionaryEntityService<BranchEntity>
	implements BranchEntityServiceLocal, BranchEntityServiceRemote {

    private static final String DICT_NAME = "BRANCHES";

    public BranchEntityServiceBean() {
	super(BranchEntityService.class, DICT_NAME, BranchEntity::new);
    }
}
