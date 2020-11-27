package casia.ibasic.dubbo.work;

import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;

import java.io.IOException;
import java.util.*;

public class GetKeyPhrase {

	private static final float weight = 0.09f;
	private static  int nKeyword = 10;         //number of keywords to extract,default 10

	static void setKeyPhraseNumber(int sysKeywordNum){
		/*设置获取的关键词数目*/
		if(sysKeywordNum<10&&sysKeywordNum>0){
			nKeyword = sysKeywordNum;
		}
	}


	private static boolean shouldInclude(Term term){
		return (CoreStopWordDictionary.shouldInclude(term))&&(term.word.length()>4)&&(term.word.length()<26)&&(checkname(term.word));
	}

	private static boolean checkname(String name) {
		/*检查字符串是否全为中文*/
		int n;
		for(int i = 0; i < name.length(); i++) {
			n = (int)name.charAt(i);
			if(!(19968 <= n && n <40869)) {
				return false;
			}
		}
		return true;
	}


	public static List<String> getKeyPhrase(String title, String content, int type) throws IOException {
		/*获取关键词入口*/
		Map<String, Float> a = getKeyPhraseWithWeight(title,content,type);
		List<Map.Entry<String, Float>> entryList = new ArrayList<Map.Entry<String, Float>>(a.entrySet());
		Collections.sort(entryList, new Comparator<Map.Entry<String, Float>>(){
			@Override
			public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2){
				return o2.getValue().compareTo(o1.getValue());
			}});
		List<String> sysKeywordList = new ArrayList();

		for (int i = 0; i < nKeyword; ++i){
			try{
				if(entryList.get(i).getValue()>=weight){
					sysKeywordList.add(entryList.get(i).getKey());
					System.out.println(entryList.get(i));
				}
			}catch(IndexOutOfBoundsException e){
				continue;
			}
		}
		return sysKeywordList;
	}



	public static Map<String, Float> getKeyPhraseWithWeight(String title, String content,int type) throws IOException {
        /*
        获取前top10词，并进行权重归一化
        */
		Map<String, Float> score = getKeyPhraseScore(title, content,type);
		//title内词权重加权
		List<Map.Entry<String, Float>> entryList = new ArrayList<Map.Entry<String, Float>>(score.entrySet());
		for (Map.Entry<String, Float> obj:entryList){
			if (title.contains(obj.getKey())){
				obj.setValue(obj.getValue()*2);
			}
		}
		//候选词按权重降序重排
		Collections.sort(entryList, new Comparator<Map.Entry<String, Float>>(){
			@Override
			public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2){
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		Map<String, Float> sysKeywordList=new HashMap<>();
		Float sum = 0f;
		for (int i = 0; i < 10; ++i){
			try{
				sum += entryList.get(i).getValue();
			}catch(IndexOutOfBoundsException e){
				continue;
			}
		}

		for (int i = 0; i < 10; i++){
			try{
				sysKeywordList.put(entryList.get(i).getKey(),entryList.get(i).getValue()/sum);
//				sysKeywordList.put(entryList.get(i).getKey(),entryList.get(i).getValue());

			}catch(IndexOutOfBoundsException e){
				continue;
			}
		}
		return sysKeywordList;
	}

	private static Map<String, Float> getKeyPhraseScore(String title, String content, int type) throws IOException {
        /*
        权重计算
         */
		List<String> wordList=new ArrayList<String>();
		//中文关键候选列表
		if(type==1){
			List<Term> termList = chunk.Chinese(title + content);
			for (Term t : termList){
				if (shouldInclude(t)){
					if(t.nature==null){
						wordList.add(t.word);
					}
				}
			}
		}
		return TextRank.ExtractTextRank(wordList);
	}

}
