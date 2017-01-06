package tetris;

import java.util.Random;

import tetris.box.Box;
import tetris.box.CheckCleanLineThread;
import tetris.box.CleanRow;
import tetris.box.GameBox;


public class GameLoop implements Runnable, CleanRow {
	
	private boolean CheckGameOver;  // 是否遊戲結束
	private boolean isClean;
	private boolean fastEnable;
	private CheckView check;
	private float fastSec;
	private float sec;
	private Random ran;
	private GameBox gameBox;
	private boolean isRun;
	private boolean isPause;
	private CheckCleanLineThread checkCleanThread;

	private int flag; // 目前使用的方塊位置
	private String[] shapeAry;

	public GameLoop() {
		isRun = true;
		ran = new Random();
		GameInit();
	}

	public void GameInit() {
		flag = 0;
		shapeAry = new String[0];
		sec = 0.2f;
		fastSec = 0.1f;
		gameBox = new GameBox();
		fastEnable = false;
		isPause = false;
		CheckGameOver = false;
		checkCleanThread = new CheckCleanLineThread();
		checkCleanThread.setObj(this);
		checkCleanThread.startThread();
		setBoxList(getRandBox(5));// 設定使用5組亂數排列方塊進行遊戲
		nextCreatBox();
	}

	public void startGame() {
		new Thread(this).start();
	}

	public void stopGame() {
		isRun = false;
	}

	@Override
	public void run() {
		while (isRun) {
			try {
				if (!isPause && !CheckGameOver) {// 沒有按暫停才可玩
					if (!gameBox.moveDown()) {// 方塊已到底停住,不能再往下移
						isClean = true;
					}
					putDelegateCode("repaint", "");
				}
				Thread.sleep((int) (1000 * (fastEnable ? fastSec : sec)));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		close();
	}

	/**
	 * 設定預載的方塊shape
	 * 
	 * @param boxList
	 */
	public void setBoxList(String boxList) {
		if (!boxList.equals("")) {
			shapeAry = boxList.split("[|]");
		}
	}

	/**
	 * 取得n組亂數排列方塊,例如取1組為:"1|5|4|3|2|6|7"
	 * 
	 * @param n
	 * @return
	 */
	public String getRandBox(int n) {
		int[] boxAry = new int[Box.getShapeCount()];
		StringBuffer shapeList = new StringBuffer();

		// 初始化可使用的方塊shape,目前為1~7
		for (int i = 1; i <= boxAry.length; i++) {
			boxAry[i - 1] = i;
		}

		// 將1~7方塊亂數排列後，轉成字串回傳
		for (int i = 0; i < n; i++) {
			int[] ary = ranBoxAry(boxAry);
			for (int j = 0; j < ary.length; j++) {
				if (shapeList.length() > 0) {
					shapeList.append("|");
				}
				shapeList.append(ary[j]);
			}
		}

		return shapeList.toString();
	}

	/**
	 * 亂數打亂方塊排列
	 * 
	 * @param ary
	 * @return
	 */
	private int[] ranBoxAry(int[] ary) {
		for (int i = 0; i < ary.length; i++) {
			int tmpIndex = ran.nextInt(ary.length);
			int shape = ary[tmpIndex];
			ary[tmpIndex] = ary[i];
			ary[i] = shape;
		}
		return ary;
	}

	/**
	 * 取出預載方塊buffer下一個方塊
	 * 
	 * @return
	 */
	public int nextBox() {
		int shape = 0;
		if (flag > shapeAry.length - 1) {
			flag = 0;
		}
		shape = Integer.parseInt(shapeAry[flag]);
		flag++;
		return shape;
	}

	/**
	 * 取出預載方塊buffer n個方塊
	 * 
	 * @param n
	 * @return
	 */
	public String[] getN_box(int n) {
		String[] nBox = new String[n];
		int tmpFlag = flag;

		for (int i = 0; i < n; i++) {
			if (tmpFlag > shapeAry.length - 1) {
				tmpFlag = 0;
			}
			nBox[i] = shapeAry[tmpFlag];
			tmpFlag++;
		}
		return nBox;
	}

	/**
	 * 控制方塊下移1格
	 */
	public boolean moveDown() {
		return gameBox.moveDown();
	}

	/**
	 * 控制方塊左移1格
	 */
	public boolean moveLeft() {
		if (isClean)
			return false;

		return gameBox.moveLeft();
	}

	/**
	 * 控制方塊右移1格
	 */
	public boolean moveRight() {
		if (isClean)
			return false;

		return gameBox.moveRight();
	}

	/**
	 * 控制方塊順轉1次
	 */
	public boolean turnLeft() {
		if (isClean)
			return false;

		return gameBox.turnLeft();
	}

	/**
	 * 控制方塊逆轉1次
	 */
	public boolean turnRight() {
		if (isClean)
			return false;

		return gameBox.turnRight();
	}

	/**
	 * 方塊直接掉落到定位
	 */
	public void quickDown() {
		if (isClean)
			return;
		gameBox.quickDown();
		isClean = true;
	}


	/**
	 * 新增垃圾方塊,一次新增一排,同一種shape
	 * 
	 * @param shape
	 *            新增的方塊shape
	 * @param lineCount
	 *            要新增幾行垃圾方塊
	 */
	public void addGarbageBox(int shape, int lineCount) {
		for (int i = 0; i < lineCount; i++) {
			gameBox.addGarbageBox_oneLine(shape);
		}
	}

	/**
	 * 新增垃圾方塊,一次新增一排,同一種style
	 * 
	 * @param styleList
	 *            新增的方塊style列表，格式為"1|2|3|4|5|6|0|0|2|3"
	 */
	public void addGarbageBox_oneLine(String shapeList) {
		gameBox.addGarbageBox_oneLine(shapeList);
	}

	/**
	 * 快速下降移動
	 * 
	 * @param b
	 *            true:啟用快移,false:關閉快移
	 */
	public void fastDown(boolean b) {
		fastEnable = b;
	}

	/**
	 * 遊戲暫停
	 */
	public void pause() {
		isPause = true;
	}

	/**
	 * 目前是否暫停中
	 * 
	 * @return
	 */
	public boolean isPause() {
		return isPause;
	}

	/**
	 * 繼續遊戲(有按暫停之後使用)
	 */
	public void rusme() {
		isPause = false;
	}

	/**
	 * 設定GameOver狀態,true:遊戲結束,false:遊戲未結束
	 * 
	 * @param b
	 * @return
	 */
	public void setGameOver(boolean b) {
		CheckGameOver = b;
	}

	/**
	 * 取得目前是否遊戲結束
	 * 
	 * @return
	 */
	public boolean CheckGameOver() {
		return CheckGameOver;
	}


	/**
	 * 設定代理者
	 * 
	 * @param o
	 */
	public void setDelegate(CheckView o) {
		check = o;
	}

	/**
	 * 設定目前方塊掉落等待秒數
	 * 
	 * @param s
	 */
	public void setSec(float s) {
		sec = s;
	}

	/**
	 * 取得目前方塊掉落等待秒數
	 * 
	 * @return
	 */
	public float getSec() {
		return sec;
	}

	/**
	 * 發送資料給代理者
	 * 
	 * @param code
	 * @param data
	 */
	public void putDelegateCode(String code, String data) {
		if (check != null) {
			check.tetrisEvent(code, data);
		}
	}

	/**
	 * 取得目前二維陣列裡，疊的方塊到第幾個位置，0~20個單位<BR>
	 * 必須在方塊落到底時呼叫(即GameLoop的cleanLine()被執行時)，才可得到正確的高度資料
	 * 
	 * @return
	 */
	public int getNowBoxIndex() {
		return gameBox.getNowBoxIndex();
	}

	/**
	 * 取得目前的整個遊戲畫面可移動方塊區域的二維陣列
	 */
	public int[][] getBoxAry() {
		return gameBox.getBoxAry();
	}

	/**
	 * 亂數產生方塊
	 */
	public boolean ranCreatBox() {
		int shape = ran.nextInt(Box.getShapeCount()) + 1;
		return gameBox.createBaseObj(shape);
	}

	/**
	 * 從buffer取方塊建立
	 * 
	 * @return
	 */
	public boolean nextCreatBox() {
		int shape = nextBox();
		return gameBox.createBaseObj(shape);
	}

	public int[][] createBox(int shape) {
		return gameBox.createBox(shape);
	}

	@Override
	public void cleanLine() {
		gameBox.addBox();
		putDelegateCode("repaint", "");
		putDelegateCode("boxDown", "");


		// 取得可消除的行數
		String lineData = gameBox.getClearLine();

		if (!lineData.equals("")) {
			putDelegateCode("willCleanLine", lineData);
			gameBox.clearLine(lineData);// 實際將可消除的方塊行數移除
			putDelegateCode("cleanLineOK", "");
		}

		putDelegateCode("garbageBox", lineData);

		boolean isOK = nextCreatBox();
		if (!isOK) {
			CheckGameOver = true;
			putDelegateCode("repaint", "");
			putDelegateCode("gameOver", "");
		}
		putDelegateCode("repaint", "");
		putDelegateCode("creatBox", "");
		isClean = false;
		checkCleanThread = new CheckCleanLineThread();
		checkCleanThread.setObj(this);
		checkCleanThread.startThread();
	}

	
	@Override
	public boolean isClean() {
		return isClean;
	}

	
	public void clearBox() {
		gameBox.clearBoxAry();
	}

	
	public int removeGarbageBox_oneLine(int count) {
		return gameBox.removeGarbageBox_oneLine(count);
	}

	
	public int[][] getNowBoxAry() {
		return gameBox.getNowBoxAry();
	}

	
	public int[] getNowBoxXY() {
		return gameBox.getNowBoxXY();
	}

	
	public int getDownY() {
		return gameBox.getDownY();
	}

	public void close() {
		checkCleanThread.stopThread();
		check = null;
		ran = null;
		checkCleanThread = null;
		gameBox = null;
		shapeAry = null;
	}

	
	public String getLineList(String lineData, boolean isGap) {
		return gameBox.getLineList(lineData, isGap);
	}

	

}
