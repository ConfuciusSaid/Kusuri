import json
import os
import re
import time
from random import random
# import threading
# import toml
# from selenium import webdriver
import redis
import requests
import uuid

userId = ""
redis_conf = {
    "host": "",
    "port": ,
    "password": "",
    "db": 0
}

cookies = {
    "SESSDATA": "",
    "buvid3": "",
    "bili_jct": "",
    "DedeUserID": userId,
}

url = "https://api.vc.bilibili.com/session_svr/v1/session_svr/new_sessions?build=0&mobi_app=web&begin_ts="
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36",
    'Origin': 'https://message.bilibili.com', 'Referer': 'https://message.bilibili.com/',
    "Content-Type": "application/json"}

cookies["buvid3"] = str(uuid.uuid1())
cookies["Domain"] = ".bilibili.com"
session = requests.session()


def checkCode(uid, code):
    print(str(uid) + " : " + code)
    with redis.StrictRedis(**redis_conf) as r:
        if r.exists(f"user:code:{code}") == 1:
            path = f"/data/avatar/{uid}.jpg"
            # 获取粉丝牌
            resp = requests.get(f"https://api.live.bilibili.com/xlive/web-ucenter/user/MedalWall?target_id={uid}",
                                headers=headers,
                                cookies=cookies)
            resp.close()
            medals = json.loads(resp.text)['data']['list']
            level = 0
            if medals is not None:
                for medal in medals:
                    if medal['medal_info']['target_id'] == 1683159082 or medal['medal_info']['target_id'] == 5832770:
                        if level < medal['medal_info']['level']:
                            level = medal['medal_info']['level']
                        
            if uid == 1683159082 or uid == 5832770:
                level = 99
            
            # 获取用户信息
            resp = requests.get(f"https://api.bilibili.com/x/web-interface/card?mid={uid}&photo=1&jsonp=jsonp",
                                headers=headers,
                                cookies=cookies)
            resp.close()
            info = json.loads(resp.text)['data']['card']

            # 写入redis
            value = {
                "name": info["name"],
                "uid": uid,
                "level": level
            }
            r.set(f"user:code:{code}", json.dumps(value), ex=480)

            if not os.path.exists(path):
                with open(path, mode="wb") as f:
                    resp = requests.get(info["face"])
                    resp.close()
                    f.write(resp.content)

    # with redis.StrictRedis(**redis_conf) as r:
    #     print(r.exists("user:code:test123"))
    #     print(r.exists("user:code:test124"))


codeMatcher = re.compile("""/auth (?P<code>[0-9a-zA-Z]+)[ ]*$""", re.S)


def checkMsg(ts):
    cookies["buvid3"] = str(uuid.uuid1())
    resp = session.get(url=url + str(ts), headers=headers, cookies=cookies)
    msgs = json.loads(resp.text)['data']['session_list']
    if msgs is not None:
        for msg in msgs:
            text = json.loads(msg['last_msg']['content'])
            if 'content' in text:
                result = codeMatcher.search(text['content'])
                if result is not None:
                    checkCode(msg['talker_id'], result.group("code"))
            if int(msg['session_ts']) > ts:
                ts = int(msg['session_ts']) + 1
    return ts


begin_ts = int(time.time() * 1000000)

while True:
    try:
        try:
            begin_ts = checkMsg(begin_ts)
        except requests.exceptions.RequestException as e:
            print("request :" + str(e))
    except Exception as e:
        print("other :" + str(e))
        begin_ts = int(time.time() * 1000000)

    time.sleep(3 + random() * 2)

    # opt = webdriver.ChromeOptions()
    # opt.add_argument("--headless")
    # opt.add_argument("--blink-settings=imagesEnabled=true")
    #
    # def refreshAccount():
    #     while True:
    #         try:
    #             with webdriver.Chrome(options=opt) as d:
    #                 d.get('https://bilibili.com/')
    #                 d.add_cookie(cookies)
    #                 d.refresh()
    #             time.sleep(60 * 60 * 5)
    #             print("refresh success")
    #         except requests.exceptions.RequestException as e:
    #             print(e)
    #
    #
    # refreshThread = threading.Thread(target=refreshAccount)
    # refreshThread.daemon = True
    # refreshThread.start()
