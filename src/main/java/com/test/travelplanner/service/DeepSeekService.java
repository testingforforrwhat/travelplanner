package com.test.travelplanner.service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.service.SystemMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static dev.langchain4j.model.openai.OpenAiModelName.GPT_3_5_TURBO;

@Service  
public class DeepSeekService {  

    private final ChatLanguageModel chatModel;
    private final StreamingChatLanguageModel streamingChatModel; // 流式模型

    /**
     *
     * Spring Boot参数注入+Builder模式初始化AI模型服务实例
     *
     * 通过构造函数注入配置参数，然后基于（类似 OpenAI SDK 的）OpenAiChatModel / ChatLanguageModel
     * 构建 DeepSeekService 类AI服务的封装 Service，例如用于与 DeepSeek 的接口通信。
     *
     *
     * 如果你的配置较多，更符合 Spring 推荐风格的做法是用 @ConfigurationProperties 批量注入：
     *
     * ```java
     * @Component
     * @ConfigurationProperties(prefix = "deepseek")
     * @Data
     * public class DeepSeekProperties {
     *     private String apiKey;
     *     private String baseUrl;
     *     private String model;
     * }
     * ```
     *
     * 然后在 Service 里@Autowired 注入 DeepSeekProperties，更加优雅、可维护。
     *
     * @param apiKey
     * @param baseUrl
     * @param model
     */
    public DeepSeekService(  
            @Value("${deepseek.api-key}") String apiKey,  
            @Value("${deepseek.base-url}") String baseUrl,  
            @Value("${deepseek.model}") String model  
    ) {  
        this.chatModel = OpenAiChatModel.builder()  
                .apiKey(apiKey)  
                .baseUrl(baseUrl)  
                .modelName(model)  
                .build();

        // 创建流式模型实例
        this.streamingChatModel = OpenAiStreamingChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(model)
                /**
                 *
                 * "trace": "java.lang.IllegalArgumentException: Model 'deepseek-chat' is unknown to jtokkit\r\n\tat dev.langchain4j.internal.Exceptions.illegalArgument(Exceptions.java:6)\r\n\ta
                 *
                 * 你的错误来自 jtokkit 库，它不认识 deepseek-chat 模型，无法为该模型计算 token。
                 * 这个错误通常发生在 LangChain4j 尝试为你的模型计算 token 数量时，但它的词汇表库中没有 DeepSeek 模型的定义。
                 *
                 * fix: .tokenizer(new OpenAiTokenizer(GPT_3_5_TURBO)) // 可以选一个类似的模型:  GPT 3.5 的 tokenizer
                 *
                 */
                .tokenizer(new OpenAiTokenizer(GPT_3_5_TURBO)) // 用 GPT 3.5 的 tokenizer
                .build();
    }

    /**
     * 发送聊天消息获取回复
     */
    public String chat(String message) {  
        return chatModel.generate(message);  
    }

    /**
     * 流式返回聊天消息（异步）
     *
     * 在 LangChain4j 中，StreamingChatLanguageModel 的 generate 方法参数如下：
     * - 输入：ChatMessage 或 List<ChatMessage>
     * - 回调处理器：StreamingResponseHandler<AIMessage>（不是 <String>）
     *
     * https://docs.langchain4j.dev/tutorials/ai-services#streaming - The AI Service can stream response token-by-token
     * https://docs.langchain4j.dev/tutorials/response-streaming/ - LLMs generate text one token at a time, so many LLM providers offer a way to stream the response token-by-token instead of waiting for the entire text to be generated. This significantly improves the user experience, as the user does not need to wait an unknown amount of time and can start reading the response almost immediately.
     * https://docs.langchain4j.dev/tutorials/spring-boot-integration/ - Spring Boot starters help with creating and configuring language models, embedding models, embedding stores, and other core LangChain4j components through properties.
     *
     */
    public void chatStream(String message, StreamingResponseHandler<AiMessage> handler) {
        // 创建用户消息
        ChatMessage userMessage = UserMessage.from(message);
        streamingChatModel.generate(Collections.singletonList(userMessage), handler);
    }

}  