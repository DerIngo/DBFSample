package deringo.dbfsample.javadbf;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import com.linuxense.javadbf.DBFUtils;

public class JavaDBFReaderTest {

    public static void main(String args[]) throws IOException {
        read(new FileInputStream(args[0]));
    }
    
    public static void read(InputStream in) {
        DBFReader reader = null;
        try {

            // create a DBFReader object
            reader = new DBFReader( in );

            // get the field count if you want for some reasons like the following

            int numberOfFields = reader.getFieldCount();

            // use this count to fetch all field information
            // if required

            for (int i = 0; i < numberOfFields; i++) {

                DBFField field = reader.getField(i);

                // do something with it if you want
                // refer the JavaDoc API reference for more details
                //
                System.out.println(field.getName());
            }

            // Now, lets us start reading the rows

            Object[] rowObjects;

            while ((rowObjects = reader.nextRecord()) != null) {

                for (int i = 0; i < rowObjects.length; i++) {
                    System.out.println(rowObjects[i]);
                }
            }

            // By now, we have iterated through all of the rows

        } catch (DBFException e) {
            e.printStackTrace();
        }
        finally {
            DBFUtils.close(reader);
        }
    }
}
