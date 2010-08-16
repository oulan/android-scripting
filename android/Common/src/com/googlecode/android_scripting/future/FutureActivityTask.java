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

package com.googlecode.android_scripting.future;

import android.app.Activity;
import android.content.Intent;

/**
 * Encapsulates an {@link Activity} and a {@link FutureObject}.
 * 
 * @author Damon Kohler (damonkohler@gmail.com)
 */
public abstract class FutureActivityTask<T> {

  private final FutureResult<T> mResult = new FutureResult<T>();
  private Activity mActivity;

  public void setActivity(Activity activity) {
    mActivity = activity;
  }

  public Activity getActivity() {
    return mActivity;
  }

  public void onCreate() {
  }

  public void onStart() {
  }

  public void onResume() {
  }

  public void onPause() {
  }

  public void onStop() {
  }

  public void onDestroy() {
  }

  // public abstract void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo);
  //  
  // public abstract boolean onPrepareOptionsMenu(Menu menu);

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
  }

  protected void setResult(T result) {
    mResult.set(result);
  }

  public T getResult() throws InterruptedException {
    return mResult.get();
  }

  public Activity getHelperActivity() {
    return mActivity;
  }

  public void finish() {
    mActivity.finish();
  }

  public void startActivity(Intent intent) {
    mActivity.startActivity(intent);
  }

  public void startActivityForResult(Intent intent, int requestCode) {
    mActivity.startActivityForResult(intent, requestCode);
  }
}
