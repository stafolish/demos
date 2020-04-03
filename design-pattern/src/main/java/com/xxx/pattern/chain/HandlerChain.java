package com.xxx.pattern.chain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HandlerChain {

    //规则过滤器列表，实现Handler接口的过滤器将真正执行对事件的处理
    @Autowired
    private List<Handler> handlers = new ArrayList<>();

    private ThreadLocal<Integer> indexLocal = new ThreadLocal();

    //向责任链中加入过滤器（单个）
    public HandlerChain addFilter(Handler filter) {
        this.handlers.add(filter);
        return this;
    }

    //向责任链中加入过滤器（多个）
    public HandlerChain addFilters(List<Handler> filters) {
        this.handlers.addAll(filters);
        return this;
    }

    //处理事件（alarm）从HandlerChain中获取处理器，进行处理，处理完成之后过滤器会再调用该方法，
    //继续执行下一个fhandler.这就需要在每个Handler接口的实现类中最后一句需要回调FilterChain的doFilter方法。
    public void doFilter(Alarm alarm) {
        Integer index = indexLocal.get();
        if (index == null) {
            index = 0;
            indexLocal.set(index);
        }
        if (index == handlers.size()) {
            return;
        }
        Handler handler = handlers.get(index);
        indexLocal.set(index++);
        handler.handle(alarm, this);
    }
}
