package core.model;

import core.model.Vertex.VertexLayout;

public class Mesh{

	private Vertex[] vertices;
	private int[] indices;
	private int instances;
	private boolean isTangentSpace;
	private boolean isInstanced;
	private VertexLayout vertexLayout;
	
	public Mesh(Vertex[] vertices, int[] indices)
	{
		this.vertices = vertices;
		this.indices = indices;
	}

	public Vertex[] getVertices() {
		return vertices;
	}

	public void setVertices(Vertex[] vertices) {
		this.vertices = vertices;
	}

	public int[] getIndices() {
		return indices;
	}

	public void setIndices(int[] indices) {
		this.indices = indices;
	}

	public boolean isInstanced() {
		return isInstanced;
	}

	public void setInstanced(boolean instanced) {
		this.isInstanced = instanced;
	}

	public int getInstances() {
		return instances;
	}

	public void setInstances(int instances) {
		this.instances = instances;
	}

	public boolean isTangentSpace() {
		return isTangentSpace;
	}

	public void setTangentSpace(boolean isTangentSpace) {
		this.isTangentSpace = isTangentSpace;
	}

	public void setVertexLayout(VertexLayout vertexLayout) {
		this.vertexLayout = vertexLayout;
	}
}
