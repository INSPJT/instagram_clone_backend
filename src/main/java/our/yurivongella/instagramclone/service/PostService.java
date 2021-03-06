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
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {
    final private PostRepository postRepository;
    final private PictureURLRepository pictureURLRepository;
    final private MemberRepository memberRepository;

    @Transactional
    public Long create(PostCreateRequestDto postCreateRequestDto, Long memberId) {
        Optional<Member> member =memberRepository.findById(SecurityUtil.getCurrentMemberId());
        Post post = postCreateRequestDto.toPost(member.orElseThrow(() -> new RuntimeException()));
        post = postRepository.save(post);

        List<PictureURL> list = pictureURLRepository.saveAll(postCreateRequestDto.getPictureURLs(post));
        post.getPictureURLs().addAll(list);

        return post.getId();
    }

    public PostReadResponseDto read(Long postId) {
        Long userId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(userId).get();
        Optional<Post> post = postRepository.findById(postId);

        return PostReadResponseDto.of(post.orElseThrow(()
                -> new RuntimeException("게시물이 없습니다.")), member);
    }

    @Transactional
    public Long delete(@NotNull Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if(post.orElseThrow(() -> new RuntimeException("게시물이 없습니다.")).getId().equals(SecurityUtil.getCurrentMemberId())) {
            throw new RuntimeException("잘못된 요청입니다.");
        }
        postRepository.delete(post.get());
        return postId;
    }
}
