package our.yurivongella.instagramclone.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import our.yurivongella.instagramclone.controller.dto.post.PostResDto;
import our.yurivongella.instagramclone.controller.dto.ProcessStatus;
import our.yurivongella.instagramclone.controller.dto.post.PostReadResDto;
import our.yurivongella.instagramclone.util.SliceHelper;
import our.yurivongella.instagramclone.entity.Member;
import our.yurivongella.instagramclone.entity.MediaUrl;
import our.yurivongella.instagramclone.repository.MediaUrlRepository;
import our.yurivongella.instagramclone.entity.Post;
import our.yurivongella.instagramclone.entity.PostLike;
import our.yurivongella.instagramclone.repository.PostLikeRepository;
import our.yurivongella.instagramclone.repository.post.PostRepository;
import our.yurivongella.instagramclone.exception.CustomException;
import our.yurivongella.instagramclone.exception.ErrorCode;
import our.yurivongella.instagramclone.util.SecurityUtil;

import com.sun.istack.NotNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import our.yurivongella.instagramclone.controller.dto.post.PostCreateReqDto;
import our.yurivongella.instagramclone.repository.MemberRepository;

@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
    private static final int pageSize = 5;

    private final PostRepository postRepository;
    private final MediaUrlRepository mediaUrlRepository;
    private final MemberRepository memberRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public Long create(PostCreateReqDto postCreateReqDto) {
        Member member = getCurrentMember();

        try {
            Post post = postRepository.save(postCreateReqDto.toPost(member));
            List<MediaUrl> list = mediaUrlRepository.saveAll(postCreateReqDto.getMediaUrls(post));
            post.getMediaUrls().addAll(list);
            return post.getId();
        } catch (Exception e) {
            log.error("게시물 생성 에러 = {}", e.getMessage());
            throw new RuntimeException("게시물 생성 에러");
        }
    }

    @Transactional
    public PostReadResDto read(Long postId) {
        Member member = getCurrentMember();
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        post.viewCountUp();
        return PostReadResDto.of(post, member);
    }

    @Transactional
    public ProcessStatus delete(@NotNull Long postId) {
        Post post = postRepository.findById(postId)
                                  .orElseThrow(() -> new RuntimeException("게시물이 없습니다."));

        if (!post.getMember().getId().equals(SecurityUtil.getCurrentMemberId())) {
            log.error("유저가 일치하지 않습니다.");
            return ProcessStatus.FAIL;
        }

        try {
            postRepository.delete(post);
            post.getMember().minusPostCount();
            return ProcessStatus.SUCCESS;
        } catch (Exception e) {
            log.error("삭제 도중 문제가 발생했습니다. = {}", e.getMessage());
        }

        return ProcessStatus.FAIL;
    }

    @Transactional
    public ProcessStatus likePost(@NotNull Long postId) {
        Post post = postRepository.findById(postId)
                                  .orElseThrow(() -> new RuntimeException("게시물이 없습니다."));

        try {
            Member member = getCurrentMember();
            PostLike postLike = createPostLike(member, post);
            postLikeRepository.save(postLike);
        } catch (Exception e) {
            log.error("좋아요 도중 문제가 발생했습니다.");
            throw e;
        }
        return ProcessStatus.SUCCESS;
    }

    @Transactional
    public ProcessStatus unlikePost(Long postId) {
        Member member = getCurrentMember();
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        PostLike postLike = postLikeRepository.findByMemberAndPost(member, post).orElseThrow(() -> new CustomException(ErrorCode.ALREADY_UNLIKE));
        try {
            postLike.unlike();
            postLikeRepository.delete(postLike);
        } catch (Exception e) {
            log.error("[포스트 번호 : {}] {}가 좋아요 취소 도중 에러가 발생했습니다.", post.getId(), member.getDisplayId());
            return ProcessStatus.FAIL;
        }
        return ProcessStatus.SUCCESS;
    }

    protected PostLike createPostLike(Member member, Post post) {
        postLikeRepository.findByMemberAndPost(member, post).ifPresent(postLike -> {
            log.error("[글번호 : {}] {}가 포스트를 이미 좋아요를 하고 있습니다.", postLike.getId(), member.getDisplayId());
            throw new CustomException(ErrorCode.ALREADY_LIKE);
        });
        return new PostLike().like(member, post);
    }

    private Member getCurrentMember() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_MEMBER));
    }

    @Transactional(readOnly = true)
    public PostResDto getFeeds(Long lastId) {
        Member currentMember = getCurrentMember();
        List<Post> content = postRepository.findAllByJoinFollow(getCurrentMember().getId(), lastId, pageSize);
        PostResDto postResDto = getFeeds(currentMember, content);
        return postResDto;
    }

    private PostResDto getFeeds(final Member currentMember, List<Post> content) {
        PostResDto postResDto = new PostResDto();
        final boolean hasNext = SliceHelper.hasNext(content, pageSize);
        postResDto.setHasNext(hasNext);
        content = SliceHelper.getContents(content, pageSize);

        postResDto.setFeeds(content.stream()
                                   .map(post -> PostReadResDto.of(post, currentMember))
                                   .collect(Collectors.toList()));
        return postResDto;
    }
}
