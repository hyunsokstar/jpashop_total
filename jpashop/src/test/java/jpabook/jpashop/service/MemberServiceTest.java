package jpabook.jpashop.service;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    // 서비스 계층의 회원 가입 함수 테스트
    // 1. 멤버 엔티티 정보를 초기화
    // 2. 초기화한 멤버 엔티티 정보를 인자로 서비스 계층의 회원 가입 함수를 호출하고 아이디 반환 받기
    // 3. 받환 받은 아이디가 인자로 넘긴 멤버 엔티티의 id와 동일한지 비교
    @Test
    public void 회원가입() throws Exception {
        Member member = new Member();
        member.setName("kim");

        Long saveId = memberService.join(member);

        assertEquals(member, memberRepository.findOne(saveId));
    }

    // 서비스 계층의 중복 회원 가입 함수 방지 테스트
    // 1. 두명의 회원 정보 초기화 (멤버 엔티티 객체 사용)
    // 2. 두명의 회원 정보에 대해 연달아 회원 가입 함수 실행
    // 3. 두번째 회원 가입시에 에러가 발생해야 성공 하도록 설정
    @Test
    public void 중복회원가입방지() throws Exception {
        // 동일한 회원 가입 정보로
        Member member1 = new Member();
        member1.setName("hyun");

        Member member2 = new Member();
        member2.setName("hyun");

        // 연속해서 회원 가입 시도를 할경우 2번째 시도에서 에러가 발생 해야 한다.
        memberService.join(member1);
        try{
            memberService.join(member2);
        } catch (IllegalStateException e){
            System.out.println("중복 회원 가입 에러 발생!!");

            // 예외가 발생할 경우 return 시켜 fail 함수가 실행되지 않도록 한다.
            return;
        }
        fail("예외가 발생해야 한다.");
    }


}
