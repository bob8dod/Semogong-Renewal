package talkwith.semogong.controller;

import talkwith.semogong.domain.dto.member.MemberHomeDto;

import java.util.Comparator;

public class CustomSorter {

    static class Times implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            //o1 - o2 = ASC , o2 - o1 = DESC
            MemberHomeDto m1 = (MemberHomeDto) o1;
            MemberHomeDto m2 = (MemberHomeDto) o2;
            long totalTime1 = 0;
            long totalTime2 = 0;
            if (m1.getTime() != null) totalTime1 = m1.getTime().getHour() * 60 + m1.getTime().getMin();
            if (m2.getTime() != null) totalTime2 = m2.getTime().getHour() * 60 + m2.getTime().getMin();
            return (int) totalTime2 - (int) totalTime1;
        }
    }
}
