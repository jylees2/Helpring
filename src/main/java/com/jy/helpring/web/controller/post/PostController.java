package com.jy.helpring.web.controller.post;

import com.jy.helpring.config.auth.UserAdapter;
import com.jy.helpring.service.category.CategoryService;
import com.jy.helpring.service.comment.CommentService;
import com.jy.helpring.service.post.PostService;
import com.jy.helpring.web.dto.category.CategoryDto;
import com.jy.helpring.web.dto.comment.CommentDto;
import com.jy.helpring.web.dto.post.PostDto;
import com.jy.helpring.web.vo.PageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/community")
@Slf4j
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    
    private final CategoryService categoryService;

    /** 글 전체 조회 (카테고리별 & 페이징) **/
    /* /{category_name}/post/page={pageNo}&orderby={orderCriteria} */
    @GetMapping("/{category_name}/post")
    public String readAllPost(@PathVariable(required = false) String category_name,
                          @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                          @RequestParam(required = false, defaultValue = "id", value = "orderby") String orderCriteria,
                          Pageable pageable,
                          @AuthenticationPrincipal UserAdapter user,
                          Model model){

        if(user != null){
            model.addAttribute("user", user);
        }
        /*
         기본적으로 보여줄 게시판 리스트는 `자유 게시판` 카테고리의 게시물들을 최신순으로 보여준다.
         */
        if(category_name.isEmpty()) category_name = "자유 게시판";

        /* 카테고리 리스트 반환 */
        List<CategoryDto.ResponseDto> categoryList = categoryService.findList();
        model.addAttribute("categoryList", categoryList);

        /** ========== 페이징 처리 ========== **/

        /*
        클라이언트 페이지에서 받은 pageNo 와 실제 접근 페이지는 다르다. Page 객체는 페이지가 0부터 시작
        따라서 실제 접근 페이지는 pageNo - 1 처리해주어야 한다.
         */
        pageNo = (pageNo == 0) ? 0 : (pageNo - 1);

        Page<PostDto.ResponsePageDto> postPageList =
                postService.getPageList(pageable, pageNo, category_name, orderCriteria); // 페이지 객체 생성
        PageVo pageVo = postService.getPageInfo(postPageList, pageNo);

        model.addAttribute("postPageList", postPageList);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("pageVo", pageVo);

        return "community/post-all";
    }

    /** 글 작성 페이지 반환 **/
    @GetMapping("/write")
    public String createForm(Model model,
                             @AuthenticationPrincipal UserAdapter user){

        /* 글 작성자가 카테고리를 선택할 수 있도록 함 */
        List<CategoryDto.ResponseDto> categoryList = categoryService.findList();
        model.addAttribute("categoryList", categoryList);

        return "community/post-create";
    }

    /** 글 상세 조회 페이지 반환 **/
    @GetMapping("/{category_name}/read/{post_id}")
    public String readById(@PathVariable("post_id") Long post_id,
                           @AuthenticationPrincipal UserAdapter user,
                           Model model){

        /* member_id 반환 */
        Long member_id = user.getMember().getId();

        /* 게시물 DTO 반환 */
        PostDto.ResponseDto postResponseDto = postService.getById(post_id);

        /* 로그인한 사용자와 게시물 작성자가 다르다면 조회수 업데이트 */
        if(member_id != postResponseDto.getMember_id()){
            postService.updateView(post_id);
        }
        /* 댓글 DTO 반환 */
        List<CommentDto.ResponseDto> commentListDto = commentService.findAllByPost(post_id);

        /* 현재 로그인한 유저가 이 게시물을 좋아요 했는지 안 했는지 여부 확인 */
        boolean like = postService.findLike(post_id, member_id);

        model.addAttribute("like", like);
        model.addAttribute("post", postResponseDto);
        model.addAttribute("commentList", commentListDto);

        return "community/post-read";
    }

    /** 글 수정 페이지 반환 **/
    @GetMapping("/{category_name}/update/{post_id}")
    public String update(@PathVariable("post_id") Long post_id,
                  @AuthenticationPrincipal UserAdapter user,
                  Model model){

        PostDto.ResponseDto post = postService.getById(post_id);

        model.addAttribute("post", post);
        model.addAttribute("user", user);

        return "community/post-update";
    }

    /** 글 검색 (카테고리별) 페이지 **/
    /* /{category_name}/search/keyword={keyword}&page={pageNo}&orderby={orderCriteria} */
    @GetMapping("/{category_name}/search")
    public String searchByCategory(@PathVariable("category_name") String category_name,
                                   @RequestParam("keyword") String keyword,
                                   @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                   @RequestParam(required = false, defaultValue = "id", value = "orderby") String orderCriteria,
                                   Pageable pageable,
                                   @AuthenticationPrincipal UserAdapter user,
                                   Model model){

        /*
        기본적으로 보여줄 게시판 리스트는 `자유 게시판` 카테고리의 게시물들을 최신순으로 보여준다.
         */
        if(category_name.isEmpty()) category_name = "자유 게시판";

        /* 카테고리 리스트 반환 */
        List<CategoryDto.ResponseDto> categoryList = categoryService.findList();
        model.addAttribute("categoryList", categoryList);

        /* ========== 페이징 처리 ========== */

        /*
        클라이언트 페이지에서 받은 pageNo 와 실제 접근 페이지는 다르다. Page 객체는 페이지가 0부터 시작
        따라서 실제 접근 페이지는 pageNo - 1 처리해주어야 한다.
         */
        pageNo = (pageNo == 0) ? 0 : (pageNo - 1);

        // 페이지 객체 생성
        Page<PostDto.ResponsePageDto> postResponsePageDto =
                postService.searchPageList(pageable, pageNo, keyword, category_name, orderCriteria);

        PageVo pageVo = postService.getPageInfo(postResponsePageDto, pageNo);


        /* 페이징 관련 정보 & 키워드 반환 */
        model.addAttribute("postPageList", postResponsePageDto);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("pageVo", pageVo);
        model.addAttribute("keyword", keyword);

        return "community/post-search";
    }

    /** 내가 작성한 글 페이지 조회 **/
    /* /mypost/{category_name}/page={pageNo} */
    @GetMapping("/mypost/{category_name}")
    public String readMyPost(@PathVariable String category_name,
                             @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                             Pageable pageable,
                             @AuthenticationPrincipal UserAdapter user,
                             Model model){
        /*
        기본적으로 보여줄 게시판 리스트는 `자유 게시판` 카테고리의 게시물들을 최신순으로 보여준다.
         */
        if(category_name.isEmpty()) category_name = "자유 게시판";

        /* 카테고리 리스트 반환 */
        List<CategoryDto.ResponseDto> categoryList = categoryService.findList();
        model.addAttribute("categoryList", categoryList);


        /* ========== 페이징 처리 ========== */

        /*
        클라이언트 페이지에서 받은 pageNo 와 실제 접근 페이지는 다르다. Page 객체는 페이지가 0부터 시작
        따라서 실제 접근 페이지는 pageNo - 1 처리해주어야 한다.
         */
        pageNo = (pageNo == 0) ? 0 : (pageNo - 1);

        // 페이지 객체 생성
        Page<PostDto.ResponsePageDto> postResponsePageDto =
                postService.getMyPostPageList(pageable, pageNo, user.getMemberDto().getId(), category_name);

        PageVo pageVo = postService.getPageInfo(postResponsePageDto, pageNo);

        /* 페이징 관련 정보 & 키워드 반환 */
        model.addAttribute("postPageList", postResponsePageDto);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("pageVo", pageVo);

        return "community/myPost";
    }
}
