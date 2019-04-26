package org.lp00579.www;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class EncryptionHandler {
	private BigInteger myPrivateKey;
	private BigInteger[] myPublicKey;
	private BigInteger[] connectedPublicKey;

	public EncryptionHandler() {
		super();
		genPublic();
	}
	
	public BigInteger getMyPrivateKey() {
		return myPrivateKey;
	}

	public BigInteger[] getConnectedPublicKey() {
		return connectedPublicKey;
	}

	public BigInteger[] getMyPublicKey() {
		return myPublicKey;
	}

	public void setConnectedPublicKey(BigInteger[] connectedPublicKey) {
		this.connectedPublicKey = connectedPublicKey;
	}

	private void genPublic() {
		BigInteger p = genPrime();
		BigInteger q = genPrime();
		BigInteger n = p.multiply(q);
		BigInteger one = BigInteger.valueOf(1);
		BigInteger phi = p.subtract(one).multiply(q.subtract(one));
		BigInteger e;

		Random rand = new SecureRandom();

		do {
			e = new BigInteger(phi.bitLength(), rand);
		} while (e.compareTo(BigInteger.ONE) <= 0 || e.compareTo(phi) >= 0 || !e.gcd(phi).equals(BigInteger.ONE));
		BigInteger d = e.modInverse(phi);

		this.myPublicKey = new BigInteger[] { n, e };
		genPrivate(phi, e);
	}

	private void genPrivate(BigInteger phi, BigInteger e) {
		BigInteger d = e.modInverse(phi);
		this.myPrivateKey = d;
	}

	private BigInteger genPrime() {
		return BigInteger.probablePrime(2048, new Random());
	}

	public String internalEncrypt(String clear) {
		StringBuilder char_ascii = new StringBuilder();
		for (int i = 0; i < clear.length(); i++) {
			char c = clear.charAt(i);
			char_ascii.append(String.format("%03d", Integer.parseInt(Integer.toString((int) c))));
		}
		BigInteger clear_ascii = new BigInteger(char_ascii.toString());
		BigInteger cipher_ascii = clear_ascii.modPow(this.myPublicKey[1], this.myPublicKey[0]);

		return cipher_ascii.toString();
	}

	public String encrypt(String clear) {
		StringBuilder char_ascii = new StringBuilder();
		for (int i = 0; i < clear.length(); i++) {
			char c = clear.charAt(i);
			char_ascii.append(String.format("%03d", Integer.parseInt(Integer.toString((int) c))));
		}
		BigInteger clear_ascii = new BigInteger(char_ascii.toString());
		BigInteger cipher_ascii = clear_ascii.modPow(this.connectedPublicKey[1], this.connectedPublicKey[0]);

		return cipher_ascii.toString();
	}

	public String decrypt(String cipher) {
		BigInteger cipher_big = new BigInteger(cipher);
		BigInteger decrypted_big_ascii = cipher_big.modPow(myPrivateKey, this.myPublicKey[0]);
		String decrypted_ascii = decrypted_big_ascii.toString();
		if (decrypted_ascii.length() % 3 != 0) {
			decrypted_ascii = "0" + decrypted_ascii;
		}
		String[] letters = decrypted_ascii.split("(?<=\\G...)");
		StringBuilder ret_str = new StringBuilder();
		for (String str : letters) {
			char c = (char) (Integer.parseInt(str));
			ret_str.append(c);
		}

		return ret_str.toString();
	}
	
	private String crop(BigInteger bigInteger_int) {
		String bigInteger = bigInteger_int.toString();
		if (bigInteger.length() > 50) {
			return bigInteger.substring(0, 50) + "...";
		} else {
			return bigInteger;
		}
	}
	
	public String display() {
		StringBuilder rsa = new StringBuilder("RSA Details:\n\n");
			rsa.append("Your Public Key: \n" + crop(getMyPublicKey()[0])
					+ crop(getMyPublicKey()[1]) + "\n\n");
			rsa.append("Client Public Key: \n" + crop(getConnectedPublicKey()[0])
					+ crop(getConnectedPublicKey()[1]) + "\n\n");
			rsa.append("Your Private Key: \n" + crop(getMyPrivateKey()) + "\n\n");

		return rsa.toString().replaceAll("(.{100})", "$1\n");
	}
}
