# Facebook page crawler 
# With Database deployment
#
# Wed Mar 12  9:49 PM
# author: Olivia Zhang

# Packages used in the program
# This program automatically crawl information from U.S. Department of Veterans Affairs page
# and use peewee for ORM into MySQL database


import requests # pip install requests
import json
import facebook
import csv
import MySQLdb
import peewee
from peewee import *
import sys
#

base_url2 = 'https://graph.facebook.com/15408433177/feed'
ACCESS_TOKEN= 'CAAJuBSwq0DEBAAjKBPlgRYbIZBcQNDIZBVZC5bo7hWHdTteAO6ZCzVyKpwj8VYzk0exiXY2WKWK5cun17VnHTgcF3kZAPyqkrFXzZCZB9c79bsKe6jFFafTqHNzSM8RLnQtcPWjLFWXTsNpnepnY8yeBiNdE2Ih7GDGeg5mbdqnYuOpD1FQ3EZC9OVS9El0S4BgZD'

reload(sys)

sys.setdefaultencoding("UTF-8")
url = '%s?access_token=%s' % (base_url2, ACCESS_TOKEN)

content = requests.get(url).json()
data= content['data']

db = MySQLDatabase('mitre', user='admin',passwd='mitre')

# table model
class Like(peewee.Model):
    post_id_pk = peewee.CharField()
    user_id_pk = peewee.CharField()
    class Meta:
       database = db
       primary_key = CompositeKey('post_id_pk','user_id_pk')

class Post(peewee.Model):
    post_id_pk = peewee.CharField(primary_key = True)
    page_id = peewee.CharField(null=False)
    created_time = peewee.CharField()

    class Meta:
        database = db

class Comment(peewee.Model):
    commend_id_pk = peewee.CharField(primary_key = True)
    user_id = peewee.CharField()
    post_id = peewee.CharField()
    created_time = peewee.CharField()
    message = peewee.TextField()
    like_count = peewee.CharField()

    class Meta:
        database = db

class Page(peewee.Model):
    page_id_pk = peewee.CharField(primary_key = True)
    page_name = peewee.CharField()
    likes_count = peewee.IntegerField()
    talking_count = peewee.IntegerField()

    class Meta:
        database = db

class User(peewee.Model):
    user_id_pk = peewee.CharField()
    user_name = peewee.CharField()
    gender = peewee.TextField(null=True)
    age = peewee.CharField(null=True)
    occupation = peewee.TextField(null=True)
    is_veteran = peewee.BooleanField(null=True)

    class Meta:
        database = db

def getUsers(feed):
    gender =""
    if 'comments' not in feed:
        return
    commentdata = feed['comments']
    while True:
        if 'data' not in commentdata:
            return
        for r in commentdata['data']:
            userid = r['from']['id']
            created_time = r['created_time']
            username = r['from']['name']
            username = username.encode('unicode_escape')
            if (created_time[:4] < "2013" or created_time[:7] > "2014-02"):
                return
            print userid,username
            db.set_autocommit(False)
            try:
                user = User(user_id_pk=userid,user_name=username)
                user.save()
            except:
                db.rollback()
            finally:
                db.commit()
        if 'next' in commentdata['paging']:
            commenturl=commentdata['paging']['next']
        else:
            return
        commentdata=requests.get(commenturl, timeout = 20).json()

def getPosts(feed):
    postid=feed['id']
    page_id = "15408433177" # please change
    postdata=feed['likes']
    created_time = feed['created_time']
    while True:
        if 'data' not in postdata:
            return
        for r in postdata['data']:
            if(created_time[:4] < "2013" or created_time[:7] > "2014-02"):
                return
            try:
                post = Post(post_id_pk=postid, page_id=page_id, created_time=created_time, )
                post.save(force_insert=True)
                post.save()
            except:
                db.rollback()
            finally:
                db.commit()
        if 'next' in postdata['paging']: # go to next page
            posturl=postdata['paging']['next']
        else:
            break
        postdata=requests.get(posturl, timeout = 15).json()

def getComments(feed):
    postid=feed['id']
    if 'comments' not in feed:
        return 
    commentdata=feed['comments']
    while True:
        if 'data' not in commentdata:
            return
        for r in commentdata['data']:
            userid = r['from']['id']
            created_time = r['created_time']
            like_count = r['like_count']
            message = r['message'] 
            message = message.encode('unicode_escape')
            commentid = r['id']
            if(created_time[:4] < "2013" or created_time[:7] > "2014-2"): # extract data after 2013
                return
            print userid, postid, like_count
            db.set_autocommit(False)
            try:
                comment = Comment(commend_id_pk=commentid,user_id=userid, post_id = postid, created_time=created_time, message=message,like_count=like_count)
                comment.save(force_insert=True)
                comment.save()
            except:
                db.rollback()
            finally:
                db.commit()
        if 'next' in commentdata['paging']:
            commenturl=commentdata['paging']['next']
        else:
            break
        commentdata=requests.get(commenturl, timeout = 15).json()

def getlikes(feed):
    postid=feed['id']
    likedata=feed['likes']
    created_time = feed['created_time']
    while True:
        if 'data' not in likedata:
            return
        for r in likedata['data']:
            userid = r['id']
            if(created_time[:4] < "2013" and created_time[:7] > "2014-2"):
                return
            print userid, created_time
            try:
                like = Like(post_id_pk=postid, user_id_pk=userid)
                like.save(force_insert=True)
                like.save()
            except:
                db.rollback()
            finally:
                db.commit()
        if 'next' in likedata['paging']: # go to next page
            likeurl=likedata['paging']['next']
        else:
            break
        likedata=requests.get(likeurl, timeout = 15).json()

content = requests.get(url, timeout = 15).json()

def writePage(): # please change
    page_id = "15408433177"
    name = "U.S. Department of Veterans Affairs"
    likes_count = 400824
    talking_count = 85733
    print page_id, name
    try:
        page = Page(page_id_pk=page_id, page_name=name, likes_count=likes_count, talking_count=talking_count)
        page.save(force_insert=True)
        page.save()
    except:
        db.rollback()
    finally:
        db.commit()

def writeTables(url):
    currentUrl=url
    while True:
        content = requests.get(currentUrl, timeout = 20).json()
        if "data" not in content:
            break
        data = content['data']
        for d in data:
            getUsers(d)
            getComments(d)
            getlikes(d)
            getPosts(d)
            writePage()
        currentUrl = content['paging']['next']

writeTables(url)