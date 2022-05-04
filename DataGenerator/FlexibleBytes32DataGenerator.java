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

    public static String reverseString( String text ) {
        String newText = "";
        for( int i = 0; i < text.length(); i++ ) {
            newText = text.charAt( i ) + newText;
        }
        return newText;
    }

    public static int[] convertToIntArray( ArrayList<Integer> list ) {
        int[] array = new int[ list.size() ];
        for( int i = 0; i < list.size(); i++ ) {
            array[i] = list.get( i );
        }
        return array;
    }

    public static long executeSpecialOperations( long value, String operations ) {
        Scanner operationScan = new Scanner( operations );
        long newValue = value;

        while( operationScan.hasNext() ) {
            String operation = operationScan.next();
            long valueToOperate = operationScan.nextLong();

            if( operation.equals( "+" ) ) {
                newValue = newValue + valueToOperate;
            } else if( operation.equals( "-" ) ) {
                newValue = newValue - valueToOperate;
            } else if( operation.equals( "*" ) ) {
                newValue = newValue * valueToOperate;
            } else if( operation.equals( "/" ) ) {
                newValue = newValue / valueToOperate;
            }
        }

        operationScan.close();
        return newValue;
    }
    
    public static int sumArray( int[] numbers ) {
        int sum = 0;
        for( int number : numbers ) {
            sum += number;
        }
        return sum;
    }

    public static String generateHexCode( int[] bitLengths, String[] specialOperations, Scanner fileScan ) {
        String hexCode;
        BigInteger value = BigInteger.ZERO;
        BigInteger multiplier = BigInteger.ONE;
        BigInteger previousMultiplier;

        while( fileScan.hasNext() ) {
            previousMultiplier = multiplier;
            String currentLine;
            do {
                currentLine = fileScan.nextLine();        
            } while( currentLine.startsWith("//") || currentLine.isEmpty() );
            
            Scanner lineScan = new Scanner( currentLine );
            for( int i = 0; i < bitLengths.length && lineScan.hasNext(); i++ ) {
                int currentBitLength = bitLengths[i];
                
                if( currentBitLength == 1 && lineScan.hasNextBoolean() ) {
                    if( lineScan.nextBoolean() == true ) {
                        value = value.add( multiplier );
                    }
                } else {
                    long currentInt;
                    long valueToAdd;
                    currentInt = lineScan.nextLong();
                    if( specialOperations[i] != null ) {
                        valueToAdd = executeSpecialOperations( currentInt, specialOperations[i] );
                    } else {
                        valueToAdd = currentInt;
                    }

                    value = value.add( BigInteger.valueOf( valueToAdd ).multiply( multiplier ) );
                }

                multiplier = multiplier.multiply( BigInteger.valueOf( currentBitLength ) );
                
            }
            lineScan.close();

            int totalBitLength = sumArray( bitLengths );
            multiplier = previousMultiplier.multiply( BigInteger.TWO.pow( totalBitLength ) );

    }
        hexCode = value.toString( 16 );
        hexCode = reverseString( hexCode );
        return hexCode;
    }


    public static void main( String[] args ) throws FileNotFoundException {
        String hexCode;
        final int MERGE_EACH = 64;
        final String TEXT_FILE_NAME = System.getProperty("user.dir") + "/DataGenerator/Recipes.txt";
        File textFile = new File( TEXT_FILE_NAME );
        Scanner fileScan = new Scanner( textFile );
        String hexCodes[];

        System.out.println("Enter your data structure as bits. Enter to end (new line).");
        System.out.println("Example: 1 5 3 2 7");
        System.out.print("Input: ");
        ArrayList<Integer> bitLengths = new ArrayList<Integer>();
        String line = userInput.nextLine();
        Scanner lineScan = new Scanner( line );
        while( lineScan.hasNextLong() ) {
            bitLengths.add( lineScan.nextInt() );
        }
        lineScan.close();

        String[] specialOperations = new String[ bitLengths.size() ];

        while( true ) {

        System.out.print("""
                Menu:
                1 - Start
                2 - Add special operation to an index
                3 - List bit lengths (with special operations)
                Choice: """);
        int userChoice = userInput.nextInt();
        //give error or error message if minus or exceeded length comes
        //method bool olup successful döndürebilir
        if( userChoice == 1 ) {
            int[] bitLengthsArray = convertToIntArray( bitLengths );
            hexCode = generateHexCode( bitLengthsArray, specialOperations, fileScan );
            hexCodes = mergeTextParts( hexCode , MERGE_EACH);
            System.out.println("Hex code:\n" + "0x" + hexCode);
            System.out.println("Hex code with parts seperated by " + MERGE_EACH + ":");
            for(int i = 0; i < hexCodes.length; i++) {
                System.out.println( hexCodes[i] );
            }
        } else if( userChoice == 2 ) {
            System.out.println("""
                (index and operation with spaces) (first one is index) 
                (no math priority, order is priority)
                Example: 0 2 3
                - 5 + 3 / 4 * 2
                    """);
            System.out.println("Enter indexes to add operations:");
            ArrayList<Integer> indexes = new ArrayList<Integer>();
            userInput.nextLine();
            line = userInput.nextLine();
            lineScan = new Scanner( line );
            while( lineScan.hasNextInt() ) {
                indexes.add( lineScan.nextInt() );
            }
            lineScan.close();
            System.out.println("Enter operation:");
            line = userInput.nextLine();
            lineScan = new Scanner( line );
            String operation = lineScan.nextLine();
            lineScan.close(); 
            for( int index : indexes ) {
                specialOperations[ index ] = operation;
            }
            
        } else if( userChoice == 3 ) {
            System.out.printf("%10s %10s %25s\n", "Index", "Bit Length", "Special Operations" ); 
            
            for( int i = 0; i < bitLengths.size(); i++ ) {
                System.out.printf( "%10d %10d %25s\n", i, bitLengths.get( i ), specialOperations[i] );
            }
        }

        }
    }
}