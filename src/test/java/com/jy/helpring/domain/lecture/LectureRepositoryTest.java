package com.jy.helpring.domain.lecture;

import com.jy.helpring.domain.Role;
import com.jy.helpring.domain.member.Member;
import com.jy.helpring.domain.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DataJpaTest
@Slf4j
public class LectureRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private MyLectureRepository myLectureRepository;

    private String username = "minha";
    private String nickname = "민하";
    private String email = "minhaprincess@jy.com";
    private Role role = Role.USER;

    private String tutor = "이영";
    private String title = "민하는 왜그럴까";
    private String intro = "민하는 귀엽지만 가끔 이해할 수 없는 행동을 한다";
    private String content = "민하가 세상에서 제일 귀여워";
    private String fileName = "minhaFile";
    private int price = 10000;

    @Test
    public void 내_강의_조회() {

        /* given */
        Member member = memberRepository.save(Member.builder()
                .username(username)
                .nickname(nickname)
                .email(email)
                .role(role)
                .build());

        Lecture lecture = lectureRepository.save(Lecture.builder()
                .tutor(tutor)
                .title(title)
                .intro(intro)
                .content(content)
                .fileName(fileName)
                .price(price)
                .member(member)
                .build());

        MyLecture myLecture = myLectureRepository.save(MyLecture.builder()
                .lecture(lecture)
                .member(member)
                .build());

        /* when */
        List<MyLecture> myLectureList =
                myLectureRepository.findByMember_IdAndLecture_Id(member.getId(), lecture.getId());

        /* then */
        assertThat(myLectureList.get(0)).isEqualTo(myLecture);
        log.info("테스트 성공");
    }
}
