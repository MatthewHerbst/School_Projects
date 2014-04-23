import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SHA512 {	
	public final static String ALGORITHM = "SHA-512";
	public final int MAXIMUM_LENGTH = 64;
	public final static String ALPHABET = "herbstA1B2C3D4!E@5#F$6%G^&H*8(I)9_J-0+K?LMNOPQRSTUVWXYZ";
	static MessageDigest md;
	static char[] best;
	static String word;
	static char[] hash;
	static int most0 = 0;
	static int count = 0;
	
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        //TODO: does the code already go through all permutations of the alphabet?
		char[] chars = ALPHABET.toCharArray();
		
		//TODO: go through all lengths
		int len = 7;
        
        iterate(chars, len, new char[len], 0);
        
        System.out.println("Finished length " + len + ", alphabet: " + new String(chars));
        System.out.println("Lowest hash for " + len + ": " + new String(best));
    }

    public static void iterate(char[] chars, int len, char[] build, int pos) throws NoSuchAlgorithmException, IOException {
        if (pos == len) {
            word = new String(build); //TODO: Not sure if this is needed
            
            //Set up the digest to run the algorithm over the current word
            md = MessageDigest.getInstance(ALGORITHM);
            md.update(word.getBytes());
           
            byte[] mb = md.digest();
            String out = "";
            for (int i = 0; i < mb.length; i++) {
                byte temp = mb[i];
                String s = Integer.toHexString(new Byte(temp));
                while (s.length() < 2) {
                    s = "0" + s;
                }
                s = s.substring(s.length() - 2);
                out += s;
            }
            
            char[] temp = out.toCharArray();
            int num = 0;
            for(int i = 0; i < temp.length; ++i) {
            	if(temp[i] == '0') {
            		num++;
            	} else {
            		break;
            	}
            }
            
            if(num > most0) {
            	most0 = num;
            	best = hash;
            	System.out.println("String: " + word + " Digest: " + out);
            }
            count++;
            
            if(count % 10000000 == 0) {
            	System.out.println(count + " hashes completed.");
            }
            
            return;
        }

        for (int i = 0; i < chars.length; i++) {
            build[pos] = chars[i];
            iterate(chars, len, build, pos + 1);
        }
    }
}
