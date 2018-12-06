/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.player.exoplayer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.http.client.ClientProtocolException;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.media.MediaDrm.KeyRequest;
import android.media.MediaDrm.ProvisionRequest;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.exoplayer.drm.MediaDrmCallback;
import com.player.apps.Home;
import com.player.util.Constant;

/**
 * A {@link MediaDrmCallback} for Widevine test content.
 */
@TargetApi(18)
public class WidevineTestMediaDrmCallback implements MediaDrmCallback {

	private static final String WIDEVINE_GTS_DEFAULT_BASE_URI = "http://mproxy.lukup.com/proxy";
	SharedPreferences settingData;
	// http://14.142.37.110/proxy; //for Production
	// http://172.16.0.8/proxy
	// http://mproxy.lukup.com/proxy //mpls network
	// http://ec2-54-237-119-34.compute-1.amazonaws.com/proxy
	// http://ec2-54-237-119-34.compute-1.amazonaws.com/proxy //for staging
	// "http://wv-staging-proxy.appspot.com/proxy?provider=YouTube&video_id=";
	// "http://ec2-54-237-119-34.compute-1.amazonaws.com:8080/proxy";
	// "http://widevine-proxy.appspot.com/proxy";
	// "http://wv-staging-proxy.appspot.com/proxy?provider=YouTube&video_id=";
	// http://54.237.119.34/proxy

	private final String defaultUri;

	public WidevineTestMediaDrmCallback(String videoId) {
		defaultUri = WIDEVINE_GTS_DEFAULT_BASE_URI + videoId;
		Log.e("Lukup", "defaulturi is" + defaultUri);
	}

	@Override
	public byte[] executeProvisionRequest(UUID uuid, ProvisionRequest request)
			throws ClientProtocolException, IOException {
		String url = request.getDefaultUrl() + "&signedRequest="
				+ new String(request.getData());
		Log.e("Lukup", "executeProvisionRequest is" + url);
		return DemoUtil.executePost(url, null, null);
	}

	@Override
	public byte[] executeKeyRequest(UUID uuid, KeyRequest request)
			throws IOException {
		String url = request.getDefaultUrl();
		if (TextUtils.isEmpty(url)) {
			url = defaultUri;
		}

		return DemoUtil.executePost(url, request.getData(), null);
	}

}
