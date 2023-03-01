package enums;

import lombok.Getter;

@Getter
public enum PlaneSize {

	SMALL("small"),
	MID("mid"),
	WIDE("wide-body"),
	JUMBO("jumbo");

	private String size;
	
	PlaneSize(String size) {
		this.size = size;
	}
	
	
}
