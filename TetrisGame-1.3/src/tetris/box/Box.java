package tetris.box;


public class Box {
	/** 1.長條 */
	public final static String SHAPE1 = "0,0|0,1|0,2|0,3@0,0|1,0|2,0|3,0";
	/** 2.反L */
	public final static String SHAPE2 = "0,0|1,0|1,1|1,2@0,0|0,1|1,0|2,0@0,0|0,1|0,2|1,2@0,1|1,1|2,0|2,1";
	/** 3.L */
	public final static String SHAPE3 = "0,2|1,0|1,1|1,2@0,0|1,0|2,0|2,1@0,0|0,1|0,2|1,0@0,0|0,1|1,1|2,1";
	/** 4.正方 */
	public final static String SHAPE4 = "0,0|0,1|1,0|1,1";
	/** 5.正閃電 */
	public final static String SHAPE5 = "0,1|0,2|1,0|1,1@0,0|1,0|1,1|2,1";
	/** 6.T型 */
	public final static String SHAPE6 = "0,1|1,0|1,1|1,2@0,1|1,1|1,2|2,1@1,0|1,1|1,2|2,1@0,1|1,0|1,1|2,1";
	/** 7.反閃電 */
	public final static String SHAPE7 = "0,0|0,1|1,1|1,2@0,1|1,0|1,1|2,0";

	private final static String[] SHAPE_LIST = { SHAPE1, SHAPE2, SHAPE3,
			SHAPE4, SHAPE5, SHAPE6, SHAPE7 };

	public int nowX; // 目前的x位置
	public int nowY; // 目前的y位置

	protected int nowturn; // 目前的轉向
	protected int shape; // 此方塊種類代碼
	protected int[][][] boxAry; // 此方塊轉向後的值
	protected int[][] Height_Wide; // 此方塊轉向後的高、寬

	public Box() {
		BoxBaseInit();
	}

	public void BoxBaseInit() {
		nowturn = 0;
		boxAry = new int[0][0][0];
		shape = 1;
		Height_Wide = new int[0][0];
	}

	public void resetXY() {
		nowX = 0;
		nowY = 0;
	}

	/**
	 * 指定shape建立方塊型狀 shape: 1.長條<BR>
	 * 2.反L<BR>
	 * 3.L <BR>
	 * 4.正方<BR>
	 * 5.正閃電 <BR>
	 * 6.T型<BR>
	 * 7.反閃電 <BR>
	 * 
	 * @param shape
	 */
	public void setBoxData(int s) {
		if (s < 0 || s > SHAPE_LIST.length) {
			System.out.println("設定的方塊型別不存在");
			return;
		}
		String data = SHAPE_LIST[s - 1];
		shape = s;
		setShapeData(data);
	}

	/**
	 * 設定方塊類型 "[形狀1]_高,寬@[形狀2]_高,寬..."<BR>
	 * 形狀:二維陣列位置1|二維陣列位置1|...<BR>
	 * 接受格式為"0,0|0,1|0,2|0,3@0,0|1,0|2,0|3,0"<BR>
	 * 
	 * @param data
	 *            SHAPE1 or SHAPE2, SHAPE3...
	 */
	public void setShapeData(String data) {
		String[] ary = data.split("[@]");
		boxAry = new int[ary.length][][];
		Height_Wide = new int[ary.length][2];

		for (int i = 0; i < ary.length; i++) {
			String[] box = ary[i].split("[|]");
			int h = 0;
			int w = 0;

			// 找尋方塊各方裡x最大格數與y的最大格數來當寬與高,找完之後因設定值為從0開始，需要再將高、寬各+1
			for (int j = 0; j < box.length; j++) {
				String[] bAry = box[j].split("[,]");
				int x = bAry[0].charAt(0) - '0';
				int y = bAry[1].charAt(0) - '0';

				if (x > h) {
					h = x;
				}
				if (y > w) {
					w = y;
				}
			}
			h++;
			w++;
			Height_Wide[i][0] = h;
			Height_Wide[i][1] = w;

			boxAry[i] = new int[h][w];

			for (int j = 0; j < box.length; j++) {
				String[] bAry = box[j].split("[,]");
				int x = bAry[0].charAt(0) - '0';
				int y = bAry[1].charAt(0) - '0';
				boxAry[i][x][y] = shape;
			}
		}
	}

	public int getShape() {
		return shape;
	}

	/**
	 * 取得目前有幾種方塊種類
	 * 
	 * @return
	 */
	public static int getShapeCount() {
		return SHAPE_LIST.length;
	}

	/**
	 * 設定目前轉向
	 * 
	 * @param n
	 */
	public void setTurn(int n) {
		nowturn = n;
	}

	/**
	 * 取得目前轉向
	 * 
	 * @return
	 */
	public int getTurn() {
		return nowturn;
	}

	public int nextTurn(int n) {
		int tmpTurn = nowturn;

		tmpTurn += n;
		if (tmpTurn < 0) {
			tmpTurn = getTrunKind() - 1;
		}

		tmpTurn %= getTrunKind();

		return tmpTurn;
	}

	/**
	 * 逆時針轉向
	 */
	public void turnLeft() {
		nowturn--;
		if (nowturn < 0) {
			nowturn = getTrunKind() - 1;
		}
	}

	/**
	 * 順時針轉向
	 * 
	 */
	public void turnRight() {
		nowturn++;
		nowturn %= getTrunKind();
	}

	/**
	 * 取得方塊有幾種轉向
	 * 
	 * @return
	 */
	public int getTrunKind() {
		return boxAry.length;
	}

	/**
	 * 取得指定轉向的寬
	 * 
	 * @param n
	 * @return
	 */
	public int getWight(int n) {
		if (n >= 0 && n < Height_Wide.length) {
			return Height_Wide[n][1];
		}
		return 0;
	}

	/**
	 * 取得目前轉向的寬
	 * 
	 * @return
	 */
	public int getNowTurnWight() {
		return getWight(nowturn);
	}

	/**
	 * 取得指定轉向的高
	 * 
	 * @param n
	 * @return
	 */
	public int getHeight(int n) {
		if (n >= 0 && n < Height_Wide.length) {
			return Height_Wide[n][0];
		}
		return 0;
	}

	/**
	 * 取得目前轉向的高
	 * 
	 * @return
	 */
	public int getNowTurnHeight() {
		return getHeight(nowturn);
	}

	/**
	 * 取得目前方塊形狀
	 * 
	 * @return
	 */
	public int[][] getNowturnBoxAry() {
		return getBoxAry(nowturn);
	}

	/**
	 * 取得目前方塊形狀
	 * 
	 * @return
	 */
	public String getNowturnBoxShapeStr() {
		return getBoxShapeStr(nowturn);
	}

	/**
	 * 取得指定轉向的方塊形狀
	 * 
	 * @param index
	 * @return
	 */
	public int[][] getBoxAry(int index) {
		if (index >= 0 && index < boxAry.length) {
			return boxAry[index];
		}
		return null;
	}

	/**
	 * 取得指定轉向的方塊形狀(以字串格式)，例如:"[形狀1]_高,寬@[形狀2]_高,寬
	 * 
	 * @param index
	 * @return
	 */
	public String getBoxShapeStr(int index) {	
		if (index >= 0 && index < boxAry.length) {
			String data = SHAPE_LIST[shape - 1];
			String[] ary = data.split("[@]");
			return ary[index];
		}
		return "";
	}

	/**
	 * 移動x幾格，y幾格,並將舊的位置記下
	 * 
	 * @param x
	 * @param y
	 */
	public void move(int x, int y) {
		nowY += y;
		nowX += x;
	}

	/**
	 * 印出指定轉向的圖
	 * 
	 * @param index
	 */
	public void printBox(int index) {
		int tmp[][] = boxAry[index];

		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp[i].length; j++) {
				if (tmp[i][j] > 0) {
					System.out.print("口");
				} else {
					System.out.print("　");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	public void printNowturnBox() {
		printBox(nowturn);
	}


}
