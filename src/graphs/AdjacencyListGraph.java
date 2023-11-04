package graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class AdjacencyListGraph<T> extends Graph<T> {
	Map<T,Vertex> keyToVertex;
	 private int edgeCount;
	private class Vertex {
		T key;
		List<Vertex> successors;
		List<Vertex> predecessors;
		
		
		Vertex(T key) {
			this.key = key;
			this.successors = new ArrayList<Vertex>();
			this.predecessors = new ArrayList<Vertex>();
		}
	}
	
	AdjacencyListGraph(Set<T> keys) {
		this.keyToVertex = new HashMap<T,Vertex>();
		for (T key : keys) {
			Vertex v = new Vertex(key);
			this.keyToVertex.put(key, v);
		}
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		 return keyToVertex.size();
	}

	@Override
	public int numEdges() {
		// TODO Auto-generated method stub
		 return edgeCount;
	}

	@Override
	public boolean addEdge(T from, T to) {
		Vertex fromVertex = keyToVertex.get(from);
	    Vertex toVertex = keyToVertex.get(to);

	    // Check if both vertices exist, if not, throw an exception.
	    if (fromVertex == null || toVertex == null) {
	        throw new NoSuchElementException("One or both vertices not found");
	    }

	    // Prevent adding the same edge if it already exists.
	    if (fromVertex.successors.contains(toVertex)) {
	        return false; // Edge already exists
	    }

	    // Add the edge since it doesn't exist.
	    fromVertex.successors.add(toVertex);
	    toVertex.predecessors.add(fromVertex); // If the graph is directed
	    edgeCount++; // increase the count of edges
	    return true;
	}

	@Override
	public boolean hasVertex(T key) {
		return keyToVertex.containsKey(key);
	}

	@Override
	public boolean hasEdge(T from, T to) throws NoSuchElementException {
		 Vertex fromVertex = keyToVertex.get(from);
	        Vertex toVertex = keyToVertex.get(to);
	        if (fromVertex == null || toVertex == null) {
	            throw new NoSuchElementException("Vertex not found");
	        }
	        return fromVertex.successors.contains(toVertex);
	}

	@Override
	public boolean removeEdge(T from, T to) throws NoSuchElementException {
		Vertex fromVertex = keyToVertex.get(from);
        Vertex toVertex = keyToVertex.get(to);
        if (fromVertex == null || toVertex == null) {
            throw new NoSuchElementException("Vertex not found");
        }
        if (fromVertex.successors.remove(toVertex)) {
            toVertex.predecessors.remove(fromVertex);
            edgeCount--;
            return true;
        }
        return false; // Edge not present
	}

	@Override
	public int outDegree(T key) {
		// TODO Auto-generated method stub
		  Vertex vertex = keyToVertex.get(key);
	        if (vertex == null) {
	            throw new NoSuchElementException("Vertex not found");
	        }
	        return vertex.successors.size();
	}

	@Override
	public int inDegree(T key) {
		// TODO Auto-generated method stub
		 Vertex vertex = keyToVertex.get(key);
	        if (vertex == null) {
	            throw new NoSuchElementException("Vertex not found");
	        }
	        return vertex.predecessors.size();
	}

	@Override
	public Set<T> keySet() {
		// TODO Auto-generated method stub
		return keyToVertex.keySet();
	}

	@Override
	public Set<T> successorSet(T key) {
		  Vertex vertex = keyToVertex.get(key);
	        if (vertex == null) {
	            throw new NoSuchElementException("Vertex not found");
	        }
	        Set<T> successors = new HashSet<>();
	        for (Vertex v : vertex.successors) {
	            successors.add(v.key);
	        }
	        return successors;
	}

	@Override
	public Set<T> predecessorSet(T key) {
		Vertex vertex = keyToVertex.get(key);
        if (vertex == null) {
            throw new NoSuchElementException("Vertex not found");
        }
        Set<T> predecessors = new HashSet<>();
        for (Vertex v : vertex.predecessors) {
            predecessors.add(v.key);
        }
        return predecessors;
	}

	@Override
	public Iterator<T> successorIterator(T key) {
		 Vertex vertex = keyToVertex.get(key);
	        if (vertex == null) {
	            throw new NoSuchElementException("Vertex not found");
	        }
	        return new Iterator<T>() {
	            private final Iterator<Vertex> it = vertex.successors.iterator();

	            @Override
	            public boolean hasNext() {
	                return it.hasNext();
	            }

	            @Override
	            public T next() {
	                return it.next().key;
	            }
	        };
	}

	@Override
	public Iterator<T> predecessorIterator(T key) {
		 Vertex vertex = keyToVertex.get(key);
		    if (vertex == null) {
		        throw new NoSuchElementException("Vertex not found");
		    }
		    return new Iterator<T>() {
		        private final Iterator<Vertex> it = vertex.predecessors.iterator();

		        @Override
		        public boolean hasNext() {
		            return it.hasNext();
		        }

		        @Override
		        public T next() {
		            return it.next().key;
		        }
		    };
	}
	private Map<Vertex, Integer> indices = new HashMap<>();
	private Map<Vertex, Integer> lowlink = new HashMap<>();
	private Stack<Vertex> stack = new Stack<>();
	private int index = 0;
	private List<Set<T>> sccs = new ArrayList<>();
	private Set<Vertex> visited = new HashSet<>();
	



	
	@Override
	public Set<T> stronglyConnectedComponent(T key) {
	    if (!hasVertex(key)) {
	        throw new NoSuchElementException("Vertex not found");
	    }

	    // Clear the previous state
	    indices.clear();
	    lowlink.clear();
	    stack.clear();
	    sccs.clear();
	    visited.clear(); // Reset the visited set
	    index = 0;

	    Vertex start = keyToVertex.get(key);
	    strongConnect(start);

	    for (Set<T> scc : sccs) {
	        if (scc.contains(key)) {
	            return scc;
	        }
	    }

	    return Collections.emptySet();
	}
	
	
	
	//THis is current
	
		
	private void strongConnect(Vertex v) {
	    if (visited.contains(v)) {
	        return;
	    }
	    visited.add(v);
	    
	    indices.put(v, index);
	    lowlink.put(v, index);
	    index++;
	    stack.push(v);

	    for (Vertex w : v.successors) {
	        if (!indices.containsKey(w)) {
	            strongConnect(w);
	            lowlink.put(v, Math.min(lowlink.get(v), lowlink.get(w)));
	        } else if (stack.contains(w)) {
	            lowlink.put(v, Math.min(lowlink.get(v), indices.get(w)));
	        }
	    }

	    if (lowlink.get(v).equals(indices.get(v))) {
	        Set<T> component = new HashSet<>();
	        Vertex w;
	        do {
	            w = stack.pop();
	            component.add(w.key);
	        } while (!w.equals(v));
	        sccs.add(component);
	    }
	}


	



	@Override
	public List<T> shortestPath(T startLabel, T endLabel) {
		 // Check if the vertices exist
	    if (!hasVertex(startLabel) || !hasVertex(endLabel)) {
	        throw new NoSuchElementException("Start or end vertex not found");
	    }

	    Queue<T> queue = new LinkedList<>();
	    Map<T, T> predecessors = new HashMap<>();
	    Set<T> visited = new HashSet<>();
	    
	    queue.add(startLabel);
	    visited.add(startLabel);

	    boolean pathExists = false;

	    while (!queue.isEmpty()) {
	        T current = queue.poll();

	        // If we reached the end vertex, break out of the loop
	        if (current.equals(endLabel)) {
	            pathExists = true;
	            break;
	        }

	        for (T neighbor : successorSet(current)) {
	            if (!visited.contains(neighbor)) {
	                predecessors.put(neighbor, current);
	                visited.add(neighbor);
	                queue.add(neighbor);
	            }
	        }
	    }

	    // If there's no path from startLabel to endLabel
	    if (!pathExists) {
	       return null;
	    }

	    // Reconstruct the path from endLabel to startLabel
	    List<T> path = new ArrayList<>();
	    T current = endLabel;
	    while (current != null) {
	        path.add(current);
	        current = predecessors.get(current);
	    }
	    Collections.reverse(path); // Reverse the path to get the correct order from start to end
	    return path;
	}

}
