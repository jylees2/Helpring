package com.jy.helpring.service.post;

import com.jy.helpring.domain.category.Category;
import com.jy.helpring.domain.category.CategoryRepository;
import com.jy.helpring.domain.file.UploadFile;
import com.jy.helpring.domain.member.Member;
import com.jy.helpring.domain.member.MemberRepository;
import com.jy.helpring.domain.post.*;
import com.jy.helpring.service.file.FileStore;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostServiceImpl implements PostService{

    private static final int PAGE_POST_COUNT = 10; // 한 화면에 보일 컨텐츠 수

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final MemberLikePostRepository memberLikePostRepository;

    /** 파일 저장 처리 객체 **/
    private final FileStore fileStore;

    /* 게시물 전체 리스트 페이징 */
    @Override
    public Page<PostDto.ResponsePageDto> getPageList(Pageable pageable, int pageNo, String category_name, String orderCriteria) {

        /* 넘겨받은 orderCriteria 를 이용해 내림차순하여 Pageable 객체 반환 */
        pageable = PageRequest.of(pageNo, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, orderCriteria));
        /* category_name에 해당하는 post 페이지 객체 반환 */
        Page<Post> page = postRepository.findByCategory_Name(category_name, pageable);

        /* Dto로 변환 */
        Page<PostDto.ResponsePageDto> postPageList = page.map(
                post -> new PostDto.ResponsePageDto(
                        post.getId(),
                        post.getMember().getId(),
                        post.getTitle(),
                        post.getMember().getNickname(),
                        post.getViewCount(),
                        post.getLikeCount(),
                        post.getCreatedDate(),
                        post.getCategory().getName())
        );

        return postPageList;
    }


    /** 게시물 검색 리스트 페이징 **/
    @Override
    public Page<PostDto.ResponsePageDto> searchPageList(Pageable pageable, int pageNo, String keyword, String category_name, String orderCriteria) {

        /* 넘겨받은 orderCriteria 를 이용해 내림차순하여 Pageable 객체 반환 */
        pageable = PageRequest.of(pageNo, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, orderCriteria));

        /* category_name에 해당하면서 keyword를 포함하는 post 페이지 객체 반환 */
        Page<Post> page = postRepository.findByCategory_NameContainingIgnoreCase(category_name, keyword, pageable);

        /* Dto로 변환 */
        Page<PostDto.ResponsePageDto> postPageList = page.map(
                post -> new PostDto.ResponsePageDto(
                        post.getId(),
                        post.getMember().getId(),
                        post.getTitle(),
                        post.getMember().getNickname(),
                        post.getViewCount(),
                        post.getLikeCount(),
                        post.getCreatedDate(),
                        post.getCategory().getName()
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
    public PostDto.ResponseDto getById(Long post_id) {
        Post post = postRepository.findById(post_id).orElseThrow(() ->
                            new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        return new PostDto.ResponseDto(post);
    }

    /** member_id 에 해당하는 게시물 리스트 페이징 **/
    @Override
    public Page<PostDto.ResponsePageDto> getMyPostPageList(Pageable pageable, int pageNo, Long member_id, String category_name) {
        /* 넘겨받은 orderCriteria 를 이용해 내림차순하여 Pageable 객체 반환 */
        pageable = PageRequest.of(pageNo, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "id"));

        /* category_name에 해당하면서 member_id에 해당하는 post 페이지 객체 반환 */
        Page<Post> page = postRepository.findByCategory_NameAndMember_Id(category_name, member_id, pageable);

        /* Dto로 변환 */
        Page<PostDto.ResponsePageDto> postPageList = page.map(
                post -> new PostDto.ResponsePageDto(
                        post.getId(),
                        post.getMember().getId(),
                        post.getTitle(),
                        post.getMember().getNickname(),
                        post.getViewCount(),
                        post.getLikeCount(),
                        post.getCreatedDate(),
                        post.getCategory().getName()
                )
        );

        return postPageList;
    }

    /** ================ 게시물 CRUD ================ **/

    /** create **/
    @Override
    public Long save(PostDto.RequestDto requestDto, Long member_id) throws IOException {

        /* 파일 저장 */
        MultipartFile post_file = requestDto.getFile();
        UploadFile uploadFile = fileStore.storeFile(post_file);

        /* 파일명 추가 */
        requestDto.addFileName(uploadFile.getStoreFileName());

        /* Member 정보, category 정보 추가 */
        Long category_id = requestDto.getCategory_id();

        Member member = memberRepository.findById(member_id).orElseThrow(() ->
                                            new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
        Category category = categoryRepository.findById(category_id).orElseThrow(() ->
                                            new IllegalArgumentException("해당 카테고리가 존재하지 않습니다."));

//        requestDto.setMember(member);
//        requestDto.setCategory(category);

        /* RequestDto -> Entity */
        Post post = requestDto.toEntity(member, category);
        return postRepository.save(post).getId();
    }

    /** update **/
    @Override
    public void update(PostDto.RequestDto requestDto, Long member_id, Long post_id) {

        Long category_id = requestDto.getCategory_id();

        Post post = postRepository.findById(post_id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

        Category category = categoryRepository.findById(category_id).orElseThrow(() ->
                                                       new IllegalArgumentException("해당 카테고리가 존재하지 않습니다."));
        /* 수정 메서드 호출 */
        post.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getFileName(), category);
    }

    /** delete **/
    @Override
    public void delete(Long post_id) {
        Post post = postRepository.findById(post_id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
        postRepository.delete(post);
    }

    /** 글 좋아요 **/
    @Override
    public boolean saveLike(Long post_id, Long member_id) {

        /** 로그인한 유저가 해당 게시물을 좋아요 했는지 안 했는지 확인 **/
        if(!findLike(post_id, member_id)){

            /* 좋아요 하지 않은 게시물이면 좋아요 추가, true 반환 */
            Member member = memberRepository.findById(member_id).orElseThrow(() ->
                    new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
            Post post = postRepository.findById(post_id).orElseThrow(() ->
                    new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

            /* 좋아요 엔티티 생성 */
            MemberLikePost memberLikePost = new MemberLikePost(member, post);
            memberLikePostRepository.save(memberLikePost);
            postRepository.plusLike(post_id);

            return true;
        } else {

            /* 좋아요 한 게시물이면 좋아요 삭제, false 반환 */
            memberLikePostRepository.deleteByPost_IdAndMember_Id(post_id, member_id);
            postRepository.minusLike(post_id);

            return false;
        }
    }

    /** 글에 좋아요 했는지 확인하는 메서드 **/
    @Override
    public boolean findLike(Long post_id, Long member_id) {

        /* 좋아요 한 게시물이 아니라면 false, 좋아요 한 게시물이라면 true */
        Optional<MemberLikePost> memberLikePost = memberLikePostRepository.findByPost_IdAndMember_Id(post_id, member_id);

        if(memberLikePost.isEmpty()){
            return false;
        } else {
            return true;
        }
    }

    /** 글 조회수 업데이트 **/
    @Override
    public void updateView(Long post_id) {
        postRepository.updateView(post_id);
    }
}
