package com.jy.helpring.web.controller.post;

import com.jy.helpring.config.auth.UserAdapter;
import com.jy.helpring.service.post.PostService;
import com.jy.helpring.web.dto.post.PostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/community/post")
@Slf4j
public class PostRestController {

    private final PostService postService;

//    /** create **/
//    @PostMapping("/post")
//    public Long save(@Validated @ModelAttribute PostDto.RequestDto postDto,
//                               @AuthenticationPrincipal UserAdapter user) throws IOException {
//
//        log.info("/community/post 진입");
//        Long memberId = user.getMember().getId();
//
//        // 글 저장
//        Long saveId = postService.save(postDto, memberId);
//
//        return saveId;
//    }

    /** update **/
    @PutMapping("/{post_id}")
    public ResponseEntity update(@RequestBody PostDto.RequestDto requestDto,
                       @PathVariable Long post_id,
                       @AuthenticationPrincipal UserAdapter user) throws IOException{

        Long member_id = user.getMember().getId();

        //글 수정
        postService.update(requestDto, member_id, post_id);

        return new ResponseEntity(HttpStatus.OK);
    }

    /** delete **/
    @DeleteMapping("/{post_id}")
    public ResponseEntity delete(@PathVariable Long post_id){
        postService.delete(post_id);

        return new ResponseEntity(HttpStatus.OK);
    }

    /** 글 좋아요 **/
    @PostMapping("/like/{post_id}")
    public boolean like(@PathVariable Long post_id, @AuthenticationPrincipal UserAdapter user){

        Long member_id = user.getMemberDto().getId();

        // 좋아요하지 않은 게시물이라 좋아요 헀다면 true, 좋아요 한 게시물이라 삭제했다면 false
        return postService.saveLike(post_id, member_id);
    }
}