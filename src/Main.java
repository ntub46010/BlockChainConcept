import java.util.ArrayList;
import java.util.Date;

public class Main {
	private static ArrayList<Block> blockChain;
	
	public static void main(String[] args) {
		blockChain = new ArrayList<Block>();
		String text = "At GitHub we build the tools that make collaborating and writing software easier for everyone. "
				+ "We��ve built a company we truly love working for, and we think you will too. "
				+ "Join us as an intern, and you��ll be exposed to a enriching environment and unforgettable learning experience.";
		
		int startIndex = 0, endIndex, section = 32;		
		for (int i = 0; i < text.length(); i ++) {
			//�w�q�e�@�Ӱ϶���Hash�A����l�Ƹ��
			String preHash = blockChain.isEmpty() ? null : blockChain.get(i - 1).getHash();
			String data;
			
			//�w�q�ҭn�^����ƪ����ް϶�
			endIndex = startIndex + section;
			
			if (endIndex <= text.length()) {
				data = text.substring(startIndex, endIndex); //�^�����
				blockChain.add(new Block(preHash, data)); //�[�J�϶���
			}else {
				data = text.substring(startIndex, text.length()); //�^���Ѿl���
				blockChain.add(new Block(preHash, data));
				break;
			}
			
			startIndex = endIndex; 
		}

		//��϶�����q�A�W�[�w����
		mineBlockChain();
		
		//�B�z�����A�L�X��Ӱ϶�����T
		printBlockChain();
		
		//���Ұ϶���O�_�X�z
		System.out.println(isChainValid());
		
		//�i��«����
		System.out.println("�}�l«��I");	
		blockChain.get(1).setData("AAA");
		blockChain.get(2).setData("BBB");
		blockChain.get(4).setData("CCC");
		blockChain.get(6).setData("DDD");
		blockChain.get(8).setData("EEE");
				
		//���F��«��᪺�o�@���϶���X�z�ơA�ݶi����q�A�ϦU�϶���Hash����@��@������
		mineBlockChain();
		
		//�L�X«��᪺�϶����T
		printBlockChain();
		
		//���Ұ϶���O�_�X�z
		System.out.println(isChainValid());
	}
	
	public static void printBlockChain() {
		Block b;
		for (int i= 0; i < blockChain.size(); i++) {
			b = blockChain.get(i);
			System.out.println(String.format("%d\tpreHash�G%s\tdata�G%s\thash�G%s", 
					i + 1, b.getPreHash(), b.getData(), b.getHash()));
		}
	}
	
	public static Boolean isChainValid(){
		Block curBlock, preBlock;
		
		curBlock = blockChain.get(0);
		
		if (!curBlock.isHashHeadCorrect()) {
			System.out.println("��1�Ӱ϶������T�G���ҽX���~");
			return false;
		}
		if (!curBlock.calculateHash().equals(curBlock.getHash())) {
			System.out.println("��1�Ӱ϶������T�G�p��Hash�P���UHash���P");
			return false;
		}
		System.out.println("��1�Ӱ϶����T");
		
		for (int i = 1; i < blockChain.size(); i++) {
			curBlock = blockChain.get(i);
			preBlock = blockChain.get(i - 1);
			
			if (!curBlock.isHashHeadCorrect()) {
				System.out.println("��" + i + "�Ӱ϶������T�G���ҽX���~");
				return false;
			}
			
			if (!preBlock.calculateHash().equals(preBlock.getHash())) {
				System.out.println("��" + i + "�Ӱ϶������T�G�p��Hash�P���UHash���P");
				return false;
			}
			
			if (!curBlock.getPreHash().equals(preBlock.getHash())) {
				System.out.println("��" + (i + 1) + "�P��" + i + "�Ӱ϶�" + "�����T�GHash�ǰt���~");
				return false;
			}
			
			if (!curBlock.calculateHash().equals(curBlock.getHash())) {
				System.out.println("��" + (i + 1) + "�Ӱ϶������T�G�p��Hash�P���UHash���P");
				return false;
			}
			
			System.out.println("��" + (i + 1) + "�Ӱ϶����T");
		}
		
		System.out.println("��Ӱ϶��쥿�T�I");
		return true;	
	}
		
	public static void mineBlockChain() {
		long startMill, totalStartMill = new Date().getTime();;
		Block block;		
		
		for (int i = 0; i < blockChain.size(); i++) {
			block = blockChain.get(i);
			
			startMill = new Date().getTime();
			block.mine();
			System.out.println("��" + (i + 1) + "�Ӱ϶����q�����A�O��" + (new Date().getTime() - startMill) + "�@��");
			
			if (i + 1 < blockChain.size())
				blockChain.get(i + 1).setPreHash(block.getHash());			
		}
		
		System.out.println("���q�����A�`�@�O��" + (new Date().getTime() - totalStartMill) + "�@��");
	}
}
