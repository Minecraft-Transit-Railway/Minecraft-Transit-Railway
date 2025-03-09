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
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * A class that may write an {@link ReadableObj} to a stream.
 */
public class ObjWriter
{
    /**
     * Writes the given {@link ReadableObj} to the given stream. The caller
     * is responsible for closing the stream.
     * 
     * @param input The {@link ReadableObj} to write.
     * @param outputStream The stream to write to.
     * @throws IOException If an IO error occurs.
     */
    public static void write(ReadableObj input, OutputStream outputStream) 
        throws IOException
    {
        OutputStreamWriter outputStreamWriter = 
            new OutputStreamWriter(outputStream);
        write(input, outputStreamWriter);
    }
    
    /**
     * Writes the given {@link ReadableObj} to the given writer. The caller
     * is responsible for closing the writer.
     * 
     * @param input The {@link ReadableObj} to write.
     * @param writer The writer to write to.
     * @throws IOException If an IO error occurs.
     */
    public static void write(ReadableObj input, Writer writer) 
        throws IOException
    {
        // Write the mtl file name
        List<String> mtlFileNames = input.getMtlFileNames();
        if (!mtlFileNames.isEmpty())
        {
            writer.write("mtllib ");
            for (int i=0; i<mtlFileNames.size(); i++)
            {
                if (i > 0)
                {
                    writer.write(" ");
                }
                writer.write(mtlFileNames.get(i));
            }
            writer.write("\n");
        }
        
        // Write the vertex- texture coordinate and normal data
        for(int i = 0; i < input.getNumVertices(); i++)
        {
            FloatTuple vertex = input.getVertex(i);
            writer.write(
                "v "+FloatTuples.createString(vertex) + "\n");
        }
        for(int i = 0; i < input.getNumTexCoords(); i++)
        {
            FloatTuple texCoord = input.getTexCoord(i);
            writer.write(
                "vt "+FloatTuples.createString(texCoord) + "\n");
        }
        for(int i = 0; i < input.getNumNormals(); i++)
        {
            FloatTuple normal = input.getNormal(i);
            writer.write(
                "vn "+FloatTuples.createString(normal) + "\n");
        }

        boolean skipWritingDefaultGroup = true; 
        for(int i = 0; i < input.getNumFaces(); i++)
        {
            ObjFace face = input.getFace(i);
            
            Set<String> activatedGroupNames = 
                input.getActivatedGroupNames(face);
            if (activatedGroupNames != null)
            {
                boolean isDefaultGroup = 
                    activatedGroupNames.equals(
                        Collections.singleton("default"));
                if (!skipWritingDefaultGroup || !isDefaultGroup)
                {
                    writer.write("g ");
                    for (String activatedGroupName : activatedGroupNames)
                    {
                        writer.write(activatedGroupName);
                        writer.write(" ");
                    }
                    writer.write("\n");
                }
                skipWritingDefaultGroup = false;
            }
                
            String activatedMaterialGroupName =
                input.getActivatedMaterialGroupName(face);
            if (activatedMaterialGroupName != null)
            {
                writer.write(
                    "usemtl " + activatedMaterialGroupName + "\n");
            }
            String faceString = ObjFaces.createString(face);
            writer.write(faceString + "\n");
        }
        writer.flush();
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private ObjWriter()
    {
        // Private constructor to prevent instantiation
    }
}
