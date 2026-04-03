package com.graduatedesign.apigateway.config;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean public TopicExchange dashboardExchange() { return new TopicExchange("dashboard.exchange"); }
    @Bean public Queue dashboardRequestsQueue() { return new Queue("dashboard.requests", true); }
    @Bean public Queue dashboardResultsQueue() { return new Queue("dashboard.results", true); }
    @Bean public Binding bindDashboardRequests() { return BindingBuilder.bind(dashboardRequestsQueue()).to(dashboardExchange()).with("dashboard.*.stats"); }
    @Bean public Binding bindDashboardResults() { return BindingBuilder.bind(dashboardResultsQueue()).to(dashboardExchange()).with("dashboard.results"); }
    @Bean public Jackson2JsonMessageConverter jsonMessageConverter() { return new Jackson2JsonMessageConverter(); }
    @Bean public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) { RabbitTemplate template = new RabbitTemplate(connectionFactory); template.setMessageConverter(jsonMessageConverter()); template.setReplyTimeout(30000); return template; }
}