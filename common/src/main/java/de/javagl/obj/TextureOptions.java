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
 * Interface for the texture options that are part of a map definition
 * of an {@link Mtl}. For details about the semantics of the properties
 * in this interface, refer to the MTL specification.
 */
public interface TextureOptions
{
    /**
     * Returns the file name of the texture
     *
     * @return The file name
     */
    String getFileName();

    /**
     * Set the file name of the texture
     *
     * @param fileName The file name
     */
    void setFileName(String fileName);

    /**
     * Returns the horizontal texture blending state (<code>-blendu</code>),
     * or <code>null</code> if it was not specified
     *
     * @return Whether horizontal blending is enabled
     */
    Boolean isBlendu();

    /**
     * Set the horizontal texture blending state
     *
     * @param blendu The blending state
     */
    void setBlendu(Boolean blendu);

    /**
     * Returns the vertical texture blending state (<code>-blendv</code>),
     * or <code>null</code> if it was not specified
     *
     * @return Whether vertical blending is enabled
     */
    Boolean isBlendv();

    /**
     * Set the vertical texture blending state
     *
     * @param blendv The blending state
     */
    void setBlendv(Boolean blendv);

    /**
     * Returns the color correction state (<code>-cc</code>),
     * or <code>null</code> if it was not specified
     *
     * @return The color correction state
     */
    Boolean isCc();

    /**
     * Set the color correction state
     *
     * @param cc The color correction state
     */
    void setCc(Boolean cc);

    /**
     * Returns the boost mip-map sharpness value (<code>-boost</code>),
     * or <code>null</code> if it was not specified
     *
     * @return The boost mip-map sharpness.
     */
    Float getBoost();

    /**
     * Set the mip-map boost value
     *
     * @param boost The boost value
     */
    void setBoost(Float boost);

    /**
     * Returns the texture map modifier (<code>-mm</code>),
     * or <code>null</code> if it was not specified
     *
     * @return The modified texture map contrast.
     */
    FloatTuple getMm();

    /**
     * Set the texture map modifier values
     *
     * @param base The base
     * @param gain The gain
     */
    void setMm(Float base, Float gain);

    /**
     * Returns the origin offset (<code>-o</code>),
     * or <code>null</code> if it was not specified
     *
     * @return The origin offset.
     */
    FloatTuple getO();

    /**
     * Set the origin offset
     *
     * @param u The u component
     * @param v The v component
     * @param w The w component
     */
    void setO(Float u, Float v, Float w);

    /**
     * Returns the scale (<code>-s</code>),
     * or <code>null</code> if it was not specified
     *
     * @return The scale.
     */
    FloatTuple getS();

    /**
     * Set the scale
     *
     * @param u The u component
     * @param v The v component
     * @param w The w component
     */
    void setS(Float u, Float v, Float w);

    /**
     * Returns the turbulence (<code>-t</code>),
     * or <code>null</code> if it was not specified
     *
     * @return The turbulence.
     */
    FloatTuple getT();

    /**
     * Set the turbulence
     *
     * @param u The u component
     * @param v The v component
     * @param w The w component
     */
    void setT(Float u, Float v, Float w);

    /**
     * Returns the texture resolution (<code>-texres</code>),
     * or <code>null</code> if it was not specified
     *
     * @return The texture resolution.
     */
    Float getTexres();

    /**
     * Set the texture resolution
     *
     * @param texres The texture resolution
     */
    void setTexres(Float texres);

    /**
     * Returns the clamping state (<code>-clamp</code>),
     * or <code>null</code> if it was not specified
     *
     * @return Whether or not clamping is enabled.
     */
    Boolean isClamp();

    /**
     * Set the clamping state
     *
     * @param clamp The clamping state
     */
    void setClamp(Boolean clamp);

    /**
     * Returns the bump multiplier (<code>-bm</code>),
     * or <code>null</code> if it was not specified
     *
     * @return The bump multiplier.
     */
    Float getBm();

    /**
     * Set the bump multiplier
     *
     * @param bm The bump multiplier
     */
    void setBm(Float bm);

    /**
     * Returns the IMF channel to use (<code>-imfchan</code>),
     * or <code>null</code> if it was not specified
     *
     * @return The IMF channel to use.
     */
    String getImfchan();

    /**
     * Set the IMF channel
     *
     * @param imfchan The IMF channel
     */
    void setImfchan(String imfchan);

    /**
     * Returns the type of texture map (<code>-type</code>),
     * or <code>null</code> if it was not specified
     *
     * @return The type of texture map
     */
    String getType();

    /**
     * Set the type
     *
     * @param type The type
     */
    void setType(String type);

}

