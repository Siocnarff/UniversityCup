import java.io.File;
import java.sql.SQLOutput;
import java.util.Scanner;

public class Map {

    public int[][] map;
    public int numRows;
    public int numCols;
    public int numUniqueShapes;
    public int numBlockedCells;
    public int[] shapeID;
    public int[] available;

    Map(String filename){
        try {
            Scanner scanner = new Scanner(new File("./inputFiles/" + filename));
            String[] data = scanner.nextLine().split(",");
            numRows = Integer.parseInt(data[0]);
            numCols = Integer.parseInt(data[0]);
            data = scanner.nextLine().split(",");
            numUniqueShapes = Integer.parseInt(data[0]);
            data = scanner.nextLine().split(",");
            numBlockedCells = Integer.parseInt(data[0]);

            map = new int[numRows][numCols];
            for(int i = 0; i < numRows; i++){
                for(int j = 0; j < numCols; j++){
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
            for(int i = 0; i < numBlockedCells; i++){
                String[] temp = BlockedData[i].split(",");
                map[Integer.parseInt(temp[0])][Integer.parseInt(temp[1])] = -1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
