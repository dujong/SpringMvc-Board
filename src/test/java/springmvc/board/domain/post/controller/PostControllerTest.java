package springmvc.board.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import springmvc.board.domain.member.Member;
import springmvc.board.domain.member.Role;
import springmvc.board.domain.member.repository.MemberRepository;
import springmvc.board.domain.post.Post;
import springmvc.board.domain.post.dto.PostInfoDto;
import springmvc.board.domain.post.dto.PostPagingDto;
import springmvc.board.domain.post.repository.PostRepository;
import springmvc.board.global.file.service.FileService;
import springmvc.board.global.jwt.service.JwtService;

import javax.persistence.EntityManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
@AutoConfigureMockMvc
class PostControllerTest { //TODO 수정, 삭제, 조회 Test Error 수정
    @Autowired
    MockMvc mockmvc;
    @Autowired
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtService jwtService;
    final String USERNAME = "username1";

    private static Member member;

    private void clear() {
        em.flush();
        em.clear();
    }

    @BeforeEach
    public void signUpMember() {
        member = memberRepository.save(Member.builder().username(USERNAME).password("1234567890").name("name").nickName("밥 잘먹는 종두").role(Role.USER).age(26).build());
        clear();
    }

    private String getAccessToken() {
        return jwtService.createAccessToken(USERNAME);
    }
    private MockMultipartFile getMockUploadFile() throws IOException {
        return new MockMultipartFile("uploadFile", "file.jpg", "image/jpg", new FileInputStream("C:/Users/ParkJongDu/Desktop/test.jpg"));
    }

    /**
     * 게시글 저장
     */
    @Test
    public void 게시글_저장_성공() throws Exception {
        //given
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("title", "제목");
        map.add("content", "내용");

        //when
        mockmvc.perform(MockMvcRequestBuilders
                        .post("/post")
                        .header("Authorization", "Bearer " + getAccessToken())
                        .contentType(MediaType.MULTIPART_FORM_DATA).params(map))
                .andExpect(status().isCreated());
        //then
        assertThat(postRepository.findAll().size()).isEqualTo(1);
    }


    /**
     * 게시글 저장
     */
    @Test
    public void 게시글_저장_실패_제목이나_내용이_없음() throws Exception {
        //given
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("title", "제목");

        //when, then
        mockmvc.perform(MockMvcRequestBuilders
                        .post("/post")
                        .header("Authorization", "Bearer " + getAccessToken())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .params(map))
                .andExpect(status().isBadRequest());

        map = new LinkedMultiValueMap<>();
        map.add("content", "내용");
        mockmvc.perform(MockMvcRequestBuilders
                        .post("/post")
                        .header("Authorization", "Bearer "+ getAccessToken())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .params(map))
                .andExpect(status().isBadRequest());
    }

    /**
     * 게시글 수정
     */
    @Test
    public void 게시글_수정_제목변경_성공() throws Exception {
        //given
        Post post = Post.builder().title("수정전제목").content("수정전내용").build();
        post.confirmWriter(member);
        Post savePost = postRepository.save(post);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        final String UPDATE_TITLE = "제목";
        map.add("title", UPDATE_TITLE);

        //when
        mockmvc.perform(MockMvcRequestBuilders
                        .put("/post/" + savePost.getId())
                        .header("Authorization", "Bearer " + getAccessToken())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .params(map))
                .andExpect(status().isOk());

        //then
        assertThat(postRepository.findAll().get(0).getTitle()).isEqualTo(UPDATE_TITLE);
    }

    /**
     * 게시글 수정
     */
    @Test
    public void 게시글_수정_내용변경_성공() throws Exception {
        //given
        Post post = Post.builder().title("수정전제목").content("수정전내용").build();
        post.confirmWriter(member);
        Post savePost = postRepository.save(post);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        final String UPDATE_CONTENT = "내용";
        map.add("content", UPDATE_CONTENT);

        //when
        mockmvc.perform(MockMvcRequestBuilders
                        .put("/post/"+savePost.getId())
                        .header("Authorization", "Bearer "+ getAccessToken())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .params(map))
                .andExpect(status().isOk());


        //then
        assertThat(postRepository.findAll().get(0).getContent()).isEqualTo(UPDATE_CONTENT);
    }

    /**
     * 게시글 수정
     */
    public void 게시글_수정_제목내용변경_성공() throws Exception {
        //given
        Post post = Post.builder().title("수정전제목").content("수정전내용").build();
        post.confirmWriter(member);
        Post savePost = postRepository.save(post);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        final String UPDATE_CONTENT = "내용";
        final String UPDATE_TITlE = "제목";
        map.add("title", UPDATE_TITlE);
        map.add("content", UPDATE_CONTENT);

        //when
        mockmvc.perform(MockMvcRequestBuilders
                        .put("/post/"+savePost.getId())
                        .header("Authorization", "Bearer "+ getAccessToken())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .params(map))
                .andExpect(status().isOk());

        //then
        Assertions.assertThat(postRepository.findAll().get(0).getContent()).isEqualTo(UPDATE_CONTENT);
        Assertions.assertThat(postRepository.findAll().get(0).getTitle()).isEqualTo(UPDATE_TITlE);
    }

    @Test
    public void 게시글_수정_업로드파일추가_성공() throws Exception {
        //given
        Post post = Post.builder().title("수정전제목").content("수정전내용").build();
        post.confirmWriter(member);
        Post savePost = postRepository.save(post);

        MockMultipartFile mockUploadFile = getMockUploadFile();


        //when

        MockMultipartHttpServletRequestBuilder requestBuilder = multipart("/post/" + savePost.getId());
        requestBuilder.with(request -> {
            request.setMethod(HttpMethod.PUT.name());
            return request;
        });

        mockmvc.perform(requestBuilder
                        .file(getMockUploadFile())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", "Bearer " + getAccessToken()))
                .andExpect(status().isOk());

        /*mockMvc.perform(multipart("/post/"+savePost.getId())

                                .file(getMockUploadFile())
                                .header("Authorization", "Bearer "+ getAccessToken())
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                               )
                .andExpect(status().isOk());
*/

        //then
        String filePath = postRepository.findAll().get(0).getFilePath();
        Assertions.assertThat(filePath).isNotNull();
        Assertions.assertThat(new File(filePath).delete()).isTrue();

    }

    /**
     게시글 수정
     */
    @Autowired private FileService fileService;
    @Test
    public void 게시글_수정_업로드파일제거_성공() throws Exception {
        //given
        Post post = Post.builder().title("수정전제목").content("수정전내용").build();
        post.confirmWriter(member);
        String path = fileService.save(getMockUploadFile());
        post.updateFilePath(path);
        Post savePost = postRepository.save(post);

        Assertions.assertThat(postRepository.findAll().get(0).getFilePath()).isNotNull();


        MockMultipartFile mockUploadFile = getMockUploadFile();


        //when

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        final String UPDATE_CONTENT = "내용";
        final String UPDATE_TITlE = "제목";
        map.add("title", UPDATE_TITlE);
        map.add("content", UPDATE_CONTENT);

        //when
        mockmvc.perform(MockMvcRequestBuilders
                        .put("/post/"+savePost.getId())
                        .header("Authorization", "Bearer "+ getAccessToken())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .params(map))
                .andExpect(status().isOk());


        //then
        Assertions.assertThat(postRepository.findAll().get(0).getContent()).isEqualTo(UPDATE_CONTENT);
        Assertions.assertThat(postRepository.findAll().get(0).getTitle()).isEqualTo(UPDATE_TITlE);
        Assertions.assertThat(postRepository.findAll().get(0).getFilePath()).isNull();

    }



    /**
     * 게시글 수정
     */
    @Test
    public void 게시글_수정_실패_권한없음() throws Exception {
        //given
        Member newMember = memberRepository.save(Member.builder().username("newMEmber1123").password("!23123124421").name("123213").nickName("123").age(22).role(Role.USER).build());
        Post post = Post.builder().title("수정전제목").content("수정전내용").build();
        post.confirmWriter(newMember);
        Post savePost = postRepository.save(post);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        final String UPDATE_CONTENT = "내용";
        final String UPDATE_TITlE = "제목";
        map.add("title", UPDATE_TITlE);
        map.add("content", UPDATE_CONTENT);

        //when
        mockmvc.perform(MockMvcRequestBuilders
                        .put("/post/"+savePost.getId())
                        .header("Authorization", "Bearer "+ getAccessToken())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .params(map))
                .andExpect(status().isForbidden());

        //then
        Assertions.assertThat(postRepository.findAll().get(0).getContent()).isEqualTo("수정전내용");
        Assertions.assertThat(postRepository.findAll().get(0).getTitle()).isEqualTo("수정전제목");
    }



    /**
     * 게시글 삭제
     */
    @Test
    public void 게시글_삭제_성공() throws Exception {
        //given
        Post post = Post.builder().title("수정전제목").content("수정전내용").build();
        post.confirmWriter(member);
        Post savePost = postRepository.save(post);

        //when
        mockmvc.perform(MockMvcRequestBuilders
                .delete("/post/"+savePost.getId())
                .header("Authorization", "Bearer "+ getAccessToken())
        ).andExpect(status().isOk());


        //then
        Assertions.assertThat(postRepository.findAll().size()).isEqualTo(0);

    }

    /**
     * 게시글 삭제
     */
    @Test
    public void 게시글_삭제_실패_권한없음() throws Exception {
        //given
        Member newMember = memberRepository.save(Member.builder().username("newMEmber1123").password("!23123124421").name("123213").nickName("123").age(22).role(Role.USER).build());
        Post post = Post.builder().title("수정전제목").content("수정전내용").build();
        post.confirmWriter(newMember);
        Post savePost = postRepository.save(post);

        //when
        mockmvc.perform(MockMvcRequestBuilders
                .delete("/post/"+savePost.getId())
                .header("Authorization", "Bearer "+ getAccessToken())
        ).andExpect(status().isForbidden());


        //then
        Assertions.assertThat(postRepository.findAll().size()).isEqualTo(1);

    }


    /**
     * 게시글 조회
     */
    @Test
    public void 게시글_조회() throws Exception {

        //given
        Member newMember = memberRepository.save(Member.builder().username("newMEmber1123").password("!23123124421").name("123213").nickName("123").age(22).role(Role.USER).build());
        Post post = Post.builder().title("title").content("content").build();
        post.confirmWriter(newMember);
        Post savePost = postRepository.save(post);

        //when
        MvcResult result = mockmvc.perform(MockMvcRequestBuilders
                .get("/post/" + savePost.getId())
                .characterEncoding(StandardCharsets.UTF_8)
                .header("Authorization", "Bearer " + getAccessToken())
        ).andExpect(status().isOk()).andReturn();

        PostInfoDto postInfoDto = objectMapper.readValue(result.getResponse().getContentAsString(), PostInfoDto.class);

        //then
        Assertions.assertThat(postInfoDto.getPostId()).isEqualTo(post.getId());
        Assertions.assertThat(postInfoDto.getContent()).isEqualTo(post.getContent());
        Assertions.assertThat(postInfoDto.getTitle()).isEqualTo(post.getTitle());


    }


    @Value("${spring.data.web.pageable.default-page-size}")
    private int pageCount;

    /**
     * 게시글 검색
     */
    @Test
    public void 게시글_검색() throws Exception {

        //given
        Member newMember = memberRepository.save(Member.builder().username("newMEmber1123").password("!23123124421").name("123213").nickName("123").age(22).role(Role.USER).build());




        final int POST_COUNT = 50;
        for(int i = 1; i<= POST_COUNT; i++ ){
            Post post = Post.builder().title("title"+ i).content("content"+i).build();
            post.confirmWriter(newMember);
            postRepository.save(post);
        }

        clear();



        //when
        MvcResult result = mockmvc.perform(MockMvcRequestBuilders
                .get("/post")
                .characterEncoding(StandardCharsets.UTF_8)
                .header("Authorization", "Bearer " + getAccessToken())
        ).andExpect(status().isOk()).andReturn();

        //then
        PostPagingDto postList = objectMapper.readValue(result.getResponse().getContentAsString(), PostPagingDto.class);

        assertThat(postList.getTotalElementCount()).isEqualTo(POST_COUNT);
        assertThat(postList.getCurrentPageElementCount()).isEqualTo(pageCount);
        assertThat(postList.getSimpleLectureDtoList().get(0).getContent()).isEqualTo("content50");

    }
}