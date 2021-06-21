package our.yurivongella.instagramclone.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import our.yurivongella.instagramclone.controller.dto.profile.ProfilePostDto;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("프로필 정보 조회 테스트")
@Transactional
@SpringBootTest
class ProfileServiceTest {
    @MockBean
    private S3Service s3Service;

    @Autowired
    private ProfileService profileService;

    private static final Long myMemberId = 2L;
    private static final String followDisplayId = "bob";

    @BeforeEach
    public void loginBeforeTest() {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(myMemberId, "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @DisplayName("내 게시글 리스트 조건에 맞는 갯수로 잘 가져오는 지 확인")
    @Test
    void getMyPostsTest() {
        List<ProfilePostDto> myPosts1 = profileService.getMyPosts(0L);
        assertThat(myPosts1.size()).isEqualTo(0);

        List<ProfilePostDto> myPosts2 = profileService.getMyPosts(11L);
        assertThat(myPosts2.size()).isEqualTo(5);

        List<ProfilePostDto> myPosts3 = profileService.getMyPosts(null);
        assertThat(myPosts3.size()).isEqualTo(12);
    }

    @DisplayName("특정 사용자 게시글 리스트 가져오기")
    @Test
    void getPostsTest() {
        List<ProfilePostDto> otherPosts1 = profileService.getPosts(followDisplayId, 0L);
        assertThat(otherPosts1.size()).isEqualTo(0);

        List<ProfilePostDto> otherPosts2 = profileService.getPosts(followDisplayId, null);
        assertThat(otherPosts2.size()).isEqualTo(8);
    }

    @DisplayName("게시글 좋아요 여부 테스트")
    @Test
    void getPostLikeTest() {
        List<ProfilePostDto> posts = profileService.getPosts(followDisplayId, 19L);
        assertThat(posts.size()).isEqualTo(1);

        ProfilePostDto profilePostDto = posts.get(0);
        assertThat(profilePostDto.getIsLike()).isTrue();
    }
}
