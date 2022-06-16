# Helpring 👩‍💻
_더 이상 혼자 울지 마세요,,💦_

> **개발자들이 취업 준비 및 정보 공유를 위해 커뮤니티에 글을 작성하고 강의를 수강하는 웹 사이트**

<br/>

# 1. 프로젝트 개요
- **프로젝트 명칭** : Helpring
- **프로젝트 소개** : 개발자들이 취업 준비 및 정보 공유를 위해 커뮤니티에서 글을 작성할 수도 있고 강의도 수강할 수 있는 웹 사이트
- **개발 인원** : 1명 (이주영)
- **개발 기간** : 2021.04.20~ 2022.05.28
- **주요 기능**
  - 폼 로그인 / `OAuth 2.0` 로그인 기능
  - `Iamport API` 이용 카카오페이 결제 기능
  - SMTP Gmail 이용 사용자가 입력한 `메일로 임시 비밀번호 전송` 기능
  - `좋아요` 기능
  - 게시물에 `파일 첨부` 기능
  - 글 `카테고리별/조회순/추천순/최신순` 조회 기능
  - 게시물 `댓글` 기능
  - 인강 `조회/구매/리뷰/장바구니/찜` 기능
 
- **백엔드 개발 언어** : `Java 11`
- **백엔드 개발 환경**
  - `Window`
  - `IntelliJ`, `Eclipse sts`
  - `SpringBoot`
  - `gradle`
  - `jpa(Spring Data JPA)`
  - `Spring Security`
  - `Oauth 2.0`
- **프론트 개발 환경 및 언어**
  - `html5`
  - `thymeleaf`
  - `bootstrap template`
  - `javascript`
  - `jquery`
- **데이터베이스** : `MySQL`
- **형상관리** : `GitHub`
- **이슈 관리** : `GitHub`


# 2. 프로젝트 요구사항

**주요 사항**
- 폼 로그인 / OAuth 2.0 로그인 기능
- 회원가입 유효성 검사
- Iamport API 이용 카카오페이 결제 기능
- 사용자가 입력한 메일로 임시 비밀번호 전송 기능
- 좋아요 기능
- 게시물 CRUD+파일 첨부 기능
- 글 카테고리별/조회순/추천순/최신순 조회 기능
- 게시물 댓글 기능
- 인강 조회/구매/리뷰/장바구니/찜 기능

## 회원 기능
> - 회원가입 시 `유효성 검사` / `중복 체크`를 통과해야 한다.
-  폼 로그인 / OAuth 2.0 로그인 기능 - `구글로 로그인` / `네이버로 로그인` 기능을 사용할 수 있다.
- 비밀번호 분실 시 가입한 이메일로 `임시 비밀번호`를 발급받을 수 있다.

## 마이페이지 기능
> - 내 회원 정보를 `조회` / `수정`할 수 있다,
- `내가 작성한 게시글` 목록을 조회할 수 있다.
- `내가 찜한 강의` / `장바구니 내역` /`내가 수강 중인 강의` 목록을 볼 수 있다.

## 커뮤니티(게시판) 기능
> - 게시판에 글을 올릴때 카테고리를 고를 수 있다. `자유게시판` `질문` `스터디 모집` `프로젝트 팀원 모집`
- 게시판에 `글(이미지 파일 첨부 가능)`을 작성할 수 있다.
- 댓글을 작성할 수 있다.
- 글을 `검색`할 수 있고 `좋아요`를 누를 수 있다.
- 글을 `조회수순`, `추천순`, `최신순`, `카테고리별`로 조회할 수 있다.

## 인강 기능
> - 회원은 `카카오페이`로 강의를 `결제`할 수 있다.
- 회원은 강의를 `조회`/`구매`/`수강`/`장바구니`/`찜` 할 수 있다
- 회원은 수강한 강의에 `수강평을 작성` / `조회`할 수 있다.

# 3. 개발 내용
> 몇 가지 내용은 이전 프로젝트([CRUD + Security](https://github.com/jylees2/NewSecurityBoard))에서 공부한 것을 토대로 구현했기 때문에 이전 프로젝트 게시물을 링크했다.

- 이전 프로젝트 글
  - 스프링 시큐리티 회원가입
  - 스프링 시큐리티 로그인
  - [스프링 데이터 JPA 이용 조회수 처리](https://velog.io/@jyleedev/%EA%B2%8C%EC%8B%9C%EA%B8%80-%EC%A1%B0%ED%9A%8C%EC%88%98-%EC%B2%98%EB%A6%AC)
  - 게시물 검색
  - [회원가입 Validation 유효성 검사(아이디/비밀번호/닉네임 규칙, 중복 체크)](https://velog.io/@jyleedev/%EC%9C%A0%ED%9A%A8%EC%84%B1%EA%B2%80%EC%82%AC)
  - [OAuth 2.0 로그인(구글, 네이버)](https://velog.io/@jyleedev/%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0-%EA%B5%AC%EA%B8%80%EB%A1%9C-%EB%A1%9C%EA%B7%B8%EC%9D%B8%ED%95%98%EA%B8%B0)
  - [@AuthenticationPrincipal 애노테이션으로 로그인 세션 정보 받아오기](https://velog.io/@jyleedev/%EB%A1%9C%EA%B7%B8%EC%9D%B8-AuthenticationPrincipal-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%A0%95%EB%B3%B4-%EB%B0%9B%EC%95%84%EC%98%A4%EA%B8%B0)


- [프로젝트 명세서](https://velog.io/@jyleedev/Helpring-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EB%AA%85%EC%84%B8%EC%84%9C)
- 로그인 실패 에러 메시지 처리
- [비밀번호 확인 후 회원 정보 수정](https://velog.io/@jyleedev/%ED%9A%8C%EC%9B%90-%EC%A0%95%EB%B3%B4-%EC%88%98%EC%A0%95)
- [게시물에 파일 첨부](https://velog.io/@jyleedev/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8JPA%ED%83%80%EC%9E%84%EB%A6%AC%ED%94%84-%EA%B2%8C%EC%8B%9C%EB%AC%BC-%ED%8C%8C%EC%9D%BC-%EC%B2%A8%EB%B6%80)
- [게시물 좋아요 기능](https://velog.io/@jyleedev/%EA%B2%8C%EC%8B%9C%EB%AC%BC-%EC%A2%8B%EC%95%84%EC%9A%94-%EA%B8%B0%EB%8A%A5)
- [게시물 최신순/좋아요순/조회수순 기준 정렬](https://velog.io/@jyleedev/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8JPA%ED%83%80%EC%9E%84%EB%A6%AC%ED%94%84-%EA%B2%8C%EC%8B%9C%EB%AC%BC-%EC%B5%9C%EC%8B%A0%EC%88%9C-%EC%A2%8B%EC%95%84%EC%9A%94%EC%88%9C-%EC%A1%B0%ED%9A%8C%EC%88%98%EC%88%9C-%EC%A1%B0%ED%9A%8C)
- [Iamport API 이용 강의 결제 구현 & 서버 검증](https://velog.io/@jyleedev/%EC%95%84%EC%9E%84%ED%8F%AC%ED%8A%B8-API-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EA%B0%95%EC%9D%98-%EA%B2%B0%EC%A0%9C-%EA%B5%AC%ED%98%84-%EC%84%9C%EB%B2%84-%EA%B2%80%EC%A6%9D)
- [강의 찜 목록 / 장바구니 추가 / 강의 구매 후 권한 획득](https://velog.io/@jyleedev/%EA%B0%95%EC%9D%98-%EC%9C%84%EC%8B%9C-%EB%A6%AC%EC%8A%A4%ED%8A%B8-%EC%9E%A5%EB%B0%94%EA%B5%AC%EB%8B%88-%EC%B6%94%EA%B0%80)
- [SMTP Gmail 이용 이메일 전송 구현 - 임시 비밀번호 발송 기능](https://velog.io/@jyleedev/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-SMTP-Gmail-%EC%9D%B4%EC%9A%A9%ED%95%98%EC%97%AC-%EC%9D%B4%EB%A9%94%EC%9D%BC-%EC%A0%84%EC%86%A1-%EA%B5%AC%ED%98%84-%EC%9E%84%EC%8B%9C-%EB%B9%84%EB%B0%80%EB%B2%88%ED%98%B8-%EB%B0%9C%EC%86%A1-%EA%B8%B0%EB%8A%A5)
- [스프링 데이터 JPA Pageable, Page](https://velog.io/@jyleedev/%EC%8A%A4%ED%94%84%EB%A7%81-%EB%8D%B0%EC%9D%B4%ED%84%B0-JPA-Pageable-Page)
- [채널톡 API 이용 관리자 문의 채팅](https://velog.io/@jyleedev/%EC%B1%84%EB%84%90%ED%86%A1-API-%EC%9D%B4%EC%9A%A9%ED%95%98%EC%97%AC-%EA%B4%80%EB%A6%AC%EC%9E%90-%EC%B1%84%ED%8C%85-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0)
- Thymeleaf CSRF 문제
- [에러 로그 시리즈](https://velog.io/@jyleedev/series/Helpring-%EC%97%90%EB%9F%AC-%EB%A1%9C%EA%B7%B8)


# 4. DB 설계
## ERD
![](https://velog.velcdn.com/images/jyleedev/post/6acdd68f-a8b4-4487-8115-1df94b4a3161/image.png)

# 5. API 설계
## 관리자 권한
![](https://velog.velcdn.com/images/jyleedev/post/10d74870-b757-4f2c-8fb4-de64dcea7b5e/image.png)

## 결제 검증
![](https://velog.velcdn.com/images/jyleedev/post/f9f93b3c-6b6b-4602-9804-5a176677c930/image.png)

## 회원 기능
![](https://velog.velcdn.com/images/jyleedev/post/e3ed1ce1-9c6d-4f2c-bff9-b5a274b27d3e/image.png)

![](https://velog.velcdn.com/images/jyleedev/post/7e2d9676-a9c5-49f0-a03d-7c6a3de40b96/image.png)

## 커뮤니티(게시판) 기능
![](https://velog.velcdn.com/images/jyleedev/post/65af36a1-5d11-43d8-86d8-19404be7c29b/image.png)

![](https://velog.velcdn.com/images/jyleedev/post/cac10f89-be08-46c0-a3ab-e6c8570e53cb/image.png)

## 댓글 기능
![](https://velog.velcdn.com/images/jyleedev/post/e82590f3-d082-47a9-9748-9ba8daf9b3e8/image.png)


## 인강 기능
![](https://velog.velcdn.com/images/jyleedev/post/a8b098b2-f105-4607-abd9-8e95aca4f6c2/image.png)

## 인강 리뷰 기능
![](https://velog.velcdn.com/images/jyleedev/post/df4bd1af-1773-4994-9feb-ed8d291ede10/image.png)

## 장바구니 기능
![](https://velog.velcdn.com/images/jyleedev/post/c11cb842-e636-4999-82e0-103bcac89f4f/image.png)

![](https://velog.velcdn.com/images/jyleedev/post/c071d9a3-a7e1-494a-bf5c-062520bcbdd9/image.png)

## 찜 기능
![](https://velog.velcdn.com/images/jyleedev/post/c33366cc-f258-4c65-84ac-2ff97554e58f/image.png)

# 6. 프로젝트 후기

