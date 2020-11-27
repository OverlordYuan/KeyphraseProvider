package casia.ibasic.dubbo.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;


/**
 * 文本关键词提取组件接口
 * @author Overlord.Y
 * @version 1.0 2019.12.16
 * @since jdk1.8
 *
 */
@Path("/analysis_group/demo")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public interface KeyPhraseService {

	/**
	 * 接口服务，通过JSON传输参数，返回参数+时间戳信息
	 */
	@Path("/ExtractKeyPhrase")
	@POST
	JSONObject ExtractKeyPhrases(JSONObject data) throws IOException;
}