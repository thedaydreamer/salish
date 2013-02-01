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
public class Resources_sv extends java.util.ListResourceBundle {

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
			{ "keytool error: ", "nyckelverktygsfel: " },
			{ "Illegal option:  ", "Ogiltigt alternativ:  " },
			{ "Try keytool -help", "Try keytool -help" },
			{ "Command option <flag> needs an argument.",
					"Kommandoalternativet {0} beh\u00f6ver ett argument." },
			{
					"Warning:  Different store and key passwords not supported for PKCS12 KeyStores. Ignoring user-specified <command> value.",
					"Varning!  PKCS12 KeyStores har inte st\u00f6d f\u00f6r olika l\u00f6senord f\u00f6r lagret och nyckeln. Det anv\u00e4ndarspecificerade {0}-v\u00e4rdet ignoreras." },
			{ "-keystore must be NONE if -storetype is {0}",
					"-keystore m\u00e5ste vara NONE om -storetype \u00e4r {0}" },
			{ "Too may retries, program terminated",
					"F\u00f6r m\u00e5nga f\u00f6rs\u00f6k. Programmet avslutas." },
			{
					"-storepasswd and -keypasswd commands not supported if -storetype is {0}",
					"-storepasswd- och -keypasswd-kommandon st\u00f6ds inte om -storetype \u00e4r {0}" },
			{
					"-keypasswd commands not supported if -storetype is PKCS12",
					" \u0096keypasswd-kommandon st\u00f6ds inte om -storetype \u00e4r inst\u00e4lld p\u00e5 PKCS12" },
			{ "-keypass and -new can not be specified if -storetype is {0}",
					"-keypass och -new kan inte anges om -storetype \u00e4r {0}" },
			{
					"if -protected is specified, then -storepass, -keypass, and -new must not be specified",
					"om -protected har angetts f\u00e5r inte -storepass, -keypass och -new anges" },
			{
					"if -srcprotected is specified, then -srcstorepass and -srckeypass must not be specified",
					"om -srcprotected anges f\u00e5r -srcstorepass och -srckeypass inte anges" },
			{
					"if keystore is not password protected, then -storepass, -keypass, and -new must not be specified",
					"om nyckelfilen inte \u00e4r l\u00f6senordsskyddad f\u00e5r -storepass, -keypass och -new inte anges" },
			{
					"if source keystore is not password protected, then -srcstorepass and -srckeypass must not be specified",
					"om k\u00e4llnyckelfilen inte \u00e4r l\u00f6senordsskyddad f\u00e5r -srcstorepass och -srckeypass inte anges" },
			{ "Validity must be greater than zero",
					"Giltigheten m\u00e5ste vara st\u00f6rre \u00e4n noll" },
			{ "provName not a provider", "{0} inte en leverant\u00f6r" },
			{ "Usage error: no command provided",
					"Anv\u00e4ndningsfel: inget kommando angivet" },
			{ "Usage error, <arg> is not a legal command",
					"Anv\u00e4ndningsfel: {0} \u00e4r inte ett giltigt kommando" },
			{ "Source keystore file exists, but is empty: ",
					"Nyckellagrets k\u00e4llfil finns, men \u00e4r tom: " },
			{ "Please specify -srckeystore", "Ange -srckeystore" },
			{ "Must not specify both -v and -rfc with 'list' command",
					"Det g\u00e5r inte att specificera b\u00e5de -v och -rfc med 'list'-kommandot" },
			{ "Key password must be at least 6 characters",
					"Nyckell\u00f6senordet m\u00e5ste inneh\u00e5lla minst 6 tecken" },
			{ "New password must be at least 6 characters",
					"Det nya l\u00f6senordet m\u00e5ste inneh\u00e5lla minst 6 tecken" },
			{ "Keystore file exists, but is empty: ",
					"Keystore-filen finns, men \u00e4r tom: " },
			{ "Keystore file does not exist: ", "Keystore-filen finns inte: " },
			{ "Must specify destination alias",
					"Du m\u00e5ste ange destinations-alias" },
			{ "Must specify alias", "Du m\u00e5ste ange alias" },
			{ "Keystore password must be at least 6 characters",
					"Keystore-l\u00f6senordet m\u00e5ste inneh\u00e5lla minst 6 tecken" },
			{ "Enter keystore password:  ", "Ange keystore-l\u00f6senord:  " },
			{ "Enter source keystore password:  ",
					"Ange l\u00f6senord f\u00f6r k\u00e4llnyckellagret:  " },
			{ "Enter destination keystore password:  ",
					"Ange destination f\u00f6r nyckellagrets l\u00f6senord:  " },
			{
					"Keystore password is too short - must be at least 6 characters",
					"Keystore-l\u00f6senordet \u00e4r f\u00f6r kort - det m\u00e5ste inneh\u00e5lla minst 6 tecken" },
			{ "Unknown Entry Type", "Ok\u00e4nd posttyp" },
			{ "Too many failures. Alias not changed",
					"Alias har inte \u00e4ndrats p.g.a. f\u00f6r m\u00e5nga fel." },
			{ "Entry for alias <alias> successfully imported.",
					"Posten f\u00f6r alias {0} har importerats." },
			{ "Entry for alias <alias> not imported.",
					"Posten f\u00f6r alias {0} har inte importerats." },
			{
					"Problem importing entry for alias <alias>: <exception>.\nEntry for alias <alias> not imported.",
					"Ett problem uppstod vid importen av posten f\u00f6r alias {0}: {1}.\nPosten har inte importerats." },
			{
					"Import command completed:  <ok> entries successfully imported, <fail> entries failed or cancelled",
					"Kommandoimporten slutf\u00f6rd: {0} poster har importerats, {1} poster var felaktiga eller utesl\u00f6ts" },
			{
					"Warning: Overwriting existing alias <alias> in destination keystore",
					"Varning! Det befintliga aliaset {0} i m\u00e5lnyckellagret skrivs \u00f6ver" },
			{ "Existing entry alias <alias> exists, overwrite? [no]:  ",
					"Aliaset {0} finns redan. Vill du skriva \u00f6ver det? [no]:  " },
			{ "Too many failures - try later",
					"F\u00f6r m\u00e5nga fel - f\u00f6rs\u00f6k igen senare" },
			{ "Certification request stored in file <filename>",
					"Certifikat-f\u00f6rfr\u00e5gan har lagrats i filen <{0}>" },
			{ "Submit this to your CA", "Skicka detta till din CA" },
			{
					"if alias not specified, destalias, srckeypass, and destkeypass must not be specified",
					"om n\u00e5got alias inte anges f\u00e5r destalias, srckeypass och destkeypass inte anges" },
			{ "Certificate stored in file <filename>",
					"Certifikatet har lagrats i filen <{0}>" },
			{ "Certificate reply was installed in keystore",
					"Certifikatsvaret har installerats i keystore-filen" },
			{ "Certificate reply was not installed in keystore",
					"Certifikatsvaret har inte installerats i keystore-filen" },
			{ "Certificate was added to keystore",
					"Certifikatet har lagts till i keystore-filen" },
			{ "Certificate was not added to keystore",
					"Certifikatet har inte lagts till i keystore-filen" },
			{ "[Storing ksfname]", "[Lagrar {0}]" },
			{ "alias has no public key (certificate)",
					"{0} saknar offentlig nyckel (certifikat)" },
			{ "Cannot derive signature algorithm",
					"Det g\u00e5r inte att h\u00e4mta n\u00e5gon signatur-algoritm" },
			{ "Alias <alias> does not exist", "Aliaset <{0}> finns inte" },
			{ "Alias <alias> has no certificate",
					"Aliaset <{0}> saknar certifikat" },
			{ "Key pair not generated, alias <alias> already exists",
					"Nyckelparet genererades inte. Aliaset <{0}> finns redan" },
			{ "Cannot derive signature algorithm",
					"Det g\u00e5r inte att h\u00e4mta n\u00e5gon signatur-algoritm" },
			{
					"Generating keysize bit keyAlgName key pair and self-signed certificate (sigAlgName) with a validity of validality days\n\tfor: x500Name",
					"Genererar {0}-bitars {1}-nyckelpar och sj\u00e4lvsignerat certifikat ({2}) med en giltighet p\u00e5 {3} dagar\n\tf\u00f6r: {4}" },
			{ "Enter key password for <alias>",
					"Ange nyckell\u00f6senord f\u00f6r <{0}>" },
			{ "\t(RETURN if same as keystore password):  ",
					"\t(RETURN om det \u00e4r identiskt med keystore-l\u00f6senordet):  " },
			{
					"Key password is too short - must be at least 6 characters",
					"Nyckell\u00f6senordet \u00e4r f\u00f6r kort - det m\u00e5ste inneh\u00e5lla minst 6 tecken" },
			{ "Too many failures - key not added to keystore",
					"F\u00f6r m\u00e5nga fel - nyckeln lades inte till i keystore-filen" },
			{ "Destination alias <dest> already exists",
					"Destinationsaliaset <{0}> finns redan" },
			{
					"Password is too short - must be at least 6 characters",
					"L\u00f6senordet \u00e4r f\u00f6r kort - det m\u00e5ste inneh\u00e5lla minst 6 tecken" },
			{ "Too many failures. Key entry not cloned",
					"F\u00f6r m\u00e5nga fel. Nyckelposten har inte klonats" },
			{ "key password for <alias>", "nyckell\u00f6senord f\u00f6r <{0}>" },
			{ "Keystore entry for <id.getName()> already exists",
					"Keystore-post f\u00f6r <{0}> finns redan" },
			{ "Creating keystore entry for <id.getName()> ...",
					"Skapar keystore-post f\u00f6r <{0}> ..." },
			{ "No entries from identity database added",
					"Inga poster fr\u00e5n identitetsdatabasen har lagts till" },
			{ "Alias name: alias", "Aliasnamn: {0}" },
			{ "Creation date: keyStore.getCreationDate(alias)",
					"Skapat den: {0,date}" },
			{ "alias, keyStore.getCreationDate(alias), ", "{0}, {1,date}, " },
			{ "alias, ", "{0}, " },
			{ "Entry type: <type>", "Posttyp: {0}" },
			{ "Certificate chain length: ",
					"L\u00e4ngd p\u00e5 certifikatskedja: " },
			{ "Certificate[(i + 1)]:", "Certifikat[{0,number,integer}]:" },
			{ "Certificate fingerprint (MD5): ",
					"Certifikatsfingeravtryck (MD5): " },
			{ "Entry type: trustedCertEntry\n", "Posttyp: trustedCertEntry\n" },
			{ "trustedCertEntry,", "trustedCertEntry," },
			{ "Keystore type: ", "Keystore-typ: " },
			{ "Keystore provider: ", "Keystore-leverant\u00f6r: " },
			{ "Your keystore contains keyStore.size() entry",
					"Din keystore inneh\u00e5ller en {0,number,integer} post" },
			{ "Your keystore contains keyStore.size() entries",
					"Din keystore inneh\u00e5ller {0,number,integer} poster" },
			{ "Failed to parse input", "Det g\u00e5r inte att analysera indata" },
			{ "Empty input", "Inga indata" },
			{ "Not X.509 certificate", "Inte ett X.509-certifikat" },
			{ "Cannot derive signature algorithm",
					"Det g\u00e5r inte att h\u00e4mta n\u00e5gon signatur-algoritm" },
			{ "alias has no public key", "{0} saknar offentlig nyckel" },
			{ "alias has no X.509 certificate", "{0} saknar X.509-certifikat" },
			{ "New certificate (self-signed):",
					"Nytt certifikat (sj\u00e4lvsignerat):" },
			{ "Reply has no certificates", "Svaret saknar certifikat" },
			{ "Certificate not imported, alias <alias> already exists",
					"Certifikatet importerades inte. Aliaset <{0}> finns redan" },
			{ "Input not an X.509 certificate",
					"Indata \u00e4r inte ett X.509-certifikat" },
			{
					"Certificate already exists in keystore under alias <trustalias>",
					"Certifikatet finns redan i keystore-filen under aliaset <{0}>" },
			{ "Do you still want to add it? [no]:  ",
					"Vill du fortfarande l\u00e4gga till det? [nej]:  " },
			{
					"Certificate already exists in system-wide CA keystore under alias <trustalias>",
					"Certifikatet finns redan i systemkeystore-filen under aliaset <{0}>" },
			{ "Do you still want to add it to your own keystore? [no]:  ",
					"Vill du fortfarande l\u00e4gga till det i din egen keystore-fil? [nej]:  " },
			{ "Trust this certificate? [no]:  ",
					"Betror du det h\u00e4r certifikatet? [nej]:  " },
			{ "YES", "JA" },
			{ "New prompt: ", "Nytt {0}: " },
			{ "Passwords must differ", "L\u00f6senorden m\u00e5ste vara olika" },
			{ "Re-enter new prompt: ", "Ange nytt {0} igen: " },
			{ "Re-enter new password: ", "Ange det nya l\u00f6senordet igen: " },
			{ "They don't match. Try again",
					"De matchar inte. F\u00f6rs\u00f6k igen" },
			{ "Enter prompt alias name:  ", "Ange {0}-aliasnamn:  " },
			{
					"Enter new alias name\t(RETURN to cancel import for this entry):  ",
					"Ange ett nytt aliasnamn\t(skriv RETURN f\u00f6r att avbryta importen av denna post):  " },
			{ "Enter alias name:  ", "Ange aliasnamn:  " },
			{ "\t(RETURN if same as for <otherAlias>)",
					"\t(RETURN om det \u00e4r det samma som f\u00f6r <{0}>)" },
			{
					"*PATTERN* printX509Cert",
					"\u00c4gare: {0}\nUtf\u00e4rdare: {1}\nSerienummer: {2}\nGiltigt fr\u00e5n: {3} till: {4}\nCertifikatfingeravtryck:\n\t MD5: {5}\n\t SHA1: {6}\n\t Signaturalgoritm: {7}\n\t Version: {8}" },
			{ "What is your first and last name?",
					"Vad heter du i f\u00f6r- och efternamn?" },
			{ "What is the name of your organizational unit?",
					"Vad heter din avdelning inom organisationen?" },
			{ "What is the name of your organization?",
					"Vad heter din organisation?" },
			{ "What is the name of your City or Locality?",
					"Vad heter din ort eller plats?" },
			{ "What is the name of your State or Province?",
					"Vad heter ditt land eller din provins?" },
			{ "What is the two-letter country code for this unit?",
					"Vilken \u00e4r den tv\u00e5st\u00e4lliga landskoden?" },
			{ "Is <name> correct?", "\u00c4r {0} korrekt?" },
			{ "no", "nej" },
			{ "yes", "ja" },
			{ "y", "j" },
			{ "  [defaultValue]:  ", "  [{0}]:  " },
			{ "Alias <alias> has no key", "Aliaset <{0}> saknar nyckel" },
			{
					"Alias <alias> references an entry type that is not a private key entry.  The -keyclone command only supports cloning of private key entries",
					"Aliaset <{0}> h\u00e4nvisar till en posttyp som inte \u00e4r n\u00e5gon privat nyckelpost. Kommandot -keyclone har endast st\u00f6d f\u00f6r kloning av privata nyckelposter" },

			{ "*****************  WARNING WARNING WARNING  *****************",
					"*****************  VARNING VARNING VARNING  *****************" },

			// Translators of the following 5 pairs, ATTENTION:
			// the next 5 string pairs are meant to be combined into 2
			// paragraphs,
			// 1+3+4 and 2+3+5. make sure your translation also does.
			{
					"* The integrity of the information stored in your keystore  *",
					"* Integriteten betr\u00e4ffande den information som lagras i keystore-filen  *" },
			{ "* The integrity of the information stored in the srckeystore*",
					"* Integriteten f\u00f6r informationen som lagras i srckeystore*" },
			{ "* has NOT been verified!  In order to verify its integrity, *",
					"* har INTE verifierats!  Om du vill verifiera dess integritet, *" },
			{
					"* you must provide your keystore password.                  *",
					"* m\u00e5ste du tillhandah\u00e5lla ditt keystore-l\u00f6senord.                  *" },
			{ "* you must provide the srckeystore password.                *",
					"* du m\u00e5ste ange l\u00f6senordet f\u00f6r srckeystore.                *" },

			{
					"Certificate reply does not contain public key for <alias>",
					"Certifikatsvaret inneh\u00e5ller inte n\u00e5gon offentlig nyckel f\u00f6r <{0}>" },
			{ "Incomplete certificate chain in reply",
					"Ofullst\u00e4ndig certifikatskedja i svaret" },
			{ "Certificate chain in reply does not verify: ",
					"Certifikatskedjan i svaret g\u00e5r inte att verifiera: " },
			{ "Top-level certificate in reply:\n",
					"Toppniv\u00e5certifikatet i svaret:\n" },
			{ "... is not trusted. ", "... \u00e4r inte betrott. " },
			{ "Install reply anyway? [no]:  ",
					"Vill du installera svaret \u00e4nd\u00e5? [nej]:  " },
			{ "NO", "NEJ" },
			{ "Public keys in reply and keystore don't match",
					"De offentliga nycklarna i svaret och keystore-filen matchar inte varandra" },
			{ "Certificate reply and certificate in keystore are identical",
					"Certifikatssvaret och certifikatet i keystore-filen \u00e4r identiska" },
			{ "Failed to establish chain from reply",
					"Det gick inte att uppr\u00e4tta n\u00e5gon kedja ur svaret" },
			{ "n", "n" },
			{ "Wrong answer, try again",
					"Fel svar. F\u00f6rs\u00f6k p\u00e5 nytt." },
			{ "Secret key not generated, alias <alias> already exists",
					"Den hemliga nyckeln har inte genererats eftersom aliaset <{0}> redan finns" },
			{ "Please provide -keysize for secret key generation",
					"Ange -keysize f\u00f6r skapande av hemlig nyckel" },
			{ "keytool usage:\n", "nyckelverktyg:\n" },

			{ "Extensions: ", "Filtill\u00e4gg: " },

			{ "-certreq     [-v] [-protected]",
					"-certreq     [-v] [-protected]" },
			{ "\t     [-alias <alias>] [-sigalg <sigalg>]",
					"\t     [-alias <alias>] [-sigalg <signaturalgoritm>]" },
			{ "\t     [-file <csr_file>] [-keypass <keypass>]",
					"\t     [-file <csr_fil>] [-keypass <nyckell\u00f6senord>]" },
			{ "\t     [-keystore <keystore>] [-storepass <storepass>]",
					"\t     [-keystore <keystore>] [-storepass <lagringsl\u00f6senord>]" },
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
					"\t     [-alias <alias>] [-file <certifikatsfil>]" },
			/** rest is same as -certreq starting from -keystore **/

			// {"-genkey      [-v] [-protected]",
			// "-genkey      [-v] [-protected]"},
			{ "-genkeypair  [-v] [-protected]",
					"-genkeypair  [-v] [-protected]" },
			{ "\t     [-alias <alias>]", "\t     [-alias <alias>]" },
			{ "\t     [-keyalg <keyalg>] [-keysize <keysize>]",
					"\t     [-keyalg <nyckelalgoritm>] [-keysize <nyckelstorlek>]" },
			{ "\t     [-sigalg <sigalg>] [-dname <dname>]",
					"\t     [-sigalg <signaturalgoritm>] [-dname <dnamn>]" },
			{ "\t     [-validity <valDays>] [-keypass <keypass>]",
					"\t     [-validity <dagar>] [-keypass <nyckell\u00f6senord>]" },
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
					"\t     [-file <certifikatsfil>] [-keypass <nyckell\u00f6senord>]" },
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
					"\t     [-srcprovidername <srcprovidername>]\n\t     [-destprovidername <destprovidername>]", // raden
																													// \u00e4r
																													// f\u00f6r
																													// l\u00e5ng,
																													// dela
																													// upp
																													// p\u00e5
																													// 2
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
			{
					"\t     [-keypass <old_keypass>] [-new <new_keypass>]",
					"\t     [-keypass <gammalt_nyckell\u00f6senord>] [-new <nytt_nyckell\u00f6senord>]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-list        [-v | -rfc] [-protected]",
					"-list        [-v | -rfc] [-protected]" },
			{ "\t     [-alias <alias>]", "\t     [-alias <alias>]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-printcert   [-v] [-file <cert_file>]",
					"-printcert   [-v] [-file <certifikatsfil>]" },

			// {"-selfcert    [-v] [-protected]",
			// "-selfcert    [-v] [-protected]"},
			{ "\t     [-alias <alias>]", "\t     [-alias <alias>]" },
			// {"\t     [-dname <dname>] [-validity <valDays>]",
			// "\t     [-dname <dname>] [-validity <valDays>]"},
			// {"\t     [-keypass <keypass>] [-sigalg <sigalg>]",
			// "\t     [-keypass <keypass>] [-sigalg <sigalg>]"},
			/** rest is same as -certreq starting from -keystore **/

			{ "-storepasswd [-v] [-new <new_storepass>]",
					"-storepasswd [-v] [-new <nytt_lagringsl\u00f6senord>]" },
			/** rest is same as -certreq starting from -keystore **/

			// policytool
			{
					"Warning: A public key for alias 'signers[i]' does not exist.  Make sure a KeyStore is properly configured.",
					"Varning! Det finns ingen offentlig nyckel f\u00f6r aliaset {0}. Kontrollera att det aktuella nyckellagret \u00e4r korrekt konfigurerat." },
			{ "Warning: Class not found: class",
					"Varning! Klassen hittades inte: {0}" },
			{ "Warning: Invalid argument(s) for constructor: arg",
					"Varning! Ogiltigt/Ogiltiga argument f\u00f6r konstrukt\u00f6r: {0}" },
			{ "Illegal Principal Type: type", "Ogiltig huvudtyp: {0}" },
			{ "Illegal option: option", "Ogiltigt alternativ: {0}" },
			{ "Usage: policytool [options]",
					"G\u00f6r s\u00e5 h\u00e4r: policytool [alternativ]" },
			{ "  [-file <file>]    policy file location",
					"  [-file <fil>]    policyfilens plats" },
			{ "New", "Nytt" },
			{ "Open", "\u00d6ppna" },
			{ "Save", "Spara" },
			{ "Save As", "Spara som" },
			{ "View Warning Log", "Visa varningslogg" },
			{ "Exit", "Avsluta" },
			{ "Add Policy Entry", "L\u00e4gg till policypost" },
			{ "Edit Policy Entry", "Redigera policypost" },
			{ "Remove Policy Entry", "Ta bort policypost" },
			{ "Edit", "Redigera" },
			{ "Retain", "Beh\u00e5ll" },

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

			{ "Add Public Key Alias", "L\u00e4gg till offentligt nyckelalias" },
			{ "Remove Public Key Alias", "Ta bort offentligt nyckelalias" },
			{ "File", "Arkiv" },
			{ "KeyStore", "Nyckellager" },
			{ "Policy File:", "Policyfil:" },
			{ "Could not open policy file: policyFile: e.toString()",
					"Det g\u00e5r inte att \u00f6ppna policyfilen: {0}: {1}" },
			{ "Policy Tool", "Policyverktyg" },
			{
					"Errors have occurred while opening the policy configuration.  View the Warning Log for more information.",
					"Det uppstod ett fel n\u00e4r policykonfigurationen skulle \u00f6ppnas.  Visa varningsloggen med ytterligare information." },
			{ "Error", "Fel" },
			{ "OK", "OK" },
			{ "Status", "Status" },
			{ "Warning", "Varning" },
			{
					"Permission:                                                       ",
					"Beh\u00f6righet:                                                       " },
			{ "Principal Type:", "Principaltyp:" },
			{ "Principal Name:", "Principalnamn:" },
			{
					"Target Name:                                                    ",
					"M\u00e5lets namn:                                                    " },
			{
					"Actions:                                                             ",
					"Funktioner:                                                             " },
			{ "OK to overwrite existing file filename?",
					"Ska den befintliga filen {0} skrivas \u00f6ver?" },
			{ "Cancel", "Avbryt" },
			{ "CodeBase:", "CodeBase:" },
			{ "SignedBy:", "SignedBy:" },
			{ "Add Principal", "L\u00e4gg till principal" },
			{ "Edit Principal", "Redigera principal" },
			{ "Remove Principal", "Ta bort principal" },
			{ "Principals:", "Principaler:" },
			{ "  Add Permission", "  L\u00e4gg till beh\u00f6righet" },
			{ "  Edit Permission", "  Redigera beh\u00f6righet" },
			{ "Remove Permission", "Ta bort beh\u00f6righet" },
			{ "Done", "Klar" },
			{ "KeyStore URL:", "Webbadress f\u00f6r nyckellager:" },
			{ "KeyStore Type:", "Nyckellagertyp:" },
			{ "KeyStore Provider:", "Nyckellagerleverant\u00f6r:" },
			{ "KeyStore Password URL:",
					"Webbadress f\u00f6r l\u00f6senord till nyckellager:" },
			{ "Principals", "Principaler" },
			{ "  Edit Principal:", "  Redigera principal:" },
			{ "  Add New Principal:", "  L\u00e4gg till ny principal:" },
			{ "Permissions", "Beh\u00f6righet" },
			{ "  Edit Permission:", "  Redigera beh\u00f6righet:" },
			{ "  Add New Permission:", "  L\u00e4gg till ny beh\u00f6righet:" },
			{ "Signed By:", "Signerad av:" },
			{
					"Cannot Specify Principal with a Wildcard Class without a Wildcard Name",
					"Det g\u00e5r inte att specificera principal med wildcard-klass utan wildcard-namn" },
			{ "Cannot Specify Principal without a Name",
					"Det g\u00e5r inte att specificera principal utan namn" },
			{ "Permission and Target Name must have a value",
					"Beh\u00f6righet och m\u00e5lnamn m\u00e5ste ha ett v\u00e4rde" },
			{ "Remove this Policy Entry?",
					"Vill du ta bort den h\u00e4r policyposten?" },
			{ "Overwrite File", "Skriva \u00f6ver fil" },
			{ "Policy successfully written to filename",
					"Policy har skrivits till {0}" },
			{ "null filename", "nullfilnamn" },
			{ "Save changes?", "Vill du spara \u00e4ndringarna?" },
			{ "Yes", "Ja" },
			{ "No", "Nej" },
			{ "Policy Entry", "Policyfel" },
			{ "Save Changes", "Vill du spara \u00e4ndringarna?" },
			{ "No Policy Entry selected",
					"N\u00e5gon policypost har inte markerats" },
			{ "Unable to open KeyStore: ex.toString()",
					"Det g\u00e5r inte att \u00f6ppna nyckellagret: {0}" },
			{ "No principal selected", "Ingen principal har markerats" },
			{ "No permission selected",
					"N\u00e5gon beh\u00f6righet har inte markerats" },
			{ "name", "namn" },
			{ "configuration type", "konfigurationstyp" },
			{ "environment variable name", "variabelnamn f\u00f6r milj\u00f6" },
			{ "library name", "biblioteksnamn" },
			{ "package name", "paketnamn" },
			{ "policy type", "policytyp" },
			{ "property name", "egenskapsnamn" },
			{ "provider name", "leverant\u00f6rsnamn" },
			{ "Principal List", "Huvudlista" },
			{ "Permission List", "Beh\u00f6righetslista" },
			{ "Code Base", "Kodbas" },
			{ "KeyStore U R L:", "Webbadress f\u00f6r nyckellager:" },
			{ "KeyStore Password U R L:",
					"Webbadress f\u00f6r l\u00f6senord till nyckellager:" },

			// javax.security.auth.PrivateCredentialPermission
			{ "invalid null input(s)", "ogiltiga null-indata" },
			{ "actions can only be 'read'",
					"funktioner kan endast 'l\u00e4sas'" },
			{ "permission name [name] syntax invalid: ",
					"syntaxen f\u00f6r beh\u00f6righetsnamnet [{0}] \u00e4r ogiltig: " },
			{ "Credential Class not followed by a Principal Class and Name",
					"Kreditivklassen f\u00f6ljs inte av principalklass eller principalnamn" },
			{ "Principal Class not followed by a Principal Name",
					"Principalklassen f\u00f6ljs inte av n\u00e5got principalnamn" },
			{ "Principal Name must be surrounded by quotes",
					"Principalnamnet m\u00e5ste anges inom citattecken" },
			{ "Principal Name missing end quote",
					"Principalnamnet saknar avslutande citattecken" },
			{
					"PrivateCredentialPermission Principal Class can not be a wildcard (*) value if Principal Name is not a wildcard (*) value",
					"V\u00e4rdet f\u00f6r principalklassen PrivateCredentialPermission kan inte ha n\u00e5got jokertecken (*) om principalnamnet inte anges med jokertecken (*)" },
			{ "CredOwner:\n\tPrincipal Class = class\n\tPrincipal Name = name",
					"CredOwner:\n\tPrincipalklass = {0}\n\tPrincipalnamn = {1}" },

			// javax.security.auth.x500
			{ "provided null name", "gav null-namn" },
			{ "provided null keyword map",
					"nullnyckelordsmappning tillhandah\u00f6lls" },
			{ "provided null OID map", "null-OID-mappning tillhandah\u00f6lls" },

			// javax.security.auth.Subject
			{ "invalid null AccessControlContext provided",
					"ogiltigt null-AccessControlContext" },
			{ "invalid null action provided", "ogiltig null-funktion" },
			{ "invalid null Class provided", "ogiltig null-klass" },
			{ "Subject:\n", "\u00c4rende:\n" },
			{ "\tPrincipal: ", "\tPrincipal: " },
			{ "\tPublic Credential: ", "\tOffentligt kreditiv: " },
			{ "\tPrivate Credentials inaccessible\n",
					"\tPrivata kreditiv \u00e4r otillg\u00e4ngliga\n" },
			{ "\tPrivate Credential: ", "\tPrivata kreditiv: " },
			{ "\tPrivate Credential inaccessible\n",
					"\tPrivata kreditiv \u00e4r otillg\u00e4ngliga\n" },
			{ "Subject is read-only", "\u00c4mnet \u00e4r skrivskyddat" },
			{
					"attempting to add an object which is not an instance of java.security.Principal to a Subject's Principal Set",
					"f\u00f6rs\u00f6k att l\u00e4gga till ett objekt som inte \u00e4r en f\u00f6rekomst av java.security.Principal till en principalupps\u00e4ttning" },
			{
					"attempting to add an object which is not an instance of class",
					"f\u00f6rs\u00f6ker l\u00e4gga till ett objekt som inte \u00e4r en f\u00f6rekomst av {0}" },

			// javax.security.auth.login.AppConfigurationEntry
			{ "LoginModuleControlFlag: ", "LoginModuleControlFlag: " },

			// javax.security.auth.login.LoginContext
			{ "Invalid null input: name", "Ogiltiga null-indata: namn" },
			{ "No LoginModules configured for name",
					"Inga inloggningsmoduler har konfigurerats f\u00f6r {0}" },
			{ "invalid null Subject provided", "ogiltigt null-Subject" },
			{ "invalid null CallbackHandler provided",
					"ogiltig null-CallbackHandler" },
			{ "null subject - logout called before login",
					"null-subject - utloggning anropades f\u00f6re inloggning" },
			{
					"unable to instantiate LoginModule, module, because it does not provide a no-argument constructor",
					"det g\u00e5r inta att representera LoginModule, {0}, eftersom den inte tillhandah\u00e5ller n\u00e5gon argumentfri konstruktion" },
			{ "unable to instantiate LoginModule",
					"det g\u00e5r inte att representera LoginModule" },
			{ "unable to instantiate LoginModule: ",
					"inloggningsmodulen kan inte skapas: " },
			{ "unable to find LoginModule class: ",
					"det g\u00e5r inte att hitta LoginModule-klassen: " },
			{ "unable to access LoginModule: ",
					"det g\u00e5r inte att komma \u00e5t LoginModule: " },
			{ "Login Failure: all modules ignored",
					"Inloggningsfel: alla moduler ignoreras" },

			// sun.security.provider.PolicyFile

			{ "java.security.policy: error parsing policy:\n\tmessage",
					"java.security.policy: fel vid analys av {0}:\n\t{1}" },
			{
					"java.security.policy: error adding Permission, perm:\n\tmessage",
					"java.security.policy: fel vid till\u00e4gg av beh\u00f6righet, {0}:\n\t{1}" },
			{ "java.security.policy: error adding Entry:\n\tmessage",
					"java.security.policy: fel vid till\u00e4gg av post:\n\t{0}" },
			{ "alias name not provided (pe.name)", "aliasnamn ej angivet ({0})" },
			{ "unable to perform substitution on alias, suffix",
					"kan ej ers\u00e4tta aliasnamn, {0}" },
			{ "substitution value, prefix, unsupported",
					"ers\u00e4ttningsv\u00e4rde, {0}, st\u00f6ds ej" },
			{ "(", "(" },
			{ ")", ")" },
			{ "type can't be null", "typen kan inte vara null" },

			// sun.security.provider.PolicyParser
			{
					"keystorePasswordURL can not be specified without also specifying keystore",
					"det g\u00e5r inte att ange keystorePasswordURL utan att ange keystore" },
			{ "expected keystore type", "f\u00f6rv\u00e4ntad keystore-typ" },
			{ "expected keystore provider",
					"keystore-leverant\u00f6r f\u00f6rv\u00e4ntades" },
			{ "multiple Codebase expressions", "flera Codebase-uttryck" },
			{ "multiple SignedBy expressions", "flera SignedBy-uttryck" },
			{ "SignedBy has empty alias", "SignedBy har ett tomt alias" },
			{
					"can not specify Principal with a wildcard class without a wildcard name",
					"Det g\u00e5r inte att specificera principal genom att ange jokertecken f\u00f6r klass utan att samtidigt ange jokertecken f\u00f6r namn" },
			{ "expected codeBase or SignedBy or Principal",
					"f\u00f6rv\u00e4ntad codeBase eller SignedBy eller Principal" },
			{ "expected permission entry",
					"f\u00f6rv\u00e4ntade beh\u00f6righetspost" },
			{ "number ", "antal " },
			{ "expected [expect], read [end of file]",
					"f\u00f6rv\u00e4ntade [{0}], l\u00e4ste [end of file]" },
			{ "expected [;], read [end of file]",
					"f\u00f6rv\u00e4ntade [;], l\u00e4ste [end of file]" },
			{ "line number: msg", "rad {0}: {1}" },
			{ "line number: expected [expect], found [actual]",
					"rad {0}: f\u00f6rv\u00e4ntade [{1}], hittade [{2}]" },
			{ "null principalClass or principalName",
					"null-principalClass eller -principalName" },

			// sun.security.pkcs11.SunPKCS11
			{ "PKCS11 Token [providerName] Password: ",
					"PKCS11-pollett [{0}] L\u00f6senord: " },

			/* --- DEPRECATED --- */
			// javax.security.auth.Policy
			{ "unable to instantiate Subject-based policy",
					"den Subject-baserade policyn kan inte skapas" } };

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
