package graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;



public class AdjacencyMatrixGraph<T> extends Graph<T> {
	private Map<T,Integer> keyToIndex;
	private List<T> indexToKey;
	private int[][] matrix;
	private int vertexCount;
	private int edgeCount;
	
	AdjacencyMatrixGraph(Set<T> keys) {
		int size = keys.size();
		this.keyToIndex = new HashMap<>();
		this.indexToKey = new ArrayList<>();
		this.matrix = new int[size][size];
		for (T key : keys) {
			this.keyToIndex.put(key, this.vertexCount);
			this.indexToKey.add(key);
			this.vertexCount++;
		}
	}
	
	@Override
	public int size() {
		return this.vertexCount;
	}

	@Override
	public int numEdges() {
		return this.edgeCount;
	}

	@Override
	public boolean addEdge(T from, T to) throws NoSuchElementException {
		if (!this.keyToIndex.containsKey(from)) throw new NoSuchElementException("Did not find 'from' vertex");
		if (!this.keyToIndex.containsKey(to)) throw new NoSuchElementException("Did not find 'to' vertex");
		int fromIndex = this.keyToIndex.get(from), toIndex = this.keyToIndex.get(to);
		if (this.matrix[fromIndex][toIndex] == 0) {
			this.matrix[fromIndex][toIndex] = 1;
			this.edgeCount++;
			return true;
		}
		return false;
	}

	@Override
	public boolean hasVertex(T key) {
		return this.keyToIndex.containsKey(key);
	}

	@Override
	public boolean hasEdge(T from, T to) throws NoSuchElementException {
		if (!this.keyToIndex.containsKey(from)) throw new NoSuchElementException("Did not find 'from' vertex");
		if (!this.keyToIndex.containsKey(to)) throw new NoSuchElementException("Did not find 'to' vertex");
		int fromIndex = this.keyToIndex.get(from), toIndex = this.keyToIndex.get(to);
		return this.matrix[fromIndex][toIndex] == 1;
	}

	@Override
	public boolean removeEdge(T from, T to) throws NoSuchElementException {
		if (!this.keyToIndex.containsKey(from)) throw new NoSuchElementException("Did not find 'from' vertex");
		if (!this.keyToIndex.containsKey(to)) throw new NoSuchElementException("Did not find 'to' vertex");
		int fromIndex = this.keyToIndex.get(from), toIndex = this.keyToIndex.get(to);
		if (this.matrix[fromIndex][toIndex] == 1) {
			this.matrix[fromIndex][toIndex] = 0;
			this.edgeCount--;
			return true;
		}
		return false;
	}

	@Override
	public int outDegree(T key) throws NoSuchElementException {
		try {
			int from = this.keyToIndex.get(key);
			int edges = 0;
			for (int to = 0; to < this.vertexCount; to++) if (this.matrix[from][to] == 1) edges++;
			return edges;
		}
		catch (NullPointerException e) { throw new NoSuchElementException("Did not find 'key' vertex"); }
	}

	@Override
	public int inDegree(T key) throws NoSuchElementException {
		try {
			int to = this.keyToIndex.get(key);
			int edges = 0;
			for (int from = 0; from < this.vertexCount; from++) if (this.matrix[from][to] == 1) edges++;
			return edges;
		}
		catch (NullPointerException e) { throw new NoSuchElementException("Did not find 'key' vertex"); }
	}

	@Override
	public Set<T> keySet() {
		return this.keyToIndex.keySet();
	}

	@Override
	public Set<T> successorSet(T key) throws NoSuchElementException {
		 // Check if the vertex exists
	    if (!this.keyToIndex.containsKey(key)) {
	        throw new NoSuchElementException("Did not find 'key' vertex");
	    }

	    Set<T> set = new HashSet<>();
	    int fromIndex = this.keyToIndex.get(key);
	    
	    for (int toIndex = 0; toIndex < this.vertexCount; toIndex++) {
	        if (this.matrix[fromIndex][toIndex] == 1) {
	            set.add(this.indexToKey.get(toIndex));
	        }
	    }
	    return set;
	}

	@Override
	public Set<T> predecessorSet(T key) throws NoSuchElementException {
		try {
			Set<T> set = new HashSet<>();
			int to = this.keyToIndex.get(key);
			for (int from = 0; from < this.vertexCount; from++) if (this.matrix[from][to] == 1) set.add(this.indexToKey.get(from));
			return set;
		}
		catch (NullPointerException e) { throw new NoSuchElementException("Did not find 'key' vertex"); }
	}

	@Override
	public Iterator<T> successorIterator(T key) throws NoSuchElementException {
		try { return new EdgeIterator(this.indexToKey, this.matrix, this.keyToIndex.get(key), true); }
		catch (NullPointerException e) { throw new NoSuchElementException("Did not find 'key' vertex"); }
	}

	@Override
	public Iterator<T> predecessorIterator(T key) throws NoSuchElementException {
		try { return new EdgeIterator(this.indexToKey, this.matrix, this.keyToIndex.get(key), false); }
		catch (NullPointerException e) { throw new NoSuchElementException("Did not find 'key' vertex"); }
	}

	@Override
	public Set<T> stronglyConnectedComponent(T key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> shortestPath(T startLabel, T endLabel) {
		// TODO Auto-generated method stub
		return null;
	}

	private class EdgeIterator implements Iterator<T> {
		List<T> indexToKey;
		int[][] matrix;
		int vertexCount;
		int from;
		int to = -1;
		boolean successor;

		EdgeIterator(List<T> indexToKey, int[][] matrix, int from, boolean successor) {
			this.indexToKey = indexToKey;
			this.matrix = matrix;
			this.vertexCount = this.matrix.length;
			this.from = from;
			this.successor = successor;
		}

		@Override
		public boolean hasNext() {
			for (int t = this.to + 1; t < this.vertexCount; t++) if ((this.successor ? this.matrix[from][t] : this.matrix[t][from]) == 1) return true;
			return false;
		}

		@Override
		public T next() {
			for (int t = this.to + 1; t < this.vertexCount; t++) if ((this.successor ? this.matrix[from][t] : this.matrix[t][from]) == 1) { this.to = t; break; }
			return this.indexToKey.get(this.to);
		}
	}

}