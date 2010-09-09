/*
 * Copyright (C) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.googlecode.android_scripting;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;

/**
 * Utility functions for handling files.
 * 
 * @author Damon Kohler (damonkohler@gmail.com)
 */
public class FileUtils {

  private FileUtils() {
    // Utility class.
  }

  public static int chmod(File path, int mode) throws Exception {
    Class<?> fileUtils = Class.forName("android.os.FileUtils");
    Method setPermissions =
        fileUtils.getMethod("setPermissions", String.class, int.class, int.class, int.class);
    return (Integer) setPermissions.invoke(null, path.getAbsolutePath(), mode, -1, -1);
  }

  public static boolean recursiveChmod(File root, int mode) throws Exception {
    boolean success = chmod(root, mode) == 0;
    for (File path : root.listFiles()) {
      if (path.isDirectory()) {
        success = recursiveChmod(path, mode);
      }
      success &= (chmod(path, mode) == 0);
    }
    return success;
  }

  public static boolean delete(File path) {
    boolean result = true;
    if (path.exists()) {
      if (path.isDirectory()) {
        for (File child : path.listFiles()) {
          result &= delete(child);
        }
        result &= path.delete(); // Delete empty directory.
      }
      if (path.isFile()) {
        result &= path.delete();
      }
      if (!result) {
        Log.e("Delete failed;");
      }
      return result;
    } else {
      Log.e("File does not exist.");
      return false;
    }
  }

  public static File copyFromStream(String name, InputStream input) {
    if (name == null || name.length() == 0) {
      Log.e("No script name specified.");
      return null;
    }
    if (!makeDirectories(name)) {
      return null;
    }
    File file = new File(name);
    try {
      OutputStream output = new FileOutputStream(file);
      IoUtils.copy(input, output);
    } catch (Exception e) {
      Log.e(e);
      return null;
    }
    return file;
  }

  public static boolean makeDirectories(String filename) {
    File file = new File(filename);
    File parent = file.getParentFile();
    if (!parent.exists()) {
      Log.v("Creating directory: " + parent.getAbsolutePath());
      if (!parent.mkdirs()) {
        Log.e("Failed to create a directory.");
        return false;
      }
    }
    return true;
  }

  public static boolean makeDirectory(File newDir) {
    if (!newDir.exists()) {
      Log.v("Creating directory: " + newDir.getName());
      if (!newDir.mkdirs()) {
        Log.e("Failed to create directory.");
        return false;
      }
    }
    return true;
  }

  public static boolean remane(File file, String name) {
    return file.renameTo(new File(file.getParent(), name));
  }

  public static String readToString(String name) throws IOException {
    File file = new File(name);
    return readFile(file);
  }

  public static String readFile(File file) throws IOException {
    if (file == null || !file.exists()) {
      return null;
    }
    FileReader reader = new FileReader(file);
    StringBuilder out = new StringBuilder();
    char[] buffer = new char[1024 * 4];
    int numRead = 0;
    while ((numRead = reader.read(buffer)) > -1) {
      out.append(String.valueOf(buffer, 0, numRead));
    }
    reader.close();
    return out.toString();
  }

  public static String readFromAssetsFile(Context context, String name) throws IOException {
    AssetManager am = context.getAssets();
    BufferedReader reader = new BufferedReader(new InputStreamReader(am.open(name)));
    String line;
    StringBuilder builder = new StringBuilder();
    while ((line = reader.readLine()) != null) {
      builder.append(line);
    }
    reader.close();
    return builder.toString();
  }

}
