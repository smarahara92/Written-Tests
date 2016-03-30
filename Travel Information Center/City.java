public class City {
	public int no;
	private boolean festivalStarted = false;
	private int revison_num = 0;
	public int distanceOfNearestFestiveCity = -1;
	public boolean visited = false;
	
	public City(int cityno) {
		this.no = cityno;
	}
	
	public void startFestival() {
		this.festivalStarted = true;
		distanceOfNearestFestiveCity = 0;
		revison_num = 2147483647;
	}
	
	public boolean doesFestivalStarted() {
		return festivalStarted;
	}
	
	public int getDistanceOfNearestFestiveCity(int revno) throws NullPointerException {
		if((revno & revison_num) == revno) {
			return distanceOfNearestFestiveCity;
		} else {
			throw new NullPointerException();
		}
	}
	
	public void update(int revno, int distance) {
		distance = distance + 1;
		if(!festivalStarted  && (revno > revison_num)) {
			this.revison_num = revno;
			this.distanceOfNearestFestiveCity = distance;
		} else if(!festivalStarted  && (revno == revison_num)) {
			if((revno == revison_num) && (this.distanceOfNearestFestiveCity > distance)) {
				this.distanceOfNearestFestiveCity = distance;
				this.visited = false;
			}
		}
	}
}
