package com.ktds.dsquare.board.paging;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PagingService {

    public Pageable orderPage(String order, Pageable pageable){
        Pageable page;
        if(order == null || order.equals("create")){
            page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createDate").descending());
        } else if (order.equals("like")) {
            page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("likeCnt", "createDate").descending());
        } else {
            throw new RuntimeException("Invalid order. Using create || like");
        }
        return page;
    }

}
