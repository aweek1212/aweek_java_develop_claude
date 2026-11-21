package shop.service;

import shop.domain.Member;
import shop.repository.MemberRepository;

public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findMember(long memberId) {
        return memberRepository.findById(memberId);
    }

    public Member[] findMembers() {
        return memberRepository.findAll();
    }
}
