package com.scaler.mybatis.page;

import java.util.List;

public class Pagination<E> {
	protected static final int DEFAULT_PAGE_SIZE = 10;
	protected int pageNo = 1;
	protected int pageSize = DEFAULT_PAGE_SIZE;
	protected int totalRecords = -1;
	protected int totalPages = -1;

	protected boolean homePage = false;
	protected boolean endPage = false;
	protected boolean hasPrePage = false;
	protected boolean hasNextPage = false;



	protected List<E> list;

	protected int getOffset() {
		return (this.pageNo - 1) * this.pageSize + 1;
	}

	protected void afterQuery() {
		if (this.totalRecords != -1)
			this.totalPages = (int) (this.totalRecords % this.pageSize == 0 ? this.totalRecords
					/ this.pageSize
					: this.totalRecords / this.pageSize + 1);
		if (this.pageNo == 1)
			this.homePage = true;
		if (this.pageNo > 1)
			this.hasPrePage = true;
		if (this.pageNo < this.totalPages)
			this.hasNextPage = true;
		if (this.pageNo == this.totalPages)
			this.endPage = true;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public boolean homePage() {
		return homePage;
	}

	public void setHomePage(boolean homePage) {
		this.homePage = homePage;
	}

	public boolean endPage() {
		return endPage;
	}

	public void setEndPage(boolean endPage) {
		this.endPage = endPage;
	}

	public boolean isHasPrePage() {
		return hasPrePage;
	}

	public void setHasPrePage(boolean hasPrePage) {
		this.hasPrePage = hasPrePage;
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public List<E> getList() {
		return list;
	}

	public void setList(List<E> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder()
				.append("Pagination [pageNo=").append(pageNo)
				.append(", pageSize=").append(pageSize)
				.append(", totalRecords=")
				.append(totalRecords < 0 ? "null" : totalRecords)
				.append(", totalPage=")
				.append(totalPages < 0 ? "null" : totalPages)
				.append(", homePage=").append(homePage).append(", endPage=")
				.append(endPage).append(", hasPrePage=").append(hasPrePage)
				.append(", hasNextPage=").append(hasNextPage).append(", list=")
				.append(list == null ? "null" : list.toString()).append("]");
		return builder.toString();
	}

}
