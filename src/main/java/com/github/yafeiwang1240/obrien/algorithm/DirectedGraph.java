package com.github.yafeiwang1240.obrien.algorithm;

import com.github.yafeiwang1240.obrien.algorithm.node.ListNode;

import java.util.List;

/**
 * 有向图算法
 */
public class DirectedGraph {

    /**
     * 搜索路径默认最大1024, 防止堆栈溢出
     */
    private static final int MAX_PATH = 1024;

    /**
     * 当前路径是否成环
     * @param node
     * @return
     */
    public static boolean ring(ListNode node) {
        List<ListNode> next = node.getNext();
        return ring(node, next, 0);
    }

    private static boolean ring(ListNode node, List<ListNode> next, int number) {
        if (number > MAX_PATH) {
            return true;
        } else {
            number++;
        }
        if (next != null) {
            if (next.contains(node)) {
                return true;
            }
            for (ListNode n : next) {
                if (ring(node, n.getNext(), number)) {
                    return true;
                }
            }
        }
        return false;
    }

}
