package our.yurivongella.instagramclone.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sun.istack.NotNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import our.yurivongella.instagramclone.controller.dto.comment.ProcessStatus;
import our.yurivongella.instagramclone.controller.dto.post.CommentResponseDto;
import our.yurivongella.instagramclone.controller.dto.post.PostCreateRequestDto;
import our.yurivongella.instagramclone.controller.dto.post.PostReadResponseDto;
import our.yurivongella.instagramclone.domain.comment.CommentRepository;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.member.MemberRepository;
import our.yurivongella.instagramclone.domain.post.MediaUrl;
import our.yurivongella.instagramclone.domain.post.MediaUrlRepository;
import our.yurivongella.instagramclone.domain.post.Post;
import our.yurivongella.instagramclone.domain.post.PostRepository;
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

    public PostReadResponseDto read(Long postId) {
        Member member = getCurrentMember();
        Post post = postRepository.findById(postId)
                                  .orElseThrow(() -> new RuntimeException("게시물이 없습니다."));
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
        log.info("게시물을 삭제 합니다.");
        try {
            postRepository.delete(post);
            return ProcessStatus.SUCCESS;
        } catch (Exception e) {
            log.error("삭제 도중 문제가 발생했습니다. = {}", e.getMessage());
        }
        return ProcessStatus.FAIL;
    }

    public List<PostReadResponseDto> getPostList(Long memberId) {
        Member member = getMember(memberId);

        return member.getPosts().stream()
                     .map(post -> PostReadResponseDto.of(post, member))
                     .collect(Collectors.toList());
    }

    private Member getCurrentMember() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                               .orElseThrow(() -> new NoSuchElementException("현재 계정 정보가 존재하지 않습니다."));
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                               .orElseThrow(() -> new NoSuchElementException("현재 계정 정보가 존재하지 않습니다."));
    }

    /**
     * Instagram Feeds
     *
     * @param lastPostId    Offset for infinite scroll
     * @param pageSize      Response collection size
     */
    @Transactional(readOnly = true)
    public List<PostReadResponseDto> getFeeds(Long lastPostId) {
        Member currentMember = getCurrentMember();

        PageRequest pageRequest = PageRequest.of(0, pageSize, Sort.by("id").descending());

        return postRepository.findAllByJoinFollow(getCurrentMember().getId(), lastPostId, pageRequest)
                .stream()
                .map(post -> PostReadResponseDto.of(post, currentMember).setCommentPreview(
                        commentRepository.findTop3ByPostOrderByIdDesc(post)
                                .stream()
                                .map(comment -> CommentResponseDto.of(comment, currentMember))
                                .collect(Collectors.toList())
                        )
                )
                .collect(Collectors.toList());
    }
}
