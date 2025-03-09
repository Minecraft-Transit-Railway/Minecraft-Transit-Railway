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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Default implementation of an {@link Obj}
 */
final class DefaultObj implements Obj
{
    /**
     * The vertices in this Obj
     */
    private final List<FloatTuple> vertices;
    
    /**
     * The texture coordinates in this Obj.
     */
    private final List<FloatTuple> texCoords;

    /**
     * The normals in this Obj
     */
    private final List<FloatTuple> normals;

    /**
     * The faces in this Obj.
     */
    private final List<ObjFace> faces;

    /**
     * The groups in this Obj.
     */
    private final List<ObjGroup> groups;
    
    /**
     * The material groups in this Obj.
     */
    private final List<ObjGroup> materialGroups;

    /** 
     * Maps a group name to a group 
     */
    private final Map<String, DefaultObjGroup> groupMap;

    /** 
     * Maps a material name to a material group 
     */
    private final Map<String, DefaultObjGroup> materialGroupMap;

    /**
     * The names of the MTL files for this Obj.
     */
    private List<String> mtlFileNames = Collections.emptyList();
    
    /**
     * A map from the faces to the names of the groups that started
     * at this face
     */
    private final Map<ObjFace, Set<String>> startedGroupNames;

    /**
     * A map from the faces to the name of the material group that started
     * at this face
     */
    private final Map<ObjFace, String> startedMaterialGroupNames;
    
    /**
     * The names for the groups that should be used for faces that are
     * added subsequently 
     */
    private Set<String> nextActiveGroupNames = null;
    
    /**
     * The name for the material group that should be used for faces that are
     * added subsequently 
     */
    private String nextActiveMaterialGroupName = null;

    /**
     * The groups that are currently active, and to which faces will be
     * added 
     */
    private List<DefaultObjGroup> activeGroups = null;

    /** 
     * The names of the groups that faces are currently added to
     */
    private Set<String> activeGroupNames = null;

    /**
     * The material group that is currently active, and to which faces will be
     * added 
     */
    private DefaultObjGroup activeMaterialGroup = null;
    
    /**
     * The name of the material group that is currently active
     */
    private String activeMaterialGroupName = null;

    /**
     * Creates a new, empty DefaultObj.
     */
    DefaultObj()
    {
        vertices = new ArrayList<>();
        normals = new ArrayList<>();
        texCoords = new ArrayList<>();
        faces = new ArrayList<>();

        groups = new ArrayList<>();
        materialGroups = new ArrayList<>();

        groupMap = new LinkedHashMap<>();
        materialGroupMap = new LinkedHashMap<>();
        
        startedGroupNames = new HashMap<>();
        startedMaterialGroupNames = new HashMap<>();
        
        setActiveGroupNames(Arrays.asList("default"));
        getGroupInternal("default");
    }


    @Override
    public int getNumVertices()
    {
        return vertices.size();
    }

    @Override
    public FloatTuple getVertex(int index)
    {
        return vertices.get(index);
    }

    @Override
    public int getNumTexCoords()
    {
        return texCoords.size();
    }

    @Override
    public FloatTuple getTexCoord(int index)
    {
        return texCoords.get(index);
    }

    @Override
    public int getNumNormals()
    {
        return normals.size();
    }

    @Override
    public FloatTuple getNormal(int index)
    {
        return normals.get(index);
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
    
    @Override
    public Set<String> getActivatedGroupNames(ObjFace face)
    {
        return startedGroupNames.get(face);
    }
    
    @Override
    public String getActivatedMaterialGroupName(ObjFace face)
    {
        return startedMaterialGroupNames.get(face);
    }

    @Override
    public int getNumGroups()
    {
        return groups.size();
    }

    @Override
    public ObjGroup getGroup(int index)
    {
        return groups.get(index);
    }

    @Override
    public ObjGroup getGroup(String name)
    {
        return groupMap.get(name);
    }

    @Override
    public int getNumMaterialGroups()
    {
        return materialGroups.size();
    }

    @Override
    public ObjGroup getMaterialGroup(int index)
    {
        return materialGroups.get(index);
    }

    @Override
    public ObjGroup getMaterialGroup(String name)
    {
        return materialGroupMap.get(name);
    }


    @Override
    public List<String> getMtlFileNames()
    {
        return mtlFileNames;
    }


    
    
    @Override
    public void addVertex(FloatTuple vertex)
    {
        Objects.requireNonNull(vertex, "The vertex is null");
        vertices.add(vertex);
    }
    
    @Override
    public void addVertex(float x, float y, float z)
    {
        vertices.add(new DefaultFloatTuple(x, y, z));
    }
    
    @Override
    public void addTexCoord(FloatTuple texCoord)
    {
        Objects.requireNonNull(texCoord, "The texCoord is null");
        texCoords.add(texCoord);
    }
    
    @Override
    public void addTexCoord(float x)
    {
        texCoords.add(new DefaultFloatTuple(x));
    }
    
    @Override
    public void addTexCoord(float x, float y)
    {
        texCoords.add(new DefaultFloatTuple(x, y));
    }
    
    @Override
    public void addTexCoord(float x, float y, float z)
    {
        texCoords.add(new DefaultFloatTuple(x, y, z));
    }
    

    @Override
    public void addNormal(FloatTuple normal)
    {
        Objects.requireNonNull(normal, "The normal is null");
        normals.add(normal);
    }

    @Override
    public void addNormal(float x, float y, float z)
    {
        normals.add(new DefaultFloatTuple(x, y, z));
    }
    
    @Override
    public void setActiveGroupNames(Collection<? extends String> groupNames)
    {
        if (groupNames == null)
        {
            return;
        }
        if (groupNames.size() == 0)
        {
            groupNames = Arrays.asList("default");
        }
        else if (groupNames.contains(null))
        {
            throw new NullPointerException("The groupNames contains null");
        }
        nextActiveGroupNames = 
            Collections.unmodifiableSet(new LinkedHashSet<String>(groupNames));
    }
    
    
    @Override
    public void setActiveMaterialGroupName(String materialGroupName)
    {
        if (materialGroupName == null)
        {
            return;
        }
        nextActiveMaterialGroupName = materialGroupName;
    }
    
    @Override
    public void addFace(ObjFace face)
    {
        if (face == null)
        {
            throw new NullPointerException("The face is null");
        }
        if (nextActiveGroupNames != null)
        {
            activeGroups = getGroupsInternal(nextActiveGroupNames);
            if (!nextActiveGroupNames.equals(activeGroupNames))
            {
                startedGroupNames.put(face, nextActiveGroupNames);startedMaterialGroupNames.put(face, activeMaterialGroupName);
            }
            activeGroupNames = nextActiveGroupNames;
            nextActiveGroupNames = null;
        }
        if (nextActiveMaterialGroupName != null)
        {
            activeMaterialGroup = 
                getMaterialGroupInternal(nextActiveMaterialGroupName);
            if (!nextActiveMaterialGroupName.equals(activeMaterialGroupName))
            {
                startedMaterialGroupNames.put(face, nextActiveMaterialGroupName);
            }
            activeMaterialGroupName = nextActiveMaterialGroupName;
            nextActiveMaterialGroupName = null;
        }
        faces.add(face);
        if (activeMaterialGroup != null)
        {
            activeMaterialGroup.addFace(face);
        }
        for (DefaultObjGroup group : activeGroups)
        {
            group.addFace(face);
        }
    }
    

    @Override
    public void addFace(int ... v)
    {
        addFace(v, null, null);
    }

    @Override
    public void addFaceWithTexCoords(int... v)
    {
        addFace(v, v, null);
    }

    @Override
    public void addFaceWithNormals(int... v)
    {
        addFace(v, null, v);
    }

    @Override
    public void addFaceWithAll(int... v)
    {
        addFace(v, v, v);
    }
    
    @Override
    public void addFace(int[] v, int[] vt, int[] vn)
    {
        Objects.requireNonNull(v, "The vertex indices are null");
        checkIndices(v, getNumVertices(), "Vertex");
        checkIndices(vt, getNumTexCoords(), "TexCoord");
        checkIndices(vn, getNumNormals(), "Normal");
        DefaultObjFace face = new DefaultObjFace(v, vt, vn);
        addFace(face);
    }
    

    @Override
    public void setMtlFileNames(Collection<? extends String> mtlFileNames)
    {
        this.mtlFileNames = Collections.unmodifiableList(
            new ArrayList<String>(mtlFileNames));
    }

    
    @Override
    public String toString()
    {
        return "Obj[" +
            "#vertices="+ vertices.size() + "," +
            "#texCoords=" + texCoords.size() + "," +
            "#normals=" + normals.size() + "," +
            "#faces=" + faces.size() + "," +
            "#groups=" + groups.size() + "," +
            "#materialGroups=" + materialGroups.size() + "," +
            "mtlFileNames=" + mtlFileNames + "]";
    }

    /**
     * Returns a set containing all groups with the given names. If the
     * groups with the given names do not exist, they are created and
     * added to this Obj.
     * 
     * @param groupNames The group names
     * @return The groups
     */
    private List<DefaultObjGroup> getGroupsInternal(
        Collection<? extends String> groupNames)
    {
        List<DefaultObjGroup> groups =
                new ArrayList<>(groupNames.size());
        for (String groupName : groupNames)
        {
            DefaultObjGroup group = getGroupInternal(groupName);
            groups.add(group);
        }
        return groups;
    }
    
    /**
     * Returns the group with the given names. If the group with the given 
     * name does not exist, it is created and added to this Obj.
     * 
     * @param groupName The group name
     * @return The group
     */
    private DefaultObjGroup getGroupInternal(String groupName)
    {
        DefaultObjGroup group = groupMap.get(groupName);
        if (group == null)
        {
            group = new DefaultObjGroup(groupName);
            groupMap.put(groupName, group);
            groups.add(group);
        }
        return group;
    }

    /**
     * Returns the material group with the given names. If the material group 
     * with the given name does not exist, it is created and added to this Obj.
     * 
     * @param materialGroupName The material group name
     * @return The material group
     */
    private DefaultObjGroup getMaterialGroupInternal(String materialGroupName)
    {
        DefaultObjGroup group = materialGroupMap.get(materialGroupName);
        if (group == null)
        {
            group = new DefaultObjGroup(materialGroupName);
            materialGroupMap.put(materialGroupName, group);
            materialGroups.add(group);
        }
        return group;
    }

    /**
     * If the given indices are <code>null</code>, then this method will
     * do nothing. Otherwise, it will check whether the given indices 
     * are valid, and throw an IllegalArgumentException if not. They
     * are valid when they are all not negative, and all smaller than 
     * the given maximum.
     * 
     * @param indices The indices
     * @param max The maximum index, exclusive
     * @param name The name of the index set
     * @throws IllegalArgumentException If the given indices are not valid
     */
    private static void checkIndices(int[] indices, int max, String name)
    {
        if (indices == null)
        {
            return;
        }
        for (int index : indices) {
            if (index < 0) {
                throw new IllegalArgumentException(
                        name + " index is negative: " + index);
            }
            if (index >= max) {
                throw new IllegalArgumentException(
                        name + " index is " + index +
                                ", but must be smaller than " + max);
            }
        }
    }
    
}
