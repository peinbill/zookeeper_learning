package first.project;

import org.apache.zookeeper.*;

import java.io.IOException;

public class DistributeServer {

    private static String connectString = "ip name:2181";
    private static int sessionTimeout = 2000;
    private ZooKeeper zk=null;
    private String parentNode = "/servers";

    public static void main(String[] args) throws Exception{
        DistributeServer server = new DistributeServer();

        // 1. 获取zk连接
        server.getConnect();

        // 2. 利用zk连接注册服务器信息
        server.registServer(args[0]);

        //3 . 开启业务场景
        server.business(args[0]);

    }

    // 创建客户端连接
    private void getConnect() throws IOException {
        zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent watchedEvent) {

            }
        });
    }

    //  业务功能
    private void business(String hostname) throws Exception{
        System.out.println(hostname+" is working ...");
        Thread.sleep(Long.MAX_VALUE);
    }

    // 注册服务器
    private void registServer(String hostname) throws Exception{
        String create = zk.create(parentNode+"/server",hostname.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(hostname+"is online "+create);
    }


}
