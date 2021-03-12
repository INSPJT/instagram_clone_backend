package our.yurivongella.instagramclone.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sun.istack.NotNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import our.yurivongella.instagramclone.controller.dto.comment.ProcessStatus;
import our.yurivongella.instagramclone.controller.dto.post.PostCreateRequestDto;
import our.yurivongella.instagramclone.controller.dto.post.PostReadResponseDto;
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
    private final PostRepository postRepository;
    private final MediaUrlRepository mediaUrlRepository;
    private final MemberRepository memberRepository;

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
     * @implNote : 아래는 Feed용 Service...........
     */

    public List<PostReadResponseDto> getFeed(Pageable pageable) {
        /** 20개를 때려박고
         *  5개씩 달라고 하면 4번 다른 종류가 나와야돼 대신 최신순으로
         */
        Member member = getCurrentMember();
        log.info("-------------------------pageQuery를 요청한 Member = {}", member);

        Slice<Post> feedSlice = postRepository.findFeedByUserId(member.getId(), pageable);

        return feedSlice
                .stream()
                .filter(ObjectUtils::isNotEmpty)
                .map(post -> PostReadResponseDto.of(post, member))
                .collect(Collectors.toList());
    }

}
