package tech.lapsa.esbd.beans.dao.dict;

import javax.ejb.Singleton;

import tech.lapsa.esbd.dao.dict.BranchEntityService;
import tech.lapsa.esbd.dao.dict.BranchEntityService.BranchEntityServiceLocal;
import tech.lapsa.esbd.dao.dict.BranchEntityService.BranchEntityServiceRemote;
import tech.lapsa.esbd.domain.dict.BranchEntity;

@Singleton(name = BranchEntityService.BEAN_NAME)
public class BranchEntityServiceBean
	extends ADictionaryEntityService<BranchEntity>
	implements BranchEntityServiceLocal, BranchEntityServiceRemote {

    private static final String DICT_NAME = "BRANCHES";

    public BranchEntityServiceBean() {
	super(BranchEntityService.class, DICT_NAME, BranchEntity::builder);
    }
}
