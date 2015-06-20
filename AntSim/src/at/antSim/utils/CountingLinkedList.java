package at.antSim.utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.sun.javafx.collections.UnmodifiableListSet;

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
	
	/**Removes an Element from this {@link CountingLinkedList}
	 * 
	 * @param key
	 * @return - true if the passed key was stored and hence has been removed
	 */
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
	
	/**
	 * @param key
	 * @return - true, if the passed key is stored inside this {@link CountingLinkedList}
	 */
	public boolean contains(E key) {
		for (Pair<E, Integer> pair : list) {
			if (pair.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}
	
	/**Increases an Element's "life" count, so it won't be deleted on the next update cycle.
	 * 
	 * @param key
	 */
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
	
	/**
	 * @return - a LinkedList of all Elements which have not been increased and hence are no more part of this {@link CountingLinkedList}
	 */
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
		
	/**
	 * @return - an UnmodifiableList of all Elements stored by this {@link CountingLinkedList}
	 */
	public List<E> getUnmodifiableList() {
		LinkedList<E> retList = new LinkedList<>();
		for (Pair<E, Integer> pair : list) {
			retList.add(pair.getKey());
		}
		return Collections.unmodifiableList(retList);
	}

	/**Removes all elements from this {@link CountingLinkedList}
	 * 
	 */
	public void clear() {
		list.clear();
	}
}
