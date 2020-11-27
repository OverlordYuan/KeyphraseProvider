package casia.ibasic.dubbo.work;

import java.util.*;

public class TextRank {

    private static final float d = 0.85f;           //damping factor, default 0.85
    private static final int max_iter = 200;        //max iteration times
    private static final float min_diff = 0.0001f;  //condition to judge whether recurse or not
    private static  int coOccuranceWindow = 3; //size of the co-occurance window, default 3

	private static void setWindowSize(int window) {
		/*设置TextRank的窗口 */
		coOccuranceWindow = window;
	}

	public static Map<String,Float> ExtractTextRank(List<String> wordList){
    	/*
    	使用TextRank计算词权重
    	 */
		Map<String, Set<String>> words = new HashMap<String, Set<String>>();
		Queue<String> que = new LinkedList<String>();
		for (String w : wordList) {
			if (!words.containsKey(w)) {
				words.put(w, new HashSet<String>());
			}
			que.offer(w);    // insert into the end of the queue
			if (que.size() > coOccuranceWindow){
				que.poll();  // pop from the queue
			}
			for (String w1 : que)
				for (String w2 : que){
					if (w1.equals(w2)){
						continue;
					}
					words.get(w1).add(w2);
					words.get(w2).add(w1);
				}
		}
		Map<String, Float> score = new HashMap<String, Float>();
		for (int i = 0; i < max_iter; ++i){
			Map<String, Float> m = new HashMap<String, Float>();
			float max_diff = 0;
			for (Map.Entry<String, Set<String>> entry : words.entrySet()){
				String key = entry.getKey();
				Set<String> value = entry.getValue();
				m.put(key, 1 - d);
				for (String other : value){
					int size = words.get(other).size();
					if (key.equals(other) || size == 0) continue;
					m.put(key, m.get(key) + d / size * (score.get(other) == null ? 0 : score.get(other)));
				}
				max_diff = Math.max(max_diff, Math.abs(m.get(key) - (score.get(key) == null ? 1 : score.get(key))));
			}
			score = m;
			if (max_diff <= min_diff) break;
		}
		return score;
	}
}