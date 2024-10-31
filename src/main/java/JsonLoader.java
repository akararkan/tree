import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalTime;
import java.util.List;

public class JsonLoader {
    public static List<Course> loadCourses(String filePath) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .create();

        try (FileReader reader = new FileReader(filePath)) {
            Type courseListType = new TypeToken<List<Course>>() {}.getType();
            return gson.fromJson(reader, courseListType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
