import java.util.Scanner;

public class main {

	public static void setMax(int [][] grid, int [] destgrid, int col) {
		int [] arr = new int [grid.length];
		for(int x = 0; x< grid.length; x++) {
			if(grid[x][col] != -1) {
				int sum = destgrid[x] - grid[x][col];
				for(int y = x; y < grid.length; y++) {
					if(grid[y][col] == -1) {
						break;
					}
					sum += grid[y][col];
					if(arr[y] < sum) {
						arr[y] = sum;
					}
				}
				
				sum = destgrid[x] - grid[x][col];
				for(int y = x; y > -1; y--) {
					if(grid[y][col] == -1) {
						break;
					}
					sum += grid[y][col];
					if(arr[y] < sum) {
						arr[y] = sum;
					}
				}
			} else {
				arr[x] = -1;
			}
		}
		for(int x = 0; x < grid.length; x++) {
			destgrid[x] = arr[x];
		}
	}
	
	public static boolean merge(int [][] grid, int [] destgrid, int col) {
		for(int x = 0; x < grid.length; x++) {
			if(grid[x][col] == -1) {
				destgrid[x] = -1;
			} else if(destgrid[x] == -1) {
				if(!isreachable(grid, col)) {
					return false;
				}
				destgrid[x] = grid[x][col];
			} else {
				destgrid[x] += grid[x][col];
			}
		}
		return true;
	}
	
	public static boolean isreachable(int [][] grid, int col) {
		for(int x = 0; x < grid.length; x++) {
			if(grid[x][col] != -1 && grid[x][col - 1] != -1) {
				return true;
			}
		}
		return false;
	}
	
	public static int [] getArrayofSums(int [][] grid) {
		int [] destgrid = new int[grid.length];
		int tcols = grid[0].length;
		for(int x = 0; x < tcols; x++) {
			boolean ok = merge(grid, destgrid, x);
			if(!ok) {
				return null;
			}
			setMax(grid, destgrid, x);
		}
		return destgrid;
	}
	
	public static void main(String [] args) {
		int n = 0, m = 0;
		Scanner sc = new Scanner(System.in);
		n = sc.nextInt();
		m = sc.nextInt();
		int [][] grid = new int[n][m];
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < m; y++) {
				grid[x][y] = sc.nextInt();
			}
		}
		int [] destgrid = getArrayofSums(grid);
		if(destgrid != null) {
			int max = destgrid[0];
			for(int x = 1; x < n; x++) {
				if(destgrid[x] > max) {
					max = destgrid[x];
				}
			}
			System.out.println(max);
		} else {
			System.out.println("-1");
		}
	}
	
}
