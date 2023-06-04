package springmvc.board.repository.member;

import springmvc.board.domain.member.Member;

import java.util.List;

public interface MemberRepository_old {

    public Member save(Member member);

    public Member findById(String memberId);

    public List<Member> findAll();

    public void update(String memberId, String memberPassword);

    public void delete(String memberId);
}