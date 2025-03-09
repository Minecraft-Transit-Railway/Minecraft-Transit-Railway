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
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Basic implementation of a {@link WritableObj} that delegates all calls
 * to consumer callbacks. <br>
 * <br>
 * The consumers for the elements of an OBJ are <code>null</code> by default,
 * causing the respective elements to be ignored. The callbacks may be set 
 * individually. For example, in order to print all vertices and faces that 
 * are read from an OBJ file, the following may be used:
 * <pre><code>
 * BasicWritableObj obj = new BasicWritableObj();
 * obj.setVertexConsumer(t -&gt; System.out.println(t));
 * obj.setFaceConsumer(t -&gt; System.out.println(t));
 * ObjReader.read(inputStream, obj);
 * </code></pre> 
 */
public class BasicWritableObj implements WritableObj
{
    /**
     * The vertex consumer
     */
    private Consumer<? super FloatTuple> vertexConsumer;

    /**
     * The texture coordinate consumer
     */
    private Consumer<? super FloatTuple> texCoordConsumer;
    
    /**
     * The normal consumer
     */
    private Consumer<? super FloatTuple> normalConsumer;
    
    /**
     * The face consumer
     */
    private Consumer<? super ObjFace> faceConsumer;
    
    /**
     * The consumer for group names
     */
    private Consumer<? super Collection<? extends String>> groupNamesConsumer;
    
    /**
     * The consumer for material group names
     */
    private Consumer<? super String> materialGroupNameConsumer;
    
    /**
     * The consumer for MTL file names
     */
    private Consumer<? super Collection<? extends String>> mtlFileNamesConsumer;
    
    /**
     * Default constructor
     */
    public BasicWritableObj()
    {
        // Default constructor
    }
    
    /**
     * Set the vertex consumer
     * 
     * @param vertexConsumer The consumer
     */
    public void setVertexConsumer(Consumer<? super FloatTuple> vertexConsumer)
    {
        this.vertexConsumer = vertexConsumer;
    }

    /**
     * Set the texture coordinate consumer
     * 
     * @param texCoordConsumer The consumer
     */
    public void setTexCoordConsumer(
        Consumer<? super FloatTuple> texCoordConsumer)
    {
        this.texCoordConsumer = texCoordConsumer;
    }

    /**
     * Set the normal consumer
     * 
     * @param normalConsumer The consumer
     */
    public void setNormalConsumer(Consumer<? super FloatTuple> normalConsumer)
    {
        this.normalConsumer = normalConsumer;
    }

    /**
     * Set the face consumer
     * 
     * @param faceConsumer The consumer
     */
    public void setFaceConsumer(Consumer<? super ObjFace> faceConsumer)
    {
        this.faceConsumer = faceConsumer;
    }

    /**
     * Set the group names consumer
     * 
     * @param groupNamesConsumer The consumer
     */
    public void setGroupNamesConsumer(
        Consumer<? super Collection<? extends String>> groupNamesConsumer)
    {
        this.groupNamesConsumer = groupNamesConsumer;
    }

    /**
     * Set the material group name consumer
     * 
     * @param materialGroupNameConsumer The consumer
     */
    public void setMaterialGroupNameConsumer(
        Consumer<? super String> materialGroupNameConsumer)
    {
        this.materialGroupNameConsumer = materialGroupNameConsumer;
    }

    /**
     * Set the MTL file names consumer
     * 
     * @param mtlFileNamesConsumer The consumer
     */
    public void setMtlFileNamesConsumer(
        Consumer<? super Collection<? extends String>> mtlFileNamesConsumer)
    {
        this.mtlFileNamesConsumer = mtlFileNamesConsumer;
    }

    
    @Override
    public final void addVertex(FloatTuple vertex)
    {
        if (vertexConsumer != null)
        {
            vertexConsumer.accept(vertex);
        }
    }
    
    @Override
    public final void addVertex(float x, float y, float z)
    {
        addVertex(FloatTuples.create(x, y, z));
    }
    
    @Override
    public final void addTexCoord(FloatTuple texCoord)
    {
        if (texCoordConsumer != null)
        {
            texCoordConsumer.accept(texCoord);
        }
    }
    
    @Override
    public final void addTexCoord(float x)
    {
        addTexCoord(FloatTuples.create(x));
    }
    
    @Override
    public final void addTexCoord(float x, float y)
    {
        addTexCoord(FloatTuples.create(x, y));
    }
    
    @Override
    public final void addTexCoord(float x, float y, float z)
    {
        addTexCoord(FloatTuples.create(x, y, z));
    }
    

    @Override
    public final void addNormal(FloatTuple normal)
    {
        if (normalConsumer != null)
        {
            normalConsumer.accept(normal);
        }
    }

    @Override
    public final void addNormal(float x, float y, float z)
    {
        addNormal(FloatTuples.create(x, y, z));
    }
    
    @Override
    public final void setActiveGroupNames(
        Collection<? extends String> groupNames)
    {
        if (groupNamesConsumer != null)
        {
            groupNamesConsumer.accept(groupNames);
        }
    }
    
    
    @Override
    public final void setActiveMaterialGroupName(String materialGroupName)
    {
        if (materialGroupNameConsumer != null)
        {
            materialGroupNameConsumer.accept(materialGroupName);
        }
    }
    
    @Override
    public final void addFace(ObjFace face)
    {
        if (faceConsumer != null)
        {
            faceConsumer.accept(face);
        }
    }
    
    @Override
    public final void addFace(int ... v)
    {
        addFace(v, null, null);
    }

    @Override
    public final void addFaceWithTexCoords(int... v)
    {
        addFace(v, v, null);
    }

    @Override
    public final void addFaceWithNormals(int... v)
    {
        addFace(v, null, v);
    }

    @Override
    public final void addFaceWithAll(int... v)
    {
        addFace(v, v, v);
    }
    
    @Override
    public final void addFace(int[] v, int[] vt, int[] vn)
    {
        Objects.requireNonNull(v, "The vertex indices are null");
        if (faceConsumer != null)
        {
            addFace(ObjFaces.create(v, vt, vn));
        }
    }
    

    @Override
    public final void setMtlFileNames(Collection<? extends String> mtlFileNames)
    {
        if (mtlFileNamesConsumer != null)
        {
            mtlFileNamesConsumer.accept(mtlFileNames);
        }
    }

    
    
}
