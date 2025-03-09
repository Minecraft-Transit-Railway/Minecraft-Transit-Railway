package org.mtr.model.render.object;

public final class IndexBuffer extends VertexBuffer {

	private int faceCount;
	private int vertexCount;
	public final int indexType;

	public IndexBuffer(int faceCount, int indexType) {
		this.faceCount = faceCount;
		this.vertexCount = faceCount * 3;
		this.indexType = indexType;
	}

	public void setFaceCount(int faceCount) {
		this.faceCount = faceCount;
		this.vertexCount = faceCount * 3;
	}

	public int getFaceCount() {
		return faceCount;
	}

	public int getVertexCount() {
		return vertexCount;
	}
}
