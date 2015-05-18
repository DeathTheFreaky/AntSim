package at.antSim.utils;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created on 12.05.2015.
 *
 * @author Clemens
 */
@SuppressWarnings("restriction")
public class DebugUtils {

	@SuppressWarnings("deprecation")
	public static void printAddresses(String label, Object... objects) {
		Field f = null;
		Unsafe unsafe = null;
		try {
			f = Unsafe.class.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			unsafe = (Unsafe) f.get(null);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		boolean is64bit = System.getProperty("sun.arch.data.model").contains("64");
		System.out.print(label + ": 0x");
		long last = 0;
		int offset = unsafe.arrayBaseOffset(objects.getClass());
		int scale = unsafe.arrayIndexScale(objects.getClass());
		switch (scale) {
			case 4:
				long factor = is64bit ? 8 : 1;
				final long i1 = (unsafe.getInt(objects, offset) & 0xFFFFFFFFL) * factor;
				System.out.print(Long.toHexString(i1));
				last = i1;
				for (int i = 1; i < objects.length; i++) {
					final long i2 = (unsafe.getInt(objects, offset + i * 4) & 0xFFFFFFFFL) * factor;
					if (i2 > last)
						System.out.print(", +" + Long.toHexString(i2 - last));
					else
						System.out.print(", -" + Long.toHexString(last - i2));
					last = i2;
				}
				break;
			case 8:
				throw new AssertionError("Not supported");
		}
		System.out.println();
	}
}
