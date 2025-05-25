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

        DegradeRule rule = new DegradeRule();
        rule.setResource("chat");
        rule.setGrade(RuleConstant.DEGRADE_GRADE_RT);        // 按响应时间熔断
        rule.setCount(100);                                  // 响应时间超过100ms
        rule.setTimeWindow(10);                              // 熔断时长10秒
        rule.setMinRequestAmount(1);                         // 最小请求数
        rule.setSlowRatioThreshold(0.5);                     // 慢调用比例50%

        rules.add(rule);
        DegradeRuleManager.loadRules(rules);
    }
}