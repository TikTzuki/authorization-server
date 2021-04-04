package authorizationserver;

import static org.junit.Assert.assertNotNull;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Test;

import authorizationserver.util.Sha256;

public class HashValue {
	
	@Test
	public void initHashValue() {
		System.out.println("hash:"+ Double.toHexString(Math.sqrt(2)));
	}
	
	@Test
	public void testBlockCount() {
		int BLOCK_BITS = 512;
		int BLOCK_BYTES = BLOCK_BITS / 8;
		byte[] message = "Please write comments if you find anything incorrect, or you want to share more information about the topic discussed above.".getBytes();
		int finalBlockLength = message.length % BLOCK_BYTES;
		int blockCount = message.length / BLOCK_BYTES + (finalBlockLength + 1 + 8 > BLOCK_BYTES ? 2 : 1);
		System.out.println("block count:" + blockCount);
	}
	
	@Test
	public void pad () {
		System.out.println("pad");
		int[] a = Sha256.pad("length7".getBytes());
		for(int i=0; i<a.length; i++) {
			System.out.println(a[i]);
		}
		System.out.println("end pad");
	}
	
	@Test
	public void appendMessageLengthAs64BitBigEndianInteger() {
		String message = "length7";
		long messageLength = message.length() * 8L;
	}
	
	@Test
	public void jsonObject() {
		JSONObject object = new JSONObject();
		assertNotNull(object);
	}
	
	@AfterClass
	public static void afterClass() {
		System.out.println("after class");
	}
	
}
