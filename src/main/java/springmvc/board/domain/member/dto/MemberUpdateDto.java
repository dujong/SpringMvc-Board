package springmvc.board.domain.member.dto;

import springmvc.board.domain.member.Member;

import javax.swing.text.html.Option;
import java.util.Optional;


public record MemberUpdateDto(
        Optional<String> name,
        Optional<String> nickName,
        Optional<Integer> age) {

}
