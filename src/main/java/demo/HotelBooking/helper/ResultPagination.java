package demo.HotelBooking.helper;

import java.util.ArrayList;
import java.util.List;

public class ResultPagination {
    public static List<Integer> getPaginationList(int minPage, int maxPage, int pageIndex, int rangePage) {
        List<Integer> result = new ArrayList<>();
        int range = (int) Math.floor(rangePage / 2);
        int min = minPage;
        int max = maxPage;
        if ((pageIndex - range) < 1) {
            if (max < rangePage) {
                addElement(min, max, result);
            } else {
                addElement(min, rangePage, result);
            }
        } else if((pageIndex + range) > max) {
            if (max - rangePage + 1 < min) {
                addElement(min, max , result);
            } else {
                addElement(max - rangePage + 1, max, result);
            }
        } else {
            addElement(pageIndex - range, pageIndex + range, result);
        }

        return result;
    }

    public static void addElement (int min, int max , List<Integer> a) {
        for(int i = min; i <= max; i++) {
            a.add(i);
        }
    }

    public static int calculateMaxCountPage(int totalResult, int sizePerPage) {
        if (totalResult % sizePerPage == 0) {
            return totalResult / sizePerPage;
        } else {
            return totalResult / sizePerPage + 1;
        }
    }
}
