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
 * A single face that is stored in an OBJ file
 */
public interface ObjFace
{
    /**
     * Returns the number of vertices this face consists of.
     * 
     * @return The number of vertices this face consists of.
     */
    int getNumVertices();

    /**
     * Returns whether this face contains texture coordinate indices
     * 
     * @return Whether this face contains texture coordinate indices
     */
    boolean containsTexCoordIndices();

    /**
     * Returns whether this face contains normal indices
     * 
     * @return Whether this face contains normal indices
     */
    boolean containsNormalIndices();

    /**
     * Returns the index of the vertex with the given number.
     * The index that is returned will be ZERO-based, in contrast
     * to the ONE-based storage in the OBJ file.
     * 
     * @param number The number of the vertex
     * @return The index of the vertex.
     * @throws IndexOutOfBoundsException If the given number is negative
     * or not smaller than {@link #getNumVertices()}
     */
    int getVertexIndex(int number);

    /**
     * Returns the index of the texture coordinate with the given number.
     * The index that is returned will be ZERO-based, in contrast
     * to the ONE-based storage in the OBJ file.
     * 
     * @param number The number of the texture coordinate
     * @return The index of the texture coordinate.
     * @throws IndexOutOfBoundsException If the given number is negative
     * or not smaller than {@link #getNumVertices()}
     */
    int getTexCoordIndex(int number);

    /**
     * Returns the index of the normal with the given number.
     * The index that is returned will be ZERO-based, in contrast
     * to the ONE-based storage in the OBJ file.
     * 
     * @param number The number of the normal
     * @return The index of the normal.
     * @throws IndexOutOfBoundsException If the given number is negative
     * or not smaller than {@link #getNumVertices()}
     */
    int getNormalIndex(int number);
}
