package com.jy.helpring.service.post;

import com.jy.helpring.domain.post.Post;
import com.jy.helpring.domain.post.PostCategoryRepository;
import com.jy.helpring.domain.post.PostRepository;
import com.jy.helpring.web.dto.post.PostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService{

    private static final int PAGE_POST_COUNT = 10; // 한 화면에 보일 컨텐츠 수

    private final PostRepository postRepository;


    /* 페이징 */
    @Override
    public Page<PostDto.ResponsePageDto> getPageList(Pageable pageable, int pageNo, Long category_id, String order_criteria) {

        /* 넘겨받은 order_criteria 를 이용해 내림차순하여 Pageable 객체 반환 */
        pageable = PageRequest.of(pageNo, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, order_criteria));
        /* category_id에 해당하는 post 페이지 객체 반환 */
        Page<Post> page = postRepository.findByPostCategory_Id(category_id, pageable);

        /* Dto로 변환 */
        Page<PostDto.ResponsePageDto> postPageList = page.map(
                post -> new PostDto.ResponsePageDto(
                        post.getId(),
                        post.getTitle(),
                        post.getMember().getNickname(),
                        post.getViewCount(),
                        post.getLikeCount(),
                        post.getCreatedDate(),
                        post.getPostCategory().getName())
        );

        return postPageList;
    }
}
