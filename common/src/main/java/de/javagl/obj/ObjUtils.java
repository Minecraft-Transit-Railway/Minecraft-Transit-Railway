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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Utility methods for handling {@link Obj}s.<br>
 * <br>
 * Unless otherwise noted, none of the parameters to these methods
 * may be <code>null</code>
 */
public class ObjUtils
{
    /**
     * Convert the given {@link ReadableObj} into an {@link Obj} that has
     * a structure appropriate for rendering it with OpenGL:
     * <ul>
     *   <li>{@link #triangulate(ReadableObj) Triangulate} it</li>
     *   <li>
     *     {@link #makeTexCoordsUnique(ReadableObj) 
     *        Make the texture coordinates unique}
     *   </li>
     *   <li>
     *     {@link #makeNormalsUnique(ReadableObj) 
     *        Make the normals unique}
     *   </li>
     *   <li>
     *     Make the result {@link #makeVertexIndexed(ReadableObj) 
     *     vertex-indexed}
     *   </li>
     * </ul>  
     * 
     * @param input The input {@link ReadableObj} 
     * @return The resulting {@link Obj}
     */
    public static Obj convertToRenderable(ReadableObj input)
    {
        return convertToRenderable(input, Objs.create());
    }
    
    /**
     * Convert the given {@link ReadableObj} into an {@link Obj} that has
     * a structure appropriate for rendering it with OpenGL:
     * <ul>
     *   <li>{@link #triangulate(ReadableObj) Triangulate} it</li>
     *   <li>
     *     {@link #makeTexCoordsUnique(ReadableObj) 
     *        Make the texture coordinates unique}
     *   </li>
     *   <li>
     *     {@link #makeNormalsUnique(ReadableObj) 
     *        Make the normals unique}
     *   </li>
     *   <li>
     *     Make the result {@link #makeVertexIndexed(ReadableObj) 
     *     vertex-indexed}
     *   </li>
     * </ul>  
     * 
     * @param <T> The type of the output
     * @param input The input {@link ReadableObj} 
     * @param output The output {@link WritableObj}
     * @return The given output
     */
    public static <T extends WritableObj> T convertToRenderable(
        ReadableObj input, T output)
    {
        Obj obj = triangulate(input);
        obj = makeTexCoordsUnique(obj);
        obj = makeNormalsUnique(obj);
        return makeVertexIndexed(obj, output);
    }
    
    
    /**
     * Triangulates the given input {@link ReadableObj} and returns the 
     * result.<br>
     * <br>
     * This method will simply subdivide faces with more than 3 vertices so 
     * that all faces in the output will be triangles.
     * 
     * @param input The input {@link ReadableObj} 
     * @return The resulting {@link Obj}
     */
    public static Obj triangulate(ReadableObj input)
    {
        return triangulate(input, Objs.create());
    }
    
    /**
     * Triangulates the given input {@link ReadableObj} and stores the result 
     * in the given {@link WritableObj}.<br>
     * <br>
     * This method will simply subdivide faces with more than 3 vertices so 
     * that all faces in the output will be triangles.
     * 
     * @param <T> The type of the output
     * @param input The input {@link ReadableObj} 
     * @param output The output {@link WritableObj}
     * @return The given output
     */
    public static <T extends WritableObj> T triangulate(
        ReadableObj input, T output)
    {
        output.setMtlFileNames(input.getMtlFileNames());
        
        addAll(input, output);

        for (int i=0; i<input.getNumFaces(); i++)
        {
            ObjFace face = input.getFace(i);
            activateGroups(input, face, output);
            if (face.getNumVertices() == 3)
            {
                output.addFace(face);
            }
            else
            {
                for(int j = 0; j < face.getNumVertices() - 2; j++)
                {
                    DefaultObjFace triangle = 
                        ObjFaces.create(face, 0, j + 1, j + 2);
                    output.addFace(triangle);
                }
            }
        }
        return output;
    }
    
    
    /**
     * Returns the given group of the given {@link ReadableObj} as a new
     * {@link Obj}.<br>
     * <br>
     * The <code>vertexIndexMapping</code> may be <code>null</code>. If it 
     * is not <code>null</code>, <code>vertexIndexMapping.get(i)</code> 
     * afterwards stores the index that vertex <code>i</code> had in the 
     * input.
     * 
     * @param input The input {@link ReadableObj}
     * @param inputGroup The group of the input
     * @param vertexIndexMapping The optional index mapping
     * @return The resulting {@link Obj}
     */
    public static Obj groupToObj(
        ReadableObj input, ObjGroup inputGroup, 
        List<Integer> vertexIndexMapping)
    {
        return groupToObj(input, inputGroup, vertexIndexMapping, Objs.create());
    }

    /**
     * Stores the given group of the given {@link ReadableObj}
     * in the given output {@link WritableObj}.<br>
     * <br>
     * The <code>vertexIndexMapping</code> may be <code>null</code>. If it 
     * is not <code>null</code>, <code>vertexIndexMapping.get(i)</code> 
     * afterwards stores the index that vertex <code>i</code> had in the 
     * input.
     * 
     * @param <T> The type of the output
     * @param input The input {@link ReadableObj}
     * @param inputGroup The group of the input
     * @param vertexIndexMapping The optional index mapping
     * @param output The output {@link WritableObj}
     * @return The given output
     */
    public static <T extends WritableObj> T groupToObj(
        ReadableObj input, ObjGroup inputGroup, 
        List<Integer> vertexIndexMapping, T output)
    {
        output.setMtlFileNames(input.getMtlFileNames());

        // vertexIndexMap[i] contains the index that vertex i of the 
        // original Obj will have in the output
        int[] vertexIndexMap = new int[input.getNumVertices()];
        int[] texCoordIndexMap = new int[input.getNumTexCoords()];
        int[] normalIndexMap = new int[input.getNumNormals()];

        Arrays.fill(vertexIndexMap, -1);
        Arrays.fill(texCoordIndexMap, -1);
        Arrays.fill(normalIndexMap, -1);

        int vertexCounter = 0;
        int texCoordCounter = 0;
        int normalCounter = 0;
        for(int i = 0; i < inputGroup.getNumFaces(); i++)
        {
            // Clone the face info from the input
            ObjFace face = inputGroup.getFace(i);

            DefaultObjFace resultFace = ObjFaces.create(face);
            
            activateGroups(input, face, output);
            
            // The indices of the cloned face have to be adjusted, 
            // so that they point to the correct vertices in the output
            for(int j = 0; j < face.getNumVertices(); j++)
            {
                int vertexIndex = face.getVertexIndex(j);
                if(vertexIndexMap[vertexIndex] == -1)
                {
                    vertexIndexMap[vertexIndex] = vertexCounter;
                    output.addVertex(input.getVertex(vertexIndex));
                    vertexCounter++;
                }
                resultFace.setVertexIndex(j, vertexIndexMap[vertexIndex]);
            }

            if(face.containsTexCoordIndices())
            {
                for(int j = 0; j < face.getNumVertices(); j++)
                {
                    int texCoordIndex = face.getTexCoordIndex(j);
                    if(texCoordIndexMap[texCoordIndex] == -1)
                    {
                        texCoordIndexMap[texCoordIndex] = texCoordCounter;
                        output.addTexCoord(input.getTexCoord(texCoordIndex));
                        texCoordCounter++;
                    }
                    resultFace.setTexCoordIndex(
                        j, texCoordIndexMap[texCoordIndex]);
                }
            }


            if(face.containsNormalIndices())
            {
                for(int j = 0; j < face.getNumVertices(); j++)
                {
                    int normalIndex = face.getNormalIndex(j);
                    if(normalIndexMap[normalIndex] == -1)
                    {
                        normalIndexMap[normalIndex] = normalCounter;
                        output.addNormal(input.getNormal(normalIndex));
                        normalCounter++;
                    }
                    resultFace.setNormalIndex(j, normalIndexMap[normalIndex]);
                }
            }

            // Add the cloned face with the adjusted indices to the output
            output.addFace(resultFace);
        }

        // Compute the vertexIndexMapping, so that vertexIndexMapping.get(i) 
        // afterwards stores the index that vertex i had in the input 
        if(vertexIndexMapping != null)
        {
            for(int i = 0; i < vertexCounter; i++)
            {
                vertexIndexMapping.add(-1);
            }
            for(int i = 0; i < input.getNumVertices(); i++)
            {
                if(vertexIndexMap[i] != -1)
                {
                    vertexIndexMapping.set(vertexIndexMap[i], i);
                }
            }
        }
        
        return output;
    }
    
    
    /**
     * Interface for accessing a property index (vertex, texture coordinate
     * or normal) from a {@link ReadableObj}
     */
    private interface PropertyIndexAccessor
    {
        /**
         * Returns the index of the specified property from the given 
         * {@link ReadableObj}
         * 
         * @param input The {@link ReadableObj}
         * @param face The face
         * @param vertexNumber The number of the vertex in the face
         * @return The index of the property
         * @throws IndexOutOfBoundsException If the index is out of bounds
         */
        int getPropertyIndex(
            ReadableObj input, ObjFace face, int vertexNumber);
        
        /**
         * Returns whether the given face has indices for the property
         * that may be queries with this accessor
         * 
         * @param face The face
         * @return Whether the face has the property 
         */
        boolean hasProperty(ObjFace face);
    }
    

    /**
     * Ensures that two vertices with different texture coordinates are 
     * actually two different vertices with different indices.<br>
     * <br>
     * Two faces may reference the same vertex in the OBJ file. But different 
     * texture coordinates may be assigned to the same vertex in 
     * both faces. The vertex that requires two different properties will be 
     * duplicated in the output, and the indices in one face will be updated
     * appropriately.<br>
     * <br>
     * This process solely operates on the <i>indices</i> of the properties.
     * It will not check whether the <i>value</i> of two properties (with
     * different indices) are actually equal.
     * 
     * @param input The input {@link ReadableObj}
     * @return The resulting {@link Obj}
     */
    public static Obj makeTexCoordsUnique(ReadableObj input)
    {
        return makeTexCoordsUnique(input, null, Objs.create());
    }
    
    /**
     * Ensures that two vertices with different texture coordinates are 
     * actually two different vertices with different indices.<br>
     * <br>
     * Two faces may reference the same vertex in the OBJ file. But different 
     * texture coordinates may be assigned to the same vertex in 
     * both faces. The vertex that requires two different properties will be 
     * duplicated in the output, and the indices in one face will be updated
     * appropriately.<br>
     * <br>
     * This process solely operates on the <i>indices</i> of the properties.
     * It will not check whether the <i>value</i> of two properties (with
     * different indices) are actually equal.
     * <br>
     * The indexMapping may be <code>null</code>. Otherwise it is assumed that 
     * it already contains the index mapping that was obtained by a call 
     * to {@link #groupToObj(ReadableObj, ObjGroup, List, WritableObj)}, and 
     * this mapping will be updated appropriately.
     * 
     * @param <T> The type of the output
     * @param input The input {@link ReadableObj}
     * @param indexMapping The optional index mapping
     * @param output The output {@link WritableObj}
     * @return The given output
     */
    public static <T extends WritableObj> T makeTexCoordsUnique(
        ReadableObj input, List<Integer> indexMapping, T output)
    {
        PropertyIndexAccessor accessor = new PropertyIndexAccessor()
        {
            @Override
            public int getPropertyIndex(
                ReadableObj input, ObjFace face, int vertexNumber)
            {
                return face.getTexCoordIndex(vertexNumber);
            }

            @Override
            public boolean hasProperty(ObjFace face)
            {
                return face.containsTexCoordIndices();
            }
        };
        makePropertiesUnique(input, accessor, indexMapping, output);
        return output;
    }
    
    
    /**
     * Ensures that two vertices with different normals are 
     * actually two different vertices with different indices.<br>
     * <br>
     * Two faces may reference the same vertex in the OBJ file. But different 
     * normals may be assigned to the same vertex in 
     * both faces. The vertex that requires two different properties will be 
     * duplicated in the output, and the indices in one face will be updated
     * appropriately.<br>
     * <br>
     * This process solely operates on the <i>indices</i> of the properties.
     * It will not check whether the <i>value</i> of two properties (with
     * different indices) are actually equal.
     * 
     * @param input The input {@link ReadableObj}
     * @return The resulting {@link Obj}
     */
    public static Obj makeNormalsUnique(ReadableObj input)
    {
        return makeNormalsUnique(input, null, Objs.create());
    }
    

    /**
     * Ensures that two vertices with different normals are 
     * actually two different vertices with different indices.<br>
     * <br>
     * Two faces may reference the same vertex in the OBJ file. But different 
     * normals may be assigned to the same vertex in 
     * both faces. The vertex that requires two different properties will be 
     * duplicated in the output, and the indices in one face will be updated
     * appropriately.<br>
     * <br>
     * This process solely operates on the <i>indices</i> of the properties.
     * It will not check whether the <i>value</i> of two properties (with
     * different indices) are actually equal.
     * <br>
     * The indexMapping may be <code>null</code>. Otherwise it is assumed that 
     * it already contains the index mapping that was obtained by a call 
     * to {@link #groupToObj(ReadableObj, ObjGroup, List, WritableObj)}, and 
     * this mapping will be updated appropriately.
     * 
     * @param <T> The type of the output
     * @param input The input {@link ReadableObj}
     * @param indexMapping The optional index mapping
     * @param output The output {@link WritableObj}
     * @return The given output
     */
    public static <T extends WritableObj> T makeNormalsUnique(
        ReadableObj input, List<Integer> indexMapping, T output)
    {
        PropertyIndexAccessor accessor = new PropertyIndexAccessor()
        {
            @Override
            public int getPropertyIndex(
                ReadableObj input, ObjFace face, int vertexNumber)
            {
                return face.getNormalIndex(vertexNumber);
            }
            
            @Override
            public boolean hasProperty(ObjFace face)
            {
                return face.containsNormalIndices();
            }
        };
        makePropertiesUnique(input, accessor, indexMapping, output);
        return output;
    }
    

    /**
     * Ensures that two vertices with different properties are 
     * actually two different vertices with different indices.<br>
     * <br>
     * Two faces may reference the same vertex in the OBJ file. But different 
     * normals or texture coordinates may be assigned to the same vertex in 
     * both faces. The vertex that requires two different properties will be 
     * duplicated in the output, and the indices in one face will be updated
     * appropriately.<br>
     * <br>
     * This process solely operates on the <i>indices</i> of the properties.
     * It will not check whether the <i>value</i> of two properties (with
     * different indices) are actually equal.
     * <br>
     * The indexMapping may be <code>null</code>. Otherwise it is assumed that 
     * it already contains the index mapping that was obtained by a call 
     * to {@link #groupToObj(ReadableObj, ObjGroup, List, WritableObj)}, and 
     * this mapping will be updated appropriately.
     * 
     * @param input The input {@link ReadableObj}
     * @param propertyIndexAccessor The accessor for the property index 
     * @param indexMapping The optional index mapping
     * @param output The output {@link WritableObj}
     */
    private static void makePropertiesUnique(
        ReadableObj input, PropertyIndexAccessor propertyIndexAccessor, 
        List<Integer> indexMapping, WritableObj output)
    {
        output.setMtlFileNames(input.getMtlFileNames());
        addAll(input, output);
        
        int[] usedPropertyIndices = new int[input.getNumVertices()];
        Arrays.fill(usedPropertyIndices, -1);
        List<FloatTuple> extendedVertices = new ArrayList<>();

        for(int i = 0; i < input.getNumFaces(); i++)
        {
            ObjFace inputFace = input.getFace(i);
            
            activateGroups(input, inputFace, output);
            
            ObjFace outputFace = inputFace;
            
            if (propertyIndexAccessor.hasProperty(inputFace))
            {
                DefaultObjFace extendedOutputFace = null;
                
                for(int j = 0; j < outputFace.getNumVertices(); j++)
                {
                    int vertexIndex = outputFace.getVertexIndex(j);
                    int propertyIndex = 
                        propertyIndexAccessor.getPropertyIndex(
                            input, outputFace, j);
    
                    // Check if the property of the vertex with the current
                    // index already has been used, and it is not equal to
                    // the property that it has in the current face
                    if(usedPropertyIndices[vertexIndex] != -1 &&
                       usedPropertyIndices[vertexIndex] != propertyIndex)  
                    {
                        FloatTuple vertex = input.getVertex(vertexIndex);
    
                        // Add the vertex which has multiple properties once
                        // more to the output, and update all indices that 
                        // now have to point to the "new" vertex
                        int extendedVertexIndex = 
                            input.getNumVertices() + extendedVertices.size();
                        extendedVertices.add(vertex);
                        output.addVertex(vertex);
                        
                        if (extendedOutputFace == null)
                        {
                            extendedOutputFace = ObjFaces.create(inputFace);
                        }
                        extendedOutputFace.setVertexIndex(
                            j, extendedVertexIndex);
    
                        if(indexMapping != null)
                        {
                            int indexInObj = indexMapping.get(vertexIndex);
                            indexMapping.add(indexInObj);
                        }
                    }
                    else
                    {
                        usedPropertyIndices[vertexIndex] = propertyIndex;
                    }
                }
                if (extendedOutputFace != null)
                {
                    outputFace = extendedOutputFace;
                }
            }
            output.addFace(outputFace);
        }
    }
    
    
    /**
     * Add all vertices, texture coordinates and normals of the given 
     * {@link ReadableObj} to the given {@link WritableObj}
     * 
     * @param input The {@link ReadableObj}
     * @param output The {@link WritableObj}
     */
    private static void addAll(ReadableObj input, WritableObj output)
    {
        for (int i=0; i<input.getNumVertices(); i++)
        {
            output.addVertex(input.getVertex(i));
        }
        for (int i=0; i<input.getNumTexCoords(); i++)
        {
            output.addTexCoord(input.getTexCoord(i));
        }
        for (int i=0; i<input.getNumNormals(); i++)
        {
            output.addNormal(input.getNormal(i));
        }
    }
    
    /**
     * Add all vertices, texture coordinates, normals and faces of the given
     * {@link ReadableObj} to the given output {@link Obj}.<br>
     * <br>
     * Note that this may cause new groups or material groups to be created
     * in the output OBJ. If the output OBJ contains group names or material 
     * group names that also exist in the input OBJ, then the elements of the
     * input OBJ will be added to these existing groups.  
     * 
     * @param input The {@link ReadableObj}
     * @param output The target {@link Obj}
     */
    public static void add(ReadableObj input, Obj output)
    {
        int verticesOffset = output.getNumVertices();
        for (int i=0; i<input.getNumVertices(); i++)
        {
            output.addVertex(input.getVertex(i));
        }
        
        int texCoordsOffset = output.getNumTexCoords();
        for (int i=0; i<input.getNumTexCoords(); i++)
        {
            output.addTexCoord(input.getTexCoord(i));
        }
        
        int normalsOffset = output.getNumNormals();
        for (int i=0; i<input.getNumNormals(); i++)
        {
            output.addNormal(input.getNormal(i));
        }
        
        for(int i = 0; i < input.getNumFaces(); i++)
        {
            ObjFace inputFace = input.getFace(i);
            
            activateGroups(input, inputFace, output);
            
            DefaultObjFace outputFace = ObjFaces.createWithOffsets(
                inputFace, verticesOffset, texCoordsOffset, normalsOffset);
            output.addFace(outputFace);
        }
    }
    

    /**
     * Converts the given {@link ReadableObj} data into data that uses the
     * same indices for vertices, texture coordinates and normals, and
     * returns the result.<br>
     * <br>
     * Note that the result may contain ambiguous texture coordinates and 
     * normals, unless they have been made unique in the input before with
     * {@link #makeTexCoordsUnique(ReadableObj)} and 
     * {@link #makeNormalsUnique(ReadableObj)}, respectively.
     * 
     * @param input The input {@link ReadableObj} 
     * @return The resulting {@link Obj}
     */
    public static Obj makeVertexIndexed(ReadableObj input)
    {
        return makeVertexIndexed(input, Objs.create());
    }
    
    /**
     * Converts the given {@link ReadableObj} data into data that uses the
     * same indices for vertices, texture coordinates and normals, and
     * stores the result in the given {@link WritableObj}.<br>
     * <br>
     * Note that the result may contain ambiguous texture coordinates and 
     * normals, unless they have been made unique in the input before with
     * {@link #makeTexCoordsUnique(ReadableObj)} and 
     * {@link #makeNormalsUnique(ReadableObj)}, respectively.
     * 
     * @param <T> The type of the output
     * @param input The input {@link ReadableObj}
     * @param output The output {@link WritableObj}
     * @return The given output
     */
    public static <T extends WritableObj> T makeVertexIndexed(
        ReadableObj input, T output)
    {
        output.setMtlFileNames(input.getMtlFileNames());
        for (int i=0; i<input.getNumVertices(); i++)
        {
            output.addVertex(input.getVertex(i));
        }
        
        boolean foundTexCoords = false;
        boolean foundNormals = false;
        int[] texCoordIndicesForVertexIndices = new int[input.getNumVertices()];
        int[] normalIndicesForVertexIndices = new int[input.getNumVertices()];
        for(int i = 0; i < input.getNumFaces(); i++)
        {
            ObjFace inputFace = input.getFace(i);
            for(int j = 0; j < inputFace.getNumVertices(); j++)
            {
                int vertexIndex = inputFace.getVertexIndex(j);
                if (inputFace.containsTexCoordIndices())
                {
                    int texCoordIndex = inputFace.getTexCoordIndex(j);
                    texCoordIndicesForVertexIndices[vertexIndex] =
                        texCoordIndex;
                    foundTexCoords = true;
                }
                if (inputFace.containsNormalIndices())
                {
                    int normalIndex = inputFace.getNormalIndex(j);
                    normalIndicesForVertexIndices[vertexIndex] =
                        normalIndex;
                    foundNormals = true;
                }
            }
        }
        
        if (foundTexCoords)
        {
            for (int i=0; i<input.getNumVertices(); i++)
            {
                int texCoordIndex = texCoordIndicesForVertexIndices[i];
                FloatTuple texCoord = input.getTexCoord(texCoordIndex);
                output.addTexCoord(texCoord);
            }
        }
        
        if (foundNormals)
        {
            for (int i=0; i<input.getNumVertices(); i++)
            {
                int normalIndex = normalIndicesForVertexIndices[i];
                FloatTuple normal = input.getNormal(normalIndex);
                output.addNormal(normal);
            }
        }
        
        for(int i = 0; i < input.getNumFaces(); i++)
        {
            ObjFace inputFace = input.getFace(i);
            
            activateGroups(input, inputFace, output);
            
            DefaultObjFace outputFace = ObjFaces.create(inputFace);
            if (inputFace.containsTexCoordIndices())
            {
                for (int j=0; j<inputFace.getNumVertices(); j++)
                {
                    outputFace.setTexCoordIndex(j, 
                        outputFace.getVertexIndex(j));
                }
            }
            if (inputFace.containsNormalIndices())
            {
                for (int j=0; j<inputFace.getNumVertices(); j++)
                {
                    outputFace.setNormalIndex(j, 
                        outputFace.getVertexIndex(j));
                }
            }
            output.addFace(outputFace);
        }
        return output;
    }
    
    
    /**
     * Set the active group names and material group name in the given
     * output based on the group names and material group name that the
     * given face activated in the input
     * 
     * @param input The input {@link ReadableObj} 
     * @param face The {@link ObjFace} to perform the activation for
     * @param output The output {@link WritableObj} 
     */
    private static void activateGroups(
        ReadableObj input, ObjFace face, WritableObj output)
    {
        Set<String> activatedGroupNames = 
            input.getActivatedGroupNames(face);
        if (activatedGroupNames != null)
        {
            output.setActiveGroupNames(
                activatedGroupNames);
        }
        String activatedMaterialGroupName = 
            input.getActivatedMaterialGroupName(face);
        if (activatedMaterialGroupName != null)
        {
            output.setActiveMaterialGroupName(
                activatedMaterialGroupName);
        }
    }
    
    
    /**
     * Create a multi-line, formatted string containing information about
     * the given {@link ReadableObj}. This method is solely intended for 
     * debugging. It should not be considered as part of the public API. 
     * The exact output is not specified.
     *  
     * @param obj The {@link ReadableObj}
     * @return The info string
     */
    public static String createInfoString(ReadableObj obj)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Obj:"+"\n");
        sb.append("    mtlFileNames     : "+obj.getMtlFileNames()+"\n");
        sb.append("    numVertices      : "+obj.getNumVertices()+"\n");
        sb.append("    numTexCoords     : "+obj.getNumTexCoords()+"\n");
        sb.append("    numNormals       : "+obj.getNumNormals()+"\n");
        sb.append("    numFaces         : "+obj.getNumFaces()+"\n");
        sb.append("    numGroups        : "+obj.getNumGroups()+"\n");
        for (int i=0; i<obj.getNumGroups(); i++)
        {
            ObjGroup objGroup = obj.getGroup(i);
            sb.append("        Group "+i+":"+"\n");
            sb.append("            name    : "+objGroup.getName()+"\n");
            sb.append("            numFaces: "+objGroup.getNumFaces()+"\n");
        }
        sb.append("    numMaterialGroups: "+obj.getNumMaterialGroups()+"\n");
        for (int i=0; i<obj.getNumMaterialGroups(); i++)
        {
            ObjGroup objGroup = obj.getMaterialGroup(i);
            sb.append("        MaterialGroup "+i+":"+"\n");
            sb.append("            name    : "+objGroup.getName()+"\n");
            sb.append("            numFaces: "+objGroup.getNumFaces()+"\n");
        }
        return sb.toString();
    }



    /**
     * Private constructor to prevent instantiation.
     */
    private ObjUtils()
    {
        // Private constructor to prevent instantiation.
    }
}
