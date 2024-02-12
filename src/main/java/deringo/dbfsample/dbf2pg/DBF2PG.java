package deringo.dbfsample.dbf2pg;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringJoiner;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import com.linuxense.javadbf.DBFRow;
import com.linuxense.javadbf.DBFUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

public class DBF2PG {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/main/resources/sample.dbf");
        DBF2PG tool = new DBF2PG();        
        tool.readDBF(Files.newInputStream(path));
        tool.initDB();
    }
    
    public void initDB() throws IOException {
        Path path = Paths.get("src/main/resources/application.properties");
        Properties properties = new Properties();
        properties.load(Files.newInputStream(path));

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit", properties);
        EntityManager em = emf.createEntityManager();
        
        
        String sql1 = "DROP TABLE IF EXISTS public.sample";
        StringJoiner j1 = new StringJoiner(", ");
        for (String field : this.allFieldNames) {
            j1.add(field + " text");
        }
        String sql2 = "CREATE TABLE public.sample ("+j1.toString()+")";
        
        em.getTransaction().begin();
        em.createNativeQuery(sql1).executeUpdate();
        em.createNativeQuery(sql2).executeUpdate();
        
        
        StringJoiner j2 = new StringJoiner(", ");
        StringJoiner j3 = new StringJoiner(", ");
        for (String field : this.allFieldNames) {
            j2.add(field);
            j3.add(":"+field);
        }
        String sql3 = "INSERT INTO public.sample ("+j2.toString()+") VALUES("+j3.toString()+")";

        for (Map<String, String> row : allRows) {
            Query query = em.createNativeQuery(sql3);
            for (String field : this.allFieldNames) {
                query.setParameter(field, row.get(field));
            }
            query.executeUpdate();
        }
        em.getTransaction().commit();
    }

    private List<String> allFieldNames;
    private List<Map<String, String>> allRows;
    
    public List<Map<String, String>> readDBF(InputStream in) {
        allRows = new ArrayList<>();
        DBFReader reader = null;
        try {
            // create a DBFReader object
            reader = new DBFReader( in );

            //
            
            //
            allFieldNames = new ArrayList<>();
            
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
