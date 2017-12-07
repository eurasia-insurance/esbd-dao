package tech.lapsa.insurance.esbd.dict;

import tech.lapsa.patterns.domain.HashCodePrime;
import tech.lapsa.patterns.domain.Pojo;

@HashCodePrime(3)
public class BranchEntity extends Pojo implements DictionaryEntity<Integer> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String code;
    private String name;

    // GENERATED

    @Override
    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }
}
