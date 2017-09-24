package org.unclazz.parsec;

import java.util.LinkedList;
import java.util.Queue;

import org.unclazz.parsec.data.ReadOnlyList;

final class ListParser<T> extends RepeatReduceValParser<T, Queue<T>, ReadOnlyList<T>>{
	private static<T> Queue<T> seed() {
		return new LinkedList<>();
	}
	private static<T> Queue<T> accumulate(Queue<T> a, T b){
		a.offer(b);
		return a;
	}
	private static<T> ReduceConfig<T, Queue<T>, ReadOnlyList<T>> reduceConfig(){
		return new ReduceConfig<T, Queue<T>, ReadOnlyList<T>>(ListParser::seed, ListParser::accumulate, a -> ReadOnlyList.of(a));
	}
	
	protected ListParser(ValParser<T> original, RepeatConfig repConf) {
		super(original, repConf, reduceConfig());
	}
}