package springmvc.board.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import springmvc.board.domain.member.Member;
import springmvc.board.domain.member.dto.MemberSignUpDto;
import springmvc.board.domain.member.repository.MemberRepository;
import springmvc.board.domain.member.service.MemberService;

import javax.persistence.EntityManager;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    EntityManager em;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired PasswordEncoder passwordEncoder;

    private static String SIGN_UP_URL = "/signUp";

    private String username = "username";
    private String password = "password1234@";
    private String name = "shinD";
    private String nickName = "shinD cute";
    private Integer age = 22;

    private void clear(){
        em.flush();
        em.clear();
    }

    private void signUp(String signUpData) throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .post(SIGN_UP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpData))
                .andExpect(status().isOk());
    }

    @Value("${jwt.access.header}")
    private String accessHeader;
    private static final String BEARER = "Bearer ";

    private String getAccessToken() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(map)))
                .andExpect(status().isOk()).andReturn();

        return mvcResult.getResponse().getHeader(accessHeader);
    }

    @Test
    public void 회원정보수정_성공() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickName, age));

        signUp(signUpData);

        String accessToken = getAccessToken();
        Map<String, Object> map = new HashMap<>();
        map.put("name",name+"변경");
        map.put("nickName",nickName+"변경");
        map.put("age",age+1);
        String updateMemberData = objectMapper.writeValueAsString(map);


        //when
        mockMvc.perform(MockMvcRequestBuilders.
                        put("/member")
                                .header(accessHeader,BEARER+accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateMemberData))
                .andExpect(status().isOk());

        //then
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new Exception("회원이 없습니다"));
        assertThat(member.getName()).isEqualTo(name+"변경");
        assertThat(member.getNickName()).isEqualTo(nickName+"변경");
        assertThat(member.getAge()).isEqualTo(age+1);
        assertThat(memberRepository.findAll().size()).isEqualTo(1);

    }



    @Test
    public void 회원정보수정_원하는필드만변경_성공() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickName, age));
        signUp(signUpData);

        String accessToken = getAccessToken();
        Map<String, Object> map = new HashMap<>();
        map.put("name",name+"변경");
        String updateMemberData = objectMapper.writeValueAsString(map);


        //when
        mockMvc.perform(MockMvcRequestBuilders.
                        put("/member")
                                .header(accessHeader,BEARER+accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateMemberData))
                .andExpect(status().isOk());

        //then
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new Exception("회원이 없습니다"));
        assertThat(member.getName()).isEqualTo(name+"변경");
        assertThat(member.getNickName()).isEqualTo(nickName);
        assertThat(member.getAge()).isEqualTo(age);
        assertThat(memberRepository.findAll().size()).isEqualTo(1);

    }



    @Test
    public void updatePassword_success() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickName, age));
        signUp(signUpData);

        String accessToken = getAccessToken();

        Map<String, String> map = new HashMap<>();
        map.put("checkPassword",password);
        map.put("toBePassword",password+"!@#@!#@!#");

        String updatePassword = objectMapper.writeValueAsString(map);
        log.info("updatePassword Json={}", updatePassword);

        //when
        mockMvc.perform(MockMvcRequestBuilders
                .put("/member/password")
                                .header(accessHeader,BEARER+accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatePassword))
                .andExpect(status().isOk());

        //then
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new Exception("회원이 없습니다"));
        assertThat(passwordEncoder.matches(password, member.getPassword())).isFalse();
        assertThat(passwordEncoder.matches(password+"!@#@!#@!#", member.getPassword())).isTrue();
    }




    @Test
    public void 비밀번호수정_실패_검증비밀번호가_틀림() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickName, age));
        signUp(signUpData);

        String accessToken = getAccessToken();

        Map<String, Object> map = new HashMap<>();
        map.put("checkPassword",password+"1");
        map.put("tobePassword",password+"!@#@!#@!#");

        String updatePassword = objectMapper.writeValueAsString(map);


        //when
        mockMvc.perform(MockMvcRequestBuilders
                .put("/member/password")
                                .header(accessHeader,BEARER+accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatePassword))
                .andExpect(status().isOk());

        //then
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new Exception("회원이 없습니다"));
        assertThat(passwordEncoder.matches(password, member.getPassword())).isFalse();
        assertThat(passwordEncoder.matches(password+"!@#@!#@!#", member.getPassword())).isFalse();
    }




    @Test
    public void 비밀번호수정_실패_바꾸려는_비밀번호_형식_올바르지않음() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickName, age));
        signUp(signUpData);

        String accessToken = getAccessToken();

        Map<String, Object> map = new HashMap<>();
        map.put("checkPassword",password);
        map.put("toBePassword","123123");

        String updatePassword = objectMapper.writeValueAsString(map);


        //when
        mockMvc.perform(MockMvcRequestBuilders.
                        put("/member/password")
                                .header(accessHeader,BEARER+accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatePassword))
                .andExpect(status().isOk());

        //then
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new Exception("회원이 없습니다"));
        assertThat(passwordEncoder.matches(password, member.getPassword())).isTrue();
        assertThat(passwordEncoder.matches("123123", member.getPassword())).isFalse();
    }




    @Test
    public void 회원탈퇴_성공() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickName, age));
        signUp(signUpData);

        String accessToken = getAccessToken();

        Map<String, Object> map = new HashMap<>();
        map.put("checkPassword",password);

        String updatePassword = objectMapper.writeValueAsString(map);

        //when
        mockMvc.perform(MockMvcRequestBuilders.
                        delete("/member")
                                .header(accessHeader,BEARER+accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatePassword))
                .andExpect(status().isOk());

        //then
        assertThrows(Exception.class, () -> memberRepository.findByUsername(username).orElseThrow(() -> new Exception("회원이 없습니다")));
    }




    @Test
    public void 회원탈퇴_실패_비밀번호틀림() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickName, age));
        signUp(signUpData);

        String accessToken = getAccessToken();

        Map<String, Object> map = new HashMap<>();
        map.put("checkPassword",password+11);

        String updatePassword = objectMapper.writeValueAsString(map);

        //when
        mockMvc.perform(MockMvcRequestBuilders.
                        delete("/member")
                                .header(accessHeader,BEARER+accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatePassword))
                .andExpect(status().isOk());

        //then
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new Exception("회원이 없습니다"));
        assertThat(member).isNotNull();


    }



    @Test
    public void 회원탈퇴_실패_권한이없음() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickName, age));
        signUp(signUpData);

        String accessToken = getAccessToken();

        Map<String, Object> map = new HashMap<>();
        map.put("checkPassword",password);

        String updatePassword = objectMapper.writeValueAsString(map);

        //when
        mockMvc.perform(MockMvcRequestBuilders.
                        delete("/member")
                                .header(accessHeader,BEARER+accessToken+"1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatePassword))
                .andExpect(status().isForbidden());

        //then
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new Exception("회원이 없습니다"));
        assertThat(member).isNotNull();
    }




    @Test
    public void 내정보조회_성공() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickName, age));
        signUp(signUpData);

        String accessToken = getAccessToken();


        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        get("/member")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .header(accessHeader, BEARER + accessToken))
                .andExpect(status().isOk()).andReturn();


        //then
        Map<String, Object> map = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new Exception("회원이 없습니다"));
        assertThat(member.getAge()).isEqualTo(map.get("age"));
        assertThat(member.getUsername()).isEqualTo(map.get("username"));
        assertThat(member.getName()).isEqualTo(map.get("name"));
        assertThat(member.getNickName()).isEqualTo(map.get("nickName"));

    }



    @Test
    public void 내정보조회_실패_JWT없음() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickName, age));
        signUp(signUpData);

        String accessToken = getAccessToken();


        //when,then
        mockMvc.perform(MockMvcRequestBuilders.
                        get("/member")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .header(accessHeader, BEARER + accessToken+1))
                .andExpect(status().isForbidden());

    }



    /**
     * 회원정보조회 성공
     * 회원정보조회 실패 -> 회원이없음
     * 회원정보조회 실패 -> 권한이없음
     */
    @Test
    public void 회원정보조회_성공() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickName, age));
        signUp(signUpData);

        String accessToken = getAccessToken();

        Long id = memberRepository.findAll().get(0).getId();

        //when

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        get("/member/"+id)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .header(accessHeader, BEARER + accessToken))
                .andExpect(status().isOk()).andReturn();


        //then
        Map<String, Object> map = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new Exception("회원이 없습니다"));
        assertThat(member.getAge()).isEqualTo(map.get("age"));
        assertThat(member.getUsername()).isEqualTo(map.get("username"));
        assertThat(member.getName()).isEqualTo(map.get("name"));
        assertThat(member.getNickName()).isEqualTo(map.get("nickName"));
    }



    @Test
    public void 회원정보조회_실패_없는회원조회() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickName, age));
        signUp(signUpData);

        String accessToken = getAccessToken();


        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                        get("/member/2211")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .header(accessHeader, BEARER + accessToken))
                .andExpect(status().isOk()).andReturn();

        //then
        assertThat(result.getResponse().getContentAsString()).isEqualTo("");//빈 문자열
    }



    @Test
    public void 회원정보조회_실패_JWT없음() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickName, age));
        signUp(signUpData);

        String accessToken = getAccessToken();


        //when,then
        mockMvc.perform(MockMvcRequestBuilders.
                        get("/member/1")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .header(accessHeader, BEARER + accessToken+1))
                .andExpect(status().isForbidden());

    }

}