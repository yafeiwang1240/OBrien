package com.github.yafeiwang1240.obrien.algorithm;

import com.github.yafeiwang1240.obrien.algorithm.node.ListNode;
import com.github.yafeiwang1240.obrien.algorithm.node.ListNodeObserverAndSubject;
import com.github.yafeiwang1240.obrien.lang.Lists;

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
        if (number > MAX_PATH) return true;
        number++;
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

    /**
     *  所有节点成环判断（复杂度 > O(n²)）
     * @param list
     * @return
     */
    public static boolean ring(List<ListNodeObserverAndSubject> list) {
        if (list == null || list.size() <= 0) return false;
        List<ListNodeObserverAndSubject> has = Lists.newArrayList(list.size());
        boolean exit = false;
        while (!exit) {
            exit = true;
            for (ListNodeObserverAndSubject node : list) {
                // 上游已完成，且此节点未执行
                if (node.canEmit() && !node.isEmit()) {
                    exit = false;
                    if (!node.emit(true)) {
                        return true;
                    }
                    has.add(node);
                }
            }
        }
        if (has.size() < list.size()) {
            // 成环相互依赖，无法执行
            return true;
        }
        return false;
    }

}
