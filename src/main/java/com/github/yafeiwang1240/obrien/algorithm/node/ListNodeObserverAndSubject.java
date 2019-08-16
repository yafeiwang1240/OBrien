package com.github.yafeiwang1240.obrien.algorithm.node;

import com.github.yafeiwang1240.obrien.algorithm.observer.Observer;
import com.github.yafeiwang1240.obrien.algorithm.observer.Subject;
import com.github.yafeiwang1240.obrien.lang.Lists;

import java.util.List;

public class ListNodeObserverAndSubject implements Observer<Boolean>, Subject<Boolean> {

    private List<Observer> list;
    private List<Subject> up;
    private int size;
    private boolean emit;
    private String name;

    public ListNodeObserverAndSubject() {
        list = Lists.newArrayList();
        up = Lists.newArrayList();
    }

    public ListNodeObserverAndSubject(String name) {
        list = Lists.newArrayList();
        up = Lists.newArrayList();
        this.name = name;
    }

    public boolean canEmit() {
        return size >= up.size();
    }

    public boolean isEmit() {
        return emit;
    }

    public boolean emit(Boolean period) {
        emit = true;
        return notifyObserver(period);
    }

    @Override
    public boolean update(Boolean period) {
        // 已经发出消息
        if (emit) {
            return false;
        }
        size++;
        if (size > up.size()) {
            return false;
        }
        return true;
    }

    public List<Subject> getSubject() {
        return up;
    }

    public boolean addSubject(Subject subject) {
        return up.add(subject);
    }

    public boolean deleteSubject(Subject subject) {
        return up.remove(subject);
    }

    @Override
    public List<Observer> getObserver() {
        return list;
    }

    @Override
    public boolean addObserver(Observer observer) {
        return list.add(observer);
    }

    @Override
    public boolean deleteObserver(Observer observer) {
        return list.remove(observer);
    }
}
