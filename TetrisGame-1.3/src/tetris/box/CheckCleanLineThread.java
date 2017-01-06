package tetris.box;

public class CheckCleanLineThread extends Thread {
	private boolean Run;
	private float sec = 0.1f;
	
	private CleanRow obj;

	public CheckCleanLineThread() {
		Run = true;
	
	}

	

	public void setObj(CleanRow o) {
		obj = o;
	}

	public void startThread() {
		start();
	}

	public void stopThread() {
		Run = false;
	}

	@Override
	public void run() {
		while (Run) {
			if (obj != null) {
				if (obj.isClean()) {
					obj.cleanLine();
					break;
				}
			}
			try {
				Thread.sleep((int) (sec * 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		close();
	}

	public void close() {
		obj = null;
	}

}
