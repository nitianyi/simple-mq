/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ztz.simple.mq.boot;

import java.net.URLDecoder;
import java.util.Base64;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.ztz.simple.mq.transport.CoreTransport;

@ComponentScan
@SpringBootApplication
public class Bootstrap {

	public static void main(String[] args) throws Exception {
		String s = "\u83b7\u53d6\u5fae\u4fe1\u767b\u5f55\u4fe1\u606f\u5931\u8d25\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5";
		
//		byte[] dest = Base64.getDecoder().decode();
		System.out.println(URLDecoder.decode(s, "UTF-8"));
		new CoreTransport(65456).startup();
//		SpringApplication.run(Bootstrap.class, args);
	}
}
