#include <stdio.h>
#include <stdlib.h>

#define MID(X, Y) (X - Y) / 2

#define FONTHEIGHT 8

#define TRUE 1
#define FALSE 0

struct ascii {
	int height;
	int width;
	char **letter;
};

void clearchar(struct ascii *letter) {
	int x = 0;
	for(x = 0; x < letter -> height; x++) {
		free(letter -> letter[x]);
	}
	free(letter -> letter);
}

void rotate(struct ascii *letter) {
	int m = letter -> width, n = letter -> height;
	char **character = (char **) malloc (m * sizeof(char *));
	int x, y, z;
	for(x = 0; x < m; x++) {
		character[x] = (char *) malloc (n * sizeof(char));
		for(y = 0, z = (n - 1); y < n; y++, z--) {
			character[x][y] = letter -> letter[z][x];
		}
	}
	clearchar(letter);
	letter -> height = m;
	letter -> width = n;
	letter -> letter = character;
}

void expand(char **letter, int n, int m, int times, char **dest, int n1, int m1) {
	int x, y, z, w, p, q;
	for(x = 0, p = n1; x < n; x++) {
		for(w = 0; w < times; w++, p++) {
			for(y = 0, q = m1; y < m; y++) {
				for(z = 0; z < times; z++, q++) {
					dest[p][q] = letter[x][y];
				}
			}
		}
	}
}


struct ascii * createchar(char *buf[], int maxlength) {
	struct ascii *character = (struct ascii *) malloc (sizeof(struct ascii));
	character -> height = FONTHEIGHT;
	character -> width = maxlength;
	character -> letter = (char **) malloc (FONTHEIGHT * sizeof(char *));
	int x, y, ok;
	for(x = 0; x < FONTHEIGHT; x++) {
		character -> letter[x] = (char *) malloc (maxlength * sizeof(char));
		ok = TRUE;
		for(y = 0; y < maxlength; y++) {
			if(ok) {
				if(buf[x][y] == '\0') {
					ok = FALSE;
					y -= 1;
				} else if(buf[x][y] == '\n') {
					character -> letter[x][y] = ' ';
				} else {
					character -> letter[x][y] = buf[x][y];
				}
			} else {
				character -> letter[x][y] = ' ';
			}
		}
	}
	return character;
}

void clearbuf(char *buf[], int n) {
	int x = 0;
	for(x = 0; x < n; x++) {
		free(buf[x]);
		buf[x] = NULL;
	}
}

struct ascii ** loadFont(FILE *fp) {
	if(fp != NULL) {
		struct ascii **font = (struct ascii **) malloc (127 * sizeof(struct ascii *));
		char *buf[FONTHEIGHT];
		int x, y;
		for(x = 0; x < FONTHEIGHT; x++) {
			buf[x] = (char *) malloc (20 * sizeof(char *));
		}
		int length, maxlength;
		size_t explength = 20;
		for(x = 0; x < 127; x++) {
			maxlength = 0;
			for(y = 0; y < FONTHEIGHT; y++) {
				length = getline(&buf[y], &explength, fp);
				if(length > maxlength) {
					maxlength = length;
				}
			}
			font[x] = createchar(buf, maxlength);
		}
		clearbuf(buf, FONTHEIGHT);
		return font;
	}
	return NULL;
}

int getMax(int *arr, int len) {
	int x, max = arr[0];
	for(x = 1; x < len; x++) {
		if(max < arr[x]) {
			max = arr[x];
		}
	}
	return max;
}

int getSum(int *arr, int len) {
	int x, sum = 0;
	for(x = 0; x < len; x++) {
		sum += arr[x];
	}
	return sum;
}



int main(int argc, char *argv[]) {
	if((argc < 3) || (((argc - 1) % 2))) {
		printf("%s name length\n", argv[0]);
		return 0;
	}
	int tn = ((argc - 1) / 2), z, x, y;
	FILE *fp = fopen("font.txt", "r");
	struct ascii **font = loadFont(fp);
	for(z = 0; z < 127; z++) {
		rotate(font[z]);
	}
	fclose(fp);
	int *height = (int *) malloc (tn * sizeof(int));
	int *inc = (int *) malloc (tn * sizeof(int));
	for(x = 2, z = 0; x < argc; x += 2, z++) {
		inc[z] = atoi(argv[x]);
	}
	
	int maxwidth = getSum(inc, tn) * 8, theight;
	
	//printf("maxwidth = %d\n", maxwidth);
	
	for(x = 1, z = 0; x < argc; x += 2, z++) {
		y = 0;
		theight = 0;
		while(argv[x][y] != '\0') {
			//printf("%d\n", font[argv[x][y]] -> height);
			theight += font[argv[x][y]] -> height;
			y += 1;
		}
		height[z] = theight * inc[z];
		//printf("%d %d\n", height[z], inc[z]);
	}
	
	int maxheight = getMax(height, tn), startr, startc;
	
	//printf("maxwidth = %d maxheight = %d\n", maxwidth, maxheight);
	
	char **banner = (char **) malloc (maxheight * sizeof(char *));
	for(x = 0; x < maxheight; x++) {
		banner[x] = (char *) malloc (maxwidth * sizeof(char));
		for(y = 0; y < maxwidth; y++) {
			banner[x][y] = ' ';
		}
	}
	
	
	for(x = 1, z = 0; x < argc; x += 2, z++) {
		y = 0;
		startr = MID(maxheight, height[z]);
		while(argv[x][y] != '\0') {
			//printf("%d\n", argv[x][y]);
			expand(font[argv[x][y]] -> letter, font[argv[x][y]] -> height, font[argv[x][y]] -> width, inc[z], banner, startr, startc);
			//printf("startr = %d startc = %d %c\n", startr, startc, argv[x][y]);
			startr += font[argv[x][y]] -> height * inc[z];
			y += 1;
		}
		startc += inc[z] * 8;
	}
	
	for(x = 0; x < maxheight; x++) {
		for(y = 0; y < maxwidth; y++) {
			printf("%c ", banner[x][y]);
		}
		printf("\n");
	}
	
	return 0;
	
}
