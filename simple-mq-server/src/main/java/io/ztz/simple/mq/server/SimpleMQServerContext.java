package io.ztz.simple.mq.server;

import static com.google.common.collect.Maps.newConcurrentMap;

import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;

import io.ztz.simple.mq.api.enums.MsgTypeEnum;
import io.ztz.simple.mq.io.serialize.SerializeProtocolEnum;
import io.ztz.simple.mq.io.serialize.Serializer;
import io.ztz.simple.mq.server.store.StoreEngine;
import io.ztz.simple.mq.server.template.RequestProcessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
//@EnableConfigurationProperties(ServerConfig.class)
public class SimpleMQServerContext implements ApplicationContextAware {

	@Autowired
	private ServerConfig config;
	
	@Autowired
	private Environment env;
	
	private ApplicationContext context;
	
	private Map<String, RequestProcessor> processorCache;
	
	private Map<String, StoreEngine> engineCache;
	
	private Map<String, Serializer> serializers;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}
	
	@PostConstruct
	private void initBeans() {
		processorCache = context.getBeansOfType(RequestProcessor.class);
		log.debug("init processorCache->{}", JSON.toJSONString(processorCache));
		
		engineCache = context.getBeansOfType(StoreEngine.class);
		log.debug("init engineCache->{}", JSON.toJSONString(engineCache));
		
		serializers = context.getBeansOfType(Serializer.class);
		log.debug("init serializers->{}", JSON.toJSONString(serializers));
	}
	
	public RequestProcessor chooseRequestProcessor(MsgTypeEnum type) {
		String key = type.name().toLowerCase().concat("ReqProcessorTemplate");
		log.debug("The request key is ->{}", key);
		return processorCache.get(key);
	}
	
	public String getMedia() {
		return env.getProperty("server.msg.store.media");
	}
	
	
	public StoreEngine chooseStoreEngine() {
		String media = config.getMedia();
		log.debug("The config media is {}", media);
		StoreEngine engine = engineCache.get(media.concat("Engine"));
		return engine;
	}
	
	public Serializer getSerializer(SerializeProtocolEnum protocol) {
		SerializeProtocolEnum defaultEnum = Optional.ofNullable(protocol).orElse(SerializeProtocolEnum.Protostuff);
		return serializers.get(defaultEnum.getServiceKey());
	}
}
