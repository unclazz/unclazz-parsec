package org.unclazz.parsec;

import java.util.LinkedList;
import java.util.Queue;

import org.unclazz.parsec.data.Seq;

final class SeqParser<T> extends RepeatReduceValParser<T, Queue<T>, Seq<T>>{
	private static<T> Queue<T> seed() {
		return new LinkedList<>();
	}
	private static<T> Queue<T> accumulate(Queue<T> a, T b){
		a.offer(b);
		return a;
	}
	private static<T> ReduceConfig<T, Queue<T>, Seq<T>> reduceConfig(){
		return new ReduceConfig<T, Queue<T>, Seq<T>>(SeqParser::seed, SeqParser::accumulate, a -> Seq.of(a));
	}
	
	protected SeqParser(ValParser<T> original, RepeatConfig repConf) {
		super(original, repConf, reduceConfig());
	}
}