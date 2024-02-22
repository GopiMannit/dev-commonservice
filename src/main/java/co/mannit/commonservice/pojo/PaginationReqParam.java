package co.mannit.commonservice.pojo;

public class PaginationReqParam extends BaseReqParam{

	private int page=1;
	private int sizePerPage=100;
	private String sortField;
	private String sortDirection;
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSizePerPage() {
		return sizePerPage;
	}
	public void setSizePerPage(int sizePerPage) {
		this.sizePerPage = sizePerPage;
	}
	public String getSortField() {
		return sortField;
	}
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}
	public String getSortDirection() {
		return sortDirection;
	}
	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}
	
	
	@Override
	public String toString() {
		return "PaginationReqParam [page=" + page + ", sizePerPage=" + sizePerPage + ", sortField=" + sortField
				+ ", sortDirection=" + sortDirection + ", toString()=" + super.toString() + "]";
	}
	
	
	
	
	
}
