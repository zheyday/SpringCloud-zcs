package zcs.seckill.utils;


import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 服务注册
 */
//@Component
public class ZkUtils {
    private String registryAddress = "192.168.186.222:2181";
    private final String REGISTRY_PATH = "/cloudOne";
    private final int SESSION_TIMEOUT = 5000;

    private ZooKeeper zooKeeper;

    private Map<String, Lock> lockMap = new HashMap<>();

    public ZkUtils() {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            zooKeeper = new ZooKeeper(registryAddress, SESSION_TIMEOUT, watchedEvent -> {
                if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    latch.countDown();
                }
            });
//            等待连接成功
            latch.await();
            System.out.println("连接ZooKeeper成功");
//            创建根结点
            if (zooKeeper.exists(REGISTRY_PATH, false) == null)
                zooKeeper.create(REGISTRY_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            create("/lock1", CreateMode.PERSISTENT);
            create("/lock2", CreateMode.PERSISTENT);
            create("/lock3", CreateMode.PERSISTENT);
            create("/lock4", CreateMode.PERSISTENT);
            lockMap.put("/lock1", new ReentrantLock());
            lockMap.put("/lock2", new ReentrantLock());
            lockMap.put("/lock3", new ReentrantLock());
            lockMap.put("/lock4", new ReentrantLock());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String lock(String parentName) {
        String parentFull = REGISTRY_PATH + parentName;
        String pathFull = parentFull + "/seq";
        try {
            pathFull = createSeq(pathFull, CreateMode.EPHEMERAL_SEQUENTIAL);
            //   创建临时节点
            List<String> children = zooKeeper.getChildren(parentFull, null);
            Collections.sort(children);
            int i = Collections.binarySearch(children, pathFull) - 1;
            if (i >= 0 && !children.get(i).equals(pathFull)) {
                CountDownLatch countDownLatch = new CountDownLatch(1);
                String pre = parentFull + "/" + children.get(i);
                Lock lock = lockMap.get(parentName);
                lock.lock();
                if (zooKeeper.exists(pre, true) != null) {
                    zooKeeper.addWatch(pre, watchedEvent -> {
                        if (Watcher.Event.EventType.NodeDeleted == watchedEvent.getType()) {
                            try {
                                countDownLatch.countDown();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, AddWatchMode.PERSISTENT);
                    lock.unlock();
                    countDownLatch.await();
                } else
                    lock.unlock();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parentFull + "/" + pathFull;
    }

    public void unLock(String lockName, String nodeName) {
        Lock lock = lockMap.get(lockName);
        try {
            lock.lock();
            zooKeeper.delete(nodeName, -1);
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public String create(String childName, CreateMode createMode) throws Exception {
        String nodeName = REGISTRY_PATH + childName;
        if (zooKeeper.exists(nodeName, false) == null) {
            String path = zooKeeper.create(nodeName, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
            return path.substring(path.lastIndexOf("/") + 1);
        }
        return null;
    }

    public String createSeq(String childName, CreateMode createMode) throws Exception {
        String path = zooKeeper.create(childName, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
        return path.substring(path.lastIndexOf("/") + 1);
    }


}
