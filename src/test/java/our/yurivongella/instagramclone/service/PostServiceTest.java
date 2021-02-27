package our.yurivongella.instagramclone.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import our.yurivongella.instagramclone.controller.dto.post.PostRequestDto;
import our.yurivongella.instagramclone.controller.dto.post.PostResponseDto;
import our.yurivongella.instagramclone.controller.dto.UsersRequestDto;
import our.yurivongella.instagramclone.domain.Post.Post;
import our.yurivongella.instagramclone.domain.Post.PostRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class PostServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    private static Long userId;
    private static PostRequestDto postRequestDto;

    @BeforeEach
    public void userSignUp() {
        // 가입시키고
        UsersRequestDto usersRequestDto1 = new UsersRequestDto();
        usersRequestDto1.setName("슈르 킴");
        userId = userService.signUp(usersRequestDto1);
    }

    @BeforeEach
    public void generatePostRequestDto() {
        // make mock PostRequestDto
        PostRequestDto postRequestDto = new PostRequestDto();
        // set content of mock PostRequestDto to mock Post content
        postRequestDto.setContent("시험중인 글 입니다.");
        // set pictureUrls mock PostRequestDto
        List<String> pictureUrls = new ArrayList<>();
        pictureUrls.add("mock url 1");
        pictureUrls.add("mock url 2");
        pictureUrls.add("mock url 3");
        postRequestDto.setPictureUrls(pictureUrls);
        this.postRequestDto = postRequestDto;
    }

    @DisplayName("게시글 생성")
    @Test
    public void createPost() {
        // request mock Post
        postService.create(postRequestDto, userService.findById(userId).get());

        List<Post> list = postRepository.findAll();
        System.out.println(list);

        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getUser().getId()).isEqualTo(userId);
        assertThat(list.get(0).getContent()).isEqualTo(postRequestDto.getContent());
        assertThat(list.get(0).getPictureURLs().size()).isEqualTo(3);
    }

    @DisplayName("게시글 한개 읽기")
    @Test
    public void readOnePost() {
        // request mock Post
        Long postId = postService.create(postRequestDto, userService.findById(userId).get());
        // reqeust mock get
        PostResponseDto postResponseDto = postService.read(postId, userId);

        assertThat(postResponseDto.getAuthor().getId()).isEqualTo(userId);
        assertThat(postResponseDto.getContent()).isEqualTo(postRequestDto.getContent());

        for (int i = 0; i < postRequestDto.getPictureUrls().size(); ++i) {
            assertThat(postResponseDto.getPictureUrls().get(i)).isEqualTo(postRequestDto.getPictureUrls().get(i));
        }

        assertThat(postResponseDto.getLikeCount()).isEqualTo(0);
        assertThat(postResponseDto.getCommentCount()).isEqualTo(0);
    }
}
