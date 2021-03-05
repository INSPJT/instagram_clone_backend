package our.yurivongella.instagramclone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import our.yurivongella.instagramclone.controller.dto.post.PostRequestDto;
import our.yurivongella.instagramclone.controller.dto.post.PostResponseDto;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.member.MemberRepository;
import our.yurivongella.instagramclone.domain.post.PictureURL;
import our.yurivongella.instagramclone.domain.post.PictureURLRepository;
import our.yurivongella.instagramclone.domain.post.Post;
import our.yurivongella.instagramclone.domain.post.PostRepository;

import java.util.List;


@RequiredArgsConstructor
@Service
public class PostService {

    final private PostRepository postRepository;
    final private PictureURLRepository pictureURLRepository;

    final private MemberRepository memberRepository;

    @Transactional
    public Long create(PostRequestDto postRequestDto, Member member) {

        Post post = postRequestDto.toPosts(member);
        post = postRepository.save(post);

        List<PictureURL> list = pictureURLRepository.saveAll(postRequestDto.toPictureURLs(post));
        post.getPictureURLs().addAll(list);

        return post.getId();
    }

    /*
    @Transactional
    public PostResponseDto read(Long postId) {
        Post post = postRepository.findById(postId).get();
        return PostResponseDto.toPostResponseDto(post);
    }
     */

    @Transactional
    public Long testCreate(PostRequestDto postRequestDto, Long userId) {
        Post post = postRequestDto.toPosts(memberRepository.findById(userId).get());
        post = postRepository.save(post);

        List<PictureURL> list = pictureURLRepository.saveAll(postRequestDto.toPictureURLs(post));
        post.getPictureURLs().addAll(list);

        return post.getId();
    }

    public PostResponseDto read(Long postId, Long userId) {
        Member member = memberRepository.findById(userId).get();
        Post post = postRepository.findById(postId).get();
        return PostResponseDto.toPostResponseDto(post, member);

    }
}
