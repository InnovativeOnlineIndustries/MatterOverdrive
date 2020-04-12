/*
 * This file is part of MatterOverdrive: Legacy Edition
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * MatterOverdrive: Legacy Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MatterOverdrive: Legacy Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.compat;

import matteroverdrive.compat.modules.CompatTConstruct;
import matteroverdrive.compat.modules.waila.CompatWaila;
import matteroverdrive.util.MOLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * Compatibility manager for Matter Overdrive.
 *
 * @author shadowfacts
 */
public class MatterOverdriveCompat {

    private static ArrayList<Class> modules = new ArrayList<>();

    //	Add additional modules that need to run on the Server and/or Client here
    public static void registerModules() {
        register(CompatTConstruct.class);
    }

    //Add additional modules that need to run ONLY on the Client
    public static void registerClientModules() {
        register(CompatWaila.class);
    }

    private static boolean register(Class clazz) {
        if (clazz.isAnnotationPresent(Compat.class)) {
            Compat annotation = (Compat) clazz.getAnnotation(Compat.class);
            if (Loader.isModLoaded(annotation.value())) {
                modules.add(clazz);

                return true;
            } else {
                MOLog.info("The mod %s was not loaded, skipping compatibility module.", annotation.value());
                return false;
            }
        }
        MOLog.error("There was a problem register a compatibility module!");
        return false;
    }

    public static void preInit(FMLPreInitializationEvent event) {
        MOLog.info("Attempting to run pre-initialization methods for all registered compatibility modules.");
        for (Class clazz : modules) {
            for (Method m : clazz.getMethods()) {
                if (m.isAnnotationPresent(Compat.PreInit.class) && Modifier.isStatic(m.getModifiers())) {
                    try {
                        m.invoke(null, event);
                    } catch (ReflectiveOperationException e) {
                        Compat annotation = (Compat) clazz.getAnnotation(Compat.class);
                        MOLog.error(String.format("There was an error trying to invoke the pre-initialization method of the compatibility module for %1$s", annotation.value()), e);
                    }
                }
            }
        }
    }

    public static void init(FMLInitializationEvent event) {
        MOLog.info("Attempting to run initialization methods for all registered compatibility modules.");
        for (Class clazz : modules) {
            for (Method m : clazz.getMethods()) {
                if (m.isAnnotationPresent(Compat.Init.class) && Modifier.isStatic(m.getModifiers())) {
                    try {
                        m.invoke(null, event);
                    } catch (ReflectiveOperationException e) {
                        Compat annotation = (Compat) clazz.getAnnotation(Compat.class);
                        MOLog.error(String.format("There was an error trying to invoke the initialization method of the compatibility module for %1$s", annotation.value()), e);
                    }
                }
            }
        }
    }

    public static void postInit(FMLPostInitializationEvent event) {
        MOLog.info("Attempting to run post-initialization methods for all registered compatibility modules.");
        for (Class clazz : modules) {
            for (Method m : clazz.getMethods()) {
                if (m.isAnnotationPresent(Compat.PostInit.class) && Modifier.isStatic(m.getModifiers())) {
                    try {
                        m.invoke(null, event);
                    } catch (ReflectiveOperationException e) {
                        Compat annotation = (Compat) clazz.getAnnotation(Compat.class);
                        MOLog.error(String.format("There was an error trying to invoke the post-initialization method of the compatibility module %1$s", annotation.value()), e);
                    }
                }
            }
        }
    }

}
