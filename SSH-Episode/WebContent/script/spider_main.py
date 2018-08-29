# coding=utf-8
'''
Created on 2017年10月12日

@author: 卫亮旭
@summary: 爬虫main模块
'''
import url_manager, html_downloader, html_parser, html_outputer
import log
import sys
import os
import time
import random


class SpiderMain(object):
    def __init__(self, old_urls):
        self.default_url = 'http://www.yidianzixun.com/channel/u12131';
        self.base_url = 'http://www.yidianzixun.com/article/0Js4XL6v';
        self.urls = url_manager.UrlManager()  # url管理器
        self.downloader = html_downloader.HtmlDownloader()  # 网页下载器
        self.parser = html_parser.HtmlParser()  # 页面解析器
        self.outputer = html_outputer.HtmlOutputer()  # 输出器
        self.urls.old_urls = old_urls
    
    def crawl(self, log):
        #获取 urls
        try:
            times = 1;
            while times < 3:#执行两次
                times += 1
                log.log('***********************************************')
                log.log('获取内容 url --- 第%d次'%times)
                html_cont = self.downloader.download(self.default_url)
                
                if html_cont is None:
                    log.log('获取 url 失败，退出执行 - 1')
                    return
                new_urls, new_data = self.parser.parse(new_url, html_cont, 1)
                if new_urls is None:
                    log.log('获取 url 失败，退出执行 - 2')
                    return
                log.log('获取到的 url :');
                log.log(new_urls);
                self.urls.add_new_urls(new_urls)  # 保存新的url
                waite = random.randint(20, 60)
                log.log('等待 %d s再获取下一组 url'%waite)
                time.sleep(waite)
                log.log('***********************************************')
        except:# 异常处理
            log.log('crawl failed')
            info = sys.exc_info()
            log.log(info)
        
        #获取数据
        try:
            count = 1
            new_url = ''
            while self.urls.has_new_url():  # url循环
                log.log('***********************************************')
                log.log('这是第%d次执行'%count)
                count += 1
                new_url = self.urls.get_new_url()  # 从url管理其中获取一个新的url
                log.log('crawl %d: %s' % (count, new_url))  # 输出爬取页数
                
                if new_url in self.urls.old_urls:#此 url 已经存在
                    log.log('该页面已访问，继续执行');
                    continue
                html_cont = self.downloader.download(new_url)  # 获取url的网页内容
                if html_cont is None:
                    log.log('获取页面失败，继续执行');
                    continue
                new_urls, new_data = self.parser.parse(new_url, html_cont, 2)
                if new_data is None:
                    log.log('获取数据失败，继续执行')
                    continue
                self.urls.old_urls.add(new_url)#保存已经访问的wrl
#                 self.urls.add_new_urls(new_urls)  # 保存新的url
                self.outputer.collect_data(new_data)  # 保存新的数据
#                 if count == 20:#限定爬取数量
#                     log.log('**爬虫执行结束**')
#                     break
                waite = random.randint(20, 60)
                log.log('等待 %d s再爬下一个网站……'%waite)
                time.sleep(waite)
                log.log('***********************************************')
        except:  # 异常处理
            self.urls.add_new_url(new_url)
            log.log('crawl failed')
            info = sys.exc_info()
            log.log(info)
        
        self.outputer.output_data(self.urls)  # 输出，收集好的数据

if __name__ == '__main__':
    log = log.MyLog('/usr/local/apache-tomcat-7.0.85/webroot/ROOT/script/data/log.dat');
    if os.path.exists(r'/usr/local/apache-tomcat-7.0.85/webroot/ROOT/script/data/') == False:
        os.makedirs(r'/usr/local/apache-tomcat-7.0.85/webroot/ROOT/script/data/')
    # 确保指定文件存在
    #open(r'/usr/local/apache-tomcat-7.0.85/webroot/ROOT/script/data/new_url.dat', 'a').close()
    open(r'/usr/local/apache-tomcat-7.0.85/webroot/ROOT/script/data/old_urls.dat', 'a').close()
    
    # 获取初始化数据
#     with open(r'/usr/local/apache-tomcat-7.0.85/webroot/ROOT/script/data/new_url.dat', 'r') as fout:
#         root_url = fout.readline()
#         if root_url is None or root_url == '':
#             root_url = 'https://www.qiushibaike.com/article/120587673'
#         log.log('url:%s' % root_url)
#         
    # 获取已经访问的url
    with open(r'/usr/local/apache-tomcat-7.0.85/webroot/ROOT/script/data/old_urls.dat', 'r') as fout:
        old_urls = set()
        for url in fout:
            log.log('old_urls:%s' % url.replace('\n', ''))
            old_urls.add(url.replace('\n', ''))
    log.log('***********************开始爬取数据************************')
    # 执行爬虫
    obj_spider = SpiderMain(old_urls)
    obj_spider.crawl(log)
