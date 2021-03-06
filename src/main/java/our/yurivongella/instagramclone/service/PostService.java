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

@RequiredArgsConstructor
@Service
public class PostService {
    final private PostRepository postRepository;
    final private PictureURLRepository pictureURLRepository;
    final private MemberRepository memberRepository;

    @Transactional
    public Long create(PostCreateRequestDto postCreateRequestDto) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new RuntimeException("조회자 정보가 존재하지 않습니다."));
        Post post = postRepository.save(postCreateRequestDto.toPost(member));

        List<PictureURL> list = pictureURLRepository.saveAll(postCreateRequestDto.getPictureURLs(post));
        post.getPictureURLs().addAll(list);

        return post.getId();
    }

    public PostReadResponseDto read(Long postId) {
        Long userId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(userId).orElseThrow();
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
}
