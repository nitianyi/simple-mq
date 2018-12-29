package org.simple.mq.api.tools;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * 
 * ClassName:IdGenerator <br/>
 * Function: 生产主键，生成策略为：时间戳 + 机器id + 自增id. <br/>
 * Date:     2016年4月11日 下午4:36:29 <br/>
 * @author   Tony
 * @version  1.0
 * @since    JDK 1.8
 */
@SuppressWarnings("serial")
public class IdGenerator implements Serializable {

	/**
	 * 机器id 全局初始化一次
	 */
	private static final int machineId;

	/**
	 * 自增id生成器，可扩展为其他策略
	 */
	private volatile static AtomicInteger inc;
	
	private static final String SEPERATOR = ".";
	
	static {
		inc = new AtomicInteger((int) (System.currentTimeMillis() / 1000));
		machineId = computeMachineId();
	}

	/**
	 * The default constructor
	 */
	private IdGenerator() {}

	/**
	 * 获取本机的信息：网卡信息 + 进程信息 + 类加载器信息
	 * 
	 * @return 机器id
	 */
	public static int computeMachineId() {
		// 获取机器id
		final int machineBlock;
		try {
			Enumeration<NetworkInterface> ni = NetworkInterface
					.getNetworkInterfaces();
			StringBuilder str = new StringBuilder();
			while (ni.hasMoreElements()) {
				str.append(ni.nextElement().toString());
			}
			machineBlock = str.toString().hashCode() << 16;
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}

		// 获取进程id
		final int processBlock;
		{
			int process = new Random().nextInt();
			try {
				process = ManagementFactory.getRuntimeMXBean().getName()
						.hashCode();
			} catch (Throwable e) {
			}

			// 获取类加载器id
			ClassLoader loader = IdGenerator.class.getClassLoader();
			int loaderId = loader == null ? 0 : System.identityHashCode(loader);
			processBlock = new StringBuilder()
					.append(Integer.toHexString(process))
					.append(Integer.toHexString(loaderId)).toString()
					.hashCode() & 0xFFFF;
		}

		return machineBlock | processBlock;
	}

	/**
	 * 生成Id
	 * 
	 * @return
	 */
	public static String generate() {
		return new StringBuffer()
				.append(Integer.toHexString((int) (System.currentTimeMillis() / 1000)))
				.append(Integer.toHexString(machineId))
				.append(Integer.toHexString(inc.addAndGet(1))).toString();
	}

	/**
	 * 
	 * generateReadable:生成带有时间戳的定长id，长度:30；可读性略强，但性能较之generate()方法差了约3倍 <br/>
	 *
	 * @author tongzheng.zhang
	 * @return
	 * @since JDK 1.6
	 */
	public static String generateReadable() {
		Calendar c = Calendar.getInstance();
		return new StringBuffer()
				.append(c.get(Calendar.YEAR))
				.append(formatNum(c.get(Calendar.MONTH) + 1))
				.append(formatNum(c.get(Calendar.DATE)))
				.append(formatNum(c.get(Calendar.HOUR_OF_DAY)))
				.append(formatNum(c.get(Calendar.MINUTE)))
				.append(formatNum(c.get(Calendar.SECOND))).append(SEPERATOR)
				.append(Integer.toHexString(machineId))
				.append(Integer.toHexString(inc.addAndGet(1))).toString();
	}

	/**
	 * 
	 * formatNum:格式化数字，主要为个位数加前缀零. <br/>
	 *
	 * @author tongzheng.zhang
	 * @param num
	 * @return
	 * @since JDK 1.6
	 */
	private static String formatNum(int num) {
		if (num < 10) {
			return String.valueOf("0" + num);
		}
		return String.valueOf(num);
	}
	
	public static void main(String[] args) {
		Stream.generate(IdGenerator :: generate).limit(100).forEach(System.out :: println);
	}
}
