package com.jy.helpring.service.post;

import com.jy.helpring.domain.post.Post;
import com.jy.helpring.domain.post.PostCategoryRepository;
import com.jy.helpring.domain.post.PostRepository;
import com.jy.helpring.web.dto.post.PostDto;
import com.jy.helpring.web.vo.PageVo;
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


    /* 게시물 전체 리스트 페이징 */
    @Override
    public Page<PostDto.ResponsePageDto> getPageList(Pageable pageable, int pageNo, String category_name, String order_criteria) {

        /* 넘겨받은 order_criteria 를 이용해 내림차순하여 Pageable 객체 반환 */
        pageable = PageRequest.of(pageNo, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, order_criteria));
        /* category_name에 해당하는 post 페이지 객체 반환 */
        Page<Post> page = postRepository.findByPostCategory_Name(category_name, pageable);

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


    /* 게시물 검색 리스트 페이징 */
    @Override
    public Page<PostDto.ResponsePageDto> searchPageList(Pageable pageable, int pageNo, String keyword, String category_name, String order_criteria) {

        /* 넘겨받은 order_criteria 를 이용해 내림차순하여 Pageable 객체 반환 */
        pageable = PageRequest.of(pageNo, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, order_criteria));

        /* category_name에 해당하면서 keyword를 포함하는 post 페이지 객체 반환 */
        Page<Post> page = postRepository.findByPostCategory_NameContainingIgnoreCase(category_name, keyword, pageable);

        /* Dto로 변환 */
        Page<PostDto.ResponsePageDto> postPageList = page.map(
                post -> new PostDto.ResponsePageDto(
                        post.getId(),
                        post.getTitle(),
                        post.getMember().getNickname(),
                        post.getViewCount(),
                        post.getLikeCount(),
                        post.getCreatedDate(),
                        post.getPostCategory().getName()
                )
        );

        return postPageList;

    }

    /** 페이징 정보 반환 **/
    @Override
    public PageVo getPageInfo(Page<PostDto.ResponsePageDto> postPageList, int pageNo) {
        int totalPage = postPageList.getTotalPages();

        // 현재 페이지를 통해 현재 페이지 그룹의 시작 페이지를 구함
        int startNumber = (int)((Math.floor(pageNo/PAGE_POST_COUNT)*PAGE_POST_COUNT)+1 <= totalPage ? (Math.floor(pageNo/PAGE_POST_COUNT)*PAGE_POST_COUNT)+1 : totalPage);
        // 전체 페이지 수와 현재 페이지 그룹의 시작 페이지를 통해 현재 페이지 그룹의 마지막 페이지를 구함
        int endNumber = (startNumber + PAGE_POST_COUNT-1 < totalPage ? startNumber + PAGE_POST_COUNT-1 : totalPage);
        boolean hasPrev = postPageList.hasPrevious();
        boolean hasNext = postPageList.hasNext();

        return new PageVo(totalPage, startNumber, endNumber, hasPrev, hasNext);
    }

    /* post_id 로 Post 객체를 찾아 PostDto.ResponseDto로 반환 */
    @Override
    public PostDto.ResponseDto findById(Long post_id) {
        Post post = postRepository.findById(post_id).orElseThrow(() ->
                            new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        return new PostDto.ResponseDto(post);
    }

    @Override
    public Page<PostDto.ResponsePageDto> getMyPostPageList(Pageable pageable, int pageNo, Long member_id, String category_name) {
        /* 넘겨받은 order_criteria 를 이용해 내림차순하여 Pageable 객체 반환 */
        pageable = PageRequest.of(pageNo, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "id"));

        /* category_name에 해당하면서 member_id에 해당하는 post 페이지 객체 반환 */
        Page<Post> page = postRepository.findByPostCategory_NameAndByMember_Id(category_name, member_id, pageable);

        /* Dto로 변환 */
        Page<PostDto.ResponsePageDto> postPageList = page.map(
                post -> new PostDto.ResponsePageDto(
                        post.getId(),
                        post.getTitle(),
                        post.getMember().getNickname(),
                        post.getViewCount(),
                        post.getLikeCount(),
                        post.getCreatedDate(),
                        post.getPostCategory().getName()
                )
        );

        return postPageList;
    }
}
