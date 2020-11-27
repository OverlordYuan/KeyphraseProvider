package casia.ibasic.dubbo.work;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ChinesePhrases {
	private static Logger logger = LoggerFactory.getLogger( ChinesePhrases.class.getName());

	public static List<String> getKeyPhrases(String title, String content, int N){
		List<String> ChinesePhrases = new ArrayList<>();
		if (content.length()>1){
			try{
				content =  content.replaceAll("[^\\u4e00-\\u9fa5^0-9.，,。？“”·—：]+", "");//去除文本中的英文及特殊提付
				if (content.length()>0) {
					GetKeyPhrase.setKeyPhraseNumber(N);
					ChinesePhrases =  GetKeyPhrase.getKeyPhrase(title,content,1);
				}
			}catch (Exception keysError){
				logger.error("keysError:",keysError);
			}
		}
		return ChinesePhrases;
	}
}
