
/*
 * Used the Book's Code and modified it to fit the need of my Project.
 * Every operation still has the same runtime as described by the authors Robert Sedgewick and Kevin Wayne.
 * The Key used in this alteration is an object Car.
 * 
 * ------------------------------------------------------------------------------------------------------------------
 * *  The {@code IndexMinPQ} class represents an indexed priority queue of generic keys.
 *  It supports the usual <em>insert</em> and <em>delete-the-minimum</em>
 *  operations, along with <em>delete</em> and <em>change-the-key</em> 
 *  methods. In order to let the client refer to keys on the priority queue,
 *  an integer between {@code 0} and {@code maxN - 1}
 *  is associated with each key—the client uses this integer to specify
 *  which key to delete or change.
 *  It also supports methods for peeking at the minimum key,
 *  testing if the priority queue is empty, and iterating through
 *  the keys.
 *  <p>
 *  This implementation uses a binary heap along with an array to associate
 *  keys with integers in the given range.
 *  The <em>insert</em>, <em>delete-the-minimum</em>, <em>delete</em>,
 *  <em>change-key</em>, <em>decrease-key</em>, and <em>increase-key</em>
 *  operations take logarithmic time.
 *  The <em>is-empty</em>, <em>size</em>, <em>min-index</em>, <em>min-key</em>,
 *  and <em>key-of</em> operations take constant time.
 *  Construction takes time proportional to the specified capacity.
 *  <p>
 *  
 *   For additional documentation, see <a href="https://algs4.cs.princeton.edu/24pq">Section 2.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  -----------------------------------------------------------------------------------------------------------------
 */

import java.util.NoSuchElementException;

public class IndexMinPQ {
	private int maxN; // maximum number of elements on PQ
	private int n; // number of elements on PQ
	// private int[] pq; // binary heap using 1-based indexing
	// private int[] qp; // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
	private Car[] keys; // keys[i] = priority of i
	public boolean type; // False for Price and True for Mileage.
	public boolean specifics;

	/**
	 * Initializes an empty indexed priority queue with indices between
	 * {@code 0} and {@code maxN - 1}.
	 * 
	 * @param maxN
	 *            the keys on this priority queue are index from {@code 0}
	 *            {@code maxN - 1}
	 * 
	 * @param type
	 *            checks whether it is a PQ for mileage based or price based.
	 * @param mode
	 *            checks whether it is normal PQ or model and make based PQ.
	 * @throws IllegalArgumentException
	 *             if {@code maxN < 0}
	 */
	public IndexMinPQ(int maxN, boolean type, boolean specifics) {
		if (maxN < 0)
			throw new IllegalArgumentException();
		this.maxN = maxN;
		this.specifics = specifics; // false for normal, true for make model
		this.type = type;
		n = 0;
		keys = new Car[maxN + 1];
	}

	/**
	 * Returns true if this priority queue is empty.
	 *
	 * @return {@code true} if this priority queue is empty; {@code false}
	 *         otherwise
	 */
	public boolean isEmpty() {
		return n == 0;
	}

	/**
	 * Is {@code i} an index on this priority queue?
	 *
	 * @param i
	 *            an index
	 * @return {@code true} if {@code i} is an index on this priority queue;
	 *         {@code false} otherwise
	 * @throws IllegalArgumentException
	 *             unless {@code 0 <= i < maxN}
	 */
	public boolean contains(int i) {
		if (i < 0 || i >= maxN)
			throw new IllegalArgumentException();
		return keys[i] != null;
	}

	/**
	 * Returns the number of keys on this priority queue.
	 *
	 * @return the number of keys on this priority queue
	 */
	public int size() {
		return n;
	}

	/**
	 * Inserts a car in the priority queue.
	 * 
	 * @param key
	 *            that needs to be added
	 * @return index of the car added
	 */

	public void insert(Car key) {
		if (key == null) {
			throw new IllegalArgumentException();
		}

		n++;
		if (n >= maxN) {
			System.out.println("Resized");
			maxN = maxN * 2;
			Car[] keysCopy = new Car[maxN + 1];

			for (int i = 0; i <= n; i++) {
				keysCopy[i] = keys[i];
			}
			keys = keysCopy;
		}

		keys[n] = key;
		swim(n, key);

	}

	/**
	 * Returns an index associated with a minimum key.
	 *
	 * @return an index associated with a minimum key
	 * @throws NoSuchElementException
	 *             if this priority queue is empty
	 */
	public int minIndex() {
		if (n == 0)
			throw new NoSuchElementException("Priority queue underflow\n");
		return 1;
	}

	/**
	 * Returns a minimum key.
	 *
	 * @return a minimum key
	 * @throws NoSuchElementException
	 *             if this priority queue is empty
	 */
	public Car minKey() {
		if (n == 0)
			throw new NoSuchElementException("Priority queue underflow\n");
		return keys[1];
	}

	/**
	 * Removes a minimum key and returns its associated key.
	 * 
	 * @return an index associated with a minimum key
	 * @throws NoSuchElementException
	 *             if this priority queue is empty
	 */
	public Car delMin() {
		if (n == 0)
			throw new NoSuchElementException("Priority queue underflow\n");
		Car deleted = keys[1];
		delete(1);
		return deleted;

	}

	/**
	 * Returns the key associated with index {@code i}.
	 *
	 * @param i
	 *            the index of the key to return
	 * @return the key associated with index {@code i}
	 * @throws IllegalArgumentException
	 *             unless {@code 0 <= i < maxN}
	 * @throws NoSuchElementException
	 *             no key is associated with index {@code i}
	 */
	public Car keyOf(int i) {
		if (i < 0 || i >= maxN)
			throw new IllegalArgumentException();
		if (!contains(i))
			throw new NoSuchElementException("index is not in the priority queue\n");
		else
			return keys[i];
	}

	/**
	 * Change the key associated with index {@code i} to the specified value.
	 *
	 * @param i
	 *            the index of the key to change
	 * @param key
	 *            change the key associated with index {@code i} to this key
	 * @throws IllegalArgumentException
	 *             unless {@code 0 <= i < maxN}
	 * @throws NoSuchElementException
	 *             no key is associated with index {@code i}
	 */
	public int changeKey(int i) {
		if (i < 0 || i >= maxN)
			throw new IllegalArgumentException();
		if (!contains(i))
			throw new NoSuchElementException("index is not in the priority queue\n");
		int index;
		index = swim(i, keys[i]);
		index = sink(i, keys[i]);
		return index;
	}
	
	/**
	 * @param i
	 * 			 removes a key associated to i
	 * 
	 * @return boolean that confirms the key associated to index i is removed
	 * @throws NoSuchElementException
	 *             if this priority queue is empty or if the index doesn't exist
	 */

	public boolean delete(int i) {
		if (i < 0 || i >= maxN)
			throw new IllegalArgumentException();
		if (!contains(i))
			throw new NoSuchElementException("index is not in the priority queue\n");
		exch(i, n--);
		swim(i, keys[i]);
		sink(i, keys[i]);
		keys[n + 1] = null;
		return true;
	}

	/***************************************************************************
	 * General helper functions.
	 ***************************************************************************/
	private boolean greater(int i, int j) {
		if (type) {
			return keys[i].getMileage() > keys[j].getMileage();
		} else if (!type) {
			return keys[i].getPrice() > keys[j].getPrice();
		}
		return false;
	}

	private void exch(int i, int j) {
		Car swap = keys[i];
		keys[i] = keys[j];
		keys[j] = swap;
		if (!specifics) {
			if (type) {
				keys[i].setMilesPQIndex(i);
				keys[j].setMilesPQIndex(j);
			} else if (!type) {
				keys[i].setPricePQIndex(i);
				keys[j].setPricePQIndex(j);
			}
		} else if (specifics) {
			if (type) {
				keys[i].setMnMMI(i);
				keys[j].setMnMMI(j);
			} else if (!type) {
				keys[i].setMnMPI(i);
				keys[j].setMnMPI(j);
			}

		}

	}

	/***************************************************************************
	 * Heap helper functions.
	 ***************************************************************************/
	private int swim(int k, Car key) {
		while (k > 1 && greater(k / 2, k)) {
			exch(k, k / 2);
			k = k / 2;
		}
		if (!specifics) {
			if (type) {
				keys[k].setMilesPQIndex(k);
			} else if (!type) {
				keys[k].setPricePQIndex(k);
			}
		} else if (specifics) {
			if (type) {
				keys[k].setMnMMI(k);
			} else if (!type) {
				keys[k].setMnMPI(k);
			}
		}
		return k;
	}

	private int sink(int k, Car key) {
		while (2 * k <= n) {
			int j = 2 * k;
			if (j < n && greater(j, j + 1))
				j++;
			if (!greater(k, j))
				break;
			exch(k, j);
			k = j;
		}
		if (!specifics) {
			if (type) {
				keys[k].setMilesPQIndex(k);
			} else if (!type) {
				keys[k].setPricePQIndex(k);
			}
		} else if (specifics) {
			if (type) {
				keys[k].setMnMMI(k);
			} else if (!type) {
				keys[k].setMnMPI(k);
			}
		}
		return k;
	}
}
