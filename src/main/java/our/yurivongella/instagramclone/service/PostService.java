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

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;


@RequiredArgsConstructor
@Service
public class PostService {
    final private PostRepository postRepository;
    final private PictureURLRepository pictureURLRepository;
    final private MemberRepository memberRepository;

    @Transactional
    public Long create(PostCreateRequestDto postCreateRequestDto, Member member) {

        Post post = postCreateRequestDto.toPosts(member);
        post = postRepository.save(post);

        List<PictureURL> list = pictureURLRepository.saveAll(postCreateRequestDto.toPictureURLs(post));
        post.getPictureURLs().addAll(list);

        return post.getId();
    }

    /* TODO : deleted */
    @Transactional
    public Long testCreate(PostCreateRequestDto postCreateRequestDto, Long userId) {
        Post post = postCreateRequestDto.toPosts(memberRepository.findById(userId).get());
        post = postRepository.save(post);

        List<PictureURL> list = pictureURLRepository.saveAll(postCreateRequestDto.toPictureURLs(post));
        post.getPictureURLs().addAll(list);

        return post.getId();
    }

    public PostReadResponseDto read(Long postId, Long userId) {
        Member member = memberRepository.findById(userId).get();
        Optional<Post> post = postRepository.findById(postId);
        PostReadResponseDto postReadResponseDto;
        if (post.isPresent()) {
            return PostReadResponseDto.toPostResponseDto(post.get(), member);
        }
        else {
            return null;
            //throw new RuntimeException("게시물이 없습니다.");
        }
    }

    @Transactional
    public Long delete(@NotNull Long postId, Long usreId) {
        postRepository.deleteById(postId);
        return postId;
    }
}
