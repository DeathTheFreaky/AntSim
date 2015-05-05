package at.antSim.utils;

import com.sun.istack.internal.NotNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created on 05.05.2015.
 *
 * @author Clemens
 */
public class ClassUtils {

	public static List<Class<?>> getAllInterfaces(@NotNull Class<?> clazz) {
		List<Class<?>> interfaceList = new LinkedList<Class<?>>();

		Class<?> cls = clazz;
		do {
			interfaceList.addAll(Arrays.asList(cls.getInterfaces()));
		} while ((cls = cls.getSuperclass()) != Object.class);

		return interfaceList;
	}

}
