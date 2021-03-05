package our.yurivongella.instagramclone.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import our.yurivongella.instagramclone.controller.dto.MemberRequestDto;
import our.yurivongella.instagramclone.controller.dto.post.PostCreateRequestDto;
import our.yurivongella.instagramclone.controller.dto.post.PostReadResponseDto;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.post.Post;
import our.yurivongella.instagramclone.domain.post.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class PostServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    private static Long userId;
    private static PostCreateRequestDto postCreateRequestDto;

    @BeforeEach
    public void userSignUp() {
        // 가입시키고
        MemberRequestDto memberRequestDto1 = new MemberRequestDto();
        memberRequestDto1.setName("슈르 킴");
        userId = memberService.signUp(memberRequestDto1);
    }

    @BeforeEach
    public void generatePostRequestDto() {
        // make mock PostRequestDto
        PostCreateRequestDto postCreateRequestDto = new PostCreateRequestDto();
        // set content of mock PostRequestDto to mock Post content
        postCreateRequestDto.setContent("시험중인 글 입니다.");
        // set pictureUrls mock PostRequestDto
        List<String> pictureUrls = new ArrayList<>();
        pictureUrls.add("mock url 1");
        pictureUrls.add("mock url 2");
        pictureUrls.add("mock url 3");
        postCreateRequestDto.setPictureUrls(pictureUrls);
        this.postCreateRequestDto = postCreateRequestDto;
    }

    @DisplayName("게시글 생성")
    @Test
    public void createPost() {
        // request mock Post
        postService.create(postCreateRequestDto, memberService.findById(userId).get());

        List<Post> list = postRepository.findAll();
        System.out.println(list);

        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getMember().getId()).isEqualTo(userId);
        assertThat(list.get(0).getContent()).isEqualTo(postCreateRequestDto.getContent());
        assertThat(list.get(0).getPictureURLs().size()).isEqualTo(3);
    }

    @DisplayName("게시글 한개 읽기")
    @Test
    public void readOnePost() {
        // request mock Post
        Long postId = postService.create(postCreateRequestDto, memberService.findById(userId).get());
        System.out.println("post id : " + postId + " member Id : " + memberService.findById(userId).get());
        Member member = memberService.findById(userId).get();
        // reqeust mock get
        PostReadResponseDto postResponseDto = postService.read(postId, userId);

        assertThat(postResponseDto.getAuthor().getId()).isEqualTo(userId);
        assertThat(postResponseDto.getContent()).isEqualTo(postCreateRequestDto.getContent());

        for (int i = 0; i < postCreateRequestDto.getPictureUrls().size(); ++i) {
            assertThat(postResponseDto.getPictureUrls().get(i)).isEqualTo(postCreateRequestDto.getPictureUrls().get(i));
        }

        assertThat(postResponseDto.getLikeCount()).isEqualTo(0);
        assertThat(postResponseDto.getCommentCount()).isEqualTo(0);
    }

    @DisplayName("게시물 삭제")
    @Test
    public void DeleteOnePost() {
        Long postId = postService.create(postCreateRequestDto, memberService.findById(userId).get());

        postService.delete(postId, userId);
        Optional<PostReadResponseDto> post = Optional.ofNullable(postService.read(postId, userId));

        assertThat(post).isEmpty();
    }
}
