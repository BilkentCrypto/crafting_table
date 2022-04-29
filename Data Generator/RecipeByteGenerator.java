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
            result += number % 2;
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
            bits = bits.substring( i * 4 + 4 );

            for( int k = 8; k != 0; k /= 2) {
                if( bits.charAt( 0 ) == '1' ) {
                    decimal += k;
                }
            }
            hexCode += convertDecimalToHex( decimal );

        }
        return hexCode;
    }
    public static void main( String[] args ) throws FileNotFoundException {
        String bytesString = "";
        final String TEXT_FILE_NAME = "Recipes.txt";
        System.out.println("Started...");
        File textFile = new File( TEXT_FILE_NAME );
        Scanner input = new Scanner( textFile );


        while( input.hasNext() ) {
            String bits = "";
            String hexCode = "0x";
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
                        bits += convertToBits( itemNumber, 5 ); // item index
                        bits += convertToBits( itemNumber, 3 ); // item amount
                    }
                    bits = fillWithZeros( bits , 8 * 4, Direction.RIGHT);
                    hexCode += convertTo4BytesHex(bits);
                }
                else {
                    bits += "0";
                }
            } else {
                input.nextLine();
            }
        }

    }
}