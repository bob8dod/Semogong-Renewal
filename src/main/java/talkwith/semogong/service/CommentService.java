package talkwith.semogong.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import talkwith.semogong.domain.dto.comment.CommentForm;
import talkwith.semogong.domain.entity.Comment;
import talkwith.semogong.repository.comment.CommentRepository;

import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public Optional<Comment> findOne(Long commentId) {
        return commentRepository.findById(commentId);
    }

    public void edit(Long commentId, CommentForm form) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.edit(form);
        } // else: 아무 동작 하지 않음
    }

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }
}
