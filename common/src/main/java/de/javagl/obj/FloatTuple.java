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
 * Interface for tuples consisting of float values
 */
public interface FloatTuple {
	/**
	 * Return the x-component of this tuple
	 *
	 * @return The x-component of this tuple
	 * @throws IndexOutOfBoundsException If this tuple has less than 1
	 *                                   dimension
	 */
	float getX();

	/**
	 * Return the y-component of this tuple
	 *
	 * @return The y-component of this tuple
	 * @throws IndexOutOfBoundsException If this tuple has less than 2
	 *                                   dimensions
	 */
	float getY();

	/**
	 * Return the z-component of this tuple
	 *
	 * @return The z-component of this tuple
	 * @throws IndexOutOfBoundsException If this tuple has less than 3
	 *                                   dimensions
	 */
	float getZ();

	/**
	 * Return the w-component of this tuple
	 *
	 * @return The w-component of this tuple
	 * @throws IndexOutOfBoundsException If this tuple has less than 4
	 *                                   dimensions
	 */
	float getW();

	/**
	 * Return the specified component of this tuple
	 *
	 * @param index The index of the component
	 * @return The specified component of this tuple
	 * @throws IndexOutOfBoundsException If the given index is negative
	 *                                   or not smaller than the {@link #dimensions() dimensions}
	 */
	float get(int index);

	/**
	 * Return the dimensions of this tuple
	 *
	 * @return The dimensions of this tuple
	 */
	int dimensions();
}
