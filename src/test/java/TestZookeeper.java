import oracle.jrockit.jfr.StringConstantPool;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TestZookeeper {

    private static String connectString = "ip name:2181";

    private static int sessionTimeout =2000;

    private ZooKeeper zkClient = null;

    @Before
    public void init() throws Exception{
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            // 回调函数
            public void process(WatchedEvent watchedEvent) {
                System.out.println(watchedEvent.getType()+"--"+watchedEvent.getPath());

                try{
                    List<String> childrens = zkClient.getChildren("/",true);
                    for(String children:childrens){
                        System.out.println(children);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    // 新增测试
    @Test
    public void create() throws Exception{
        String nodeCreated = zkClient.create("/test","test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    //获取子节点并监听变化
    @Test
    public void getDataAndWatch() throws Exception{
        List<String> childrens = zkClient.getChildren("/",true);
        for(String children:childrens){
            System.out.println(children);
        }
        // 因为该监听进程要持续，不能中断
        Thread.sleep(Long.MAX_VALUE);
    }

    @Test
    public void exist() throws Exception{
        Stat stat = zkClient.exists("/test",false);
        System.out.println(stat==null?"not exist":"exist");
    }


}
