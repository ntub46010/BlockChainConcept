import java.util.ArrayList;
import java.util.Date;

public class Main {
	private static ArrayList<Block> blockChain;
	
	public static void main(String[] args) {
		blockChain = new ArrayList<Block>();
		String text = "At GitHub we build the tools that make collaborating and writing software easier for everyone. "
				+ "We’ve built a company we truly love working for, and we think you will too. "
				+ "Join us as an intern, and you’ll be exposed to a enriching environment and unforgettable learning experience.";
		
		int startIndex = 0, endIndex, section = 32;		
		for (int i = 0; i < text.length(); i ++) {
			//定義前一個區塊的Hash，鰾初始化資料
			String preHash = blockChain.isEmpty() ? null : blockChain.get(i - 1).getHash();
			String data;
			
			//定義所要擷取資料的索引區間
			endIndex = startIndex + section;
			
			if (endIndex <= text.length()) {
				data = text.substring(startIndex, endIndex); //擷取資料
				blockChain.add(new Block(preHash, data)); //加入區塊鏈
			}else {
				data = text.substring(startIndex, text.length()); //擷取剩餘資料
				blockChain.add(new Block(preHash, data));
				break;
			}
			
			startIndex = endIndex; 
		}

		//對區塊鏈挖礦，增加安全性
		mineBlockChain();
		
		//處理完成，印出整個區塊的資訊
		printBlockChain();
		
		//驗證區塊鏈是否合理
		System.out.println(isChainValid());
		
		//進行竄改資料
		System.out.println("開始竄改！");	
		blockChain.get(1).setData("AAA");
		blockChain.get(2).setData("BBB");
		blockChain.get(4).setData("CCC");
		blockChain.get(6).setData("DDD");
		blockChain.get(8).setData("EEE");
				
		//為了使竄改後的這一條區塊鏈合理化，需進行挖礦，使各區塊的Hash能夠一對一的對應
		mineBlockChain();
		
		//印出竄改後的區塊鏈資訊
		printBlockChain();
		
		//驗證區塊鏈是否合理
		System.out.println(isChainValid());
	}
	
	public static void printBlockChain() {
		Block b;
		for (int i= 0; i < blockChain.size(); i++) {
			b = blockChain.get(i);
			System.out.println(String.format("%d\tpreHash：%s\tdata：%s\thash：%s", 
					i + 1, b.getPreHash(), b.getData(), b.getHash()));
		}
	}
	
	public static Boolean isChainValid(){
		Block curBlock, preBlock;
		
		curBlock = blockChain.get(0);
		
		if (!curBlock.isHashHeadCorrect()) {
			System.out.println("第1個區塊不正確：驗證碼錯誤");
			return false;
		}
		if (!curBlock.calculateHash().equals(curBlock.getHash())) {
			System.out.println("第1個區塊不正確：計算Hash與註冊Hash不同");
			return false;
		}
		System.out.println("第1個區塊正確");
		
		for (int i = 1; i < blockChain.size(); i++) {
			curBlock = blockChain.get(i);
			preBlock = blockChain.get(i - 1);
			
			if (!curBlock.isHashHeadCorrect()) {
				System.out.println("第" + i + "個區塊不正確：驗證碼錯誤");
				return false;
			}
			
			if (!preBlock.calculateHash().equals(preBlock.getHash())) {
				System.out.println("第" + i + "個區塊不正確：計算Hash與註冊Hash不同");
				return false;
			}
			
			if (!curBlock.getPreHash().equals(preBlock.getHash())) {
				System.out.println("第" + (i + 1) + "與第" + i + "個區塊" + "不正確：Hash匹配錯誤");
				return false;
			}
			
			if (!curBlock.calculateHash().equals(curBlock.getHash())) {
				System.out.println("第" + (i + 1) + "個區塊不正確：計算Hash與註冊Hash不同");
				return false;
			}
			
			System.out.println("第" + (i + 1) + "個區塊正確");
		}
		
		System.out.println("整個區塊鏈正確！");
		return true;	
	}
		
	public static void mineBlockChain() {
		long startMill, totalStartMill = new Date().getTime();;
		Block block;		
		
		for (int i = 0; i < blockChain.size(); i++) {
			block = blockChain.get(i);
			
			startMill = new Date().getTime();
			block.mine();
			System.out.println("第" + (i + 1) + "個區塊挖礦完成，費時" + (new Date().getTime() - startMill) + "毫秒");
			
			if (i + 1 < blockChain.size())
				blockChain.get(i + 1).setPreHash(block.getHash());			
		}
		
		System.out.println("挖礦完成，總共費時" + (new Date().getTime() - totalStartMill) + "毫秒");
	}
}
