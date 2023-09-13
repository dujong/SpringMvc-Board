package springmvc.board.domain.post.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import springmvc.board.domain.comment.Comment;
import springmvc.board.domain.comment.dto.CommentInfoDto;
import springmvc.board.domain.comment.repository.CommentRepository;
import springmvc.board.domain.member.Member;
import springmvc.board.domain.member.Role;
import springmvc.board.domain.member.dto.MemberSignUpDto;
import springmvc.board.domain.member.repository.MemberRepository;
import springmvc.board.domain.member.service.MemberService;
import springmvc.board.domain.post.Post;
import springmvc.board.domain.post.cond.PostSearchCondition;
import springmvc.board.domain.post.dto.PostInfoDto;
import springmvc.board.domain.post.dto.PostPagingDto;
import springmvc.board.domain.post.dto.PostSaveDto;
import springmvc.board.domain.post.dto.PostUpdateDto;
import springmvc.board.domain.post.exception.PostException;
import springmvc.board.domain.post.repository.PostRepository;

import javax.persistence.EntityManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
class PostServiceImplTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private PostService postService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;

    private static final String USERNAME = "username";
    private static final String PASSWORD = "PASSWORD123@@";

    private void clear() {
        em.flush();
        em.clear();
    }

    private void deleteFile(String filePath) {
        File files = new File(filePath);
        files.delete();
    }

    private MockMultipartFile getMockUploadFile() throws IOException {
        return new MockMultipartFile("file", "file.jpg", "image/jpg", new FileInputStream("C:/Users/ParkJongDu/Desktop/test.jpg"));
    }

    @BeforeEach
    private void signUpAndSetAuthentication() throws Exception {
        memberService.signUp(new MemberSignUpDto(USERNAME, PASSWORD, "name", "nickName", 26));
        SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
        emptyContext.setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        User.builder()
                                .username(USERNAME)
                                .password(PASSWORD)
                                .roles(Role.USER.toString())
                                .build(),
                        null
                )
        );

        SecurityContextHolder.setContext(emptyContext);
        clear();
    }

    @Test
    public void 포스트_저장_성공_업로드_파일_없음() throws Exception {
        //given
        String title = "제목";
        String content = "내용";
        PostSaveDto postSaveDto = new PostSaveDto(title, content, Optional.empty());

        //when
        postService.save(postSaveDto);
        clear();

        //then
        Post findPost = em.createQuery("select p from Post p", Post.class).getSingleResult();
        Post post = em.find(Post.class, findPost.getId());

        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.getWriter().getUsername()).isEqualTo(USERNAME);
        assertThat(post.getFilePath()).isNull();
    }

    @Test
    public void 포스트_저장_성공_업로드_파일_있음() throws Exception {
        //given
        String title = "제목";
        String content = "내용";
        PostSaveDto postSaveDto = new PostSaveDto(title, content, Optional.ofNullable(getMockUploadFile()));

        //when
        postService.save(postSaveDto);
        clear();

        //then
        Post findPost = em.createQuery("select p from Post p", Post.class).getSingleResult();
        Post post = em.find(Post.class, findPost.getId());

        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.getWriter().getUsername()).isEqualTo(USERNAME);
        assertThat(post.getFilePath()).isNotNull();

        deleteFile(post.getFilePath());
    }

    @Test
    public void 포스트_저장_실패_제목이나_내용이_없음() throws Exception {
        //given
        String title = "제목";
        String content = "내용";

        //then
        PostSaveDto postSaveDto1 = new PostSaveDto(null, content, Optional.empty());
        PostSaveDto postSaveDto2 = new PostSaveDto(title, null, Optional.empty());

        //when
        assertThrows(Exception.class, () -> postService.save(postSaveDto1));
        assertThrows(Exception.class, () -> postService.save(postSaveDto2));
    }

    @Test
    public void 포스트_업데이트_성공_업로드파일_없음TO없음() throws Exception {
        //given
        String title = "제목";
        String content = "내용";
        PostSaveDto postSaveDto = new PostSaveDto(title, content, Optional.empty());
        postService.save(postSaveDto);
        clear();

        //then
        Post findPost = em.createQuery("select p from Post p", Post.class).getSingleResult();
        PostUpdateDto postUpdateDto = new PostUpdateDto(Optional.ofNullable("바꾼제목"), Optional.ofNullable("바꾼내용"), Optional.empty());
        postService.update(findPost.getId(), postUpdateDto);
        clear();

        //when
        Post post = em.find(Post.class, findPost.getId());
        assertThat(post.getTitle()).isEqualTo("바꾼제목");
        assertThat(post.getContent()).isEqualTo("바꾼내용");
        assertThat(post.getWriter().getUsername()).isEqualTo(USERNAME);
        assertThat(post.getFilePath()).isNull();
    }

    @Test
    public void 포스트_업데이트_성공_업로드파일_없음TO있음() throws Exception {
        //given
        String title = "제목";
        String content = "내용";
        PostSaveDto postSaveDto = new PostSaveDto(title, content, Optional.empty());
        postService.save(postSaveDto);
        clear();

        //when
        Post findPost = em.createQuery("select p from Post p", Post.class).getSingleResult();
        PostUpdateDto postUpdateDto = new PostUpdateDto(Optional.ofNullable("바꾼제목"), Optional.ofNullable("바꾼내용"), Optional.ofNullable(getMockUploadFile()));
        postService.update(findPost.getId(), postUpdateDto);
        clear();

        //then
        Post post = em.find(Post.class, findPost.getId());
        assertThat(post.getContent()).isEqualTo("바꾼내용");
        assertThat(post.getWriter().getUsername()).isEqualTo(USERNAME);
        assertThat(post.getFilePath()).isNotNull();
    }

    @Test
    public void 포스트_업데이트_성공_업로드파일_있음TO없음() throws Exception {
        //given
        String title = "제목";
        String content = "내용";
        PostSaveDto postSaveDto = new PostSaveDto(title,content, Optional.ofNullable(getMockUploadFile()));
        postService.save(postSaveDto);

        Post findPost = em.createQuery("select p from Post p", Post.class).getSingleResult();
        assertThat(findPost.getFilePath()).isNotNull();
        clear();

        //when
        PostUpdateDto postUpdateDto = new PostUpdateDto(Optional.ofNullable("바꾼제목"),Optional.ofNullable("바꾼내용"), Optional.empty());
        postService.update(findPost.getId(),postUpdateDto);
        clear();

        //then
        findPost = em.find(Post.class, findPost.getId());
        assertThat(findPost.getContent()).isEqualTo("바꾼내용");
        assertThat(findPost.getWriter().getUsername()).isEqualTo(USERNAME);
        assertThat(findPost.getFilePath()).isNull();
    }

    @Test
    public void 포스트_업데이트_실패_권한이없음() throws Exception {
        //given
        String title = "제목";
        String content = "내용";
        PostSaveDto postSaveDto = new PostSaveDto(title, content, Optional.empty());

        postService.save(postSaveDto);
        clear();

        //when
        setAnotherAuthentication();
        Post findPost = em.createQuery("select p from Post p", Post.class).getSingleResult();
        PostUpdateDto postUpdateDto = new PostUpdateDto(Optional.ofNullable("바꾼제목"), Optional.ofNullable("바꾼내용"), Optional.empty());

        //then
        assertThrows(PostException.class, () -> postService.update(findPost.getId(), postUpdateDto));
    }

    @Test
    public void 포스트삭제_성공() throws Exception {
        //given
        String title = "제목";
        String content = "내용";
        PostSaveDto postSaveDto = new PostSaveDto(title, content, Optional.empty());
        postService.save(postSaveDto);
        clear();

        //when
        Post findPost = em.createQuery("select p from Post p", Post.class).getSingleResult();
        postService.delete(findPost.getId());

        //then
        List<Post> findPosts = em.createQuery("select p from Post p", Post.class).getResultList();
        assertThat(findPosts.size()).isEqualTo(0);
    }

    @Test
    public void 포스트삭제_실패() throws Exception {
        //given
        String title = "제목";
        String content = "내용";
        PostSaveDto postSaveDto = new PostSaveDto(title, content, Optional.empty());
        postService.save(postSaveDto);
        clear();

        //when, then
        setAnotherAuthentication();
        Post findPost = em.createQuery("select p from Post p", Post.class).getSingleResult();

        assertThrows(PostException.class, () -> postService.delete(findPost.getId()));

    }

    @Test
    public void 포스트_조회() throws Exception {
        Member member1 = memberRepository.save(Member.builder().username("username1").password("1234567890").name("USER1").nickName("밥 잘먹는 동훈이1").role(Role.USER).age(22).build());
        Member member2 = memberRepository.save(Member.builder().username("username2").password("1234567890").name("USER1").nickName("밥 잘먹는 동훈이2").role(Role.USER).age(22).build());
        Member member3 = memberRepository.save(Member.builder().username("username3").password("1234567890").name("USER1").nickName("밥 잘먹는 동훈이3").role(Role.USER).age(22).build());
        Member member4 = memberRepository.save(Member.builder().username("username4").password("1234567890").name("USER1").nickName("밥 잘먹는 동훈이4").role(Role.USER).age(22).build());
        Member member5 = memberRepository.save(Member.builder().username("username5").password("1234567890").name("USER1").nickName("밥 잘먹는 동훈이5").role(Role.USER).age(22).build());

        HashMap<Integer, Long> memberIdMap = new HashMap<>();
        memberIdMap.put(1,member1.getId());
        memberIdMap.put(2,member2.getId());
        memberIdMap.put(3,member3.getId());
        memberIdMap.put(4,member4.getId());
        memberIdMap.put(5,member5.getId());

        /**
         * Post 생성
         */

        Post post = Post.builder().title("게시글").content("내용").build();
        post.confirmWriter(member1);
        postRepository.save(post);
        em.flush();

        /**
         * Comment 생성
         */
        final int COMMENT_COUNT = 10;

        for (int i = 1; i <= COMMENT_COUNT; i++) {
            Comment comment = Comment.builder().content("댓글" + 1).build();
            comment.confirmWriter(memberRepository.findById(memberIdMap.get(i % 3 + 1)).orElse(null));
            comment.confirmPost(post);
            commentRepository.save(comment);
        }

        /**
         * ReComment 생성(대댓글)
         */

        final int COMMENT_PER_RECOMMENT_COUNT = 20;
        commentRepository.findAll().stream().forEach(comment -> {
            for(int i = 1; i<=20; i++ ){
                Comment recomment = Comment.builder().content("대댓글" + i).build();
                recomment.confirmWriter(memberRepository.findById(memberIdMap.get(i % 3 + 1)).orElse(null));

                recomment.confirmPost(comment.getPost());
                recomment.confirmParent(comment);
                commentRepository.save(recomment);
            }

        });
        clear();


        //when
        PostInfoDto postInfo = postService.getPostInfo(post.getId());



        //then
        assertThat(postInfo.getPostId()).isEqualTo(post.getId());
        assertThat(postInfo.getContent()).isEqualTo(post.getContent());
        assertThat(postInfo.getWriterDto().getUsername()).isEqualTo(post.getWriter().getUsername());


        int recommentCount = 0;
        for (CommentInfoDto commentInfoDto : postInfo.getCommentInfoDtoList()) {
            recommentCount += commentInfoDto.getReCommentInfoDtoList().size();
        }

        assertThat(postInfo.getCommentInfoDtoList().size()).isEqualTo(COMMENT_COUNT);
        assertThat(recommentCount).isEqualTo(COMMENT_PER_RECOMMENT_COUNT * COMMENT_COUNT);
    }

    @Test
    public void 포스트_검색_조건없음() throws Exception {
        //given
        /**
         * Member 생성
         */
        Member member1 = memberRepository.save(Member.builder().username("username1").password("1234567890").name("USER1").nickName("밥 잘먹는 종두1").role(Role.USER).age(26).build());

        /**
         * Post 생성
         */
        final int POST_COUNT = 50;
        for (int i = 1; i <= POST_COUNT; i++) {
            Post post = Post.builder().title("게시글" + 1).content("내용" + i).build();
            post.confirmWriter(member1);
            postRepository.save(post);
        }

        clear();

        //when
        final int PAGE = 0;
        final int SIZE = 20;
        PageRequest pageRequest = PageRequest.of(PAGE, SIZE);
        PostSearchCondition postSearchCondition = new PostSearchCondition();
        PostPagingDto postList = postService.getPostList(pageRequest, postSearchCondition);

        //then
        assertThat(postList.getTotalElementCount()).isEqualTo(POST_COUNT);
    }

    @Test
    public void 포스트_검색_제목일치() throws Exception {
        //given
        /**
         * Member 생성
         */
        Member member1 = memberRepository.save(Member.builder().username("username1").password("1234567890").name("USER1").nickName("밥 잘먹는 종두1").role(Role.USER).age(26).build());

        /**
         * Post 생성
         */
        final int DEFAULT_POST_COUNT = 100;
        for (int i = 1; i <= DEFAULT_POST_COUNT; i++) {
            Post post = Post.builder().title("게시글" + 1).content("내용" + i).build();
            post.confirmWriter(member1);
            postRepository.save(post);
        }

        /**
         * 제목에 AAA가 들어간 POST 생성
         */
        final String SEARCH_TITLE_STR = "AAA";
        final int COND_POST_COUNT = 100;
        for(int i = 1; i<=COND_POST_COUNT; i++ ){
            Post post = Post.builder().title(SEARCH_TITLE_STR+ i).content("내용"+i).build();
            post.confirmWriter(member1);
            postRepository.save(post);
        }
        clear();

        //when
        final int PAGE = 2;
        final int SIZE = 20;
        PageRequest pageRequest = PageRequest.of(PAGE, SIZE);
        PostSearchCondition postSearchCondition = new PostSearchCondition();
        postSearchCondition.setTitle(SEARCH_TITLE_STR);

        PostPagingDto postList = postService.getPostList(pageRequest, postSearchCondition);

        //then
        assertThat(postList.getTotalElementCount()).isEqualTo(COND_POST_COUNT);
        assertThat(postList.getTotalPageCount()).isEqualTo((COND_POST_COUNT % SIZE == 0)
                                                            ? COND_POST_COUNT / SIZE
                                                            : COND_POST_COUNT / SIZE + 1);

        assertThat(postList.getCurrentPageNum()).isEqualTo(PAGE);
        assertThat(postList.getCurrentPageElementCount()).isEqualTo(SIZE);
    }

    @Test
    public void 포스트_검색_내용일치() throws Exception {
        //given
        /**
         * MEMBER 저장
         */

        Member member1 = memberRepository.save(Member.builder().username("username1").password("1234567890").name("USER1").nickName("밥 잘먹는 동훈이1").role(Role.USER).age(22).build());


        /**
         * 일반 POST 생성
         */
        final int DEFAULT_POST_COUNT = 100;
        for(int i = 1; i<=DEFAULT_POST_COUNT; i++ ){
            Post post = Post.builder().title("게시글"+ i).content("내용"+i).build();
            post.confirmWriter(member1);
            postRepository.save(post);
        }

        /**
         * 제목에 AAA가 들어간 POST 생성
         */
        final String SEARCH_CONTENT_STR = "AAA";

        final int COND_POST_COUNT = 100;

        for(int i = 1; i<=COND_POST_COUNT; i++ ){
            Post post = Post.builder().title("게시글"+ i).content(SEARCH_CONTENT_STR+i).build();
            post.confirmWriter(member1);
            postRepository.save(post);
        }
        clear();

        //when
        final int PAGE = 2;
        final int SIZE = 20;
        PageRequest pageRequest = PageRequest.of(PAGE, SIZE);

        PostSearchCondition postSearchCondition = new PostSearchCondition();
        postSearchCondition.setContent(SEARCH_CONTENT_STR);

        PostPagingDto postList = postService.getPostList(pageRequest, postSearchCondition);

        //then
        assertThat(postList.getTotalElementCount()).isEqualTo(COND_POST_COUNT);
        assertThat(postList.getTotalPageCount()).isEqualTo((COND_POST_COUNT % SIZE == 0)
                                                            ? COND_POST_COUNT/SIZE
                                                            : COND_POST_COUNT/SIZE + 1);
        assertThat(postList.getCurrentPageNum()).isEqualTo(PAGE);
        assertThat(postList.getCurrentPageElementCount()).isEqualTo(SIZE);
    }

    @Test
    public void 포스트_검색_제목과내용일치() throws Exception {
        //given
        /**
         * MEMBER 저장
         */

        Member member1 = memberRepository.save(Member.builder().username("username1").password("1234567890").name("USER1").nickName("밥 잘먹는 동훈이1").role(Role.USER).age(22).build());


        /**
         * 일반 POST 생성
         */
        final int DEFAULT_POST_COUNT = 100;
        for(int i = 1; i<=DEFAULT_POST_COUNT; i++ ){
            Post post = Post.builder().title("게시글"+ i).content("내용"+i).build();
            post.confirmWriter(member1);
            postRepository.save(post);
        }


        /**
         * 제목에 AAA가 들어간 POST 생성
         */
        final String SEARCH_TITLE_STR = "AAA";
        final String SEARCH_CONTENT_STR = "BBB";

        final int COND_POST_COUNT = 100;

        for(int i = 1; i<=COND_POST_COUNT; i++ ){
            Post post = Post.builder().title(SEARCH_TITLE_STR+ i).content(SEARCH_CONTENT_STR+i).build();
            post.confirmWriter(member1);
            postRepository.save(post);
        }
        clear();



        //when
        final int PAGE = 2;
        final int SIZE = 20;
        PageRequest pageRequest = PageRequest.of(PAGE, SIZE);

        PostSearchCondition postSearchCondition = new PostSearchCondition();
        postSearchCondition.setTitle(SEARCH_TITLE_STR);
        postSearchCondition.setContent(SEARCH_CONTENT_STR);

        PostPagingDto postList = postService.getPostList(pageRequest, postSearchCondition);

        //then
        assertThat(postList.getTotalElementCount()).isEqualTo(COND_POST_COUNT);
        assertThat(postList.getTotalPageCount()).isEqualTo((COND_POST_COUNT % SIZE == 0)
                                                            ? COND_POST_COUNT/SIZE
                                                            : COND_POST_COUNT/SIZE + 1);
        assertThat(postList.getCurrentPageNum()).isEqualTo(PAGE);
        assertThat(postList.getCurrentPageElementCount()).isEqualTo(SIZE);
    }

    private void setAnotherAuthentication() throws Exception {
        memberService.signUp(new MemberSignUpDto(USERNAME+"123",PASSWORD,"name","nickName",22));
        SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
        emptyContext.setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        User.builder()
                                .username(USERNAME+"123")
                                .password(PASSWORD)
                                .roles(Role.USER.toString())
                                .build(),
                        null)
        );
        SecurityContextHolder.setContext(emptyContext);
        clear();
    }



}