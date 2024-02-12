package deringo.dbfsample.javadbf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import com.linuxense.javadbf.DBFRow;
import com.linuxense.javadbf.DBFUtils;

public class ReadItAll {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/sample.dbf");
        List<Map<String, String>> allRows = read(Files.newInputStream(path));
        
        allRows.get(0).keySet().forEach(key -> System.out.println(key +" : "+allRows.get(0).get(key)));
    }
    
    public static List<Map<String, String>> read(InputStream in) {
        List<Map<String, String>> allRows = new ArrayList<>();
        DBFReader reader = null;
        try {
            // create a DBFReader object
            reader = new DBFReader( in );

            //
            
            //
            List<String> allFieldNames = new ArrayList<>();
            
            // get the field count if you want for some reasons like the following
            int numberOfFields = reader.getFieldCount();

            // use this count to fetch all field information
            // if required
            for (int i = 0; i < numberOfFields; i++) {
                DBFField field = reader.getField(i);
                allFieldNames.add(field.getName());
            }

            // Now, lets us start reading the rows
            DBFRow row;
            while ((row = reader.nextRow()) != null) {
                Map<String, String> aRow = new HashMap<>(); 
                for (String fieldName : allFieldNames) {
                    aRow.put(fieldName, row.getString(fieldName));
                }
                allRows.add(aRow);
            }

            // By now, we have iterated through all of the rows

            return allRows;
            
        } catch (DBFException e) {
            e.printStackTrace();
            return allRows;
        }
        finally {
            DBFUtils.close(reader);
        }
    }
}
