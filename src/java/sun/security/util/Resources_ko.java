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
public class Resources_ko extends java.util.ListResourceBundle {

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
			{ "keytool error: ", "keytool \uc624\ub958: " },
			{ "Illegal option:  ", "\uc798\ubabb\ub41c \uc635\uc158:   " },
			{ "Try keytool -help", "keytool -help \uc0ac\uc6a9" },
			{
					"Command option <flag> needs an argument.",
					"\uba85\ub839 \uc635\uc158 {0}\uc5d0 \uc778\uc218\uac00 \ud544\uc694\ud569\ub2c8\ub2e4." },
			{
					"Warning:  Different store and key passwords not supported for PKCS12 KeyStores. Ignoring user-specified <command> value.",
					"\uacbd\uace0:\t  \ub2e4\ub978 \uc800\uc7a5\uc18c \ubc0f \ud0a4 \uc554\ud638\ub294 PKCS12 \ud0a4 \uc800\uc7a5\uc18c\uc5d0 \ub300\ud574 \uc9c0\uc6d0\ub418\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4. \uc0ac\uc6a9\uc790\uac00 \uc9c0\uc815\ud55c {0} \uac12\uc744 \ubb34\uc2dc\ud569\ub2c8\ub2e4." },
			{
					"-keystore must be NONE if -storetype is {0}",
					"-storetype\uc774 {0}\uc778 \uacbd\uc6b0 -keystore\uac00 NONE\uc774\uc5b4\uc57c \ud568" },
			{
					"Too may retries, program terminated",
					"\uc7ac\uc2dc\ub3c4 \ud69f\uc218\uac00 \ub108\ubb34 \ub9ce\uc544 \ud504\ub85c\uadf8\ub7a8\uc774 \uc885\ub8cc\ub418\uc5c8\uc2b5\ub2c8\ub2e4." },
			{
					"-storepasswd and -keypasswd commands not supported if -storetype is {0}",
					"-storetype\uc774 {0}\uc778 \uacbd\uc6b0 -storepasswd \ubc0f -keypasswd \uba85\ub839\uc774 \uc9c0\uc6d0\ub418\uc9c0 \uc54a\uc74c" },
			{
					"-keypasswd commands not supported if -storetype is PKCS12",
					"-storetype\uc774 PKCS12\uc778 \uacbd\uc6b0\uc5d0\ub294 -keypasswd \uba85\ub839\uc774 \uc9c0\uc6d0\ub418\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4." },
			{
					"-keypass and -new can not be specified if -storetype is {0}",
					"-storetype\uc774 {0}\uc778 \uacbd\uc6b0 -keypass \ubc0f -new\ub97c \uc9c0\uc815\ud560 \uc218 \uc5c6\uc74c" },
			{
					"if -protected is specified, then -storepass, -keypass, and -new must not be specified",
					"-protected\uac00 \uc9c0\uc815\ub41c \uacbd\uc6b0 -storepass, -keypass \ubc0f -new\ub294 \uc9c0\uc815\ub418\uc9c0 \uc54a\uc544\uc57c \ud569\ub2c8\ub2e4." },
			{
					"if -srcprotected is specified, then -srcstorepass and -srckeypass must not be specified",
					"-srcprotected\ub97c \uc9c0\uc815\ud55c \uacbd\uc6b0 -srcstorepass \ubc0f -srckeypass\ub97c \uc9c0\uc815\ud558\uba74 \uc548 \ub429\ub2c8\ub2e4." },
			{
					"if keystore is not password protected, then -storepass, -keypass, and -new must not be specified",
					"\ud0a4 \uc800\uc7a5\uc18c\uac00 \uc554\ud638\ub85c \ubcf4\ud638\ub418\uc9c0 \uc54a\uc740 \uacbd\uc6b0 -storepass, -keypass \ubc0f -new\ub97c \uc9c0\uc815\ud558\uba74 \uc548 \ub428" },
			{
					"if source keystore is not password protected, then -srcstorepass and -srckeypass must not be specified",
					"\uc18c\uc2a4 \ud0a4 \uc800\uc7a5\uc18c\uac00 \uc554\ud638\ub85c \ubcf4\ud638\ub418\uc9c0 \uc54a\uc740 \uacbd\uc6b0 -srcstorepass \ubc0f -srckeypass\ub97c \uc9c0\uc815\ud558\uba74 \uc548 \ub428" },
			{ "Validity must be greater than zero",
					"\uc720\ud6a8\uc131\uc740 0\ubcf4\ub2e4 \ucee4\uc57c \ud569\ub2c8\ub2e4." },
			{ "provName not a provider",
					" {0}\uc740(\ub294) \uacf5\uae09\uc790\uac00 \uc544\ub2d9\ub2c8\ub2e4." },
			{
					"Usage error: no command provided",
					"\uc0ac\uc6a9\ubc95 \uc624\ub958: \uba85\ub839\uc744 \uc785\ub825\ud558\uc9c0 \uc54a\uc558\uc2b5\ub2c8\ub2e4." },
			{
					"Usage error, <arg> is not a legal command",
					"\uc0ac\uc6a9\ubc95 \uc624\ub958\uc785\ub2c8\ub2e4. {0}\uc740(\ub294) \uc720\ud6a8\ud55c \uba85\ub839\uc774 \uc544\ub2d9\ub2c8\ub2e4." },
			{
					"Source keystore file exists, but is empty: ",
					"\uc18c\uc2a4 \ud0a4 \uc800\uc7a5\uc18c \ud30c\uc77c\uc774 \uc788\uc9c0\ub9cc \ube44\uc5b4 \uc788\uc2b5\ub2c8\ub2e4. " },
			{ "Please specify -srckeystore",
					"-srckeystore\ub97c \uc9c0\uc815\ud558\uc2ed\uc2dc\uc624." },
			{
					"Must not specify both -v and -rfc with 'list' command",
					"'list' \uba85\ub839\uc5d0 -v\uc640 -rfc\ub97c \ubaa8\ub450 \uc9c0\uc815\ud574\uc11c\ub294 \uc548 \ub429\ub2c8\ub2e4." },
			{
					"Key password must be at least 6 characters",
					"\ud0a4 \uc554\ud638\ub294 \uc5ec\uc12f \uae00\uc790 \uc774\uc0c1\uc774\uc5b4\uc57c \ud569\ub2c8\ub2e4." },
			{
					"New password must be at least 6 characters",
					"\uc0c8 \uc554\ud638\ub294 \uc5ec\uc12f \uae00\uc790 \uc774\uc0c1\uc774\uc5b4\uc57c \ud569\ub2c8\ub2e4." },
			{
					"Keystore file exists, but is empty: ",
					"keystore \ud30c\uc77c\uc774 \uc788\uc9c0\ub9cc \ube44\uc5b4 \uc788\uc2b5\ub2c8\ub2e4: " },
			{ "Keystore file does not exist: ",
					"keystore \ud30c\uc77c\uc774 \uc5c6\uc2b5\ub2c8\ub2e4:  " },
			{ "Must specify destination alias",
					"\ub300\uc0c1 \ubcc4\uce6d\uc744 \uc9c0\uc815\ud574\uc57c \ud569\ub2c8\ub2e4." },
			{ "Must specify alias",
					"\ubcc4\uce6d\uc744 \uc9c0\uc815\ud574\uc57c \ud569\ub2c8\ub2e4." },
			{
					"Keystore password must be at least 6 characters",
					"Keystore \uc554\ud638\ub294 \uc5ec\uc12f \uae00\uc790 \uc774\uc0c1\uc774\uc5b4\uc57c \ud569\ub2c8\ub2e4." },
			{ "Enter keystore password:  ",
					"keystore \uc554\ud638\ub97c \uc785\ub825\ud558\uc2ed\uc2dc\uc624:  " },
			{ "Enter source keystore password:  ",
					"\uc18c\uc2a4 \ud0a4 \uc800\uc7a5\uc18c \uc554\ud638 \uc785\ub825:  " },
			{ "Enter destination keystore password:  ",
					"\ub300\uc0c1 \ud0a4 \uc800\uc7a5\uc18c \uc554\ud638 \uc785\ub825:  " },
			{
					"Keystore password is too short - must be at least 6 characters",
					"Keystore \uc554\ud638\uac00 \ub108\ubb34 \uc9e7\uc2b5\ub2c8\ub2e4. \uc5ec\uc12f \uae00\uc790 \uc774\uc0c1\uc774\uc5b4\uc57c \ud569\ub2c8\ub2e4." },
			{ "Unknown Entry Type",
					"\uc54c \uc218 \uc5c6\ub294 \ud56d\ubaa9 \uc720\ud615" },
			{
					"Too many failures. Alias not changed",
					"\uc2e4\ud328 \ud69f\uc218\uac00 \ub108\ubb34 \ub9ce\uc2b5\ub2c8\ub2e4. \ubcc4\uce6d\uc774 \ubcc0\uacbd\ub418\uc9c0 \uc54a\uc558\uc2b5\ub2c8\ub2e4." },
			{
					"Entry for alias <alias> successfully imported.",
					"\ubcc4\uce6d {0}\uc5d0 \ub300\ud55c \ud56d\ubaa9\uc744 \uc131\uacf5\uc801\uc73c\ub85c \uac00\uc838\uc654\uc2b5\ub2c8\ub2e4." },
			{
					"Entry for alias <alias> not imported.",
					"\ubcc4\uce6d {0}\uc5d0 \ub300\ud55c \ud56d\ubaa9\uc744 \uac00\uc838\uc624\uc9c0 \ubabb\ud588\uc2b5\ub2c8\ub2e4." },
			{
					"Problem importing entry for alias <alias>: <exception>.\nEntry for alias <alias> not imported.",
					"\ubcc4\uce6d {0}\uc5d0 \ub300\ud55c \ud56d\ubaa9\uc744 \uac00\uc838\uc624\ub294 \ub3d9\uc548 \ubb38\uc81c\uac00 \ubc1c\uc0dd\ud588\uc2b5\ub2c8\ub2e4. {1}.\n\ubcc4\uce6d {0\uc5d0 \ub300\ud55c \ud56d\ubaa9\uc744 \uac00\uc838\uc624\uc9c0 \ubabb\ud588\uc2b5\ub2c8\ub2e4." },
			{
					"Import command completed:  <ok> entries successfully imported, <fail> entries failed or cancelled",
					"\uac00\uc838\uc624\uae30 \uba85\ub839 \uc644\ub8cc:  {0}\uac1c \ud56d\ubaa9\uc744 \uc131\uacf5\uc801\uc73c\ub85c \uac00\uc838\uc654\uc2b5\ub2c8\ub2e4. {1}\uac1c \ud56d\ubaa9\uc740 \uc2e4\ud328\ud588\uac70\ub098 \ucde8\uc18c\ub418\uc5c8\uc2b5\ub2c8\ub2e4." },
			{
					"Warning: Overwriting existing alias <alias> in destination keystore",
					"\uacbd\uace0:\t \ub300\uc0c1 \ud0a4 \uc800\uc7a5\uc18c\uc5d0\uc11c \uae30\uc874 \ubcc4\uce6d {0}\uc744(\ub97c) \ub36e\uc5b4\uc4f0\ub294 \uc911" },
			{
					"Existing entry alias <alias> exists, overwrite? [no]:  ",
					"\uae30\uc874 \ud56d\ubaa9 \ubcc4\uce6d {0}\uc774(\uac00) \uc788\uc2b5\ub2c8\ub2e4. \ub36e\uc5b4\uc4f0\uc2dc\uaca0\uc2b5\ub2c8\uae4c? [\uc544\ub2c8\uc624]:  " },
			{
					"Too many failures - try later",
					"\uc624\ub958\uac00 \ub108\ubb34 \ub9ce\uc2b5\ub2c8\ub2e4. \ub098\uc911\uc5d0 \ub2e4\uc2dc \uc2dc\ub3c4\ud558\uc2ed\uc2dc\uc624." },
			{
					"Certification request stored in file <filename>",
					"\uc778\uc99d \uc694\uccad\uc774 <{0}> \ud30c\uc77c\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4." },
			{ "Submit this to your CA",
					"CA\uc5d0\uac8c \uc81c\ucd9c\ud558\uc2ed\uc2dc\uc624." },
			{
					"if alias not specified, destalias, srckeypass, and destkeypass must not be specified",
					"\ubcc4\uce6d\uc744 \uc9c0\uc815\ud558\uc9c0 \uc54a\uc740 \uacbd\uc6b0 destalias, srckeypass \ubc0f destkeypass\ub97c \uc9c0\uc815\ud558\uba74 \uc548 \ub429\ub2c8\ub2e4." },
			{
					"Certificate stored in file <filename>",
					"\uc778\uc99d\uc11c\uac00 <{0}> \ud30c\uc77c\uc5d0 \uc800\uc7a5\ub418\uc5c8\uc2b5\ub2c8\ub2e4." },
			{
					"Certificate reply was installed in keystore",
					"\uc778\uc99d\uc11c \ud68c\uc2e0\uc774 keystore\uc5d0 \uc124\uce58\ub418\uc5c8\uc2b5\ub2c8\ub2e4." },
			{
					"Certificate reply was not installed in keystore",
					"\uc778\uc99d \ud68c\uc2e0\uc774 keystore\uc5d0 \uc124\uce58\ub418\uc9c0 \uc54a\uc558\uc2b5\ub2c8\ub2e4." },
			{ "Certificate was added to keystore",
					"\uc778\uc99d\uc774 keystore\uc5d0 \ucd94\uac00\ub418\uc5c8\uc2b5\ub2c8\ub2e4." },
			{
					"Certificate was not added to keystore",
					"\uc778\uc99d\uc11c\uac00 keystore\uc5d0 \ucd94\uac00\ub418\uc9c0 \uc54a\uc558\uc2b5\ub2c8\ub2e4." },
			{ "[Storing ksfname]", "[{0} \uc800\uc7a5 \uc911]" },
			{
					"alias has no public key (certificate)",
					"{0}\uc5d0\ub294 \uacf5\uac1c \ud0a4(\uc778\uc99d\uc11c)\uac00 \uc5c6\uc2b5\ub2c8\ub2e4." },
			{
					"Cannot derive signature algorithm",
					"\uc11c\uba85 \uc54c\uace0\ub9ac\uc998\uc744 \uc720\ub3c4\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4." },
			{ "Alias <alias> does not exist",
					"<{0}> \ubcc4\uce6d\uc774 \uc5c6\uc2b5\ub2c8\ub2e4." },
			{ "Alias <{0}> has no certificate",
					"<{0}> \ubcc4\uce6d\uc5d0 \uc778\uc99d\uc11c\uac00 \uc5c6\uc2b5\ub2c8\ub2e4." },
			{
					"Key pair not generated, alias <alias> already exists",
					"\ud0a4 \uc30d\uc774 \uc0dd\uc131\ub418\uc9c0 \uc54a\uc558\uace0 <{0}> \ubcc4\uce6d\uc774 \uc774\ubbf8 \uc788\uc2b5\ub2c8\ub2e4." },
			{
					"Cannot derive signature algorithm",
					"\uc11c\uba85 \uc54c\uace0\ub9ac\uc998\uc744 \uc720\ub3c4\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4." },
			{
					"Generating keysize bit keyAlgName key pair and self-signed certificate (sigAlgName) with a validity of validality days\n\tfor: x500Name",
					"\ub2e4\uc74c\uc5d0 \ub300\ud574 \uc720\ud6a8 \uae30\uac04\uc774 {3}\uc77c\uc778 {0}\ube44\ud2b8 {1} \ud0a4 \uc30d \ubc0f \uc790\uccb4 \uc11c\uba85\ub41c \uc778\uc99d\uc11c({2}) \uc0dd\uc131 \uc911\n\t: {4}" },
			{
					"Enter key password for <alias>",
					"<{0}>\uc5d0 \ub300\ud55c \ud0a4 \uc554\ud638\ub97c \uc785\ub825\ud558\uc2ed\uc2dc\uc624." },
			{
					"\t(RETURN if same as keystore password):  ",
					"\t(keystore \uc554\ud638\uc640 \uac19\uc740 \uacbd\uc6b0 Enter\ub97c \ub204\ub974\uc2ed\uc2dc\uc624):  " },
			{
					"Key password is too short - must be at least 6 characters",
					"\ud0a4 \uc554\ud638\uac00 \ub108\ubb34 \uc9e7\uc2b5\ub2c8\ub2e4. \uc5ec\uc12f \uae00\uc790 \uc774\uc0c1\uc774\uc5b4\uc57c \ud569\ub2c8\ub2e4." },
			{
					"Too many failures - key not added to keystore",
					"\uc624\ub958\uac00 \ub108\ubb34 \ub9ce\uc2b5\ub2c8\ub2e4. keystore\uc5d0 \ud0a4\uac00 \ucd94\uac00\ub418\uc9c0 \uc54a\uc558\uc2b5\ub2c8\ub2e4." },
			{
					"Destination alias <dest> already exists",
					"\ub300\uc0c1 \ubcc4\uce6d <{0}>\uc774(\uac00) \uc774\ubbf8 \uc788\uc2b5\ub2c8\ub2e4." },
			{
					"Password is too short - must be at least 6 characters",
					"\uc554\ud638\uac00 \ub108\ubb34 \uc9e7\uc2b5\ub2c8\ub2e4. \uc5ec\uc12f \uae00\uc790 \uc774\uc0c1\uc774\uc5b4\uc57c \ud569\ub2c8\ub2e4." },
			{
					"Too many failures. Key entry not cloned",
					"\uc624\ub958\uac00 \ub108\ubb34 \ub9ce\uc2b5\ub2c8\ub2e4. \ud0a4 \ud56d\ubaa9\uc774 \ubcf5\uc81c\ub418\uc9c0 \uc54a\uc558\uc2b5\ub2c8\ub2e4." },
			{ "key password for <alias>",
					"<{0}>\uc5d0 \ub300\ud55c \ud0a4 \uc554\ud638" },
			{
					"Keystore entry for <id.getName()> already exists",
					"<{0}>\uc5d0 \ub300\ud55c keystore \ud56d\ubaa9\uc774 \uc774\ubbf8 \uc788\uc2b5\ub2c8\ub2e4." },
			{
					"Creating keystore entry for <id.getName()> ...",
					"<{0}>\uc5d0 \ub300\ud55c keystore \ud56d\ubaa9\uc744 \uc791\uc131\ud558\ub294 \uc911 ..." },
			{
					"No entries from identity database added",
					"\uc2e0\uc6d0 \ub370\uc774\ud130\ubca0\uc774\uc2a4\uc5d0\uc11c \ud56d\ubaa9\uc774 \ucd94\uac00\ub418\uc9c0 \uc54a\uc558\uc2b5\ub2c8\ub2e4." },
			{ "Alias name: alias", "\ubcc4\uce6d \uc774\ub984: {0}" },
			{ "Creation date: keyStore.getCreationDate(alias)",
					"\uc791\uc131\uc77c: keyStore.getCreationDate(alias)" },
			{ "alias, keyStore.getCreationDate(alias), ", "{0}, {1,date}, " },
			{ "alias, ", "{0}," },
			{ "Entry type: <type>", "\ud56d\ubaa9 \uc720\ud615: {0}" },
			{ "Certificate chain length: ",
					"\uc778\uc99d\uc11c \uccb4\uc778 \uae38\uc774: " },
			{ "Certificate[(i + 1)]:",
					"\uc778\uc99d\uc11c[{0,number,integer}]:" },
			{ "Certificate fingerprint (MD5): ",
					"\uc778\uc99d\uc11c \uc9c0\ubb38(MD5): " },
			{ "Entry type: trustedCertEntry\n",
					"\uc785\ub825 \uc720\ud615: trustedCertEntry\n" },
			{ "trustedCertEntry,", "trustedCertEntry," },
			{ "Keystore type: ", "Keystore \uc720\ud615: " },
			{ "Keystore provider: ", "Keystore \uacf5\uae09\uc790: " },
			{
					"Your keystore contains keyStore.size() entry",
					"Keystore\uc5d0\ub294 {0,number,integer} \ud56d\ubaa9\uc774 \ud3ec\ud568\ub418\uc5b4 \uc788\uc2b5\ub2c8\ub2e4." },
			{
					"Your keystore contains keyStore.size() entries",
					"Keystore\uc5d0\ub294 {0,number,integer} \ud56d\ubaa9\uc774 \ud3ec\ud568\ub418\uc5b4 \uc788\uc2b5\ub2c8\ub2e4." },
			{
					"Failed to parse input",
					"\uc785\ub825\uc744 \uad6c\ubb38 \ubd84\uc11d\ud558\uc9c0 \ubabb\ud588\uc2b5\ub2c8\ub2e4." },
			{ "Empty input",
					"\uc785\ub825\uc774 \ube44\uc5b4\uc788\uc2b5\ub2c8\ub2e4." },
			{ "Not X.509 certificate",
					"X.509 \uc778\uc99d\uc11c\uac00 \uc544\ub2d9\ub2c8\ub2e4." },
			{
					"Cannot derive signature algorithm",
					"\uc11c\uba85 \uc54c\uace0\ub9ac\uc998\uc744 \uc720\ub3c4\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4." },
			{ "alias has no public key",
					"{0}\uc5d0\ub294 \uacf5\uac1c \ud0a4\uac00 \uc5c6\uc2b5\ub2c8\ub2e4." },
			{ "alias has no X.509 certificate",
					"{0}\uc5d0 X.509 \uc778\uc99d\uc11c\uac00 \uc5c6\uc2b5\ub2c8\ub2e4." },
			{ "New certificate (self-signed):",
					"\uc0c8 \uc778\uc99d\uc11c(\uc790\uccb4 \uc11c\uba85):" },
			{ "Reply has no certificates",
					"\ud68c\uc2e0\uc5d0 \uc778\uc99d\uc11c\uac00 \uc5c6\uc2b5\ub2c8\ub2e4." },
			{
					"Certificate not imported, alias <alias> already exists",
					"\uc778\uc99d\uc11c\ub97c \uac00\uc838\uc624\uc9c0 \uc54a\uc558\uace0 <{0}> \ubcc4\uce6d\uc774 \uc774\ubbf8 \uc788\uc2b5\ub2c8\ub2e4." },
			{ "Input not an X.509 certificate",
					"\uc785\ub825\uc774 X.509 \uc778\uc99d\uc11c\uac00 \uc544\ub2d9\ub2c8\ub2e4." },
			{
					"Certificate already exists in keystore under alias <trustalias>",
					"\uc778\uc99d\uc11c\uac00 <{0}> \ubcc4\uce6d \uc544\ub798\uc758 keystore\uc5d0 \uc774\ubbf8 \uc788\uc2b5\ub2c8\ub2e4." },
			{ "Do you still want to add it? [no]:  ",
					"\ucd94\uac00\ud558\uc2dc\uaca0\uc2b5\ub2c8\uae4c? [\uc544\ub2c8\uc624]:  " },
			{
					"Certificate already exists in system-wide CA keystore under alias <trustalias>",
					"\uc778\uc99d\uc11c\uac00 <{0}> \ubcc4\uce6d \uc544\ub798\uc758 \uc2dc\uc2a4\ud15c \ubc94\uc704 CA keystore\uc5d0 \uc774\ubbf8 \uc788\uc2b5\ub2c8\ub2e4." },
			{
					"Do you still want to add it to your own keystore? [no]:  ",
					"\uc0ac\uc6a9\uc790 keystore\uc5d0 \ucd94\uac00\ud558\uc2dc\uaca0\uc2b5\ub2c8\uae4c? [\uc544\ub2c8\uc624]:  " },
			{
					"Trust this certificate? [no]:  ",
					"\uc774 \uc778\uc99d\uc11c\ub97c \uc2e0\ub8b0\ud558\uc2ed\ub2c8\uae4c? [\uc544\ub2c8\uc624]:  " },
			{ "YES", "\uc608" },
			{ "New prompt: ", "\uc0c8 \ud504\ub86c\ud504\ud2b8: " },
			{ "Passwords must differ",
					"\uc554\ud638\ub294 \ub2ec\ub77c\uc57c \ud569\ub2c8\ub2e4." },
			{ "Re-enter new prompt: ",
					"\uc0c8 {0}\uc744(\ub97c) \ub2e4\uc2dc \uc785\ub825\ud558\uc2ed\uc2dc\uc624: " },
			{ "Re-enter new password: ",
					"\uc0c8 \uc554\ud638\ub97c \ub2e4\uc2dc \uc785\ub825\ud558\uc2ed\uc2dc\uc624: " },
			{
					"They don't match. Try again",
					"\uc77c\uce58\ud558\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4. \ub2e4\uc2dc \uc2dc\ub3c4\ud558\uc2ed\uc2dc\uc624." },
			{ "Enter prompt alias name:  ",
					"{0} \ubcc4\uce6d \uc774\ub984\uc744 \uc785\ub825\ud558\uc2ed\uc2dc\uc624:  " },
			{
					"Enter new alias name\t(RETURN to cancel import for this entry):  ",
					"\uc0c8 \ubcc4\uce6d \uc785\ub825\t(Enter - \uc774 \ud56d\ubaa9\uc5d0 \ub300\ud55c \uac00\uc838\uc624\uae30 \ucde8\uc18c):  " },
			{ "Enter alias name:  ",
					"\ubcc4\uce6d \uc774\ub984\uc744 \uc785\ub825\ud558\uc2ed\uc2dc\uc624:  " },
			{
					"\t(RETURN if same as for <otherAlias>)",
					"\t(<{0}>\uc640(\uacfc) \uac19\uc740 \uacbd\uc6b0 Enter\ub97c \ub204\ub974\uc2ed\uc2dc\uc624.)" },
			{
					"*PATTERN* printX509Cert",
					"\uc18c\uc720\uc790: {0}\n\ubc1c\uae09\uc790: {1}\n\uc77c\ub828 \ubc88\ud638: {2}\n\uc720\ud6a8 \uae30\uac04 \uc2dc\uc791: {3} \ub05d: {4}\n\uc778\uc99d \uc9c0\ubb38:\n\t MD5:  {5}\n\t SHA1: {6}\n\t \uc11c\uba85 \uc54c\uace0\ub9ac\uc998 \uc774\ub984: {7}\n\t \ubc84\uc804: {8}" },
			{ "What is your first and last name?",
					"\uc774\ub984\uacfc \uc131\uc744 \uc785\ub825\ud558\uc2ed\uc2dc\uc624." },
			{
					"What is the name of your organizational unit?",
					"\uc870\uc9c1 \ub2e8\uc704 \uc774\ub984\uc744 \uc785\ub825\ud558\uc2ed\uc2dc\uc624." },
			{ "What is the name of your organization?",
					"\uc870\uc9c1 \uc774\ub984\uc744 \uc785\ub825\ud558\uc2ed\uc2dc\uc624." },
			{ "What is the name of your City or Locality?",
					"\uad6c/\uad70/\uc2dc \uc774\ub984\uc744 \uc785\ub825\ud558\uc2ed\uc2dc\uc624?" },
			{ "What is the name of your State or Province?",
					"\uc2dc/\ub3c4 \uc774\ub984\uc744 \uc785\ub825\ud558\uc2ed\uc2dc\uc624." },
			{
					"What is the two-letter country code for this unit?",
					"\uc774 \uc870\uc9c1\uc758 \ub450 \uc790\ub9ac \uad6d\uac00 \ucf54\ub4dc\ub97c \uc785\ub825\ud558\uc2ed\uc2dc\uc624." },
			{ "Is <name> correct?",
					"{0}\uc774(\uac00) \ub9de\uc2b5\ub2c8\uae4c?" },
			{ "no", "\uc544\ub2c8\uc624" },
			{ "yes", "\uc608" },
			{ "y", "y" },
			{ "  [defaultValue]:  ", "  [{0}]:  " },
			{ "Alias <alias> has no key",
					"\ubcc4\uce6d <{0}>\uc5d0 \ud0a4 \uc5c6\uc74c" },
			{
					"Alias <alias> references an entry type that is not a private key entry.  The -keyclone command only supports cloning of private key entries",
					"\ubcc4\uce6d <{0}>\uc774(\uac00) \uac1c\uc778 \ud0a4 \ud56d\ubaa9\uc774 \uc544\ub2cc \ud56d\ubaa9 \uc720\ud615\uc744 \ucc38\uc870\ud569\ub2c8\ub2e4.  -keyclone \uba85\ub839\uc740 \uac1c\uc778 \ud0a4 \ud56d\ubaa9\uc758 \ubcf5\uc81c\ub9cc \uc9c0\uc6d0\ud569\ub2c8\ub2e4." },

			{ "*****************  WARNING WARNING WARNING  *****************",
					"**************  \uacbd\uace0 \uacbd\uace0 \uacbd\uace0  **************" },

			// Translators of the following 5 pairs, ATTENTION:
			// the next 5 string pairs are meant to be combined into 2
			// paragraphs,
			// 1+3+4 and 2+3+5. make sure your translation also does.
			{
					"* The integrity of the information stored in your keystore  *",
					"* keystore\uc5d0 \uc800\uc7a5\ub41c \uc815\ubcf4\uc758 \ubb34\uacb0\uc131\uc774 \ud655\uc778\ub418\uc9c0 *" },
			{ "* The integrity of the information stored in the srckeystore*",
					"* srckeystore\uc5d0 \uc800\uc7a5\ub41c \uc815\ubcf4\uc758 \ubb34\uacb0\uc131*" },
			{
					"* has NOT been verified!  In order to verify its integrity, *",
					"* \uc54a\uc558\uc2b5\ub2c8\ub2e4! \ubb34\uacb0\uc131\uc744 \ud655\uc778\ud558\ub824\uba74 keystore   *" },
			{
					"* you must provide your keystore password.                  *",
					"* \uc554\ud638\ub97c \uc81c\uacf5\ud574\uc57c \ud569\ub2c8\ub2e4.                    *" },
			{
					"* you must provide the srckeystore password.                *",
					"* srckeystore \uc554\ud638\ub97c \uc81c\uacf5\ud574\uc57c \ud569\ub2c8\ub2e4.                *" },

			{
					"Certificate reply does not contain public key for <alias>",
					"\uc778\uc99d\uc11c \ud68c\uc2e0\uc5d0 <{0}>\uc5d0 \ub300\ud55c \uacf5\uac1c \ud0a4\uac00 \ub4e4\uc5b4\uc788\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4." },
			{ "Incomplete certificate chain in reply",
					"\ud68c\uc2e0\uc758 \ubd88\uc644\uc804\ud55c \uc778\uc99d\uc11c \uccb4\uc778" },
			{
					"Certificate chain in reply does not verify: ",
					"\ud68c\uc2e0\uc758 \uc778\uc99d\uc11c \uccb4\uc778\uc774 \ud655\uc778\ub418\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4: " },
			{ "Top-level certificate in reply:\n",
					"\ud68c\uc2e0\uc758 \ucd5c\uc0c1\uc704 \uc778\uc99d\uc11c:\n" },
			{ "... is not trusted. ",
					"... \uc778\uc99d\ub418\uc9c0 \uc54a\uc558\uc2b5\ub2c8\ub2e4. " },
			{
					"Install reply anyway? [no]:  ",
					"\ud68c\uc2e0\uc744 \uc124\uce58\ud558\uc2dc\uaca0\uc2b5\ub2c8\uae4c? [\uc544\ub2c8\uc624]:  " },
			{ "NO", "\uc544\ub2c8\uc624" },
			{
					"Public keys in reply and keystore don't match",
					"\ud68c\uc2e0\uacfc keystore\uc758 \uacf5\uac1c \ud0a4\uac00 \uc77c\uce58\ud558\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4." },
			{
					"Certificate reply and certificate in keystore are identical",
					"\ud68c\uc2e0\uc758 \uc778\uc99d\uc11c\uc640 keystore\uc758 \uc778\uc99d\uc11c\uac00 \ub3d9\uc77c\ud558\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4." },
			{
					"Failed to establish chain from reply",
					"\ud68c\uc2e0\uc758 \uccb4\uc778\uc744 \uc124\uc815\ud558\uc9c0 \ubabb\ud588\uc2b5\ub2c8\ub2e4." },
			{ "n", "n" },
			{
					"Wrong answer, try again",
					"\uc798\ubabb\ub41c \uc751\ub2f5\uc785\ub2c8\ub2e4. \ub2e4\uc2dc \uc2dc\ub3c4\ud558\uc2ed\uc2dc\uc624." },
			{
					"Secret key not generated, alias <alias> already exists",
					"\ubcf4\uc548 \ud0a4\uac00 \uc0dd\uc131\ub418\uc9c0 \uc54a\uc558\uc2b5\ub2c8\ub2e4. \ubcc4\uce6d <{0}>\uc774(\uac00) \uc774\ubbf8 \uc788\uc2b5\ub2c8\ub2e4." },
			{
					"Please provide -keysize for secret key generation",
					"\ubcf4\uc548 \ud0a4 \uc0dd\uc131\uc744 \uc704\ud55c -keysize\ub97c \uc81c\uacf5\ud558\uc2ed\uc2dc\uc624." },
			{ "keytool usage:\n", "keytool \uc0ac\uc6a9\ubc95:\n" },

			{ "Extensions: ", "\ud655\uc7a5\uc790: " },

			{ "-certreq     [-v] [-protected]",
					"-certreq     [-v] [-protected]" },
			{
					"\t     [-alias <alias>] [-sigalg <sigalg>]",
					"\t     [-alias <\ubcc4\uce6d>] [-sigalg <\uc11c\uba85 \uc54c\uace0\ub9ac\uc998>]" },
			{ "\t     [-file <csr_file>] [-keypass <keypass>]",
					"\t     [-file <csr \ud30c\uc77c>] [-keypass <\ud0a4 \uc554\ud638>]" },
			{
					"\t     [-keystore <keystore>] [-storepass <storepass>]",
					"\t     [-keystore <\ud0a4 \uc800\uc7a5\uc18c>] [-storepass <\uc800\uc7a5\uc18c \uc554\ud638>]" },
			{
					"\t     [-storetype <storetype>] [-providername <name>]",
					"\t     [-storetype <\uc800\uc7a5\uc18c \uc720\ud615>] [-providername <\uc774\ub984>]" },
			{
					"\t     [-providerclass <provider_class_name> [-providerarg <arg>]] ...",
					"\t     [-providerclass <\uacf5\uae09\uc790 \ud074\ub798\uc2a4 \uc774\ub984> [-providerarg <\uc778\uc218>]] ..." },
			{ "\t     [-providerpath <pathlist>]",
					"\t     [-providerpath <\uacbd\ub85c \ubaa9\ub85d>]" },
			{ "-delete      [-v] [-protected] -alias <alias>",
					"-delete      [-v] [-protected] -alias <\ubcc4\uce6d>" },
			/** rest is same as -certreq starting from -keystore **/

			// {"-export      [-v] [-rfc] [-protected]",
			// "-export      [-v] [-rfc] [-protected]"},
			{ "-exportcert  [-v] [-rfc] [-protected]",
					"-exportcert  [-v] [-rfc] [-protected]" },
			{ "\t     [-alias <alias>] [-file <cert_file>]",
					"\t     [-alias <\ubcc4\uce6d>] [-file <\uc778\uc99d\uc11c \ud30c\uc77c>]" },
			/** rest is same as -certreq starting from -keystore **/

			// {"-genkey      [-v] [-protected]",
			// "-genkey      [-v] [-protected]"},
			{ "-genkeypair  [-v] [-protected]",
					"-genkeypair  [-v] [-protected]" },
			{ "\t     [-alias <alias>]", "\t     [-alias <\ubcc4\uce6d>]" },
			{
					"\t     [-keyalg <keyalg>] [-keysize <keysize>]",
					"\t     [-keyalg <\ud0a4 \uc54c\uace0\ub9ac\uc998>] [-keysize <\ud0a4 \ud06c\uae30>]" },
			{
					"\t     [-sigalg <sigalg>] [-dname <dname>]",
					"\t     [-sigalg <\uc11c\uba85 \uc54c\uace0\ub9ac\uc998>] [-dname <\ub300\uc0c1 \uc774\ub984>]" },
			{ "\t     [-validity <valDays>] [-keypass <keypass>]",
					"\t     [-validity <\uc720\ud6a8\uc77c>] [-keypass <\ud0a4 \uc554\ud638>]" },
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
			{ "\t     [-alias <alias>]", "\t     [-alias <\ubcc4\uce6d>]" },
			{ "\t     [-alias <alias>] [-keypass <keypass>]",
					"\t     [-alias <\ubcc4\uce6d>] [-keypass <\ud0a4 \uc554\ud638>]" },
			{
					"\t     [-file <cert_file>] [-keypass <keypass>]",
					"\t     [-file <\uc778\uc99d\uc11c \ud30c\uc77c>] [-keypass <\ud0a4 \uc554\ud638>]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-importkeystore [-v] ", "-importkeystore [-v] " },
			{
					"\t     [-srckeystore <srckeystore>] [-destkeystore <deststoretype>]",
					"\t     [-srckeystore <\uc18c\uc2a4 \ud0a4 \uc800\uc7a5\uc18c>] [-destkeystore <\ub300\uc0c1 \ud0a4 \uc800\uc7a5\uc18c>]" },
			{
					"\t     [-srcstoretype <srcstoretype>] [-deststoretype <deststoretype>]",
					"\t     [-srcstoretype <\uc18c\uc2a4 \uc800\uc7a5\uc18c \uc720\ud615>] [-deststoretype <\ub300\uc0c1 \uc800\uc7a5\uc18c \uc720\ud615>]" },
			{ "\t     [-srcprotected] [-destprotected]",
					"\t     [-srcprotected] [-destprotected]" },
			{
					"\t     [-srcstorepass <srcstorepass>] [-deststorepass <deststorepass>]",
					"\t     [-srcstorepass <\uc18c\uc2a4 \uc800\uc7a5\uc18c \uc554\ud638>] [-deststorepass <\ub300\uc0c1 \uc800\uc7a5\uc18c \uc554\ud638>]" },
			{
					"\t     [-srcprovidername <\uc18c\uc2a4 \uacf5\uae09\uc790 \uc774\ub984>]\n\t     [-destprovidername <\ub300\uc0c1 \uacf5\uae09\uc790 \uc774\ub984>]", // \ud589\uc774
																																											// \ub108\ubb34
																																											// \uae41\ub2c8\ub2e4.
																																											// 2\ud589\uc73c\ub85c
																																											// \ubd84\ud560\ud558\uc2ed\uc2dc\uc624.
					"\t     [-srcprovidername <\uc18c\uc2a4 \uacf5\uae09\uc790 \uc774\ub984>]\n\t     [-destprovidername <\ub300\uc0c1 \uacf5\uae09\uc790 \uc774\ub984>]" },
			{
					"\t     [-srcalias <srcalias> [-destalias <destalias>]",
					"\t     [-srcalias <\uc18c\uc2a4 \ubcc4\uce6d> [-destalias <\ub300\uc0c1 \ubcc4\uce6d>]" },
			{
					"\t       [-srckeypass <srckeypass>] [-destkeypass <destkeypass>]]",
					"\t       [-srckeypass <\uc18c\uc2a4 \ud0a4 \uc554\ud638>] [-destkeypass <\ub300\uc0c1 \ud0a4 \uc554\ud638>]]" },
			{ "\t     [-noprompt]", "\t     [-noprompt]" },
			/** rest is same as -certreq starting from -keystore **/

			{
					"-changealias [-v] [-protected] -alias <alias> -destalias <destalias>",
					"-changealias [-v] [-protected] -alias <\ubcc4\uce6d> -destalias <\ub300\uc0c1 \ubcc4\uce6d>" },
			{ "\t     [-keypass <keypass>]",
					"\t     [-keypass <\ud0a4 \uc554\ud638>]" },

			// {"-keyclone    [-v] [-protected]",
			// "-keyclone    [-v] [-protected]"},
			// {"\t     [-alias <alias>] -dest <destalias>",
			// "\t     [-alias <\ubcc4\uce6d>] -dest <\ub300\uc0c1 \ubcc4\uce6d>"},
			// {"\t     [-keypass <keypass>] [-new <new_keypass>]",
			// "\t     [-keypass <\ud0a4 \uc554\ud638>] [-new <\uc0c8 \ud0a4 \uc554\ud638>]"},
			/** rest is same as -certreq starting from -keystore **/

			{ "-keypasswd   [-v] [-alias <alias>]",
					"-keypasswd   [-v] [-alias <\ubcc4\uce6d>]" },
			{
					"\t     [-keypass <old_keypass>] [-new <new_keypass>]",
					"\t     [-keypass <\uae30\uc874 \ud0a4 \uc554\ud638>] [-new <\uc0c8 \ud0a4 \uc554\ud638>]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-list        [-v | -rfc] [-protected]",
					"-list        [-v | -rfc] [-protected]" },
			{ "\t     [-alias <alias>]", "\t     [-alias <\ubcc4\uce6d>]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-printcert   [-v] [-file <cert_file>]",
					"-printcert   [-v] [-file <\uc778\uc99d\uc11c \ud30c\uc77c>]" },

			// {"-selfcert    [-v] [-protected]",
			// "-selfcert    [-v] [-protected]"},
			{ "\t     [-alias <alias>]", "\t     [-alias <\ubcc4\uce6d>]" },
			// {"\t     [-dname <dname>] [-validity <valDays>]",
			// "\t     [-dname <\ub300\uc0c1 \uc774\ub984>] [-validity <\uc720\ud6a8\uc77c>]"},
			// {"\t     [-keypass <keypass>] [-sigalg <sigalg>]",
			// "\t     [-keypass <\ud0a4 \uc554\ud638>] [-sigalg <\uc11c\uba85 \uc54c\uace0\ub9ac\uc998>]"},
			/** rest is same as -certreq starting from -keystore **/

			{ "-storepasswd [-v] [-new <new_storepass>]",
					"-storepasswd [-v] [-new <\uc0c8 \uc800\uc7a5\uc18c \uc554\ud638>]" },
			/** rest is same as -certreq starting from -keystore **/

			// policytool
			{
					"Warning: A public key for alias 'signers[i]' does not exist.  Make sure a KeyStore is properly configured.",
					"\uacbd\uace0:\t \ubcc4\uce6d {0}\uc5d0 \ub300\ud55c \uacf5\uac1c \ud0a4\uac00 \uc5c6\uc2b5\ub2c8\ub2e4.  \ud0a4 \uc800\uc7a5\uc18c\uac00 \uc81c\ub300\ub85c \uad6c\uc131\ub418\uc5b4 \uc788\ub294\uc9c0 \ud655\uc778\ud558\uc2ed\uc2dc\uc624." },
			{ "Warning: Class not found: class",
					"\uacbd\uace0: \ud074\ub798\uc2a4\ub97c \ucc3e\uc744 \uc218 \uc5c6\uc74c: {0}" },
			{ "Warning: Invalid argument(s) for constructor: {0}",
					"\uacbd\uace0:\t \uc798\ubabb\ub41c \uad6c\uc131\uc790 \uc778\uc218: {0}" },
			{ "Illegal Principal Type: type",
					"\uc798\ubabb\ub41c \uae30\ubcf8 \uc720\ud615: {0}" },
			{ "Illegal option: option", "\uc798\ubabb\ub41c \uc635\uc158: {0}" },
			{ "Usage: policytool [options]",
					"\uc0ac\uc6a9\ubc95: policytool [\uc635\uc158]" },
			{ "  [-file <file>]    policy file location",
					"  [-file <\ud30c\uc77c>]    \uc815\ucc45 \ud30c\uc77c \uc704\uce58" },
			{ "New", "\uc0c8\ub85c \ub9cc\ub4e4\uae30" },
			{ "Open", "\uc5f4\uae30" },
			{ "Save", "\uc800\uc7a5" },
			{ "Save As", "\ub2e4\ub978 \uc774\ub984\uc73c\ub85c \uc800\uc7a5" },
			{ "View Warning Log", "\uacbd\uace0 \ub85c\uadf8 \ubcf4\uae30" },
			{ "Exit", "\uc885\ub8cc" },
			{ "Add Policy Entry", "\uc815\ucc45 \ud56d\ubaa9 \ucd94\uac00" },
			{ "Edit Policy Entry", "\uc815\ucc45 \ud56d\ubaa9 \ud3b8\uc9d1" },
			{ "Remove Policy Entry", "\uc815\ucc45 \ud56d\ubaa9 \uc81c\uac70" },
			{ "Edit", "\ud3b8\uc9d1" },
			{ "Retain", "\uc720\uc9c0" },

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
					"\uacf5\uac1c \ud0a4 \ubcc4\uce6d \ucd94\uac00" },
			{ "Remove Public Key Alias",
					"\uacf5\uac1c \ud0a4 \ubcc4\uce6d \uc81c\uac70" },
			{ "File", "\ud30c\uc77c" },
			{ "KeyStore", "\ud0a4 \uc800\uc7a5\uc18c" },
			{ "Policy File:", "\uc815\ucc45 \ud30c\uc77c:" },
			{
					"Could not open policy file: policyFile: e.toString()",
					"\uc815\ucc45 \ud30c\uc77c\uc744 \uc5f4\uc9c0 \ubabb\ud588\uc2b5\ub2c8\ub2e4. {0}: {1}" },
			{ "Policy Tool", "\uc815\ucc45 \ub3c4\uad6c" },
			{
					"Errors have occurred while opening the policy configuration.  View the Warning Log for more information.",
					"\uc815\ucc45 \uad6c\uc131\uc744 \uc5ec\ub294 \ub3d9\uc548 \uc624\ub958\uac00 \ubc1c\uc0dd\ud588\uc2b5\ub2c8\ub2e4. \uc790\uc138\ud55c \ub0b4\uc6a9\uc740 \uacbd\uace0 \ub85c\uadf8\ub97c \ubcf4\uc2ed\uc2dc\uc624." },
			{ "Error", "\uc624\ub958" },
			{ "OK", "\ud655\uc778" },
			{ "Status", "\uc0c1\ud0dc" },
			{ "Warning", "\uacbd\uace0" },
			{
					"Permission:                                                       ",
					"\uc0ac\uc6a9 \uad8c\ud55c:                                                       " },
			{ "Principal Type:", "Principal \uc720\ud615:" },
			{ "Principal Name:", "Principal \uc774\ub984:" },
			{
					"Target Name:                                                    ",
					"\ub300\uc0c1 \uc774\ub984:                                                    " },
			{
					"Actions:                                                             ",
					"\uc791\uc5c5:                                                             " },
			{
					"OK to overwrite existing file filename?",
					"\uae30\uc874 \ud30c\uc77c {0}\uc744(\ub97c) \uacb9\uccd0\uc4f0\uc2dc\uaca0\uc2b5\ub2c8\uae4c?" },
			{ "Cancel", "\ucde8\uc18c" },
			{ "CodeBase:", "CodeBase:" },
			{ "SignedBy:", "SignedBy:" },
			{ "Add Principal", "Principal \ucd94\uac00" },
			{ "Edit Principal", "Principal \ud3b8\uc9d1" },
			{ "Remove Principal", "Principal \uc81c\uac70" },
			{ "Principals:", "Principals:" },
			{ "  Add Permission", "  \uc0ac\uc6a9 \uad8c\ud55c \ucd94\uac00" },
			{ "  Edit Permission", "  \uc0ac\uc6a9 \uad8c\ud55c \ud3b8\uc9d1" },
			{ "Remove Permission", "\uc0ac\uc6a9 \uad8c\ud55c \uc81c\uac70" },
			{ "Done", "\uc644\ub8cc" },
			{ "KeyStore URL:", "\ud0a4 \uc800\uc7a5\uc18c URL:" },
			{ "KeyStore Type:", "\ud0a4 \uc800\uc7a5\uc18c \uc720\ud615:" },
			{ "KeyStore Provider:",
					"\ud0a4 \uc800\uc7a5\uc18c \uacf5\uae09\uc790:" },
			{ "KeyStore Password URL:",
					"\ud0a4 \uc800\uc7a5\uc18c \ube44\ubc00\ubc88\ud638 URL:" },
			{ "Principals", "Principals" },
			{ "  Edit Principal:", "  Principal \ud3b8\uc9d1:" },
			{ "  Add New Principal:", "  \uc0c8 Principal \ucd94\uac00:" },
			{ "Permissions", "\uc0ac\uc6a9 \uad8c\ud55c" },
			{ "  Edit Permission:", "  \uc0ac\uc6a9 \uad8c\ud55c \ud3b8\uc9d1:" },
			{ "  Add New Permission:",
					"  \uc0c8 \uc0ac\uc6a9 \uad8c\ud55c \ucd94\uac00:" },
			{ "Signed By:", "\uc11c\uba85\uc790:" },
			{
					"Cannot Specify Principal with a Wildcard Class without a Wildcard Name",
					"\uc640\uc77c\ub4dc\uce74\ub4dc \uc774\ub984 \uc5c6\uc774 \uc640\uc77c\ub4dc\uce74\ub4dc \ud074\ub798\uc2a4\ub97c \uac00\uc9c4 Principal\uc744 \uc9c0\uc815\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4." },
			{
					"Cannot Specify Principal without a Name",
					"\uc774\ub984 \uc5c6\uc774 Principal\uc744 \uc9c0\uc815\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4." },
			{
					"Permission and Target Name must have a value",
					"\uc0ac\uc6a9 \uad8c\ud55c\uacfc \ub300\uc0c1 \uc774\ub984\uc740 \uac12\uc744 \uac00\uc838\uc57c \ud569\ub2c8\ub2e4." },
			{
					"Remove this Policy Entry?",
					"\uc774 \uc815\ucc45 \ud56d\ubaa9\uc744 \uc81c\uac70\ud558\uc2dc\uaca0\uc2b5\ub2c8\uae4c?" },
			{ "Overwrite File", "\ud30c\uc77c \uacb9\uccd0\uc4f0\uae30" },
			{
					"Policy successfully written to filename",
					"\uc815\ucc45\uc744 \ud30c\uc77c \uc774\ub984\uc5d0 \uc131\uacf5\uc801\uc73c\ub85c \uae30\ub85d\ud588\uc2b5\ub2c8\ub2e4." },
			{ "null filename", "\ud30c\uc77c \uc774\ub984\uc774 \uc5c6\uc74c" },
			{
					"Save changes?",
					"\ubcc0\uacbd \uc0ac\ud56d\uc744 \uc800\uc7a5\ud558\uc2dc\uaca0\uc2b5\ub2c8\uae4c?" },
			{ "Yes", "\uc608" },
			{ "No", "\uc544\ub2c8\uc624" },
			{ "Policy Entry", "\uc815\ucc45 \ud56d\ubaa9" },
			{ "Save Changes", "\ubcc0\uacbd \uc0ac\ud56d \uc800\uc7a5" },
			{
					"No Policy Entry selected",
					"\uc815\ucc45 \ud56d\ubaa9\uc774 \uc120\ud0dd\ub418\uc9c0 \uc54a\uc558\uc2b5\ub2c8\ub2e4." },
			{ "Unable to open KeyStore: ex.toString()",
					"\ud0a4 \uc800\uc7a5\uc18c\ub97c \uc5f4 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4. {0}" },
			{ "No principal selected",
					"Principal\uc744 \uc120\ud0dd\ud558\uc9c0 \uc54a\uc558\uc2b5\ub2c8\ub2e4." },
			{
					"No permission selected",
					"\uc0ac\uc6a9 \uad8c\ud55c\uc744 \uc120\ud0dd\ud558\uc9c0 \uc54a\uc558\uc2b5\ub2c8\ub2e4." },
			{ "name", "\uc774\ub984" },
			{ "configuration type", "\uad6c\uc131 \uc720\ud615" },
			{ "environment variable name",
					"\ud658\uacbd \ubcc0\uc218 \uc774\ub984" },
			{ "library name", "\ub77c\uc774\ube0c\ub7ec\ub9ac \uc774\ub984" },
			{ "package name", "\ud328\ud0a4\uc9c0 \uc774\ub984" },
			{ "policy type", "\uc815\ucc45 \uc720\ud615" },
			{ "property name", "\ud2b9\uc131 \uc774\ub984" },
			{ "provider name", "\uacf5\uae09\uc790 \uc774\ub984" },
			{ "Principal List", "\uae30\ubcf8 \ubaa9\ub85d" },
			{ "Permission List", "\uad8c\ud55c \ubaa9\ub85d" },
			{ "Code Base", "\ucf54\ub4dc \ubca0\uc774\uc2a4" },
			{ "KeyStore U R L:", "\ud0a4 \uc800\uc7a5\uc18c U R L:" },
			{ "KeyStore Password U R L:",
					"\ud0a4 \uc800\uc7a5\uc18c \ube44\ubc00\ubc88\ud638 U R L:" },

			// javax.security.auth.PrivateCredentialPermission
			{ "invalid null input(s)", "\uc798\ubabb\ub41c null \uc785\ub825" },
			{ "actions can only be 'read'",
					"\uc791\uc5c5\uc740 '\uc77d\uae30' \uc804\uc6a9\uc785\ub2c8\ub2e4." },
			{
					"permission name [name] syntax invalid: ",
					"\uc0ac\uc6a9 \uad8c\ud55c \uc774\ub984 [{0}] \uad6c\ubb38\uc774 \uc798\ubabb\ub418\uc5c8\uc2b5\ub2c8\ub2e4: " },
			{
					"Credential Class not followed by a Principal Class and Name",
					"Principal \ud074\ub798\uc2a4 \ubc0f \uc774\ub984 \ub2e4\uc74c\uc5d0 \uc778\uc99d\uc11c \ud074\ub798\uc2a4\uac00 \uc5c6\uc2b5\ub2c8\ub2e4." },
			{
					"Principal Class not followed by a Principal Name",
					"Principal \uc774\ub984 \ub2e4\uc74c\uc5d0 Principal \ud074\ub798\uc2a4\uac00 \uc5c6\uc2b5\ub2c8\ub2e4." },
			{
					"Principal Name must be surrounded by quotes",
					"Principal \uc774\ub984\uc740 \uc778\uc6a9 \ubd80\ud638\ub85c \ubb36\uc5b4\uc57c \ud569\ub2c8\ub2e4." },
			{
					"Principal Name missing end quote",
					"Principal \uc774\ub984\uc5d0 \ub2eb\ub294 \uc778\uc6a9 \ubd80\ud638\uac00 \uc5c6\uc2b5\ub2c8\ub2e4." },
			{
					"PrivateCredentialPermission Principal Class can not be a wildcard (*) value if Principal Name is not a wildcard (*) value",
					"PrivateCredentialPermission Principal \ud074\ub798\uc2a4\ub294 Principal \uc774\ub984\uc774 \uc640\uc77c\ub4dc\uce74\ub4dc(*) \uac12\uc774 \uc544\ub2cc \uacbd\uc6b0 \uc640\uc77c\ub4dc\uce74\ub4dc(*) \uac12\uc774 \ub420 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4." },
			{
					"CredOwner:\n\tPrincipal Class = class\n\tPrincipal Name = name",
					"CredOwner:\n\tPrincipal \ud074\ub798\uc2a4 = \ud074\ub798\uc2a4\n\tPrincipal \uc774\ub984 = \uc774\ub984" },

			// javax.security.auth.x500
			{ "provided null name",
					"null \uc774\ub984\uc744 \uc81c\uacf5\ud588\uc2b5\ub2c8\ub2e4." },
			{ "provided null keyword map",
					"null \ud0a4\uc6cc\ub4dc \ub9f5\uc744 \uc81c\uacf5\ud588\uc2b5\ub2c8\ub2e4." },
			{ "provided null OID map",
					"null OID \ub9f5\uc744 \uc81c\uacf5\ud588\uc2b5\ub2c8\ub2e4." },

			// javax.security.auth.Subject
			{
					"invalid null AccessControlContext provided",
					"\uc798\ubabb\ub41c null AccessControlContext\ub97c \uc81c\uacf5\ud588\uc2b5\ub2c8\ub2e4." },
			{
					"invalid null action provided",
					"\uc798\ubabb\ub41c null \uc791\uc5c5\uc744 \uc81c\uacf5\ud588\uc2b5\ub2c8\ub2e4." },
			{
					"invalid null Class provided",
					"\uc798\ubabb\ub41c null \ud074\ub798\uc2a4\ub97c \uc81c\uacf5\ud588\uc2b5\ub2c8\ub2e4." },
			{ "Subject:\n", "\uc81c\ubaa9:\n" },
			{ "\tPrincipal: ", "\tPrincipal: " },
			{ "\tPublic Credential: ", "\t\uacf5\uac1c \uc778\uc99d\uc11c: " },
			{
					"\tPrivate Credentials inaccessible\n",
					"\t\uac1c\uc778 \uc778\uc99d\uc11c\uc5d0 \uc561\uc138\uc2a4\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.\n" },
			{ "\tPrivate Credential: ", "\t\uac1c\uc778 \uc778\uc99d\uc11c: " },
			{
					"\tPrivate Credential inaccessible\n",
					"\t\uac1c\uc778 \uc778\uc99d\uc11c\uc5d0 \uc561\uc138\uc2a4\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.\n" },
			{ "Subject is read-only",
					"\uc81c\ubaa9\uc774 \uc77d\uae30 \uc804\uc6a9\uc785\ub2c8\ub2e4." },
			{
					"attempting to add an object which is not an instance of java.security.Principal to a Subject's Principal Set",
					"java.security.Principal\uc758 \uc778\uc2a4\ud134\uc2a4\uac00 \uc544\ub2cc \uac1d\uccb4\ub97c \uc81c\ubaa9\uc758 Principal \uc138\ud2b8\uc5d0 \ucd94\uac00\ud558\ub824\uace0 \uc2dc\ub3c4\ud558\ub294 \uc911" },
			{
					"attempting to add an object which is not an instance of class",
					"\ud074\ub798\uc2a4\uc758 \uc778\uc2a4\ud134\uc2a4\uac00 \uc544\ub2cc \uac1d\uccb4\ub97c \ucd94\uac00\ud558\ub824\uace0 \uc2dc\ub3c4\ud558\ub294 \uc911" },

			// javax.security.auth.login.AppConfigurationEntry
			{ "LoginModuleControlFlag: ", "LoginModuleControlFlag: " },

			// javax.security.auth.login.LoginContext
			{ "Invalid null input: name",
					"\uc798\ubabb\ub41c null \uc785\ub825: \uc774\ub984" },
			{
					"No LoginModules configured for name",
					"{0}\uc5d0 \ub300\ud574 LoginModules\uac00 \uad6c\uc131\ub418\uc9c0 \uc54a\uc558\uc74c" },
			{
					"invalid null Subject provided",
					"\uc798\ubabb\ub41c null \uc81c\ubaa9\uc744 \uc81c\uacf5\ud588\uc2b5\ub2c8\ub2e4." },
			{
					"invalid null CallbackHandler provided",
					"\uc798\ubabb\ub41c null CallbackHandler\ub97c \uc81c\uacf5\ud588\uc2b5\ub2c8\ub2e4." },
			{
					"null subject - logout called before login",
					"null \uc81c\ubaa9 - \ub85c\uadf8\uc778 \uc804\uc5d0 \ub85c\uadf8\uc544\uc6c3\uc744 \ud638\ucd9c\ud588\uc2b5\ub2c8\ub2e4." },
			{
					"unable to instantiate LoginModule, module, because it does not provide a no-argument constructor",
					"\uc778\uc218\uac00 \uc5c6\ub294 \uad6c\uc131\uc790\ub97c \uc81c\uacf5\ud558\uc9c0 \uc54a\uae30 \ub54c\ubb38\uc5d0 LoginModule, {0}\uc744(\ub97c) \uc778\uc2a4\ud134\uc2a4\ud654\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4." },
			{
					"unable to instantiate LoginModule",
					"LoginModule\uc744 \uc778\uc2a4\ud134\uc2a4\ud654\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4." },
			{
					"unable to instantiate LoginModule: ",
					"LoginModule\uc744 \uc778\uc2a4\ud134\uc2a4\ud654\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4. " },
			{
					"unable to find LoginModule class: ",
					"LoginModule \ud074\ub798\uc2a4\ub97c \ucc3e\uc744 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4: " },
			{ "unable to access LoginModule: ",
					"LoginModule\uc5d0 \uc561\uc138\uc2a4\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4: " },
			{
					"Login Failure: all modules ignored",
					"\ub85c\uadf8\uc778 \uc2e4\ud328: \ubaa8\ub4e0 \ubaa8\ub4c8\uc774 \ubb34\uc2dc\ub418\uc5c8\uc2b5\ub2c8\ub2e4." },

			// sun.security.provider.PolicyFile

			{
					"java.security.policy: error parsing policy:\n\tmessage",
					"java.security.policy: {0}\uc744(\ub97c) \uad6c\ubb38 \ubd84\uc11d\ud558\ub294 \uc911 \uc624\ub958 \ubc1c\uc0dd:\n\t{1}" },
			{
					"java.security.policy: error adding Permission, perm:\n\tmessage",
					"java.security.policy: {0} \uc0ac\uc6a9 \uad8c\ud55c\uc744 \ucd94\uac00\ud558\ub294 \uc911 \uc624\ub958 \ubc1c\uc0dd:\n\t{1}" },
			{
					"java.security.policy: error adding Entry:\n\tmessage",
					"java.security.policy: \ud56d\ubaa9\uc744 \ucd94\uac00\ud558\ub294 \uc911 \uc624\ub958 \ubc1c\uc0dd:\n\t{0}" },
			{ "alias name not provided (pe.name)",
					"\ubcc4\uce6d\uc774 \uc81c\uacf5\ub418\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4({0})." },
			{
					"unable to perform substitution on alias, suffix",
					"\ubcc4\uce6d{0}\uc5d0\uc11c \ub300\uccb4 \uc218\ud589\uc774 \ubd88\uac00\ub2a5\ud569\ub2c8\ub2e4." },
			{
					"substitution value, prefix, unsupported",
					"\ub300\uccb4 \uac12 {0}\uc774(\uac00) \uc9c0\uc6d0\ub418\uc9c0 \uc54a\uc2b5\ub2c8\ub2e4." },
			{ "(", "(" },
			{ ")", ")" },
			{ "type can't be null",
					"\uc720\ud615\uc740 null\uc77c \uc218 \uc5c6\uc2b5\ub2c8\ub2e4." },

			// sun.security.provider.PolicyParser
			{
					"keystorePasswordURL can not be specified without also specifying keystore",
					"Keystore \uc9c0\uc815 \uc5c6\uc774 keystorePasswordURL\uc744 \uc9c0\uc815\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4." },
			{ "expected keystore type",
					"keystore \uc720\ud615\uc774 \uc608\uc0c1\ub429\ub2c8\ub2e4." },
			{ "expected keystore provider",
					"keystore \uacf5\uae09\uc790\uac00 \uc608\uc0c1\ub429\ub2c8\ub2e4." },
			{ "multiple Codebase expressions",
					"\ubcf5\uc218 Codebase \ud45c\ud604\uc2dd" },
			{ "multiple SignedBy expressions",
					"\ubcf5\uc218 SignedBy \ud45c\ud604\uc2dd" },
			{
					"SignedBy has empty alias",
					"SignedBy\uc5d0 \ube44\uc5b4 \uc788\ub294 \ubcc4\uce6d\uc774 \uc788\uc2b5\ub2c8\ub2e4." },
			{
					"can not specify Principal with a wildcard class without a wildcard name",
					"\uc640\uc77c\ub4dc\uce74\ub4dc \uc774\ub984 \uc5c6\uc774 \uc640\uc77c\ub4dc\uce74\ub4dc \ud074\ub798\uc2a4\ub97c \uac00\uc9c4 Principal\uc744 \uc9c0\uc815\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4." },
			{
					"expected codeBase or SignedBy or Principal",
					"codeBase, SignedBy \ub610\ub294 Principal\uc774 \uc608\uc0c1\ub429\ub2c8\ub2e4." },
			{ "expected permission entry",
					"\uc0ac\uc6a9 \uad8c\ud55c \ud56d\ubaa9\uc774 \uc608\uc0c1\ub429\ub2c8\ub2e4." },
			{ "number ", "\ubc88\ud638 " },
			{
					"expected [expect], read [end of file]",
					"[{0}]\uc774 \uc608\uc0c1\ub429\ub2c8\ub2e4. [EOF]\ub97c \uc77d\uc5c8\uc2b5\ub2c8\ub2e4." },
			{
					"expected [;], read [end of file]",
					"[;]\uc774 \uc608\uc0c1\ub429\ub2c8\ub2e4. [EOF]\ub97c \uc77d\uc5c8\uc2b5\ub2c8\ub2e4." },
			{ "line number: msg", "\ud589 {0}: {1}" },
			{
					"line number: expected [expect], found [actual]",
					"\ud589 {0}: [{1}]\uc744(\ub97c) \uc608\uc0c1\ud588\ub294\ub370, [{2}]\uc774(\uac00) \ubc1c\uacac\ub418\uc5c8\uc2b5\ub2c8\ub2e4." },
			{ "null principalClass or principalName",
					"principalClass \ub610\ub294 principalName\uc774 \uc5c6\uc2b5\ub2c8\ub2e4." },

			// sun.security.pkcs11.SunPKCS11
			{ "PKCS11 Token [providerName] Password: ",
					"PKCS11 \ud1a0\ud070 [{0}] \uc554\ud638: " },

			/* --- DEPRECATED --- */
			// javax.security.auth.Policy
			{
					"unable to instantiate Subject-based policy",
					"\uc8fc\uc81c \uae30\ubc18 \uc815\ucc45\uc744 \uc778\uc2a4\ud134\uc2a4\ud654\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4." } };

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
