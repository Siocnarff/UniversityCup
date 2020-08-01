import com.google.gson.Gson;
import shapes.Shape;
import shapes.Shapes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        Map map = new Map("grid_1.txt");
        map.insertShapes();
    }
}
