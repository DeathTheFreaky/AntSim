package at.antSim.utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**Used to store objects as long as they are being "updated" by increasing their internal update count.<br>
 * When an object is no more updated (when their internal update count reaches 0), it is deleted from the list.
 * 
 * @author Flo
 *
 * @param <E>
 */
public class CountingLinkedList<E> {
	
	LinkedList<Pair<E, Integer>> list = new LinkedList<>();
	
	private void add(E key) {
		list.add(new Pair<E, Integer>(key, 1));
	}
	
	public boolean remove(E key) {
		
		int idx = 0;
		int foundIdx = -1;
		
		for (Pair<E, Integer> pair : list) {
			if (pair.getKey().equals(key)) {
				foundIdx = idx;
				break;
			}
			idx++;
		}
		
		if (foundIdx == -1) {
			return false;
		} else {
			list.remove(foundIdx);
			return true;
		}
	}
	
	public boolean contains(E key) {
		for (Pair<E, Integer> pair : list) {
			if (pair.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}
	
	public void increaseCount(E key) {
		boolean found = false;
		for (Pair<E, Integer> pair : list) {
			if (pair.getKey().equals(key)) {
				pair.setValue(pair.getValue() + 1);
				found = true; 
				break;
			}
		}
		if (!found) {
			add(key);
		}
	}
	
	public LinkedList<E> update() {
		LinkedList<Pair<E, Integer>> deleteAbles = new LinkedList<>();
		LinkedList<E> deleteds = new LinkedList<>();
		for (Pair<E, Integer> pair : list) {
			if (pair.getValue() <= 0) {
				deleteAbles.add(pair);
				deleteds.add(pair.getKey());
			} else {
				pair.setValue(pair.getValue() - 1);
			}
		}
		list.removeAll(deleteAbles);
		return deleteds;
	}
		
	public List<E> getUnmodifiableList() {
		LinkedList<E> retList = new LinkedList<>();
		for (Pair<E, Integer> pair : list) {
			retList.add(pair.getKey());
		}
		return Collections.unmodifiableList(retList);
	}

	public void clear() {
		list.clear();
	}
}
