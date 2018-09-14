package test.caching.beans;

import java.util.UUID;

import tech.lapsa.esbd.domain.AEntity;

public class CachableEntity extends AEntity {

    private static final long serialVersionUID = 1L;

    private final UUID uuid = UUID.randomUUID();

    public CachableEntity(Integer id) {
	super(id);
    }

    public UUID getUuid() {
	return uuid;
    }
}
