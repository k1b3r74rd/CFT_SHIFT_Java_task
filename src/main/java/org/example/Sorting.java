package org.example;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Sorting {
    public static <T extends Comparable<? super T>> List<T> merge_sort(List<T> list_for_sort) {
        if (list_for_sort.size() == 1) {
            return list_for_sort;
        }
        if (list_for_sort.isEmpty()) {
            return list_for_sort;
        }

        List<T> left_part = list_for_sort.subList(0, list_for_sort.size() / 2);
        List<T> right_part = list_for_sort.subList(list_for_sort.size() / 2, list_for_sort.size());

        left_part = merge_sort(left_part);
        right_part = merge_sort(right_part);

        List<T> sorted_list = sort(left_part, right_part);
        return sorted_list;
    }

    public static <T extends Comparable<? super T>> List<T> sort(List<T> left_list, List<T> right_list) {
        List<T> sorted_list = new ArrayList<>();
        Iterator<T> left_iter = left_list.iterator();
        Iterator<T> right_iter = right_list.iterator();

        T left = left_iter.next();
        T right = right_iter.next();

        while (true) {
            if (left.compareTo(right) <= 0) {
                sorted_list.add(left);

                if (left_iter.hasNext()) {
                    left = left_iter.next();
                } else {
                    sorted_list.add(right);
                    while (right_iter.hasNext()) {
                        sorted_list.add(right_iter.next());
                    }
                    break;
                }
            } else {
                sorted_list.add(right);

                if (right_iter.hasNext()) {
                    right = right_iter.next();
                } else {
                    sorted_list.add(left);
                    while (left_iter.hasNext()) {
                        sorted_list.add(left_iter.next());
                    }
                    break;
                }
            }
        }
        return sorted_list;
    }
}
