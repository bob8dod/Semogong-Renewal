package talkwith.semogong.domain.etc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import talkwith.semogong.domain.att.DesiredJob;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCond {

    private String category;
    private String title;
    private String name;
    private DesiredJob desiredJob;
    private String content;

}
