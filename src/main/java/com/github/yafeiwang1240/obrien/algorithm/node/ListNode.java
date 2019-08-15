package com.github.yafeiwang1240.obrien.algorithm.node;

import java.util.ArrayList;
import java.util.List;

/**
 * list node
 */
public class ListNode {
    private String name;
    private List<ListNode> next;

    public ListNode(String name) {
        this.name = name;
        if (this.name == null || this.name.length() <= 0) {
            throw new IllegalArgumentException("名称不能为空");
        }
    }

    public ListNode(String name, List<ListNode> next) {
        this.name = name;
        if (this.name == null || this.name.length() <= 0) {
            throw new IllegalArgumentException("名称不能为空");
        }
        this.next = next;
    }

    public String getName() {
        return name;
    }

    public List<ListNode> getNext() {
        return next;
    }

    public void setNext(List<ListNode> next) {
        this.next = next;
    }

    public void addNext(ListNode node) {
        if (next == null) {
            next = new ArrayList<>();
        }
        next.add(node);
    }

    @Override
    public int hashCode() {
        int result = name == null ? 0 : name.hashCode();
        return result * 31 + 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof ListNode)) return false;
        ListNode that = (ListNode) obj;
        return this.name.equals(that);
    }
}
