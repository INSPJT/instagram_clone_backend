package our.yurivongella.instagramclone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import our.yurivongella.instagramclone.controller.dto.member.SignupReqDto;
import our.yurivongella.instagramclone.controller.dto.post.PostDto;
import our.yurivongella.instagramclone.controller.dto.post.PostReqDto;
import our.yurivongella.instagramclone.entity.Follow;
import our.yurivongella.instagramclone.entity.Member;
import our.yurivongella.instagramclone.repository.FollowRepository;
import our.yurivongella.instagramclone.repository.MemberRepository;

import java.util.Collections;
import java.util.List;

@Transactional
@SpringBootTest
public abstract class TestBase {

    @MockBean
    protected S3Service s3Service;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected FollowRepository followRepository;

    @Autowired
    protected AuthService authService;

    @Autowired
    protected PostService postService;

    /* 테스트 계정 가입하기 */
    public Member signup(String displayId, String email) {
        SignupReqDto signupReqDto = SignupReqDto.builder()
                                                .displayId(displayId)
                                                .email(email)
                                                .password("1q2w3e4r")
                                                .nickname("test nickname")
                                                .build();

        authService.signup(signupReqDto);
        return memberRepository.findByEmail(email).get();
    }

    /* 테스트 계정 로그인 시키기 */
    public void login(Long memberId) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberId, "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /* 테스트 계정 로그인 시키기 */
    public Member login(String displayId) {
        Member member = memberRepository.findByDisplayId(displayId).get();
        login(member.getId());
        return member;
    }

    /* 테스트 계정 가입 및 로그인 */
    public Member signupAndLogin(String displayId, String email) {
        Member member = signup(displayId, email);
        login(member.getId());
        return member;
    }

    /* 게시글 생성하기 */
    public PostDto createPost(String content) {
        return postService.createPost(new PostReqDto(content, Collections.emptyList()));
    }

    public PostDto createPostWithImageUrls(String content, List<String> imageUrls) {
        return postService.createPost(new PostReqDto(content, imageUrls));
    }

    /* 팔로우 하기 */
    public void follow(Long fromMemberId, Long toMemberId) {
        Member fromMember = memberRepository.findById(fromMemberId).get();
        Member toMember = memberRepository.findById(toMemberId).get();

        Follow follow = new Follow(fromMember, toMember);
        followRepository.save(follow);
    }

    /* 팔로우 하기 */
    public void follow(String fromDisplayId, String toDisplayId) {
        Member fromMember = memberRepository.findByDisplayId(fromDisplayId).get();
        Member toMember = memberRepository.findByDisplayId(toDisplayId).get();

        Follow follow = new Follow(fromMember, toMember);
        followRepository.save(follow);
    }
}
