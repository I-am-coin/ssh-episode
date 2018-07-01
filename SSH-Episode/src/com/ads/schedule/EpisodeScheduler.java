package com.ads.schedule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ads.service.EpisodeService;

@Component
public class EpisodeScheduler {
	@Resource
	private EpisodeService episodeService;
	
	private Logger logger = LogManager.getLogger("DailyRollingFile");
		
	/**
	 * 时间任务，指定时间（每天0:10）重复执行
	 * 删除15天以前的段子
	 */
	@Scheduled(cron="0 10 0 * * ?")
	public void deleteEpisode() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 15);//当前时间减去15天
		episodeService.deleteEpisode(calendar.getTime());
	}
	
	/**
	 * 时间任务，指定时间（每周末0:30）重复执行
	 * 删除oldUrl
	 */
	@Scheduled(cron="0 30 0 ? * 1")
	public void deleteOldUrls() {
		String filePath = "/usr/local/apache-tomcat-7.0.85/webroot/ROOT/script/data/old_urls.dat";
		File oldURLs = new File(filePath);
		
		try {
			if (oldURLs.exists()) {
				oldURLs.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 时间任务，指定时间（每天2:30）重复执行
	 * 执行Python脚本爬取段子保存到文件
	 */
	@Scheduled(cron="0 30 2 * * ?")
	public void execSpider () {
		logger.warn("执行Python脚本...");
		//服务器上的绝对地址
		//Windows:/E:/SSH/WorkSpace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\SSH-Episode\WEB-INF\classes
		//Ubuntu:/home/wlx/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/SSH-Episode/WEB-INF/classes
		String clazzPath = this.getClass().getClassLoader().getResource("").getPath();
		String projectPath = clazzPath.substring(0, clazzPath.indexOf("WEB-INF"));
		logger.warn(clazzPath);
		logger.warn(projectPath);
		String pythonFilePath = "script/spider_main.py";
		logger.warn("python脚本地址:"+projectPath+pythonFilePath);
		String[] cmd = new String[2];
		cmd[0] = "python3";
		cmd[1] = projectPath + pythonFilePath;
		Runtime rt = Runtime.getRuntime();
		Process pr;
		BufferedReader br1 = null;
		BufferedReader br2 = null;
		try {
			//cmd执行Python脚本
			pr = rt.exec(cmd);
			//Charset.forName("GBK")解决中文乱码问题
			br1 = new BufferedReader(new InputStreamReader(pr.getInputStream(), Charset.forName("UTF-8")));
			//输出正常执行信息
			String line = "";
			while((line = br1.readLine()) != null) {
			logger.warn(line);
			}
			//错误信息流
			br2 = new BufferedReader(new InputStreamReader(pr.getErrorStream(), Charset.forName("UTF-8")));
			//输出错误执行信息
			String error = "";
			while((error = br2.readLine()) != null) {
			logger.warn(error);
			}
		} catch (IOException e) {
			logger.warn("执行Python脚本出错");
			e.printStackTrace();
		} finally {//关闭数据流
			try {
				if (br1 != null) {
					br1.close();
				}
				if (br2 != null) {
					br2.close();
				}
			} catch (IOException e) {
					logger.warn("关闭数据流出错");
					e.printStackTrace();
			}
		}
		logger.warn("脚本执行完成！");
	}
	
	/**
	 * 时间任务，指定时间（每天03:30）重复执行
	 * 读取段子文件保存到数据库
	 */
	@Scheduled(cron="0 30 3 * * ?")
	public void addEpisode() {
		//获取当天日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String fileName = "episode_" + sdf.format(new Date()) + ".dat";//文件名
		String clazzPath = this.getClass().getClassLoader().getResource("").getPath();
		String projectPath = clazzPath.substring(0, clazzPath.indexOf("WEB-INF"));
		String filePath = projectPath + "script/data/episode/" + fileName;
		File filePath_f = null;//文件路径
		File destFile = null;//目标文件
		BufferedReader br = null;
		
		logger.warn("读取文件...("+fileName+")");
		try {
			logger.warn(filePath);
			filePath_f = new File(projectPath + "script/data/episode/");
			destFile = new File(filePath);
			//确保文件存在
			if (!filePath_f.exists()) {
				filePath_f.mkdirs();
			}
			if (!destFile.exists()) {
				destFile.createNewFile();
			}
			//获取输入流，Charset.forName("UTF-8")解决中文乱码问题
			br = new BufferedReader(new InputStreamReader(new FileInputStream(destFile), Charset.forName("UTF-8")));
			//读取数据，存入数据库
			logger.warn("保存数据中...");
			String line = "";
			while ((line=br.readLine()) != null) {
				logger.warn(line);
				if (line.contains("糗")) {
					continue;
				}
				episodeService.insertEpisode(line);
			}
			logger.warn("保存完成！");

		} catch (FileNotFoundException e) {
			logger.warn("文件打开异常");
			e.printStackTrace();
		} catch (IOException e) {
			logger.warn("文件读取异常");
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.warn("文件关闭异常");
					e.printStackTrace();
				}
			}
		}
	}
}
