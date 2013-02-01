/*
 * Copyright (c) 2001, 2006, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package sun.security.util;

/**
 * <p>
 * This class represents the <code>ResourceBundle</code> for javax.security.auth
 * and sun.security.
 * 
 */
public class Resources_ja extends java.util.ListResourceBundle {

	private static final Object[][] contents = {

			// shared (from jarsigner)
			{ " ", " " },
			{ "  ", "  " },
			{ "      ", "      " },
			{ ", ", ", " },
			// shared (from keytool)
			{ "\n", "\n" },
			{ "*******************************************",
					"*******************************************" },
			{ "*******************************************\n\n",
					"*******************************************\n\n" },

			// keytool
			{ "keytool error: ", "keytool \u30a8\u30e9\u30fc: " },
			{ "Illegal option:  ",
					"\u4e0d\u6b63\u306a\u30aa\u30d7\u30b7\u30e7\u30f3:  " },
			{
					"Try keytool -help",
					"keytool -help \u3092\u5b9f\u884c\u3057\u3066\u307f\u3066\u304f\u3060\u3055\u3044" },
			{
					"Command option <flag> needs an argument.",
					"\u30b3\u30de\u30f3\u30c9\u30aa\u30d7\u30b7\u30e7\u30f3 {0} \u306b\u306f\u5f15\u6570\u304c\u5fc5\u8981\u3067\u3059\u3002" },
			{
					"Warning:  Different store and key passwords not supported for PKCS12 KeyStores. Ignoring user-specified <command> value.",
					"\u8b66\u544a: PKCS12 \u30ad\u30fc\u30b9\u30c8\u30a2\u3067\u306f\u3001\u30b9\u30c8\u30a2\u306e\u30d1\u30b9\u30ef\u30fc\u30c9\u3068\u9375\u306e\u30d1\u30b9\u30ef\u30fc\u30c9\u304c\u7570\u306a\u3063\u3066\u3044\u3066\u306f\u306a\u308a\u307e\u305b\u3093\u3002\u30e6\u30fc\u30b6\u30fc\u304c\u6307\u5b9a\u3057\u305f {0} \u306e\u5024\u306f\u7121\u8996\u3057\u307e\u3059\u3002" },
			{
					"-keystore must be NONE if -storetype is {0}",
					"-storetype \u304c {0} \u306e\u5834\u5408 -keystore \u306f NONE \u3067\u306a\u3051\u308c\u3070\u306a\u308a\u307e\u305b\u3093" },
			{
					"Too may retries, program terminated",
					"\u518d\u8a66\u884c\u304c\u591a\u3059\u304e\u307e\u3059\u3002\u30d7\u30ed\u30b0\u30e9\u30e0\u304c\u7d42\u4e86\u3057\u307e\u3057\u305f" },
			{
					"-storepasswd and -keypasswd commands not supported if -storetype is {0}",
					"-storetype \u304c {0} \u306e\u5834\u5408 -storepasswd \u30b3\u30de\u30f3\u30c9\u3068 -keypasswd \u30b3\u30de\u30f3\u30c9\u306f\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u307e\u305b\u3093" },
			{
					"-keypasswd commands not supported if -storetype is PKCS12",
					"-storetype \u304c PKCS12 \u306e\u5834\u5408\u3001-keypasswd \u30b3\u30de\u30f3\u30c9\u306f\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u307e\u305b\u3093" },
			{
					"-keypass and -new can not be specified if -storetype is {0}",
					"-storetype \u304c {0} \u306e\u5834\u5408 -keypass \u3068 -new \u306f\u6307\u5b9a\u3067\u304d\u307e\u305b\u3093" },
			{
					"if -protected is specified, then -storepass, -keypass, and -new must not be specified",
					"-protected \u304c\u6307\u5b9a\u3055\u308c\u3066\u3044\u308b\u5834\u5408\u3001-storepass\u3001-keypass\u3001-new \u3092\u6307\u5b9a\u3059\u308b\u3053\u3068\u306f\u3067\u304d\u307e\u305b\u3093" },
			{
					"if -srcprotected is specified, then -srcstorepass and -srckeypass must not be specified",
					"-srcprotected \u3092\u6307\u5b9a\u3059\u308b\u5834\u5408\u3001-srcstorepass \u304a\u3088\u3073 -srckeypass \u306f\u6307\u5b9a\u3067\u304d\u307e\u305b\u3093" },
			{
					"if keystore is not password protected, then -storepass, -keypass, and -new must not be specified",
					"\u30ad\u30fc\u30b9\u30c8\u30a2\u304c\u30d1\u30b9\u30ef\u30fc\u30c9\u3067\u4fdd\u8b77\u3055\u308c\u3066\u3044\u306a\u3044\u5834\u5408\u3001-storepass\u3001-keypass\u3001\u304a\u3088\u3073 -new \u306f\u6307\u5b9a\u3067\u304d\u307e\u305b\u3093" },
			{
					"if source keystore is not password protected, then -srcstorepass and -srckeypass must not be specified",
					"\u30bd\u30fc\u30b9\u30ad\u30fc\u30b9\u30c8\u30a2\u304c\u30d1\u30b9\u30ef\u30fc\u30c9\u3067\u4fdd\u8b77\u3055\u308c\u3066\u3044\u306a\u3044\u5834\u5408\u3001-srcstorepass \u3068 -srckeypass \u306f\u6307\u5b9a\u3067\u304d\u307e\u305b\u3093" },
			{
					"Validity must be greater than zero",
					"\u59a5\u5f53\u6027\u306f\u30bc\u30ed\u3088\u308a\u5927\u304d\u304f\u306a\u3051\u308c\u3070\u306a\u308a\u307e\u305b\u3093\u3002" },
			{
					"provName not a provider",
					"{0} \u306f\u30d7\u30ed\u30d0\u30a4\u30c0\u3067\u306f\u3042\u308a\u307e\u305b\u3093\u3002" },
			{
					"Usage error: no command provided",
					"\u4f7f\u7528\u30a8\u30e9\u30fc: \u30b3\u30de\u30f3\u30c9\u304c\u6307\u5b9a\u3055\u308c\u3066\u3044\u307e\u305b\u3093" },
			{
					"Usage error, <arg> is not a legal command",
					"\u4f7f\u7528\u30a8\u30e9\u30fc: {0} \u306f\u4e0d\u6b63\u306a\u30b3\u30de\u30f3\u30c9\u3067\u3059" },
			{
					"Source keystore file exists, but is empty: ",
					"\u30bd\u30fc\u30b9\u30ad\u30fc\u30b9\u30c8\u30a2\u30d5\u30a1\u30a4\u30eb\u306f\u3001\u5b58\u5728\u3057\u307e\u3059\u304c\u7a7a\u3067\u3059: " },
			{ "Please specify -srckeystore",
					"-srckeystore \u3092\u6307\u5b9a\u3057\u3066\u304f\u3060\u3055\u3044" },
			{
					"Must not specify both -v and -rfc with 'list' command",
					"'list' \u30b3\u30de\u30f3\u30c9\u306b -v \u3068 -rfc \u306e\u4e21\u65b9\u3092\u6307\u5b9a\u3059\u308b\u3053\u3068\u306f\u3067\u304d\u307e\u305b\u3093\u3002" },
			{
					"Key password must be at least 6 characters",
					"\u9375\u306e\u30d1\u30b9\u30ef\u30fc\u30c9\u306f 6 \u6587\u5b57\u4ee5\u4e0a\u3067\u306a\u3051\u308c\u3070\u306a\u308a\u307e\u305b\u3093\u3002" },
			{
					"New password must be at least 6 characters",
					"\u65b0\u898f\u30d1\u30b9\u30ef\u30fc\u30c9\u306f 6 \u6587\u5b57\u4ee5\u4e0a\u3067\u306a\u3051\u308c\u3070\u306a\u308a\u307e\u305b\u3093\u3002" },
			{
					"Keystore file exists, but is empty: ",
					"\u30ad\u30fc\u30b9\u30c8\u30a2\u30d5\u30a1\u30a4\u30eb\u306f\u5b58\u5728\u3057\u307e\u3059\u304c\u3001\u7a7a\u3067\u3059: " },
			{
					"Keystore file does not exist: ",
					"\u30ad\u30fc\u30b9\u30c8\u30a2\u30d5\u30a1\u30a4\u30eb\u306f\u5b58\u5728\u3057\u307e\u305b\u3093: " },
			{
					"Must specify destination alias",
					"\u5b9b\u5148\u306e\u5225\u540d\u3092\u6307\u5b9a\u3059\u308b\u5fc5\u8981\u304c\u3042\u308a\u307e\u3059\u3002" },
			{
					"Must specify alias",
					"\u5225\u540d\u3092\u6307\u5b9a\u3059\u308b\u5fc5\u8981\u304c\u3042\u308a\u307e\u3059\u3002" },
			{
					"Keystore password must be at least 6 characters",
					"\u30ad\u30fc\u30b9\u30c8\u30a2\u306e\u30d1\u30b9\u30ef\u30fc\u30c9\u306f 6 \u6587\u5b57\u4ee5\u4e0a\u3067\u306a\u3051\u308c\u3070\u306a\u308a\u307e\u305b\u3093\u3002" },
			{
					"Enter keystore password:  ",
					"\u30ad\u30fc\u30b9\u30c8\u30a2\u306e\u30d1\u30b9\u30ef\u30fc\u30c9\u3092\u5165\u529b\u3057\u3066\u304f\u3060\u3055\u3044:  " },
			{
					"Enter source keystore password:  ",
					"\u30bd\u30fc\u30b9\u30ad\u30fc\u30b9\u30c8\u30a2\u306e\u30d1\u30b9\u30ef\u30fc\u30c9\u3092\u5165\u529b\u3057\u3066\u304f\u3060\u3055\u3044:  " },
			{
					"Enter destination keystore password:  ",
					"\u51fa\u529b\u5148\u30ad\u30fc\u30b9\u30c8\u30a2\u306e\u30d1\u30b9\u30ef\u30fc\u30c9\u3092\u5165\u529b\u3057\u3066\u304f\u3060\u3055\u3044:  " },
			{
					"Keystore password is too short - must be at least 6 characters",
					"\u30ad\u30fc\u30b9\u30c8\u30a2\u306e\u30d1\u30b9\u30ef\u30fc\u30c9\u304c\u77ed\u904e\u304e\u307e\u3059 - 6 \u6587\u5b57\u4ee5\u4e0a\u306b\u3057\u3066\u304f\u3060\u3055\u3044\u3002" },
			{ "Unknown Entry Type",
					"\u672a\u77e5\u306e\u30a8\u30f3\u30c8\u30ea\u30bf\u30a4\u30d7" },
			{
					"Too many failures. Alias not changed",
					"\u969c\u5bb3\u304c\u591a\u3059\u304e\u307e\u3059\u3002\u5225\u540d\u306f\u5909\u66f4\u3055\u308c\u307e\u305b\u3093" },
			{
					"Entry for alias <alias> successfully imported.",
					"\u5225\u540d {0} \u306e\u30a8\u30f3\u30c8\u30ea\u306e\u30a4\u30f3\u30dd\u30fc\u30c8\u306b\u6210\u529f\u3057\u307e\u3057\u305f\u3002" },
			{
					"Entry for alias <alias> not imported.",
					"\u5225\u540d {0} \u306e\u30a8\u30f3\u30c8\u30ea\u306f\u30a4\u30f3\u30dd\u30fc\u30c8\u3055\u308c\u307e\u305b\u3093\u3067\u3057\u305f\u3002" },
			{
					"Problem importing entry for alias <alias>: <exception>.\nEntry for alias <alias> not imported.",
					"\u5225\u540d {0} \u306e\u30a8\u30f3\u30c8\u30ea\u306e\u30a4\u30f3\u30dd\u30fc\u30c8\u4e2d\u306b\u554f\u984c\u304c\u767a\u751f\u3057\u307e\u3057\u305f: {1}\u3002\n\u5225\u540d {0} \u306e\u30a8\u30f3\u30c8\u30ea\u306f\u30a4\u30f3\u30dd\u30fc\u30c8\u3055\u308c\u307e\u305b\u3093\u3067\u3057\u305f\u3002" },
			{
					"Import command completed:  <ok> entries successfully imported, <fail> entries failed or cancelled",
					"\u30a4\u30f3\u30dd\u30fc\u30c8\u30b3\u30de\u30f3\u30c9\u304c\u5b8c\u4e86\u3057\u307e\u3057\u305f:  {0} \u4ef6\u306e\u30a8\u30f3\u30c8\u30ea\u306e\u30a4\u30f3\u30dd\u30fc\u30c8\u304c\u6210\u529f\u3057\u307e\u3057\u305f\u3002{1} \u4ef6\u306e\u30a8\u30f3\u30c8\u30ea\u306e\u30a4\u30f3\u30dd\u30fc\u30c8\u304c\u5931\u6557\u3057\u305f\u304b\u53d6\u308a\u6d88\u3055\u308c\u307e\u3057\u305f" },
			{
					"Warning: Overwriting existing alias <alias> in destination keystore",
					"\u8b66\u544a: \u51fa\u529b\u5148\u30ad\u30fc\u30b9\u30c8\u30a2\u5185\u306e\u65e2\u5b58\u306e\u5225\u540d {0} \u3092\u4e0a\u66f8\u304d\u3057\u3066\u3044\u307e\u3059" },
			{
					"Existing entry alias <alias> exists, overwrite? [no]:  ",
					"\u65e2\u5b58\u306e\u30a8\u30f3\u30c8\u30ea\u306e\u5225\u540d {0} \u304c\u5b58\u5728\u3057\u3066\u3044\u307e\u3059\u3002\u4e0a\u66f8\u304d\u3057\u307e\u3059\u304b? [no]:  " },
			{
					"Too many failures - try later",
					"\u969c\u5bb3\u304c\u591a\u904e\u304e\u307e\u3059 - \u5f8c\u3067\u5b9f\u884c\u3057\u3066\u304f\u3060\u3055\u3044\u3002" },
			{
					"Certification request stored in file <filename>",
					"\u8a3c\u660e\u66f8\u8981\u6c42\u304c\u30d5\u30a1\u30a4\u30eb <{0}> \u306b\u4fdd\u5b58\u3055\u308c\u307e\u3057\u305f\u3002" },
			{
					"Submit this to your CA",
					"\u3053\u308c\u3092 CA \u306b\u63d0\u51fa\u3057\u3066\u304f\u3060\u3055\u3044\u3002" },
			{
					"if alias not specified, destalias, srckeypass, and destkeypass must not be specified",
					"\u5225\u540d\u3092\u6307\u5b9a\u3057\u306a\u3044\u5834\u5408\u3001\u51fa\u529b\u5148\u30ad\u30fc\u30b9\u30c8\u30a2\u306e\u5225\u540d\u3001\u30bd\u30fc\u30b9\u30ad\u30fc\u30b9\u30c8\u30a2\u306e\u30d1\u30b9\u30ef\u30fc\u30c9\u3001\u304a\u3088\u3073\u51fa\u529b\u5148\u30ad\u30fc\u30b9\u30c8\u30a2\u306e\u30d1\u30b9\u30ef\u30fc\u30c9\u306f\u6307\u5b9a\u3067\u304d\u307e\u305b\u3093" },
			{
					"Certificate stored in file <filename>",
					"\u8a3c\u660e\u66f8\u304c\u30d5\u30a1\u30a4\u30eb <{0}> \u306b\u4fdd\u5b58\u3055\u308c\u307e\u3057\u305f\u3002" },
			{
					"Certificate reply was installed in keystore",
					"\u8a3c\u660e\u66f8\u5fdc\u7b54\u304c\u30ad\u30fc\u30b9\u30c8\u30a2\u306b\u30a4\u30f3\u30b9\u30c8\u30fc\u30eb\u3055\u308c\u307e\u3057\u305f\u3002" },
			{
					"Certificate reply was not installed in keystore",
					"\u8a3c\u660e\u66f8\u5fdc\u7b54\u304c\u30ad\u30fc\u30b9\u30c8\u30a2\u306b\u30a4\u30f3\u30b9\u30c8\u30fc\u30eb\u3055\u308c\u307e\u305b\u3093\u3067\u3057\u305f\u3002" },
			{
					"Certificate was added to keystore",
					"\u8a3c\u660e\u66f8\u304c\u30ad\u30fc\u30b9\u30c8\u30a2\u306b\u8ffd\u52a0\u3055\u308c\u307e\u3057\u305f\u3002" },
			{
					"Certificate was not added to keystore",
					"\u8a3c\u660e\u66f8\u304c\u30ad\u30fc\u30b9\u30c8\u30a2\u306b\u8ffd\u52a0\u3055\u308c\u307e\u305b\u3093\u3067\u3057\u305f\u3002" },
			{ "[Storing ksfname]", "[{0} \u3092\u683c\u7d0d\u4e2d]" },
			{
					"alias has no public key (certificate)",
					"{0} \u306b\u306f\u516c\u958b\u9375 (\u8a3c\u660e\u66f8) \u304c\u3042\u308a\u307e\u305b\u3093\u3002" },
			{
					"Cannot derive signature algorithm",
					"\u7f72\u540d\u30a2\u30eb\u30b4\u30ea\u30ba\u30e0\u3092\u53d6\u5f97\u3067\u304d\u307e\u305b\u3093\u3002" },
			{ "Alias <alias> does not exist",
					"\u5225\u540d <{0}> \u306f\u5b58\u5728\u3057\u307e\u305b\u3093\u3002" },
			{
					"Alias <alias> has no certificate",
					"\u5225\u540d <{0}> \u306f\u8a3c\u660e\u66f8\u3092\u4fdd\u6301\u3057\u307e\u305b\u3093\u3002" },
			{
					"Key pair not generated, alias <alias> already exists",
					"\u9375\u30da\u30a2\u306f\u751f\u6210\u3055\u308c\u307e\u305b\u3093\u3067\u3057\u305f\u3002\u5225\u540d <{0}> \u306f\u3059\u3067\u306b\u5b58\u5728\u3057\u307e\u3059\u3002" },
			{
					"Cannot derive signature algorithm",
					"\u7f72\u540d\u30a2\u30eb\u30b4\u30ea\u30ba\u30e0\u3092\u53d6\u5f97\u3067\u304d\u307e\u305b\u3093\u3002" },
			{
					"Generating keysize bit keyAlgName key pair and self-signed certificate (sigAlgName) with a validity of validality days\n\tfor: x500Name",
					"{3} \u65e5\u9593\u6709\u52b9\u306a {0} \u30d3\u30c3\u30c8\u306e {1} \u306e\u9375\u30da\u30a2\u3068\u81ea\u5df1\u7f72\u540d\u578b\u8a3c\u660e\u66f8 ({2}) \u3092\u751f\u6210\u3057\u3066\u3044\u307e\u3059\n\t\u30c7\u30a3\u30ec\u30af\u30c8\u30ea\u540d: {4}" },
			{
					"Enter key password for <alias>",
					"<{0}> \u306e\u9375\u30d1\u30b9\u30ef\u30fc\u30c9\u3092\u5165\u529b\u3057\u3066\u304f\u3060\u3055\u3044\u3002" },
			{
					"\t(RETURN if same as keystore password):  ",
					"\t(\u30ad\u30fc\u30b9\u30c8\u30a2\u306e\u30d1\u30b9\u30ef\u30fc\u30c9\u3068\u540c\u3058\u5834\u5408\u306f RETURN \u3092\u62bc\u3057\u3066\u304f\u3060\u3055\u3044):  " },
			{
					"Key password is too short - must be at least 6 characters",
					"\u9375\u30d1\u30b9\u30ef\u30fc\u30c9\u304c\u77ed\u904e\u304e\u307e\u3059 - 6 \u6587\u5b57\u4ee5\u4e0a\u3092\u6307\u5b9a\u3057\u3066\u304f\u3060\u3055\u3044\u3002" },
			{
					"Too many failures - key not added to keystore",
					"\u969c\u5bb3\u304c\u591a\u904e\u304e\u307e\u3059 - \u9375\u306f\u30ad\u30fc\u30b9\u30c8\u30a2\u306b\u8ffd\u52a0\u3055\u308c\u307e\u305b\u3093\u3067\u3057\u305f" },
			{
					"Destination alias <dest> already exists",
					"\u5b9b\u5148\u306e\u5225\u540d <{0}> \u306f\u3059\u3067\u306b\u5b58\u5728\u3057\u307e\u3059\u3002" },
			{
					"Password is too short - must be at least 6 characters",
					"\u30d1\u30b9\u30ef\u30fc\u30c9\u304c\u77ed\u904e\u304e\u307e\u3059 - 6 \u6587\u5b57\u4ee5\u4e0a\u3092\u6307\u5b9a\u3057\u3066\u304f\u3060\u3055\u3044\u3002" },
			{
					"Too many failures. Key entry not cloned",
					"\u969c\u5bb3\u304c\u591a\u904e\u304e\u307e\u3059\u3002\u9375\u30a8\u30f3\u30c8\u30ea\u306f\u8907\u88fd\u3055\u308c\u307e\u305b\u3093\u3067\u3057\u305f\u3002" },
			{ "key password for <alias>",
					"<{0}> \u306e\u9375\u306e\u30d1\u30b9\u30ef\u30fc\u30c9" },
			{
					"Keystore entry for <id.getName()> already exists",
					"<{0}> \u306e\u30ad\u30fc\u30b9\u30c8\u30a2\u30a8\u30f3\u30c8\u30ea\u306f\u3059\u3067\u306b\u5b58\u5728\u3057\u307e\u3059\u3002" },
			{
					"Creating keystore entry for <id.getName()> ...",
					"<{0}> \u306e\u30ad\u30fc\u30b9\u30c8\u30a2\u30a8\u30f3\u30c8\u30ea\u3092\u4f5c\u6210\u4e2d..." },
			{
					"No entries from identity database added",
					"\u30a2\u30a4\u30c7\u30f3\u30c6\u30a3\u30c6\u30a3\u30c7\u30fc\u30bf\u30d9\u30fc\u30b9\u304b\u3089\u8ffd\u52a0\u3055\u308c\u305f\u30a8\u30f3\u30c8\u30ea\u306f\u3042\u308a\u307e\u305b\u3093\u3002" },
			{ "Alias name: alias", "\u5225\u540d: {0}" },
			{ "Creation date: keyStore.getCreationDate(alias)",
					"\u4f5c\u6210\u65e5: {0,date}" },
			{ "alias, keyStore.getCreationDate(alias), ", "{0}, {1,date}, " },
			{ "alias, ", "{0}, " },
			{ "Entry type: <type>",
					"\u30a8\u30f3\u30c8\u30ea\u30bf\u30a4\u30d7: {0}" },
			{ "Certificate chain length: ",
					"\u8a3c\u660e\u9023\u9396\u306e\u9577\u3055: " },
			{ "Certificate[(i + 1)]:",
					"\u8a3c\u660e\u66f8[{0,number,integer}]:" },
			{
					"Certificate fingerprint (MD5): ",
					"\u8a3c\u660e\u66f8\u306e\u30d5\u30a3\u30f3\u30ac\u30fc\u30d7\u30ea\u30f3\u30c8 (MD5): " },
			{ "Entry type: trustedCertEntry\n",
					"\u30a8\u30f3\u30c8\u30ea\u306e\u30bf\u30a4\u30d7: trustedCertEntry\n" },
			{ "trustedCertEntry,", "trustedCertEntry," },
			{ "Keystore type: ",
					"\u30ad\u30fc\u30b9\u30c8\u30a2\u306e\u30bf\u30a4\u30d7: " },
			{ "Keystore provider: ",
					"\u30ad\u30fc\u30b9\u30c8\u30a2\u306e\u30d7\u30ed\u30d0\u30a4\u30c0: " },
			{
					"Your keystore contains keyStore.size() entry",
					"\u30ad\u30fc\u30b9\u30c8\u30a2\u306b\u306f {0,number,integer} \u30a8\u30f3\u30c8\u30ea\u304c\u542b\u307e\u308c\u307e\u3059\u3002" },
			{
					"Your keystore contains keyStore.size() entries",
					"\u30ad\u30fc\u30b9\u30c8\u30a2\u306b\u306f {0,number,integer} \u30a8\u30f3\u30c8\u30ea\u304c\u542b\u307e\u308c\u307e\u3059\u3002" },
			{
					"Failed to parse input",
					"\u5165\u529b\u306e\u69cb\u6587\u89e3\u6790\u306b\u5931\u6557\u3057\u307e\u3057\u305f\u3002" },
			{ "Empty input",
					"\u5165\u529b\u304c\u3042\u308a\u307e\u305b\u3093\u3002" },
			{ "Not X.509 certificate",
					"X.509 \u8a3c\u660e\u66f8\u3067\u306f\u3042\u308a\u307e\u305b\u3093\u3002" },
			{
					"Cannot derive signature algorithm",
					"\u7f72\u540d\u30a2\u30eb\u30b4\u30ea\u30ba\u30e0\u3092\u53d6\u5f97\u3067\u304d\u307e\u305b\u3093\u3002" },
			{ "alias has no public key",
					"{0} \u306b\u306f\u516c\u958b\u9375\u304c\u3042\u308a\u307e\u305b\u3093\u3002" },
			{
					"alias has no X.509 certificate",
					"{0} \u306b\u306f X.509 \u8a3c\u660e\u66f8\u304c\u3042\u308a\u307e\u305b\u3093\u3002" },
			{ "New certificate (self-signed):",
					"\u65b0\u3057\u3044\u8a3c\u660e\u66f8 (\u81ea\u5df1\u7f72\u540d\u578b):" },
			{
					"Reply has no certificates",
					"\u5fdc\u7b54\u306b\u306f\u8a3c\u660e\u66f8\u304c\u3042\u308a\u307e\u305b\u3093\u3002" },
			{
					"Certificate not imported, alias <alias> already exists",
					"\u8a3c\u660e\u66f8\u306f\u30a4\u30f3\u30dd\u30fc\u30c8\u3055\u308c\u307e\u305b\u3093\u3067\u3057\u305f\u3002\u5225\u540d <{0}> \u306f\u3059\u3067\u306b\u5b58\u5728\u3057\u307e\u3059\u3002" },
			{
					"Input not an X.509 certificate",
					"\u5165\u529b\u306f X.509 \u8a3c\u660e\u66f8\u3067\u306f\u3042\u308a\u307e\u305b\u3093\u3002" },
			{
					"Certificate already exists in keystore under alias <trustalias>",
					"\u8a3c\u660e\u66f8\u306f\u3001\u5225\u540d <{0}> \u306e\u30ad\u30fc\u30b9\u30c8\u30a2\u306b\u3059\u3067\u306b\u5b58\u5728\u3057\u307e\u3059\u3002" },
			{ "Do you still want to add it? [no]:  ",
					"\u8ffd\u52a0\u3057\u307e\u3059\u304b? [no]:  " },
			{
					"Certificate already exists in system-wide CA keystore under alias <trustalias>",
					"\u8a3c\u660e\u66f8\u306f\u3001\u5225\u540d <{0}> \u306e\u30b7\u30b9\u30c6\u30e0\u898f\u6a21\u306e CA \u30ad\u30fc\u30b9\u30c8\u30a2\u5185\u306b\u3059\u3067\u306b\u5b58\u5728\u3057\u307e\u3059\u3002" },
			{
					"Do you still want to add it to your own keystore? [no]:  ",
					"\u30ad\u30fc\u30b9\u30c8\u30a2\u306b\u8ffd\u52a0\u3057\u307e\u3059\u304b? [no]:  " },
			{
					"Trust this certificate? [no]:  ",
					"\u3053\u306e\u8a3c\u660e\u66f8\u3092\u4fe1\u983c\u3057\u307e\u3059\u304b? [no]:  " },
			{ "YES", "YES" },
			{ "New prompt: ", "\u65b0\u898f {0}: " },
			{
					"Passwords must differ",
					"\u30d1\u30b9\u30ef\u30fc\u30c9\u306f\u7570\u306a\u3063\u3066\u3044\u306a\u3051\u308c\u3070\u306a\u308a\u307e\u305b\u3093\u3002" },
			{
					"Re-enter new prompt: ",
					"\u65b0\u898f {0} \u3092\u518d\u5165\u529b\u3057\u3066\u304f\u3060\u3055\u3044: " },
			{
					"Re-enter new password: ",
					"\u65b0\u898f\u30d1\u30b9\u30ef\u30fc\u30c9\u3092\u518d\u5165\u529b\u3057\u3066\u304f\u3060\u3055\u3044: " },
			{
					"They don't match. Try again",
					"\u4e00\u81f4\u3057\u307e\u305b\u3093\u3002\u3082\u3046\u4e00\u5ea6\u5b9f\u884c\u3057\u3066\u304f\u3060\u3055\u3044" },
			{
					"Enter prompt alias name:  ",
					"{0} \u306e\u5225\u540d\u3092\u5165\u529b\u3057\u3066\u304f\u3060\u3055\u3044:  " },
			{
					"Enter new alias name\t(RETURN to cancel import for this entry):  ",
					"\u65b0\u3057\u3044\u5225\u540d\u3092\u5165\u529b\u3057\u3066\u304f\u3060\u3055\u3044\t(\u3053\u306e\u30a8\u30f3\u30c8\u30ea\u306e\u30a4\u30f3\u30dd\u30fc\u30c8\u3092\u53d6\u308a\u6d88\u3059\u5834\u5408\u306f RETURN \u3092\u62bc\u3057\u3066\u304f\u3060\u3055\u3044):  " },
			{ "Enter alias name:  ",
					"\u5225\u540d\u3092\u5165\u529b\u3057\u3066\u304f\u3060\u3055\u3044:  " },
			{
					"\t(RETURN if same as for <otherAlias>)",
					"\t(<{0}> \u3068\u540c\u3058\u5834\u5408\u306f RETURN \u3092\u62bc\u3057\u3066\u304f\u3060\u3055\u3044)" },
			{
					"*PATTERN* printX509Cert",
					"\u6240\u6709\u8005: {0}\n\u767a\u884c\u8005: {1}\n\u30b7\u30ea\u30a2\u30eb\u756a\u53f7: {2}\n\u6709\u52b9\u671f\u9593\u306e\u958b\u59cb\u65e5: {3} \u7d42\u4e86\u65e5: {4}\n\u8a3c\u660e\u66f8\u306e\u30d5\u30a3\u30f3\u30ac\u30fc\u30d7\u30ea\u30f3\u30c8:\n\t MD5:  {5}\n\t SHA1: {6}\n\t \u7f72\u540d\u30a2\u30eb\u30b4\u30ea\u30ba\u30e0\u540d: {7}\n\t \u30d0\u30fc\u30b8\u30e7\u30f3: {8}" },
			{ "What is your first and last name?",
					"\u59d3\u540d\u3092\u5165\u529b\u3057\u3066\u304f\u3060\u3055\u3044\u3002" },
			{
					"What is the name of your organizational unit?",
					"\u7d44\u7e54\u5358\u4f4d\u540d\u3092\u5165\u529b\u3057\u3066\u304f\u3060\u3055\u3044\u3002" },
			{
					"What is the name of your organization?",
					"\u7d44\u7e54\u540d\u3092\u5165\u529b\u3057\u3066\u304f\u3060\u3055\u3044\u3002" },
			{
					"What is the name of your City or Locality?",
					"\u90fd\u5e02\u540d\u307e\u305f\u306f\u5730\u57df\u540d\u3092\u5165\u529b\u3057\u3066\u304f\u3060\u3055\u3044\u3002" },
			{
					"What is the name of your State or Province?",
					"\u5dde\u540d\u307e\u305f\u306f\u5730\u65b9\u540d\u3092\u5165\u529b\u3057\u3066\u304f\u3060\u3055\u3044\u3002" },
			{
					"What is the two-letter country code for this unit?",
					"\u3053\u306e\u5358\u4f4d\u306b\u8a72\u5f53\u3059\u308b 2 \u6587\u5b57\u306e\u56fd\u756a\u53f7\u3092\u5165\u529b\u3057\u3066\u304f\u3060\u3055\u3044\u3002" },
			{ "Is <name> correct?",
					"{0} \u3067\u3088\u308d\u3057\u3044\u3067\u3059\u304b?" },
			{ "no", "no" },
			{ "yes", "yes" },
			{ "y", "y" },
			{ "  [defaultValue]:  ", "  [{0}]:  " },
			{ "Alias <alias> has no key",
					"\u5225\u540d <{0}> \u306b\u306f\u9375\u304c\u3042\u308a\u307e\u305b\u3093" },
			{
					"Alias <alias> references an entry type that is not a private key entry.  The -keyclone command only supports cloning of private key entries",
					"\u5225\u540d <{0}> \u304c\u53c2\u7167\u3057\u3066\u3044\u308b\u30a8\u30f3\u30c8\u30ea\u30bf\u30a4\u30d7\u306f\u975e\u516c\u958b\u9375\u30a8\u30f3\u30c8\u30ea\u3067\u306f\u3042\u308a\u307e\u305b\u3093\u3002-keyclone \u30b3\u30de\u30f3\u30c9\u306f\u975e\u516c\u958b\u9375\u30a8\u30f3\u30c8\u30ea\u306e\u8907\u88fd\u306e\u307f\u3092\u30b5\u30dd\u30fc\u30c8\u3057\u307e\u3059" },

			{ "*****************  WARNING WARNING WARNING  *****************",
					"*****************  \u8b66\u544a \u8b66\u544a \u8b66\u544a  *****************" },

			// Translators of the following 5 pairs, ATTENTION:
			// the next 5 string pairs are meant to be combined into 2
			// paragraphs,
			// 1+3+4 and 2+3+5. make sure your translation also does.
			{
					"* The integrity of the information stored in your keystore  *",
					"*  \u30ad\u30fc\u30b9\u30c8\u30a2\u306b\u4fdd\u5b58\u3055\u308c\u305f\u60c5\u5831\u306e\u5b8c\u5168\u6027\u306f\u691c\u8a3c\u3055\u308c\u3066  *" },
			{
					"* The integrity of the information stored in the srckeystore*",
					"* \u30bd\u30fc\u30b9\u30ad\u30fc\u30b9\u30c8\u30a2\u306b\u4fdd\u5b58\u3055\u308c\u305f\u60c5\u5831\u306e\u5b8c\u5168\u6027*" },
			{
					"* has NOT been verified!  In order to verify its integrity, *",
					"*  \u3044\u307e\u305b\u3093!  \u5b8c\u5168\u6027\u3092\u691c\u8a3c\u3059\u308b\u306b\u306f\u3001\u30ad\u30fc\u30b9\u30c8\u30a2\u306e   *" },
			{
					"* you must provide your keystore password.                  *",
					"*  \u30d1\u30b9\u30ef\u30fc\u30c9\u3092\u5165\u529b\u3059\u308b\u5fc5\u8981\u304c\u3042\u308a\u307e\u3059\u3002            *" },
			{
					"* you must provide the srckeystore password.                *",
					"* \u30bd\u30fc\u30b9\u30ad\u30fc\u30b9\u30c8\u30a2\u306e\u30d1\u30b9\u30ef\u30fc\u30c9\u3092\u5165\u529b\u3059\u308b\u5fc5\u8981\u304c\u3042\u308a\u307e\u3059\u3002                *" },

			{
					"Certificate reply does not contain public key for <alias>",
					"\u8a3c\u660e\u66f8\u5fdc\u7b54\u306b\u306f\u3001<{0}> \u306e\u516c\u958b\u9375\u306f\u542b\u307e\u308c\u307e\u305b\u3093\u3002" },
			{
					"Incomplete certificate chain in reply",
					"\u5fdc\u7b54\u3057\u305f\u8a3c\u660e\u9023\u9396\u306f\u4e0d\u5b8c\u5168\u3067\u3059\u3002" },
			{
					"Certificate chain in reply does not verify: ",
					"\u5fdc\u7b54\u3057\u305f\u8a3c\u660e\u9023\u9396\u306f\u691c\u8a3c\u3055\u308c\u3066\u3044\u307e\u305b\u3093: " },
			{
					"Top-level certificate in reply:\n",
					"\u5fdc\u7b54\u3057\u305f\u30c8\u30c3\u30d7\u30ec\u30d9\u30eb\u306e\u8a3c\u660e\u66f8:\n" },
			{ "... is not trusted. ",
					"... \u306f\u4fe1\u983c\u3055\u308c\u3066\u3044\u307e\u305b\u3093\u3002 " },
			{
					"Install reply anyway? [no]:  ",
					"\u5fdc\u7b54\u3092\u30a4\u30f3\u30b9\u30c8\u30fc\u30eb\u3057\u307e\u3059\u304b? [no]:  " },
			{ "NO", "NO" },
			{
					"Public keys in reply and keystore don't match",
					"\u5fdc\u7b54\u3057\u305f\u516c\u958b\u9375\u3068\u30ad\u30fc\u30b9\u30c8\u30a2\u304c\u4e00\u81f4\u3057\u307e\u305b\u3093\u3002" },
			{
					"Certificate reply and certificate in keystore are identical",
					"\u8a3c\u660e\u66f8\u5fdc\u7b54\u3068\u30ad\u30fc\u30b9\u30c8\u30a2\u5185\u306e\u8a3c\u660e\u66f8\u304c\u540c\u3058\u3067\u3059\u3002" },
			{
					"Failed to establish chain from reply",
					"\u5fdc\u7b54\u304b\u3089\u9023\u9396\u3092\u78ba\u7acb\u3067\u304d\u307e\u305b\u3093\u3067\u3057\u305f\u3002" },
			{ "n", "n" },
			{
					"Wrong answer, try again",
					"\u5fdc\u7b54\u304c\u9593\u9055\u3063\u3066\u3044\u307e\u3059\u3002\u3082\u3046\u4e00\u5ea6\u5b9f\u884c\u3057\u3066\u304f\u3060\u3055\u3044\u3002" },
			{
					"Secret key not generated, alias <alias> already exists",
					"\u79d8\u5bc6\u9375\u306f\u751f\u6210\u3055\u308c\u307e\u305b\u3093\u3067\u3057\u305f\u3002\u5225\u540d <{0}> \u306f\u3059\u3067\u306b\u5b58\u5728\u3057\u3066\u3044\u307e\u3059" },
			{
					"Please provide -keysize for secret key generation",
					"\u79d8\u5bc6\u9375\u306e\u751f\u6210\u6642\u306b\u306f -keysize \u3092\u6307\u5b9a\u3057\u3066\u304f\u3060\u3055\u3044" },
			{ "keytool usage:\n", "keytool \u306e\u4f7f\u3044\u65b9:\n" },

			{ "Extensions: ", "\u62e1\u5f35: " },

			{ "-certreq     [-v] [-protected]",
					"-certreq     [-v] [-protected]" },
			{ "\t     [-alias <alias>] [-sigalg <sigalg>]",
					"\t     [-alias <alias>] [-sigalg <sigalg>]" },
			{ "\t     [-file <csr_file>] [-keypass <keypass>]",
					"\t     [-file <csr_file>] [-keypass <keypass>]" },
			{ "\t     [-keystore <keystore>] [-storepass <storepass>]",
					"\t     [-keystore <keystore>] [-storepass <storepass>]" },
			{ "\t     [-storetype <storetype>] [-providername <name>]",
					"\t     [-storetype <storetype>] [-providername <name>]" },
			{
					"\t     [-providerclass <provider_class_name> [-providerarg <arg>]] ...",
					"\t     [-providerclass <provider_class_name> [-providerarg <arg>]] ..." },
			{ "\t     [-providerpath <pathlist>]",
					"\t     [-providerpath <pathlist>]" },
			{ "-delete      [-v] [-protected] -alias <alias>",
					"-delete      [-v] [-protected] -alias <alias>" },
			/** rest is same as -certreq starting from -keystore **/

			// {"-export      [-v] [-rfc] [-protected]",
			// "-export      [-v] [-rfc] [-protected]"},
			{ "-exportcert  [-v] [-rfc] [-protected]",
					"-exportcert  [-v] [-rfc] [-protected]" },
			{ "\t     [-alias <alias>] [-file <cert_file>]",
					"\t     [-alias <alias>] [-file <cert_file>]" },
			/** rest is same as -certreq starting from -keystore **/

			// {"-genkey      [-v] [-protected]",
			// "-genkey      [-v] [-protected]"},
			{ "-genkeypair  [-v] [-protected]",
					"-genkeypair  [-v] [-protected]" },
			{ "\t     [-alias <alias>]", "\t     [-alias <alias>]" },
			{ "\t     [-keyalg <keyalg>] [-keysize <keysize>]",
					"\t     [-keyalg <keyalg>] [-keysize <keysize>]" },
			{ "\t     [-sigalg <sigalg>] [-dname <dname>]",
					"\t     [-sigalg <sigalg>] [-dname <dname>]" },
			{ "\t     [-validity <valDays>] [-keypass <keypass>]",
					"\t     [-validity <valDays>] [-keypass <keypass>]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-genseckey   [-v] [-protected]",
					"-genseckey   [-v] [-protected]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-help", "-help" },
			// {"-identitydb  [-v] [-protected]",
			// "-identitydb  [-v] [-protected]"},
			// {"\t     [-file <idb_file>]", "\t     [-file <idb_file>]"},
			/** rest is same as -certreq starting from -keystore **/

			// {"-import      [-v] [-noprompt] [-trustcacerts] [-protected]",
			// "-import      [-v] [-noprompt] [-trustcacerts] [-protected]"},
			{ "-importcert  [-v] [-noprompt] [-trustcacerts] [-protected]",
					"-importcert  [-v] [-noprompt] [-trustcacerts] [-protected]" },
			{ "\t     [-alias <alias>]", "\t     [-alias <alias>]" },
			{ "\t     [-alias <alias>] [-keypass <keypass>]",
					"\t     [-alias <alias>] [-keypass <keypass>]" },
			{ "\t     [-file <cert_file>] [-keypass <keypass>]",
					"\t     [-file <cert_file>] [-keypass <keypass>]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-importkeystore [-v] ", "-importkeystore [-v] " },
			{
					"\t     [-srckeystore <srckeystore>] [-destkeystore <destkeystore>]",
					"\t     [-srckeystore <srckeystore>] [-destkeystore <destkeystore>]" },
			{
					"\t     [-srcstoretype <srcstoretype>] [-deststoretype <deststoretype>]",
					"\t     [-srcstoretype <srcstoretype>] [-deststoretype <deststoretype>]" },
			{ "\t     [-srcprotected] [-destprotected]",
					"\t     [-srcprotected] [-destprotected]" },
			{
					"\t     [-srcstorepass <srcstorepass>] [-deststorepass <deststorepass>]",
					"\t     [-srcstorepass <srcstorepass>] [-deststorepass <deststorepass>]" },
			{
					"\t     [-srcprovidername <srcprovidername>]\n\t     [-destprovidername <destprovidername>]", // \u3053\u306e\u884c\u306f\u9577\u3059\u304e\u307e\u3059\u30022
																													// \u884c\u306b\u5206\u5272\u3057\u3066\u304f\u3060\u3055\u3044
					"\t     [-srcprovidername <srcprovidername>]\n\t     [-destprovidername <destprovidername>]" },
			{ "\t     [-srcalias <srcalias> [-destalias <destalias>]",
					"\t     [-srcalias <srcalias> [-destalias <destalias>]" },
			{
					"\t       [-srckeypass <srckeypass>] [-destkeypass <destkeypass>]]",
					"\t       [-srckeypass <srckeypass>] [-destkeypass <destkeypass>]]" },
			{ "\t     [-noprompt]", "\t     [-noprompt]" },
			/** rest is same as -certreq starting from -keystore **/

			{
					"-changealias [-v] [-protected] -alias <alias> -destalias <destalias>",
					"-changealias [-v] [-protected] -alias <alias> -destalias <destalias>" },
			{ "\t     [-keypass <keypass>]", "\t     [-keypass <keypass>]" },

			// {"-keyclone    [-v] [-protected]",
			// "-keyclone    [-v] [-protected]"},
			// {"\t     [-alias <alias>] -dest <dest_alias>",
			// "\t     [-alias <alias>] -dest <dest_alias>"},
			// {"\t     [-keypass <keypass>] [-new <new_keypass>]",
			// "\t     [-keypass <keypass>] [-new <new_keypass>]"},
			/** rest is same as -certreq starting from -keystore **/

			{ "-keypasswd   [-v] [-alias <alias>]",
					"-keypasswd   [-v] [-alias <alias>]" },
			{ "\t     [-keypass <old_keypass>] [-new <new_keypass>]",
					"\t     [-keypass <old_keypass>] [-new <new_keypass>]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-list        [-v | -rfc] [-protected]",
					"-list        [-v | -rfc] [-protected]" },
			{ "\t     [-alias <alias>]", "\t     [-alias <alias>]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-printcert   [-v] [-file <cert_file>]",
					"-printcert   [-v] [-file <cert_file>]" },

			// {"-selfcert    [-v] [-protected]",
			// "-selfcert    [-v] [-protected]"},
			{ "\t     [-alias <alias>]", "\t     [-alias <alias>]" },
			// {"\t     [-dname <dname>] [-validity <valDays>]",
			// "\t     [-dname <dname>] [-validity <valDays>]"},
			// {"\t     [-keypass <keypass>] [-sigalg <sigalg>]",
			// "\t     [-keypass <keypass>] [-sigalg <sigalg>]"},
			/** rest is same as -certreq starting from -keystore **/

			{ "-storepasswd [-v] [-new <new_storepass>]",
					"-storepasswd [-v] [-new <new_storepass>]" },
			/** rest is same as -certreq starting from -keystore **/

			// policytool
			{
					"Warning: A public key for alias 'signers[i]' does not exist.  Make sure a KeyStore is properly configured.",
					"\u8b66\u544a: \u5225\u540d {0} \u306e\u516c\u958b\u9375\u304c\u5b58\u5728\u3057\u307e\u305b\u3093\u3002\u30ad\u30fc\u30b9\u30c8\u30a2\u304c\u6b63\u3057\u304f\u8a2d\u5b9a\u3055\u308c\u3066\u3044\u308b\u3053\u3068\u3092\u78ba\u8a8d\u3057\u3066\u304f\u3060\u3055\u3044\u3002" },
			{
					"Warning: Class not found: class",
					"\u8b66\u544a: \u30af\u30e9\u30b9\u304c\u898b\u3064\u304b\u308a\u307e\u305b\u3093: {0}" },
			{
					"Warning: Invalid argument(s) for constructor: arg",
					"\u8b66\u544a: \u30b3\u30f3\u30b9\u30c8\u30e9\u30af\u30bf\u306e\u5f15\u6570\u304c\u7121\u52b9\u3067\u3059: {0}" },
			{ "Illegal Principal Type: type",
					"\u4e0d\u6b63\u306a\u4e3b\u4f53\u306e\u30bf\u30a4\u30d7: {0}" },
			{ "Illegal option: option",
					"\u4e0d\u6b63\u306a\u30aa\u30d7\u30b7\u30e7\u30f3: {0}" },
			{ "Usage: policytool [options]",
					"\u4f7f\u3044\u65b9: policytool [options]" },
			{
					"  [-file <file>]    policy file location",
					"  [-file <file>]    \u30dd\u30ea\u30b7\u30fc\u30d5\u30a1\u30a4\u30eb\u306e\u5834\u6240" },
			{ "New", "\u65b0\u898f" },
			{ "Open", "\u958b\u304f" },
			{ "Save", "\u4fdd\u5b58" },
			{ "Save As", "\u5225\u540d\u4fdd\u5b58" },
			{ "View Warning Log", "\u8b66\u544a\u30ed\u30b0\u306e\u8868\u793a" },
			{ "Exit", "\u7d42\u4e86" },
			{ "Add Policy Entry",
					"\u30dd\u30ea\u30b7\u30fc\u30a8\u30f3\u30c8\u30ea\u306e\u8ffd\u52a0" },
			{ "Edit Policy Entry",
					"\u30dd\u30ea\u30b7\u30fc\u30a8\u30f3\u30c8\u30ea\u306e\u7de8\u96c6" },
			{ "Remove Policy Entry",
					"\u30dd\u30ea\u30b7\u30fc\u30a8\u30f3\u30c8\u30ea\u306e\u524a\u9664" },
			{ "Edit", "\u7de8\u96c6" },
			{ "Retain", "\u4fdd\u6301" },

			{
					"Warning: File name may include escaped backslash characters. "
							+ "It is not necessary to escape backslash characters "
							+ "(the tool escapes characters as necessary when writing "
							+ "the policy contents to the persistent store).\n\n"
							+ "Click on Retain to retain the entered name, or click on "
							+ "Edit to edit the name.",
					"Warning: File name may include escaped backslash characters. "
							+ "It is not necessary to escape backslash characters "
							+ "(the tool escapes characters as necessary when writing "
							+ "the policy contents to the persistent store).\n\n"
							+ "Click on Retain to retain the entered name, or click on "
							+ "Edit to edit the name." },

			{ "Add Public Key Alias",
					"\u516c\u958b\u9375\u306e\u5225\u540d\u3092\u8ffd\u52a0" },
			{ "Remove Public Key Alias",
					"\u516c\u958b\u9375\u306e\u5225\u540d\u3092\u524a\u9664" },
			{ "File", "\u30d5\u30a1\u30a4\u30eb" },
			{ "KeyStore", "\u30ad\u30fc\u30b9\u30c8\u30a2" },
			{ "Policy File:",
					"\u30dd\u30ea\u30b7\u30fc\u30d5\u30a1\u30a4\u30eb:" },
			{
					"Could not open policy file: policyFile: e.toString()",
					"\u30dd\u30ea\u30b7\u30fc\u30d5\u30a1\u30a4\u30eb\u3092\u958b\u3051\u307e\u305b\u3093\u3067\u3057\u305f: {0}: {1}" },
			{ "Policy Tool", "Policy Tool" },
			{
					"Errors have occurred while opening the policy configuration.  View the Warning Log for more information.",
					"\u30dd\u30ea\u30b7\u30fc\u69cb\u6210\u306e\u30aa\u30fc\u30d7\u30f3\u4e2d\u306b\u30a8\u30e9\u30fc\u304c\u767a\u751f\u3057\u307e\u3057\u305f\u3002\u8a73\u7d30\u306f\u8b66\u544a\u30ed\u30b0\u3092\u53c2\u7167\u3057\u3066\u304f\u3060\u3055\u3044\u3002" },
			{ "Error", "\u30a8\u30e9\u30fc" },
			{ "OK", "\u4e86\u89e3" },
			{ "Status", "\u72b6\u614b" },
			{ "Warning", "\u8b66\u544a" },
			{
					"Permission:                                                       ",
					"\u30a2\u30af\u30bb\u30b9\u6a29:                                                       " },
			{ "Principal Type:", "\u4e3b\u4f53\u306e\u30bf\u30a4\u30d7:" },
			{ "Principal Name:", "\u4e3b\u4f53\u306e\u540d\u524d:" },
			{
					"Target Name:                                                    ",
					"\u30bf\u30fc\u30b2\u30c3\u30c8\u540d:                                                    " },
			{
					"Actions:                                                             ",
					"\u30a2\u30af\u30b7\u30e7\u30f3:                                                             " },
			{
					"OK to overwrite existing file filename?",
					"\u65e2\u5b58\u306e\u30d5\u30a1\u30a4\u30eb {0} \u306b\u4e0a\u66f8\u304d\u3057\u307e\u3059\u304b?" },
			{ "Cancel", "\u53d6\u6d88\u3057" },
			{ "CodeBase:", "CodeBase:" },
			{ "SignedBy:", "SignedBy:" },
			{ "Add Principal", "\u4e3b\u4f53\u306e\u8ffd\u52a0" },
			{ "Edit Principal", "\u4e3b\u4f53\u306e\u7de8\u96c6" },
			{ "Remove Principal", "\u4e3b\u4f53\u306e\u524a\u9664" },
			{ "Principals:", "\u4e3b\u4f53:" },
			{ "  Add Permission",
					"  \u30a2\u30af\u30bb\u30b9\u6a29\u306e\u8ffd\u52a0" },
			{ "  Edit Permission",
					"  \u30a2\u30af\u30bb\u30b9\u6a29\u306e\u7de8\u96c6" },
			{ "Remove Permission",
					"\u30a2\u30af\u30bb\u30b9\u6a29\u306e\u524a\u9664" },
			{ "Done", "\u5b8c\u4e86" },
			{ "KeyStore URL:", "\u30ad\u30fc\u30b9\u30c8\u30a2 URL:" },
			{ "KeyStore Type:",
					"\u30ad\u30fc\u30b9\u30c8\u30a2\u306e\u30bf\u30a4\u30d7:" },
			{ "KeyStore Provider:",
					"\u30ad\u30fc\u30b9\u30c8\u30a2\u30d7\u30ed\u30d0\u30a4\u30c0:" },
			{ "KeyStore Password URL:",
					"\u30ad\u30fc\u30b9\u30c8\u30a2\u30d1\u30b9\u30ef\u30fc\u30c9 URL:" },
			{ "Principals", "\u4e3b\u4f53" },
			{ "  Edit Principal:", "  \u4e3b\u4f53\u306e\u7de8\u96c6:" },
			{ "  Add New Principal:",
					"  \u4e3b\u4f53\u306e\u65b0\u898f\u8ffd\u52a0:" },
			{ "Permissions", "\u30a2\u30af\u30bb\u30b9\u6a29" },
			{ "  Edit Permission:",
					"  \u30a2\u30af\u30bb\u30b9\u6a29\u306e\u7de8\u96c6:" },
			{ "  Add New Permission:",
					"  \u65b0\u898f\u30a2\u30af\u30bb\u30b9\u6a29\u306e\u8ffd\u52a0:" },
			{ "Signed By:", "\u7f72\u540d\u8005:" },
			{
					"Cannot Specify Principal with a Wildcard Class without a Wildcard Name",
					"\u30ef\u30a4\u30eb\u30c9\u30ab\u30fc\u30c9\u540d\u306e\u306a\u3044\u30ef\u30a4\u30eb\u30c9\u30ab\u30fc\u30c9\u30af\u30e9\u30b9\u3092\u4f7f\u3063\u3066\u4e3b\u4f53\u3092\u6307\u5b9a\u3059\u308b\u3053\u3068\u306f\u3067\u304d\u307e\u305b\u3093\u3002" },
			{
					"Cannot Specify Principal without a Name",
					"\u540d\u524d\u3092\u4f7f\u308f\u305a\u306b\u4e3b\u4f53\u3092\u6307\u5b9a\u3059\u308b\u3053\u3068\u306f\u3067\u304d\u307e\u305b\u3093\u3002" },
			{
					"Permission and Target Name must have a value",
					"\u30a2\u30af\u30bb\u30b9\u6a29\u3068\u30bf\u30fc\u30b2\u30c3\u30c8\u540d\u306f\u3001\u5024\u3092\u4fdd\u6301\u3059\u308b\u5fc5\u8981\u304c\u3042\u308a\u307e\u3059\u3002" },
			{
					"Remove this Policy Entry?",
					"\u3053\u306e\u30dd\u30ea\u30b7\u30fc\u30a8\u30f3\u30c8\u30ea\u3092\u524a\u9664\u3057\u307e\u3059\u304b?" },
			{ "Overwrite File",
					"\u30d5\u30a1\u30a4\u30eb\u3092\u4e0a\u66f8\u304d\u3057\u307e\u3059\u3002" },
			{
					"Policy successfully written to filename",
					"\u30dd\u30ea\u30b7\u30fc\u306e {0} \u3078\u306e\u66f8\u304d\u8fbc\u307f\u306b\u6210\u529f\u3057\u307e\u3057\u305f\u3002" },
			{ "null filename",
					"\u30d5\u30a1\u30a4\u30eb\u540d\u304c\u3042\u308a\u307e\u305b\u3093\u3002" },
			{ "Save changes?",
					"\u5909\u66f4\u3092\u4fdd\u5b58\u3057\u307e\u3059\u304b?" },
			{ "Yes", "\u306f\u3044" },
			{ "No", "\u3044\u3044\u3048" },
			{ "Policy Entry",
					"\u30dd\u30ea\u30b7\u30fc\u30a8\u30f3\u30c8\u30ea" },
			{ "Save Changes",
					"\u5909\u66f4\u3092\u4fdd\u5b58\u3057\u307e\u3059\u3002" },
			{
					"No Policy Entry selected",
					"\u30dd\u30ea\u30b7\u30fc\u30a8\u30f3\u30c8\u30ea\u304c\u9078\u629e\u3055\u308c\u3066\u3044\u307e\u305b\u3093\u3002" },
			{ "Unable to open KeyStore: ex.toString()",
					"\u30ad\u30fc\u30b9\u30c8\u30a2 {0} \u3092\u958b\u3051\u307e\u305b\u3093" },
			{
					"No principal selected",
					"\u4e3b\u4f53\u304c\u9078\u629e\u3055\u308c\u3066\u3044\u307e\u305b\u3093\u3002" },
			{
					"No permission selected",
					"\u30a2\u30af\u30bb\u30b9\u6a29\u304c\u9078\u629e\u3055\u308c\u3066\u3044\u307e\u305b\u3093\u3002" },
			{ "name", "\u540d\u524d" },
			{ "configuration type", "\u8a2d\u5b9a\u30bf\u30a4\u30d7" },
			{ "environment variable name", "\u74b0\u5883\u5909\u6570\u540d" },
			{ "library name", "\u30e9\u30a4\u30d6\u30e9\u30ea\u540d" },
			{ "package name", "\u30d1\u30c3\u30b1\u30fc\u30b8\u540d" },
			{ "policy type", "\u30dd\u30ea\u30b7\u30fc\u30bf\u30a4\u30d7" },
			{ "property name", "\u30d7\u30ed\u30d1\u30c6\u30a3\u540d" },
			{ "provider name", "\u30d7\u30ed\u30d0\u30a4\u30c0\u540d" },
			{ "Principal List", "\u4e3b\u4f53\u306e\u30ea\u30b9\u30c8" },
			{ "Permission List",
					"\u30a2\u30af\u30bb\u30b9\u6a29\u306e\u30ea\u30b9\u30c8" },
			{ "Code Base", "\u30b3\u30fc\u30c9\u30d9\u30fc\u30b9" },
			{ "KeyStore U R L:", "\u30ad\u30fc\u30b9\u30c8\u30a2 U R L:" },
			{ "KeyStore Password U R L:",
					"\u30ad\u30fc\u30b9\u30c8\u30a2\u30d1\u30b9\u30ef\u30fc\u30c9 U R L:" },

			// javax.security.auth.PrivateCredentialPermission
			{ "invalid null input(s)",
					"null \u306e\u5165\u529b\u306f\u7121\u52b9\u3067\u3059\u3002" },
			{
					"actions can only be 'read'",
					"\u30a2\u30af\u30b7\u30e7\u30f3\u306f '\u8aad\u307f\u8fbc\u307f' \u306e\u307f\u53ef\u80fd\u3067\u3059\u3002" },
			{
					"permission name [name] syntax invalid: ",
					"\u30a2\u30af\u30bb\u30b9\u6a29\u540d [{0}] \u306e\u69cb\u6587\u304c\u7121\u52b9\u3067\u3059: " },
			{
					"Credential Class not followed by a Principal Class and Name",
					"Credential \u30af\u30e9\u30b9\u306e\u6b21\u306b Principal \u30af\u30e9\u30b9\u304a\u3088\u3073\u540d\u524d\u304c\u3042\u308a\u307e\u305b\u3093\u3002" },
			{
					"Principal Class not followed by a Principal Name",
					"Principal \u30af\u30e9\u30b9\u306e\u6b21\u306b\u4e3b\u4f53\u540d\u304c\u3042\u308a\u307e\u305b\u3093\u3002" },
			{
					"Principal Name must be surrounded by quotes",
					"\u4e3b\u4f53\u540d\u306f\u5f15\u7528\u7b26\u3067\u56f2\u3080\u5fc5\u8981\u304c\u3042\u308a\u307e\u3059\u3002" },
			{
					"Principal Name missing end quote",
					"\u4e3b\u4f53\u540d\u306e\u6700\u5f8c\u306b\u5f15\u7528\u7b26\u304c\u3042\u308a\u307e\u305b\u3093\u3002" },
			{
					"PrivateCredentialPermission Principal Class can not be a wildcard (*) value if Principal Name is not a wildcard (*) value",
					"\u4e3b\u4f53\u540d\u304c\u30ef\u30a4\u30eb\u30c9\u30ab\u30fc\u30c9 (*) \u5024\u3067\u306a\u3044\u5834\u5408\u3001PrivateCredentialPermission \u306e Principal \u30af\u30e9\u30b9\u3092\u30ef\u30a4\u30eb\u30c9\u30ab\u30fc\u30c9 (*) \u5024\u306b\u3059\u308b\u3053\u3068\u306f\u3067\u304d\u307e\u305b\u3093\u3002" },
			{ "CredOwner:\n\tPrincipal Class = class\n\tPrincipal Name = name",
					"CredOwner:\n\tPrincipal \u30af\u30e9\u30b9 = {0}\n\t\u4e3b\u4f53\u540d = {1}" },

			// javax.security.auth.x500
			{
					"provided null name",
					"\u7a7a\u306e\u540d\u524d\u304c\u6307\u5b9a\u3055\u308c\u307e\u3057\u305f\u3002" },
			{
					"provided null keyword map",
					"null \u306e\u30ad\u30fc\u30ef\u30fc\u30c9\u30de\u30c3\u30d7\u304c\u6307\u5b9a\u3055\u308c\u307e\u3057\u305f" },
			{
					"provided null OID map",
					"null \u306e OID \u30de\u30c3\u30d7\u304c\u6307\u5b9a\u3055\u308c\u307e\u3057\u305f" },

			// javax.security.auth.Subject
			{
					"invalid null AccessControlContext provided",
					"\u7121\u52b9\u306a null AccessControlContext \u304c\u6307\u5b9a\u3055\u308c\u307e\u3057\u305f\u3002" },
			{
					"invalid null action provided",
					"\u7121\u52b9\u306a null \u30a2\u30af\u30b7\u30e7\u30f3\u304c\u6307\u5b9a\u3055\u308c\u307e\u3057\u305f\u3002" },
			{
					"invalid null Class provided",
					"\u7121\u52b9\u306a null \u30af\u30e9\u30b9\u304c\u6307\u5b9a\u3055\u308c\u307e\u3057\u305f\u3002" },
			{ "Subject:\n", "\u30b5\u30d6\u30b8\u30a7\u30af\u30c8:\n" },
			{ "\tPrincipal: ", "\t\u4e3b\u4f53: " },
			{ "\tPublic Credential: ", "\t\u516c\u958b\u8cc7\u683c: " },
			{
					"\tPrivate Credentials inaccessible\n",
					"\t\u975e\u516c\u958b\u8cc7\u683c\u306b\u306f\u30a2\u30af\u30bb\u30b9\u3067\u304d\u307e\u305b\u3093\u3002\n" },
			{ "\tPrivate Credential: ", "\t\u975e\u516c\u958b\u8cc7\u683c: " },
			{
					"\tPrivate Credential inaccessible\n",
					"\t\u975e\u516c\u958b\u8cc7\u683c\u306b\u306f\u30a2\u30af\u30bb\u30b9\u3067\u304d\u307e\u305b\u3093\u3002\n" },
			{
					"Subject is read-only",
					"\u30b5\u30d6\u30b8\u30a7\u30af\u30c8\u306f\u8aad\u307f\u53d6\u308a\u5c02\u7528\u3067\u3059\u3002" },
			{
					"attempting to add an object which is not an instance of java.security.Principal to a Subject's Principal Set",
					"java.security.Principal \u306e\u30a4\u30f3\u30b9\u30bf\u30f3\u30b9\u3067\u306f\u306a\u3044\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u3092\u3001\u30b5\u30d6\u30b8\u30a7\u30af\u30c8\u306e\u4e3b\u4f53\u30bb\u30c3\u30c8\u306b\u8ffd\u52a0\u3057\u3088\u3046\u3068\u3057\u307e\u3057\u305f\u3002" },
			{
					"attempting to add an object which is not an instance of class",
					"{0} \u306e\u30a4\u30f3\u30b9\u30bf\u30f3\u30b9\u3067\u306f\u306a\u3044\u30aa\u30d6\u30b8\u30a7\u30af\u30c8\u3092\u8ffd\u52a0\u3057\u3088\u3046\u3068\u3057\u307e\u3057\u305f\u3002" },

			// javax.security.auth.login.AppConfigurationEntry
			{ "LoginModuleControlFlag: ", "LoginModuleControlFlag: " },

			// javax.security.auth.login.LoginContext
			{ "Invalid null input: name",
					"\u7121\u52b9\u306a null \u5165\u529b: \u540d\u524d" },
			{
					"No LoginModules configured for name",
					"{0} \u7528\u306b\u69cb\u6210\u3055\u308c\u305f LoginModules \u306f\u3042\u308a\u307e\u305b\u3093\u3002" },
			{
					"invalid null Subject provided",
					"\u7121\u52b9\u306a null \u30b5\u30d6\u30b8\u30a7\u30af\u30c8\u304c\u6307\u5b9a\u3055\u308c\u307e\u3057\u305f\u3002" },
			{
					"invalid null CallbackHandler provided",
					"\u7121\u52b9\u306a null CallbackHandler \u304c\u6307\u5b9a\u3055\u308c\u307e\u3057\u305f\u3002" },
			{
					"null subject - logout called before login",
					"null \u30b5\u30d6\u30b8\u30a7\u30af\u30c8 - \u30ed\u30b0\u30a4\u30f3\u3059\u308b\u524d\u306b\u30ed\u30b0\u30a2\u30a6\u30c8\u304c\u547c\u3073\u51fa\u3055\u308c\u307e\u3057\u305f\u3002" },
			{
					"unable to instantiate LoginModule, module, because it does not provide a no-argument constructor",
					"LoginModule {0} \u306f\u5f15\u6570\u3092\u53d6\u3089\u306a\u3044\u30b3\u30f3\u30b9\u30c8\u30e9\u30af\u30bf\u3092\u6307\u5b9a\u3067\u304d\u306a\u3044\u305f\u3081\u3001\u30a4\u30f3\u30b9\u30bf\u30f3\u30b9\u3092\u751f\u6210\u3067\u304d\u307e\u305b\u3093\u3002" },
			{
					"unable to instantiate LoginModule",
					"LoginModule \u306e\u30a4\u30f3\u30b9\u30bf\u30f3\u30b9\u3092\u751f\u6210\u3067\u304d\u307e\u305b\u3093\u3002" },
			{
					"unable to instantiate LoginModule: ",
					"LoginModule \u306e\u30a4\u30f3\u30b9\u30bf\u30f3\u30b9\u3092\u751f\u6210\u3067\u304d\u307e\u305b\u3093: " },
			{
					"unable to find LoginModule class: ",
					"LoginModule \u30af\u30e9\u30b9\u3092\u691c\u51fa\u3067\u304d\u307e\u305b\u3093: " },
			{ "unable to access LoginModule: ",
					"LoginModule \u306b\u30a2\u30af\u30bb\u30b9\u3067\u304d\u307e\u305b\u3093: " },
			{
					"Login Failure: all modules ignored",
					"\u30ed\u30b0\u30a4\u30f3\u5931\u6557: \u3059\u3079\u3066\u306e\u30e2\u30b8\u30e5\u30fc\u30eb\u306f\u7121\u8996\u3055\u308c\u307e\u3059\u3002" },

			// sun.security.provider.PolicyFile

			{
					"java.security.policy: error parsing policy:\n\tmessage",
					"java.security.policy: {0} \u306e\u69cb\u6587\u89e3\u6790\u30a8\u30e9\u30fc:\n\t{1}" },
			{
					"java.security.policy: error adding Permission, perm:\n\tmessage",
					"java.security.policy: \u30a2\u30af\u30bb\u30b9\u6a29 {0} \u306e\u8ffd\u52a0\u30a8\u30e9\u30fc:\n\t{1}" },
			{
					"java.security.policy: error adding Entry:\n\tmessage",
					"java.security.policy: \u30a8\u30f3\u30c8\u30ea\u306e\u8ffd\u52a0\u30a8\u30e9\u30fc:\n\t{0}" },
			{ "alias name not provided (pe.name)",
					"\u5225\u540d\u306e\u6307\u5b9a\u304c\u3042\u308a\u307e\u305b\u3093 ({0})" },
			{
					"unable to perform substitution on alias, suffix",
					"\u5225\u540d {0} \u306b\u5bfe\u3057\u3066\u7f6e\u63db\u64cd\u4f5c\u304c\u3067\u304d\u307e\u305b\u3093" },
			{
					"substitution value, prefix, unsupported",
					"\u7f6e\u63db\u5024 {0} \u306f\u30b5\u30dd\u30fc\u30c8\u3055\u308c\u3066\u3044\u307e\u305b\u3093" },
			{ "(", "(" },
			{ ")", ")" },
			{
					"type can't be null",
					"\u5165\u529b\u3092 null \u306b\u3059\u308b\u3053\u3068\u306f\u3067\u304d\u307e\u305b\u3093\u3002" },

			// sun.security.provider.PolicyParser
			{
					"keystorePasswordURL can not be specified without also specifying keystore",
					"\u30ad\u30fc\u30b9\u30c8\u30a2\u3092\u6307\u5b9a\u3057\u306a\u3044\u5834\u5408\u3001keystorePasswordURL \u306f\u6307\u5b9a\u3067\u304d\u307e\u305b\u3093\u3002" },
			{
					"expected keystore type",
					"\u671f\u5f85\u3055\u308c\u305f\u30ad\u30fc\u30b9\u30c8\u30a2\u30bf\u30a4\u30d7" },
			{
					"expected keystore provider",
					"\u671f\u5f85\u3055\u308c\u305f\u30ad\u30fc\u30b9\u30c8\u30a2\u30d7\u30ed\u30d0\u30a4\u30c0" },
			{ "multiple Codebase expressions",
					"\u8907\u6570\u306e Codebase \u5f0f" },
			{ "multiple SignedBy expressions",
					"\u8907\u6570\u306e SignedBy \u5f0f" },
			{
					"SignedBy has empty alias",
					"SignedBy \u306f\u7a7a\u306e\u5225\u540d\u3092\u4fdd\u6301\u3057\u307e\u3059\u3002" },
			{
					"can not specify Principal with a wildcard class without a wildcard name",
					"\u30ef\u30a4\u30eb\u30c9\u30ab\u30fc\u30c9\u540d\u306e\u306a\u3044\u30ef\u30a4\u30eb\u30c9\u30ab\u30fc\u30c9\u30af\u30e9\u30b9\u3092\u4f7f\u3063\u3066\u3001\u4e3b\u4f53\u3092\u6307\u5b9a\u3059\u308b\u3053\u3068\u306f\u3067\u304d\u307e\u305b\u3093\u3002" },
			{
					"expected codeBase or SignedBy or Principal",
					"\u671f\u5f85\u3055\u308c\u305f codeBase\u3001SignedBy\u3001\u307e\u305f\u306f Principal" },
			{
					"expected permission entry",
					"\u671f\u5f85\u3055\u308c\u305f\u30a2\u30af\u30bb\u30b9\u6a29\u30a8\u30f3\u30c8\u30ea" },
			{ "number ", "\u6570 " },
			{
					"expected [expect], read [end of file]",
					"[{0}] \u3067\u306f\u306a\u304f [\u30d5\u30a1\u30a4\u30eb\u306e\u7d42\u308f\u308a] \u304c\u8aad\u307f\u8fbc\u307e\u308c\u307e\u3057\u305f\u3002" },
			{
					"expected [;], read [end of file]",
					"[;] \u3067\u306f\u306a\u304f [\u30d5\u30a1\u30a4\u30eb\u306e\u7d42\u308f\u308a] \u304c\u8aad\u307f\u8fbc\u307e\u308c\u307e\u3057\u305f\u3002" },
			{ "line number: msg", "\u884c {0}: {1}" },
			{
					"line number: expected [expect], found [actual]",
					"\u884c {0}: [{1}] \u3067\u306f\u306a\u304f [{2}] \u304c\u691c\u51fa\u3055\u308c\u307e\u3057\u305f\u3002" },
			{ "null principalClass or principalName",
					"null \u306e principalClass \u307e\u305f\u306f principalName" },

			// sun.security.pkcs11.SunPKCS11
			{ "PKCS11 Token [providerName] Password: ",
					"PKCS11 \u30c8\u30fc\u30af\u30f3 [{0}] \u30d1\u30b9\u30ef\u30fc\u30c9: " },

			/* --- DEPRECATED --- */
			// javax.security.auth.Policy
			{
					"unable to instantiate Subject-based policy",
					"\u30b5\u30d6\u30b8\u30a7\u30af\u30c8\u30d9\u30fc\u30b9\u306e\u30dd\u30ea\u30b7\u30fc\u306e\u30a4\u30f3\u30b9\u30bf\u30f3\u30b9\u3092\u751f\u6210\u3067\u304d\u307e\u305b\u3093" } };

	/**
	 * Returns the contents of this <code>ResourceBundle</code>.
	 * 
	 * <p>
	 * 
	 * @return the contents of this <code>ResourceBundle</code>.
	 */
	public Object[][] getContents() {
		return contents;
	}
}
