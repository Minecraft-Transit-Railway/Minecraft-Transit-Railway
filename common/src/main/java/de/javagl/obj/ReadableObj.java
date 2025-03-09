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

import java.util.List;
import java.util.Set;

/**
 * Interface for classes providing part of the data that may be stored in 
 * an OBJ file. The data represented by this interface is:<br>
 * <ul>
 *   <li>Vertices</li>
 *   <li>Texture coordinates</li>
 *   <li>Normals</li>
 *   <li>Faces</li>
 *   <li>Groups</li>
 *   <li>Material groups</li>
 * </ul>
 */
public interface ReadableObj
{
    /**
     * Returns the number of vertices in the Obj.
     * 
     * @return The number of vertices in the Obj.
     */
    int getNumVertices();

    /**
     * Returns the vertex with the given index. Note that the index
     * is <b>0</b>-based, in contrast to the <b>1</b>-based indices of the
     * actual OBJ file.
     * 
     * @param index The index of the vertex
     * @return The vertex with the given index
     * @throws IndexOutOfBoundsException If the index is negative or not
     * smaller than {@link #getNumVertices()}
     */
    FloatTuple getVertex(int index);

    
    /**
     * Returns the number of texture coordinates in the Obj.
     * 
     * @return The number of texture coordinates in the Obj.
     */
    int getNumTexCoords();

    /**
     * Returns the texture coordinate with the given index. Note that the 
     * is <b>0</b>-based, in contrast to the <b>1</b>-based indices of the
     * actual OBJ file.
     * 
     * @param index The index of the texture coordinate
     * @return The texture coordinate with the given index
     * @throws IndexOutOfBoundsException If the index is negative or not
     * smaller than {@link #getNumTexCoords()}
     */
    FloatTuple getTexCoord(int index);

    
    /**
     * Returns the number of normals in the Obj.
     * 
     * @return The number of normals in the Obj.
     */
    int getNumNormals();

    /**
     * Returns the normal with the given index. Note that the index
     * is <b>0</b>-based, in contrast to the <b>1</b>-based indices of the
     * actual OBJ file.
     * 
     * @param index The index of the normal
     * @return The normal with the given index
     * @throws IndexOutOfBoundsException If the index is negative or not
     * smaller than {@link #getNumNormals()}
     */
    FloatTuple getNormal(int index);
    
    
    /**
     * Returns the number of faces in the Obj.
     * 
     * @return The number of faces in the Obj.
     */
    int getNumFaces();

    /**
     * Returns the face with the given index. 
     * 
     * @param index The index of the face.
     * @return The face with the given index
     * @throws IndexOutOfBoundsException If the index is negative or not
     * smaller than {@link #getNumFaces()}
     */
    ObjFace getFace(int index);

    /**
     * Returns an unmodifiable set containing the names of the groups that 
     * are activated with the given face. If the groups that are 
     * activated with the given face are the same as for the previous face, 
     * then <code>null</code> will be returned.
     *  
     * @param face The face
     * @return The names of the groups that are activated with the
     * given face
     */
    Set<String> getActivatedGroupNames(ObjFace face);

    /**
     * Returns the name of the material group that is activated with the
     * given face. If the material group that is activated with the given
     * face is the same as for the previous face, then <code>null</code>
     * will be returned.
     *  
     * @param face The face
     * @return The name of the material group that is activated with the
     * given face
     */
    String getActivatedMaterialGroupName(ObjFace face);
    
    /**
     * Returns the number of groups in this Obj.
     *
     * @return The number of groups in this Obj.
     */
    int getNumGroups();

    /**
     * Returns the group with the given index.
     * 
     * @param index The index of the group.
     * @return The group with the given index.
     * @throws IndexOutOfBoundsException If the index is negative or not
     * smaller than {@link #getNumGroups()}
     */
    ObjGroup getGroup(int index);

    /**
     * Returns the group with the given name, or <code>null</code> if
     * there is no such group in this Obj.
     * 
     * @param name The name of the group.
     * @return The group with the given name.
     */
    ObjGroup getGroup(String name);

    
    
    /**
     * Returns the number of material groups in this Obj.
     *
     * @return The number of material groups in this Obj.
     */
    int getNumMaterialGroups();

    /**
     * Returns the material group with the given index.
     * 
     * @param index The index of the material group.
     * @return The material group with the given index.
     * @throws IndexOutOfBoundsException If the index is negative or not
     * smaller than {@link #getNumMaterialGroups()}
     */
    ObjGroup getMaterialGroup(int index);

    /**
     * Returns the material group with the given name, or <code>null</code> if
     * there is no such group in this Obj.
     * 
     * @param name The name of the material group.
     * @return The material group with the given name.
     */
    ObjGroup getMaterialGroup(String name);

    
    /**
     * Returns an unmodifiable list containing the names of the MTL file 
     * that are associated with this OBJ, as they have been read from 
     * the <code>mtllib</code> line. 
     * This may be an empty list, if no MTL file names have been read.
     * 
     * @return The names of the MTL files.
     */
    List<String> getMtlFileNames();

}
