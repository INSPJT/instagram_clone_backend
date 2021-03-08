package our.yurivongella.instagramclone.service;

import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import our.yurivongella.instagramclone.controller.dto.post.PostCreateRequestDto;
import our.yurivongella.instagramclone.controller.dto.post.PostReadResponseDto;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.member.MemberRepository;
import our.yurivongella.instagramclone.domain.post.PictureURL;
import our.yurivongella.instagramclone.domain.post.PictureURLRepository;
import our.yurivongella.instagramclone.domain.post.Post;
import our.yurivongella.instagramclone.domain.post.PostRepository;
import our.yurivongella.instagramclone.util.SecurityUtil;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final PictureURLRepository pictureURLRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long create(PostCreateRequestDto postCreateRequestDto) {
        Member member = getCurrentMember();
        Post post = postRepository.save(postCreateRequestDto.toPost(member));

        List<PictureURL> list = pictureURLRepository.saveAll(postCreateRequestDto.getPictureURLs(post));
        post.getPictureURLs().addAll(list);

        return post.getId();
    }

    public PostReadResponseDto read(Long postId) {
        Member member = getCurrentMember();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물이 없습니다."));

        return PostReadResponseDto.of(post, member);
    }

    @Transactional
    public Long delete(@NotNull Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물이 없습니다."));

        if (!post.getMember().getId().equals(SecurityUtil.getCurrentMemberId())) {
            throw new RuntimeException("게시물 삭제 권한이 없습니다.");
        }

        postRepository.delete(post);
        return postId;
    }

    public List<PostReadResponseDto> getPostList(Long userId) {
        Member member = getCurrentMember();
        List<Post> postList = member.getPosts();

        return postList.stream()
                .map(post -> PostReadResponseDto.of(post,member))
                .collect(Collectors.toList());
    }

    private Member getCurrentMember() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new NoSuchElementException("현재 계정 정보가 존재하지 않습니다."));
    }
}
