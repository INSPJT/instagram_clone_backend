package our.yurivongella.instagramclone.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sun.istack.NotNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import our.yurivongella.instagramclone.controller.dto.comment.ProcessStatus;
import our.yurivongella.instagramclone.controller.dto.comment.CommentResponseDto;
import our.yurivongella.instagramclone.controller.dto.post.PostCreateRequestDto;
import our.yurivongella.instagramclone.controller.dto.post.PostReadResponseDto;
import our.yurivongella.instagramclone.domain.comment.CommentRepository;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.member.MemberRepository;
import our.yurivongella.instagramclone.domain.post.*;
import our.yurivongella.instagramclone.exception.CustomException;
import our.yurivongella.instagramclone.exception.ErrorCode;
import our.yurivongella.instagramclone.util.SecurityUtil;

@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
    private static final int pageSize = 5;

    private final PostRepository postRepository;
    private final MediaUrlRepository mediaUrlRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public Long create(PostCreateRequestDto postCreateRequestDto) {
        Member member = getCurrentMember();

        try {
            Post post = postRepository.save(postCreateRequestDto.toPost(member));
            List<MediaUrl> list = mediaUrlRepository.saveAll(postCreateRequestDto.getMediaUrls(post));
            post.getMediaUrls().addAll(list);
            return post.getId();
        } catch (Exception e) {
            log.error("게시물 생성 에러 = {}", e.getMessage());
            throw new RuntimeException("게시물 생성 에러");
        }
    }

    @Transactional
    public PostReadResponseDto read(Long postId) {
        Member member = getCurrentMember();
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        post.viewCountUp();
        return PostReadResponseDto.of(post, member);
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
                               .orElseThrow(() -> new NoSuchElementException("현재 계정 정보가 존재하지 않습니다."));
    }

    /**
     * @Parameter : lastPostId = Offset
     */
    @Transactional(readOnly = true)
    public Slice<PostReadResponseDto> getFeeds(Pageable pageRequest) {
        Member currentMember = getCurrentMember();

        //페이지를 가져오고
        Slice<Post> result = postRepository.findAllByJoinFollow(getCurrentMember().getId(), pageRequest);
        List<PostReadResponseDto> postReadResponseDtoList = result.getContent()
                                                  .stream()
                                                  .map(post -> PostReadResponseDto.of(post, currentMember).setCommentPreview(
                                                          commentRepository.findTop3ByPostOrderByIdDesc(post)
                                                                           .stream()
                                                                           .map(comment -> CommentResponseDto.of(comment, currentMember))
                                                                           .collect(Collectors.toList())
                                                       )
                                                  )
                                                  .collect(Collectors.toList());

        return new SliceImpl<>(postReadResponseDtoList, result.getPageable(), result.hasNext());
    }
}
