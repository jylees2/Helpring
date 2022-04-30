package com.jy.helpring.web.controller.post;

import com.jy.helpring.config.auth.UserAdapter;
import com.jy.helpring.service.comment.CommentService;
import com.jy.helpring.service.post.PostCategoryService;
import com.jy.helpring.service.post.PostService;
import com.jy.helpring.web.dto.comment.CommentDto;
import com.jy.helpring.web.dto.post.PostCategoryDto;
import com.jy.helpring.web.dto.post.PostDto;
import com.jy.helpring.web.vo.PageVo;
import lombok.RequiredArgsConstructor;
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
public class PostController {

    private final PostService postService;
    private final PostCategoryService postCategoryService;
    private final CommentService commentService;

    /** 글 전체 조회 (카테고리별) **/
    /* /{postCategory_name}/post/page={page_no}&orderby={order_criteria} */
    @GetMapping("/{postCategory_name}/post")
    public String readAll(@PathVariable(required = false) String postCategory_name,
                          @RequestParam(required = false, defaultValue = "0", value = "page") int page_no,
                          @RequestParam(required = false, defaultValue = "id", value = "orderby") String order_criteria,
                          Pageable pageable,
                          @AuthenticationPrincipal UserAdapter user,
                          Model model){

        /*
         기본적으로 보여줄 게시판 리스트는 `자유 게시판` 카테고리의 게시물들을 최신순으로 보여준다.
         */
        if(postCategory_name.isEmpty()) postCategory_name = "자유 게시판";

        /* 카테고리 리스트 반환 */
        List<PostCategoryDto.ResponseDto> postCategoryList = postCategoryService.findList();
        model.addAttribute("postCatgoryList", postCategoryList);

        /* ========== 페이징 처리 ========== */

        /*
        클라이언트 페이지에서 받은 page_no 와 실제 접근 페이지는 다르다. Page 객체는 페이지가 0부터 시작
        따라서 실제 접근 페이지는 page_no - 1 처리해주어야 한다.
         */
        page_no = (page_no == 0) ? 0 : (page_no - 1);

        Page<PostDto.ResponsePageDto> postPageList =
                postService.getPageList(pageable, page_no, postCategory_name, order_criteria); // 페이지 객체 생성
        PageVo pageVo = postService.getPageInfo(postPageList, page_no);

        model.addAttribute("postPageList", postPageList);
        model.addAttribute("pageNo", page_no);
        model.addAttribute("pageVo", pageVo);

        return "community/post-all";
    }

    /** 글 작성 페이지 반환 **/
    @GetMapping("/write")
    public String createForm(Model model,
                             @AuthenticationPrincipal UserAdapter user){

        /* 글 작성자가 카테고리를 선택할 수 있도록 함 */
        List<PostCategoryDto.ResponseDto> postCategoryList = postCategoryService.findList();
        model.addAttribute("postCatgoryList", postCategoryList);

        return "community/post-create";
    }

    /** 글 상세 조회 페이지 반환 **/
    @GetMapping("/{postCategory_name}/read/{post_id}")
    public String readById(@PathVariable("post_id") Long post_id,
                           @AuthenticationPrincipal UserAdapter user,
                           Model model){

        /* 조회수 업데이트 */

        /* 게시물 DTO 반환 */
        PostDto.ResponseDto postResponseDto = postService.findById(post_id);

        /* 댓글 DTO 반환 */
        List<CommentDto.ResponseDto> commentListDto = commentService.findListById(post_id);

        /* 현재 로그인한 유저가 이 게시물을 좋아요 했는지 안 했는지 여부 확인 */
        Long member_id = user.getMember().getId(); // user의 member_id 반환

        model.addAttribute("post", postResponseDto);
        model.addAttribute("commentList", commentListDto);

        return "community/post-read";
    }

    /** 글 수정 페이지 반환 **/
    @GetMapping("/{postCategory_name}/update/{post_id}")
    public String update(@PathVariable("post_id") Long post_id,
                  @AuthenticationPrincipal UserAdapter user,
                  Model model){

        PostDto.ResponseDto post = postService.findById(post_id);

        model.addAttribute("post", post);
        model.addAttribute("user", user);

        return "community/post-update";
    }

    /** 글 검색 (카테고리별) 페이지 **/
    /* /{postCategory_name}/search/keyword={keyword}&page={page_no}&orderby={order_criteria} */
    @GetMapping("/{postCategory_name}/search")
    public String searchByCategory(@PathVariable("postCategory_name") String postCategory_name,
                                   @RequestParam("keyword") String keyword,
                                   @RequestParam(required = false, defaultValue = "0", value = "page") int page_no,
                                   @RequestParam(required = false, defaultValue = "id", value = "orderby") String order_criteria,
                                   Pageable pageable,
                                   @AuthenticationPrincipal UserAdapter user,
                                   Model model){

        /*
        기본적으로 보여줄 게시판 리스트는 `자유 게시판` 카테고리의 게시물들을 최신순으로 보여준다.
         */
        if(postCategory_name.isEmpty()) postCategory_name = "자유 게시판";

        /* 카테고리 리스트 반환 */
        List<PostCategoryDto.ResponseDto> postCategoryList = postCategoryService.findList();
        model.addAttribute("postCatgoryList", postCategoryList);

        /* ========== 페이징 처리 ========== */

        /*
        클라이언트 페이지에서 받은 page_no 와 실제 접근 페이지는 다르다. Page 객체는 페이지가 0부터 시작
        따라서 실제 접근 페이지는 page_no - 1 처리해주어야 한다.
         */
        page_no = (page_no == 0) ? 0 : (page_no - 1);

        // 페이지 객체 생성
        Page<PostDto.ResponsePageDto> postResponsePageDto =
                postService.searchPageList(pageable, page_no, keyword, postCategory_name, order_criteria);

        PageVo pageVo = postService.getPageInfo(postResponsePageDto, page_no);


        /* 페이징 관련 정보 & 키워드 반환 */
        model.addAttribute("postPageList", postResponsePageDto);
        model.addAttribute("pageNo", page_no);
        model.addAttribute("pageVo", pageVo);
        model.addAttribute("keyword", keyword);

        return "community/post-search";
    }

    /** 내가 작성한 글 페이지 조회 **/
    /* /mypost/{postCategory_name}/page={page_no} */
    @GetMapping("/mypost/{postCategory_name}")
    public String readMyPost(@PathVariable String postCategory_name,
                             @RequestParam(required = false, defaultValue = "0", value = "page") int page_no,
                             Pageable pageable,
                             @AuthenticationPrincipal UserAdapter user,
                             Model model){
        /*
        기본적으로 보여줄 게시판 리스트는 `자유 게시판` 카테고리의 게시물들을 최신순으로 보여준다.
         */
        if(postCategory_name.isEmpty()) postCategory_name = "자유 게시판";

        /* 카테고리 리스트 반환 */
        List<PostCategoryDto.ResponseDto> postCategoryList = postCategoryService.findList();
        model.addAttribute("postCatgoryList", postCategoryList);


        /* ========== 페이징 처리 ========== */

        /*
        클라이언트 페이지에서 받은 page_no 와 실제 접근 페이지는 다르다. Page 객체는 페이지가 0부터 시작
        따라서 실제 접근 페이지는 page_no - 1 처리해주어야 한다.
         */
        page_no = (page_no == 0) ? 0 : (page_no - 1);

        // 페이지 객체 생성
        Page<PostDto.ResponsePageDto> postResponsePageDto =
                postService.getMyPostPageList(pageable, page_no, user.getMemberDto().getId(), postCategory_name);

        PageVo pageVo = postService.getPageInfo(postResponsePageDto, page_no);

        /* 페이징 관련 정보 & 키워드 반환 */
        model.addAttribute("postPageList", postResponsePageDto);
        model.addAttribute("pageNo", page_no);
        model.addAttribute("pageVo", pageVo);

        return "community/mypost";
    }
}
