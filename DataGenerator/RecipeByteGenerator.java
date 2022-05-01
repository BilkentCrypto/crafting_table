import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class RecipeByteGenerator {

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

    public static String convertToBits( int number, int length ) {
        String result = "";

        while( number != 0 ) {
            result = (number % 2) + result;
            number /= 2;
        }

        result = fillWithZeros( result , length, Direction.LEFT);
        return result;
    }

    public static char convertDecimalToHex( int decimal ) {
        if( decimal > 9 ) {
            return (char) ( decimal + 87 );
        } else {
            return (char) (decimal + 48);
        }
    }

    public static String convertTo4BytesHex( String bits ) {
        String hexCode = "";
        for( int i = 0; i < 8; i++ ) {
            String currentPart = bits.substring(i * 4, i * 4 + 4);
            int decimal = 0;
            int index = 0;
            for( int k = 8; k != 0; k /= 2) {
                if( currentPart.charAt( index ) == '1' ) {
                    decimal += k;
                }
                index++;
            }
            hexCode += convertDecimalToHex( decimal );
        }
        return hexCode;
    }

    public static String[] mergeTextParts( String text, int mergeEach ) {
        int mergeNumber = text.length() / mergeEach + 1;
        String[] texts = new String[ mergeNumber ];
        int index;
        String currentText;
        for( index = 0; index < mergeNumber - 1; index++) {
            currentText = text.substring(index * mergeEach, (index + 1) * mergeEach );
            texts[index] = "0x" + currentText;
        }

        currentText = text.substring(index * mergeEach);
        texts[index] = "0x" + fillWithZeros( currentText, mergeEach, Direction.RIGHT);

        return texts;
    }
    public static void main( String[] args ) throws FileNotFoundException {
        String bytesString = "";
        final int MERGE_EACH = 64;
        final String TEXT_FILE_NAME = "/DataGenerator/Recipes.txt";
        System.out.println("Started...");
        File textFile = new File( System.getProperty("user.dir") + TEXT_FILE_NAME );
        Scanner input = new Scanner( textFile );
        String hexCode = "";
        

        System.out.println( convertToBits(2, 2) );

        while( input.hasNext() ) {
            String bits = "";
            String current = input.next();
            if( !current.startsWith("//") ) {
                if( current.equals( "true" ) ) { // is craftable
                    bits += "1";

                    if( input.nextBoolean() ) bits += "1"; // is 2x2 craftable
                    else bits += "0";

                    bits += convertToBits( input.nextInt() - 1, 4 ); //produced amount
                    int itemNumber = input.nextInt();
                    bits += convertToBits( itemNumber, 2 ); // required item number
                    for( int i = 0; i < itemNumber; i++ ) {
                        bits += convertToBits( input.nextInt(), 5 ); // item index
                        bits += convertToBits( input.nextInt() - 1, 3 ); // item amount
                    }
                }
                else {
                    bits += "0";
                }

                bits = fillWithZeros( bits , 8 * 4, Direction.RIGHT);
                System.out.println(bits);
                hexCode += convertTo4BytesHex(bits);

            } else {
                input.nextLine();
            }
        }
        
        String hexWithParts[] = mergeTextParts( hexCode, MERGE_EACH );
        
        for( int i = 0; i < hexWithParts.length; i++ ) {
            System.out.printf("Part %d: %s\n", i + 1, hexWithParts[i] );
        }

    }
}