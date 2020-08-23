package first.project;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DistributeClient {

    private static String connectString = "ip Name:2181";
    private static int sessionTimeout = 2000;
    private ZooKeeper zk =null;
    private String parentNode = "/servers";


    public static void main(String[] args) throws Exception{
        DistributeClient client = new DistributeClient();

        // 1. 获取zk连接
        client.getConnect();

        // 2. 获取servers的子节点信息，从中获取服务器信息列表
        client.getServerList();

        // 3. 业务进程启动
        client.business();
    }

    public void getConnect() throws IOException{
        zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                try{
                    // 持续监听
                    getServerList();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void getServerList() throws Exception{
        List<String> children = zk.getChildren(parentNode,true);

        List<String> servers = new ArrayList<String>();

        for(String child:children){
            byte[] data = zk.getData(parentNode+"/"+child,false,null);
            servers.add(new String(data));
        }

        System.out.println(servers);
    }

    public void business() throws Exception{
        System.out.println("client is working ...");
        Thread.sleep(Long.MAX_VALUE);
    }

}
