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

/**
 * Methods for creating {@link ObjFace} instances
 */
public class ObjFaces
{
    /**
     * Create a copy of the given face.<br> 
     * 
     * @param face The input face
     * @return The copy
     */
    static DefaultObjFace create(ObjFace face)
    {
        int[] v = new int[face.getNumVertices()];
        int[] vt = null;
        int[] vn = null;
        for(int i = 0; i < face.getNumVertices(); i++)
        {
            v[i] = face.getVertexIndex(i);
        }

        if(face.containsTexCoordIndices())
        {
            vt = new int[face.getNumVertices()];
            for(int i = 0; i < face.getNumVertices(); i++)
            {
                vt[i] = face.getTexCoordIndex(i);
            }
        }

        if(face.containsNormalIndices())
        {
            vn = new int[face.getNumVertices()];
            for(int i = 0; i < face.getNumVertices(); i++)
            {
                vn[i] = face.getNormalIndex(i);
            }
        }
        
        DefaultObjFace result = new DefaultObjFace(v, vt, vn);
        return result;
    }
    
    /**
     * Create a copy of the given face, adding the given offsets to the
     * respective indices. If the given face does not contain texture 
     * coordinate or normal indices, then the respective offsets will
     * be ignored.<br> 
     * 
     * @param face The input face
     * @param verticesOffset The offset for the vertex indices
     * @param texCoordsOffset  The offset for the texture coordinate indices
     * @param normalsOffset The offset for the normal indices
     * @return The copy
     */
    static DefaultObjFace createWithOffsets(ObjFace face, 
        int verticesOffset, int texCoordsOffset, int normalsOffset)
    {
        int[] v = new int[face.getNumVertices()];
        int[] vt = null;
        int[] vn = null;
        for(int i = 0; i < face.getNumVertices(); i++)
        {
            v[i] = face.getVertexIndex(i) + verticesOffset;
        }

        if(face.containsTexCoordIndices())
        {
            vt = new int[face.getNumVertices()];
            for(int i = 0; i < face.getNumVertices(); i++)
            {
                vt[i] = face.getTexCoordIndex(i) + texCoordsOffset;
            }
        }

        if(face.containsNormalIndices())
        {
            vn = new int[face.getNumVertices()];
            for(int i = 0; i < face.getNumVertices(); i++)
            {
                vn[i] = face.getNormalIndex(i) + normalsOffset;
            }
        }
        
        DefaultObjFace result = new DefaultObjFace(v, vt, vn);
        return result;
    }
    
    
    
    
    
    /**
     * Create a copy of the given face, using only the specified vertices 
     * of the given face.
     * 
     * @param face The input face
     * @param n The vertices to use
     * @return The copy
     */
    static DefaultObjFace create(ObjFace face, int ... n)
    {
        int[] v = new int[n.length];
        int[] vt = null;
        int[] vn = null;
        for(int i = 0; i < n.length; i++)
        {
            v[i] = face.getVertexIndex(n[i]);
        }
        if(face.containsTexCoordIndices())
        {
            vt = new int[n.length];
            for(int i = 0; i < n.length; i++)
            {
                vt[i] = face.getTexCoordIndex(n[i]);
            }
        }

        if(face.containsNormalIndices())
        {
            vn = new int[n.length];
            for(int i = 0; i < n.length; i++)
            {
                vn[i] = face.getNormalIndex(n[i]);
            }
        }

        DefaultObjFace result = new DefaultObjFace(v, vt, vn);
        return result;
        
    }

    
    /**
     * Create a face with the given indices. The texCoord indices and the 
     * normal indices may be <code>null</code>. In any case, it is assumed
     * that all non-<code>null</code> arrays have equal length. References
     * to the given arrays will be stored internally, so they should <b>not</b>
     * be modified after they have been passed to this method.
     * 
     * @param v The vertex indices
     * @param vt The texCoord indices
     * @param vn The normal indices
     * @return The face
     */
    public static ObjFace create(int[] v, int[] vt, int[] vn)
    {
        return createDefault(v, vt, vn);
    }
    
    /**
     * Create a face with the given indices. The texCoord indices and the 
     * normal indices may be <code>null</code>. In any case, it is assumed
     * that all non-<code>null</code> arrays have equal length. References
     * to the given arrays will be stored internally, so they should <b>not</b>
     * be modified after they have been passed to this method.
     * 
     * @param v The vertex indices
     * @param vt The texCoord indices
     * @param vn The normal indices
     * @return The face
     */
    static DefaultObjFace createDefault(int[] v, int[] vt, int[] vn)
    {
        DefaultObjFace result = new DefaultObjFace(v, vt, vn);
        return result;
    }

    
    /**
     * Returns the string for the given face that makes up one 'f' line
     * in an OBJ file
     * 
     * @param face The face
     * @return The string for the face
     */
    public static String createString(ObjFace face)
    {
        StringBuilder sb = new StringBuilder("f ");
        for(int i = 0; i < face.getNumVertices(); i++)
        {
            if (i > 0)
            {
                sb.append(" ");
            }
            sb.append(face.getVertexIndex(i) + 1);
            if(face.containsTexCoordIndices() || face.containsNormalIndices())
            {
                sb.append("/");
            }
            if(face.containsTexCoordIndices())
            {
                sb.append(face.getTexCoordIndex(i) + 1);
            }
            if(face.containsNormalIndices())
            {
                sb.append("/").append(face.getNormalIndex(i) + 1);
            }
        }
        return sb.toString();
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private ObjFaces()
    {
        // Private constructor to prevent instantiation
    }

}
