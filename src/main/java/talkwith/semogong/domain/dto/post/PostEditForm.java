package talkwith.semogong.domain.dto.post;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class PostEditForm {

    @NotBlank
    private String title;
    private String introduce;
    private String content;
    private List<String> times;

}
