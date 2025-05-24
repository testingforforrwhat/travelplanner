package com.test.travelplanner.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;


/**
 *
 * 消息队列（Message Queue）作为一种进程间或线程间的异步通信机制，在现代软件系统中发挥着至关重要的作用。它的基本原理是生产者在生成消息后，将消息存储在队列中，消费者则在适当时机从队列中获取并处理消息。这样的机制使得消息的发送者与接收者无需同时在线或直接交互，从而实现了系统的异步通信和松耦合。
 *
 * 通过消息队列，服务之间的依赖性大大降低，系统的扩展性和可靠性也随之增强。例如，在高并发场景下，消息队列能够缓冲流量高峰，防止业务系统的过载。同时，若某个服务出现故障，未被处理的消息仍然可以保留在队列中，待系统恢复后继续处理，进一步提高系统的容错能力。
 *
 * 目前，Apache ActiveMQ和RabbitMQ等。这些消息中间件不仅提供了丰富的功能，还支持多种通信协议和分布式部署方案，方便开发者根据具体需求选择合适的解决方案。综上所述，消息队列作为现代分布式架构的重要基础设施，对于提高系统的解耦性、稳定性与可扩展性具有不可替代的价值。
 *
 */
@Configuration
public class KafkaConfig {

    public static final String ORDER_CREATED_TOPIC = "order-created";
    public static final String ORDER_UPDATED_TOPIC = "order-updated";
    public static final String ORDER_CANCELLED_TOPIC = "order-cancelled";

    @Bean
    public NewTopic orderCreatedTopic() {
        return TopicBuilder.name(ORDER_CREATED_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic orderUpdatedTopic() {
        return TopicBuilder.name(ORDER_UPDATED_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic orderCancelledTopic() {
        return TopicBuilder.name(ORDER_CANCELLED_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}