# redis 命令学习总结

#### connection命令组

```text
1.ECHO  message 回显输入的字符串
2.PING  ping服务器
3.QUIT  关闭连接退出
4.SELECT index 选择新的数据库
```


#### keys命令组
```$xslt
1.DEL key[key ...] 删除指定的key(一个或多个)
2.DUMP key 导出key的值
3.EXISTS key[key ...] 查询一个key是否存在
4.EXPIRE key seconds 设置一个key的过期秒数
5.keys pattern 查找所有匹配给定的模式的键
6.MOVE key db 移动一个key到另一个数据库
7.PERSIST key 设置key的过期时间
8.RENAME key newkey 将一个key重命名
9.RENAMENX key newkey 重命名一个key,新的key必须是不存在的
10.TTL key 获取key的有效时间（单位：秒）
11. TYPE key 获取key的存储类型
```


### strings命令组
```
1.APPEND key value  追加一个值到key上(若key不存在，则创建)
2.DECR key 整数原子减1（若值不为整数，则报错）
3.DECRBY key decrement  原子减指定的整数
4.GET key 返回指定key的value
5.GETRANGE key start end 获取存储在key上的值的一个字符串
6.GETSET key value 设置一个key的value，并获取设置前的值
7.INCR key 执行原子加1操作
8.INCRBY key increment 执行原子增加一个整数
9.INCRBYFLOAT key increment 执行原子增加一个浮点数
10.MGET key[key ...] 获取所有key的值
11.MSET key value[key value ...] 设置多个key的value
12. SETNX key value 当键不存在时设置值
13.STRLEN key 获取指定key值的长度
```



### list命令组
```$xslt

1.BLPOP key [key ...] timeout 删除并获得列表中的第一个元素，或阻塞，直到有一个可用
2.BRPOP key [key ...] timeout 删除并获得列表中的第一个元素，或阻塞，直到有一个可用
3.BRPOPLPUSH source destination timeout 弹出一个列表的值，将它推入另一个列表并返回它，或阻塞直到有一个可用
4.LINDEX key index  获取一个元素，通过其索引列表
5.LLEN key 获取list的长度
6.LPOP RPOP key 从队列弹出一个元素
7.LPUSH RPUSH key value [value ...] 向队列里插入元素
8.LRANGE key start stop 从列表中获取指定返回的元素
9.LPUSHX RPUSHX key value [value ...] 仅当队列存在时执行push操作
10.RPOPLPUSH source destination 删除列表中的最后一个元素，将其追加到另一个列表
11.LTRIM key start stop 截取制定范围内的清单
12.LREM key count value 从列表中删除指定数目的元素（若为0删除全部，若为正从左侧计数，若为负数从右侧计数）
```

### set命令组
```$xslt
1.SADD key mumber[mumber...] 添加一个或多个集合到集合当中
2.SCARD key 获取集合里面的元素数量
3.SMEMBERS key 获取集合里面的所有元素
4.SPOP key [count] 删除并获取集合里的元素
```



### hash命令组
```$xslt

1.HDEL key field[field...] 删除一个或多个hash的field
2.HEXISTS key field 判断field是否存在于hash中
3.HGET key field 获取hash中field的值
4.HGETALL key 从hash中读取全部的域和值
5.HKEYS key 获取hash的所有字段
6.HLEN key 获取hash里所有字段的数量
7.HMGET key field[field] 获取hash里面指定的字段
8.HMSET key field [field value] 设置hash字段值
9.HSETNX key field value 设置hash字段的一个值（只有当这个字段不存在时有效）
10.HSTRLEN key field 获取hash里面指定field的长度的
11.HVALS 获得hash的所有值
```

###sorted sets命令组
```
1.ZADD key score member [score member...] 添加到有序set的一个或多个成员
2.ZCARD key 获取一个排序集合中的成员的数量
3.ZCOUNT key min max 返回分数范围内的所有成员数量
4.ZINCRBY key increment member 给一个成员增加排分
5.ZRANK key member 确定排序集合成员的索引
6.ZSCORE key member 获取成员在排序设置相关的比分
7.ZREM key member[member ...] 从排序的集合中删除一个或多个成员
8.ZRANGE key start stop[WITHSCORES] 根据指定的index返回，返回指定sortedset的成员列表
```
哈哈哈 github dev
