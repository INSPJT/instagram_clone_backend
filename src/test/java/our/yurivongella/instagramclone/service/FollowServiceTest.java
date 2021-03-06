package our.yurivongella.instagramclone.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import our.yurivongella.instagramclone.controller.dto.member.MemberResDto;
import our.yurivongella.instagramclone.controller.dto.member.SignupReqDto;
import our.yurivongella.instagramclone.entity.Follow;
import our.yurivongella.instagramclone.repository.FollowRepository;
import our.yurivongella.instagramclone.entity.Member;
import our.yurivongella.instagramclone.repository.MemberRepository;
import our.yurivongella.instagramclone.util.SecurityUtil;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("회원 테스트")
@Transactional
@SpringBootTest
class FollowServiceTest {
    @MockBean
    private S3Service s3Service;

    @Autowired
    private AuthService authService;

    @Autowired
    private FollowService followService;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private MemberRepository memberRepository;

    private final String myDisplayId = "test1";
    private final String myNickname = "testNickname1";
    private final String myEmail = "memberService1@test.net1";
    private final String myPassword = "1q2w3e4r1";

    private final String targetDisplayId = "test2";
    private final String targetNickname = "testNickname2";
    private final String targetEmail = "memberService2@test.net2";
    private final String targetPassword = "1q2w3e4r2";

    @BeforeEach
    public void signupBeforeTest() {
        // 두명 가입시키기
        SignupReqDto signupReqDto1 = SignupReqDto.builder()
                                                 .displayId(myDisplayId)
                                                 .nickname(myNickname)
                                                 .email(myEmail)
                                                 .password(myPassword)
                                                 .build();

        SignupReqDto signupReqDto2 = SignupReqDto.builder()
                                                 .displayId(targetDisplayId)
                                                 .nickname(targetNickname)
                                                 .email(targetEmail)
                                                 .password(targetPassword)
                                                 .build();

        SignupReqDto signupReqDto3 = SignupReqDto.builder()
                                                 .displayId(targetDisplayId + 3)
                                                 .nickname(targetNickname + 3)
                                                 .email(targetEmail + 3)
                                                 .password(targetPassword + 3)
                                                 .build();

        authService.signup(signupReqDto1);
        authService.signup(signupReqDto2);
        authService.signup(signupReqDto3);
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
            // when
            followService.follow(targetDisplayId);
            Member member = memberRepository.findById(targetId).get();

            // then
            List<Follow> follows = followRepository.findByToMember(member);

            assertThat(follows.size()).isEqualTo(1);

            Member targetMember = follows.get(0).getToMember();
            assertThat(targetMember.getNickname()).isEqualTo(targetNickname);
            assertThat(targetMember.getDisplayId()).isEqualTo(targetDisplayId);
            assertThat(targetMember.getEmail()).isEqualTo(targetEmail);
            assertThat(targetMember.getFollowers().size()).isEqualTo(1);
            assertThat(targetMember.getFollowerCount()).isEqualTo(1);

            Member myMember = follows.get(0).getFromMember();
            assertThat(myMember.getNickname()).isEqualTo(myNickname);
            assertThat(myMember.getDisplayId()).isEqualTo(myDisplayId);
            assertThat(myMember.getEmail()).isEqualTo(myEmail);
            assertThat(myMember.getFollowings().size()).isEqualTo(1);
            assertThat(myMember.getFollowingCount()).isEqualTo(1);
        }

        @DisplayName("이미 팔로우 중임")
        @Test
        public void alreadyFollow() {
            // when
            followService.follow(targetDisplayId);

            // then
            Assertions.assertThrows(
                    RuntimeException.class,
                    () -> followService.follow(targetDisplayId)
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
            followService.follow(targetDisplayId);
        }

        @DisplayName("언팔로우 성공")
        @Test
        public void successUnFollow() {
            // given
            Member myMember = memberRepository.findById(SecurityUtil.getCurrentMemberId()).get();
            Member targetMember = memberRepository.findById(targetId).get();
            assertThat(myMember.getFollowingCount()).isEqualTo(1);
            assertThat(targetMember.getFollowerCount()).isEqualTo(1);

            // when
            followService.unFollow(targetDisplayId);

            // then
            assertThat(myMember.getFollowingCount()).isEqualTo(0);
            assertThat(targetMember.getFollowerCount()).isEqualTo(0);
            List<Follow> follows = followRepository.findByToMember(targetMember);
            assertThat(follows.size()).isEqualTo(0);
        }

        @DisplayName("팔로우 중이지 않아서 실패")
        @Test
        public void notFollowingTarget() {
            // when
            followService.unFollow(targetDisplayId);

            // then
            Assertions.assertThrows(
                    RuntimeException.class,
                    () -> followService.unFollow(targetDisplayId)
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
            List<MemberResDto> followers = followService.getFollowers();
            assertThat(followers.get(0).getDisplayId()).isEqualTo(targetDisplayId);
            assertThat(followers.size()).isEqualTo(1);
        }

        @DisplayName("내 팔로우 중인 대상들 가져오기")
        @Test
        public void getFollowingTest() {
            List<MemberResDto> followings = followService.getFollowings();
            assertThat(followings.get(0).getDisplayId()).isEqualTo(targetDisplayId);
            assertThat(followings.get(1).getDisplayId()).isEqualTo(targetDisplayId + 3);
            assertThat(followings.size()).isEqualTo(2);
        }
    }
}
