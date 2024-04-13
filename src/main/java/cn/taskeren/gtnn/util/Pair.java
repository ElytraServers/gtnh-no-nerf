package cn.taskeren.gtnn.util;

public class Pair<A, B> {

	private final A a;
	private final B b;

	public Pair(A a, B b) {
		this.a = a;
		this.b = b;
	}

	public A getLeft() {
		return a;
	}

	public B getRight() {
		return b;
	}

}
