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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class PostRestController {

    private final PostService postService;

    /** create **/
    @PostMapping("/post")
    public Long save(@ModelAttribute PostDto.RequestDto dto,
                               @AuthenticationPrincipal UserAdapter user) throws IOException {

        Long memberId = user.getMember().getId();

        // 글 저장
        Long saveId = postService.save(dto, memberId);

        return saveId;
    }

    /** update **/
    @PutMapping("/post/{post_id}")
    public ResponseEntity update(@ModelAttribute PostDto.RequestDto requestDto,
                       @PathVariable Long post_id,
                       @AuthenticationPrincipal UserAdapter user) throws IOException{

        Long member_id = user.getMember().getId();

        //글 수정
        postService.update(requestDto, member_id, post_id);

        return new ResponseEntity(HttpStatus.OK);
    }

    /** delete **/
    @DeleteMapping("/post/{post_id}")
    public ResponseEntity delete(@PathVariable Long post_id){
        postService.delete(post_id);

        return new ResponseEntity(HttpStatus.OK);
    }

    /** 글 좋아요 **/
    @PostMapping("/like")
    public boolean like(Long post_id, Long member_id){
        // 저장 true, 삭제 false
        boolean result = postService.saveLike(post_id, member_id);
        return result;
    }
}
