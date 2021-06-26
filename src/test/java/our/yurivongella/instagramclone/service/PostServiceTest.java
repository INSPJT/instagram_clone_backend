package our.yurivongella.instagramclone.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import our.yurivongella.instagramclone.controller.dto.ProcessStatus;
import our.yurivongella.instagramclone.controller.dto.member.MemberResDto;
import our.yurivongella.instagramclone.controller.dto.post.MediaUrlDto;
import our.yurivongella.instagramclone.controller.dto.post.PostDto;
import our.yurivongella.instagramclone.controller.dto.post.PostResDto;
import our.yurivongella.instagramclone.entity.Post;
import our.yurivongella.instagramclone.repository.post.PostRepository;
import our.yurivongella.instagramclone.exception.CustomException;
import our.yurivongella.instagramclone.exception.ErrorCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("게시글 Service 테스트")
public class PostServiceTest extends TestBase {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void loginBeforeTest() {
        signupAndLogin("testPost", "testPost@test.net");
    }

    @DisplayName("게시글 생성")
    @Test
    void testCreate() {
        // given
        Long postId = createPostWithImageUrls("test post", Arrays.asList("image1.jpg", "image2.jpg", "image3.jpg")).getId();

        // when
        PostDto postDto = postService.getPost(postId);

        // then
        assertThat(postDto.getId()).isEqualTo(postId);
        assertThat(postDto.getContent()).isEqualTo("test post");
        assertThat(postDto.getAuthor().getDisplayId()).isEqualTo("testPost");
        assertThat(postDto.getAuthor().getPostCount()).isEqualTo(1);

        List<String> imageUrls = postDto.getMediaUrls().stream().map(MediaUrlDto::getUrl).collect(Collectors.toList());
        List<String> types = postDto.getMediaUrls().stream().map(MediaUrlDto::getType).collect(Collectors.toList());
        assertThat(imageUrls).containsExactly("image1.jpg", "image2.jpg", "image3.jpg");
        assertThat(types).containsExactly("IMAGE", "IMAGE", "IMAGE");

        assertThat(postDto.getLikeCount()).isEqualTo(0L);
        assertThat(postDto.getCommentCount()).isEqualTo(0L);
        assertThat(postDto.getViewCount()).isEqualTo(1L);
    }

    @DisplayName("게시글 삭제 테스트")
    @Nested
    class deletePostTest {

        @DisplayName("게시글 삭제 성공")
        @Test
        void successDelete() {
            // given
            Long postId = createPost("test post").getId();
            Post deletedPost = postRepository.findById(postId).get();

            // when
            ProcessStatus processStatus = postService.deletePost(postId);

            // then
            assertThat(processStatus).isEqualTo(ProcessStatus.SUCCESS);
            assertThat(postRepository.findById(postId)).isEmpty();
            assertThat(deletedPost.getMember().getPostCount()).isEqualTo(0);
        }

        @DisplayName("삭제 대상이 없으면 실패")
        @Test
        void failWhenNonExistentPost() {
            CustomException ex = assertThrows(CustomException.class, () -> postService.deletePost(-1L));
            Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, ex.getErrorCode());
        }

        @DisplayName("내 게시글이 아니면 실패")
        @Test
        void failWhenNotMyPost() {
            // given
            Long postId = createPost("test post").getId();
            Post deletedPost = postRepository.findById(postId).get();

            // when
            signupAndLogin("other", "other@test.net");
            ProcessStatus processStatus = postService.deletePost(postId);

            // then
            assertThat(processStatus).isEqualTo(ProcessStatus.FAIL);
            assertThat(postRepository.findById(postId)).isPresent();
            assertThat(deletedPost.getMember().getPostCount()).isEqualTo(1);
        }
    }

    @DisplayName("좋아요 테스트")
    @Nested
    class likePostTest {

        @DisplayName("좋아요 성공")
        @Test
        void successLike() {
            // given
            Long postId = createPost("test post").getId();

            // when
            ProcessStatus processStatus = postService.likePost(postId);

            // then
            Post post = postRepository.findById(postId).get();
            assertThat(processStatus).isEqualTo(ProcessStatus.SUCCESS);
            assertThat(post.getLikeCount()).isEqualTo(1);
        }

        @DisplayName("존재하지 않는 게시글에 좋아요 하려고 하면 실패")
        @Test
        void failWhenNonExistentPost() {
            CustomException ex = assertThrows(CustomException.class, () -> postService.likePost(-1L));
            assertEquals(ErrorCode.POST_NOT_FOUND, ex.getErrorCode());
        }

        @DisplayName("이미 좋아요 되어 있으면 실패")
        @Test
        void failWhenAlreadyLiked() {
            // given
            Long postId = createPost("test post").getId();

            // when
            postService.likePost(postId);

            // then
            CustomException ex = assertThrows(CustomException.class, () -> postService.likePost(postId));
            assertEquals(ErrorCode.ALREADY_LIKE, ex.getErrorCode());
        }
    }

    @DisplayName("좋아요 취소 테스트")
    @Nested
    class unlikeCommentTest {

        @DisplayName("좋아요 취소 성공")
        @Test
        void successUnlike() {
            // given
            Long postId = createPost("test post").getId();
            postService.likePost(postId);

            // when
            ProcessStatus processStatus = postService.unlikePost(postId);

            // then
            Post post = postRepository.findById(postId).get();
            assertThat(processStatus).isEqualTo(ProcessStatus.SUCCESS);
            assertThat(post.getLikeCount()).isEqualTo(0);
        }

        @DisplayName("존재하지 않는 게시글에 좋아요 취소 하려고 하면 실패")
        @Test
        void failWhenNonExistentPost() {
            CustomException ex = assertThrows(CustomException.class, () -> postService.unlikePost(-1L));
            assertEquals(ErrorCode.POST_NOT_FOUND, ex.getErrorCode());
        }

        @DisplayName("좋아요 되어있지 않으면 실패")
        @Test
        void failWhenAlreadyUnlike() {
            // given
            Long postId = createPost("test post").getId();

            // when
            CustomException ex = assertThrows(CustomException.class, () -> postService.unlikePost(postId));

            // then
            assertEquals(ErrorCode.ALREADY_UNLIKE, ex.getErrorCode());
        }
    }

    @DisplayName("null 체크")
    @Nested
    class NullPostIdTest {

        @DisplayName("postId = null 로 들어오는 post 삭제 실패")
        @Test
        void delete() {
            Exception exception = assertThrows(Exception.class, () -> postService.deletePost(null));
            assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
            assertEquals("The given id must not be null!", exception.getCause().getMessage());
        }

        @DisplayName("postId = null 로 들어오는 post 좋아요 실패")
        @Test
        void like() {
            Exception exception = assertThrows(Exception.class, () -> postService.likePost(null));
            assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
            assertEquals("The given id must not be null!", exception.getCause().getMessage());
        }

        @DisplayName("postId = null 로 들어오는 post 좋아요 취소 실패")
        @Test
        void unlike() {
            Exception exception = assertThrows(Exception.class, () -> postService.unlikePost(null));
            assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
            assertEquals("The given id must not be null!", exception.getCause().getMessage());
        }
    }

    @DisplayName("내 정보 조회")
    @Nested
    class GetMyPostsClass {

        private static final int MAX_PAGE_SIZE = 12;

        @DisplayName("내 게시글 없을 때")
        @Test
        void readSuccessNoPosts() {
            PostResDto postResDto1 = postService.getMyPosts(null);
            assertThat(postResDto1.getHasNext()).isFalse();
            assertThat(postResDto1.getPosts()).isEmpty();

            PostResDto postResDto2 = postService.getMyPosts(Long.MAX_VALUE);
            assertThat(postResDto2.getHasNext()).isFalse();
            assertThat(postResDto2.getPosts()).isEmpty();
        }

        @DisplayName("게시글 20 개 생성해두고 MAX_PAGE_SIZE 인 12 를 경계값으로 테스트")
        @Test
        void readSuccessPaging() {
            // MAX_PAGE_SIZE 보다 많은 게시글 생성
            List<Long> postIds = new ArrayList<>();

            for (int i = 0; i < 20; i++) {
                Long postId = createPost("test post").getId();
                postIds.add(postId);
            }

            // 11 개 호출
            Long postId11 = postIds.get(MAX_PAGE_SIZE - 1);
            PostResDto postResDto11 = postService.getMyPosts(postId11);
            assertThat(postResDto11.getHasNext()).isFalse();
            assertThat(postResDto11.getPosts().size()).isEqualTo(MAX_PAGE_SIZE - 1);

            // 12 개 호출
            Long postId12 = postIds.get(MAX_PAGE_SIZE);
            PostResDto postResDto12 = postService.getMyPosts(postId12);
            assertThat(postResDto12.getHasNext()).isFalse();
            assertThat(postResDto12.getPosts().size()).isEqualTo(MAX_PAGE_SIZE);

            // 최대로 호출
            PostResDto postResDto13 = postService.getMyPosts(null);
            assertThat(postResDto13.getHasNext()).isTrue();
            assertThat(postResDto13.getPosts().size()).isEqualTo(MAX_PAGE_SIZE);
        }
    }

    @DisplayName("특정 계정 정보 조회")
    @Nested
    class GetPostsClass {

        private static final int MAX_PAGE_SIZE = 12;

        @DisplayName("상대방 게시글 없을 때")
        @Test
        void readSuccessNoPosts() {
            signupAndLogin("other", "other@test.net");

            PostResDto postResDto1 = postService.getPosts("testPost", null);
            assertThat(postResDto1.getHasNext()).isFalse();
            assertThat(postResDto1.getPosts()).isEmpty();

            PostResDto postResDto2 = postService.getPosts("testPost", Long.MAX_VALUE);
            assertThat(postResDto2.getHasNext()).isFalse();
            assertThat(postResDto2.getPosts()).isEmpty();
        }

        @DisplayName("게시글 20 개 생성해두고 MAX_PAGE_SIZE 인 12 를 경계값으로 테스트")
        @Test
        void readSuccessPaging() {
            // MAX_PAGE_SIZE 보다 많은 게시글 생성
            List<Long> postIds = new ArrayList<>();

            for (int i = 0; i < 20; i++) {
                Long postId = createPost("test post").getId();
                postIds.add(postId);
            }

            // 다른 계정으로 로그인
            signupAndLogin("other", "other@test.net");

            // 11 개 호출
            Long postId11 = postIds.get(MAX_PAGE_SIZE - 1);
            PostResDto postResDto11 = postService.getPosts("testPost", postId11);
            assertThat(postResDto11.getHasNext()).isFalse();
            assertThat(postResDto11.getPosts().size()).isEqualTo(MAX_PAGE_SIZE - 1);

            // 12 개 호출
            Long postId12 = postIds.get(MAX_PAGE_SIZE);
            PostResDto postResDto12 = postService.getPosts("testPost", postId12);
            assertThat(postResDto12.getHasNext()).isFalse();
            assertThat(postResDto12.getPosts().size()).isEqualTo(MAX_PAGE_SIZE);

            // 최대로 호출
            PostResDto postResDto13 = postService.getPosts("testPost", null);
            assertThat(postResDto13.getHasNext()).isTrue();
            assertThat(postResDto13.getPosts().size()).isEqualTo(MAX_PAGE_SIZE);
        }
    }

    @DisplayName("피드 (내가 팔로우 중인 사람들과 내 게시글) 조회")
    @Test
    void testFeeds() {
        final int MAX_PAGE_SIZE = 12;

        // 내 게시글 생성
        createPost("test post");

        // 내가 팔로우 하는 사람들 게시글 작성
        for (int i = 1; i <= 4; i++) {
            String displayId = "following" + i;

            signup(displayId, displayId + "@test.net");
            follow("testPost", displayId);
            login(displayId);

            // 게시글 3개씩 작성
            for (int j = 0; j < 3; j++) {
                createPost("test post by " + displayId);
            }
        }

        // 나를 팔로우 하는 사람들 게시글 작성
        for (int i = 1; i <= 4; i++) {
            String displayId = "follower" + i;

            signup(displayId, displayId + "@test.net");
            follow(displayId, "testPost");
            login(displayId);
            createPost("test post by " + displayId);
        }

        // 내 계정으로 로그인해서 피드 호출
        login("testPost");
        PostResDto feeds = postService.getFeeds(null);

        // 피드 결과물은 총 13 개 중에 12 개
        assertThat(feeds.getHasNext()).isTrue();

        List<PostDto> posts = feeds.getPosts();
        assertThat(posts.size()).isEqualTo(MAX_PAGE_SIZE);

        // 게시글 작성자 목록
        List<String> displayIds = posts.stream().map(PostDto::getAuthor).map(MemberResDto::getDisplayId).collect(Collectors.toList());
        assertThat(displayIds).contains("following1", "following2", "following3", "following4");

        // 게시글 목록
        List<String> contents = posts.stream().map(PostDto::getContent).collect(Collectors.toList());
        assertThat(contents).contains("test post by following1", "test post by following2", "test post by following3", "test post by following4");

        // 1 개만 호출해서 가장 처음에 내가 작성한 글도 나오는지 확인
        PostResDto myFeeds = postService.getFeeds(posts.get(0).getId());
        assertThat(myFeeds.getHasNext()).isFalse();

        List<PostDto> myPosts = myFeeds.getPosts();
        assertThat(myPosts.size()).isEqualTo(12);
        assertThat(myPosts.get(MAX_PAGE_SIZE - 1).getContent()).isEqualTo("test post");
        assertThat(myPosts.get(MAX_PAGE_SIZE - 1).getAuthor().getDisplayId()).isEqualTo("testPost");
    }
}
