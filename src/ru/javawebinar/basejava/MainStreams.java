package ru.javawebinar.basejava;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainStreams {
    public static void main(String[] args) {
        System.out.println(minValue(new int[]{1,2,3,3,2,3}));
        System.out.println(minValue(new int[]{9,8}));

        Integer[] testArray = new Integer[] {1,2,3,3,2,3,1};
        List<Integer> list = Arrays.asList(testArray);
        System.out.println((oddOrEven(list).toString()));
    }

    private static int minValue(int[] values) {
        return IntStream.of(values)
                .distinct()
                .sorted()
                .reduce(0, (left, right) -> 10*left + right);
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        Map<Boolean, List<Integer>> collect = integers.stream()
                .collect(Collectors.partitioningBy(MainStreams::test));

        return test(integers.stream()
                .mapToInt(Integer::intValue)
                .sum()) ? collect.get(true) : collect.get(false);
    }

    private static Boolean test(Integer integer) {
        return 0 == (integer % 2);
    }
}
