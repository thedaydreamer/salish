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
public class Resources_zh_TW extends java.util.ListResourceBundle {

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
			{ "keytool error: ", "keytool \u932f\u8aa4\uff1a " },
			{ "Illegal option:  ", "\u975e\u6cd5\u9078\u9805\uff1a  " },
			{ "Try keytool -help", "\u5617\u8a66 keytool -help" },
			{ "Command option <flag> needs an argument.",
					"\u6307\u4ee4\u9078\u9805 {0} \u9700\u8981\u5f15\u6578\u3002" },
			{
					"Warning:  Different store and key passwords not supported for PKCS12 KeyStores. Ignoring user-specified <command> value.",
					"\u8b66\u544a\ufe30PKCS12 \u91d1\u9470\u5132\u5b58\u5eab\u4e0d\u652f\u63f4\u4e0d\u540c\u7684\u5132\u5b58\u5eab\u548c\u91d1\u9470\u5bc6\u78bc\u3002\u5ffd\u7565\u4f7f\u7528\u8005\u6307\u5b9a\u7684 {0} \u503c\u3002" },
			{
					"-keystore must be NONE if -storetype is {0}",
					"\u5982\u679c -storetype \u70ba {0}\uff0c\u5247 -keystore \u5fc5\u9808\u70ba NONE" },
			{ "Too may retries, program terminated",
					"\u91cd\u8a66\u6b21\u6578\u592a\u591a\uff0c\u7a0b\u5f0f\u5df2\u7d42\u6b62" },
			{
					"-storepasswd and -keypasswd commands not supported if -storetype is {0}",
					"\u5982\u679c -storetype \u70ba {0}\uff0c\u5247\u4e0d\u652f\u63f4 -storepasswd \u548c -keypasswd \u6307\u4ee4" },
			{
					"-keypasswd commands not supported if -storetype is PKCS12",
					"\u5982\u679c -storetype \u70ba PKCS12\uff0c\u5247 -keypasswd \u6307\u4ee4\u4e0d\u53d7\u652f\u63f4" },
			{
					"-keypass and -new can not be specified if -storetype is {0}",
					"\u5982\u679c -storetype \u70ba {0}\uff0c\u5247\u4e0d\u80fd\u6307\u5b9a -keypass \u548c -new" },
			{
					"if -protected is specified, then -storepass, -keypass, and -new must not be specified",
					"\u5982\u679c\u6307\u5b9a -protected\uff0c\u5247\u4e0d\u80fd\u6307\u5b9a -storepass\u3001-keypass \u548c -new" },
			{
					"if -srcprotected is specified, then -srcstorepass and -srckeypass must not be specified",
					"\u5982\u679c\u6307\u5b9a -srcprotected\uff0c\u5247\u4e0d\u80fd\u6307\u5b9a -srcstorepass \u548c -srckeypass" },
			{
					"if keystore is not password protected, then -storepass, -keypass, and -new must not be specified",
					"\u5982\u679c\u91d1\u9470\u5eab\u4e0d\u53d7\u5bc6\u78bc\u4fdd\u8b77\uff0c\u5247\u4e0d\u80fd\u6307\u5b9a -storepass\u3001-keypass \u548c -new" },
			{
					"if source keystore is not password protected, then -srcstorepass and -srckeypass must not be specified",
					"\u5982\u679c\u4f86\u6e90\u91d1\u9470\u5eab\u4e0d\u53d7\u5bc6\u78bc\u4fdd\u8b77\uff0c\u5247\u4e0d\u80fd\u6307\u5b9a -srcstorepass \u548c -srckeypass" },
			{ "Validity must be greater than zero",
					"\u6709\u6548\u6027\u5fc5\u9808\u6bd4\u96f6\u9084\u5927" },
			{ "provName not a provider",
					"{0} \u4e0d\u662f\u4e00\u500b\u63d0\u4f9b\u8005" },
			{ "Usage error: no command provided",
					"\u7528\u6cd5\u932f\u8aa4\uff1a\u672a\u63d0\u4f9b\u6307\u4ee4" },
			{ "Usage error, <arg> is not a legal command",
					"\u7528\u6cd5\u932f\u8aa4\uff0c{0} \u4e0d\u662f\u5408\u6cd5\u6307\u4ee4" },
			{
					"Source keystore file exists, but is empty: ",
					"\u4f86\u6e90\u91d1\u9470\u5132\u5b58\u5eab\u6a94\u6848\u5b58\u5728\uff0c\u4f46\u70ba\u7a7a\uff1a" },
			{ "Please specify -srckeystore", "\u8acb\u6307\u5b9a -srckeystore" },
			{
					"Must not specify both -v and -rfc with 'list' command",
					"\u4e0d\u5f97\u4ee5\u300c\u6e05\u55ae\u300d\u6307\u4ee4\u6307\u5b9a -v \u53ca -rfc" },
			{ "Key password must be at least 6 characters",
					"\u95dc\u9375\u5bc6\u78bc\u5fc5\u9808\u81f3\u5c11\u70ba 6 \u500b\u5b57\u5143" },
			{ "New password must be at least 6 characters",
					"\u65b0\u7684\u5bc6\u78bc\u5fc5\u9808\u81f3\u5c11\u70ba 6 \u500b\u5b57\u5143" },
			{ "Keystore file exists, but is empty: ",
					"Keystore \u6a94\u6848\u5b58\u5728\uff0c\u4f46\u70ba\u7a7a\u767d\uff1a " },
			{ "Keystore file does not exist: ",
					"Keystore \u6a94\u6848\u4e0d\u5b58\u5728\uff1a " },
			{ "Must specify destination alias",
					"\u5fc5\u9808\u6307\u5b9a\u76ee\u7684\u5730\u5225\u540d" },
			{ "Must specify alias", "\u5fc5\u9808\u6307\u5b9a\u5225\u540d" },
			{ "Keystore password must be at least 6 characters",
					"Keystore \u5bc6\u78bc\u5fc5\u9808\u81f3\u5c11\u70ba 6 \u500b\u5b57\u5143" },
			{ "Enter keystore password:  ",
					"\u8f38\u5165 keystore \u5bc6\u78bc\uff1a  " },
			{
					"Enter source keystore password:  ",
					"\u8acb\u8f38\u5165\u4f86\u6e90\u91d1\u9470\u5132\u5b58\u5eab\u5bc6\u78bc\uff1a" },
			{
					"Enter destination keystore password:  ",
					"\u8acb\u8f38\u5165\u76ee\u6a19\u91d1\u9470\u5132\u5b58\u5eab\u5bc6\u78bc\uff1a" },
			{
					"Keystore password is too short - must be at least 6 characters",
					"Keystore \u5bc6\u78bc\u592a\u77ed - \u5fc5\u9808\u81f3\u5c11\u70ba 6 \u500b\u5b57\u5143" },
			{ "Unknown Entry Type",
					"\u4e0d\u660e\u7684\u9805\u76ee\u985e\u578b" },
			{ "Too many failures. Alias not changed",
					"\u592a\u591a\u932f\u8aa4\u3002\u672a\u8b8a\u66f4\u5225\u540d" },
			{ "Entry for alias <alias> successfully imported.",
					"\u5df2\u6210\u529f\u532f\u5165\u5225\u540d {0} \u7684\u9805\u76ee\u3002" },
			{ "Entry for alias <alias> not imported.",
					"\u672a\u532f\u5165\u5225\u540d {0} \u7684\u9805\u76ee\u3002" },
			{
					"Problem importing entry for alias <alias>: <exception>.\nEntry for alias <alias> not imported.",
					"\u532f\u5165\u5225\u540d {0} \u7684\u9805\u76ee\u6642\u51fa\u73fe\u554f\u984c\uff1a{1}\u3002\n\u672a\u532f\u5165\u5225\u540d {0} \u7684\u9805\u76ee\u3002" },
			{
					"Import command completed:  <ok> entries successfully imported, <fail> entries failed or cancelled",
					"\u5df2\u5b8c\u6210\u532f\u5165\u6307\u4ee4\uff1a\u6210\u529f\u532f\u5165 {0} \u500b\u9805\u76ee\uff0c{1} \u500b\u9805\u76ee\u5931\u6557\u6216\u5df2\u53d6\u6d88" },
			{
					"Warning: Overwriting existing alias <alias> in destination keystore",
					"\u8b66\u544a\uff1a\u6b63\u5728\u8986\u5beb\u76ee\u6a19\u91d1\u9470\u5132\u5b58\u5eab\u4e2d\u7684\u73fe\u6709\u5225\u540d {0}" },
			{
					"Existing entry alias <alias> exists, overwrite? [no]:  ",
					"\u73fe\u6709\u9805\u76ee\u5225\u540d {0} \u5b58\u5728\uff0c\u662f\u5426\u8986\u5beb\uff1f[\u5426]\uff1a  " },
			{ "Too many failures - try later",
					"\u592a\u591a\u932f\u8aa4 - \u8acb\u7a0d\u5f8c\u518d\u8a66" },
			{ "Certification request stored in file <filename>",
					"\u8a8d\u8b49\u8981\u6c42\u5132\u5b58\u5728\u6a94\u6848 <{0}>" },
			{ "Submit this to your CA",
					"\u5c07\u6b64\u63d0\u9001\u81f3\u60a8\u7684 CA" },
			{
					"if alias not specified, destalias, srckeypass, and destkeypass must not be specified",
					"\u5982\u679c\u672a\u6307\u5b9a\u5225\u540d\uff0c\u5247\u4e0d\u80fd\u6307\u5b9a destalias\u3001srckeypass \u53ca destkeypass" },
			{ "Certificate stored in file <filename>",
					"\u8a8d\u8b49\u5132\u5b58\u5728\u6a94\u6848 <{0}>" },
			{ "Certificate reply was installed in keystore",
					"\u8a8d\u8b49\u56de\u8986\u5df2\u5b89\u88dd\u5728 keystore \u4e2d" },
			{ "Certificate reply was not installed in keystore",
					"\u8a8d\u8b49\u56de\u8986\u672a\u5b89\u88dd\u5728 keystore \u4e2d" },
			{ "Certificate was added to keystore",
					"\u8a8d\u8b49\u5df2\u65b0\u589e\u81f3 keystore \u4e2d" },
			{ "Certificate was not added to keystore",
					"\u8a8d\u8b49\u672a\u65b0\u589e\u81f3 keystore \u4e2d" },
			{ "[Storing ksfname]", "[\u5132\u5b58 {0}]" },
			{ "alias has no public key (certificate)",
					"{0} \u6c92\u6709\u516c\u958b\u91d1\u9470\uff08\u8a8d\u8b49\uff09" },
			{ "Cannot derive signature algorithm",
					"\u7121\u6cd5\u53d6\u5f97\u7c3d\u540d\u6f14\u7b97\u6cd5" },
			{ "Alias <alias> does not exist",
					"\u5225\u540d <{0}> \u4e0d\u5b58\u5728" },
			{ "Alias <alias> has no certificate",
					"\u5225\u540d <{0}> \u6c92\u6709\u8a8d\u8b49" },
			{
					"Key pair not generated, alias <alias> already exists",
					"\u6c92\u6709\u5efa\u7acb\u9375\u503c\u5c0d\uff0c\u5225\u540d <{0}> \u5df2\u7d93\u5b58\u5728" },
			{ "Cannot derive signature algorithm",
					"\u7121\u6cd5\u53d6\u5f97\u7c3d\u540d\u6f14\u7b97\u6cd5" },
			{
					"Generating keysize bit keyAlgName key pair and self-signed certificate (sigAlgName) with a validity of validality days\n\tfor: x500Name",
					"\u91dd\u5c0d {4} \u7522\u751f\u6709\u6548\u671f\u70ba {3} \u5929\u7684 {0} \u4f4d\u5143 {1} \u91d1\u9470\u5c0d\u4ee5\u53ca\u81ea\u6211\u7c3d\u7f72\u6191\u8b49 ({2})\n\t" },
			{ "Enter key password for <alias>",
					"\u8f38\u5165 <{0}> \u7684\u4e3b\u5bc6\u78bc" },
			{
					"\t(RETURN if same as keystore password):  ",
					"\t\uff08RETURN \u5982\u679c\u548c keystore \u5bc6\u78bc\u76f8\u540c\uff09\uff1a  " },
			{
					"Key password is too short - must be at least 6 characters",
					"\u4e3b\u5bc6\u78bc\u592a\u77ed - \u5fc5\u9808\u81f3\u5c11\u70ba 6 \u500b\u5b57\u5143" },
			{
					"Too many failures - key not added to keystore",
					"\u592a\u591a\u932f\u8aa4 - \u9375\u503c\u672a\u88ab\u65b0\u589e\u81f3 keystore \u4e2d" },
			{ "Destination alias <dest> already exists",
					"\u76ee\u7684\u5730\u5225\u540d <{0}> \u5df2\u7d93\u5b58\u5728" },
			{
					"Password is too short - must be at least 6 characters",
					"\u5bc6\u78bc\u592a\u77ed - \u5fc5\u9808\u81f3\u5c11\u70ba 6 \u500b\u5b57\u5143" },
			{
					"Too many failures. Key entry not cloned",
					"\u592a\u591a\u932f\u8aa4\u3002 \u9375\u503c\u8f38\u5165\u672a\u88ab\u8907\u88fd" },
			{ "key password for <alias>", "<{0}> \u7684\u4e3b\u5bc6\u78bc" },
			{ "Keystore entry for <id.getName()> already exists",
					"<{0}> \u7684 Keystore \u8f38\u5165\u5df2\u7d93\u5b58\u5728" },
			{ "Creating keystore entry for <id.getName()> ...",
					"\u5efa\u7acb <{0}> \u7684 keystore \u8f38\u5165..." },
			{
					"No entries from identity database added",
					"\u5f9e\u65b0\u589e\u8fa8\u8b58\u8cc7\u6599\u5eab\u4e2d\uff0c\u6c92\u6709\u8f38\u5165" },
			{ "Alias name: alias", "\u5225\u540d\u540d\u7a31\uff1a {0}" },
			{ "Creation date: keyStore.getCreationDate(alias)",
					"\u5efa\u7acb\u65e5\u671f\uff1a {0,date}" },
			{ "alias, keyStore.getCreationDate(alias), ", "{0}, {1,date}, " },
			{ "alias, ", "{0}, " },
			{ "Entry type: <type>", "\u9805\u76ee\u985e\u578b\uff1a{0}" },
			{ "Certificate chain length: ",
					"\u8a8d\u8b49\u93c8\u9577\u5ea6\uff1a " },
			{ "Certificate[(i + 1)]:", "\u8a8d\u8b49 [{0,number,integer}]:" },
			{ "Certificate fingerprint (MD5): ",
					"\u8a8d\u8b49\u6307\u7d0b (MD5)\uff1a " },
			{ "Entry type: trustedCertEntry\n",
					"\u8f38\u5165\u985e\u578b\uff1a trustedCertEntry\n" },
			{ "trustedCertEntry,", "trustedCertEntry," },
			{ "Keystore type: ", "Keystore \u985e\u578b\uff1a " },
			{ "Keystore provider: ", "Keystore \u63d0\u4f9b\u8005\uff1a " },
			{ "Your keystore contains keyStore.size() entry",
					"\u60a8\u7684 keystore \u5305\u542b {0,number,integer} \u8f38\u5165" },
			{ "Your keystore contains keyStore.size() entries",
					"\u60a8\u7684 keystore \u5305\u542b {0,number,integer} \u8f38\u5165" },
			{ "Failed to parse input",
					"\u7121\u6cd5\u8a9e\u6cd5\u5206\u6790\u8f38\u5165" },
			{ "Empty input", "\u7a7a\u8f38\u5165" },
			{ "Not X.509 certificate", "\u975e X.509 \u8a8d\u8b49" },
			{ "Cannot derive signature algorithm",
					"\u7121\u6cd5\u53d6\u5f97\u7c3d\u540d\u6f14\u7b97\u6cd5" },
			{ "alias has no public key", "{0} \u7121\u516c\u958b\u91d1\u9470" },
			{ "alias has no X.509 certificate", "{0} \u7121 X.509 \u8a8d\u8b49" },
			{ "New certificate (self-signed):",
					"\u65b0\u8a8d\u8b49\uff08\u81ea\u6211\u7c3d\u7f72\uff09\uff1a" },
			{ "Reply has no certificates",
					"\u56de\u8986\u4e0d\u542b\u8a8d\u8b49" },
			{
					"Certificate not imported, alias <alias> already exists",
					"\u8a8d\u8b49\u672a\u8f38\u5165\uff0c\u5225\u540d <{0}> \u5df2\u7d93\u5b58\u5728" },
			{ "Input not an X.509 certificate",
					"\u6240\u8f38\u5165\u7684\u4e0d\u662f\u4e00\u500b X.509 \u8a8d\u8b49" },
			{
					"Certificate already exists in keystore under alias <trustalias>",
					"\u5728 <{0}> \u7684\u5225\u540d\u4e4b\u4e0b\uff0c\u8a8d\u8b49\u5df2\u7d93\u5b58\u5728 keystore \u4e2d" },
			{
					"Do you still want to add it? [no]:  ",
					"\u60a8\u4ecd\u7136\u60f3\u8981\u5c07\u4e4b\u65b0\u589e\u55ce\uff1f [\u5426]\uff1a  " },
			{
					"Certificate already exists in system-wide CA keystore under alias <trustalias>",
					"\u5728 <{0}> \u7684\u5225\u540d\u4e4b\u4e0b\uff0c\u8a8d\u8b49\u5df2\u7d93\u5b58\u5728\u65bc CA keystore \u6574\u500b\u7cfb\u7d71\u4e4b\u4e2d" },
			{
					"Do you still want to add it to your own keystore? [no]:  ",
					"\u60a8\u4ecd\u7136\u60f3\u8981\u5c07\u4e4b\u65b0\u589e\u81f3\u81ea\u5df1\u7684 keystore \u55ce\uff1f [\u5426]\uff1a  " },
			{ "Trust this certificate? [no]:  ",
					"\u4fe1\u4efb\u9019\u500b\u8a8d\u8b49\uff1f [\u5426]\uff1a  " },
			{ "YES", "\u662f" },
			{ "New prompt: ", "\u65b0 {0}\uff1a " },
			{ "Passwords must differ",
					"\u5fc5\u9808\u662f\u4e0d\u540c\u7684\u5bc6\u78bc" },
			{ "Re-enter new prompt: ",
					"\u91cd\u65b0\u8f38\u5165\u65b0 {0}\uff1a " },
			{ "Re-enter new password: ",
					"\u91cd\u65b0\u8f38\u5165\u65b0\u5bc6\u78bc: " },
			{ "They don't match. Try again",
					"\u5b83\u5011\u4e0d\u76f8\u7b26\u3002\u8acb\u91cd\u8a66" },
			{ "Enter prompt alias name:  ",
					"\u8f38\u5165 {0} \u5225\u540d\u540d\u7a31\uff1a  " },
			{
					"Enter new alias name\t(RETURN to cancel import for this entry):  ",
					"\u8acb\u8f38\u5165\u65b0\u7684\u5225\u540d\t(RETURN \u4ee5\u53d6\u6d88\u532f\u5165\u6b64\u9805\u76ee\u7684\u5225\u540d)\uff1a  " },
			{ "Enter alias name:  ",
					"\u8f38\u5165\u5225\u540d\u540d\u7a31\uff1a  " },
			{ "\t(RETURN if same as for <otherAlias>)",
					"\t\uff08RETURN \u5982\u679c\u548c <{0}> \u7684\u76f8\u540c\uff09" },
			{
					"*PATTERN* printX509Cert",
					"\u6240\u6709\u8005\uff1a{0}\n\u6838\u767c\u8005\uff1a{1}\n\u5e8f\u865f\uff1a{2}\n\u81ea\u4ee5\u4e0b\u65e5\u671f\u958b\u59cb\u751f\u6548\uff1a{3}\uff0c\u76f4\u5230\uff1a{4}\n\u6191\u8b49\u6307\u7d0b\uff1a\n\tMD5\uff1a{5}\n\tSHA1\uff1a{6}\n\t\u7c3d\u540d\u6f14\u7b97\u6cd5\u540d\u7a31\uff1a{7}\n\t\u7248\u672c\uff1a{8}" },
			{ "What is your first and last name?",
					"\u60a8\u7684\u540d\u5b57\u8207\u59d3\u6c0f\u70ba\u4f55\uff1f" },
			{ "What is the name of your organizational unit?",
					"\u60a8\u7684\u7de8\u5236\u55ae\u4f4d\u540d\u7a31\u70ba\u4f55\uff1f" },
			{ "What is the name of your organization?",
					"\u60a8\u7684\u7d44\u7e54\u540d\u7a31\u70ba\u4f55\uff1f" },
			{
					"What is the name of your City or Locality?",
					"\u60a8\u6240\u5728\u7684\u57ce\u5e02\u6216\u5730\u5340\u540d\u7a31\u70ba\u4f55\uff1f" },
			{
					"What is the name of your State or Province?",
					"\u60a8\u6240\u5728\u7684\u5dde\u53ca\u7701\u4efd\u540d\u7a31\u70ba\u4f55\uff1f" },
			{ "What is the two-letter country code for this unit?",
					"\u8a72\u55ae\u4f4d\u7684\u4e8c\u5b57\u570b\u78bc\u70ba\u4f55" },
			{ "Is <name> correct?", "{0} \u6b63\u78ba\u55ce\uff1f" },
			{ "no", "\u5426" },
			{ "yes", "\u662f" },
			{ "y", "y" },
			{ "  [defaultValue]:  ", "  [{0}]\uff1a  " },
			{ "Alias <alias> has no key",
					"\u5225\u540d <{0}> \u6c92\u6709\u91d1\u9470" },
			{
					"Alias <alias> references an entry type that is not a private key entry.  The -keyclone command only supports cloning of private key entries",
					"\u5225\u540d <{0}> \u6240\u53c3\u7167\u7684\u9805\u76ee\u4e0d\u662f\u79c1\u5bc6\u91d1\u9470\u985e\u578b\u3002-keyclone \u6307\u4ee4\u50c5\u652f\u63f4\u79c1\u5bc6\u91d1\u9470\u9805\u76ee\u7684\u8907\u88fd" },

			{ "*****************  WARNING WARNING WARNING  *****************",
					"***************** \u8b66\u544a \u8b66\u544a \u8b66\u544a  *****************" },

			// Translators of the following 5 pairs, ATTENTION:
			// the next 5 string pairs are meant to be combined into 2
			// paragraphs,
			// 1+3+4 and 2+3+5. make sure your translation also does.
			{
					"* The integrity of the information stored in your keystore  *",
					"* \u8cc7\u6599\u7684\u5b8c\u6574\u6027\u5df2\u5132\u5b58\u5728\u60a8\u7684 keystore \u4e2d  *" },
			{ "* The integrity of the information stored in the srckeystore*",
					"* \u5b8c\u6574\u7684\u8cc7\u8a0a\u5132\u5b58\u5728 srckeystore \u4e2d *" },
			{
					"* has NOT been verified!  In order to verify its integrity, *",
					"* \u5c1a\u672a\u88ab\u9a57\u8b49\uff01  \u70ba\u4e86\u9a57\u8b49\u5176\u5b8c\u6574\u6027\uff0c *" },
			{
					"* you must provide your keystore password.                  *",
					"* \u60a8\u5fc5\u9808\u63d0\u4f9b\u60a8 keystore \u7684\u5bc6\u78bc\u3002                  *" },
			{
					"* you must provide the srckeystore password.                *",
					"* \u60a8\u5fc5\u9808\u63d0\u4f9b srckeystore \u5bc6\u78bc\u3002                *" },

			{
					"Certificate reply does not contain public key for <alias>",
					"\u8a8d\u8b49\u56de\u8986\u4e26\u672a\u5305\u542b <{0}> \u7684\u516c\u958b\u91d1\u9470" },
			{ "Incomplete certificate chain in reply",
					"\u56de\u8986\u6642\u7684\u8a8d\u8b49\u9375\u4e0d\u5b8c\u6574" },
			{ "Certificate chain in reply does not verify: ",
					"\u56de\u8986\u6642\u7684\u8a8d\u8b49\u93c8\u672a\u9a57\u8b49\uff1a " },
			{ "Top-level certificate in reply:\n",
					"\u56de\u8986\u6642\u7684\u6700\u9ad8\u7d1a\u8a8d\u8b49\uff1a\n" },
			{ "... is not trusted. ",
					"... \u662f\u4e0d\u88ab\u4fe1\u4efb\u7684\u3002 " },
			{ "Install reply anyway? [no]:  ",
					"\u9084\u662f\u8981\u5b89\u88dd\u56de\u8986\uff1f [\u5426]\uff1a  " },
			{ "NO", "\u5426" },
			{ "Public keys in reply and keystore don't match",
					"\u56de\u8986\u6642\u7684\u516c\u958b\u91d1\u9470\u8207 keystore \u4e0d\u7b26" },
			{
					"Certificate reply and certificate in keystore are identical",
					"\u8a8d\u8b49\u56de\u8986\u8207 keystore \u4e2d\u7684\u8a8d\u8b49\u662f\u76f8\u540c\u7684" },
			{ "Failed to establish chain from reply",
					"\u7121\u6cd5\u5f9e\u56de\u8986\u4e2d\u5c07\u9375\u5efa\u7acb\u8d77\u4f86" },
			{ "n", "n" },
			{ "Wrong answer, try again",
					"\u932f\u8aa4\u7684\u7b54\u6848\uff0c\u8acb\u518d\u8a66\u4e00\u6b21" },
			{
					"Secret key not generated, alias <alias> already exists",
					"\u672a\u7522\u751f\u79d8\u5bc6\u91d1\u9470\uff0c\u5225\u540d <{0}> \u5df2\u5b58\u5728" },
			{ "Please provide -keysize for secret key generation",
					"\u8acb\u63d0\u4f9b -keysize \u4ee5\u7522\u751f\u79d8\u5bc6\u91d1\u9470" },
			{ "keytool usage:\n", "keytool \u7528\u6cd5\uff1a\n" },

			{ "Extensions: ", "\u5ef6\u4f38\uff1a " },

			{ "-certreq     [-v] [-protected]",
					"-certreq     [-v] [-protected]" },
			{ "\t     [-alias <alias>] [-sigalg <sigalg>]",
					"\t     [-alias <\u5225\u540d>] [-sigalg <\u7c3d\u7ae0\u6f14\u7b97\u6cd5>]" },
			{
					"\t     [-file <csr_file>] [-keypass <keypass>]",
					"\t     [-file <\u6191\u8b49\u7c3d\u7ae0\u8981\u6c42\u6a94\u6848>] [-keypass <\u4e3b\u5bc6\u78bc>]" },
			{
					"\t     [-keystore <keystore>] [-storepass <storepass>]",
					"\t     [-keystore <\u91d1\u9470\u5132\u5b58\u5eab>] [-storepass <\u5132\u5b58\u5eab\u5bc6\u78bc>]" },
			{
					"\t     [-storetype <storetype>] [-providername <name>]",
					"\t[-storetype <\u5132\u5b58\u5eab\u985e\u578b>] [-providername <\u540d\u7a31>]" },
			{
					"\t     [-providerclass <provider_class_name> [-providerarg <arg>]] ...",
					"\t[-providerclass <\u63d0\u4f9b\u8005\u985e\u5225\u540d\u7a31> [-providerarg <\u5f15\u6578>]] ..." },
			{ "\t     [-providerpath <pathlist>]",
					"\t[-providerpath <\u8def\u5f91\u6e05\u55ae>]" },
			{ "-delete      [-v] [-protected] -alias <alias>",
					"-delete      [-v] [-protected] -alias <\u5225\u540d>" },
			/** rest is same as -certreq starting from -keystore **/

			// {"-export      [-v] [-rfc] [-protected]",
			// "-export      [-v] [-rfc] [-protected]"},
			{ "-exportcert  [-v] [-rfc] [-protected]",
					"-exportcert [-v] [-rfc] [-protected]" },
			{ "\t     [-alias <alias>] [-file <cert_file>]",
					"\t     [-alias <\u5225\u540d>] [-file <\u8a8d\u8b49\u6a94\u6848>]" },
			/** rest is same as -certreq starting from -keystore **/

			// {"-genkey      [-v] [-protected]",
			// "-genkey      [-v] [-protected]"},
			{ "-genkeypair  [-v] [-protected]", "-genkeypair [-v] [-protected]" },
			{ "\t     [-alias <alias>]", "\t     [-alias <\u5225\u540d>]" },
			{
					"\t     [-keyalg <keyalg>] [-keysize <keysize>]",
					"\t     [-keyalg <\u91d1\u9470\u6f14\u7b97\u6cd5>] [-keysize <\u91d1\u9470\u5927\u5c0f>]" },
			{
					"\t     [-sigalg <sigalg>] [-dname <dname>]",
					"\t     [-sigalg <\u7c3d\u7ae0\u6f14\u7b97\u6cd5>] [-dname <\u7db2\u57df\u540d\u7a31>]" },
			{ "\t     [-validity <valDays>] [-keypass <keypass>]",
					"\t     [-validity <\u6709\u6548\u5929\u6578>] [-keypass <\u4e3b\u5bc6\u78bc>]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-genseckey   [-v] [-protected]", "-genseckey [-v] [-protected]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-help", "-help" },
			// {"-identitydb  [-v] [-protected]",
			// "-identitydb  [-v] [-protected]"},
			// {"\t     [-file <idb_file>]",
			// "\t     [-file <\u8fa8\u8b58\u8cc7\u6599\u5eab\u6a94\u6848>]"},
			/** rest is same as -certreq starting from -keystore **/

			// {"-import      [-v] [-noprompt] [-trustcacerts] [-protected]",
			// "-import      [-v] [-noprompt] [-trustcacerts] [-protected]"},
			{ "-importcert  [-v] [-noprompt] [-trustcacerts] [-protected]",
					"-importcert [-v] [-noprompt] [-trustcacerts] [-protected]" },
			{ "\t     [-alias <alias>]", "\t     [-alias <\u5225\u540d>]" },
			{ "\t     [-alias <alias>] [-keypass <keypass>]",
					"\t[-alias <\u5225\u540d>] [-keypass <\u4e3b\u5bc6\u78bc>]" },
			{ "\t     [-file <cert_file>] [-keypass <keypass>]",
					"\t     [-file <\u8a8d\u8b49\u6a94\u6848>] [-keypass <\u4e3b\u5bc6\u78bc>]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-importkeystore [-v] ", "-importkeystore [-v]" },
			{
					"\t     [-srckeystore <srckeystore>] [-destkeystore <destkeystore>]",
					"\t[-srckeystore <\u4f86\u6e90\u91d1\u9470\u5132\u5b58\u5eab>] [-destkeystore <\u76ee\u6a19\u91d1\u9470\u5132\u5b58\u5eab>]" },
			{
					"\t     [-srcstoretype <srcstoretype>] [-deststoretype <deststoretype>]",
					"\t[-srcstoretype <\u4f86\u6e90\u5132\u5b58\u5eab\u985e\u578b>] [-deststoretype <\u76ee\u6a19\u5132\u5b58\u5eab\u985e\u578b>]" },
			{ "\t     [-srcprotected] [-destprotected]",
					"\t[-srcprotected] [-destprotected]" },
			{
					"\t     [-srcstorepass <srcstorepass>] [-deststorepass <deststorepass>]",
					"\t[-srcstorepass <\u4f86\u6e90\u5132\u5b58\u5eab\u5bc6\u78bc>] [-deststorepass <\u76ee\u6a19\u5132\u5b58\u5eab\u5bc6\u78bc>]" },
			{
					"\t     [-srcprovidername <srcprovidername>]\n\t     [-destprovidername <destprovidername>]", // \u884c\u592a\u9577\uff0c\u8acb\u5206\u70ba
																													// 2
																													// \u884c
					"\t[-srcprovidername <\u4f86\u6e90\u63d0\u4f9b\u8005\u540d\u7a31>]\n\t[-destprovidername <\u76ee\u6a19\u63d0\u4f9b\u8005\u540d\u7a31>]" },
			{
					"\t     [-srcalias <srcalias> [-destalias <destalias>]",
					"\t[-srcalias <\u4f86\u6e90\u5225\u540d> [-destalias <\u76ee\u6a19\u5225\u540d>]" },
			{
					"\t       [-srckeypass <srckeypass>] [-destkeypass <destkeypass>]]",
					"\t[-srckeypass <\u4f86\u6e90\u4e3b\u5bc6\u78bc>] [-destkeypass <\u76ee\u6a19\u4e3b\u5bc6\u78bc>]]" },
			{ "\t     [-noprompt]", "\t[-noprompt]" },
			/** rest is same as -certreq starting from -keystore **/

			{
					"-changealias [-v] [-protected] -alias <alias> -destalias <destalias>",
					"-changealias [-v] [-protected] -alias <\u5225\u540d> -destalias <\u76ee\u6a19\u5225\u540d>" },
			{ "\t     [-keypass <keypass>]",
					"\t     [-keypass <\u4e3b\u5bc6\u78bc>]" },

			// {"-keyclone    [-v] [-protected]",
			// "-keyclone    [-v] [-protected]"},
			// {"\t     [-alias <alias>] -dest <dest_alias>",
			// "\t     [-alias <\u5225\u540d>] -dest <\u76ee\u6a19\u5225\u540d>"},
			// {"\t     [-keypass <keypass>] [-new <new_keypass>]",
			// "\t     [-keypass <\u4e3b\u5bc6\u78bc>] [-new <\u65b0\u4e3b\u5bc6\u78bc>]"},
			/** rest is same as -certreq starting from -keystore **/

			{ "-keypasswd   [-v] [-alias <alias>]",
					"-keypasswd   [-v] [-alias <\u5225\u540d>]" },
			{
					"\t     [-keypass <old_keypass>] [-new <new_keypass>]",
					"\t     [-keypass <\u820a\u4e3b\u5bc6\u78bc>] [-new <\u65b0\u4e3b\u5bc6\u78bc>]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-list        [-v | -rfc] [-protected]",
					"-list        [-v | -rfc] [-protected]" },
			{ "\t     [-alias <alias>]", "\t     [-alias <\u5225\u540d>]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-printcert   [-v] [-file <cert_file>]",
					"-printcert   [-v] [-file <\u8a8d\u8b49\u6a94\u6848>]" },

			// {"-selfcert    [-v] [-protected]",
			// "-selfcert    [-v] [-protected]"},
			{ "\t     [-alias <alias>]", "\t     [-alias <\u5225\u540d>]" },
			// {"\t     [-dname <dname>] [-validity <valDays>]",
			// "\t     [-dname <\u7db2\u57df\u540d\u7a31>] [-validity <\u6709\u6548\u5929\u6578>]"},
			// {"\t     [-keypass <keypass>] [-sigalg <sigalg>]",
			// "\t     [-keypass <\u4e3b\u5bc6\u78bc>] [-sigalg <\u7c3d\u7ae0\u6f14\u7b97\u6cd5>]"},
			/** rest is same as -certreq starting from -keystore **/

			{ "-storepasswd [-v] [-new <new_storepass>]",
					"-storepasswd [-v] [-new <\u65b0\u5132\u5b58\u5eab\u5bc6\u78bc>]" },
			/** rest is same as -certreq starting from -keystore **/

			// policytool
			{
					"Warning: A public key for alias 'signers[i]' does not exist.  Make sure a KeyStore is properly configured.",
					"\u8b66\u544a\ufe30\u5225\u540d {0} \u7684\u516c\u958b\u91d1\u9470\u4e0d\u5b58\u5728\u3002\u8acb\u78ba\u5b9a\u91d1\u9470\u5132\u5b58\u5eab\u914d\u7f6e\u6b63\u78ba\u3002" },
			{ "Warning: Class not found: class",
					"\u8b66\u544a\ufe30\u627e\u4e0d\u5230\u985e\u5225 {0}" },
			{ "Warning: Invalid argument(s) for constructor: arg",
					"\u8b66\u544a\ufe30\u7121\u6548\u7684\u5efa\u69cb\u5b50\u5f15\u6578\uff1a{0}" },
			{ "Illegal Principal Type: type",
					"\u975e\u6cd5\u7684\u4e3b\u9ad4\u985e\u578b\ufe30{0}" },
			{ "Illegal option: option",
					"\u975e\u6cd5\u7684\u9078\u9805\uff1a{0}" },
			{ "Usage: policytool [options]",
					"\u7528\u6cd5\uff1a policytool [\u9078\u9805]" },
			{ "  [-file <file>]    policy file location",
					"  [-file <file>]    \u898f\u5247\u6a94\u6848\u4f4d\u7f6e" },
			{ "New", "\u65b0\u589e" },
			{ "Open", "\u958b\u555f" },
			{ "Save", "\u5132\u5b58" },
			{ "Save As", "\u53e6\u5b58\u65b0\u6a94" },
			{ "View Warning Log", "\u6aa2\u8996\u8b66\u544a\u8a18\u9304" },
			{ "Exit", "\u96e2\u958b" },
			{ "Add Policy Entry", "\u65b0\u589e\u898f\u5247\u9805\u76ee" },
			{ "Edit Policy Entry", "\u7de8\u8f2f\u898f\u5247\u9805\u76ee" },
			{ "Remove Policy Entry", "\u79fb\u9664\u898f\u5247\u9805\u76ee" },
			{ "Edit", "\u7de8\u8f2f" },
			{ "Retain", "\u4fdd\u7559" },

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
					"\u65b0\u589e\u516c\u958b\u91d1\u9470\u5225\u540d" },
			{ "Remove Public Key Alias",
					"\u79fb\u9664\u516c\u958b\u91d1\u9470\u5225\u540d" },
			{ "File", "\u6a94\u6848" },
			{ "KeyStore", "\u91d1\u9470\u5132\u5b58\u5eab" },
			{ "Policy File:", "\u898f\u5247\u6a94\u6848\uff1a" },
			{ "Could not open policy file: policyFile: e.toString()",
					"\u7121\u6cd5\u958b\u555f\u7b56\u7565\u6a94\u6848\uff1a{0}\uff1a{1}" },
			{ "Policy Tool", "\u898f\u5247\u5de5\u5177" },
			{
					"Errors have occurred while opening the policy configuration.  View the Warning Log for more information.",
					"\u958b\u555f\u898f\u5247\u8a18\u7f6e\u6642\u767c\u751f\u932f\u8aa4\u3002 \u8acb\u6aa2\u8996\u8b66\u544a\u8a18\u9304\u4ee5\u53d6\u5f97\u66f4\u591a\u7684\u8cc7\u8a0a" },
			{ "Error", "\u932f\u8aa4" },
			{ "OK", "\u78ba\u8a8d" },
			{ "Status", "\u72c0\u614b" },
			{ "Warning", "\u8b66\u544a" },
			{
					"Permission:                                                       ",
					"\u8a31\u53ef\uff1a                                                       " },
			{ "Principal Type:", "Principal \u985e\u578b\uff1a" },
			{ "Principal Name:", "Principal \u540d\u7a31\uff1a" },
			{
					"Target Name:                                                    ",
					"\u76ee\u6a19\u540d\u7a31\uff1a                                                    " },
			{
					"Actions:                                                             ",
					"\u52d5\u4f5c\uff1a                                                             " },
			{ "OK to overwrite existing file filename?",
					"\u78ba\u8a8d\u8986\u5beb\u73fe\u5b58\u7684\u6a94\u6848 {0}\uff1f" },
			{ "Cancel", "\u53d6\u6d88" },
			{ "CodeBase:", "CodeBase:" },
			{ "SignedBy:", "SignedBy:" },
			{ "Add Principal", "\u65b0\u589e Principal" },
			{ "Edit Principal", "\u7de8\u8f2f Principal" },
			{ "Remove Principal", "\u79fb\u9664 Principal" },
			{ "Principals:", "Principals\uff1a" },
			{ "  Add Permission", "  \u65b0\u589e\u8a31\u53ef\u6b0a" },
			{ "  Edit Permission", "  \u7de8\u8f2f\u8a31\u53ef\u6b0a" },
			{ "Remove Permission", "\u79fb\u9664\u8a31\u53ef\u6b0a" },
			{ "Done", "\u5b8c\u6210" },
			{ "KeyStore URL:", "\u91d1\u9470\u5132\u5b58\u5eab URL\uff1a" },
			{ "KeyStore Type:",
					"\u91d1\u9470\u5132\u5b58\u5eab\u985e\u578b\ufe30" },
			{ "KeyStore Provider:",
					"\u91d1\u9470\u5132\u5b58\u5eab\u63d0\u4f9b\u8005\ufe30" },
			{ "KeyStore Password URL:",
					"\u91d1\u9470\u5132\u5b58\u5eab\u5bc6\u78bc URL\uff1a" },
			{ "Principals", "Principals" },
			{ "  Edit Principal:", "  \u7de8\u8f2f Principal\uff1a" },
			{ "  Add New Principal:", "  \u52a0\u5165\u65b0 Principal\uff1a" },
			{ "Permissions", "\u8a31\u53ef\u6b0a" },
			{ "  Edit Permission:", "  \u7de8\u8f2f\u8a31\u53ef\u6b0a" },
			{ "  Add New Permission:",
					"  \u52a0\u5165\u65b0\u7684\u8a31\u53ef\u6b0a" },
			{ "Signed By:", "\u7c3d\u7f72\u4eba\uff1a" },
			{
					"Cannot Specify Principal with a Wildcard Class without a Wildcard Name",
					"\u6c92\u6709\u901a\u914d\u7b26\u865f\u540d\u7a31\uff0c\u7121\u6cd5\u6307\u5b9a\u542b\u6709\u901a\u914d\u7b26\u865f\u985e\u5225\u7684 Principal" },
			{ "Cannot Specify Principal without a Name",
					"\u6c92\u6709\u540d\u7a31\uff0c\u7121\u6cd5\u6307\u5b9a Principal" },
			{
					"Permission and Target Name must have a value",
					"\u8a31\u53ef\u6b0a\u53ca\u76ee\u6a19\u5fc5\u9808\u6709\u4e00\u500b\u503c\u3002" },
			{ "Remove this Policy Entry?",
					"\u79fb\u9664\u9019\u500b\u898f\u5247\u9805\u76ee\uff1f" },
			{ "Overwrite File", "\u8986\u5beb\u6a94\u6848" },
			{ "Policy successfully written to filename",
					"\u898f\u5247\u6210\u529f\u5beb\u81f3 {0}" },
			{ "null filename", "\u7121\u6548\u7684\u6a94\u540d" },
			{ "Save changes?", "\u5132\u5b58\u8b8a\u66f4\uff1f" },
			{ "Yes", "\u662f" },
			{ "No", "\u5426" },
			{ "Policy Entry", "\u898f\u5247\u9805\u76ee" },
			{ "Save Changes", "\u5132\u5b58\u8b8a\u66f4" },
			{ "No Policy Entry selected",
					"\u6c92\u6709\u9078\u53d6\u898f\u5247\u9805\u76ee" },
			{ "Unable to open KeyStore: ex.toString()",
					"\u7121\u6cd5\u958b\u555f\u91d1\u9470\u5132\u5b58\u5eab\uff1a{0}" },
			{ "No principal selected", "\u672a\u9078\u53d6 Principal" },
			{ "No permission selected",
					"\u6c92\u6709\u9078\u53d6\u8a31\u53ef\u6b0a" },
			{ "name", "\u540d\u7a31" },
			{ "configuration type", "\u914d\u7f6e\u985e\u578b" },
			{ "environment variable name",
					"\u74b0\u5883\u8b8a\u6578\u540d\u7a31" },
			{ "library name", "\u7a0b\u5f0f\u5eab\u540d\u7a31" },
			{ "package name", "\u5957\u88dd\u8edf\u9ad4\u540d\u7a31" },
			{ "policy type", "\u7b56\u7565\u985e\u578b" },
			{ "property name", "\u5c6c\u6027\u540d\u7a31" },
			{ "provider name", "\u63d0\u4f9b\u8005\u540d\u7a31" },
			{ "Principal List", "\u4e3b\u9ad4\u6e05\u55ae" },
			{ "Permission List", "\u6b0a\u9650\u6e05\u55ae" },
			{ "Code Base", "\u4ee3\u78bc\u57fa\u6e96" },
			{ "KeyStore U R L:", "\u91d1\u9470\u5132\u5b58\u5eab U R L\uff1a" },
			{ "KeyStore Password U R L:",
					"\u91d1\u9470\u5132\u5b58\u5eab\u5bc6\u78bc U R L\uff1a" },

			// javax.security.auth.PrivateCredentialPermission
			{ "invalid null input(s)", "\u7121\u6548\u7a7a\u8f38\u5165" },
			{ "actions can only be 'read'",
					"\u52d5\u4f5c\u53ea\u80fd\u88ab\u8b80\u53d6'" },
			{
					"permission name [name] syntax invalid: ",
					"\u8a31\u53ef\u6b0a\u540d\u7a31 [{0}] \u662f\u7121\u6548\u7684\u8a9e\u6cd5\uff1a " },
			{
					"Credential Class not followed by a Principal Class and Name",
					"\u8a8d\u8b49\u7b49\u7d1a\u672a\u63a5\u5728 Principal \u985e\u5225\u53ca\u540d\u7a31\u4e4b\u5f8c" },
			{ "Principal Class not followed by a Principal Name",
					"Principal \u985e\u5225\u672a\u63a5\u5728 Principal \u540d\u7a31\u4e4b\u5f8c" },
			{ "Principal Name must be surrounded by quotes",
					"Principal \u540d\u7a31\u5fc5\u9808\u4ee5\u5f15\u865f\u5708\u4f4f" },
			{ "Principal Name missing end quote",
					"Principal \u540d\u7a31\u7f3a\u5c11\u4e0b\u5f15\u865f" },
			{
					"PrivateCredentialPermission Principal Class can not be a wildcard (*) value if Principal Name is not a wildcard (*) value",
					"\u5982\u679c Principal \u540d\u7a31\u4e0d\u662f\u4e00\u500b\u901a\u914d\u7b26\u865f (*) \u503c\uff0c\u90a3\u9ebc PrivateCredentialPermission Principal \u985e\u5225\u5c31\u4e0d\u6703\u662f\u4e00\u500b\u901a\u914d\u7b26\u865f (*) \u503c" },
			{ "CredOwner:\n\tPrincipal Class = class\n\tPrincipal Name = name",
					"CredOwner:\n\tPrincipal \u985e\u5225 = {0}\n\tPrincipal \u540d\u7a31 = {1}" },

			// javax.security.auth.x500
			{ "provided null name", "\u63d0\u4f9b\u7a7a\u540d" },
			{ "provided null keyword map",
					"\u63d0\u4f9b\u7a7a\u7684\u95dc\u9375\u5b57\u5c0d\u6620" },
			{ "provided null OID map",
					"\u63d0\u4f9b\u7a7a\u7684 OID \u5c0d\u6620" },

			// javax.security.auth.Subject
			{ "invalid null AccessControlContext provided",
					"\u63d0\u4f9b\u7121\u6548\u7684\u7a7a AccessControlContext" },
			{ "invalid null action provided",
					"\u63d0\u4f9b\u7121\u6548\u7684\u7a7a\u52d5\u4f5c" },
			{ "invalid null Class provided",
					"\u63d0\u4f9b\u7121\u6548\u7684\u7a7a\u985e\u5225" },
			{ "Subject:\n", "\u4e3b\u984c\uff1a\n" },
			{ "\tPrincipal: ", "\tPrincipal: " },
			{ "\tPublic Credential: ", "\t\u516c\u7528\u8a8d\u8b49 " },
			{ "\tPrivate Credentials inaccessible\n",
					"\t\u79c1\u4eba\u8a8d\u8b49\u7121\u6cd5\u9032\u5165\n" },
			{ "\tPrivate Credential: ", "\t\u79c1\u4eba\u6388\u6b0a " },
			{ "\tPrivate Credential inaccessible\n",
					"\t\u79c1\u4eba\u8a8d\u8b49\u7121\u6cd5\u9032\u5165\n" },
			{ "Subject is read-only", "\u4e3b\u984c\u70ba\u552f\u8b80" },
			{
					"attempting to add an object which is not an instance of java.security.Principal to a Subject's Principal Set",
					"\u8a66\u5716\u65b0\u589e\u4e00\u500b\u975e java.security.Principal \u6848\u4f8b\u7684\u7269\u4ef6\u81f3\u4e3b\u984c\u7684 Principal \u7fa4\u4e2d" },
			{ "attempting to add an object which is not an instance of class",
					"\u8a66\u5716\u65b0\u589e\u4e00\u500b\u975e {0} \u6848\u4f8b\u7684\u7269\u4ef6" },

			// javax.security.auth.login.AppConfigurationEntry
			{ "LoginModuleControlFlag: ", "LoginModuleControlFlag: " },

			// javax.security.auth.login.LoginContext
			{ "Invalid null input: name",
					"\u7121\u6548\u7a7a\u8f38\u5165\uff1a \u540d\u7a31" },
			{ "No LoginModules configured for name",
					"\u7121\u91dd\u5c0d {0} \u914d\u7f6e\u7684 LoginModules" },
			{ "invalid null Subject provided",
					"\u63d0\u4f9b\u7121\u6548\u7a7a\u4e3b\u984c" },
			{ "invalid null CallbackHandler provided",
					"\u63d0\u4f9b\u7121\u6548\u7a7a CallbackHandler" },
			{
					"null subject - logout called before login",
					"\u7a7a\u4e3b\u984c - \u5728\u767b\u5165\u4e4b\u524d\u5373\u547c\u53eb\u767b\u51fa" },
			{
					"unable to instantiate LoginModule, module, because it does not provide a no-argument constructor",
					"\u7121\u6cd5\u5be6\u5217\u5316 LoginModule\uff0c{0}\uff0c\u56e0\u70ba\u5b83\u4e26\u672a\u63d0\u4f9b\u4e00\u500b\u975e\u5f15\u6578\u7684\u69cb\u9020\u51fd\u6578" },
			{ "unable to instantiate LoginModule",
					"\u7121\u6cd5\u5be6\u4f8b\u5316 LoginModule" },
			{ "unable to instantiate LoginModule: ",
					"\u7121\u6cd5\u5275\u8a2d LoginModule\uff1a" },
			{ "unable to find LoginModule class: ",
					"\u7121\u6cd5\u627e\u5230 LoginModule \u985e\u5225\uff1a " },
			{ "unable to access LoginModule: ",
					"\u7121\u6cd5\u5b58\u53d6 LoginModule: " },
			{ "Login Failure: all modules ignored",
					"\u767b\u5165\u5931\u6557\uff1a \u5ffd\u7565\u6240\u6709\u6a21\u7d44" },

			// sun.security.provider.PolicyFile

			{ "java.security.policy: error parsing policy:\n\tmessage",
					"java.security.policy: \u89e3\u6790\u932f\u8aa4 {0}\uff1a\n\t{1}" },
			{
					"java.security.policy: error adding Permission, perm:\n\tmessage",
					"java.security.policy: \u65b0\u589e\u8a31\u53ef\u6b0a\u932f\u8aa4 {0}\uff1a\n\t{1}" },
			{ "java.security.policy: error adding Entry:\n\tmessage",
					"java.security.policy: \u65b0\u589e\u9805\u76ee\u932f\u8aa4\uff1a\n\t{0}" },
			{ "alias name not provided (pe.name)",
					"\u5225\u540d\u540d\u7a31 ({0}) \u672a\u63d0\u4f9b" },
			{ "unable to perform substitution on alias, suffix",
					"\u7121\u6cd5\u5c0d\u5225\u540d\u57f7\u884c\u66ff\u63db\uff0c{0}" },
			{ "substitution value, prefix, unsupported",
					"\u4e0d\u652f\u63f4\u7684\u66ff\u63db\u503c\uff0c{0}" },
			{ "(", "(" },
			{ ")", ")" },
			{ "type can't be null", "\u4e0d\u80fd\u70ba\u7a7a\u8f38\u5165" },

			// sun.security.provider.PolicyParser
			{
					"keystorePasswordURL can not be specified without also specifying keystore",
					"\u6307\u5b9a keystorePasswordURL \u9700\u8981\u540c\u6642\u6307\u5b9a keystore" },
			{ "expected keystore type",
					"\u9810\u671f\u7684 keystore \u985e\u578b" },
			{ "expected keystore provider",
					"\u9810\u671f\u7684 keystore \u63d0\u4f9b\u8005" },
			{ "multiple Codebase expressions",
					"\u591a\u52d5 Codebase \u8868\u793a\u5f0f" },
			{ "multiple SignedBy expressions",
					"\u591a\u91cd SignedBy \u8868\u793a\u5f0f" },
			{ "SignedBy has empty alias", "SignedBy \u6709\u7a7a\u5225\u540d" },
			{
					"can not specify Principal with a wildcard class without a wildcard name",
					"\u6c92\u6709\u901a\u914d\u7b26\u865f\u540d\u7a31\uff0c\u7121\u6cd5\u6307\u5b9a\u542b\u6709\u901a\u914d\u7b26\u865f\u985e\u5225\u7684 Principal" },
			{ "expected codeBase or SignedBy or Principal",
					"\u9810\u671f\u7684 codeBase \u6216 SignedBy \u6216 Principal" },
			{ "expected permission entry",
					"\u9810\u671f\u7684\u8a31\u53ef\u6b0a\u9805\u76ee" },
			{ "number ", "\u865f\u78bc " },
			{ "expected [expect], read [end of file]",
					"\u9810\u671f\u7684 [{0}], \u8b80\u53d6 [end of file]" },
			{ "expected [;], read [end of file]",
					"\u9810\u671f\u7684 [;], \u8b80\u53d6 [end of file]" },
			{ "line number: msg", "\u884c {0}\uff1a {1}" },
			{ "line number: expected [expect], found [actual]",
					"\u884c {0}\uff1a \u9810\u671f\u7684 [{1}]\uff0c\u767c\u73fe [{2}]" },
			{ "null principalClass or principalName",
					"\u7a7a principalClass \u6216 principalName" },

			// sun.security.pkcs11.SunPKCS11
			{ "PKCS11 Token [providerName] Password: ",
					"PKCS11 \u8a18\u865f [{0}] \u5bc6\u78bc\uff1a " },

			/* --- DEPRECATED --- */
			// javax.security.auth.Policy
			{ "unable to instantiate Subject-based policy",
					"\u7121\u6cd5\u5275\u8a2d\u57fa\u65bc\u4e3b\u9ad4\u7684\u7b56\u7565" } };

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
