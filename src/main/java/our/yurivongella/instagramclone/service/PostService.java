package our.yurivongella.instagramclone.service;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import our.yurivongella.instagramclone.controller.dto.post.PostDto;
import our.yurivongella.instagramclone.controller.dto.post.PostReqDto;
import our.yurivongella.instagramclone.controller.dto.post.PostResDto;
import our.yurivongella.instagramclone.controller.dto.ProcessStatus;
import our.yurivongella.instagramclone.entity.*;
import our.yurivongella.instagramclone.exception.ErrorCode;
import our.yurivongella.instagramclone.repository.FollowRepository;
import our.yurivongella.instagramclone.util.SliceHelper;
import our.yurivongella.instagramclone.repository.PostLikeRepository;
import our.yurivongella.instagramclone.repository.post.PostRepository;
import our.yurivongella.instagramclone.exception.CustomException;
import our.yurivongella.instagramclone.util.SecurityUtil;

import com.sun.istack.NotNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import our.yurivongella.instagramclone.repository.MemberRepository;

import static java.util.stream.Collectors.toList;
import static our.yurivongella.instagramclone.exception.ErrorCode.*;

@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
    private static final int POST_PAGE_SIZE = 12;

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostLikeRepository postLikeRepository;
    private final FollowRepository followRepository;

    @Transactional
    public PostDto createPost(PostReqDto postReqDto) {
        Post post = postReqDto.toEntity(getCurrentMember());
        postRepository.save(post);
        return PostDto.of(post);
    }

    @Transactional
    public PostDto getPost(Long postId) {
        Post post = getCurrentPost(postId);
        post.viewCountUp();
        return createPostDto(post);
    }

    @Transactional
    public ProcessStatus deletePost(@NotNull Long postId) {
        Post post = getCurrentPost(postId);

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
        Post post = getCurrentPost(postId);
        Member member = getCurrentMember();
        PostLike postLike = createPostLike(member, post);

        postLikeRepository.save(postLike);
        return ProcessStatus.SUCCESS;
    }

    @Transactional
    public ProcessStatus unlikePost(@NotNull Long postId) {
        Member member = getCurrentMember();
        Post post = getCurrentPost(postId);
        PostLike postLike = postLikeRepository.findByMemberAndPost(member, post).orElseThrow(() -> new CustomException(ALREADY_UNLIKE));

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
            throw new CustomException(ALREADY_LIKE);
        });
        return new PostLike().like(member, post);
    }

    /* 내 게시글들 조회 */
    public PostResDto getMyPosts(Long lastId) {
        Member member = getCurrentMember();
        return findPostsOfMember(member, lastId);
    }

    /* 내 특정 멤버의 게시글들 조회 */
    public PostResDto getPosts(String displayId, Long lastId) {
        Member member = getMemberByDisplayId(displayId);
        return findPostsOfMember(member, lastId);
    }

    private PostResDto findPostsOfMember(Member member, Long lastId) {
        List<Post> posts = postRepository.findAllByMemberIdAndIdLessThan(member.getId(), lastId, POST_PAGE_SIZE);
        return createPostResDto(posts, POST_PAGE_SIZE);
    }

    /* 인스타 피드 조회 */
    public PostResDto getFeeds(Long lastId) {
        List<Post> posts = postRepository.findAllByJoinFollow(getCurrentMember().getId(), lastId, POST_PAGE_SIZE);
        return createPostResDto(posts, POST_PAGE_SIZE);
    }

    /**
     * private
     */
    private PostResDto createPostResDto(List<Post> posts, int pageSize) {
        boolean hasNext = SliceHelper.hasNext(posts, pageSize);
        List<PostDto> postDtos = SliceHelper.getContents(posts, pageSize)
                                            .stream()
                                            .map(this::createPostDto)
                                            .collect(toList());

        return PostResDto.of(hasNext, postDtos);
    }

    private PostDto createPostDto(Post post) {
        Member currentMember = getCurrentMember();
        PostDto postDto = PostDto.of(post);

        followRepository.findByFromMemberAndToMember(currentMember, post.getMember())
                        .ifPresent(ignored -> postDto.getAuthor().setFollowTrue());

        postLikeRepository.findByMemberAndPost(currentMember, post)
                          .ifPresent(ignored -> postDto.setLikeTrue());

        return postDto;
    }

    private Member getCurrentMember() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                               .orElseThrow(() -> new CustomException(UNAUTHORIZED_MEMBER));
    }

    private Member getMemberByDisplayId(String displayId) {
        return memberRepository.findByDisplayId(displayId)
                               .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Post getCurrentPost(Long postId) {
        return postRepository.findById(postId)
                             .orElseThrow(() -> new CustomException(POST_NOT_FOUND));
    }
}
