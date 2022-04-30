

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



public class FileRenamer {

    
    enum Direction {
        LEFT,
        RIGHT
    }

    public static String fillWithZeros( String text, int length, Direction direction ) {
        String result = text;
        while( result.length() < length ) {
            if( direction == Direction.RIGHT )
            result = result + "0"; 
            else 
            result = "0" + result;
        }
        return result;
    }
    public static void main( String[] args ) {
        final int LAST_FILE_INDEX = 24;
        final String FILES_PATH = System.getProperty("user.dir") + "/ItemData/Metadatas/itemMetadatas/{id}.json";
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));

        for( int i = 0; i <= LAST_FILE_INDEX; i++ ) {
            String currentPath = FILES_PATH.replace("{id}", Integer.toString(i) );
            String newId;
            newId = Integer.toHexString(i);
            newId = fillWithZeros( newId , 64, Direction.LEFT);
            String newPath = FILES_PATH.replace("{id}", newId);

            Path source = Paths.get( currentPath );
            Path target = Paths.get( newPath );

            try{
                Files.move(source, target);
              } catch (IOException e) {
                e.printStackTrace();
              }

        }
    }

}
