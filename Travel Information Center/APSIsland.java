import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Iterator;

public class APSIsland {

	private List<City> [] graph;
	private City [] cities;
	private int revison_num = 1;
	
	private final int MAXTREELEVEL = 4000;
	
	private int treelevel = 0;
	private int myrelativelevel = 0;
	private int relativelevel = 2147483646;
	
	public APSIsland(int totalCities) {
		this.graph = new List[totalCities];
		this.cities = new City[totalCities];
		for(int x = 0; x < totalCities; x++) {
			graph[x] = new ArrayList<>();
			cities[x] = new City(x + 1);
		}
		cities[0].startFestival();
	}
	
	public void addPath(int c1, int c2) {
		graph[c1 - 1].add(cities[c2 - 1]);
		graph[c2 - 1].add(cities[c1 - 1]);
	}
	
	public void announceFestival(int c1) {
		cities[c1 - 1].startFestival();
		revison_num += 1;
	}
	
	/*public void display() {
		for(int x = 0; x < graph.length; x++) {
			System.out.printf("%d : ", x + 1);
			for(City city : graph[x]) {
				System.out.printf("%d-%d ", city.no, city.distanceOfNearestFestiveCity);
			}
			System.out.println("");
		}
	}*/
	
	public int getDistanceOfNearestFestiveCity(int c1) {
		int nearcitydistance = 2147483646;
		if(!cities[c1 - 1].visited) {
			cities[c1 - 1].visited = true;
			try {
				nearcitydistance = cities[c1 - 1].getDistanceOfNearestFestiveCity(revison_num);
			} catch (NullPointerException npe) {
				for(City city : graph[c1 - 1]) {
					int distance = getDistanceOfNearestFestiveCity(city.no);
					if(distance < nearcitydistance) {
						nearcitydistance = distance;
						if(distance == 0) break;
					}
				}
				cities[c1 - 1].update(revison_num, nearcitydistance);
				nearcitydistance += 1;
			}
			cities[c1 - 1].visited = false;
		}
		return nearcitydistance;
	}
	
	public int getDistanceOfNearestFestiveCityRevised(int c1) {
		int nearcitydistance = 2147483646;
		if(!cities[c1 - 1].visited) {
			if(treelevel < MAXTREELEVEL) {
				treelevel += 1;
				cities[c1 - 1].visited = true;
				if(myrelativelevel < relativelevel) {
					myrelativelevel += 1;
					try {
						nearcitydistance = cities[c1 - 1].getDistanceOfNearestFestiveCity(revison_num);
					} catch (NullPointerException npe) {
						for(City city : graph[c1 - 1]) {
							int distance = getDistanceOfNearestFestiveCityRevised(city.no);
							if(distance < nearcitydistance) {
								nearcitydistance = distance;
								myrelativelevel = 0;
								relativelevel = nearcitydistance;
							}
						}
						if(nearcitydistance != 2147483646 && nearcitydistance != -1) {
							cities[c1 - 1].update(revison_num, nearcitydistance);
							nearcitydistance += 1;
						}
					}
					myrelativelevel -= 1;
				}
				cities[c1 - 1].visited = false;
				treelevel -= 1;
			} else {
				return -1;
			}
		}
		return nearcitydistance;
	}
	
	public int getDistance(int c1) {
		relativelevel = 2147483646;
		int dist = getDistanceOfNearestFestiveCityRevised(c1);
		if(dist == -1) {
			updateAll();
			dist = cities[c1 - 1].getDistanceOfNearestFestiveCity(revison_num);
		}
		return dist;
	}
	
	public void updateAll() {
		LinkedList<City> citylist = new LinkedList<City>();
		citylist.add(cities[0]);
		cities[0].visited = true;
		while(!citylist.isEmpty()) {
			City city = citylist.remove();
			for(City c : graph[city.no - 1]) {
				c.update(revison_num, city.getDistanceOfNearestFestiveCity(revison_num));
				if(!c.visited) {
					c.visited = true;
					citylist.add(c);
				}
			}
		}
		for(int x = 0; x < cities.length; x++) {
			cities[x].visited = false;
		}
	}
}
