package casia.ibasic.dubbo.work;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.common.Term;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class chunk {

	public static List<Term> Chinese(String str) throws IOException {
        /*
        中文短语抽取
         */
		Pattern r = Pattern.compile("a*n+");
//		List<String> result = new ArrayList<>();
		List<Term> termList = HanLP.segment(str);
		String label = "";
		for (Term t : termList){
			label += t.nature.toString().substring(0,1).replace("j","n");
		}
		Matcher m = r.matcher(label);
		while (m.find()) {
			termList.get(m.start()).nature =  Nature.fromString("null");
			for(int i = m.start()+1;i<m.end();i++){
				termList.get(m.start()).word += termList.get(i).word;
//				termList.get(i).nature = Nature.fromString("s");
			}
		}
		return termList;
	}
}
