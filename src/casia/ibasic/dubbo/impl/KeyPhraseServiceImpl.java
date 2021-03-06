package casia.ibasic.dubbo.impl;

import casia.ibasic.dubbo.service.KeyPhraseService;
import casia.ibasic.dubbo.work.ChinesePhrases;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class KeyPhraseServiceImpl implements KeyPhraseService {

	private static final Logger logger = LoggerFactory.getLogger(KeyPhraseServiceImpl.class.getName());

	public JSONObject ExtractKeyPhrases(JSONObject data){
		long st = System.currentTimeMillis();
		JSONObject o = new JSONObject();
		if(null == data){
			o.put("Error", "ERROR! Empty Json Object");
			return o;
		}
		int number = data.getIntValue("number");
		int languageType = data.getIntValue("language");
		String content = data.getString("content");
		String title = data.getString("title");
		String uuid = "";
		try {
			Object obj = data.get("uuid");
			uuid = (null == obj) ? "" : obj.toString();
		} catch (Exception uuidError) {
			logger.error("数据uuid解析异常");
		}
		String index = "";
		try {
			Object obj = data.get("index");
			index = (null == obj) ? "" : obj.toString();
		} catch (Exception uuidError) {
			logger.error("数据uuid解析异常");
		}
//		content = content.replaceAll("[\r\n\t:，。？！“”、,‘’''（）()【】%|@$<>《》#*^&…]", "");
		try {
			List<String> KeyPhrases = new ArrayList<>();
			if(languageType==1||languageType==31||languageType==32){
				KeyPhrases = ChinesePhrases.getKeyPhrases(title,content,number);
//			}else if (languageType==2){
//				KeyPhrases = ForeignPhrases.getKeyPhrases(title,content,number);
			}else {
				o.put("error","The language is not supported!");
				logger.error("LanguageError:The language is not supported!");
				return o;
			}
			 if(KeyPhrases == null || KeyPhrases.size() == 0) {
				 return null;
			 }
			o.put("KeyPhrase", KeyPhrases);
		} catch (Exception e) {
			o.put("error", e.getMessage());
			e.printStackTrace();
			logger.error("Exception!", e);
		}
		logger.info("Program version 1.Content length = "+content.length()+",uuid = "+ uuid +",indexName = "+ index +",time elapse ={}ms,result={}.",(System.currentTimeMillis() - st),o);
		return o;
	}
	// 测试函数
	  public static void main(String[] args) throws Exception {
		  String title = "10多名医护被感染，武汉为何不早点让公众知情？";
		  String content = "编者按：据武汉市卫健委：目前，武汉市共有15名医务人员确诊为新型冠状病毒感染的肺炎病例，另有1名为疑似病例。16例患者中，危重症1例，其余病情稳定，均已隔离治疗。作为疫情发源地，武汉方面更应该用科学有效的措施进行防控，保障民众生命安全，打好这场硬仗。文 | 陶短房 1月20日晚间，针对“新型冠状病毒感染的肺炎疫情”有关防控情况，国家卫健委高级别专家组组长钟南山院士在央视采访中提到，新型冠状病毒肺炎存在人传人现象，有14名医务人员在护理一名患者过程中被感染。\n" +
				 				  "1月20日，中国卫生部门更新了去年12月源自武汉的新型冠状病毒肺炎（nCoV）疫情通报，截至当天18时，中国境内累计报告病例224例，其中确诊217例（武汉198例，北京5例，广东14例），疑似7例（四川2例，云南1例，上海2例，广西1例，山东1例），死亡3例，境外报告确诊病例4例（韩国1例，日本1例，泰国2例），这些病例中绝大多数，均系短短两天内所通报。\n" +
				 				  "自疫情明朗化以来，中国有关方面和世卫组织（WHO）、各国政府及卫生部门进行了密切配合，国家最高领导人也在关键时刻作出了重要指示，各地相继启动了疫情通报机制和发热门诊专门通道，种种迹象表明，此次中国有关方面在应对类似疫情时，较当年“非典”有了长足进步。\n" +
				 				  "从武汉有关方面来看，2019年12月31日以来，有关部门对疫情进展也建立了通报制度，但是纵观其发布的通报内容，并无“14名医务人员被感染”的信息，而是一直强调可防可控。14人不是个小数字，医务人员更不是对卫生和防疫常识一知半解或一无所知的“白丁”，他们的疫情究竟何时感染？何时发现“疑似”？何时确诊？这些显然需要确切说法。\n" +
				  				  "未通报14名医务人员被感染，只是人们忧心的问题切面。春节将近、春运已启，作为疫情发源地，当地有关方面在防控方面是否做到了位，无疑是公众关心的问题。民众对疫情严重程度和疫情可能传播的途径、预防应对方法的了解，是否及时和足够？当地在防止疫情扩散方面做了哪些努力？有些问题到底是客观条件所限，还是主观因素所致……这些也需要答案。\n" +
				  				  "事实上，此次新型冠状病毒肺炎被发现是比较早的，而且最初的病例集中在武汉市华南海鲜批发市场，“41个病例”也早为人所熟知，由于冠状病毒肺炎和当年肆虐中国境内外、至今思之心有余悸的“非典”系出同源，许多知情者早早就给予了关注。\n" +
				  				  "但当地有关方面直到1月14日起才开始在各“窗口”和大众运输节点启动红外线温度计监测旅客体温；直到1月15日的通报中才首次提及“不能排除有限人传人的可能”，但仍强调“尚未发现明确的人传人证据、持续人传人的风险较低”。\n" +
								  "而此前1天，泰国已报道了一例来自武汉的确诊病例。当时确诊病例仅41起，如果能做到“料敌从宽”，围绕疫情可能出现的最严重后果作出应对，从“41”到“224”或许不会来得这么快。就在1月19日，即中国卫生部门更新疫情发出前一天，当地媒体报道显示，武汉市百步亭社区居然举行了有4万多个家庭参加、包含13986道菜品的“第二十届万家宴”。而要避免疫情和传染病交叉感染，本该尽早对这类大规模人员聚集进行风险告知。\n" +
				 				  "都说“窥一斑而知全豹”，但我希望，这不是当地有关方面疫情防控得力与否的缩影或例证。就目前看，这起疫情传播原理和“人传人”等方面的细节尚待澄清，但其源头十分明确，病例高度集中。可以想见，如果发源地相应防控措施及时、妥善、到位，那也能避免在春运这样的人口大规模流动节点为弥补“大洞”而付出更多，而疲于奔命。\n" +
				 				  "鉴于此，就疫情快速扩散等问题，武汉有关方面宜应该做出解释，若其中存在防控不力的责任人，也要坚决追责，用透明的信息通报制度保障民众的知情权。如今，国家层面对武汉新型冠状病毒肺炎（nCoV）疫情已经高度重视，关于疫情防治指南，根据国家卫健委下发的关于疫情防治指南的诊疗方案，各级政府部门正在有条不紊地来找疫情防治工作。作为疫情发源地，武汉方面更应该用科学有效的措施进行防控，保障民众生命安全，打好这场硬仗。□陶短房（媒体人）";
		  KeyPhraseServiceImpl test = new KeyPhraseServiceImpl();
		  JSONObject o = new JSONObject();
		  o.put("title", title);
		  o.put("content", content);
		  o.put("number", 10);
		  o.put("language",1);
		  o.put("index","news_info");
		  o.put("uuid","123456");
		  System.out.println();
		  JSONObject r = test.ExtractKeyPhrases(o);
		  System.out.println(r);
	  }

}