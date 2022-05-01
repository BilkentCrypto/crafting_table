import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;
public class FlexibleBytes32DataGenerator {

    private static Scanner userInput = new Scanner( System.in );

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

    public static String generateHexCode( int[] bitLengths, String[] specialOperations, Scanner fileScan ) {
        String hexCode;
        BigInteger value = BigInteger.ZERO;
        BigInteger multiplier = BigInteger.ONE;

        while( fileScan.hasNext() ) {
        
            String currentLine;
            do {
                currentLine = fileScan.nextLine();        
            } while( currentLine.startsWith("//") || currentLine.isEmpty() );
        
            Scanner lineScan = new Scanner( currentLine );
            for( int i = 0; i < bitLengths.length; i++ ) {
                if( bitLengths[i] == 1 && lineScan.hasNextBoolean() ) {

                } else {
                    int currentInt;
                    currentInt = fileScan.nextInt();
                }
                
            }


    }

        hexCode = value.toString( 16 );
        return hexCode;
    }


    public static void main( String[] args ) throws FileNotFoundException {
        String bytesString = "";
        final int MERGE_EACH = 64;
        final String TEXT_FILE_NAME = "Data Generator/Recipes.txt";
        File textFile = new File( TEXT_FILE_NAME );
        Scanner fileInput = new Scanner( System.getProperty("user.dir")  + textFile );
        String hexCode = "";

        System.out.println("Enter your data structure as bits. Enter to end (new line).");
        System.out.println("Example: 1 5 3 2 7");
        System.out.print("Input: ");
        ArrayList<Integer> bitLengths = new ArrayList<Integer>();
        String line = userInput.nextLine();
        Scanner lineScan = new Scanner( line );
        while( lineScan.hasNextInt() ) {
            bitLengths.add( lineScan.nextInt() );
        }

        String[] specialOperations = new String[ bitLengths.size() ];

        while( true ) {

        System.out.print("""
                Menu:
                1 - Start
                2 - Add special operation to an index
                (index and operation with spaces) 
                (no math priority, order is priority)
                Example: - 5 + 3 / 4 * 2
                Choice: """);
        int userChoice = userInput.nextInt();
        //give error or error message if minus or exceeded length comes
        //method bool olup successful döndürebilir
        if( userChoice == 1 ) {
            //hexCode = generateHexCode( bitLengths.toArray(), specialOperations, fileInput );
        } else if( userChoice == 2 ) {
            int index;
            userInput.nextLine();
            line = userInput.nextLine();
            lineScan = new Scanner( line );
            index = lineScan.nextInt();
            specialOperations[ index ] = lineScan.nextLine();
        }

        }
    }
}