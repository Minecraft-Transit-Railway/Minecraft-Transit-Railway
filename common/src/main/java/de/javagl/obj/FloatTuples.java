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
 * Methods to create {@link FloatTuple} instances
 */
public class FloatTuples {
	/**
	 * Create a copy of the given {@link FloatTuple}
	 *
	 * @param other The other tuple
	 * @return The {@link FloatTuple}
	 */
	public static FloatTuple copy(FloatTuple other) {
		return new DefaultFloatTuple(other);
	}

	/**
	 * Create a new {@link FloatTuple} with the given coordinate
	 *
	 * @param x The x-coordinate
	 * @return The {@link FloatTuple}
	 */
	public static FloatTuple create(float x) {
		return new DefaultFloatTuple(x);
	}

	/**
	 * Create a new {@link FloatTuple} with the given coordinates
	 *
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 * @return The {@link FloatTuple}
	 */
	public static FloatTuple create(float x, float y) {
		return new DefaultFloatTuple(x, y);
	}

	/**
	 * Create a new {@link FloatTuple} with the given coordinates
	 *
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 * @param z The z-coordinate
	 * @return The {@link FloatTuple}
	 */
	public static FloatTuple create(float x, float y, float z) {
		return new DefaultFloatTuple(x, y, z);
	}

	/**
	 * Create a new {@link FloatTuple} with the given coordinates
	 *
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 * @param z The z-coordinate
	 * @param w The w-coordinate
	 * @return The {@link FloatTuple}
	 */
	public static FloatTuple create(float x, float y, float z, float w) {
		return new DefaultFloatTuple(x, y, z, w);
	}


	/**
	 * Returns the string for the given tuple that is used for representing
	 * the given tuple in an OBJ file
	 *
	 * @param tuple The tuple
	 * @return The string for the given tuple
	 */
	public static String createString(FloatTuple tuple) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tuple.dimensions(); i++) {
			if (i > 0) {
				sb.append(" ");
			}
			sb.append(tuple.get(i));
		}
		return sb.toString();
	}

	/**
	 * Private constructor to prevent instantiation
	 */
	private FloatTuples() {
		// Private constructor to prevent instantiation
	}
}
