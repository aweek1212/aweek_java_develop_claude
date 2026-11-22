package shop.service;

import shop.domain.Member;
import shop.repository.MemberRepository;
import shop.util.StringUtil;

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

    public Member join(String name, String email) {
        // 1. 이름이나 이메일이 비어 있으면 실패
        if (StringUtil.isBlank(name) || StringUtil.isBlank(email)) {
            System.out.println("[가입 실패] 이름과 이메일은 필수입니다.");
            return null;
        }

        // 2. 같은 이메일로 이미 가입한 회원이 있으면 실패
        if (memberRepository.findByEmail(email) != null) {
            System.out.println("[가입 실패] 이미 가입된 이메일입니다: " + email);
            return null;
        }

        // 3. 새 회원을 만들어 저장소에 저장
        Member member = new Member(name, email);
        Member savedMember = memberRepository.save(member);
        if (savedMember == null) {
            System.out.println("[가입 실패] 저장 공간이 부족합니다.");
        }
        return savedMember;
    }

}

