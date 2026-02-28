package domain.service;

import domain.entity.Member;
import domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member create(Member member) {
        return memberRepository.save(member);
    }

    public Member get(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("member not found"));
    }

    public List<Member> getAll() {
        return memberRepository.findAll();
    }

    public void delete(Long id) {
        memberRepository.deleteById(id);
    }
}
