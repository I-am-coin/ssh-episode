# coding=utf-8
'''
Created on 2018年06月09日

@author: 卫亮旭
@summary: 输入系统日志
'''
import time

class MyLog(object):
    def __init__(self, path):
        self.path = path;
    
    def log(self, info):
        with open(self.path, 'a') as f:
            this_time = time.strftime('%Y%m%d', time.localtime(time.time()))
            f.write(this_time+':'+str(info)+'\n')