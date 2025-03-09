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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A class that may read OBJ data from a stream and
 * store the read data in an {@link WritableObj}.
 */
public class ObjReader
{
    /**
     * Read the OBJ data from the given stream and return it as an {@link Obj}.
     * The caller is responsible for closing the given stream.
     *
     * @param inputStream The stream to read from
     * @return The {@link Obj}
     * @throws IOException If an IO error occurs
     */
    public static Obj read(InputStream inputStream) throws IOException
    {
        return read(inputStream, Objs.create());
    }

    /**
     * Read the OBJ data from the given stream and store the read
     * elements in the given {@link WritableObj}.
     * The caller is responsible for closing the given stream.
     *
     * @param <T> The output type
     * @param inputStream The stream to read from
     * @param output The {@link WritableObj} to store the read data
     * @return The output
     * @throws IOException If an IO error occurs
     */
    public static <T extends WritableObj> T read(
        InputStream inputStream, T output)
        throws IOException
    {
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(inputStream, StandardCharsets.US_ASCII));
        return readImpl(reader, output);
    }

    /**
     * Read the OBJ data from the given reader and return it as an {@link Obj}.
     * The caller is responsible for closing the given reader.
     *
     * @param reader The reader to read from
     * @return The {@link Obj}
     * @throws IOException If an IO error occurs
     */
    public static Obj read(Reader reader) throws IOException
    {
        return read(reader, Objs.create());
    }

    /**
     * Read the OBJ data from the given reader and store the read
     * elements in the given {@link WritableObj}.
     * The caller is responsible for closing the given reader.
     *
     * @param <T> The output type
     * @param reader The reader to read from
     * @param output The {@link WritableObj} to store the read data
     * @return The output
     * @throws IOException If an IO error occurs
     */
    public static <T extends WritableObj> T read(
        Reader reader, T output)
        throws IOException
    {
        if (reader instanceof BufferedReader)
        {
            return readImpl((BufferedReader)reader, output);
        }
        return readImpl(new BufferedReader(reader), output);

    }

    /**
     * Read the OBJ data from the given reader and store the read
     * elements in the given {@link WritableObj}.
     * The caller is responsible for closing the given reader.
     *
     * @param <T> The output type
     * @param reader The reader to read from
     * @param output The {@link WritableObj} to store the read data
     * @return The output
     * @throws IOException If an IO error occurs
     */
    private static <T extends WritableObj> T readImpl(
        BufferedReader reader, T output)
        throws IOException
    {
        ObjFaceParser objFaceParser = new ObjFaceParser();String groupOrObject = "";

        int vertexCounter = 0;
        int texCoordCounter = 0;
        int normalCounter = 0;
        while(true)
        {
            String line = reader.readLine();
            if(line == null)
            {
                break;
            }

            line = line.trim();

            //System.out.println("read line: "+line);

            // Combine lines that have been broken
            boolean finished = false;
            while(line.endsWith("\\"))
            {
                line = line.substring(0, line.length() - 2);
                String nextLine = reader.readLine();
                if (nextLine == null)
                {
                    finished = true;
                    break;
                }
                line += " " + nextLine;
            }
            if (finished)
            {
                break;
            }

            StringTokenizer st = new StringTokenizer(line);
            if(!st.hasMoreTokens())
            {
                continue;
            }

            String identifier = st.nextToken().toLowerCase();

            // v: Vertex coordinates
            switch (identifier) {
                case "v":
                    output.addVertex(Utils.readFloatTuple(st));
                    vertexCounter++;
                    break;

                // vt: Texture coordinates for a vertex
                case "vt":
                    output.addTexCoord(Utils.readFloatTuple(st));
                    texCoordCounter++;
                    break;

                // vn: Vertex normal
                case "vn":
                    output.addNormal(Utils.readFloatTuple(st));
                    normalCounter++;
                    break;

                // mtllib: Name of the MTL file
                case "mtllib": {
                    String s = line.substring(6).trim();
                    //output.setMtlFileNames(readStrings(s));
                    // According to the OBJ specification, the "mtllib" keyword
                    // may be followed by multiple file names, separated with
                    // whitespaces:
                    // "When you assign a material library using the Model
                    //  program, only one map library per .obj file is allowed.
                    //  You can assign multiple libraries using a text editor."
                    // However, to avoid problems with file names that contain
                    // whitespaces, only ONE file name is assumed here:
                    output.setMtlFileNames(Collections.singleton(s));
                    break;
                }

                // usemtl: Material groups
                case "usemtl":
                    String materialGroupName = line.substring(6).trim();
                    output.setActiveMaterialGroupName(materialGroupName);
                    break;

                // g: Geometry groups
                case "g":case "o": if (!groupOrObject.equals(identifier) && !groupOrObject.isEmpty()) break; {
                    String s = line.substring(1).trim();
                    String[] groupNames = readStrings(s);
                    output.setActiveGroupNames(Arrays.asList(groupNames));groupOrObject = identifier;
                    break;
                }

                // f: A face definition
                case "f":
                    objFaceParser.parse(line);
                    int[] v = objFaceParser.getVertexIndices();
                    int[] vt = objFaceParser.getTexCoordIndices();
                    int[] vn = objFaceParser.getNormalIndices();
                    makeIndicesAbsolute(v, vertexCounter);
                    makeIndicesAbsolute(vt, texCoordCounter);
                    makeIndicesAbsolute(vn, normalCounter);
                    output.addFace(ObjFaces.create(v, vt, vn));
                    break;
            }
        }
        return output;
    }

    /**
     * Convert the indices in the given array to be absolute (non-negative)
     * and zero-based. This means that negative values are made positive
     * by adding the given count, and positive values are decreased by one.
     *
     * @param array The array. If this is <code>null</code>, nothing will
     * be done
     * @param count The count
     */
    private static void makeIndicesAbsolute(int[] array, int count)
    {
        if (array == null)
        {
            return;
        }
        for (int i=0; i<array.length; i++)
        {
            if (array[i] < 0)
            {
                array[i] = count + array[i];
            }
            else
            {
                array[i]--;
            }
        }
    }


    /**
     * Read all tokens from the given input string that are separated
     * by whitespaces
     *
     * @param input The input string
     * @return The list of tokens
     */
    private static String[] readStrings(String input)
    {
        StringTokenizer st = new StringTokenizer(input);
        List<String> tokens = new ArrayList<>();
        while (st.hasMoreTokens())
        {
            tokens.add(st.nextToken());
        }
        return tokens.toArray(new String[tokens.size()]);
    }

    /**
     * Private constructor to prevent instantiation
     */
    private ObjReader()
    {
        // Private constructor to prevent instantiation
    }

}
