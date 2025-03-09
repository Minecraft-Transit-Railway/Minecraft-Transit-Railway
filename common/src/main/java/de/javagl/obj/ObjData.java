/*
 * www.javagl.de - Obj
 *
 * Copyright (c) 2008-2015 Marco Hutter - http://www.javagl.de
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.obj;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * Methods to obtain the data from {@link ReadableObj}s as plain arrays
 * or buffers
 */
public class ObjData
{
    
    //=========================================================================
    // Number of vertices

    /**
     * Returns an array containing the number of vertices of the faces
     * of the given {@link ReadableObj}
     * 
     * @param obj The {@link ReadableObj}
     * @return The array containing the number of vertices of the faces
     */
    private static int[] getNumFaceVertices(ReadableObj obj)
    {
        int[] numVerticesOfFaces = new int[obj.getNumFaces()];
        for(int i = 0; i < obj.getNumFaces(); i++)
        {
            ObjFace face = obj.getFace(i);
            numVerticesOfFaces[i] = face.getNumVertices();
        }
        return numVerticesOfFaces;
    }
    
    /**
     * Returns the sum of all numbers of vertices of all faces in the given 
     * {@link ReadableObj}. If the given {@link ReadableObj} only contains 
     * triangles, this will be the same as <code>obj.getNumFaces() * 3</code>.
     * 
     * @param obj The {@link ReadableObj}
     * @return The number of face vertex indices
     */
    public static int getTotalNumFaceVertices(ReadableObj obj)
    {
        return sum(getNumFaceVertices(obj));
    }
    
    /**
     * Returns the sum of the elements of the given array
     * 
     * @param array The array
     * @return The sum of the elements of the given array
     */
    private static int sum(int[] array)
    {
        int sum = 0;
        for (int i : array)
        {
            sum += i;
        }
        return sum;
    }
    
    //=========================================================================
    // Vertex indices
    
    
    /**
     * Returns the vertex indices from the faces of the given 
     * {@link ReadableObj} as an array. <br>
     * <br>
     * This method will compute the 
     * {@link #getTotalNumFaceVertices(ReadableObj) number of face vertex 
     * indices} and return an array with this size. If the number of vertices 
     * per face is known and equal for all faces, 
     * {@link #getFaceVertexIndices(ReadableObj,int)} may be used instead. 
     * 
     * @param obj The obj
     * @return The face vertex indices
     */
    public static int[] getFaceVertexIndicesArray(ReadableObj obj)
    {
        int[] array = new int[getTotalNumFaceVertices(obj)];
        getFaceVertexIndices(obj, IntBuffer.wrap(array));
        return array;
    }

    /**
     * Returns the vertex indices from the faces of the given 
     * {@link ReadableObj} as direct IntBuffer. The position 
     * of the returned buffer will be 0, and its limit and
     * capacity will match the stored data.<br>
     * <br>
     * This method will compute the 
     * {@link #getTotalNumFaceVertices(ReadableObj) number of face vertex 
     * indices} and return a buffer with this size. If the number of vertices 
     * per face is known and equal for all faces, 
     * {@link #getFaceVertexIndices(ReadableObj,int)} may be used instead. 
     * 
     * @param obj The obj
     * @return The face vertex indices
     */
    public static IntBuffer getFaceVertexIndices(ReadableObj obj)
    {
        IntBuffer buffer = createDirectIntBuffer(getTotalNumFaceVertices(obj));
        getFaceVertexIndices(obj, buffer);
        buffer.position(0);
        return buffer;
    }
    
    /**
     * Returns the vertex indices from the faces of the given 
     * {@link ReadableObj} as an array. <br>
     * <br>
     * This method assumes that all faces have the given number of vertices
     * 
     * @param obj The {@link ReadableObj}
     * @param numVerticesPerFace The number of vertices per face
     * @return The face vertex indices
     */
    public static int[] getFaceVertexIndicesArray(
        ReadableObj obj, int numVerticesPerFace)
    {
        int[] array = new int[obj.getNumFaces() * numVerticesPerFace];
        getFaceVertexIndices(obj, IntBuffer.wrap(array));
        return array;
    }
    
    /**
     * Returns the vertex indices from the faces of the given 
     * {@link ReadableObj} as direct IntBuffer. The position 
     * of the returned buffer will be 0, and its limit and
     * capacity will match the stored data.<br>
     * <br>
     * This method assumes that all faces have the given number of vertices
     * 
     * @param obj The {@link ReadableObj}
     * @param numVerticesPerFace The number of vertices per face
     * @return The face vertex indices
     */
    public static IntBuffer getFaceVertexIndices(
        ReadableObj obj, int numVerticesPerFace)
    {
        IntBuffer buffer = createDirectIntBuffer(
            obj.getNumFaces() * numVerticesPerFace);
        getFaceVertexIndices(obj, buffer);
        buffer.position(0);
        return buffer;
    }
    
    /**
     * Stores the vertex indices of the faces of the given 
     * {@link ReadableObj} in the given buffer. The position
     * of the given buffer will be advanced accordingly.<br>
     * <br>
     * This method assumes that the given buffer is sufficiently large
     * to store all indices. The required size may be computed with
     * {@link #getTotalNumFaceVertices(ReadableObj)}, or, if the number 
     * of vertices per face is known and equal for all faces, with 
     * <code>n = obj.getNumFaces() * numVerticesPerFace</code>
     * 
     * @param obj The {@link ReadableObj}
     * @param target The buffer that will store the result
     * @throws BufferOverflowException If the buffer can not store the result
     */
    public static void getFaceVertexIndices(
        ReadableObj obj, IntBuffer target)
    {
        for(int i = 0; i < obj.getNumFaces(); i++)
        {
            ObjFace face = obj.getFace(i);
            for (int j=0; j<face.getNumVertices(); j++)
            {
                target.put(face.getVertexIndex(j));
            }
        }
    }
    
    
    
    //=========================================================================
    // TexCoord indices

    /**
     * Returns the texCoord indices from the faces of the given 
     * {@link ReadableObj} as an array. <br>
     * <br>
     * This method will compute the 
     * {@link #getTotalNumFaceVertices(ReadableObj) number of face vertices} 
     * and return an array with this size. If the number of vertices per
     * face is known and equal for all faces, 
     * {@link #getFaceTexCoordIndices(ReadableObj,int)} may be used instead. 
     * 
     * @param obj The obj
     * @return The face texCoord indices
     */
    public static int[] getFaceTexCoordIndicesArray(ReadableObj obj)
    {
        int[] array = new int[getTotalNumFaceVertices(obj)];
        getFaceTexCoordIndices(obj, IntBuffer.wrap(array));
        return array;
    }
    
    /**
     * Returns the texCoord indices from the faces of the given 
     * {@link ReadableObj} as a direct IntBuffer. The position 
     * of the returned buffer will be 0, and its limit and
     * capacity will match the stored data.<br>
     * <br>
     * This method will compute the 
     * {@link #getTotalNumFaceVertices(ReadableObj) number of face vertices} 
     * and return a buffer with this size. If the number of vertices per
     * face is known and equal for all faces, 
     * {@link #getFaceTexCoordIndices(ReadableObj,int)} may be used instead. 
     * 
     * @param obj The obj
     * @return The face texCoord indices
     */
    public static IntBuffer getFaceTexCoordIndices(ReadableObj obj)
    {
        IntBuffer buffer = createDirectIntBuffer(getTotalNumFaceVertices(obj));
        getFaceTexCoordIndices(obj, buffer);
        buffer.position(0);
        return buffer;
    }
    
    
    /**
     * Returns the texCoord indices from the faces of the given 
     * {@link ReadableObj} as an array. <br>
     * <br>
     * This method assumes that all faces have the given number of vertices.<br>
     * <br>
     * This method assumes that the faces contain texture coordinate indices.
     * 
     * @param obj The {@link ReadableObj}
     * @param numVerticesPerFace The number of vertices per face
     * @return The face texCoord indices
     */
    public static int[] getFaceTexCoordIndicesArray(
        ReadableObj obj, int numVerticesPerFace)
    {
        int[] array = new int[obj.getNumFaces() * numVerticesPerFace];
        getFaceTexCoordIndices(obj, IntBuffer.wrap(array));
        return array;
    }
    
    
    /**
     * Returns the texCoord indices from the faces of the given 
     * {@link ReadableObj} as a direct IntBuffer. The position 
     * of the returned buffer will be 0, and its limit and
     * capacity will match the stored data.<br>
     * <br>
     * This method assumes that all faces have the given number of vertices.<br>
     * <br>
     * This method assumes that the faces contain texture coordinate indices.
     * 
     * @param obj The {@link ReadableObj}
     * @param numVerticesPerFace The number of vertices per face
     * @return The face texCoord indices
     */
    public static IntBuffer getFaceTexCoordIndices(
        ReadableObj obj, int numVerticesPerFace)
    {
        IntBuffer buffer = createDirectIntBuffer(
            obj.getNumFaces() * numVerticesPerFace);
        getFaceTexCoordIndices(obj, buffer);
        buffer.position(0);
        return buffer;
    }
    
    
    /**
     * Stores the texCoord indices of the faces of the given 
     * {@link ReadableObj} in the given buffer. The position
     * of the given buffer will be advanced accordingly.<br>
     * <br>
     * This method assumes that the given buffer is sufficiently large
     * to store all indices. The required size may be computed with
     * {@link #getTotalNumFaceVertices(ReadableObj)}, or, if the number of 
     * vertices per face is known and equal for all faces, with 
     * <code>n = obj.getNumFaces() * numVerticesPerFace</code>.<br>
     * <br>
     * This method assumes that the faces contain texture coordinate indices.
     * 
     * 
     * @param obj The {@link ReadableObj}
     * @param target The buffer that will store the result
     * @throws BufferOverflowException If the buffer can not store the result
     */
    public static void getFaceTexCoordIndices(
        ReadableObj obj, IntBuffer target)
    {
        for(int i = 0; i < obj.getNumFaces(); i++)
        {
            ObjFace face = obj.getFace(i);
            for (int j=0; j<face.getNumVertices(); j++)
            {
                target.put(face.getTexCoordIndex(j));
            }
        }
    }

    
    //=========================================================================
    // Normal indices

    /**
     * Returns the normal indices from the faces of the given 
     * {@link ReadableObj} as an array. <br>
     * <br>
     * This method will compute the 
     * {@link #getTotalNumFaceVertices(ReadableObj) number of face vertices} 
     * and return an array with this size. If the number of vertices per
     * face is known and equal for all faces, 
     * {@link #getFaceNormalIndices(ReadableObj,int)} may be used instead. 
     * 
     * @param obj The {@link ReadableObj}
     * @return The face normal indices
     */
    public static int[] getFaceNormalIndicesArray(ReadableObj obj)
    {
        int[] array = new int[getTotalNumFaceVertices(obj)];
        getFaceNormalIndices(obj, IntBuffer.wrap(array));
        return array;
    }

    /**
     * Returns the normal indices from the faces of the given 
     * {@link ReadableObj} as a direct IntBuffer. The position 
     * of the returned buffer will be 0, and its limit and
     * capacity will match the stored data.<br>
     * <br>
     * This method will compute the 
     * {@link #getTotalNumFaceVertices(ReadableObj) number of face vertices} 
     * and return a buffer with this size. If the number of vertices per
     * face is known and equal for all faces, 
     * {@link #getFaceNormalIndices(ReadableObj,int)} may be used instead. 
     * 
     * @param obj The {@link ReadableObj}
     * @return The face normal indices
     */
    public static IntBuffer getFaceNormalIndices(ReadableObj obj)
    {
        IntBuffer buffer = createDirectIntBuffer(getTotalNumFaceVertices(obj));
        getFaceNormalIndices(obj, buffer);
        buffer.position(0);
        return buffer;
    }
    
    
    /**
     * Returns the normal indices from the faces of the given 
     * {@link ReadableObj} as an array. <br>
     * <br>
     * This method assumes that all faces have the given number of vertices.<br>
     * <br>
     * This method assumes that the faces contain normal indices.
     * 
     * @param obj The {@link ReadableObj}
     * @param numVerticesPerFace The number of vertices per face
     * @return The face normal indices
     */
    public static int[] getFaceNormalIndicesArray(
        ReadableObj obj, int numVerticesPerFace)
    {
        int[] array = new int[obj.getNumFaces() * numVerticesPerFace];
        getFaceNormalIndices(obj, IntBuffer.wrap(array));
        return array;
    }

    
    /**
     * Returns the normal indices from the faces of the given 
     * {@link ReadableObj} as a direct IntBuffer. The position 
     * of the returned buffer will be 0, and its limit and
     * capacity will match the stored data.<br>
     * <br>
     * This method assumes that all faces have the given number of vertices.<br>
     * <br>
     * This method assumes that the faces contain normal indices.
     * 
     * @param obj The {@link ReadableObj}
     * @param numVerticesPerFace The number of vertices per face
     * @return The face normal indices
     */
    public static IntBuffer getFaceNormalIndices(
        ReadableObj obj, int numVerticesPerFace)
    {
        IntBuffer buffer = createDirectIntBuffer(
            obj.getNumFaces() * numVerticesPerFace);
        getFaceNormalIndices(obj, buffer);
        buffer.position(0);
        return buffer;
    }
    
    
    /**
     * Stores the normal indices of the faces of the given 
     * {@link ReadableObj} in the given buffer. The position
     * of the given buffer will be advanced accordingly.<br>
     * <br>
     * This method assumes that the given buffer is sufficiently large
     * to store all indices. The required size may be computed with
     * {@link #getTotalNumFaceVertices(ReadableObj)}, or, if the number of 
     * vertices per face is known and equal for all faces, with 
     * <code>n = obj.getNumFaces() * numVerticesPerFace</code>.<br>
     * <br>
     * This method assumes that the faces contain texture coordinate indices.
     * 
     * 
     * @param obj The {@link ReadableObj}
     * @param target The buffer that will store the result
     * @throws BufferOverflowException If the buffer can not store the result
     */
    public static void getFaceNormalIndices(
        ReadableObj obj, IntBuffer target)
    {
        for(int i = 0; i < obj.getNumFaces(); i++)
        {
            ObjFace face = obj.getFace(i);
            for (int j=0; j<face.getNumVertices(); j++)
            {
                target.put(face.getNormalIndex(j));
            }
        }
    }
    
    //=========================================================================
    // Vertices
    
    /**
     * Returns all vertices of the given {@link ReadableObj} as an array.
     * Three consecutive entries in the resulting array are the
     * x,y,z coordinates of one vertex
     * 
     * @param obj The {@link ReadableObj}
     * @return The resulting array
     */
    public static float[] getVerticesArray(ReadableObj obj)
    {
        float[] array = new float[obj.getNumVertices() * 3];
        getVertices(obj, FloatBuffer.wrap(array));
        return array;
    }

    /**
     * Returns all vertices of the given {@link ReadableObj} as a direct
     * FloatBuffer.
     * Three consecutive entries in the resulting buffer are the
     * x,y,z coordinates of one vertex. The position 
     * of the returned buffer will be 0, and its limit and
     * capacity will match the stored data.
     * 
     * @param obj The {@link ReadableObj}
     * @return The resulting buffer
     */
    public static FloatBuffer getVertices(ReadableObj obj)
    {
        FloatBuffer buffer = createDirectFloatBuffer(obj.getNumVertices() * 3);
        getVertices(obj, buffer);
        buffer.position(0);
        return buffer;
    }
    
    /**
     * Stores the vertices of the given {@link ReadableObj} in the given 
     * buffer. The position of the target will be increased by 
     * <code>obj.getNumVertices() * 3. The position
     * of the given buffer will be advanced accordingly.</code>
     * 
     * @param obj The obj
     * @param target The target that will store the result
     * @throws BufferOverflowException If the target can not store the result
     */
    public static void getVertices(
        ReadableObj obj, FloatBuffer target)
    {
        for(int i = 0; i < obj.getNumVertices(); i++)
        {
            FloatTuple tuple = obj.getVertex(i);
            target.put(tuple.getX());
            target.put(tuple.getY());
            target.put(tuple.getZ());
        }
    }
    
    
    
    
    //=========================================================================
    // TexCoords
    
    
    /**
     * Returns all texture coordinates of the given 
     * {@link ReadableObj} as an array.
     * 
     * @param obj The {@link ReadableObj}
     * @param dimensions The dimensions that are assumed for the coordinates
     * @return The resulting array
     */
    public static float[] getTexCoordsArray(ReadableObj obj, int dimensions)
    {
        return getTexCoordsArray(obj, dimensions, false);
    }

    /**
     * Returns all texture coordinates of the given 
     * {@link ReadableObj} as an array.
     * 
     * @param obj The {@link ReadableObj}
     * @param dimensions The dimensions that are assumed for the coordinates
     * @param flipY Whether the texture coordinates should be flipped 
     * vertically. This means that the y-coordinates (at dimension index 1)
     * will be replaced with <code>1.0f - y</code>. Most image loaders provide
     * image data with the first pixel being the <i>upper left</i> pixel of
     * the image. But OpenGL <code>glTexImage2D</code> calls expect the first
     * pixel to be the <i>lower left</i>. Flipping the texture coordinates
     * by passing <code>flipY=true</code> to this method allows to compensate
     * for this mismatch.  
     * @return The resulting array
     */
    public static float[] getTexCoordsArray(
        ReadableObj obj, int dimensions, boolean flipY)
    {
        float[] array = new float[obj.getNumTexCoords() * dimensions];
        getTexCoords(obj, FloatBuffer.wrap(array), dimensions, flipY);
        return array;
    }
    
    /**
     * Returns all texture coordinates of the given 
     * {@link ReadableObj} as direct FloatBuffer. The position 
     * of the returned buffer will be 0, and its limit and
     * capacity will match the stored data.
     * 
     * @param obj The {@link ReadableObj}
     * @param dimensions The dimensions that are assumed for the coordinates
     * @return The resulting buffer
     */
    public static FloatBuffer getTexCoords(ReadableObj obj, int dimensions)
    {
        return getTexCoords(obj, dimensions, false);
    }
    
    /**
     * Returns all texture coordinates of the given 
     * {@link ReadableObj} as direct FloatBuffer. The position 
     * of the returned buffer will be 0, and its limit and
     * capacity will match the stored data.
     * 
     * @param obj The {@link ReadableObj}
     * @param dimensions The dimensions that are assumed for the coordinates
     * @param flipY Whether the texture coordinates should be flipped 
     * vertically. This means that the y-coordinates (at dimension index 1)
     * will be replaced with <code>1.0f - y</code>. Most image loaders provide
     * image data with the first pixel being the <i>upper left</i> pixel of
     * the image. But OpenGL <code>glTexImage2D</code> calls expect the first
     * pixel to be the <i>lower left</i>. Flipping the texture coordinates
     * by passing <code>flipY=true</code> to this method allows to compensate
     * for this mismatch.  
     * @return The resulting buffer
     */
    public static FloatBuffer getTexCoords(
        ReadableObj obj, int dimensions, boolean flipY)
    {
        FloatBuffer buffer = 
            createDirectFloatBuffer(obj.getNumTexCoords() * dimensions);
        getTexCoords(obj, buffer, dimensions, flipY);
        buffer.position(0);
        return buffer;
    }
    

    /**
     * Stores the texture coordinates of the given {@link ReadableObj} 
     * in the given buffer. The position of the target will be increased by 
     * <code>obj.getNumTexCoords() * dimensions</code>. The position
     * of the given buffer will be advanced accordingly.
     * 
     * @param obj The {@link ReadableObj}
     * @param target The target that will store the result
     * @param dimensions The dimensions that are assumed for the coordinates
     * @throws BufferOverflowException If the target can not store the result
     */
    public static void getTexCoords(
        ReadableObj obj, FloatBuffer target, int dimensions)
    {
        getTexCoords(obj, target, dimensions, false);
    }

    /**
     * Stores the texture coordinates of the given {@link ReadableObj} 
     * in the given buffer. The position of the target will be increased by 
     * <code>obj.getNumTexCoords() * dimensions</code>. The position
     * of the given buffer will be advanced accordingly.
     * 
     * @param obj The {@link ReadableObj}
     * @param target The target that will store the result
     * @param dimensions The dimensions that are assumed for the coordinates
     * @param flipY Whether the texture coordinates should be flipped 
     * vertically. This means that the y-coordinates (at dimension index 1)
     * will be replaced with <code>1.0f - y</code>. Most image loaders provide
     * image data with the first pixel being the <i>upper left</i> pixel of
     * the image. But OpenGL <code>glTexImage2D</code> calls expect the first
     * pixel to be the <i>lower left</i>. Flipping the texture coordinates
     * by passing <code>flipY=true</code> to this method allows to compensate
     * for this mismatch.  
     * @throws BufferOverflowException If the target can not store the result
     */
    public static void getTexCoords(
        ReadableObj obj, FloatBuffer target, int dimensions, boolean flipY)
    {
        if (flipY) 
        {
            for(int i = 0; i < obj.getNumTexCoords(); i++)
            {
                FloatTuple tuple = obj.getTexCoord(i);
                for (int j=0; j<dimensions; j++)
                {
                    if (j == 1) 
                    {
                        target.put(1.0f - tuple.get(j));
                    }
                    else
                    {
                        target.put(tuple.get(j));
                    }
                }
            }
        }
        else
        {
            for(int i = 0; i < obj.getNumTexCoords(); i++)
            {
                FloatTuple tuple = obj.getTexCoord(i);
                for (int j=0; j<dimensions; j++)
                {
                    target.put(tuple.get(j));
                }
            }
        }
    }
    
    //=========================================================================
    // Normals
    
    /**
     * Returns all normals of the given {@link ReadableObj} as an array.
     * Three consecutive entries in the resulting array are the
     * x,y,z coordinates of one normal.
     * 
     * @param obj The {@link ReadableObj}
     * @return The resulting array
     */
    public static float[] getNormalsArray(ReadableObj obj)
    {
        float[] array = new float[obj.getNumNormals() * 3];
        getNormals(obj, FloatBuffer.wrap(array));
        return array;
    }

    /**
     * Returns all normals of the given {@link ReadableObj} as an array.
     * Three consecutive entries in the resulting buffer are the
     * x,y,z coordinates of one normal. The position 
     * of the returned buffer will be 0, and its limit and
     * capacity will match the stored data.
     * 
     * @param obj The {@link ReadableObj}
     * @return The resulting buffer
     */
    public static FloatBuffer getNormals(ReadableObj obj)
    {
        FloatBuffer buffer = createDirectFloatBuffer(obj.getNumNormals() * 3);
        getNormals(obj, buffer);
        buffer.position(0);
        return buffer;
    }
    
    /**
     * Stores the normals of the given {@link ReadableObj} in the given 
     * buffer. The position of the target will be increased by 
     * <code>obj.getNumNormals() * 3</code>. The position
     * of the given buffer will be advanced accordingly.
     * 
     * @param obj The {@link ReadableObj}
     * @param target The buffer that will store the result
     * @throws BufferOverflowException If the target can not store the result
     */
    public static void getNormals(
        ReadableObj obj, FloatBuffer target)
    {
        for(int i = 0; i < obj.getNumNormals(); i++)
        {
            FloatTuple tuple = obj.getNormal(i);
            target.put(tuple.getX());
            target.put(tuple.getY());
            target.put(tuple.getZ());
        }
    }
    
    /**
     * Convert the given IntBuffer to a (direct) ShortBuffer, by casting all
     * elements to <code>short</code>. <br>
     * <br>
     * Note that these casts will be unchecked. If there is any value in the 
     * input buffer that is larger than the maximum value that a 
     * <b>signed</b> <code>short</code> can represent, then the 
     * <code>short</code> value will become negative. The resulting buffer 
     * will then still be valid for passing it to OpenGL when the index
     * mode is <code>GL_UNSIGNED_SHORT</code>, because the bitwise 
     * representation is the same. When one of the integer values is larger 
     * than the value that can be represented with an <b>unsigned</b>
     * <code>short</code>, then the resulting indices will be invalid
     * and cause rendering artifacts.  
     * 
     * @param intBuffer The IntBuffer
     * @return The ShortBuffer
     */
    public static ShortBuffer convertToShortBuffer(IntBuffer intBuffer)
    {
        ShortBuffer shortBuffer = createDirectShortBuffer(intBuffer.capacity());
        for (int i = 0; i < intBuffer.capacity(); i++)
        {
            shortBuffer.put(i, (short) intBuffer.get());
        }
        return shortBuffer;
    }    
    
    /**
     * Create a direct IntBuffer with the given size
     * 
     * @param size The size 
     * @return The IntBuffer
     */
    private static IntBuffer createDirectIntBuffer(int size)
    {
        return ByteBuffer.allocateDirect(size * 4)
            .order(ByteOrder.nativeOrder())
            .asIntBuffer();
    }
    
    /**
     * Create a direct ShortBuffer with the given size
     * 
     * @param size The size 
     * @return The ShortBuffer
     */
    private static ShortBuffer createDirectShortBuffer(int size)
    {
        return ByteBuffer.allocateDirect(size * 2)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer();
    }

    /**
     * Create a direct FloatBuffer with the given size
     * 
     * @param size The size 
     * @return The FloatBuffer
     */
    private static FloatBuffer createDirectFloatBuffer(int size)
    {
        return ByteBuffer.allocateDirect(size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer();
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private ObjData()
    {
        // Private constructor to prevent instantiation
    }
}
