#coding=utf-8
'''
Created on 2017年10月12日

@author: Administrator
'''
import urllib.request
import sys
import log


class HtmlDownloader(object):
    def __init__(self):
        self.log = log.MyLog('/usr/local/apache-tomcat-7.0.85/webroot/ROOT/script/data/log.dat');
    
    def download(self, url):
        '''
        @summary: 下载指定url网页
        @param url: str
        @return: 下载好的网页
        '''
        if url is None:
            return
        # 根据当前URL创建请求包
        req = urllib.request.Request(url)
        # 添加头信息，伪装成浏览器访问
        req.add_header('Accept', '*/*')
#         req.add_header('Accept-Encoding', 'gzip, deflate, sdch, br')
#         req.add_header('Accept-Language', 'zh-CN,zh;q=0.8')
        req.add_header('Connection', 'keep-alive')
        req.add_header('Origin', 'https://www.qiushibaike.com')
#         req.add_header('Referer', url)
        req.add_header('User-Agent', 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.104 Safari/537.36 Core/1.53.4882.400 QQBrowser/9.7.13059.400')
        
        try:
            # 发起请求
            response = urllib.request.urlopen(req)
            if response.getcode() != 200:
                return
            return response.read()
        except:
            info = sys.exc_info()
            self.log.log(info)
            return
    
    



