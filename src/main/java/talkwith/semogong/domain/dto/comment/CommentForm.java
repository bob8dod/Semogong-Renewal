package talkwith.semogong.domain.dto.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentForm {

    @NotBlank
    private String content;
}
