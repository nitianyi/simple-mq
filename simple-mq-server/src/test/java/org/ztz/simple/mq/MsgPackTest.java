package org.ztz.simple.mq;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;
import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import com.google.common.collect.Lists;

import javassist.expr.NewArray;

public class MsgPackTest {

	@Test
	public void test() {
		List<String> cs = Lists.newArrayList();
		cs.add("One");
		cs.add("Two");
		cs.add("Three");
		
		MessagePack mp = new MessagePack();
		try {
			byte[] bs = mp.write(cs);
			List<String> cc = mp.read(bs, Templates.tList(Templates.TString));
			cc.stream().forEach(c -> {
				
				System.out.println(c);
			});
			assertEquals(cs, cc);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String regDate = format.format(1388246400000l);
        System.out.println(regDate);
        System.out.println(new Date(1388246400000l));
	}
	
	@Test
	public void testMessageBufferPacker() {
		
	}

}
