package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;

public class MatrixStackHolder {

	private int pushCounter;
	private final PoseStack matrices;

	public MatrixStackHolder(PoseStack matrices) {
		this.matrices = matrices;
	}

	public void push() {
		pushCounter++;
		matrices.pushPose();
	}

	public void pop() {
		if (pushCounter > 0) {
			pushCounter--;
			matrices.popPose();
		}
	}

	public void popAll() {
		while (pushCounter > 0) {
			pop();
		}
	}
}
