package deringo.dbfsample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import deringo.dbfsample.javadbf.JavaDBFReaderTest;
import deringo.dbfsample.javadbf.JavaDBFReaderWithFieldNamesTest;
import deringo.dbfsample.javadbf.ReadItAll;

public class TestMain {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/sample.dbf");
        System.out.println("sample.dbf exists: "+path.toFile().exists());
        
        JavaDBFReaderTest.read(Files.newInputStream(path));
        JavaDBFReaderWithFieldNamesTest.read(Files.newInputStream(path));
        
        List<Map<String, String>> allRows = ReadItAll.read(Files.newInputStream(path));
        allRows.get(0).keySet().forEach(key -> System.out.println(key +" : "+allRows.get(0).get(key)));
    }

}
