import com.google.gson.Gson;
import com.sun.source.tree.Tree;
import shapes.Orientation;
import shapes.Shape;
import shapes.Shapes;

import java.io.File;
import java.io.FileWriter;
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
    String outputName;
    String fileOutput = "";

    Map(String filename, String outputName) {
        this.outputName = outputName;
        try {
            getShapeDefs();
            Scanner scanner = new Scanner(new File("./inputFiles/" + filename));
            String[] data = scanner.nextLine().split(",");
            numRows = Integer.parseInt(data[0]);
            numCols = Integer.parseInt(data[1]);
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

            for (int j = 0; j < numUniqueShapes; j++) {
                data = scanner.nextLine().split(",");
                shapeID[j] = Integer.parseInt(data[0]);
                available[j] = Integer.parseInt(data[1]);
            }

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
                        insertOptimally(k, shapeID[k], i, j);
                    }
                }
            }
        }
    }

    private void insertOptimally(int index, int id, int i, int j) {
        Shape s = getShape(id);
        for (Orientation orientation : s.orientations) {
            for (int[] coors : orientation.cells) {
                if (fits(orientation, i, j, coors)) {
                    available[index]--;
                    insertIntoMap(s.shape_id, orientation, i, j, coors);
                }
            }
        }
    }

    private void insertIntoMap(int shape_id, Orientation orientation, int i, int j, int[] start) {
        fileOutput += shape_id + "|";
        //writeToFile(shape_id + "|");
        //System.out.print(shape_id + "|");
        String out = "";
        for (int[] coors : orientation.cells) {
            map[i + coors[0] - start[0]][j + coors[1] - start[1]] = shape_id;
            out += (i + coors[0] - start[0]) + "," + (j + coors[1] - start[1]) + "|";
        }
        out = out.substring(0, out.length()-1);
        //System.out.println(out);
        //writeToFile(out + "\n");
        fileOutput += out + "\n";
    }

    void printToFile(){
        writeToFile(fileOutput);
    }

    private void writeToFile(String data){
        try {
            File file = new File(outputName);
            FileWriter myWriter = new FileWriter(file, true);
            myWriter.write(data);
            myWriter.close();
            //System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            //System.out.println("An error occurred.");
            e.printStackTrace();
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

    public void print(){
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                System.out.print(map[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
