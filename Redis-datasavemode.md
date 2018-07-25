# Redis数据持久化的两种方式

## 1、RDB(快照)持久化方式


Redis 是一种内存数据库，它将数据存储在内存中，所以如果不将数据保存到硬盘中，
那么一旦 Redis 进程退出，保存在内存中的数据将会丢失。为此，Redis 提供了两
种不同的持久化方法来将数据存储到硬盘里面。一种方法叫做快照（snapshotting），
它可以将存在于某一时刻的所有数据写入硬盘里面。另一种方法叫 AOF（append-only file），
它会在执行写命令时，将被执行的写命令复制到硬盘里面。这种两持久化方法既可以同时使用，
也可以单独使用，当然如果 Redis 单纯只作为缓存系统使用的话，也可以两种持久化方法都不使用。
具体选择哪种持久化方法取决于用户的应用场景。快照持久化方法是 Redis 默认开启的持久化
方法，本文介绍快照持久化方法，AOF 方法将在另一篇文章中介绍。


## RDB 文件
Redis 将某一时刻的快照保存成一种称为 RDB 格式的文件中。RDB 文件是一个经过压缩的二进制文件，通过该文件，Redis 可以将内存中的数据恢复成某一时刻的状态。


## SAVE 命令
Redis 提供了两个命令用来生成 RDB 文件，一个是 SAVE，一个是 BGSAVE。
SAVE 命令会阻塞 Redis 服务器进程，走到 RDB 文件创建完毕为止，在 Redis 服务器进程阻塞期间，Redis 不能处理任何命令请求。

```text
redis 127.0.0.1:6379> save
OK
```


在生产环境，我们一般不会直接使用 SAVE 命令，原因是由于它会阻塞 Redis 进程。但是，如果机器已没有足够的内存去执行 BGSAVE 命令，或者即使等待持久化操作完毕也无所谓，我们也可以使用 SAVE 命令来生成 RDB 文件。

BGSAVE 命令
BGSAVE 命令会派生出一个子进程，然后由子进程创建 RDB 文件，因此，BGSAVE 命令不会阻塞 Redis 服务器进程。

redis 127.0.0.1:6379> bgsave
Background saving started
可以使用 LASTSAVE 命令来检查保存 RDB 文件的操作是否执行成功。

## 自动保存 RDB 文件
除了手动执行 SAVE 和 BGSAVE 命令来生成 RDB 文件外，Redis 还提供了自动保存 RDB 文件的功能。由于 BGSAVE 命令可以在不阻塞服务器进程的情况下执行，所以 Redis 允许用户通过设置配置来让 Redis 服务器每隔一段时间自动执行一次 BGSAVE 命令。
下面是 Redis 配置文件 redis.conf 中有关自动保存 RDB 文件的有关配置内容：

```
save 900 1
save 300 10
save 60 10000
```

上面配置的含义是，Redis 服务器只要满足以下三个条件中的任意一个， BGSAVE 命令就会被执行：

Redis 服务器在 900 秒之内，对数据库进行了至少一次修改
Redis 服务器在 300 秒之内，对数据库进行了至少 10 次修改
Redis 服务器在 60 秒之内，对数据库进行了至少 10000 次修改
涉及 RDB 文件的配置选项还包括：

```text
dbfilename dump.rdb
dir ./
stop-writes-on-bgsave-error yes
rdbcompression yes
rdbchecksum yes
dbfilename：配置 RDB 文件名称
dir：配置 RDB 文件存放的路径
stop-writes-on-bgsave-error：当生成 RDB 文件出错时是否继续处理 Redis 写命令，默认为不处理
rdbcompression：是否对 RDB 文件进行压缩
rdbchecksum：是否对 RDB 文件进行校验和校验
```


## 快照持久化的优点
快照持久化的方法采用创建一个子进程的方法来将 Redis 的内存数据保存到硬盘中，因此，它并不会对 Redis 服务器性能造成多大的影响，这可以说是快照持久化方法最大的一个优点。
快照持久化使用的 RDB 文件是一种经过压缩的二进制文件，可以方便地在网络中传输与保存。通过恰当的配置，可以让我们方便快捷地将 Redis 内存数据恢复到某一历史状态。这对于提高数据的安全性，应对服务器宕机等意外的发生很有帮助。

## 快照持久化的缺点
尽管快照持久化允许 Redis 恢复到快照文件的状态，但如果 RDB 文件生成后，Redis 服务器继续处理了写命令导致 Redis 内存数据有更新，这时恰好 Redis 崩溃了而来不及保存新的 RDB 文件，那么 Redis 将会丢失这部分新的数据。也就是说，如果系统真的发生崩溃，那么我们将会丢失最近一次生成 RDB 文件之后更改的所有数据。因此，快照持久化方法只适用于那些即使丢失一部分数据也不会造成问题的应用场景。
另外，快照持久化方法需要调用fork()方法创建子进程。当 Redis 内存的数据量较大时，创建子进程和生成 RDB 文件得占用较多的系统资源和处理时间，这会对 Redis 正常处理客户端命令的性能造成较大的影响。
当然，如果我们可以妥善处理快照持久化方法可能带来的数据丢失，那么快照持久化仍然是一个不错的选择。


## 2.AOF（append only file）模式


除了快照持久化外，Redis 还提供了 AOF（Append Only File）持久化功能。与快照持久化通过直接保存 Redis 的键值对数据不同，AOF 持久化是通过保存 Redis 执行的写命令来记录 Redis 的内存数据。


## AOF 持久化的原理
理论上说，只要我们保存了所有可能修改 Redis 内存数据的命令（也就是写命令），那么根据这些保存的写命令，我们可以重新恢复 Redis 的内存状态。AOF 持久化正是利用这个原理来实现数据的持久化与数据的恢复的。 


## AOF 配置
为了打开 AOF 持久化的功能，我们只需要将 redis.conf 配置文件中的appendonly配置选项设置为yes即可。涉及 AOF 持久化的几个常用配置如下所示：


```text
appendonly yes
appendfilename "appendonly.aof"
appendfsync everysec


appendonly：是否打开 AOF 持久化功能
appendfilename：AOF 文件名称
appendfsync：同步频率，可选配置如表1所示
```


表 1：appendfsync 选项及同步频率

```text
选项	同步频率
always	每个 Redis 命令都要同步写入硬盘。这样会严重降低 Redis 的性能
everysec	每秒执行一次同步，显式地将多个写命令同步到硬盘
no	让操作系统来决定应该何时进行同步
```

AOF 文件的生成过程具体包括命令追加，文件写入，文件同步三个步骤。 
Redis 打开 AOF 持久化功能后，Redis 在执行完一个写命令后，都会将执行的写命令追回到 Redis 内部的缓冲区的末尾。这个过程是命令的追加过程。 
接下来，缓冲区的写命令会被写入到 AOF 文件，这一过程是文件写入过程。对于操作系统来说，调用write函数并不会立刻将数据写入到硬盘，为了将数据真正写入硬盘，还需要调用fsync函数，调用fsync函数即是文件同步的过程。只有经过文件同步过程，AOF 文件才在硬盘中真正保存了 Redis 的写命令。appendfsync 配置选项正是用来配置将写命令同步到文件的频率的，各个选项的值的含义如表 1 所示。


## AOF 文件
前面我们提到，AOF 持久化是通过 AOF 文件来实现的，那么 AOF 里面是如何保存 Redis 的写命令的呢？ 
假设我们对一个空的 Redis 数据库执行以下命令：

```text
set msg hello
set site leehao.me
```

执行后，Redis 数据库保存了键 msg 与 site 的数据。打开保存的 AOF 文件appendonly.aof（AOF 文件的路径和名称由 redis.conf 配置，具体可以参考上面 AOF 配置的描述），可以看到其内容为：


```text
*2
$6
SELECT
$1
0
*3
$3
set
$3
msg
$5
hello
*3
$3
set
$4
site
$9
leehao.me

```
appendonly.aof以 Redis 协议格式 RESP 来保存写命令。由于 RESP 协议中包含了换行符，所以上面展示的 AOF 文件时遇到换行符时进行了换行。在 AOF 文件里面，除了用于指定数据库的 SELECT 命令是自动添加的之外，其他都是我们通过客户端发送的命令。 
appendonly.aof保存的命令会在 Redis 下次重启时使用来还原 Redis 数据库。



## 重写 / 压缩 AOF 文件
由于 Redis 会不断地将被执行的命令记录到 AOF 文件里面，所以随着 Redis 不断运行，AOF 文件的体积会越来越大。另外，如果 AOF 文件的体积很大，那么还原操作所需要的时间也会非常地长。 
为了解决 AOF 文件越来越大的问题，用户可以向 Redis 发送 BGREWRITEAOF 命令，这个命令会移除 AOF 文件中冗余的命令来重写 AOF 文件，使 AOF 文件的体积变得尽可能地小。 
BGREWRITEAOF 的工作原理和快照持久化命令 BGSAVE 的工作原理类似，Redis 会创建一个子进程来负责对 AOF 文件进行重写。 
值得注意的是，进行 AOF 文件重写时，如果原来的 AOF 文件体积已经非常大，那么重写 AOF 并删除旧 AOF 文件的过程将会对 Redis 的性能造成较大的影响。 
为此，Redis 提供auto-aof-rewrite-percentage和auto-aof-rewrite-min-size两个配置选项来对 AOF 重写进行配置。auto-aof-rewrite-percentage和auto-aof-rewrite-min-size两个配置选项的含义可以参考 redis.conf 配置中的详细说明，具体来说，auto-aof-rewrite-percentage配置当 AOF 文件需要比旧 AOF 文件增大多少时才进行 AOF 重写，而auto-aof-rewrite-min-size则配置当 AOF 文件需要达到多大体积时才进行 AOF 重写。只有这两个配置的条件都达到时，才会进行 AOF 重写。

## AOF 的优点
AOF 持久化的方法提供了多种的同步频率，即使使用默认的同步频率每秒同步一次，Redis 最多也就丢失 1 秒的数据而已。
AOF 文件使用 Redis 命令追加的形式来构造，因此，即使 Redis 只能向 AOF 文件写入命令的片断，使用 redis-check-aof 工具也很容易修正 AOF 文件。
AOF 文件的格式可读性较强，这也为使用者提供了更灵活的处理方式。例如，如果我们不小心错用了 FLUSHALL 命令，在重写还没进行时，我们可以手工将最后的 FLUSHALL 命令去掉，然后再使用 AOF 来恢复数据。
## AOF 的缺点
对于具有相同数据的的 Redis，AOF 文件通常会比 RDF 文件体积更大。
虽然 AOF 提供了多种同步的频率，默认情况下，每秒同步一次的频率也具有较高的性能。但在 Redis 的负载较高时，RDB 比 AOF 具好更好的性能保证。
RDB 使用快照的形式来持久化整个 Redis 数据，而 AOF 只是将每次执行的命令追加到 AOF 文件中，因此从理论上说，RDB 比 AOF 方式更健壮。官方文档也指出，AOF 的确也存在一些 BUG，这些 BUG 在 RDB 没有存在。
