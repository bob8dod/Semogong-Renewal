package talkwith.semogong.domain.etc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import talkwith.semogong.config.CategoryConst;
import talkwith.semogong.domain.att.DesiredJob;

import static talkwith.semogong.config.CategoryConst.MY;
import static talkwith.semogong.config.CategoryConst.TODAY;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCond {

    private String category;
    private String title;
    private String name;
    private DesiredJob desiredJob;
    private String content;

    public void setCategory(String category) {
        if (category.equals("today")) {
            this.category = TODAY;
        } else if (category.equals("my")) {
            this.category = MY;
        }
    }

}
