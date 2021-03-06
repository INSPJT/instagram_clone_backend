package our.yurivongella.instagramclone.service;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import our.yurivongella.instagramclone.controller.dto.FollowRequestDto;
import our.yurivongella.instagramclone.controller.dto.SignupRequestDto;
import our.yurivongella.instagramclone.domain.follow.Follow;
import our.yurivongella.instagramclone.domain.follow.FollowRepository;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.member.MemberRepository;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("회원 테스트")
@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private MemberRepository memberRepository;

    private final String myName = "test1";
    private final String myNickName = "testNickname1";
    private final String myEmail = "memberService1@test.net1";
    private final String myPassword = "1q2w3e4r1";

    private final String targetName = "test2";
    private final String targetNickName = "testNickname2";
    private final String targetEmail = "memberService2@test.net2";
    private final String targetPassword = "1q2w3e4r2";

    @BeforeEach
    public void signupBeforeTest() {
        // 두명 가입시키기
        SignupRequestDto signupRequestDto1 = SignupRequestDto.builder()
                .name(myName)
                .nickName(myNickName)
                .email(myEmail)
                .password(myPassword)
                .build();

        SignupRequestDto signupRequestDto2 = SignupRequestDto.builder()
                .name(targetName)
                .nickName(targetNickName)
                .email(targetEmail)
                .password(targetPassword)
                .build();

        SignupRequestDto signupRequestDto3 = SignupRequestDto.builder()
                .name(targetName + 3)
                .nickName(targetNickName + 3)
                .email(targetEmail + 3)
                .password(targetPassword + 3)
                .build();

        authService.signup(signupRequestDto1);
        authService.signup(signupRequestDto2);
        authService.signup(signupRequestDto3);
    }

    @DisplayName("팔로우 테스트")
    @Nested
    class FollowTest {
        private Long targetId;

        @BeforeEach
        public void findMemberId() {
            Long myId = memberRepository.findByEmail(myEmail).map(Member::getId).get();
            targetId = memberRepository.findByEmail(targetEmail).map(Member::getId).get();

            // 한명 로그인 처리
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(myId, "", Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        @DisplayName("팔로우 성공")
        @Test
        public void successFollow() {
            // given
            FollowRequestDto followRequestDto = FollowRequestDto.builder()
                    .id(targetId)
                    .build();

            // when
            memberService.follow(followRequestDto);

            // then
            List<Follow> follows = followRepository.findByToMemberId(targetId);

            assertThat(follows.size()).isEqualTo(1);

            Member targetMember = follows.get(0).getToMember();
            assertThat(targetMember.getNickName()).isEqualTo(targetNickName);
            assertThat(targetMember.getName()).isEqualTo(targetName);
            assertThat(targetMember.getEmail()).isEqualTo(targetEmail);
            assertThat(targetMember.getFollowers().size()).isEqualTo(1);

            Member myMember = follows.get(0).getFromMember();
            assertThat(myMember.getNickName()).isEqualTo(myNickName);
            assertThat(myMember.getName()).isEqualTo(myName);
            assertThat(myMember.getEmail()).isEqualTo(myEmail);
            assertThat(myMember.getFollowings().size()).isEqualTo(1);
        }

        @DisplayName("이미 팔로우 중임")
        @Test
        public void alreadyFollow() {
            // given
            FollowRequestDto followRequestDto = FollowRequestDto.builder()
                    .id(targetId)
                    .build();

            // when
            memberService.follow(followRequestDto);

            // then
            Assertions.assertThrows(
                    RuntimeException.class,
                    () -> memberService.follow(followRequestDto)
            );
        }
    }

    @DisplayName("언팔로우 테스트")
    @Nested
    class UnFollowTest {
        private Long targetId;

        @BeforeEach
        public void findMemberId() {
            Long myId = memberRepository.findByEmail(myEmail).map(Member::getId).get();
            targetId = memberRepository.findByEmail(targetEmail).map(Member::getId).get();

            // 한명 로그인 처리
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(myId, "", Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 언팔로우 테스트를 위해 미리 팔로우 처리
            FollowRequestDto followRequestDto = FollowRequestDto.builder()
                    .id(targetId)
                    .build();

            memberService.follow(followRequestDto);
        }

        @DisplayName("언팔로우 성공")
        @Test
        public void successUnFollow() {
            // given
            FollowRequestDto followRequestDto = FollowRequestDto.builder()
                    .id(targetId)
                    .build();

            // when
            memberService.unFollow(followRequestDto);

            // then
            List<Follow> follows = followRepository.findByToMemberId(targetId);
            assertThat(follows.size()).isEqualTo(0);
        }

        @DisplayName("팔로우 중이지 않아서 실패")
        @Test
        public void notFollowingTarget() {
            // given
            FollowRequestDto followRequestDto = FollowRequestDto.builder()
                    .id(targetId)
                    .build();

            // when
            memberService.unFollow(followRequestDto);

            // then
            Assertions.assertThrows(
                    RuntimeException.class,
                    () -> memberService.unFollow(followRequestDto)
            );
        }
    }

    @DisplayName("팔로워/팔로잉 리스트 가져오기")
    @Nested
    class FollowerAndFollowingTest {

        @BeforeEach
        public void followBeforeTest() {
            Member member1 = memberRepository.findByEmail(myEmail).get();
            Member member2 = memberRepository.findByEmail(targetEmail).get();
            Member member3 = memberRepository.findByEmail(targetEmail + 3).get();

            followRepository.save(Follow.builder().fromMember(member1).toMember(member2).build());
            followRepository.save(Follow.builder().fromMember(member1).toMember(member3).build());
            followRepository.save(Follow.builder().fromMember(member2).toMember(member1).build());

            // 한명 로그인 처리
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(member1.getId(), "", Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        @DisplayName("내 팔로워 가져오기")
        @Test
        public void getFollowersTest() {
            assertThat(memberService.getFollowers().size()).isEqualTo(1);
        }

        @DisplayName("내 팔로우 중인 대상들 가져오기")
        @Test
        public void getFollowingTest() {
            assertThat(memberService.getFollowings().size()).isEqualTo(2);
        }
    }
}