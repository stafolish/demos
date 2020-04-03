package com.xxx.pattern.chain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class HandlerChain {

    //规则过滤器列表，实现Handler接口的过滤器将真正执行对事件的处理
    @Autowired
    private List<Handler> handlers = new ArrayList<>();

    //过滤器列表的索引
    private AtomicInteger index = new AtomicInteger();

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
        int i = index.get();
        if (i == handlers.size()) {
            return;
        }
        Handler handler = handlers.get(i);
        index.incrementAndGet();
        handler.handle(alarm, this);
    }
}
