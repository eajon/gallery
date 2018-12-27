package cn.no7player.controller.pagination;


import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;

public class PageMeta implements Serializable {
    private static final long serialVersionUID = 1L;
    private PageData pageData;
    private List list;

    public PageMeta(PageInfo pageInfo) {
        this.pageData = new PageData();
        this.pageData.pageNum = pageInfo.getPageNum();
        this.pageData.pageSize = pageInfo.getPageSize();
        this.pageData.pages = pageInfo.getPages();
        this.list = pageInfo.getList();
        this.pageData.size = pageInfo.getSize();
        this.pageData.total = pageInfo.getTotal();
        this.pageData.isFirstPage = pageInfo.isIsFirstPage();
        this.pageData.isLastPage = pageInfo.isIsLastPage();

    }


    public List getList() {
        return this.list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public PageData getPageData() {
        return pageData;
    }

    public void setPageData(PageData pageData) {
        this.pageData = pageData;
    }


    private class PageData
    {
        private int pageNum;
        private int pageSize;
        private int size;
        private long total;
        private int pages;
        private boolean isFirstPage;
        private boolean isLastPage;


        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public long getTotal() {
            return total;
        }

        public void setTotal(long total) {
            this.total = total;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public boolean isFirstPage() {
            return isFirstPage;
        }

        public void setFirstPage(boolean firstPage) {
            isFirstPage = firstPage;
        }

        public boolean isLastPage() {
            return isLastPage;
        }

        public void setLastPage(boolean lastPage) {
            isLastPage = lastPage;
        }

    }
}

