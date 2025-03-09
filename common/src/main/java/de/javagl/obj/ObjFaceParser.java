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
import java.util.Arrays;

/**
 * A class for reading the index data for an {@link ObjFace} from an 
 * <code>'f'</code>-line that was read from an OBJ file
 */
final class ObjFaceParser
{
    /**
     * The initial size for the index buffers
     */
    private static final int INITIAL_BUFFER_SIZE = 6;
    
    /**
     * Buffer for vertex indices
     */
    private int[] vertexIndexBuffer = new int[INITIAL_BUFFER_SIZE];

    /**
     * Buffer for texture coordinates
     */
    private int[] texCoordIndexBuffer = new int[INITIAL_BUFFER_SIZE];

    /**
     * Buffer normal indices
     */
    private int[] normalIndexBuffer = new int[INITIAL_BUFFER_SIZE];
    
    /**
     * Flag whether texture coordinates have been found during the last
     * call to {@link #parse(String)}
     */
    private boolean foundTexCoordIndices = false;

    /**
     * Flag whether normal indices have been found during the last
     * call to {@link #parse(String)}
     */
    private boolean foundNormalIndices = false;
    
    /**
     * Counter for the vertices
     */
    private int vertexCounter = 0;

    /**
     * Index in the input array
     */
    private int idx = 0;
    
    /**
     * The input array to parse
     */
    private char[] lineData;

    
    /**
     * Parse the given <code>'f'</code>-line that was read from an OBJ file
     *   
     * @param line The line
     * @throws IOException If the given line can not be parsed
     */
    void parse(String line) throws IOException
    {
        parseLine(line);
    }

    /**
     * Returns a new array containing the vertex indices that have
     * been parsed during the last call to 
     * {@link #parse(String)}.
     * 
     * @return The vertex indices
     */
    int[] getVertexIndices()
    {
        return Arrays.copyOf(vertexIndexBuffer, vertexCounter);
    }
    
    /**
     * Returns a new array containing the texCoord indices that have
     * been parsed during the last call to 
     * {@link #parse(String)}, or <code>null</code> 
     * if no texture coordinate indices have been read
     * 
     * @return The texCord indices
     */
    int[] getTexCoordIndices()
    {
        if (foundTexCoordIndices)
        {
            return Arrays.copyOf(texCoordIndexBuffer, vertexCounter);
        }
        return null;
    }
    
    /**
     * Returns a new array containing the normal indices that have
     * been parsed during the last call to 
     * {@link #parse(String)}, or <code>null</code> 
     * if no normal indices have been read
     * 
     * @return The normal indices
     */
    int[] getNormalIndices()
    {
        if(foundNormalIndices)
        {
            return Arrays.copyOf(normalIndexBuffer, vertexCounter);
        }
        return null;
    }
    
    /**
     * Parse the Face from the given line <br>
     * f v0/vt0/vn0 ... vN/vtN/vnN <br>
     *
     * @param line String
     * @throws IOException If the line could not be parsed
     */
    void parseLine(String line) throws IOException
    {
        foundTexCoordIndices = false;
        foundNormalIndices = false;
        vertexCounter = 0;
        idx = 0;
        lineData = line.toCharArray();


        skipSpaces();
        if(endOfInput())
        {
            // Empty line
            return;
        }

        // Read the leading 'f' or 'F'
        if(lineData[idx] != 'f' && lineData[idx] != 'F')
        {
            throw new IOException(
                "Expected 'f' or 'F', but found '" + lineData[idx] +
                " in \""+line+"\"");
        }
        idx++;

        // Read all vertex v/vt/vn triples
        int count = 0;
        while(true)
        {
            skipSpaces();
            if(endOfInput())
            {
                break;
            }
            
            // Read the vertex index
            int vertexIndex = parseNonzeroInt();
            if (vertexIndex == 0)
            {
                throw new IOException(
                    "Could not read vertex index in \""+line+"\"");
            }
            if (count >= vertexIndexBuffer.length)
            {
                vertexIndexBuffer = 
                    Arrays.copyOf(vertexIndexBuffer, count+1);
                texCoordIndexBuffer = 
                    Arrays.copyOf(texCoordIndexBuffer, count+1);
                normalIndexBuffer = 
                    Arrays.copyOf(normalIndexBuffer, count+1);
            }
            if (vertexIndex != 0)
            {
                vertexIndexBuffer[count] = vertexIndex;
            }
            vertexCounter = count + 1;

            skipSpaces();
            if(endOfInput())
            {
                break;
            }

            // Read the texCoord index
            if(lineData[idx] == '/')
            {
                idx++;

                skipSpaces();
                if(endOfInput())
                {
                    throw new IOException(
                        "Unexpected end of input after '/' " +
                        "in  \""+line+"\"");
                }
                
                int texCoordIndex = parseNonzeroInt();

                // It is not an error if texCoordIndex == 0: The indices
                // may be given as "1//2", and thus not contain an 
                // texCoordIndex
                
                if(texCoordIndex != 0)
                {
                    texCoordIndexBuffer[count] = texCoordIndex;
                    foundTexCoordIndices = true;
                }

                skipSpaces();
                if(endOfInput())
                {
                    break;
                }

                // Read the normal index
                if(lineData[idx] == '/')
                {
                    idx++;

                    skipSpaces();
                    if(endOfInput())
                    {
                        throw new IOException(
                            "Unexpected end of input after '/' " + 
                            "in  \""+line+"\"");
                    }

                    int normalIndex = parseNonzeroInt();
                    if(normalIndex == 0)
                    {
                        throw new IOException(
                            "Could not read normal index from \""+line+"\"");
                    }
                    foundNormalIndices = true;
                    if (normalIndex != 0)
                    {
                        normalIndexBuffer[count] = normalIndex;
                    }
                }
            }
            count++;
        }
    }

    /**
     * Returns whether the end of the input was reached
     * 
     * @return 'true' iff the end of the input was reached
     */
    private boolean endOfInput()
    {
        return idx >= lineData.length;
    }

    /**
     * Skips all space characters until the first non-space character is 
     * found or the end of the input is reached
     */
    private void skipSpaces()
    {
        while (!endOfInput() && lineData[idx] == ' ')
        {
            idx++;
        }
    }

    /**
     * Returns the next int in the input, or 0 if no int could be 
     * read
     *  
     * @return The next int in the input 
     */
    private int parseNonzeroInt()
    {
        int parsedInt = 0;
        boolean negate = false;
        if (lineData[idx] == '-')
        {
            negate = true;
            idx++;
            skipSpaces();
            if(endOfInput())
            {
                return 0;
            }
        }
        if(lineData[idx] >= '0' && lineData[idx] <= '9')
        {
            parsedInt = (lineData[idx] - '0');
            idx++;
            while(!endOfInput() && 
                  lineData[idx] >= '0' && 
                  lineData[idx] <= '9')
            {
                parsedInt *= 10;
                parsedInt += (lineData[idx] - '0');
                idx++;
            }
        }
        return negate ? -parsedInt : parsedInt;
    }

}
