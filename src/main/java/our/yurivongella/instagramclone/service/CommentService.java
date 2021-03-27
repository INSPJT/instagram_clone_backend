package our.yurivongella.instagramclone.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import our.yurivongella.instagramclone.controller.dto.CommentCreateDto;
import our.yurivongella.instagramclone.controller.dto.comment.ProcessStatus;
import our.yurivongella.instagramclone.controller.dto.post.CommentResponseDto;
import our.yurivongella.instagramclone.domain.comment.Comment;
import our.yurivongella.instagramclone.domain.comment.CommentLike;
import our.yurivongella.instagramclone.domain.comment.CommentLikeRepository;
import our.yurivongella.instagramclone.domain.comment.CommentRepository;
import our.yurivongella.instagramclone.domain.member.Member;
import our.yurivongella.instagramclone.domain.member.MemberRepository;
import our.yurivongella.instagramclone.domain.post.Post;
import our.yurivongella.instagramclone.domain.post.PostRepository;
import our.yurivongella.instagramclone.util.SecurityUtil;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    public List<CommentResponseDto> getComments(Long postId) {
        Member member = getCurrentMember();

        return postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("해당 게시물이 없습니다."))
                             .getComments()
                             .stream()
                             .map(c -> CommentResponseDto.of(c, member))
                             .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponseDto createComment(Long postId, CommentCreateDto commentCreateDto) {
        Member member = getCurrentMember();

        Post post = postRepository.findById(postId)
                                  .orElseThrow(() -> new NoSuchElementException("해당 게시물이 없습니다."));

        Comment comment = new Comment();
        comment.create(member, post, commentCreateDto.getContent());
        commentRepository.save(comment);
        return CommentResponseDto.of(comment, member);
    }

    @Transactional
    public ProcessStatus deleteComment(Long commentId) throws Exception {
        Member member = getCurrentMember();
        Comment comment = getCurrentComment(commentId);

        if (!member.equals(comment.getMember())) {
            log.error("유저가 일치하지 않습니다.");
            return ProcessStatus.FAIL;
        }

        try {
            commentRepository.deleteById(commentId);
            return ProcessStatus.SUCCESS;
        } catch (Exception e) {
            log.error("삭제 도중 문제가 발생했습니다 = {}", e.getMessage());
        }
        return ProcessStatus.FAIL;
    }

    @Transactional
    public ProcessStatus likeComment(Long commentId) throws Exception {
        Member member = getCurrentMember();
        Comment comment = getCurrentComment(commentId);

        boolean check = commentLikeRepository.findAllByCommentId(commentId)
                                             .stream()
                                             .anyMatch(cl -> cl.getMember().equals(member));
        if (!check) {
            log.info("{}가 댓글 {}를 좋아요 표시합니다.", member.getDisplayId(), comment.getContent());
            CommentLike commentLike = createCommentLike(member, comment);
            commentLikeRepository.save(commentLike);
            return ProcessStatus.SUCCESS;
        }
        return ProcessStatus.FAIL;
    }

    private CommentLike createCommentLike(Member member, Comment comment) {
        return new CommentLike().like(member, comment);
    }

    @Transactional
    public ProcessStatus unlikeComment(Long commentId) throws Exception {
        Member member = getCurrentMember();
        Comment comment = getCurrentComment(commentId);

        return commentLikeRepository.findAllByCommentId(commentId)
                                    .stream()
                                    .filter(cl -> cl.getMember().equals(member))
                                    .findFirst()
                                    .map(cl -> {
                                        log.info("{}가 댓글 {}를 좋아요 취소합니다.", member.getDisplayId(), comment.getContent());
                                        cl.unlike();
                                        commentLikeRepository.delete(cl);
                                        return ProcessStatus.SUCCESS;
                                    }).orElse(ProcessStatus.FAIL);
    }

    private Member getCurrentMember() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                               .orElseThrow(() -> new NoSuchElementException("현재 계정 정보가 존재하지 않습니다."));
    }

    private Comment getCurrentComment(Long commentId) throws NotFoundException {
        return commentRepository.findById(commentId)
                                .orElseThrow(() -> new NotFoundException("존재하지 않는 댓글입니다."));
    }
}
