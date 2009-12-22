/*
 * Copyright (C) 2009 Samuel Audet
 *
 * This file is part of ProCamCalib.
 *
 * ProCamCalib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * ProCamCalib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProCamCalib.  If not, see <http://www.gnu.org/licenses/>.
 */

package name.audet.samuel.procamcalib;

import java.beans.IntrospectionException;
import java.beans.PropertyEditor;
import java.beans.beancontext.BeanContext;
import java.util.HashMap;
import javax.swing.Action;
import org.openide.nodes.BeanChildren;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;

/**
 *
 * @author Samuel Audet
 */
public class CleanBeanNode<T> extends BeanNode<T> {
    public CleanBeanNode(T o,
            final HashMap<String, Class<? extends PropertyEditor>> editors,
            String displayName) throws IntrospectionException {
        super(o, o instanceof BeanContext ? new BeanChildren((BeanContext)o,
                new BeanChildren.Factory() {
                    public Node createNode(Object bean) throws IntrospectionException {
                        return new CleanBeanNode<Object>(bean, editors);
                    }
                }) : null);

        setIconBaseWithExtension("org/openide/nodes/defaultNode.png");

        Property[] ps = getPropertySets()[0].getProperties();
        for (Property p : ps) {
            if (editors != null && editors.containsKey(p.getName())) {
                Class<? extends PropertyEditor> c = editors.get(p.getName());
                if (c == null) {
                    p.setHidden(true);
                } else {
                    ((PropertySupport.Reflection<?>)p).setPropertyEditorClass(c);
                }
            }
            if (p.getName().equals("beanContext") || p.getName().equals("beanContextChildPeer") ||
                    p.getName().equals("beanContextPeer") || p.getName().equals("class") ||
                    p.getName().equals("delegated") || p.getName().equals("designTime") ||
                    p.getName().equals("empty") || p.getName().equals("locale") ||
                    p.getName().equals("serializing")) {
                p.setHidden(true);
            }
        }
        if (displayName != null) {
            setDisplayName(displayName);
        }
    }
    public CleanBeanNode(T o, String displayName) throws IntrospectionException {
        this(o, null, displayName);
    }
    public CleanBeanNode(T o, HashMap<String, Class<? extends PropertyEditor>> editors)
            throws IntrospectionException {
        this(o, editors, null);
    }

    @Override public boolean canCopy() {
        return false;
    }
    @Override public Action[] getActions(boolean context) {
        return null;
    }

}