import com.google.gson.Gson;
import com.sun.source.tree.Tree;
import shapes.Orientation;
import shapes.Shape;
import shapes.Shapes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Map {

    public int[][] map;
    public int numRows;
    public int numCols;
    public int numUniqueShapes;
    public int numBlockedCells;
    public int[] shapeID;
    public int[] available;
    private Shapes shapes;

    Map(String filename) {
        try {
            getShapeDefs();
            Scanner scanner = new Scanner(new File("./inputFiles/" + filename));
            String[] data = scanner.nextLine().split(",");
            numRows = Integer.parseInt(data[0]);
            numCols = Integer.parseInt(data[0]);
            data = scanner.nextLine().split(",");
            numUniqueShapes = Integer.parseInt(data[0]);
            data = scanner.nextLine().split(",");
            numBlockedCells = Integer.parseInt(data[0]);

            map = new int[numRows][numCols];
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    map[i][j] = 0;
                }
            }

            shapeID = new int[numUniqueShapes];
            available = new int[numUniqueShapes];

            TreeMap<Integer, Shape> sortedShapes = new TreeMap<Integer, Shape>();
            for(Shape s : shapes.shapes) {
                sortedShapes.put(s.capacity, s);
            }

            for(int j = 0; j < numUniqueShapes; j++) {
                data = scanner.nextLine().split(",");
                for(Shape s: sortedShapes.values()) {
                    if(s.shape_id == Integer.parseInt(data[0])) {
                        shapeID[j] = Integer.parseInt(data[0]);
                        available[j] = Integer.parseInt(data[1]);
                    }
                }
            }

           /* for (int j = 0; j < numUniqueShapes; j++) {
                data = scanner.nextLine().split(",");
                shapeID[j] = Integer.parseInt(data[0]);
                available[j] = Integer.parseInt(data[1]);
            }*/

            String[] BlockedData = scanner.nextLine().split("\\|");
            for (int i = 0; i < numBlockedCells; i++) {
                String[] temp = BlockedData[i].split(",");
                map[Integer.parseInt(temp[0])][Integer.parseInt(temp[1])] = -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getShapeDefs() throws IOException {
        String json = new String(Files.readAllBytes(Paths.get("shapes_file.json")));
        this.shapes = new Gson().fromJson(json, Shapes.class);
    }

    public void insertShapes() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (map[i][i] != 0) {
                    continue;
                }
                for(int k = 0; k < shapeID.length; k++) {
                    if (available[k] != 0) {
                        insertOptimally(shapeID[k], i, j);
                    }
                }
            }
        }
    }

    private void insertOptimally(int id, int i, int j) {
        Shape s = getShape(id);
        for (Orientation orientation : s.orientations) {
            for (int[] coors : orientation.cells) {
                if (fits(orientation, i, j, coors)) {
                    insertIntoMap(s.shape_id, orientation, i, j, coors);
                }
            }
        }
    }

    private void insertIntoMap(int shape_id, Orientation orientation, int i, int j, int[] start) {
        for (int[] coors : orientation.cells) {
            map[i + coors[0] - start[0]][j + coors[1] - start[1]] = shape_id;
        }
    }

    private boolean fits(Orientation orientation, int i, int j, int[] start) {
        for (int[] coors : orientation.cells) {
            if (i + coors[0] - start[0] < 0
                    || i + coors[0] - start[0] >= numRows
                    || j + coors[1] - start[1] >= numCols
                    || j + coors[1] - start[1] < 0
                    || map[i + coors[0] - start[0]][j + coors[1] - start[1]] != 0
            ) {
                return false;
            }
        }
        return true;
    }


    private Shape getShape(int id) {
        for (Shape s : shapes.shapes) {
            if (s.shape_id == id) {
                return s;
            }
        }
        return null;
    }
}
