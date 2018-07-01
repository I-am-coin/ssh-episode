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

import com.ads.service.PictureService;

@Component
public class PictureScheduler {
	@Resource
	private PictureService pictureService;
	private Logger logger = LogManager.getLogger("DailyRollingFile");
	
	
	/**
	 * 时间任务，指定时间（每天0:20）重复执行
	 * 删除15天以前的图片
	 */
	@Scheduled(cron="0 20 0 * * ?")
	public void deleteEpisode() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 15);//当前时间减去15天
		pictureService.deletePicture(calendar.getTime());
	}
	
	
	/**
	 * 时间任务，指定时间（每天3:00）重复执行
	 * 读取图片文件保存到数据库
	 */
	@Scheduled(cron="0 0 4 * * ?")
	public void addPicture() {
		//获取当天日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String fileName = "img_" + sdf.format(new Date()) + ".dat";//文件名
		String clazzPath = this.getClass().getClassLoader().getResource("").getPath();
		String projectPath = clazzPath.substring(0, clazzPath.indexOf("WEB-INF"));
		String filePath = projectPath + "script/data/img/" + fileName;
		File filePath_f = null;//文件路径
		File destFile = null;//目标文件
		BufferedReader br = null;
		
		logger.warn("读取文件...("+fileName+")");
		try {
			logger.warn(filePath);
			filePath_f = new File(projectPath + "script/data/img/");
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
				if (line.contains("糗")) {
					continue;
				}
				String imgName = line.substring(0, line.indexOf(":"));
				String description =  line.substring(line.indexOf(":")+1);
				String imgPath = "script/data/img/" + imgName;
				logger.warn(imgName);
				// 数据库操作
				pictureService.insertPicture(imgPath, description);
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
