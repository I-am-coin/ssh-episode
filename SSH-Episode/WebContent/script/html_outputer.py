#coding=utf-8
'''
Created on 2017年10月12日

@author: Administrator
'''
import log
import time
import os
from idlelib.IOBinding import encoding
import urllib


class HtmlOutputer(object):
    def __init__(self):
        self.log = log.MyLog(r'/usr/local/apache-tomcat-7.0.85/webroot/ROOT/script/data/log.dat');
        self.data = []
        self.count = 1
    
    def collect_data(self, data):
        '''
        @summary: 收集数据
        @param data: 单个数据-str
        '''
        if data is None:
            return
        self.data.append(data)
    
    def output_data(self, urls):
        self.log.log(r'正在保存数据……')
        #保存段子数据到文件中
        if os.path.exists(r'/usr/local/apache-tomcat-7.0.85/webroot/ROOT/script/data/episode') == False:
            os.makedirs(r'/usr/local/apache-tomcat-7.0.85/webroot/ROOT/script/data/episode')
        this_time = time.strftime('%Y%m%d', time.localtime(time.time()))
        #保存图片数据到文件中
        if os.path.exists(r'/usr/local/apache-tomcat-7.0.85/webroot/ROOT/script/data/img') == False:
            os.makedirs(r'/usr/local/apache-tomcat-7.0.85/webroot/ROOT/script/data/img')
        
        for data in self.data:
            if data['img'] != None:
                index = data['img'].rfind('.')
                img_name = this_time+'_'+str(self.count)+data['img'][index:]
                self.count += 1
                
                #保存图片
                with open(r'/usr/local/apache-tomcat-7.0.85/webroot/ROOT/script/data/img/%s'%img_name, 'wb') as img:
                    byte = urllib.request.urlopen('https:'+data['img']);
                    img.write(byte.read())
                    self.log.log(r'图片保存（https:%s）。。。'%(data['img']))
                #保存标题
                with open(r'/usr/local/apache-tomcat-7.0.85/webroot/ROOT/script/data/img/img_%s.dat'%this_time, 'a', encoding='utf-8') as fout:
                    #replace(u'\xa0', u' ')防止网页中的&nbsp;转换异常
                    if data['content'] != None and data['content'][0:4] == 'img:':
                        fout.write((img_name+':'+data['content'][4:])+'\n')
                        self.log.log(r'图片标题保存。。。')
            else:#没有图片，段子
                with open(r'/usr/local/apache-tomcat-7.0.85/webroot/ROOT/script/data/episode/episode_%s.dat'%this_time, 'a', encoding='utf-8') as fout:
                    #replace(u'\xa0', u' ')防止网页中的&nbsp;转换异常
                    if data['content'] != None and data['content'][0:4] != 'img:':
                        fout.write(data['content']+'\n')
                        self.log.log(r'段子保存。。。')
    
        self.log.log(r'数据保存完成……')
        
        self.log.log(r'正在保存URLs……')
        #保存访问过的url 到文件中
        with open(r'/usr/local/apache-tomcat-7.0.85/webroot/ROOT/script/data/old_urls.dat', 'w', encoding='utf-8') as fout:
            old_urls = urls.old_urls;
            for url in old_urls:
                fout.write(url+'\n')
        self.log.log(r'old_urls保存完成(data/old_urls.dat)')
    
        #保存一个新的url
        with open(r'/usr/local/apache-tomcat-7.0.85/webroot/ROOT/script/data/new_url.dat', 'w', encoding='utf-8') as fout:
            if urls.has_new_url():
                fout.write(urls.get_new_url())
        self.log.log(r'new_url保存完成(data/new_url.dat)')
    



