package com.jy.helpring.web.vo;

/* 페이지 정보 */
public class PageVo {

    public int totalPage; // 전체 페이지 수
    public int startNumber; // 뷰에서 보여줄 페이지 숫자 목록의 첫번째 범위
    public int endNumber; // 뷰에서 보여줄 페이지 숫자 목록의 마지막 범위
    public boolean hasPrev, hasNext; // 다음 페이지, 이전 페이지 유무
//    public int prevIndex; // 이전 페이지 번호
//    public int nextIndex; // 다음 페이지 번호

    public PageVo(int totalPage, int startNumber, int endNumber, boolean hasPrev, boolean hasNext){
        this.totalPage = totalPage;
        this.startNumber = startNumber;
        this.endNumber = endNumber;
        this.hasPrev = hasPrev;
        this.hasNext = hasNext;
//        this.prevIndex = prevIndex;
//        this.nextIndex = nextIndex;
    }

}
