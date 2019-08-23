package com.cloud.rediswebmanager.controller;



import com.cloud.rediswebmanager.cache.RedisCache;
import com.cloud.rediswebmanager.config.JedisConfig;
import com.cloud.rediswebmanager.vo.*;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.*;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@RestController
public class RedisController {
    private static final Logger logger = LoggerFactory.getLogger(RedisController.class);
    @Autowired
    private JedisPoolConfig jedisPoolConfig;
    @Autowired
    private RedisCache redisCache;
    @ApiOperation("新建连接")
    @RequestMapping(value = "/newConn",method = RequestMethod.POST)
    public String newConn(@RequestBody RedisConn redisConn){
        String name = redisConn.getHost()+":"+redisConn.getPort();
        JedisPool jedisPool = redisCache.getConn(name);
        if(jedisPool == null){
            if(redisConn.getAuth() != null && !redisConn.getAuth().equals("")){
                jedisPool = new JedisPool(jedisPoolConfig,redisConn.getHost(),redisConn.getPort(),1000,redisConn.getAuth());
            }else{
                jedisPool = new JedisPool(jedisPoolConfig,redisConn.getHost(),redisConn.getPort(),1000);
            }
            try(Jedis jedis = jedisPool.getResource()){
                redisCache.addConn(name,jedisPool);
            }catch (Exception e){
                logger.error("连接失败，请检查您的ip和port，再重新尝试");
                name = "连接失败，请检查您的ip和port，再重新尝试";
            }
        }
        return name;
    }

    @ApiOperation("查看所有键")
    @RequestMapping(value = "/getAllKey",method = RequestMethod.POST)
    public List<String> getAllKey(@RequestBody PostValue value){
        List<String> list = null;
        Jedis jedis = getJedis(value);
        if(jedis == null)
            return list;
        ScanParams scanParams = new ScanParams();
        scanParams.count(2);
        String key = value.getKey()==null?"*":"*"+value.getKey()+"*";
        scanParams.match(key);
        String cursor = value.getIndex()+"";
        ScanResult<String> result = jedis.scan(cursor,scanParams);
        list = result.getResult();
        list.add(0,result.getCursor());
        return list;
    }

    @ApiOperation("查看键")
    @RequestMapping(value = "/lookUpKey",method = RequestMethod.POST)
    public PostResponse lookUpKey(@RequestBody PostValue value) throws Exception {
        PostResponse res = new PostResponse();
        String key = value.getKey();
        String connName = value.getConnName();
        Jedis jedis = getJedis(value);
        if(jedis == null){
            res.setValue("请先连接redis数据库");
            return res;
        }

        String type = jedis.exists(key)?jedis.type(key):"null";
        res.setKey(key);
        res.setTtl(jedis.ttl(key));
        switch (type){
            case "string":
                String v = jedis.get(key);
                res.setValue(v);
                res.setType(KeyType.STRING);
                break;
            case "list":
                List<String> lv = jedis.lrange(key,0,-1);
                res.setValue(lv);
                res.setType(KeyType.LIST);
                break;
            case "set":
                Set<String> sv = jedis.smembers(key);
                res.setValue(sv);
                res.setType(KeyType.SET);
                break;
            case "hash":
                Map<String,String> hv = jedis.hgetAll(key);
                res.setValue(hv);
                res.setType(KeyType.HASH);
                break;
            case "zset":
                Set<String> zsv = jedis.smembers(key);
                res.setValue(zsv);
                res.setType(KeyType.ZSET);
            case "null":
                res.setValue(null);
                break;
                default:
                    throw new Exception("类型异常");
        }
        jedis.close();
        return res;
    }

    @ApiOperation("添加string")
    @RequestMapping(value = "/addStringKey",method = RequestMethod.POST)
    public PostResponse addStringKey(@RequestBody PostString postString){
        Jedis jedis = getJedis(postString);
        PostResponse res = new PostResponse();
        if(jedis == null){
            res.setValue("请先连接redis数据库");
            return res;
        }
        jedis.set(postString.getKey(),postString.getValue());
        res.setValue(postString.getValue());
        res.setKey(postString.getKey());
        res.setType(KeyType.STRING);
        res.setTtl(jedis.ttl(postString.getKey()));
        jedis.close();
        return res;
    }
    @ApiOperation("删除string")
    @RequestMapping(value = "delStringKey",method = RequestMethod.POST)
    public String delStringKey(@RequestBody PostString postString){
        Jedis jedis = getJedis(postString);
        if(jedis == null){
            return "请先连接redis数据库";
        }
        jedis.del(postString.getKey());
        jedis.close();
        return "success";
    }
    @ApiOperation("添加list值")
    @RequestMapping(value = "addListKey",method = RequestMethod.POST)
    public PostResponse addListKey(@RequestBody PostList postList){
        PostResponse res = new PostResponse();
        Jedis jedis = getJedis(postList);
        if(jedis == null){
            res.setValue("请先连接redis数据库");
            return res;
        }
        jedis.lpush(postList.getKey(),postList.getValue());
        res.setKey(postList.getKey());
        res.setValue(jedis.lrange(postList.getKey(),0,-1));
        res.setTtl(jedis.ttl(postList.getKey()));
        res.setType(KeyType.LIST);
        jedis.close();
        return res;
    }

    @ApiOperation("删除list值")
    @RequestMapping(value = "/delListKey",method = RequestMethod.POST)
    public PostResponse delListKey(@RequestBody PostList postList){
        PostResponse res = new PostResponse();
        Jedis jedis = getJedis(postList);
        if(jedis == null){
            res.setValue("请先连接redis数据库");
            return res;
        }
        jedis.lrem(postList.getKey(),1,postList.getValue());
        res.setKey(postList.getKey());
        res.setType(KeyType.LIST);
        res.setTtl(jedis.ttl(postList.getKey()));
        res.setValue(jedis.lrange(postList.getKey(),0,-1));
        jedis.close();
        return res;
    }

    @ApiOperation("添加set值")
    @RequestMapping(value = "/addSetKey",method = RequestMethod.POST)
    public PostResponse addSetKey(@RequestBody PostSet postSet){
        PostResponse res = new PostResponse();
        Jedis jedis = getJedis(postSet);
        if(jedis == null){
            res.setValue("请先连接redis数据库");
            return res;
        }
        jedis.sadd(postSet.getKey(),postSet.getValue());
        res.setValue(jedis.smembers(postSet.getKey()));
        res.setKey(postSet.getKey());
        res.setTtl(jedis.ttl(postSet.getKey()));
        res.setType(KeyType.SET);
        jedis.close();
        return res;
    }
    @ApiOperation("删除set值")
    @RequestMapping(value = "delSetKey",method = RequestMethod.POST)
    public PostResponse delSetKey(@RequestBody PostSet postSet){
        PostResponse res = new PostResponse();
        Jedis jedis = getJedis(postSet);
        if(jedis == null){
            res.setValue("请先连接redis数据库");
            return res;
        }
        jedis.srem(postSet.getKey(),postSet.getValue());
        res.setValue(jedis.smembers(postSet.getKey()));
        res.setKey(postSet.getKey());
        res.setTtl(jedis.ttl(postSet.getKey()));
        res.setType(KeyType.SET);
        jedis.close();
        return res;
    }

    @ApiOperation("模糊查询set")
    @RequestMapping(value = "vagueSearchSet",method = RequestMethod.POST)
    public PostResponse vagueSearchSet(@RequestBody PostValue value){
        PostResponse res = new PostResponse();
        List<String> list = null;
        Jedis jedis = getJedis(value);
        if(jedis == null){
            res.setValue("请先连接到redis数据库");
            return res;
        }
        ScanParams scanParams = new ScanParams();
        scanParams.count(500);
        String key ="*"+value.getPattern()+"*";
        scanParams.match(key);
        String cursor = value.getIndex()+"";
        ScanResult<String> result = jedis.sscan(value.getKey(),cursor,scanParams);
        jedis.close();
        list = result.getResult();
        list.add(0,result.getCursor());
        res.setValue(list);
        res.setType(KeyType.LIST);
        res.setKey(value.getKey());
        res.setTtl(jedis.ttl(value.getKey()));
        jedis.close();
        return res;
    }

    @ApiOperation("添加hash值")
    @RequestMapping(value = "addHashKey",method = RequestMethod.POST)
    public PostResponse addHashKey(@RequestBody PostHash postHash){
        PostResponse res = new PostResponse();
        Jedis jedis = getJedis(postHash);
        if(jedis == null){
            res.setValue("请先连接redis数据库");
            return res;
        }
        Map<String,String> map = jedis.hgetAll(postHash.getKey());
        if(map == null){
            map = new HashMap<String,String>();
        }
        map.put(postHash.getField(),postHash.getValue());
        jedis.hmset(postHash.getKey(),map);
        res.setType(KeyType.HASH);
        res.setTtl(jedis.ttl(postHash.getKey()));
        res.setKey(postHash.getKey());
        res.setValue(map);
        jedis.close();
        return res;
    }

    @ApiOperation("删除hash值")
    @RequestMapping(value = "delHashKey",method = RequestMethod.POST)
    public PostResponse delHashKey(@RequestBody PostHash postHash){
        PostResponse res = new PostResponse();
        Jedis jedis = getJedis(postHash);
        if(jedis == null){
            res.setValue("请先连接到redis数据库");
            return res;
        }
        jedis.hdel(postHash.getKey(),postHash.getField());
        res.setKey(postHash.getKey());
        res.setTtl(jedis.ttl(postHash.getKey()));
        res.setValue(jedis.hgetAll(postHash.getKey()));
        res.setType(KeyType.HASH);
        jedis.close();
        return res;
    }

    @ApiOperation("模糊查询hash值")
    @RequestMapping(value = "vagueSearchHash",method = RequestMethod.POST)
    public PostResponse vagueSearchHash(@RequestBody PostValue postValue){
        PostResponse res = new PostResponse();
        Jedis jedis = getJedis(postValue);
        if(jedis == null){
            res.setValue("请先连接到redis数据库");
            return res;
        }
        ScanParams scanParams = new ScanParams();
        scanParams.match("*"+postValue.getPattern()+"*");
        scanParams.count(500);
        String cursor = postValue.getIndex()+"";
        ScanResult result = jedis.hscan(postValue.getKey(),cursor,scanParams);
        List<Map.Entry<String,String>> list = result.getResult();
        Map<String,String> map = new HashMap<>();
        list.forEach(entry->{
            map.put(entry.getKey(),entry.getValue());
        });
        map.put("cursor",result.getCursor());
        res.setValue(map);
        res.setKey(postValue.getKey());
        res.setType(KeyType.HASH);
        res.setTtl(jedis.ttl(postValue.getKey()));
        return res;
    }

    @ApiOperation("添加zset值")
    @RequestMapping(value = "addZsetKey",method = RequestMethod.POST)
    public PostResponse addZsetKey(@RequestBody PostZSet postZSet){
        PostResponse res = new PostResponse();
        Jedis jedis = getJedis(postZSet);
        if(jedis == null){
            res.setValue("请先连接到redis数据库");
            return res;
        }
        jedis.zadd(postZSet.getKey(),postZSet.getScore(),postZSet.getValue());
        res.setValue(jedis.zrange(postZSet.getKey(),0,-1));
        res.setTtl(jedis.ttl(postZSet.getKey()));
        res.setType(KeyType.ZSET);
        jedis.close();
        return res;
    }
    @ApiOperation("删除zset值")
    @RequestMapping(value = "delZsetKey",method = RequestMethod.POST)
    public PostResponse delZsetKey(@RequestBody PostZSet postZSet){
        PostResponse res = new PostResponse();
        Jedis jedis = getJedis(postZSet);
        if(jedis == null){
            res.setValue("请先连接到redis数据库");
            return res;
        }
        jedis.zrem(postZSet.getKey(),postZSet.getValue());
        jedis.zadd(postZSet.getKey(),postZSet.getScore(),postZSet.getValue());
        res.setValue(jedis.zrange(postZSet.getKey(),0,-1));
        res.setTtl(jedis.ttl(postZSet.getKey()));
        res.setType(KeyType.ZSET);
        jedis.close();
        return res;
    }

    @ApiOperation("设置过期时间")
    @RequestMapping(value = "expireKey",method = RequestMethod.POST)
    public String expireKey(@RequestBody PostValue postValue){
        Jedis jedis = getJedis(postValue);
        if(jedis == null){
            return "请先连接到redis数据库";
        }
        jedis.expire(postValue.getKey(),postValue.getTtl());
        return "success";
    }

    private Jedis getJedis(BaseVo vo){
        JedisPool pool = redisCache.getConn(vo.getConnName());
        try(Jedis jedis = pool.getResource()) {
            jedis.select(vo.getDatabase());
            return jedis;
        }catch (Exception e){
            logger.error("请先连接到redis数据库");
        }
        return null;
    }

}
