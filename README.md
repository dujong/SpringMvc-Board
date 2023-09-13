# Spring MVC 게시판

## 개발 동기
- 이론과 간단한 코드를 통해서 배운 Spring MVC를 프로젝트를 통해서 구체화 시키며 전체적인 흐름을 잡고 싶어서 시작하게 된 게시판 프로젝트 입니다.
- 흔한 프로젝트이지만 백엔드 개발자로써 경험해봐야할 기술 스택들의 집합체라고 생각하여서 주제를 정하게 되었습니다.
- 개발하는 과정에서 실제로 개발할 때 발생하는 Error를 해결하고, 실제 서비스를 위해서 구현 시 고려해야 하는 상황, 아키텍쳐 등을 생각하며 성장하는 것에 초점을 둘 것 입니다.
- 전체적인 Process를 구현해 봄으로써 WEB 흐름 파악을 목표로 Front 적인 부분보다는 Backend적인 부분에 초점을 맞추고 개발하였습니다.

## ⚒️Skill Stack
- Front-end
    - [HTML ,CSS, Thymeleaf] (구현예정)
- Back-end
    - [JAVA16], [Spring 2.7.11], [Security 5.7.8], [Spring Data JPA], [QueryDSL 5.0.0], [AOP], [WEBMVC]
- DB
    - MYSQL8
- Tool
    - IntelliJ
    - Github
    - aws
## 프로젝트 구현 기능
- 회원(Member)
    - 로그인
    - 로그아웃
    - 회원가입
    - 회원탈퇴
- 게시글(Post)
    - 게시글 등록
    - 게시글 삭제
    - 게시글 수정
    - 게시글 조회
    - 게시글 검색
- 댓글(Comment)
    - 댓글 등록
    - 대댓글 등록
    - 댓글 수정
    - 댓글 삭제
## ERD
![Untitled](https://github.com/dujong/RoadMap_Recommand_Service_AI/assets/55770741/e5a925c6-09cf-43c6-9fb8-53271d14a4b5)

## API 명세서
| Group | Function | Method | End Point |
| --- | --- | --- | --- |
| 회원 | 회원가입 | POST | /signUp |
|  | 회원정보 수정 | PUT | /member |
|  | 비밀번호 수정 | PUT | /member/password |
|  | 회원탈퇴 | DELETE | /member |
|  | 회원정보 조회 | GET | /member/{id} |
| 게시글 | 게시글 등록 | POST | /post |
|  | 게시글 수정 | PUT | /post/{postid} |
|  | 게시글 삭제 | DELETE | /post/{postid} |
|  | 게시글 조회 | GET | /post/{postid} |
|  | 게시글 검색 | POST | /post |
| 댓글 | 댓글 등록 | POST | /comment/{postId} |
|  | 대댓글 등록 | POST | /cooment/{postId}/{commentId} |
|  | 댓글 수정 | PUT | /comment/{commentId} |
|  | 댓글 삭제 | DELETE | /comment/{commentId} |

## 개발일정
**프로젝트 기간 : 23.06.26 ~ 23.08.22**
- 06.26 ~ 06.30 : 프로젝트 주제 선정 및 설계
- 07.03 ~ 07.21 : 프로젝트 1차 기능 구현 및 테스트
- 07.26 ~ 08.22 : 프로젝트 2차 기능 구현 및 테스트
- 08.23 ~ 08.25 : 회고록 작성
- 08.25 ~ : 리팩토링(Front부분 만지기 및 Redis 써보기)

## 회고
전반적인 이론과 간단한 실습등으로 공부한 Spring을 실제로 프로젝트로 진행해보니 서비스가 완성되어 가는 과정에서 뿌듯함과 재미를 느꼈습니다. 특정 기술이나 라이브러리를 사용할 때, 이것이 최적의 방법인지 항상 고민하고 생각하며 프로젝트를 진행했습니다. 하지만 실제로 서비스를 제공하거나 배포해본 경험이 없어 어떤 스킬이 실제 서비스를 제공할 때 유리한 점을 가져가는지 판단이 쉽지 않아 힘들었습니다. 다음 프로젝트는 AWS를 활용하여 실제로 배포하여 사람들에게 서비스를 제공해보고 싶다고 느꼈습니다.
