package io.ztz.simple.mq.api.tools;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 
 * @author Tony
 *
 */
public final class PrintExceptionStacktrace {

	public static String getStacktrace(Throwable ex) {
		try (StringWriter sw = new StringWriter(); PrintWriter writer = new PrintWriter(sw);) {
			ex.printStackTrace(writer);
			return sw.toString();
		} catch (Exception e) {
			return null;
		}
	}
}
