基于DAG图的任务流引擎

GraphScheduleEngin通过消息队列的机制，提供了任务配置、任务启动、任务依赖分析、任务分配的方案；开发任务worker时只需要关注任务本身的计算、运行，通过订阅MessageQueue的消息来获取分配的任务，并在任务完成时通过MessageQueue将任务状态发送至GraphScheduleEngine；GraphScheduleEngine在收到一个任务状态后判断是否开始下一轮的任务分配。

GraphScheduleEngine使用：
1、	运行apache-activemq作为消息中间件，并创建消息队列："ReduceQueue"
2、	启动hbase作为任务配置信息和任务运行日志的数据仓库
3、	启动GraphScheduleEngine
4、	配置任务，创建一个名为任务worker_name的消息队列
5、	在任务worker中订阅"ReduceQueue"消息，并将任务完成状态发布至worker_name

GraphScheduleEngine是我本人为了协调诸多Java,python,shell编写的不同数据挖掘任务、进行任务调度、监测的项目。
各项功能、代码的健壮性，需要有兴趣的同行一同来完善、发展这个项目，欢迎与本人
邮箱：bchengzhou@163.com 
blog: http://blog.csdn.net/zbc1090549839
进行交流讨论。
