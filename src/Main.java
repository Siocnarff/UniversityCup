import com.google.gson.Gson;
import shapes.Shape;
import shapes.Shapes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get("shapes_file.json")));
        System.out.println(json);
        System.out.println(json);
        Shapes shapes = new Gson().fromJson(json, Shapes.class);
        System.out.println(shapes.shapes[0].shape_id);
        Map map = new Map("grid_1.txt");
    }
}
