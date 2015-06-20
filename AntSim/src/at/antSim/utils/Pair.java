package at.antSim.utils;

/**Used to store key value pairs of arbitrary types.
 * 
 * @author Flo
 *
 * @param <E>
 * @param <F>
 */
public class Pair<E, F> {

	E key;
	F value;
	
	public Pair(E key, F value) {
		this.key = key;
		this.value = value;
	}

	public E getKey() {
		return key;
	}

	public F getValue() {
		return value;
	}

	public void setValue(F value) {
		this.value = value;
	}
}
