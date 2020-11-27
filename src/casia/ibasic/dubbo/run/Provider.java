package casia.ibasic.dubbo.run;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.FileSystemXmlApplicationContext;


/**
 * 文本关键短语提取组件接口
 * @author Overlord.Y
 * @version 1.0 2020.03.19
 * @since jdk1.8
 *
 */
public class Provider {
	private static Logger logger = Logger.getLogger(Provider.class);
	public static void main(String[] args){
		
		PropertyConfigurator.configure("config/log4j.properties");
		int port = Integer.parseInt(args[0]);
		FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(
				new String[] { "provider_" + port + ".xml" });
		context.start();		
		
		synchronized (Provider.class) {
            while (true) {
                try {
                	Provider.class.wait();
                	Thread.sleep(100);
                } catch (Throwable e) {
                	logger.error(e.getMessage() + "\n" + e.getStackTrace());
                	context.close();
                }
            }
        }
	}
}
