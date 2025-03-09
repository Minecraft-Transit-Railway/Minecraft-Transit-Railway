/*
 * www.javagl.de - Obj
 *
 * Copyright (c) 2008-2017 Marco Hutter - http://www.javagl.de
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Methods for splitting {@link ReadableObj} objects into multiple parts, 
 * based on different criteria. 
 */
public class ObjSplitting
{
    /**
     * Split the given {@link ReadableObj} based on its groups. This will
     * create one {@link Obj} from each (non-empty) group in the 
     * given input.
     * 
     * @param obj The input {@link ReadableObj}
     * @return One {@link Obj} for each non-empty group of the given input
     */
    public static Map<String, Obj> splitByGroups(ReadableObj obj)
    {
        Map<String, Obj> objs = new LinkedHashMap<>();
        int numGroups = obj.getNumGroups();
        for (int i = 0; i < numGroups; i++)
        {
            ObjGroup group = obj.getGroup(i);
            if (group.getNumFaces() > 0)
            {
                String groupName = group.getName();
                Obj groupObj = ObjUtils.groupToObj(obj, group, null);
                objs.put(groupName, groupObj);
            }
        }
        return objs;
    }
    
    /**
     * Split the given {@link ReadableObj} based on its material groups. 
     * This will create one {@link Obj} from each (non-empty) material 
     * group in the given input. <br>
     * <br>
     * Note that if the given OBJ does not contain any material groups
     * (that is, when it does not refer to a material with a 
     * <code>usemtl</code> directive), then the resulting map will
     * be empty. <br>
     * <br>
     * Faces that are not associated with any material group 
     * will not be contained in the output.
     * 
     * @param obj The input {@link ReadableObj}
     * @return The mapping from material group names (corresponding to the
     * <code>usemtl</code> directives in the input file) to the {@link Obj} 
     * that represents this material group.
     */
    public static Map<String, Obj> splitByMaterialGroups(ReadableObj obj)
    {
        Map<String, Obj> objs = new LinkedHashMap<>();
        int numMaterialGroups = obj.getNumMaterialGroups();
        for (int i = 0; i < numMaterialGroups; i++)
        {
            ObjGroup materialGroup = obj.getMaterialGroup(i);
            if (materialGroup.getNumFaces() > 0)
            {
                String materialGroupName = materialGroup.getName();
                Obj materialGroupObj = 
                    ObjUtils.groupToObj(obj, materialGroup, null);
                objs.put(materialGroupName, materialGroupObj);
            }
        }
        return objs;
    }

    /**
     * Split the given {@link ReadableObj} into {@link Obj} instances based
     * on the given maximum number of vertices.<br> 
     * <br>
     * Note that this method will not split any <i>faces</i> in the given
     * OBJ. So although the resulting parts will usually not have more 
     * than the given maximum number of vertices, this cannot be guaranteed:
     * When there is a face in the input OBJ that has <code>n</code> vertices, 
     * the resulting parts will have at most <code>maxNumVertices + n - 1</code>
     * vertices. For example, if the given input contains only triangles, 
     * then the parts will have at most <code>maxNumVertices + 2</code>
     * vertices.<br>
     * <br>
     * Many details about the splitting process that is used internally are
     * intentionally not specified. This method is mainly intended for 
     * splitting a large {@link Obj} into parts that it may be rendered in 
     * environments that only allow a certain number of vertices (indices) 
     * per rendered object. For example, in order to create parts whose 
     * indices can be represented with an <code>unsigned short</code> value, 
     * this method may be called with <code>maxNumVertices=65000</code>.  
     * 
     * @param obj The input {@link ReadableObj}
     * @param maxNumVertices The maximum number of vertices
     * @return One {@link Obj} for each part. 
     * @throws IllegalArgumentException If the given number is smaller than 3.
     */
    public static List<Obj> splitByMaxNumVertices(
        ReadableObj obj, int maxNumVertices)
    {
        if (maxNumVertices < 3)
        {
            throw new IllegalArgumentException(
                "The given number of vertices must at least be 3");
        }
        
        ObjSplitter splitter = new ObjSplitter(maxNumVertices);
        return splitter.split(obj);
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private ObjSplitting()
    {
        // Private constructor to prevent instantiation
    }
    
}
