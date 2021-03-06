package our.yurivongella.instagramclone.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(currentMemberId)
                                        .orElseThrow(() -> new NoSuchElementException("유저가 존재하지 않습니다."));

        return postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("해당 게시물이 없습니다."))
                             .getComments()
                             .stream()
                             .map(c -> CommentResponseDto.of(c, member))
                             .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponseDto createComment(Long postId, CommentCreateDto commentCreateDto) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(currentMemberId)
                                        .orElseThrow(() -> new UsernameNotFoundException("유저가 존재하지 않습니다."));

        Post post = postRepository.findById(postId)
                                  .orElseThrow(() -> new NoSuchElementException("해당 게시물이 없습니다."));

        Comment comment = new Comment();
        comment.create(member, post, commentCreateDto.getContent());

        commentRepository.save(comment);
        return CommentResponseDto.of(comment, member);
    }

    @Transactional
    public ProcessStatus deleteComment(Long commentId) throws Exception {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(currentMemberId)
                                        .orElseThrow(() -> new UsernameNotFoundException("유저가 존재하지 않습니다."));

        Comment comment = commentRepository.findById(commentId)
                                           .orElseThrow(() -> new NotFoundException("존재하지 않는 댓글입니다."));

        if (member.getId().equals(comment.getMember().getId())) {
            try {
                commentRepository.deleteById(commentId);
                return ProcessStatus.SUCCESS;
            } catch (Exception e) {
                log.error("삭제 도중 문제가 발생했습니다 = {}", e.getMessage());
            }
        }
        return ProcessStatus.FAIL;
    }

    @Transactional
    public ProcessStatus likeComment(Long commentId) throws Exception {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(currentMemberId)
                                        .orElseThrow(() -> new UsernameNotFoundException("유저가 존재하지 않습니다."));

        Comment comment = commentRepository.findById(commentId)
                                           .orElseThrow(() -> new NoSuchElementException("댓글이 존재하지 않습니다."));

        boolean check = commentLikeRepository.findAllByCommentId(commentId)
                                             .stream()
                                             .anyMatch(cl -> cl.getMember().getId().equals(member.getId()));
        if(!check){
            log.info("{}가 댓글 {}를 좋아요 표시합니다.", member.getName(), comment.getContent());
            CommentLike commentLike = likeComment(member, comment);
            commentLikeRepository.save(commentLike);
            return ProcessStatus.SUCCESS;
        }
        return ProcessStatus.FAIL;
    }

    private CommentLike likeComment(Member member, Comment comment) {
        CommentLike commentLike = new CommentLike();
        commentLike.like(member, comment);
        return commentLike;
    }

    @Transactional
    public ProcessStatus unlikeComment(Long commentId) throws Exception {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findById(currentMemberId)
                                        .orElseThrow(() -> new UsernameNotFoundException("유저가 존재하지 않습니다."));

        Comment comment = commentRepository.findById(commentId)
                                           .orElseThrow(() -> new NoSuchElementException("댓글이 존재하지 않습니다."));

        return commentLikeRepository.findAllByCommentId(commentId)
                                    .stream()
                                    .filter(cl -> cl.getMember().getId().equals(member.getId()))
                                    .findFirst()
                                    .map(cl -> {
                                        log.info("{}가 댓글 {}를 좋아요 취소합니다.", member.getName(), comment.getContent());
                                        cl.unlike();
                                        commentLikeRepository.delete(cl);
                                        return ProcessStatus.SUCCESS;
                                    }).orElse(ProcessStatus.FAIL);
    }

    public long getCommentLikes(Long commentId) {
        return commentRepository.findById(commentId)
                                .orElseThrow(() -> new NoSuchElementException("댓글이 존재하지 않습니다."))
                                .getCommentLikes().size();
    }

}
