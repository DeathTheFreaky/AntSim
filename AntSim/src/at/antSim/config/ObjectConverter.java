package at.antSim.config;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Generic object converter.
 * <p>
 * <h3>Use examples</h3>
 * 
 * <pre>
 * Object o1 = Boolean.TRUE;
 * Integer i = ObjectConverter.convert(o1, Integer.class);
 * System.out.println(i); // 1
 * 
 * Object o2 = "false";
 * Boolean b = ObjectConverter.convert(o2, Boolean.class);
 * System.out.println(b); // false
 * 
 * Object o3 = new Integer(123);
 * String s = ObjectConverter.convert(o3, String.class);
 * System.out.println(s); // 123
 * </pre>
 * 
 * Not all possible conversions are implemented. You can extend the <tt>ObjectConverter</tt>
 * easily by just adding a new method to it, with the appropriate logic. For example:
 * 
 * <pre>
 * public static ToObject fromObjectToObject(FromObject fromObject) {
 *     // Implement.
 * }
 * </pre>
 * 
 * The method name doesn't matter. It's all about the parameter type and the return type.
 * 
 * @author BalusC
 * @link http://balusc.blogspot.com/2007/08/generic-object-converter.html
 */
public final class ObjectConverter {

    // Init ---------------------------------------------------------------------------------------

    private static final Map<String, Method> CONVERTERS = new HashMap<String, Method>();

    static {
        // Preload converters.
        Method[] methods = ObjectConverter.class.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getParameterTypes().length == 1) {
                // Converter should accept 1 argument. This skips the convert() method.
                CONVERTERS.put(method.getParameterTypes()[0].getName() + "_"
                    + method.getReturnType().getName(), method);
            }
        }
    }

    private ObjectConverter() {
        // Utility class, hide the constructor.
    }

    // Action -------------------------------------------------------------------------------------

    /**
     * Convert the given object value to the given class.
     * @param from The object value to be converted.
     * @param to The type class which the given object should be converted to.
     * @return The converted object value.
     * @throws NullPointerException If 'to' is null.
     * @throws UnsupportedOperationException If no suitable converter can be found.
     * @throws RuntimeException If conversion failed somehow. This can be caused by at least
     * an ExceptionInInitializerError, IllegalAccessException or InvocationTargetException.
     */
    @SuppressWarnings("unchecked")
	public static <T> T convert(Object from, Class<T> to) {
    	
        // Null is just null.
        if (from == null) {
            return null;
        }

        // Can we cast? Then just do it.
        if (to.isAssignableFrom(from.getClass())) {
            return to.cast(from);
        }

        // Lookup the suitable converter.
        String converterId = from.getClass().getName() + "_" + to.getName();
        Method converter = CONVERTERS.get(converterId);
        if (converter == null) {
            throw new UnsupportedOperationException("Cannot convert from " 
                + from.getClass().getName() + " to " + to.getName()
                + ". Requested converter does not exist.");
        }

        // Convert the value.
        try {
        	
        	//cannot call cast on a primitive
        	if (to.isPrimitive()) {
        		return (T) converter.invoke(to, from);
        	} else {
        		return to.cast(converter.invoke(to, from));
        	}
        } catch (Exception e) {
            throw new RuntimeException("Cannot convert from " 
                + from.getClass().getName() + " to " + to.getName()
                + ". Conversion failed with " + e.getMessage(), e);
        }
    }

    // Converters ---------------------------------------------------------------------------------

    //primitives ---------------------------------------------
    
    /**
     * Converts int to boolean. If int value is 0, then return FALSE, else return TRUE.
     * @param value The int to be converted.
     * @return The converted boolean value.
     */
    public static boolean pIntegerToBoolean(int value) {
        return value == 0 ? false : true;
    }

    /**
     * Converts boolean to int. If boolean value is TRUE, then return 1, else return 0.
     * @param value The boolean to be converted.
     * @return The converted int value.
     */
    public static int pBooleanToInteger(boolean value) {
        return value ? 1 : 0;
    }

    /**
     * Converts int to String.
     * @param value The int to be converted.
     * @return The converted String value.
     */
    public static String pIntegerToString(int value) {
        return String.valueOf(value);
    }

    /**
     * Converts String to int.
     * @param value The String to be converted.
     * @return The converted int value.
     */
    public static int pStringToInteger(String value) {
        return Integer.parseInt(value);
    }

    /**
     * Converts boolean to String.
     * @param value The boolean to be converted.
     * @return The converted String value.
     */
    public static String pBooleanToString(boolean value) {
        return String.valueOf(value);
    }

    /**
     * Converts String to boolean.
     * @param value The String to be converted.
     * @return The converted boolean value.
     */
    public static boolean pStringToBoolean(String value) {
        return Boolean.parseBoolean(value);
    }
    
    /**
     * Converts String to double.
     * @param value The String to be converted.
     * @return The converted double value.
     */
    public static double pStringToDouble(String value) {
        return Double.parseDouble(value);
    }

    /**
     * Converts String to double.
     * @param value The String to be converted.
     * @return The converted float value.
     */
    public static float pStringToFloat(String value) {
        return Float.parseFloat(value);
    }
    
    /**
     * Converts double to String.
     * @param value The double to be converted.
     * @return The converted String value.
     */
    public static String pDoubleToString(double value) {
        return String.valueOf(value);
    }
    
    //objects ------------------------------------
    
    /**
     * Converts Integer to Boolean. If integer value is 0, then return FALSE, else return TRUE.
     * @param value The Integer to be converted.
     * @return The converted Boolean value.
     */
    public static Boolean oIntegerToBoolean(Integer value) {
        return value.intValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
    }

    /**
     * Converts Boolean to Integer. If boolean value is TRUE, then return 1, else return 0.
     * @param value The Boolean to be converted.
     * @return The converted Integer value.
     */
    public static Integer oBooleanToInteger(Boolean value) {
        return value.booleanValue() ? Integer.valueOf(1) : Integer.valueOf(0);
    }

    /**
     * Converts Double to BigDecimal.
     * @param value The Double to be converted.
     * @return The converted BigDecimal value.
     */
    public static BigDecimal oDoubleToBigDecimal(Double value) {
        return new BigDecimal(value.doubleValue());
    }

    /**
     * Converts BigDecimal to Double.
     * @param value The BigDecimal to be converted.
     * @return The converted Double value.
     */
    public static Double oBigDecimalToDouble(BigDecimal value) {
        return new Double(value.doubleValue());
    }

    /**
     * Converts Integer to String.
     * @param value The Integer to be converted.
     * @return The converted String value.
     */
    public static String oIntegerToString(Integer value) {
        return value.toString();
    }

    /**
     * Converts String to Integer.
     * @param value The String to be converted.
     * @return The converted Integer value.
     */
    public static Integer oStringToInteger(String value) {
        return Integer.valueOf(value);
    }

    /**
     * Converts Boolean to String.
     * @param value The Boolean to be converted.
     * @return The converted String value.
     */
    public static String oBooleanToString(Boolean value) {
        return value.toString();
    }

    /**
     * Converts String to Boolean.
     * @param value The String to be converted.
     * @return The converted Boolean value.
     */
    public static Boolean oStringToBoolean(String value) {
        return Boolean.valueOf(value);
    }
    
    /**
     * Converts String to Double.
     * @param value The String to be converted.
     * @return The converted Double value.
     */
    public static Double oStringToDouble(String value) {
        return Double.valueOf(value);
    }
    
    /**
     * Converts Double to String.
     * @param value The Double to be converted.
     * @return The converted String value.
     */
    public static String oDoubleToString(Double value) {
        return value.toString();
    }

    /**
     * Converts String to Float.
     * @param value The String to be converted.
     * @return The converted Float value.
     */
    public static Float oStringToFloat(String value) {
        return Float.valueOf(value);
    }
}
