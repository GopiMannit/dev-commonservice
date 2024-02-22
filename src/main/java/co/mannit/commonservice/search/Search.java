package co.mannit.commonservice.search;

import lombok.Data;

@Data
public class Search<E> {

	private String name;
	private String type;
	private String operator;
	private E value;
	private String joinOp;
	
	public boolean isAndFilter() {
		return joinOp != null && "and".equalsIgnoreCase(joinOp);
	}
	
	public boolean isOrFilter() {
		return joinOp != null && "or".equalsIgnoreCase(joinOp);
	}
}
