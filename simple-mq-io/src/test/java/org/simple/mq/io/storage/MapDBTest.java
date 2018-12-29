package org.simple.mq.io.storage;

import org.junit.Test;
import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

public class MapDBTest {

	DB db = DBMaker.memoryDirectDB().allocateStartSize(10240000).make();
	
	@Test
	public void test() {
		BTreeMap<String, Object> map = db.treeMap("topic").keySerializer(Serializer.STRING).valueSerializer(Serializer.JAVA).create();
		
		
	}

}
