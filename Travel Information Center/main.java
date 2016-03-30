import java.util.Scanner;

public class main {
	public static void main(String [] args) {
		Scanner sc = new Scanner(System.in);
		int n = 0, m = 0, c1 = 0, c2 = 0, q = 0, c = 0;
		n = sc.nextInt();
		m = sc.nextInt();
		APSIsland apsi = new APSIsland(n);
		n -= 1;
		for(int x = 0; x < n; x++) {
			c1 = sc.nextInt();
			c2 = sc.nextInt();
			apsi.addPath(c1, c2);
		}
		for(int x = 0; x < m; x++) {
			q = sc.nextInt();
			c = sc.nextInt();
			if(q == 1) {
				apsi.announceFestival(c);
			} else {
				System.out.println(apsi.getDistance(c));
			}
		}
	}
}
