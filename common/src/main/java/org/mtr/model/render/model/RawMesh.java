package org.mtr.model.render.model;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.mtr.MTR;
import org.mtr.model.OptimizedModel;
import org.mtr.model.render.batch.MaterialProperties;
import org.mtr.model.render.object.IndexBuffer;
import org.mtr.model.render.object.VertexBuffer;
import org.mtr.model.render.tool.OffHeapAllocator;
import org.mtr.model.render.tool.Utilities;
import org.mtr.model.render.vertex.Vertex;
import org.mtr.model.render.vertex.VertexAttributeMapping;
import org.mtr.model.render.vertex.VertexAttributeSource;
import org.mtr.model.render.vertex.VertexAttributeType;

import java.nio.ByteBuffer;
import java.util.stream.IntStream;

public final class RawMesh {

	public final MaterialProperties materialProperties;
	public final ObjectArrayList<Vertex> vertices = new ObjectArrayList<>();
	public final ObjectArrayList<Face> faces = new ObjectArrayList<>();

	public RawMesh(MaterialProperties materialProperties) {
		this.materialProperties = materialProperties;
	}

	public RawMesh(OptimizedModel.ShaderType shaderType, RawMesh rawMesh) {
		if (rawMesh.materialProperties.shaderType == OptimizedModel.ShaderType.CUTOUT) {
			materialProperties = new MaterialProperties(shaderType, rawMesh.materialProperties.getTexture(), rawMesh.materialProperties.vertexAttributeState.color);
		} else {
			materialProperties = rawMesh.materialProperties;
		}
		rawMesh.vertices.forEach(vertex -> vertices.add(new Vertex(vertex)));
		rawMesh.faces.forEach(face -> faces.add(new Face(face)));
	}

	public void append(RawMesh nextMesh) {
		if (nextMesh == this) {
			final IllegalStateException e = new IllegalStateException("Mesh self-appending");
			MTR.LOGGER.error("", e);
			throw e;
		}
		final int vertOffset = vertices.size();
		vertices.addAll(nextMesh.vertices);
		nextMesh.faces.forEach(face -> {
			final Face newFace = new Face(face);
			for (int i = 0; i < newFace.vertices.length; i++) {
				newFace.vertices[i] += vertOffset;
			}
			faces.add(newFace);
		});
	}

	public void clear() {
		vertices.clear();
		faces.clear();
	}

	public void addVertex(Vertex vertex) {
		vertices.add(vertex);
		if (vertices.size() % 4 == 0) {
			faces.add(new Face(IntStream.range(vertices.size() - 4, vertices.size()).toArray()));
		}
	}

	public boolean hasFaces() {
		return !faces.isEmpty();
	}

	public void validateVertexIndex() {
		for (final Face face : faces) {
			for (final int vertexIndex : face.vertices) {
				if (vertexIndex < 0 || vertexIndex >= vertices.size()) {
					final IndexOutOfBoundsException e = new IndexOutOfBoundsException(String.format("RawMesh contains invalid vertex index %s (should be 0 to %s)", vertexIndex, vertices.size() - 1));
					MTR.LOGGER.error("", e);
					throw e;
				}
			}
		}
	}

	public void triangulate() {
		final ObjectArrayList<Face> newFaces = new ObjectArrayList<>();
		faces.forEach(face -> newFaces.addAll(Face.triangulate(face.vertices)));
		faces.clear();
		faces.addAll(newFaces);
	}

	/**
	 * Removes duplicate vertices and faces from the mesh.
	 */
	public void distinct() {
		final ObjectArrayList<Vertex> distinctVertices = new ObjectArrayList<>(vertices.size());
		final Object2ObjectOpenHashMap<Vertex, Integer> verticesLookup = new Object2ObjectOpenHashMap<>(vertices.size());
		final ObjectOpenHashSet<Face> distinctFaces = new ObjectOpenHashSet<>(faces.size());

		faces.forEach(face -> {
			for (int i = 0; i < face.vertices.length; i++) {
				final Vertex vertex = vertices.get(face.vertices[i]);
				final int newIndex;
				if (verticesLookup.containsKey(vertex)) {
					newIndex = verticesLookup.get(vertex);
				} else {
					distinctVertices.add(vertex);
					newIndex = distinctVertices.size() - 1;
					verticesLookup.put(vertex, newIndex);
				}
				face.vertices[i] = newIndex;
			}
			distinctFaces.add(face);
		});

		vertices.clear();
		vertices.addAll(distinctVertices);
		faces.clear();
		faces.addAll(distinctFaces);
	}

	/**
	 * Generates normals for vertices without a normal vector. Produces duplicate vertices.
	 */
	public void generateNormals() {
		final ObjectArrayList<Vertex> newVertices = new ObjectArrayList<>(vertices.size());
		faces.forEach(face -> {
			if (face.vertices.length >= 3) {
				final int i0 = face.vertices[0];
				final int i1 = face.vertices[1];
				final int i2 = face.vertices[2];
				final double ax = vertices.get(i1).position.x - vertices.get(i0).position.x;
				final double ay = vertices.get(i1).position.y - vertices.get(i0).position.y;
				final double az = vertices.get(i1).position.z - vertices.get(i0).position.z;
				final double bx = vertices.get(i2).position.x - vertices.get(i0).position.x;
				final double by = vertices.get(i2).position.y - vertices.get(i0).position.y;
				final double bz = vertices.get(i2).position.z - vertices.get(i0).position.z;
				final double nx = ay * bz - az * by;
				final double ny = az * bx - ax * bz;
				final double nz = ax * by - ay * bx;
				final double t1 = nx * nx + ny * ny + nz * nz;
				if (t1 != 0) {
					double t2 = 1 / Math.sqrt(t1);
					final float mx = (float) (nx * t2);
					final float my = (float) (ny * t2);
					final float mz = (float) (nz * t2);
					for (int j = 0; j < face.vertices.length; j++) {
						final Vertex newVertex = new Vertex(vertices.get(face.vertices[j]));
						if (vectorIsZero(newVertex.normal)) {
							newVertex.normal = new Vector3f(mx, my, mz);
						}
						newVertices.add(newVertex);
						face.vertices[j] = newVertices.size() - 1;
					}
				} else {
					for (int i = 0; i < face.vertices.length; i++) {
						final Vertex newVertex = new Vertex(vertices.get(face.vertices[i]));
						if (vectorIsZero(vertices.get(face.vertices[i]).normal)) {
							newVertex.normal = new Vector3f(0, 1, 0);
						}
						newVertices.add(newVertex);
						face.vertices[i] = newVertices.size() - 1;
					}
				}
			}
		});
		vertices.clear();
		vertices.addAll(newVertices);
	}

	public void upload(Mesh mesh, VertexAttributeMapping mapping) {
		distinct();

		final ByteBuffer byteBuffer = OffHeapAllocator.allocate(vertices.size() * mapping.strideVertex);
		vertices.forEach(vertex -> {
			if (shouldWriteVertexBuffer(mapping, VertexAttributeType.POSITION)) {
				final Vector3f position = vertex.position;
				byteBuffer.putFloat(position.x).putFloat(position.y).putFloat(position.z);
			}
			if (shouldWriteVertexBuffer(mapping, VertexAttributeType.COLOR)) {
				byteBuffer.putInt(vertex.color);
			}
			if (shouldWriteVertexBuffer(mapping, VertexAttributeType.UV_TEXTURE)) {
				byteBuffer.putFloat(vertex.u).putFloat(vertex.v);
			}
			if (shouldWriteVertexBuffer(mapping, VertexAttributeType.UV_LIGHTMAP)) {
				byteBuffer.putInt(vertex.light);
			}
			if (shouldWriteVertexBuffer(mapping, VertexAttributeType.NORMAL)) {
				final Vector3f normal = Utilities.copy(vertex.normal);
				normal.normalize();
				byteBuffer.put((byte) (normal.x * 0x7F)).put((byte) (normal.y * 0x7F)).put((byte) (normal.z * 0x7F));
			}
			if (mapping.paddingVertex > 0) {
				byteBuffer.put((byte) 0);
			}
		});
		mesh.vertexBuffer.upload(byteBuffer, VertexBuffer.USAGE_STATIC_DRAW);
		OffHeapAllocator.free(byteBuffer);

		final ByteBuffer indexBuffer = OffHeapAllocator.allocate(faces.size() * 3 * 4);
		faces.forEach(face -> {
			for (int j = 0; j < face.vertices.length; j++) {
				indexBuffer.putInt(face.vertices[j]);
			}
		});
		mesh.indexBuffer.upload(indexBuffer, VertexBuffer.USAGE_STATIC_DRAW);
		mesh.indexBuffer.setFaceCount(faces.size());
		OffHeapAllocator.free(indexBuffer);
	}

	public Mesh upload(VertexAttributeMapping vertexAttributeMapping) {
		validateVertexIndex();
		final Mesh mesh = new Mesh(new VertexBuffer(), new IndexBuffer(faces.size(), GL11.GL_UNSIGNED_INT), materialProperties);
		upload(mesh, vertexAttributeMapping);
		return mesh;
	}

	private static boolean shouldWriteVertexBuffer(VertexAttributeMapping vertexAttributeMapping, VertexAttributeType vertexAttributeType) {
		return vertexAttributeMapping.sources.get(vertexAttributeType) == VertexAttributeSource.VERTEX_BUFFER;
	}

	private static int getVertexBufferPosition(VertexAttributeMapping vertexAttributeMapping, int vertexId, VertexAttributeType vertexAttributeType) {
		return vertexAttributeMapping.strideVertex * vertexId + vertexAttributeMapping.pointers.get(vertexAttributeType);
	}

	private static boolean vectorIsZero(Vector3f vector3f) {
		return vector3f.x == 0 && vector3f.y == 0 && vector3f.z == 0;
	}

	public void applyMatrix(Matrix4f matrix4f) {
		vertices.forEach(vertex -> {
			vertex.position = Utilities.transformPosition(matrix4f, vertex.position);
			vertex.normal = Utilities.transformDirection(matrix4f, vertex.normal);
		});
	}

	public void applyTranslation(float x, float y, float z) {
		vertices.forEach(vertex -> vertex.position.add(x, y, z));
	}

	public void applyRotation(Vector3f axis, float angle) {
		vertices.forEach(vertex -> {
			vertex.position.rotateAxis((float) Math.toRadians(angle), axis.x, axis.y, axis.z);
			vertex.normal.rotateAxis((float) Math.toRadians(angle), axis.x, axis.y, axis.z);
		});
	}

	public void applyScale(float x, float y, float z) {
		final float rx = 1 / x;
		final float ry = 1 / y;
		final float rz = 1 / z;
		final float rx2 = rx * rx;
		final float ry2 = ry * ry;
		final float rz2 = rz * rz;
		final boolean reverse = x * y * z < 0;
		vertices.forEach(vertex -> {
			vertex.position.mul(x, y, z);
			final float nx2 = vertex.normal.x * vertex.normal.x;
			final float ny2 = vertex.normal.y * vertex.normal.y;
			final float nz2 = vertex.normal.z * vertex.normal.z;
			final float u1 = nx2 * rx2 + ny2 * ry2 + nz2 * rz2;
			if (u1 != 0) {
				float u2 = (float) Math.sqrt((nx2 + ny2 + nz2) / u1);
				vertex.normal.mul(rx * u2, ry * u2, rz * u2);
			}
		});

		if (reverse) {
			faces.forEach(Face::flip);
		}
	}

	public void applyMirror(boolean vx, boolean vy, boolean vz, boolean nx, boolean ny, boolean nz) {
		vertices.forEach(vertex -> {
			vertex.position.mul(vx ? -1 : 1, vy ? -1 : 1, vz ? -1 : 1);
			vertex.normal.mul(nx ? -1 : 1, ny ? -1 : 1, nz ? -1 : 1);
		});

		int numFlips = 0;
		if (vx) {
			numFlips++;
		}
		if (vy) {
			numFlips++;
		}
		if (vz) {
			numFlips++;
		}

		if (numFlips % 2 != 0) {
			faces.forEach(Face::flip);
		}
	}

	public void applyUVMirror(boolean u, boolean v) {
		vertices.forEach(vertex -> {
			if (u) {
				vertex.u = 1 - vertex.u;
			}
			if (v) {
				vertex.v = 1 - vertex.v;
			}
		});
	}

	public void applyShear(Vector3f dir, Vector3f shear, float ratio) {
		vertices.forEach(vertex -> {
			final float n1 = ratio * (dir.x * vertex.position.x + dir.y * vertex.position.y + dir.z * vertex.position.z);
			final Vector3f offset1 = Utilities.copy(shear);
			offset1.mul(n1);
			vertex.position.add(offset1);
			if (!vectorIsZero(vertex.normal)) {
				final float n2 = ratio * (shear.x * vertex.normal.x + shear.y * vertex.normal.y + shear.z * vertex.normal.z);
				final Vector3f offset2 = Utilities.copy(dir);
				offset2.mul(-n2);
				vertex.normal.add(offset2);
				vertex.normal.normalize();
			}
		});
	}
}
