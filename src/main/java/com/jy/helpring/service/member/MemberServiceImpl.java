package com.jy.helpring.service.member;

import com.jy.helpring.domain.member.Member;
import com.jy.helpring.domain.member.MemberRepository;
import com.jy.helpring.web.dto.member.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;


    /** 회원가입 **/
    @Override
    public void userJoin(MemberDto.RequestDto memberDto) {
        /* 비밀번호 암호화 */
        memberDto.encryptPassword(encoder.encode(memberDto.getPassword()));

        Member member = memberDto.toEntity();
        memberRepository.save(member);

        log.info("회원 저장 성공");
    }

    /** =============== 회원 수정 =============== **/

    /** 닉네임 중복 체크 **/
    @Override
    public boolean checkNickname(Long member_id, String nickname) {

        if(memberRepository.existsByNickname(nickname)){
            if(memberRepository.findByNickname(nickname).getId() == member_id){
                // 입력 받은 닉네임의 회원 id와 일치한다면 즉, 현재 닉네임을 그대로 입력한 경우
                return false;
            } else{
                // 다른 사람이 사용하고 있는 닉네임이라면
                return true;
            }
        } else{
            // 중복된 닉네임이 아니라면
            return false;
        }
    }

    /** 회원 수정 **/
    @Override
    public void userInfoUpdate(MemberDto.RequestDto memberDto) {

        /* 회원 찾기 */
        Member member = memberRepository.findById(memberDto.toEntity().getId()).orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        /* 수정한 비밀번호 암호화 */
        String encryptPassword = encoder.encode(memberDto.getPassword());
        member.update(memberDto.getNickname(), encryptPassword); // 회원 수정

        log.info("회원 수정 성공");
    }

    /** 비밀번호 일치 확인 **/
    @Override
    public boolean checkPassword(Long member_id, String checkPassword) {

        Member member = memberRepository.findById(member_id).orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        /* DB에 저장되어있는 암호화된 비밀번호 */
        String realPassword = member.getPassword();

        /* 입력한 비밀번호와 암호화되어 저장되어있는 비밀번호가 일치하는지 확인 */
        boolean matches = encoder.matches(checkPassword, realPassword);

        return matches;
    }

    /** =============== 비밀번호 찾기 : 임시 비밀번호 전송 =============== **/

    /** 이메일이 존재하는지 확인 **/
    @Override
    public boolean checkEmail(String memberEmail) {

        /* 이메일이 존재하면 true, 이메일이 없으면 false  */
        return memberRepository.existsByEmail(memberEmail);
    }

    /** member_id로 memberDto 반환 **/
    @Override
    public MemberDto.ResponseDto getById(Long member_id) {

        Member member = memberRepository.findById(member_id).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        return new MemberDto.ResponseDto(member);
    }

    /** 임시 비밀번호 생성 **/
    @Override
    public String getTmpPassword() {
        char[] charSet = new char[]{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        String pwd = "";

        /* 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 조합 */
        int idx = 0;
        for(int i = 0; i < 10; i++){
            idx = (int) (charSet.length * Math.random());
            pwd += charSet[idx];
        }

        log.info("임시 비밀번호 생성");

        return pwd;
    }

    /** 임시 비밀번호로 업데이트 **/
    @Override
    public void updatePassword(String tmpPassword, String memberEmail) {

        String encryptPassword = encoder.encode(tmpPassword);
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        member.updatePassword(encryptPassword);
        log.info("임시 비밀번호 업데이트");
    }

}
