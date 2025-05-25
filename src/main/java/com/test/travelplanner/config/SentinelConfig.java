package com.test.travelplanner.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 限流和熔断规则可通过yml配置，但生产建议用dashboard集中推送。
 *
 */
@Configuration
public class SentinelConfig {

    // 限流规则
    @PostConstruct
    public void initFlowRules() {
        List<FlowRule> rules = new ArrayList<>();
        
        FlowRule rule = new FlowRule();
        rule.setResource("chat");           // 资源名
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS); // 按QPS限流
        rule.setCount(1);                      // 每秒最多10个请求
        rule.setStrategy(RuleConstant.STRATEGY_DIRECT); // 直接拒绝
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
        
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

    // 熔断规则
    @PostConstruct
    public void initDegradeRules() {
        List<DegradeRule> rules = new ArrayList<>();

        // 至少需要 1个请求 才开始统计
        // 当响应时间超过 100ms 的请求占比达到 50% 时触发熔断
        // 熔断后 10秒 内所有请求直接走降级逻辑
        // 10秒后进入半开状态，允许少量请求试探
        DegradeRule rule = new DegradeRule();
        rule.setResource("chat");
        rule.setGrade(RuleConstant.DEGRADE_GRADE_RT);           // 按响应时间统计熔断
        rule.setCount(100);                                     // RT阈值，单位ms（100毫秒）ms; 正整数, > 0;
        rule.setTimeWindow(10);                                 // 熔断持续10秒
        rule.setMinRequestAmount(1);                            // 触发熔断最小请求数（建议生产设大一点）
        rule.setSlowRatioThreshold(0.5);                        // 慢调用比＞50%触发熔断

        rules.add(rule);
        DegradeRuleManager.loadRules(rules);
    }
}