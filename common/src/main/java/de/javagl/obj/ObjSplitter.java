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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Package-private class that can split an OBJ into multiple parts, based
 * on its number of vertices. Many details about the behavior of this class 
 * are intentionally not specified.
 */
class ObjSplitter
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(ObjSplitter.class.getName());
    
    /**
     * The log level
     */
    private static final Level level = Level.FINE;

    /**
     * A predicate interface, equivalent to java.util.functional.Predicate,
     * but defined here in order to target Java 1.7
     *
     * @param <T> The parameter type
     */
    private interface Predicate<T>
    {
        /**
         * Test the given object
         * 
         * @param t The object
         * @return Whether this predicate applies to the object
         */
        boolean test(T t);
    }
    
    /**
     * The predicate that will be used to check whether an OBJ should
     * be split.
     */
    private final Predicate<? super ReadableObj> splitPredicate;
    
    /**
     * Creates a new instance that splits OBJs into parts that have at most 
     * the given maximum number of vertices (if this is possible without
     * splitting faces)
     * 
     * @param maxNumVertices The maximum number of vertices
     */
    ObjSplitter(final int maxNumVertices)
    {
        splitPredicate = new Predicate<ReadableObj>()
        {
            @Override
            public boolean test(ReadableObj obj)
            {
                return obj.getNumVertices() > maxNumVertices;
            }
        };
    }
    
    /**
     * Split the given OBJ into multiple parts, if it is necessary according
     * to the {@link #splitPredicate}.
     * 
     * @param obj The input OBJ
     * @return The list of resulting OBJs
     */
    List<Obj> split(ReadableObj obj)
    {
        // If no splitting is necessary, just return a single OBJ that
        // is a copy of the input.
        if (!splitPredicate.test(obj))
        {
            Obj singleObj = Objs.create();
            ObjUtils.add(obj, singleObj);
            return Collections.singletonList(singleObj);
        }

        // Perform the initial split, and proceed splitting the
        // parts until none of them matches the splitPredicate
        List<Obj> currentObjs = splitSingle(obj);
        boolean didSplit = currentObjs.size() > 1;
        while (didSplit)
        {
            didSplit = false;
            List<Obj> nextObjs = new ArrayList<>();
            for (Obj currentObj : currentObjs)
            {
                if (splitPredicate.test(currentObj))
                {
                    List<Obj> parts = splitSingle(currentObj);
                    nextObjs.addAll(parts);
                    if (parts.size() > 1)
                    {
                        didSplit = true;
                    }
                }
                else
                {
                    nextObjs.add(currentObj);
                }
            }
            currentObjs = nextObjs;
        }
        return currentObjs;
    }
    
    /**
     * Split a single OBJ into two parts. Depending on the exact splitting
     * strategy, this may not be possible in certain corner cases. In 
     * these cases, the returned list will contain only a single element. 
     * 
     * @param obj The input OBJ
     * @return The list containing the parts
     */
    private static List<Obj> splitSingle(ReadableObj obj)
    {
        logger.log(level,
            "Splitting OBJ with " + obj.getNumVertices() + " vertices");

        Predicate<ObjFace> facePredicate = computeFacePredicate(obj);
        List<ObjFace> faces0 = new ArrayList<>();
        List<ObjFace> faces1 = new ArrayList<>();
        for (int i = 0; i < obj.getNumFaces(); i++)
        {
            ObjFace face = obj.getFace(i);
            if (facePredicate.test(face))
            {
                faces0.add(face);
            }
            else
            {
                faces1.add(face);
            }
        }
        
        // When there are faces that are basically equal, then geometric 
        // splitting is not possible. In this case, just split the list 
        // of faces into two halves.
        if (faces0.isEmpty())
        {
            return split(obj, faces1);
        }
        else if (faces1.isEmpty())
        {
            return split(obj, faces0);
        }
        
        logger.log(level,
            "Split OBJ with " + obj.getNumFaces() + " faces "
            + "into " + faces0.size() + " and " + faces1.size() + " faces");
        
        Obj obj0 = ObjUtils.groupToObj(obj, asGroup(faces0), null);
        Obj obj1 = ObjUtils.groupToObj(obj, asGroup(faces1), null);
        return Arrays.asList(obj0, obj1);
    }

    /**
     * Split the given list of faces into two parts of approximately equal 
     * size, and return the {@link Obj} instances that correspond to the
     * subsets of these faces in the given {@link Obj}. If the given
     * list contains only a single element, then a single-element list
     * will be returned.
     *  
     * @param obj The input OBJ
     * @param allFaces The list of all faces
     * @return The list containing the parts
     */
    private static List<Obj> split(ReadableObj obj, List<ObjFace> allFaces)
    {
        if (allFaces.size() <= 1)
        {
            Obj obj0 = ObjUtils.groupToObj(obj, asGroup(allFaces), null);
            return Arrays.asList(obj0);
        }
        int centerIndex = (allFaces.size() + 1) / 2;
        List<ObjFace> faces0 = allFaces.subList(0, centerIndex);
        List<ObjFace> faces1 = allFaces.subList(centerIndex, allFaces.size());
        Obj obj0 = ObjUtils.groupToObj(obj, asGroup(faces0), null);
        Obj obj1 = ObjUtils.groupToObj(obj, asGroup(faces1), null);
        return Arrays.asList(obj0, obj1);
    }
    
    
    /**
     * Returns a new OBJ group that is a view on the given list
     * 
     * @param faces The list of faces
     * @return The OBJ group
     */
    private static ObjGroup asGroup(final List<? extends ObjFace> faces)
    {
        return new ObjGroup()
        {
            @Override
            public String getName()
            {
                return "default";
            }

            @Override
            public int getNumFaces()
            {
                return faces.size();
            }
            
            @Override
            public ObjFace getFace(int index)
            {
                return faces.get(index);
            }
        };
    }
    
    /**
     * Compute a predicate that splits the faces of the given OBJ into two
     * parts. Further details are not specified.
     * 
     * @param obj The OBJ
     * @return The predicate
     */
    private static Predicate<ObjFace> computeFacePredicate(
        final ReadableObj obj)
    {
        // Compute the projections of the centers of all faces along the axes
        float[] centersX = computeFaceCenters(obj, 0);
        float[] centersY = computeFaceCenters(obj, 1);
        float[] centersZ = computeFaceCenters(obj, 2);
        
        // Compute the mean of the projections along the axes
        final float meanX = arithmeticMean(centersX);
        final float meanY = arithmeticMean(centersY);
        final float meanZ = arithmeticMean(centersZ);
        
        // Compute the variances of the projections
        float varianceX = variance(centersX, meanX);
        float varianceY = variance(centersY, meanY);
        float varianceZ = variance(centersZ, meanZ);
        
        // Create a predicate that splits the faces based on their location
        // relative to the mean, along the axis with the largest variance
        if (varianceX >= varianceY && varianceX >= varianceZ)
        {
            return new Predicate<ObjFace>()
            {
                @Override
                public boolean test(ObjFace objFace)
                {
                    float faceCenterX = computeFaceCenter(obj, objFace, 0);
                    return faceCenterX >= meanX;
                }
            };
        }
        if (varianceY >= varianceX && varianceY >= varianceZ)
        {
            return new Predicate<ObjFace>()
            {
                @Override
                public boolean test(ObjFace objFace)
                {
                    float faceCenterY = computeFaceCenter(obj, objFace, 1);
                    return faceCenterY >= meanY;
                }
            };
        }
        return new Predicate<ObjFace>()
        {
            @Override
            public boolean test(ObjFace objFace)
            {
                float faceCenterZ = computeFaceCenter(obj, objFace, 2);
                return faceCenterZ >= meanZ;
            }
        };
        
    }
    
    /**
     * Compute the specified components of the center positions of the
     * faces of the given OBJ
     * 
     * @param obj The OBJ
     * @param component The component, 0,1 or 2 for x, y or z.
     * @return The face centers
     */
    private static float[] computeFaceCenters(ReadableObj obj, int component)
    {
        int n = obj.getNumFaces();
        float[] result = new float[n];
        for (int i = 0; i < n; i++)
        {
            ObjFace face = obj.getFace(i);
            result[i] = computeFaceCenter(obj, face, component);
        }
        return result;
    }    
    
    /**
     * Compute the specified component of the center position of the given 
     * OBJ face
     * 
     * @param obj The OBJ
     * @param face The face
     * @param component The component, 0,1 or 2 for x, y or z.
     * @return The center
     */
    private static float computeFaceCenter(
        ReadableObj obj, ObjFace face, int component)
    {
        float sum = 0;
        int n = face.getNumVertices();
        for (int i = 0; i < n; i++)
        {
            int vertexIndex = face.getVertexIndex(i);
            FloatTuple vertex = obj.getVertex(vertexIndex);
            sum += vertex.get(component);
        }
        return sum / n;
    }
    
    
    /**
     * Returns the arithmetic mean of the given array
     *
     * @param array The input array
     * @return The mean
     */
    private static float arithmeticMean(float[] array)
    {
        float sum = 0;
        for (float value : array)
        {
            sum += value;
        }
        return sum / array.length;
    }

    /**
     * Returns the bias-corrected sample variance of the given array.
     *
     * @param array The input array
     * @param mean The mean
     * @return The variance
     */
    private static float variance(float[] array, float mean)
    {
        float variance = 0;
        for (float v : array) {
            double difference = v - mean;
            variance += difference * difference;
        }
        return variance / (array.length - 1);
    }    
    
}
