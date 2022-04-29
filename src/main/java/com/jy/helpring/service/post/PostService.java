package com.jy.helpring.service.post;

import com.jy.helpring.web.dto.post.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {


    /* 게시물 리스트 페이징 */
    Page<PostDto.ResponsePageDto> getPageList(Pageable pageable, int pageNo, Long category_id, String order_criteria);

    /* 페이징 정보 반환 */

}
