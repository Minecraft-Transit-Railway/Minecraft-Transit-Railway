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

import java.util.Collection;

/**
 * Interface for all classes that may receive the data that is read
 * from an OBJ file by an {@link ObjReader}
 */
public interface WritableObj
{
    /**
     * Add the given vertex
     * 
     * @param vertex The vertex to add.
     * @throws NullPointerException If the vertex is <code>null</code>
     */
    void addVertex(FloatTuple vertex);

    /**
     * Add the given vertex 
     * 
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @param z The z-coordinate
     */
    void addVertex(float x, float y, float z);

    /**
     * Add the given texture coordinate
     * 
     * @param texCoord The texture coordinate to add.
     * @throws NullPointerException If the  texture coordinate is 
     * <code>null</code>
     */
    void addTexCoord(FloatTuple texCoord);

    /**
     * Add the given texture coordinate 
     * 
     * @param x The x-coordinate
     */
    void addTexCoord(float x);
    
    /**
     * Add the given texture coordinate 
     * 
     * @param x The x-coordinate
     * @param y The y-coordinate
     */
    void addTexCoord(float x, float y);
    
    /**
     * Add the given texture coordinate 
     * 
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @param z The z-coordinate
     */
    void addTexCoord(float x, float y, float z);
    
    /**
     * Add the given normal
     * 
     * @param normal The normal to add.
     * @throws NullPointerException If the normal is <code>null</code>
     */
    void addNormal(FloatTuple normal);

    /**
     * Add the given normal 
     * 
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @param z The z-coordinate
     */
    void addNormal(float x, float y, float z);
    
    /**
     * Set the groups with the given names to be active right now. Faces that 
     * are added subsequently will be added to all active groups, creating
     * these groups if necessary. If the given collection is <code>null</code>,
     * then this call will have no effect. If the given collection is empty,
     * then the default group (named "default") will be activated.
     * 
     * @param groupNames The group names
     * @throws NullPointerException If the given collection contains 
     * <code>null</code> elements
     */
    void setActiveGroupNames(Collection<? extends String> groupNames);
    
    /**
     * Set the material group with the given names to be active right now 
     * Faces that are added subsequently will be added to the active 
     * material group, creating this material group if necessary. If
     * the given name is <code>null</code>, then this call will have no
     * effect.
     * 
     * @param materialGroupName The material group name
     */
    void setActiveMaterialGroupName(String materialGroupName);
   
    /**
     * Add the given face. 
     * The indices in the given face are absolute (non-negative) and 
     * <b>0</b>-based. 
     * The implementation is free to store a <b>reference</b> to the given 
     * face.  
     * 
     * @param face The face to add.
     * @throws NullPointerException If the face is <code>null</code>
     */
    void addFace(ObjFace face);
    
    /**
     * Add the specified face with the given vertex indices, but without 
     * texture- or normal indices.
     * The given indices are absolute (non-negative) and <b>0</b>-based. 
     * The implementation is free to store a <b>reference</b> to the given 
     * array. So the array should not be modified after this method has 
     * been called. 
     * 
     * @param v The vertex indices
     * @throws IllegalArgumentException If one of the given indices is
     * negative or not smaller than the number of vertices that have been
     * added until now.
     */
    void addFace(int ... v);

    /**
     * Add the specified face with the given vertex and texture coordinate
     * indices, but without normal indices.
     * The given indices are absolute (non-negative) and <b>0</b>-based. 
     * The implementation is free to store a <b>reference</b> to the given 
     * array. So the array should not be modified after this method has 
     * been called. 
     * 
     * @param v The vertex- and texture coordinate indices
     * @throws IllegalArgumentException If one of the given indices is
     * negative or not smaller than the number of vertices or texture 
     * coordinates that have been added until now.
     */
    void addFaceWithTexCoords(int ... v);

    /**
     * Add the specified face with the given vertex and normal indices, 
     * but without texture coordinate indices.
     * The given indices are absolute (non-negative) and <b>0</b>-based. 
     * The implementation is free to store a <b>reference</b> to the given 
     * array. So the array should not be modified after this method has 
     * been called. 
     * 
     * @param v The vertex- and normal indices
     * @throws IllegalArgumentException If one of the given indices is
     * negative or not smaller than the number of vertices or normals 
     * that have been added until now.
     */
    void addFaceWithNormals(int ... v);

    /**
     * Add the specified face with the given vertex, texture coordinate
     * and normal indices.
     * The given indices are absolute (non-negative) and <b>0</b>-based. 
     * The implementation is free to store a <b>reference</b> to the given 
     * array. So the array should not be modified after this method has 
     * been called. 
     * 
     * @param v The vertex- texture coordinate and normal indices
     * @throws IllegalArgumentException If one of the given indices is
     * negative or not smaller than the number of vertices, texture
     * coordinates or normals that have been added until now.
     */
    void addFaceWithAll(int ... v);
    
    /**
     * Add the specified face. 
     * The given indices are absolute (non-negative) and <b>0</b>-based. 
     * The implementation is free to store a <b>reference</b> to the given 
     * arrays. So the arrays should not be modified after this method has 
     * been called. 
     * 
     * @param v The vertex indices
     * @param vt The texture coordinate indices. May be <code>null</code>.
     * @param vn The normal indices. May be <code>null</code>
     * @throws NullPointerException If the vertex indices array is 
     * <code>null</code>
     * @throws IllegalArgumentException If one of the given indices is
     * negative or not smaller than the number of corresponding vertices,
     * texture coordinates or normals that have been added until now,
     * respectively.
     * @throws IllegalArgumentException If the given (non-null) arrays 
     * have different lengths
     */
    void addFace(int[] v, int[] vt, int[] vn);
    
    /**
     * Set the given MTL file names. A copy of the given
     * collection will be stored.
     * 
     * @param mtlFileNames The names of the MTL file
     */
    void setMtlFileNames(Collection<? extends String> mtlFileNames);
    
}
