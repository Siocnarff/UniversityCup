package map;

import java.io.File;
import java.util.Scanner;

public class Map {
    char[][] map;
    Meta m;

    public Map(String name) {
        readFromFile(name);
    }

    private void readFromFile(String fileName) {
        try {
            Scanner scanner = new Scanner(new File("./inputFiles/" + fileName));
            String[] data = scanner.nextLine().split(",");
            m = new Meta(
                    Integer.parseInt(data[0]),
                    Integer.parseInt(data[1]),
                    Integer.parseInt(data[2]),
                    Integer.parseInt(data[3]),
                    Integer.parseInt(data[4])
            );
            for (int j = 0; j < m.map_height; j++) {
                char[] line = scanner.nextLine().toCharArray();
                for (int i = 0; i < m.map_width; i++) {
                    map[i][j] = line[i];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class Meta {
        public final int worm_count, num_crates, num_bases, map_width, map_height;
        public Meta(int wc, int nc, int nb, int w, int h) {
            worm_count = wc;
            num_crates = nc;
            num_bases = nb;
            map_width = w;
            map_height = h;
        }
    }
}
