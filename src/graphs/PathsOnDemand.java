package graphs;
import java.util.List;
import java.util.Scanner;



/**
 * Interactive prompt for finding shortest paths.
 * Run this code just for fun, once you get shortestPaths working.
 * 
 * @author nate
 *
 */
public class PathsOnDemand {

	public static void main(String[] args) {

		Graph<String> livingPeopleALGraph = WikiSurfing.wikiLivingPeopleGraphAL(true);

		Scanner sc = new Scanner(System.in);
		
		while (true) {
			
			System.out.print("Enter starting person: ");
			String start = sc.nextLine();
			if (start.equals("")) {
				break;
			}
			if (!livingPeopleALGraph.hasVertex(start)) {
				System.out.println(start + " is not in (or has degree 0 in) the Living Persons category of Wikipedia.");
				continue;
			}
			System.out.print("Enter ending person: ");
			String end = sc.nextLine();
			if (end.equals("")) {
				break;
			}
			if (!livingPeopleALGraph.hasVertex(end)) {
				System.out.println(end + " is not in (or has degree 0 in) the Living Persons category of Wikipedia.");
				continue;
			}
		
			List<String> shortestPath = livingPeopleALGraph.shortestPath(start, end);
			System.out.println("Shortest path of length " + shortestPath.size() + ": " + shortestPath.toString());
		}
		
		sc.close();
		
	}

}
