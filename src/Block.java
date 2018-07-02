import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class Block {
	private String hash, preHash, data;
	private long timestamp;
	private int nonce;
	
	public Block(String preHash, String data) {
		this.preHash = preHash;
		this.data = data;
		this.timestamp = new Date().getTime();
		this.hash = calculateHash(preHash + data + String.valueOf(timestamp) + String.valueOf(nonce));
	}
	
	public String getPreHash() {
		return preHash;
	}
	
	public String getData() {
		return data;
	}
	
	public String getHash() {
		return hash;
	}
	
	public void setData(String data) {
		this.data = data;
	}

	public void setPreHash(String preHash) {
		this.preHash = preHash;
	}	
	
	public String calculateHash(String s) {
		//此處以MD5代替SHA256
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes(), 0, s.length());
            BigInteger i = new BigInteger(1, m.digest());
            return String.format("%1$032x", i).toUpperCase();
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
	
	public String calculateHash() {
		//Hash的計算是將該區塊所有資料組合起來，進行雜湊運運算
		return calculateHash(preHash + data + String.valueOf(timestamp) + String.valueOf(nonce));
	}
	
	public void mine() {
		//為了增加安全性，規定Hash的前幾位為數個0，藉由加入一隨機值來改變區塊的Hash
		hash = calculateHash();
		
		while (!isHashHeadCorrect()) {
			nonce ++;
			hash = calculateHash();
		}
	}
	
	public boolean isHashHeadCorrect() {
		return hash.substring(0, 4).equals("0000");
	}
}
