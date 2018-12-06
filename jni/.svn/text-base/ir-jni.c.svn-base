/*
 * Copyright (C) 2009 The Android Open Source Project
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
 *
 */
	#include <string.h>
	#include <jni.h>

	/* This is a trivial JNI example where we use a native method
	 * to return a new VM String. See the corresponding Java source
	 * file located at:
	 *
	 *   apps/samples/hello-jni/project/src/com/example/hellojni/HelloJni.java
	 */
	int Dev;
	//char vendorname[100] = "tatasky";



	jstring
	Java_com_player_network_ir_IRTransmitter_stringFromJNI( JNIEnv* env,jobject thiz )
	{
		return (*env)->NewStringUTF(env, "Testing IR in logcat!");
	}


	jint
	Java_com_player_network_ir_IRTransmitter_start(JNIEnv *env, jobject thiz, jint key, jstring dev,  jint mask)
	{
		jboolean isCopy;
	   const char *vendorname = (*env)->GetStringUTFChars(env, dev, &isCopy);
	   return key = start( Dev, vendorname); //"/dev/tty4", vendorname);

	}

	JNIEXPORT jint JNICALL
	Java_com_player_network_ir_IRTransmitter_inputfunc(JNIEnv *env, jclass cls, jint func, jint handle, jstring dev, jobject thiz)
	{
		jboolean isCopy;
		const char *vendorname = (*env)->GetStringUTFChars(env, dev, &isCopy);

		 return inputfunc(func, handle, vendorname); //func,handle, szDeviceName );

	}

	JNIEXPORT jint JNICALL
	Java_com_player_network_ir_IRTransmitter_inputfuncnec(JNIEnv *env, jclass cls, jint func, jint handle, jstring dev, jobject thiz)
	{
		jboolean isCopy;
		const char *vendorname = (*env)->GetStringUTFChars(env, dev, &isCopy);

		 return inputfuncnec(func, handle, vendorname); //func,handle, szDeviceName );

	}

	JNIEXPORT jint JNICALL
	Java_com_player_network_ir_IRTransmitter_inputfuncdish(JNIEnv *env, jclass cls, jint func, jint handle, jstring dev, jobject thiz)
	{
		jboolean isCopy;
		const char *vendorname = (*env)->GetStringUTFChars(env, dev, &isCopy);

		 return inputfuncdish(func, handle, vendorname); //func,handle, szDeviceName );

	}

	JNIEXPORT jint JNICALL
	Java_com_player_network_ir_IRTransmitter_inputfuncsun(JNIEnv *env, jclass cls, jint func, jint handle, jstring dev, jobject thiz)
	{
		jboolean isCopy;
		const char *vendorname = (*env)->GetStringUTFChars(env, dev, &isCopy);

		 return inputfuncsun(func, handle, vendorname); //func,handle, szDeviceName );

	}

	JNIEXPORT jint JNICALL
	Java_com_player_network_ir_IRTransmitter_inputfuncvideocon(JNIEnv *env, jclass cls, jint func, jint handle, jstring dev, jobject thiz)
	{
		jboolean isCopy;
		const char *vendorname = (*env)->GetStringUTFChars(env, dev, &isCopy);

		 return inputfuncvideocon(func, handle, vendorname); //func,handle, szDeviceName );

	}

	JNIEXPORT jint JNICALL
	Java_com_player_network_ir_IRTransmitter_inputfuncbroadcom(JNIEnv *env, jclass cls, jint func, jint handle, jstring dev, jobject thiz)
	{
		jboolean isCopy;
		const char *vendorname = (*env)->GetStringUTFChars(env, dev, &isCopy);

		 return inputfuncbroadcom(func, handle, vendorname); //func,handle, szDeviceName );

	}

	jint
	Java_com_player_network_ir_IRTransmitter_stop(JNIEnv *env, jint handle, jobject thiz)
	{
		return stop(handle);
	}

