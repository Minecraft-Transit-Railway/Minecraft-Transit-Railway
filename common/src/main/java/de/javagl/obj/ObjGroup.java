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
 * Interface describing a single group of an OBJ file. This may either
 * be a geometry group that is identified by the <code>'g'</code> token, 
 * or a group that is implied by a common material, identified by the 
 * <code>'usemtl'</code> token.
 */
public interface ObjGroup
{
    /**
     * Returns the name of this group.
     * 
     * @return The name of this group.
     */
    String getName();

    /**
     * Returns the number of faces in this group.
     * 
     * @return The number of faces in this group.
     */
    int getNumFaces();

    /**
     * Returns the face with the given index.
     * 
     * @param index The index of the face
     * @return The face with the given index.
     * @throws IndexOutOfBoundsException If the index is negative or not
     * smaller than {@link #getNumFaces()}
     */
    ObjFace getFace(int index);

}
