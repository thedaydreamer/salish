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
public class Resources_zh_CN extends java.util.ListResourceBundle {

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
			{ "keytool error: ", "keytool\u9519\u8bef\uff1a " },
			{ "Illegal option:  ", "\u975e\u6cd5\u9009\u9879\uff1a  " },
			{ "Try keytool -help", "\u5c1d\u8bd5 keytool -help" },
			{ "Command option <flag> needs an argument.",
					"\u547d\u4ee4\u9009\u9879 {0} \u9700\u8981\u4e00\u4e2a\u53c2\u6570\u3002" },
			{
					"Warning:  Different store and key passwords not supported for PKCS12 KeyStores. Ignoring user-specified <command> value.",
					"\u8b66\u544a: PKCS12 KeyStores \u4e0d\u652f\u6301\u5176\u4ed6\u5b58\u50a8\u548c\u5bc6\u94a5\u53e3\u4ee4\u3002\u5ffd\u7565\u7528\u6237\u6307\u5b9a\u7684 {0} \u503c\u3002" },
			{
					"-keystore must be NONE if -storetype is {0}",
					"\u5982\u679c -storetype \u4e3a {0}\uff0c\u5219 -keystore \u5fc5\u987b\u4e3a NONE" },
			{ "Too may retries, program terminated",
					"\u91cd\u8bd5\u6b21\u6570\u8fc7\u591a\uff0c\u7a0b\u5e8f\u5df2\u7ec8\u6b62" },
			{
					"-storepasswd and -keypasswd commands not supported if -storetype is {0}",
					"\u5982\u679c -storetype \u4e3a {0}\uff0c\u5219\u4e0d\u652f\u6301 -storepasswd \u548c -keypasswd \u547d\u4ee4" },
			{
					"-keypasswd commands not supported if -storetype is PKCS12",
					"\u5982\u679c -storetype \u4e3a PKCS12\uff0c\u5219\u4e0d\u652f\u6301 -keypasswd \u547d\u4ee4" },
			{
					"-keypass and -new can not be specified if -storetype is {0}",
					"\u5982\u679c -storetype \u4e3a {0}\uff0c\u5219\u4e0d\u80fd\u6307\u5b9a -keypass \u548c -new" },
			{
					"if -protected is specified, then -storepass, -keypass, and -new must not be specified",
					"\u5982\u679c\u6307\u5b9a\u4e86 -protected\uff0c\u5219\u4e0d\u8981\u6307\u5b9a -storepass\u3001-keypass \u548c -new" },
			{
					"if -srcprotected is specified, then -srcstorepass and -srckeypass must not be specified",
					"\u5982\u679c\u6307\u5b9a\u4e86 -srcprotected\uff0c\u5219\u4e0d\u80fd\u6307\u5b9a -srcstorepass \u548c -srckeypass" },
			{
					"if keystore is not password protected, then -storepass, -keypass, and -new must not be specified",
					"\u5982\u679c\u5bc6\u94a5\u5e93\u672a\u53d7\u5bc6\u7801\u4fdd\u62a4\uff0c\u5219\u8bf7\u52ff\u6307\u5b9a -storepass\u3001-keypass \u548c -new" },
			{
					"if source keystore is not password protected, then -srcstorepass and -srckeypass must not be specified",
					"\u5982\u679c\u6e90\u5bc6\u94a5\u5e93\u672a\u53d7\u5bc6\u7801\u4fdd\u62a4\uff0c\u5219\u8bf7\u52ff\u6307\u5b9a -srcstorepass \u548c -srckeypass" },
			{ "Validity must be greater than zero",
					"\u6709\u6548\u6027\u5fc5\u987b\u5927\u4e8e\u96f6" },
			{ "provName not a provider",
					"{0}\u4e0d\u662f\u4e00\u4e2a\u63d0\u4f9b\u8005" },
			{ "Usage error: no command provided",
					"\u7528\u6cd5\u9519\u8bef: \u6ca1\u6709\u63d0\u4f9b\u547d\u4ee4" },
			{ "Usage error, <arg> is not a legal command",
					"\u7528\u6cd5\u9519\u8bef\uff0c{0} \u4e0d\u662f\u5408\u6cd5\u7684\u547d\u4ee4" },
			{ "Source keystore file exists, but is empty: ",
					"\u6e90\u5bc6\u94a5\u5e93\u6587\u4ef6\u5b58\u5728\uff0c\u4f46\u4e3a\u7a7a: " },
			{ "Please specify -srckeystore", "\u8bf7\u6307\u5b9a -srckeystore" },
			{
					"Must not specify both -v and -rfc with 'list' command",
					"\u4e0d\u5f97\u4ee5\u300c\u5217\u8868\u300d\u6307\u4ee4\u6765\u6307\u5b9a-v \u53ca-rfc" },
			{ "Key password must be at least 6 characters",
					"\u5173\u952e\u5bc6\u7801\u81f3\u5c11\u5fc5\u987b\u4e3a6\u4e2a\u5b57\u7b26" },
			{ "New password must be at least 6 characters",
					"\u65b0\u5bc6\u7801\u81f3\u5c11\u5fc5\u987b\u4e3a6\u4e2a\u5b57\u7b26" },
			{ "Keystore file exists, but is empty: ",
					"Keystore\u6587\u4ef6\u5b58\u5728\uff0c\u4f46\u4e3a\u7a7a\u6587\u4ef6\uff1a " },
			{ "Keystore file does not exist: ",
					"Keystore \u6587\u4ef6\u4e0d\u5b58\u5728\uff1a " },
			{ "Must specify destination alias",
					"\u5fc5\u987b\u6307\u5b9a\u76ee\u7684\u5730\u522b\u540d" },
			{ "Must specify alias", "\u5fc5\u987b\u6307\u5b9a\u522b\u540d" },
			{ "Keystore password must be at least 6 characters",
					"Keystore \u5bc6\u7801\u81f3\u5c11\u5fc5\u987b\u4e3a6\u4e2a\u5b57\u7b26" },
			{ "Enter keystore password:  ",
					"\u8f93\u5165keystore\u5bc6\u7801\uff1a  " },
			{ "Enter source keystore password:  ",
					"\u8f93\u5165\u6e90\u5bc6\u94a5\u5e93\u53e3\u4ee4:  " },
			{ "Enter destination keystore password:  ",
					"\u8f93\u5165\u76ee\u6807\u5bc6\u94a5\u5e93\u53e3\u4ee4:  " },
			{
					"Keystore password is too short - must be at least 6 characters",
					"Keystore \u5bc6\u7801\u592a\u77ed -\u81f3\u5c11\u5fc5\u987b\u4e3a6\u4e2a\u5b57\u7b26" },
			{ "Unknown Entry Type", "\u672a\u77e5\u9879\u7c7b\u578b" },
			{ "Too many failures. Alias not changed",
					"\u9519\u8bef\u8fc7\u591a\u3002\u672a\u66f4\u6539\u522b\u540d" },
			{ "Entry for alias <alias> successfully imported.",
					"\u5df2\u6210\u529f\u5bfc\u5165\u522b\u540d {0} \u9879\u3002" },
			{ "Entry for alias <alias> not imported.",
					"\u672a\u5bfc\u5165\u522b\u540d {0} \u9879\u3002" },
			{
					"Problem importing entry for alias <alias>: <exception>.\nEntry for alias <alias> not imported.",
					"\u5bfc\u5165\u522b\u540d {0} \u9879\u65f6\u51fa\u73b0\u95ee\u9898: {1}\u3002\n\u672a\u5bfc\u5165\u522b\u540d {0} \u9879\u3002" },
			{
					"Import command completed:  <ok> entries successfully imported, <fail> entries failed or cancelled",
					"\u5df2\u5b8c\u6210\u5bfc\u5165\u547d\u4ee4: {0} \u9879\u6210\u529f\u5bfc\u5165\uff0c{1} \u9879\u5931\u8d25\u6216\u53d6\u6d88" },
			{
					"Warning: Overwriting existing alias <alias> in destination keystore",
					"\u8b66\u544a: \u6b63\u5728\u8986\u76d6\u76ee\u6807\u5bc6\u94a5\u5e93\u4e2d\u7684\u73b0\u6709\u522b\u540d {0}" },
			{
					"Existing entry alias <alias> exists, overwrite? [no]:  ",
					"\u5b58\u5728\u73b0\u6709\u9879\u522b\u540d {0}\uff0c\u662f\u5426\u8981\u8986\u76d6\uff1f[\u5426]:  " },
			{ "Too many failures - try later",
					"\u592a\u591a\u9519\u8bef - \u8bf7\u7a0d\u540e\u518d\u8bd5" },
			{ "Certification request stored in file <filename>",
					"\u4fdd\u5b58\u5728\u6587\u4ef6\u4e2d\u7684\u8ba4\u8bc1\u8981\u6c42 <{0}>" },
			{ "Submit this to your CA",
					"\u5c06\u6b64\u63d0\u4ea4\u7ed9\u60a8\u7684CA" },
			{
					"if alias not specified, destalias, srckeypass, and destkeypass must not be specified",
					"\u5982\u679c\u6ca1\u6709\u6307\u5b9a\u522b\u540d\uff0c\u5219\u4e0d\u80fd\u6307\u5b9a\u76ee\u6807\u522b\u540d\u3001\u6e90\u5bc6\u94a5\u5e93\u53e3\u4ee4\u548c\u76ee\u6807\u5bc6\u94a5\u5e93\u53e3\u4ee4" },
			{ "Certificate stored in file <filename>",
					"\u4fdd\u5b58\u5728\u6587\u4ef6\u4e2d\u7684\u8ba4\u8bc1 <{0}>" },
			{ "Certificate reply was installed in keystore",
					"\u8ba4\u8bc1\u56de\u590d\u5df2\u5b89\u88c5\u5728 keystore\u4e2d" },
			{ "Certificate reply was not installed in keystore",
					"\u8ba4\u8bc1\u56de\u590d\u672a\u5b89\u88c5\u5728 keystore\u4e2d" },
			{ "Certificate was added to keystore",
					"\u8ba4\u8bc1\u5df2\u6dfb\u52a0\u81f3keystore\u4e2d" },
			{ "Certificate was not added to keystore",
					"\u8ba4\u8bc1\u672a\u6dfb\u52a0\u81f3keystore\u4e2d" },
			{ "[Storing ksfname]", "[\u6b63\u5728\u5b58\u50a8 {0}]" },
			{ "alias has no public key (certificate)",
					"{0} \u6ca1\u6709\u516c\u5f00\u91d1\u94a5\uff08\u8ba4\u8bc1\uff09" },
			{ "Cannot derive signature algorithm",
					"\u65e0\u6cd5\u53d6\u5f97\u7b7e\u540d\u7b97\u6cd5" },
			{ "Alias <alias> does not exist",
					"\u522b\u540d <{0}> \u4e0d\u5b58\u5728" },
			{ "Alias <alias> has no certificate",
					"\u522b\u540d <{0}> \u6ca1\u6709\u8ba4\u8bc1" },
			{
					"Key pair not generated, alias <alias> already exists",
					"\u6ca1\u6709\u521b\u5efa\u952e\u503c\u5bf9\uff0c\u522b\u540d <{0}> \u5df2\u7ecf\u5b58\u5728" },
			{ "Cannot derive signature algorithm",
					"\u65e0\u6cd5\u53d6\u5f97\u7b7e\u540d\u7b97\u6cd5" },
			{
					"Generating keysize bit keyAlgName key pair and self-signed certificate (sigAlgName) with a validity of validality days\n\tfor: x500Name",
					"\u6b63\u5728\u4e3a\u4ee5\u4e0b\u5bf9\u8c61\u751f\u6210 {0} \u4f4d {1} \u5bc6\u94a5\u5bf9\u548c\u81ea\u7b7e\u540d\u8bc1\u4e66 ({2})\uff08\u6709\u6548\u671f\u4e3a {3} \u5929\uff09:\n\t {4}" },
			{ "Enter key password for <alias>",
					"\u8f93\u5165<{0}>\u7684\u4e3b\u5bc6\u7801" },
			{
					"\t(RETURN if same as keystore password):  ",
					"\t\uff08\u5982\u679c\u548c keystore \u5bc6\u7801\u76f8\u540c\uff0c\u6309\u56de\u8f66\uff09\uff1a  " },
			{
					"Key password is too short - must be at least 6 characters",
					"\u4e3b\u5bc6\u7801\u592a\u77ed -\u81f3\u5c11\u5fc5\u987b\u4e3a 6 \u4e2a\u5b57\u7b26" },
			{
					"Too many failures - key not added to keystore",
					"\u592a\u591a\u9519\u8bef - \u952e\u503c\u672a\u88ab\u6dfb\u52a0\u81f3keystore\u4e2d" },
			{ "Destination alias <dest> already exists",
					"\u76ee\u7684\u5730\u522b\u540d <{0}> \u5df2\u7ecf\u5b58\u5728" },
			{ "Password is too short - must be at least 6 characters",
					"\u5bc6\u7801\u592a\u77ed -\u81f3\u5c11\u5fc5\u987b\u4e3a6\u4e2a\u5b57\u7b26" },
			{
					"Too many failures. Key entry not cloned",
					"\u592a\u591a\u9519\u8bef\u3002\u952e\u503c\u8f93\u5165\u672a\u88ab\u590d\u5236" },
			{ "key password for <alias>", "<{0}> \u7684\u4e3b\u5bc6\u7801" },
			{ "Keystore entry for <id.getName()> already exists",
					"<{0}> \u7684 Keystore \u8f93\u5165\u5df2\u7ecf\u5b58\u5728" },
			{ "Creating keystore entry for <id.getName()> ...",
					"\u521b\u5efa <{0}> \u7684 keystore\u8f93\u5165..." },
			{
					"No entries from identity database added",
					"\u4ece\u6dfb\u52a0\u7684\u8fa8\u8bc6\u6570\u636e\u5e93\u4e2d\uff0c\u6ca1\u6709\u8f93\u5165" },
			{ "Alias name: alias", "\u522b\u540d\u540d\u79f0\uff1a {0}" },
			{ "Creation date: keyStore.getCreationDate(alias)",
					"\u521b\u5efa\u65e5\u671f\uff1a {0,date}" },
			{ "alias, keyStore.getCreationDate(alias), ", "{0}, {1,date}, " },
			{ "alias, ", "{0}, " },
			{ "Entry type: <type>", "\u9879\u7c7b\u578b: {0}" },
			{ "Certificate chain length: ",
					"\u8ba4\u8bc1\u94fe\u957f\u5ea6\uff1a " },
			{ "Certificate[(i + 1)]:", "\u8ba4\u8bc1 [{0,number,integer}]:" },
			{ "Certificate fingerprint (MD5): ",
					"\u8ba4\u8bc1\u6307\u7eb9 (MD5)\uff1a " },
			{ "Entry type: trustedCertEntry\n",
					"\u8f93\u5165\u7c7b\u578b\uff1a trustedCertEntry\n" },
			{ "trustedCertEntry,", "trustedCertEntry," },
			{ "Keystore type: ", "Keystore \u7c7b\u578b\uff1a " },
			{ "Keystore provider: ", "Keystore \u63d0\u4f9b\u8005\uff1a " },
			{ "Your keystore contains keyStore.size() entry",
					"\u60a8\u7684 keystore \u5305\u542b {0,number,integer} \u8f93\u5165" },
			{ "Your keystore contains keyStore.size() entries",
					"\u60a8\u7684 keystore \u5305\u542b {0,number,integer} \u8f93\u5165" },
			{ "Failed to parse input",
					"\u65e0\u6cd5\u5bf9\u8f93\u5165\u8fdb\u884c\u8bed\u6cd5\u5206\u6790" },
			{ "Empty input", "\u7a7a\u8f93\u5165" },
			{ "Not X.509 certificate", "\u975e X.509 \u8ba4\u8bc1" },
			{ "Cannot derive signature algorithm",
					"\u65e0\u6cd5\u53d6\u5f97\u7b7e\u540d\u7b97\u6cd5" },
			{ "alias has no public key", "{0} \u65e0\u516c\u7528\u5bc6\u94a5" },
			{ "alias has no X.509 certificate", "{0} \u65e0 X.509 \u8ba4\u8bc1" },
			{ "New certificate (self-signed):",
					"\u65b0\u8ba4\u8bc1\uff08\u81ea\u6211\u7b7e\u7f72\uff09\uff1a" },
			{ "Reply has no certificates",
					"\u56de\u590d\u4e2d\u6ca1\u6709\u8ba4\u8bc1" },
			{
					"Certificate not imported, alias <alias> already exists",
					"\u8ba4\u8bc1\u672a\u8f93\u5165\uff0c\u522b\u540d <{0}> \u5df2\u7ecf\u5b58\u5728" },
			{ "Input not an X.509 certificate",
					"\u6240\u8f93\u5165\u7684\u4e0d\u662f\u4e00\u4e2a X.509 \u8ba4\u8bc1" },
			{
					"Certificate already exists in keystore under alias <trustalias>",
					"\u5728 <{0}> \u7684\u522b\u540d\u4e4b\u4e0b\uff0c\u8ba4\u8bc1\u5df2\u7ecf\u5b58\u5728 keystore \u4e2d" },
			{ "Do you still want to add it? [no]:  ",
					"\u60a8\u4ecd\u7136\u60f3\u8981\u6dfb\u52a0\u5b83\u5417\uff1f [\u5426]\uff1a  " },
			{
					"Certificate already exists in system-wide CA keystore under alias <trustalias>",
					"\u5728 <{0}> \u7684\u522b\u540d\u4e4b\u4e0b\uff0c\u8ba4\u8bc1\u5df2\u7ecf\u5b58\u5728\u4e8e CA keystore \u6574\u4e2a\u7cfb\u7edf\u4e4b\u4e2d" },
			{
					"Do you still want to add it to your own keystore? [no]:  ",
					"\u60a8\u4ecd\u7136\u60f3\u8981\u5c06\u5b83\u6dfb\u52a0\u5230\u81ea\u5df1\u7684keystore \u5417\uff1f [\u5426]\uff1a  " },
			{ "Trust this certificate? [no]:  ",
					"\u4fe1\u4efb\u8fd9\u4e2a\u8ba4\u8bc1\uff1f [\u5426]\uff1a  " },
			{ "YES", "\u662f" },
			{ "New prompt: ", "\u65b0 {0}\uff1a " },
			{ "Passwords must differ",
					"\u5fc5\u987b\u662f\u4e0d\u540c\u7684\u5bc6\u7801" },
			{ "Re-enter new prompt: ",
					"\u91cd\u65b0\u8f93\u5165\u65b0 {0}\uff1a " },
			{ "Re-enter new password: ",
					"\u518d\u6b21\u8f93\u5165\u65b0\u5bc6\u7801: " },
			{ "They don't match. Try again",
					"\u5b83\u4eec\u4e0d\u5339\u914d\u3002\u8bf7\u91cd\u8bd5" },
			{ "Enter prompt alias name:  ",
					"\u8f93\u5165 {0} \u522b\u540d\u540d\u79f0\uff1a  " },
			{
					"Enter new alias name\t(RETURN to cancel import for this entry):  ",
					"\u8f93\u5165\u65b0\u7684\u522b\u540d\t\uff08\u6309\u56de\u8f66\u952e\u4ee5\u53d6\u6d88\u5bf9\u6b64\u9879\u7684\u5bfc\u5165\uff09:  " },
			{ "Enter alias name:  ",
					"\u8f93\u5165\u522b\u540d\u540d\u79f0\uff1a  " },
			{
					"\t(RETURN if same as for <otherAlias>)",
					"\t\uff08\u5982\u679c\u548c <{0}> \u7684\u76f8\u540c\uff0c\u6309\u56de\u8f66\uff09" },
			{
					"*PATTERN* printX509Cert",
					"\u6240\u6709\u8005:{0}\n\u7b7e\u53d1\u4eba:{1}\n\u5e8f\u5217\u53f7:{2}\n\u6709\u6548\u671f: {3} \u81f3{4}\n\u8bc1\u4e66\u6307\u7eb9:\n\t MD5:{5}\n\t SHA1:{6}\n\t \u7b7e\u540d\u7b97\u6cd5\u540d\u79f0:{7}\n\t \u7248\u672c: {8}" },
			{ "What is your first and last name?",
					"\u60a8\u7684\u540d\u5b57\u4e0e\u59d3\u6c0f\u662f\u4ec0\u4e48\uff1f" },
			{ "What is the name of your organizational unit?",
					"\u60a8\u7684\u7ec4\u7ec7\u5355\u4f4d\u540d\u79f0\u662f\u4ec0\u4e48\uff1f" },
			{ "What is the name of your organization?",
					"\u60a8\u7684\u7ec4\u7ec7\u540d\u79f0\u662f\u4ec0\u4e48\uff1f" },
			{
					"What is the name of your City or Locality?",
					"\u60a8\u6240\u5728\u7684\u57ce\u5e02\u6216\u533a\u57df\u540d\u79f0\u662f\u4ec0\u4e48\uff1f" },
			{
					"What is the name of your State or Province?",
					"\u60a8\u6240\u5728\u7684\u5dde\u6216\u7701\u4efd\u540d\u79f0\u662f\u4ec0\u4e48\uff1f" },
			{
					"What is the two-letter country code for this unit?",
					"\u8be5\u5355\u4f4d\u7684\u4e24\u5b57\u6bcd\u56fd\u5bb6\u4ee3\u7801\u662f\u4ec0\u4e48" },
			{ "Is <name> correct?", "{0} \u6b63\u786e\u5417\uff1f" },
			{ "no", "\u5426" },
			{ "yes", "\u662f" },
			{ "y", "y" },
			{ "  [defaultValue]:  ", "  [{0}]\uff1a  " },
			{ "Alias <alias> has no key",
					"\u522b\u540d <{0}> \u6ca1\u6709\u5bc6\u94a5" },
			{
					"Alias <alias> references an entry type that is not a private key entry.  The -keyclone command only supports cloning of private key entries",
					"\u522b\u540d <{0}> \u5f15\u7528\u4e86\u4e0d\u5c5e\u4e8e\u4e13\u7528\u5bc6\u94a5\u9879\u7684\u9879\u7c7b\u578b\u3002-keyclone \u547d\u4ee4\u4ec5\u652f\u6301\u5bf9\u4e13\u7528\u5bc6\u94a5\u9879\u7684\u514b\u9686" },

			{ "*****************  WARNING WARNING WARNING  *****************",
					"***************** \u8b66\u544a \u8b66\u544a \u8b66\u544a  *****************" },

			// Translators of the following 5 pairs, ATTENTION:
			// the next 5 string pairs are meant to be combined into 2
			// paragraphs,
			// 1+3+4 and 2+3+5. make sure your translation also does.
			{
					"* The integrity of the information stored in your keystore  *",
					"*\u4fdd\u5b58\u5728\u60a8\u7684 keystore \u4e2d\u6570\u636e\u7684\u5b8c\u6574\u6027  *" },
			{
					"* The integrity of the information stored in the srckeystore*",
					"* srckeystore \u4e2d\u6240\u5b58\u50a8\u7684\u4fe1\u606f\u7684\u5b8c\u6574\u6027*" },
			{
					"* has NOT been verified!  In order to verify its integrity, *",
					"* \u5c1a\u672a\u88ab\u9a8c\u8bc1\uff01  \u4e3a\u4e86\u9a8c\u8bc1\u5176\u5b8c\u6574\u6027\uff0c *" },
			{
					"* you must provide your keystore password.                  *",
					"* \u60a8\u5fc5\u987b\u63d0\u4f9b\u60a8 keystore \u7684\u5bc6\u7801\u3002                  *" },
			{
					"* you must provide the srckeystore password.                *",
					"* \u60a8\u5fc5\u987b\u63d0\u4f9b\u6e90\u5bc6\u94a5\u5e93\u53e3\u4ee4\u3002                *" },

			{
					"Certificate reply does not contain public key for <alias>",
					"\u8ba4\u8bc1\u56de\u590d\u5e76\u672a\u5305\u542b <{0}> \u7684\u516c\u7528\u5bc6\u94a5" },
			{ "Incomplete certificate chain in reply",
					"\u56de\u590d\u4e2d\u7684\u8ba4\u8bc1\u94fe\u4e0d\u5b8c\u6574" },
			{ "Certificate chain in reply does not verify: ",
					"\u56de\u590d\u4e2d\u7684\u8ba4\u8bc1\u94fe\u672a\u9a8c\u8bc1\uff1a " },
			{ "Top-level certificate in reply:\n",
					"\u56de\u590d\u4e2d\u7684\u6700\u9ad8\u7ea7\u8ba4\u8bc1\uff1a\n" },
			{ "... is not trusted. ",
					"... \u662f\u4e0d\u53ef\u4fe1\u7684\u3002 " },
			{ "Install reply anyway? [no]:  ",
					"\u8fd8\u662f\u8981\u5b89\u88c5\u56de\u590d\uff1f [\u5426]\uff1a  " },
			{ "NO", "\u5426" },
			{ "Public keys in reply and keystore don't match",
					"\u56de\u590d\u4e2d\u7684\u516c\u7528\u5bc6\u94a5\u4e0e keystore \u4e0d\u7b26" },
			{
					"Certificate reply and certificate in keystore are identical",
					"\u8ba4\u8bc1\u56de\u590d\u4e0ekeystore\u4e2d\u7684\u8ba4\u8bc1\u662f\u76f8\u540c\u7684" },
			{ "Failed to establish chain from reply",
					"\u65e0\u6cd5\u4ece\u56de\u590d\u4e2d\u5efa\u7acb\u94fe\u63a5" },
			{ "n", "n" },
			{ "Wrong answer, try again",
					"\u9519\u8bef\u7684\u7b54\u6848\uff0c\u8bf7\u518d\u8bd5\u4e00\u6b21" },
			{
					"Secret key not generated, alias <alias> already exists",
					"\u6ca1\u6709\u751f\u6210\u5bc6\u94a5\uff0c\u522b\u540d <{0}> \u5df2\u7ecf\u5b58\u5728" },
			{ "Please provide -keysize for secret key generation",
					"\u8bf7\u63d0\u4f9b -keysize \u4ee5\u751f\u6210\u5bc6\u94a5" },
			{ "keytool usage:\n", "keytool \u7528\u6cd5\uff1a\n" },

			{ "Extensions: ", "\u6269\u5c55: " },

			{ "-certreq     [-v] [-protected]",
					"-certreq     [-v] [-protected]" },
			{ "\t     [-alias <alias>] [-sigalg <sigalg>]",
					"\t     [-alias <\u522b\u540d>] [-sigalg <sigalg>]" },
			{ "\t     [-file <csr_file>] [-keypass <keypass>]",
					"\t     [-file <csr_file>] [-keypass <\u5bc6\u94a5\u5e93\u53e3\u4ee4>]" },
			{
					"\t     [-keystore <keystore>] [-storepass <storepass>]",
					"\t     [-keystore <\u5bc6\u94a5\u5e93>] [-storepass <\u5b58\u50a8\u5e93\u53e3\u4ee4>]" },
			{ "\t     [-storetype <storetype>] [-providername <name>]",
					"\t     [-storetype <\u5b58\u50a8\u7c7b\u578b>] [-providername <\u540d\u79f0>]" },
			{
					"\t     [-providerclass <provider_class_name> [-providerarg <arg>]] ...",
					"\t     [-providerclass <\u63d0\u4f9b\u65b9\u7c7b\u540d\u79f0> [-providerarg <\u53c2\u6570>]] ..." },
			{ "\t     [-providerpath <pathlist>]",
					"\t     [-providerpath <\u8def\u5f84\u5217\u8868>]" },
			{ "-delete      [-v] [-protected] -alias <alias>",
					"-delete      [-v] [-protected] -alias <\u522b\u540d>" },
			/** rest is same as -certreq starting from -keystore **/

			// {"-export      [-v] [-rfc] [-protected]",
			// "-export      [-v] [-rfc] [-protected]"},
			{ "-exportcert  [-v] [-rfc] [-protected]",
					"-exportcert  [-v] [-rfc] [-protected]" },
			{ "\t     [-alias <alias>] [-file <cert_file>]",
					"\t     [-alias <\u522b\u540d>] [-file <\u8ba4\u8bc1\u6587\u4ef6>]" },
			/** rest is same as -certreq starting from -keystore **/

			// {"-genkey      [-v] [-protected]",
			// "-genkey      [-v] [-protected]"},
			{ "-genkeypair  [-v] [-protected]",
					"-genkeypair  [-v] [-protected]" },
			{ "\t     [-alias <alias>]", "\t     [-alias <\u522b\u540d>]" },
			{ "\t     [-keyalg <keyalg>] [-keysize <keysize>]",
					"\t     [-keyalg <keyalg>] [-keysize <\u5bc6\u94a5\u5927\u5c0f>]" },
			{ "\t     [-sigalg <sigalg>] [-dname <dname>]",
					"\t     [-sigalg <sigalg>] [-dname <dname>]" },
			{ "\t     [-validity <valDays>] [-keypass <keypass>]",
					"\t     [-validity <valDays>] [-keypass <\u5bc6\u94a5\u5e93\u53e3\u4ee4>]" },
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
			{ "\t     [-alias <alias>]", "\t     [-alias <\u522b\u540d>]" },
			{ "\t     [-alias <alias>] [-keypass <keypass>]",
					"\t     [-alias <\u522b\u540d>] [-keypass <\u5bc6\u94a5\u5e93\u53e3\u4ee4>]" },
			{
					"\t     [-file <cert_file>] [-keypass <keypass>]",
					"\t     [-file <\u8ba4\u8bc1\u6587\u4ef6>] [-keypass <\u5bc6\u94a5\u5e93\u53e3\u4ee4>]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-importkeystore [-v] ", "-importkeystore [-v] " },
			{
					"\t     [-srckeystore <srckeystore>] [-destkeystore <destkeystore>]",
					"\t     [-srckeystore <\u6e90\u5bc6\u94a5\u5e93>] [-destkeystore <\u76ee\u6807\u5bc6\u94a5\u5e93>]" },
			{
					"\t     [-srcstoretype <srcstoretype>] [-deststoretype <deststoretype>]",
					"\t     [-srcstoretype <\u6e90\u5b58\u50a8\u7c7b\u578b>] [-deststoretype <\u76ee\u6807\u5b58\u50a8\u7c7b\u578b>]" },
			{ "\t     [-srcprotected] [-destprotected]",
					"\t     [-srcprotected] [-destprotected]" },
			{
					"\t     [-srcstorepass <srcstorepass>] [-deststorepass <deststorepass>]",
					"\t     [-srcstorepass <\u6e90\u5b58\u50a8\u5e93\u53e3\u4ee4>] [-deststorepass <\u76ee\u6807\u5b58\u50a8\u5e93\u53e3\u4ee4>]" },
			{
					"\t     [-srcprovidername <srcprovidername>]\n\t     [-destprovidername <destprovidername>]", // \u884c\u592a\u957f\uff0c\u5206\u4e3a\u4e24\u884c
					"\t     [-srcprovidername <\u6e90\u63d0\u4f9b\u65b9\u540d\u79f0>]\n\t     [-destprovidername <\u76ee\u6807\u63d0\u4f9b\u65b9\u540d\u79f0>]" },
			{
					"\t     [-srcalias <srcalias> [-destalias <destalias>]",
					"\t     [-srcalias <\u6e90\u522b\u540d> [-destalias <\u76ee\u6807\u522b\u540d>]" },
			{
					"\t       [-srckeypass <srckeypass>] [-destkeypass <destkeypass>]]",
					"\t       [-srckeypass <\u6e90\u5bc6\u94a5\u5e93\u53e3\u4ee4>] [-destkeypass <\u76ee\u6807\u5bc6\u94a5\u5e93\u53e3\u4ee4>]]" },
			{ "\t     [-noprompt]", "\t     [-noprompt]" },
			/** rest is same as -certreq starting from -keystore **/

			{
					"-changealias [-v] [-protected] -alias <alias> -destalias <destalias>",
					"-changealias [-v] [-protected] -alias <\u522b\u540d> -destalias <\u76ee\u6807\u522b\u540d>" },
			{ "\t     [-keypass <keypass>]",
					"\t     [-keypass <\u5bc6\u94a5\u5e93\u53e3\u4ee4>]" },

			// {"-keyclone    [-v] [-protected]",
			// "-keyclone    [-v] [-protected]"},
			// {"\t     [-alias <alias>] -dest <dest_alias>",
			// "\t     [-alias <alias>] -dest <dest_alias>"},
			// {"\t     [-keypass <keypass>] [-new <new_keypass>]",
			// "\t     [-keypass <keypass>] [-new <new_keypass>]"},
			/** rest is same as -certreq starting from -keystore **/

			{ "-keypasswd   [-v] [-alias <alias>]",
					"-keypasswd   [-v] [-alias <\u522b\u540d>]" },
			{
					"\t     [-keypass <old_keypass>] [-new <new_keypass>]",
					"\t     [-keypass <\u65e7\u5bc6\u94a5\u5e93\u53e3\u4ee4>] [-new <\u65b0\u5bc6\u94a5\u5e93\u53e3\u4ee4>]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-list        [-v | -rfc] [-protected]",
					"-list        [-v | -rfc] [-protected]" },
			{ "\t     [-alias <alias>]", "\t     [-alias <\u522b\u540d>]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-printcert   [-v] [-file <cert_file>]",
					"-printcert   [-v] [-file <\u8ba4\u8bc1\u6587\u4ef6>]" },

			// {"-selfcert    [-v] [-protected]",
			// "-selfcert    [-v] [-protected]"},
			{ "\t     [-alias <alias>]", "\t     [-alias <\u522b\u540d>]" },
			// {"\t     [-dname <dname>] [-validity <valDays>]",
			// "\t     [-dname <dname>] [-validity <valDays>]"},
			// {"\t     [-keypass <keypass>] [-sigalg <sigalg>]",
			// "\t     [-keypass <\u5bc6\u94a5\u5e93\u53e3\u4ee4>] [-sigalg <sigalg>]"},
			/** rest is same as -certreq starting from -keystore **/

			{ "-storepasswd [-v] [-new <new_storepass>]",
					"-storepasswd [-v] [-new <\u65b0\u5b58\u50a8\u5e93\u53e3\u4ee4>]" },
			/** rest is same as -certreq starting from -keystore **/

			// policytool
			{
					"Warning: A public key for alias 'signers[i]' does not exist.  Make sure a KeyStore is properly configured.",
					"\u8b66\u544a: \u522b\u540d {0} \u7684\u516c\u7528\u5bc6\u94a5\u4e0d\u5b58\u5728\u3002\u8bf7\u786e\u4fdd\u5df2\u6b63\u786e\u914d\u7f6e\u5bc6\u94a5\u5e93\u3002" },
			{ "Warning: Class not found: class",
					"\u8b66\u544a: \u627e\u4e0d\u5230\u7c7b: {0}" },
			{ "Warning: Invalid argument(s) for constructor: arg",
					"\u8b66\u544a: \u6784\u9020\u51fd\u6570\u7684\u53c2\u6570\u65e0\u6548: {0}" },
			{ "Illegal Principal Type: type",
					"\u975e\u6cd5\u7684 Principal \u7c7b\u578b: {0}" },
			{ "Illegal option: option", "\u975e\u6cd5\u9009\u9879: {0}" },
			{ "Usage: policytool [options]",
					"\u7528\u6cd5\uff1a policytool [\u9009\u9879]" },
			{ "  [-file <file>]    policy file location",
					"  [-file <file>]    \u89c4\u5219\u6587\u4ef6\u4f4d\u7f6e" },
			{ "New", "\u65b0\u6587\u4ef6" },
			{ "Open", "\u6253\u5f00" },
			{ "Save", "\u4fdd\u5b58" },
			{ "Save As", "\u53e6\u5b58\u4e3a" },
			{ "View Warning Log", "\u67e5\u770b\u8b66\u544a\u8bb0\u5f55" },
			{ "Exit", "\u9000\u51fa" },
			{ "Add Policy Entry", "\u6dfb\u52a0\u89c4\u5219\u9879\u76ee" },
			{ "Edit Policy Entry", "\u7f16\u8f91\u89c4\u5219\u9879\u76ee" },
			{ "Remove Policy Entry", "\u5220\u9664\u89c4\u5219\u9879\u76ee" },
			{ "Edit", "\u7f16\u8f91" },
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
					"\u6dfb\u52a0\u516c\u7528\u5bc6\u94a5\u522b\u540d" },
			{ "Remove Public Key Alias",
					"\u5220\u9664\u516c\u7528\u5bc6\u94a5\u522b\u540d" },
			{ "File", "\u6587\u4ef6" },
			{ "KeyStore", "\u5bc6\u94a5\u5e93" },
			{ "Policy File:", "\u89c4\u5219\u6587\u4ef6\uff1a" },
			{ "Could not open policy file: policyFile: e.toString()",
					"\u65e0\u6cd5\u6253\u5f00\u7b56\u7565\u6587\u4ef6: {0}: {1}" },
			{ "Policy Tool", "\u89c4\u5219\u5de5\u5177" },
			{
					"Errors have occurred while opening the policy configuration.  View the Warning Log for more information.",
					"\u6253\u5f00\u89c4\u5219\u914d\u7f6e\u65f6\u53d1\u751f\u9519\u8bef\u3002 \u8bf7\u67e5\u770b\u8b66\u544a\u8bb0\u5f55\u83b7\u53d6\u66f4\u591a\u4fe1\u606f" },
			{ "Error", "\u9519\u8bef" },
			{ "OK", "\u786e\u8ba4" },
			{ "Status", "\u72b6\u6001" },
			{ "Warning", "\u8b66\u544a" },
			{
					"Permission:                                                       ",
					"\u8bb8\u53ef\uff1a                                                       " },
			{ "Principal Type:", "Principal \u7c7b\u578b\uff1a" },
			{ "Principal Name:", "Principal \u540d\u79f0\uff1a" },
			{
					"Target Name:                                                    ",
					"\u76ee\u6807\u540d\u79f0\uff1a                                                    " },
			{
					"Actions:                                                             ",
					"\u52a8\u4f5c\uff1a                                                             " },
			{ "OK to overwrite existing file filename?",
					"\u786e\u8ba4\u66ff\u6362\u73b0\u6709\u7684\u6587\u4ef6 {0}\uff1f" },
			{ "Cancel", "\u53d6\u6d88" },
			{ "CodeBase:", "CodeBase:" },
			{ "SignedBy:", "SignedBy:" },
			{ "Add Principal", "\u6dfb\u52a0 Principal" },
			{ "Edit Principal", "\u7f16\u8f91 Principal" },
			{ "Remove Principal", "\u5220\u9664 Principal" },
			{ "Principals:", "Principals\uff1a" },
			{ "  Add Permission", "  \u6dfb\u52a0\u6743\u9650" },
			{ "  Edit Permission", "  \u7f16\u8f91\u6743\u9650" },
			{ "Remove Permission", "\u5220\u9664\u6743\u9650" },
			{ "Done", "\u5b8c\u6210" },
			{ "KeyStore URL:", "\u5bc6\u94a5\u5e93 URL:" },
			{ "KeyStore Type:", "\u5bc6\u94a5\u5e93\u7c7b\u578b:" },
			{ "KeyStore Provider:", "\u5bc6\u94a5\u5e93\u63d0\u4f9b\u8005:" },
			{ "KeyStore Password URL:", "\u5bc6\u94a5\u5e93\u53e3\u4ee4 URL:" },
			{ "Principals", "Principals" },
			{ "  Edit Principal:", "  \u7f16\u8f91 Principal\uff1a" },
			{ "  Add New Principal:", "  \u52a0\u5165\u65b0 Principal\uff1a" },
			{ "Permissions", "\u6743\u9650" },
			{ "  Edit Permission:", "  \u7f16\u8f91\u6743\u9650" },
			{ "  Add New Permission:", "  \u52a0\u5165\u65b0\u7684\u6743\u9650" },
			{ "Signed By:", "\u7b7e\u7f72\u4eba\uff1a" },
			{
					"Cannot Specify Principal with a Wildcard Class without a Wildcard Name",
					"\u6ca1\u6709\u901a\u914d\u7b26\u540d\u79f0\uff0c\u65e0\u6cd5\u7528\u901a\u914d\u7b26\u7c7b\u522b\u6307\u5b9aPrincipal" },
			{ "Cannot Specify Principal without a Name",
					"\u6ca1\u6709\u540d\u79f0\uff0c\u65e0\u6cd5\u6307\u5b9a Principal" },
			{
					"Permission and Target Name must have a value",
					"\u6743\u9650\u53ca\u76ee\u6807\u540d\u5fc5\u987b\u6709\u4e00\u4e2a\u503c\u3002" },
			{ "Remove this Policy Entry?",
					"\u5220\u9664\u6b64\u89c4\u5219\u9879\uff1f" },
			{ "Overwrite File", "\u66ff\u6362\u6587\u4ef6" },
			{ "Policy successfully written to filename",
					"\u89c4\u5219\u6210\u529f\u5199\u81f3 {0}" },
			{ "null filename", "\u65e0\u6548\u7684\u6587\u4ef6\u540d" },
			{ "Save changes?",
					"\u662f\u5426\u4fdd\u5b58\u6240\u505a\u7684\u66f4\u6539\uff1f" },
			{ "Yes", "\u662f" },
			{ "No", "\u5426" },
			{ "Policy Entry", "\u89c4\u5219\u9879\u76ee" },
			{ "Save Changes", "\u4fdd\u5b58\u4fee\u6539" },
			{ "No Policy Entry selected",
					"\u6ca1\u6709\u9009\u62e9\u89c4\u5219\u9879\u76ee" },
			{ "Unable to open KeyStore: ex.toString()",
					"\u65e0\u6cd5\u6253\u5f00\u5bc6\u94a5\u5e93: {0}" },
			{ "No principal selected", "\u672a\u9009\u62e9 Principal" },
			{ "No permission selected", "\u6ca1\u6709\u9009\u62e9\u6743\u9650" },
			{ "name", "\u540d\u79f0" },
			{ "configuration type", "\u914d\u7f6e\u7c7b\u578b" },
			{ "environment variable name", "\u73af\u5883\u53d8\u91cf\u540d" },
			{ "library name", "\u7a0b\u5e8f\u5e93\u540d\u79f0" },
			{ "package name", "\u8f6f\u4ef6\u5305\u540d\u79f0" },
			{ "policy type", "\u7b56\u7565\u7c7b\u578b" },
			{ "property name", "\u5c5e\u6027\u540d\u79f0" },
			{ "provider name", "\u63d0\u4f9b\u8005\u540d\u79f0" },
			{ "Principal List", "Principal \u5217\u8868" },
			{ "Permission List", "\u6743\u9650\u5217\u8868" },
			{ "Code Base", "Code Base\uff08\u4ee3\u7801\u5e93\uff09" },
			{ "KeyStore U R L:", "\u5bc6\u94a5\u5e93 URL:" },
			{ "KeyStore Password U R L:", "\u5bc6\u94a5\u5e93\u53e3\u4ee4 URL:" },

			// javax.security.auth.PrivateCredentialPermission
			{ "invalid null input(s)", "\u65e0\u6548\u7a7a\u8f93\u5165" },
			{ "actions can only be 'read'",
					"\u52a8\u4f5c\u53ea\u80fd\u88ab\u2018\u8bfb\u53d6'" },
			{ "permission name [name] syntax invalid: ",
					"\u6743\u9650\u540d\u79f0 [{0}]\u8bed\u6cd5\u65e0\u6548\uff1a " },
			{
					"Credential Class not followed by a Principal Class and Name",
					"\u8ba4\u8bc1\u7b49\u7ea7\u540e\u672a\u52a0\u4e0aPrincipal \u7c7b\u522b\u53ca\u540d\u79f0" },
			{ "Principal Class not followed by a Principal Name",
					"Principal \u7c7b\u522b\u540e\u9762\u6ca1\u52a0\u4e0aPrincipal \u540d\u79f0" },
			{ "Principal Name must be surrounded by quotes",
					"Principal \u540d\u79f0\u5fc5\u987b\u653e\u5728\u5f15\u53f7\u5185" },
			{ "Principal Name missing end quote",
					"Principal \u540d\u79f0\u7f3a\u5c11\u4e0b\u5f15\u53f7" },
			{
					"PrivateCredentialPermission Principal Class can not be a wildcard (*) value if Principal Name is not a wildcard (*) value",
					"\u5982\u679c Principal \u540d\u79f0\u4e0d\u662f\u4e00\u4e2a\u901a\u914d\u7b26 (*) \u503c\uff0c\u90a3\u4e48 PrivateCredentialPermission Principal \u7c7b\u522b\u5c31\u4e0d\u4f1a\u662f\u4e00\u4e2a\u901a\u914d\u7b26 (*) \u503c" },
			{ "CredOwner:\n\tPrincipal Class = class\n\tPrincipal Name = name",
					"CredOwner:\n\tPrincipal \u7c7b\u522b = {0}\n\tPrincipal \u540d\u79f0 = {1}" },

			// javax.security.auth.x500
			{ "provided null name", "\u6240\u4f9b\u540d\u79f0\u65e0\u6548" },
			{ "provided null keyword map",
					"\u63d0\u4f9b\u4e86\u7a7a\u5173\u952e\u5b57\u6620\u5c04" },
			{ "provided null OID map",
					"\u63d0\u4f9b\u4e86\u7a7a OID \u6620\u5c04" },

			// javax.security.auth.Subject
			{ "invalid null AccessControlContext provided",
					"\u63d0\u4f9b\u65e0\u6548\u7684\u7a7a AccessControlContext" },
			{ "invalid null action provided",
					"\u63d0\u4f9b\u4e86\u65e0\u6548\u7684\u7a7a\u52a8\u4f5c" },
			{ "invalid null Class provided",
					"\u63d0\u4f9b\u4e86\u65e0\u6548\u7684\u7a7a\u7c7b\u522b" },
			{ "Subject:\n", "\u4e3b\u9898\uff1a\n" },
			{ "\tPrincipal: ", "\tPrincipal: " },
			{ "\tPublic Credential: ", "\t\u516c\u7528\u8ba4\u8bc1 " },
			{ "\tPrivate Credentials inaccessible\n",
					"\t\u65e0\u6cd5\u8bbf\u95ee\u79c1\u4eba\u8ba4\u8bc1\n" },
			{ "\tPrivate Credential: ", "\t\u79c1\u4eba\u8ba4\u8bc1 " },
			{ "\tPrivate Credential inaccessible\n",
					"\t\u65e0\u6cd5\u8bbf\u95ee\u79c1\u4eba\u8ba4\u8bc1\n" },
			{ "Subject is read-only", "\u4e3b\u9898\u4e3a\u53ea\u8bfb" },
			{
					"attempting to add an object which is not an instance of java.security.Principal to a Subject's Principal Set",
					"\u8bd5\u56fe\u5c06\u4e00\u4e2a\u975e java.security.Principal \u5b9e\u4f8b\u7684\u5bf9\u8c61\u6dfb\u52a0\u81f3\u4e3b\u9898\u7684 Principal \u96c6\u4e2d" },
			{ "attempting to add an object which is not an instance of class",
					"\u8bd5\u56fe\u6dfb\u52a0\u4e00\u4e2a\u975e {0} \u5b9e\u4f8b\u7684\u5bf9\u8c61" },

			// javax.security.auth.login.AppConfigurationEntry
			{ "LoginModuleControlFlag: ", "LoginModuleControlFlag: " },

			// javax.security.auth.login.LoginContext
			{ "Invalid null input: name",
					"\u65e0\u6548\u7a7a\u8f93\u5165\uff1a\u540d\u79f0" },
			{ "No LoginModules configured for name",
					"\u6ca1\u6709\u4e3a {0} \u914d\u7f6eLoginModules" },
			{ "invalid null Subject provided",
					"\u63d0\u4f9b\u4e86\u65e0\u6548\u7a7a\u4e3b\u9898" },
			{ "invalid null CallbackHandler provided",
					"\u63d0\u4f9b\u4e86\u65e0\u6548\u7684\u7a7a CallbackHandler" },
			{
					"null subject - logout called before login",
					"\u65e0\u6548\u4e3b\u9898 - \u5728\u767b\u5f55\u4e4b\u524d\u5373\u88ab\u8c03\u7528\u6ce8\u9500" },
			{
					"unable to instantiate LoginModule, module, because it does not provide a no-argument constructor",
					"\u65e0\u6cd5\u4f8b\u793a LoginModule\uff0c {0}\uff0c\u56e0\u4e3a\u5b83\u5e76\u672a\u63d0\u4f9b\u4e00\u4e2a\u4e0d\u542b\u53c2\u6570\u7684\u6784\u9020\u7a0b\u5e8f" },
			{ "unable to instantiate LoginModule",
					"\u65e0\u6cd5\u4f8b\u793a LoginModule" },
			{ "unable to instantiate LoginModule: ",
					"\u65e0\u6cd5\u5b9e\u4f8b\u5316 LoginModule: " },
			{ "unable to find LoginModule class: ",
					"\u65e0\u6cd5\u627e\u5230 LoginModule \u7c7b\u522b\uff1a " },
			{ "unable to access LoginModule: ",
					"\u65e0\u6cd5\u8bbf\u95ee LoginModule: " },
			{ "Login Failure: all modules ignored",
					"\u767b\u5f55\u5931\u8d25\uff1a\u5ffd\u7565\u6240\u6709\u6a21\u5757" },

			// sun.security.provider.PolicyFile

			{ "java.security.policy: error parsing policy:\n\tmessage",
					"java.security.policy: \u89e3\u6790\u9519\u8bef {0}\uff1a\n\t{1}" },
			{
					"java.security.policy: error adding Permission, perm:\n\tmessage",
					"java.security.policy: \u6dfb\u52a0\u6743\u9650\u9519\u8bef {0}\uff1a\n\t{1}" },
			{ "java.security.policy: error adding Entry:\n\tmessage",
					"java.security.policy: \u6dfb\u52a0\u9879\u76ee\u9519\u8bef\uff1a\n\t{0}" },
			{ "alias name not provided (pe.name)",
					"\u672a\u63d0\u4f9b\u522b\u540d ({0})" },
			{ "unable to perform substitution on alias, suffix",
					"\u4e0d\u80fd\u5728\u522b\u540d\u4e0a\u6267\u884c\u66ff\u4ee3\uff0c {0}" },
			{ "substitution value, prefix, unsupported",
					"\u66ff\u4ee3\u503c {0} \u4e0d\u53d7\u652f\u6301" },
			{ "(", "(" },
			{ ")", ")" },
			{ "type can't be null",
					"\u4e0d\u80fd\u4e3a\u65e0\u6548\u7c7b\u578b" },

			// sun.security.provider.PolicyParser
			{
					"keystorePasswordURL can not be specified without also specifying keystore",
					"\u4e0d\u6307\u5b9a keystore \u65f6\u65e0\u6cd5\u6307\u5b9a keystorePasswordURL" },
			{ "expected keystore type",
					"\u9884\u671f\u7684 keystore \u7c7b\u578b" },
			{ "expected keystore provider",
					"\u9884\u671f\u7684 keystore \u63d0\u4f9b\u8005" },
			{ "multiple Codebase expressions",
					"\u591a\u79cd Codebase \u8868\u8fbe\u5f0f" },
			{ "multiple SignedBy expressions",
					"\u591a\u79cd SignedBy \u8868\u8fbe\u5f0f" },
			{ "SignedBy has empty alias", "SignedBy \u6709\u7a7a\u522b\u540d" },
			{
					"can not specify Principal with a wildcard class without a wildcard name",
					"\u6ca1\u6709\u901a\u914d\u7b26\u540d\u79f0\uff0c\u65e0\u6cd5\u7528\u901a\u914d\u7b26\u7c7b\u522b\u6307\u5b9aPrincipal" },
			{ "expected codeBase or SignedBy or Principal",
					"\u9884\u671f\u7684 codeBase \u6216 SignedBy \u6216 Principal" },
			{ "expected permission entry",
					"\u9884\u671f\u7684\u6743\u9650\u9879\u76ee" },
			{ "number ", "\u53f7\u7801" },
			{ "expected [expect], read [end of file]",
					"\u9884\u671f\u7684 [{0}], \u8bfb\u53d6 [end of file]" },
			{ "expected [;], read [end of file]",
					"\u9884\u671f\u7684 [;], \u8bfb\u53d6[end of file]" },
			{ "line number: msg", "\u5217 {0}\uff1a {1}" },
			{ "line number: expected [expect], found [actual]",
					"\u884c\u53f7 {0}\uff1a\u9884\u671f\u7684 [{1}]\uff0c\u627e\u5230 [{2}]" },
			{ "null principalClass or principalName",
					"\u65e0\u6548 principalClass \u6216 principalName" },

			// sun.security.pkcs11.SunPKCS11
			{ "PKCS11 Token [providerName] Password: ",
					"PKCS11 Token [{0}] \u5bc6\u7801: " },

			/* --- DEPRECATED --- */
			// javax.security.auth.Policy
			{ "unable to instantiate Subject-based policy",
					"\u65e0\u6cd5\u5b9e\u4f8b\u5316\u57fa\u4e8e\u4e3b\u9898\u7684\u7b56\u7565" } };

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
