package casia.ibasic.dubbo.run;

import casia.ibasic.dubbo.work.ChinesePhrases;
import com.alibaba.fastjson.JSONObject;
import casia.ibasic.dubbo.util.ExcelUtils;
import casia.ibasic.dubbo.util.csvUtils;
import casia.ibasic.dubbo.util.readfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class run_KeyPhrases {
	private static Logger logger = LoggerFactory.getLogger(run_KeyPhrases.class.getName());
	private static ExcelUtils ex = new ExcelUtils();
	private static csvUtils cs = new csvUtils();
	private static readfile rf = new readfile();
	private static List<String> con = new ArrayList<String>();
	private static List<String> titles_list = new ArrayList<String>();
	private static List<String> keywords = new ArrayList<String>();

	private static void save(String name){
			JSONObject res = new JSONObject();
			res.put("titles",titles_list);
			res.put("content",con);
			res.put("keyPhrases",keywords);
			String[] INFO = {"titles","content","keyPhrases"};
			String outpath ="output/"+name+".xls";
			ex.exportExcel(outpath,res,INFO);
		}

	public static void count_num(List array,String name) {
		Set<String> set=new HashSet<>();
		for(int i=0;i<array.size();i++)
		{
			set.add(array.get(i).toString());
		}
		List<String> list = new ArrayList<>(set);
//		Collections.sort(list);
		List<Integer> count_set = new ArrayList<>();
		for(int i=0;i<list.size();i++) {
			int count=0;
			for(int j=0;j<array.size();j++) {
				if(list.get(i).equals(array.get(j))){
					count++;
				}
			}
			count_set.add(count);
//			System.out.println(list.get(i)+":"+count+"次");
		}
		JSONObject res = new JSONObject();
		res.put("count_num",count_set);
		res.put("keyPhrases",list);
//		res.put("keywords_array",array);
		String[] INFO = {"keyPhrases","count_num"};
		String outpath ="output/"+name+".xls";
		ex.exportExcel(outpath,res,INFO);

	}

	public static void list_save(List array,String name) {


		JSONObject res = new JSONObject();
		res.put("keyPhrases",array);

//		res.put("keywords_array",array);
		String[] INFO = {"keyPhrases"};
		String outpath ="output/"+name+"-keyPhrases.xls";
		ex.exportExcel(outpath,res,INFO);

	}
		public static void main(String[] args) throws InterruptedException {
			String file_name = "阿斯利康";
//			String file_name = "奥运会";
			List<JSONObject> files = rf.read("input/"+file_name+".csv");
			int j = 0;
			long start = System.currentTimeMillis();
			int count = 0;
			List<String> keys = new ArrayList<>();
			for(JSONObject file:files){
				List<List<String>> content = cs.readCSV(file.get("absolutepath").toString());
				List<String> contents = content.get(0);
				List<String> titles = content.get(1);
				count += content.size();
				int i = 0;
				for(int k=0;k<titles.size();k++){
					System.out.println(i++);
					String title = titles.get(k);
					String item = contents.get(k);
					List obj = ChinesePhrases.getKeyPhrases(title,item,10);
//					List obj_with = ChinesePhrases.getKeyPhrases(title,item,10);
					System.out.println(obj);
					con.add(item);
					titles_list.add(title);
					keywords.add(obj.toString());
					for(Object it :obj){
						keys.add(it.toString());
					}
				}
			}
			list_save(keys,file_name);
//			count_num(keys,file_name);
//			save(file_name);
		System.out.println(count);
		long end = System.currentTimeMillis();
		logger.info("Time elapse = {} ms.",(end - start));
	}
}

