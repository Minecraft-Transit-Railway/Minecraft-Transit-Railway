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
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * Utility methods for reading and parsing
 */
class Utils
{
    /**
     * Reads a float tuple from the given StringTokenizer
     *
     * @param st The StringTokenizer
     * @return The FloatTuple
     * @throws IOException If the tuple can not be read
     */
    static FloatTuple readFloatTuple(StringTokenizer st)
        throws IOException
    {
        float x = parseFloat(st.nextToken());
        if (st.hasMoreTokens())
        {
            float y = parseFloat(st.nextToken());

            if (st.hasMoreTokens())
            {
                float z = parseFloat(st.nextToken());

                if (st.hasMoreTokens())
                {
                    float w = parseFloat(st.nextToken());
                    return FloatTuples.create(x,y,z,w);
                }
                return FloatTuples.create(x,y,z);
            }
            return FloatTuples.create(x,y);
        }
        return FloatTuples.create(x);
    }

    /**
     * Parse a float from the given string, wrapping number format
     * exceptions into an IOException
     *
     * @param s The string
     * @return The float
     * @throws IOException If the string does not contain a valid float value
     */
    static float parseFloat(String s) throws IOException
    {
        try
        {
            return Float.parseFloat(s);
        }
        catch (NumberFormatException e)
        {
            throw new IOException(e);
        }
    }

    /**
     * Returns whether the given string can be parsed into a float value
     *
     * @param s The string
     * @return Whether the string is a float value. If the given string is
     * <code>null</code>, then <code>false</code> is returned.
     */
    private static boolean isFloat(String s)
    {
        if (s == null)
        {
            return false;
        }
        try
        {
            Float.parseFloat(s);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    /**
     * Parse up to <code>max</code> float values from the given tokens.
     * If there are fewer than <code>max</code> tokens, then the
     * resulting values will be <code>null</code>. The parsing will
     * stop when a value is encountered that is not a float value.
     *
     * @param tokens The input tokens
     * @param max The maximum number of tokens to process
     * @return The array containing the parsed values
     */
    static Float[] parseFloats(Queue<String> tokens, int max)
    {
        Float[] result = new Float[max];
        for (int i = 0; i < max; i++)
        {
            String token = tokens.poll();
            if (Utils.isFloat(token))
            {
                float value = Float.parseFloat(token);
                result[i] = value;
            }
        }
        return result;
    }

    /**
     * Parse a boolean value from the given string, converting
     * <code>"true"<code> and <code>"on"<code> to <code>true</code>,
     * and anything else to <code>false</code>.
     *
     * @param s The string
     * @return The boolean value
     */
    static boolean parseBoolean(String s)
    {
        if ("true".equalsIgnoreCase(s))
        {
            return true;
        }
        if ("on".equalsIgnoreCase(s))
        {
            return true;
        }
        return false;
    }


    /**
     * Parse an int from the given string, wrapping number format
     * exceptions into an IOException
     *
     * @param s The string
     * @return The int
     * @throws IOException If the string does not contain a valid int value
     */
    static int parseInt(String s) throws IOException
    {
        try
        {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException e)
        {
            throw new IOException(e);
        }
    }

    /**
     * Creates a {@link FloatTuple} from the given RGB values, treating
     * optional values as described in the MTL specification: If
     * the <code>r</code> component is <code>null</code>, then
     * <code>null</code> is returned. If the <code>g</code> or
     * <code>b</code> component is null, then the <code>r</code>
     * component will be used instead.
     *
     * @param r The r-component
     * @param g The g-component
     * @param b The b-component
     * @return The {@link FloatTuple}
     */
    static FloatTuple createRgbTuple(Float r, Float g, Float b)
    {
        if (r == null)
        {
            return null;
        }
        float fr = r;
        float fg = r;
        float fb = r;
        if (g != null)
        {
            fg = g;
        }
        if (b != null)
        {
            fb = b;
        }
        return FloatTuples.create(fr, fg, fb);
    }

    /**
     * Creates a {@link FloatTuple} from the given UVW values, treating
     * optional values as described in the MTL specification: If
     * the <code>u</code> component is <code>null</code>, then
     * <code>null</code> is returned. If the <code>v</code> or
     * <code>w</code> component is null, then the given default value
     * will be used.
     *
     * @param u The u-component
     * @param v The v-component
     * @param w The w-component
     * @param defaultValue The default value for v and w
     * @return The {@link FloatTuple}
     */
    static FloatTuple createUvwTuple(
        Float u, Float v, Float w, float defaultValue)
    {
        if (u == null)
        {
            return null;
        }
        float fu = u;
        float fv = (v == null ? defaultValue : v);
        float fw = (w == null ? defaultValue : w);
        return FloatTuples.create(fu, fv, fw);
    }


    /**
     * Private constructor to prevent instantiation
     */
    private Utils()
    {
        // Private constructor to prevent instantiation
    }

}
