package io.ztz.simple.mq.client;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import io.ztz.simple.mq.io.serialize.SerializeProtocolEnum;
import io.ztz.simple.mq.io.serialize.Serializer;
import io.ztz.simple.mq.io.serialize.SerializerFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ServiceLoader implements ApplicationContextAware, InitializingBean, DisposableBean {

	private ApplicationContext context;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private SerializerFactory serializerFactory;
		
	private List<ClientEngine> engines = Lists.newArrayList();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Resource resource = context.getResource("classpath:simple-mq.properties");
		if (!resource.exists()) {
			throw new Exception("simple-mq.properties file not exists");
		}
		
		String serverStr = env.getProperty("broker.hosts");
		log.info("The hosts config ->{}", serverStr);
		if (Strings.isNullOrEmpty(serverStr)) {
			throw new Exception("broker.hosts not config yet");
		}
		
		String serializerConfig = env.getProperty("msg.serializer");
		Serializer serializer = serializerFactory.getSerializer(SerializeProtocolEnum.getByName(serializerConfig));
		
		Arrays.stream(serverStr.split(",")).forEach(h -> {
			try {
				String[] hosts = h.split(":");
				if (hosts.length != 2 || !hosts[1].matches("\\d{4,5}")) {
					throw new RuntimeException("invalid host config, eg like host:port");
				}
				
				connectBrokers(hosts[0], Integer.valueOf(hosts[1]), serializer);
			} catch (Exception e) {
				log.error("error occurs when connecting brokers", e);
				throw new RuntimeException(e);
			}
		});
	}
	
	void connectBrokers(String host, int port, Serializer serializer) throws Exception {
		ClientEngine engine = new ClientTransport(host, port, serializer);
		engine.start();
		engines.add(engine);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}

	@Override
	public void destroy() throws Exception {
		engines.forEach(n -> {
			try {
				n.close();
			} catch (Exception e) {
				log.error("error when close the engines", e);
			}
		});
	}
	
	@PostConstruct
	private void init() {
		
	}

}
