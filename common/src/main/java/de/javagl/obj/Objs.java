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

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Methods to create {@link Obj} instances
 */
public class Objs
{
    /**
     * Creates a new default {@link Obj}
     * 
     * @return The {@link Obj}
     */
    public static Obj create()
    {
        return new DefaultObj();
    }
    
    /**
     * Create an {@link Obj} from the given (single-) indexed triangle data.<br>
     * <br>
     * This method will not perform any sanity checks. It silently assumes
     * that the given indices are valid indices for the given buffers.
     * <br>
     * The texture coordinates and normals may be <code>null</code>.<br>
     * <br>
     * The buffers that are not <code>null</code> will be accessed using
     * the <b>absolute</b> access methods, up to their capacity. This means 
     * that the position of the given buffers will not be affected. 
     * 
     * @param indices The indices. Three consecutive elements of this buffer
     * are assumed to form one triangle.
     * @param vertices The vertices
     * @param texCoords The texture coordinates, assumed to be 2D.
     * @param normals The normals
     * @return The {@link Obj}
     * @throws NullPointerException If the indices or vertices are
     * <code>null</code>.
     */
    public static Obj createFromIndexedTriangleData(
        IntBuffer indices, 
        FloatBuffer vertices, 
        FloatBuffer texCoords, 
        FloatBuffer normals)
    {
        int numTriangles = indices.capacity() / 3;
        int numVertices = vertices.capacity() / 3;

        Obj obj = Objs.create();
        
        for (int i=0; i<numVertices; i++)
        {
            float x = vertices.get(i * 3 + 0);
            float y = vertices.get(i * 3 + 1);
            float z = vertices.get(i * 3 + 2);
            obj.addVertex(x, y, z);
        }
        
        if (texCoords != null)
        {
            int numTexCoords = texCoords.capacity() / 2;
            for (int i=0; i<numTexCoords; i++)
            {
                float x = texCoords.get(i * 2 + 0);
                float y = texCoords.get(i * 2 + 1);
                obj.addTexCoord(x, y);
            }
        }
        if (normals != null)
        {
            int numNormals = normals.capacity() / 3;
            for (int i=0; i<numNormals; i++)
            {
                float x = normals.get(i * 3 + 0);
                float y = normals.get(i * 3 + 1);
                float z = normals.get(i * 3 + 2);
                obj.addNormal(x, y, z);
            }
        }
        
        for (int i=0; i<numTriangles; i++)
        {
            int i0 = indices.get(i * 3 + 0);
            int i1 = indices.get(i * 3 + 1);
            int i2 = indices.get(i * 3 + 2);

            int[] v = { i0, i1, i2 };
            int[] vt = null;
            int[] vn = null;
            if (texCoords != null)
            {
                vt = v;
            }
            if (normals != null)
            {
                vn = v;
            }
            obj.addFace(v, vt, vn);
        }
        return obj;
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private Objs()
    {
        // Private constructor to prevent instantiation
    }

}
