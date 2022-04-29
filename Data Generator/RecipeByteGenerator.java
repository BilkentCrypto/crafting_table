import java.util.Scanner;
public class RecipeByteGenerator {

    public static String fillWithZeros( String text, int length ) {
        String result = text;
        while( text.length() < length ) {
            result += "0";
        }
        return result;
    }

    public static String convertToBits( int number, int length ) {
        String result = "";

        while( number != 0 ) {
            result += number % 2;
            number /= 2;
        }

        result = fillWithZeros( result , length);
        return result;
    }
    public static void main( String[] args ) {
        String bytesString = "";
        Scanner input = new Scanner( System.in );

        System.out.println( convertToBits(5, 7) );

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
                        bits += convertToBits( itemNumber, 5 ); // item index
                        bits += convertToBits( itemNumber, 3 ); // item amount
                    }


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