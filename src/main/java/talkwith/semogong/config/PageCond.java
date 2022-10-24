package talkwith.semogong.config;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageCond {

    private static final int postSize = 16;
    private static final int memberSize = 12;
    private static final Sort postSort = Sort.by(Sort.Direction.DESC, "createdDate");
    private static final Sort memberSort = Sort.by(Sort.Direction.DESC, "createdDate");

    public static PageRequest getPostPageRequest(int page) {
        return PageRequest.of(page, postSize, postSort);
    }

    public static PageRequest getPostPageRequest(int page, Sort sortCond) {
        return PageRequest.of(page, postSize, sortCond);
    }

    public static PageRequest getMemberPageRequest(int page) {
        return PageRequest.of(page, postSize, postSort);
    }

    public static PageRequest getMemberPageRequest(int page, Sort sortCond) {
        return PageRequest.of(page, postSize, sortCond);
    }
}
