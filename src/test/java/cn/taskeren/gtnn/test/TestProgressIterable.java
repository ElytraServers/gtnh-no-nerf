package cn.taskeren.gtnn.test;

import cn.taskeren.gtnn.util.ProgressIterable;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestProgressIterable {

	public static void main(String[] args) {
		var list = IntStream.range(0, 10000).mapToObj(i -> i).collect(Collectors.toList());
		var iterable = ProgressIterable.ofCollection(list, 10, System.out::println);

		for(var k : iterable) {
			// ignored
		}
	}

}
