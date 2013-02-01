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
public class Resources_fr extends java.util.ListResourceBundle {

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
					"*******************************************" },

			// keytool
			{ "keytool error: ", "erreur keytool : " },
			{ "Illegal option:  ", "Option non valide :  " },
			{ "Try keytool -help", "Essayez keytool -help" },
			{ "Command option <flag> needs an argument.",
					"L'option de commande {0} requiert un argument." },
			{
					"Warning:  Different store and key passwords not supported for PKCS12 KeyStores. Ignoring user-specified <command> value.",
					"Avertissement\u00a0:  Les mots de passe store et key distincts ne sont pas pris en charge pour les keystores PKCS12. La valeur {0} sp\u00e9cifi\u00e9e par l'utilisateur est ignor\u00e9e." },
			{ "-keystore must be NONE if -storetype is {0}",
					"-keystore doit \u00eatre d\u00e9fini sur NONE si -storetype est {0}" },
			{ "Too may retries, program terminated",
					"Trop de tentatives, fin du programme" },
			{
					"-storepasswd and -keypasswd commands not supported if -storetype is {0}",
					"Les commandes -storepasswd et -keypasswd ne sont pas prises en charge si -storetype est d\u00e9fini sur {0}" },
			{
					"-keypasswd commands not supported if -storetype is PKCS12",
					"Les commandes -keypasswd ne sont pas prises en charge si -storetype est d\u00e9fini sur PKCS12" },
			{
					"-keypass and -new can not be specified if -storetype is {0}",
					"Les commandes -keypass et -new ne peuvent pas \u00eatre sp\u00e9cifi\u00e9es si -storetype est d\u00e9fini sur {0}" },
			{
					"if -protected is specified, then -storepass, -keypass, and -new must not be specified",
					"si -protected est sp\u00e9cifi\u00e9, alors -storepass, -keypass et -new ne doivent pas \u00eatre sp\u00e9cifi\u00e9s" },
			{
					"if -srcprotected is specified, then -srcstorepass and -srckeypass must not be specified",
					"Si \u0096srcprotected est sp\u00e9cifi\u00e9, alors -srcstorepass et \u0096srckeypass ne doivent pas \u00eatre sp\u00e9cifi\u00e9s" },
			{
					"if keystore is not password protected, then -storepass, -keypass, and -new must not be specified",
					"Si le keystore n'est pas prot\u00e9g\u00e9 par un mot de passe, les commandes -storepass, -keypass et -new ne doivent pas \u00eatre sp\u00e9cifi\u00e9es" },
			{
					"if source keystore is not password protected, then -srcstorepass and -srckeypass must not be specified",
					"Si le keystore source n'est pas prot\u00e9g\u00e9 par un mot de passe, les commandes -srcstorepass et -srckeypass ne doivent pas \u00eatre sp\u00e9cifi\u00e9es" },
			{ "Validity must be greater than zero",
					"La validit\u00e9 doit \u00eatre sup\u00e9rieure \u00e0 z\u00e9ro" },
			{ "provName not a provider", "{0} n''est pas un fournisseur" },
			{ "Usage error: no command provided",
					"Erreur d'utilisation\u00a0: aucune commande fournie" },
			{ "Usage error, <arg> is not a legal command",
					"Erreur d'utilisation, {0} n'est pas une commande valide" },
			{ "Source keystore file exists, but is empty: ",
					"Le fichier du keystore source existe, mais il est vide\u00a0: " },
			{ "Please specify -srckeystore",
					"veuillez sp\u00e9cifier -srckeystore" },
			{
					"Must not specify both -v and -rfc with 'list' command",
					"-v et -rfc ne peuvent \u00eatre sp\u00e9cifi\u00e9s simultan\u00e9ment avec la commande 'list'" },
			{ "Key password must be at least 6 characters",
					"Un mot de passe de cl\u00e9 doit comporter au moins 6 caract\u00e8res" },
			{ "New password must be at least 6 characters",
					"Le nouveau mot de passe doit comporter au moins 6 caract\u00e8res" },
			{ "Keystore file exists, but is empty: ",
					"Fichier Keystore existant mais vide : " },
			{ "Keystore file does not exist: ",
					"Fichier Keystore introuvable : " },
			{ "Must specify destination alias",
					"L'alias de destination doit \u00eatre sp\u00e9cifi\u00e9" },
			{ "Must specify alias", "Vous devez sp\u00e9cifier un alias" },
			{ "Keystore password must be at least 6 characters",
					"Un mot de passe de Keystore doit comporter au moins 6 caract\u00e8res" },
			{ "Enter keystore password:  ",
					"Tapez le mot de passe du Keystore :  " },
			{ "Enter source keystore password:  ",
					"Saisissez le mot de passe du keystore source\u00a0:  " },
			{ "Enter destination keystore password:  ",
					"Saisissez le mot de passe du keystore de destination\u00a0:  " },
			{
					"Keystore password is too short - must be at least 6 characters",
					"Mot de passe de Keystore trop court, il doit compter au moins 6 caract\u00e8res" },
			{ "Unknown Entry Type", "Type d'entr\u00e9e inconnu" },
			{ "Too many failures. Alias not changed",
					"Trop d'erreurs. Alias non modifi\u00e9" },
			{ "Entry for alias <alias> successfully imported.",
					"L'entr\u00e9e de l'alias {0} a \u00e9t\u00e9 import\u00e9e." },
			{ "Entry for alias <alias> not imported.",
					"L'entr\u00e9e de l'alias {0} n'a pas \u00e9t\u00e9 import\u00e9e." },
			{
					"Problem importing entry for alias <alias>: <exception>.\nEntry for alias <alias> not imported.",
					"Probl\u00e8me lors de l'importation de l'entr\u00e9e de l'alias {0}\u00a0: {1}.\nL'entr\u00e9e de l'alias {0} n'a pas \u00e9t\u00e9 import\u00e9e." },
			{
					"Import command completed:  <ok> entries successfully imported, <fail> entries failed or cancelled",
					"Commande d'importation ex\u00e9cut\u00e9e\u00a0:  {0} entr\u00e9es import\u00e9es, \u00e9chec ou annulation de {1} entr\u00e9es" },
			{
					"Warning: Overwriting existing alias <alias> in destination keystore",
					"Avertissement\u00a0: L'alias existant {0} est \u00e9cras\u00e9 dans le keystore de destination" },
			{
					"Existing entry alias <alias> exists, overwrite? [no]:  ",
					"L'alias d'entr\u00e9e {0} existe d\u00e9j\u00e0, voulez-vous l'\u00e9craser\u00a0? [non]\u00a0:  " },
			{ "Too many failures - try later",
					"Trop d'erreurs - r\u00e9essayez plus tard" },
			{ "Certification request stored in file <filename>",
					"Demande de certification enregistr\u00e9e dans le fichier <{0}>" },
			{ "Submit this to your CA", "Soumettre \u00e0 votre CA" },
			{
					"if alias not specified, destalias, srckeypass, and destkeypass must not be specified",
					"si l'alias n'est pas sp\u00e9cifi\u00e9, destalias, srckeypass et destkeypass ne doivent pas \u00eatre sp\u00e9cifi\u00e9s" },
			{ "Certificate stored in file <filename>",
					"Certificat enregistr\u00e9 dans le fichier <{0}>" },
			{ "Certificate reply was installed in keystore",
					"R\u00e9ponse de certificat install\u00e9e dans le Keystore" },
			{ "Certificate reply was not installed in keystore",
					"R\u00e9ponse de certificat non install\u00e9e dans le Keystore" },
			{ "Certificate was added to keystore",
					"Certificat ajout\u00e9 au Keystore" },
			{ "Certificate was not added to keystore",
					"Certificat non ajout\u00e9 au Keystore" },
			{ "[Storing ksfname]", "[Stockage de {0}]" },
			{ "alias has no public key (certificate)",
					"{0} ne poss\u00e8de pas de cl\u00e9 publique (certificat)" },
			{ "Cannot derive signature algorithm",
					"Impossible de d\u00e9duire l'algorithme de signature" },
			{ "Alias <alias> does not exist", "Alias <{0}> introuvable" },
			{ "Alias <alias> has no certificate",
					"L''alias <{0}> ne poss\u00e8de pas de certificat" },
			{
					"Key pair not generated, alias <alias> already exists",
					"Paire de cl\u00e9s non g\u00e9n\u00e9r\u00e9e, l''alias <{0}> existe d\u00e9j\u00e0" },
			{ "Cannot derive signature algorithm",
					"Impossible de d\u00e9duire l'algorithme de signature" },
			{
					"Generating keysize bit keyAlgName key pair and self-signed certificate (sigAlgName) with a validity of validality days\n\tfor: x500Name",
					"G\u00e9n\u00e9ration d''une paire de cl\u00e9s {1} de {0} bits et d''un certificat autosign\u00e9 ({2}) d''une validit\u00e9 de {3} jours\n\tpour : {4}" },
			{ "Enter key password for <alias>",
					"Sp\u00e9cifiez le mot de passe de la cl\u00e9 pour <{0}>" },
			{ "\t(RETURN if same as keystore password):  ",
					"\t(appuyez sur Entr\u00e9e s'il s'agit du mot de passe du Keystore) :  " },
			{ "Key password is too short - must be at least 6 characters",
					"Le mot de passe de cl\u00e9 doit comporter au moins 6 caract\u00e8res." },
			{ "Too many failures - key not added to keystore",
					"Trop d'erreurs - cl\u00e9 non ajout\u00e9e au Keystore" },
			{ "Destination alias <dest> already exists",
					"L''alias de la destination <{0}> existe d\u00e9j\u00e0" },
			{ "Password is too short - must be at least 6 characters",
					"Le mot de passe doit comporter au moins 6 caract\u00e8res." },
			{ "Too many failures. Key entry not cloned",
					"Trop d'erreurs. Entr\u00e9e de cl\u00e9 non clon\u00e9e" },
			{ "key password for <alias>", "mot de passe de cl\u00e9 pour <{0}>" },
			{ "Keystore entry for <id.getName()> already exists",
					"L''entr\u00e9e Keystore pour <{0}> existe d\u00e9j\u00e0" },
			{ "Creating keystore entry for <id.getName()> ...",
					"Cr\u00e9ation d''une entr\u00e9e keystore pour <{0}> ..." },
			{
					"No entries from identity database added",
					"Aucune entr\u00e9e ajout\u00e9e \u00e0 partir de la base de donn\u00e9es d'identit\u00e9s" },
			{ "Alias name: alias", "Nom d''alias : {0}" },
			{ "Creation date: keyStore.getCreationDate(alias)",
					"Date de cr\u00e9ation : {0,date}" },
			{ "alias, keyStore.getCreationDate(alias), ", "{0}, {1,date}, " },
			{ "alias, ", "{0}, " },
			{ "Entry type: <type>", "Type d'entr\u00e9e\u00a0: {0}" },
			{ "Certificate chain length: ",
					"Longueur de cha\u00eene du certificat : " },
			{ "Certificate[(i + 1)]:", "Certificat[{0,number,integer}]:" },
			{ "Certificate fingerprint (MD5): ",
					"Empreinte du certificat (MD5) : " },
			{ "Entry type: trustedCertEntry\n",
					"Type d'entr\u00e9e : trustedCertEntry\n" },
			{ "trustedCertEntry,", "trustedCertEntry," },
			{ "Keystore type: ", "Type Keystore : " },
			{ "Keystore provider: ", "Fournisseur Keystore : " },
			{ "Your keystore contains keyStore.size() entry",
					"Votre Keystore contient {0,number,integer} entr\u00e9e(s)" },
			{ "Your keystore contains keyStore.size() entries",
					"Votre Keystore contient {0,number,integer} entr\u00e9e(s)" },
			{ "Failed to parse input",
					"L'analyse de l'entr\u00e9e a \u00e9chou\u00e9" },
			{ "Empty input", "Entr\u00e9e vide" },
			{ "Not X.509 certificate", "Pas un certificat X.509" },
			{ "Cannot derive signature algorithm",
					"Impossible de d\u00e9duire l'algorithme de signature" },
			{ "alias has no public key",
					"{0} ne poss\u00e8de pas de cl\u00e9 publique" },
			{ "alias has no X.509 certificate",
					"{0} ne poss\u00e8de pas de certificat X.509" },
			{ "New certificate (self-signed):",
					"Nouveau certificat (auto-sign\u00e9) :" },
			{ "Reply has no certificates",
					"La r\u00e9ponse n'a pas de certificat" },
			{ "Certificate not imported, alias <alias> already exists",
					"Certificat non import\u00e9, l''alias <{0}> existe d\u00e9j\u00e0" },
			{ "Input not an X.509 certificate",
					"L'entr\u00e9e n'est pas un certificat X.509" },
			{
					"Certificate already exists in keystore under alias <trustalias>",
					"Le certificat existe d\u00e9j\u00e0 dans le Keystore sous l''alias <{0}>" },
			{ "Do you still want to add it? [no]:  ",
					"Voulez-vous toujours l'ajouter ? [non] :  " },
			{
					"Certificate already exists in system-wide CA keystore under alias <trustalias>",
					"Le certificat existe d\u00e9j\u00e0 dans le Keystore CA syst\u00e8me sous l''alias <{0}>alias <{0}>" },
			{ "Do you still want to add it to your own keystore? [no]:  ",
					"Voulez-vous toujours l'ajouter \u00e0 votre Keystore ? [non] :  " },
			{ "Trust this certificate? [no]:  ",
					"Faire confiance \u00e0 ce certificat ? [non] :  " },
			{ "YES", "OUI" },
			{ "New prompt: ", "Nouveau {0} : " },
			{ "Passwords must differ",
					"Les mots de passe doivent diff\u00e9rer" },
			{ "Re-enter new prompt: ", "Sp\u00e9cifiez nouveau {0} : " },
			{ "Re-enter new password: ",
					"Ressaisissez le nouveau mot de passe : " },
			{ "They don't match. Try again",
					"ne correspondent pas. R\u00e9essayez." },
			{ "Enter prompt alias name:  ",
					"Sp\u00e9cifiez le nom d''alias {0} :  " },
			{
					"Enter new alias name\t(RETURN to cancel import for this entry):  ",
					"Saisissez le nom du nouvel alias\t(ou appuyez sur ENTR\u00c9E pour annuler l'importation pour cette entr\u00e9e)\u00a0:  " },
			{ "Enter alias name:  ", "Sp\u00e9cifiez le nom d'alias :  " },
			{ "\t(RETURN if same as for <otherAlias>)",
					"\t(appuyez sur Entr\u00e9e si le r\u00e9sultat est identique \u00e0 <{0}>)" },
			{
					"*PATTERN* printX509Cert",
					"Propri\u00e9taire\u00a0: {0}\n\u00c9metteur\u00a0: {1}\nNum\u00e9ro de s\u00e9rie\u00a0: {2}\nValide du\u00a0: {3} au\u00a0: {4}\nEmpreintes du certificat\u00a0:\n\t MD5\u00a0:  {5}\n\t SHA1\u00a0: {6}\n\t Nom de l'algorithme de signature\u00a0: {7}\n\t Version\u00a0: {8}" },
			{ "What is your first and last name?",
					"Quels sont vos pr\u00e9nom et nom ?" },
			{ "What is the name of your organizational unit?",
					"Quel est le nom de votre unit\u00e9 organisationnelle ?" },
			{ "What is the name of your organization?",
					"Quelle est le nom de votre organisation ?" },
			{ "What is the name of your City or Locality?",
					"Quel est le nom de votre ville de r\u00e9sidence ?" },
			{ "What is the name of your State or Province?",
					"Quel est le nom de votre \u00e9tat ou province ?" },
			{ "What is the two-letter country code for this unit?",
					"Quel est le code de pays \u00e0 deux lettres pour cette unit\u00e9 ?" },
			{ "Is <name> correct?", "Est-ce {0} ?" },
			{ "no", "non" },
			{ "yes", "oui" },
			{ "y", "o" },
			{ "  [defaultValue]:  ", "  [{0}] :  " },
			{ "Alias <alias> has no key",
					"L'alias <{0}> n'est associ\u00e9 \u00e0 aucune cl\u00e9" },
			{
					"Alias <alias> references an entry type that is not a private key entry.  The -keyclone command only supports cloning of private key entries",
					"L'entr\u00e9e \u00e0 laquelle l'alias <{0}> fait r\u00e9f\u00e9rence n'est pas une entr\u00e9e de type cl\u00e9 priv\u00e9e.  La commande -keyclone prend uniquement en charge le clonage des cl\u00e9s priv\u00e9es" },

			{ "*****************  WARNING WARNING WARNING  *****************",
					"*****************  A V E R T I S S E M E N T  *****************" },

			// Translators of the following 5 pairs, ATTENTION:
			// the next 5 string pairs are meant to be combined into 2
			// paragraphs,
			// 1+3+4 and 2+3+5. make sure your translation also does.
			{
					"* The integrity of the information stored in your keystore  *",
					"* L'int\u00e9grit\u00e9 des informations enregistr\u00e9es dans votre Keystore  *" },
			{ "* The integrity of the information stored in the srckeystore*",
					"* L'int\u00e9grit\u00e9 des informations enregistr\u00e9es dans srckeystore*" },
			{ "* has NOT been verified!  In order to verify its integrity, *",
					"* n'a PAS \u00e9t\u00e9 v\u00e9rifi\u00e9e !  Pour cela, *" },
			{
					"* you must provide your keystore password.                  *",
					"* vous devez sp\u00e9cifier le mot de passe de votre Keystore.                  *" },
			{ "* you must provide the srckeystore password.                *",
					"* vous devez fournir le mot de passe srckeystore.                *" },

			{ "Certificate reply does not contain public key for <alias>",
					"La r\u00e9ponse au certificat ne contient pas de cl\u00e9 publique pour <{0}>" },
			{ "Incomplete certificate chain in reply",
					"Cha\u00eene de certificat incompl\u00e8te dans la r\u00e9ponse" },
			{ "Certificate chain in reply does not verify: ",
					"La cha\u00eene de certificat de la r\u00e9ponse ne concorde pas : " },
			{ "Top-level certificate in reply:\n",
					"Certificat du plus haut niveau dans la r\u00e9ponse :\n" },
			{ "... is not trusted. ", "... n'est pas digne de confiance. " },
			{ "Install reply anyway? [no]:  ",
					"Installer la r\u00e9ponse quand m\u00eame ? [non] :  " },
			{ "NO", "NON" },
			{ "Public keys in reply and keystore don't match",
					"Les cl\u00e9s publiques de la r\u00e9ponse et du Keystore ne concordent pas" },
			{ "Certificate reply and certificate in keystore are identical",
					"La r\u00e9ponse au certificat et le certificat du Keystore sont identiques" },
			{ "Failed to establish chain from reply",
					"Impossible de cr\u00e9er une cha\u00eene \u00e0 partir de la r\u00e9ponse" },
			{ "n", "n" },
			{ "Wrong answer, try again", "R\u00e9ponse incorrecte, recommencez" },
			{
					"Secret key not generated, alias <alias> already exists",
					"Cl\u00e9 secr\u00e8te non g\u00e9n\u00e9r\u00e9e, l'alias <{0}> existe d\u00e9j\u00e0" },
			{
					"Please provide -keysize for secret key generation",
					"Veuillez sp\u00e9cifier -keysize pour la g\u00e9n\u00e9ration de la cl\u00e9 secr\u00e8te" },
			{ "keytool usage:\n", "Syntaxe keytool :\n" },

			{ "Extensions: ", "Extensions\u00a0: " },

			{ "-certreq     [-v] [-protected]",
					"-certreq     [-v] [-protected]" },
			{ "\t     [-alias <alias>] [-sigalg <sigalg>]",
					"\t     [-alias <alias>] [-sigalg <sigalg>]" },
			{ "\t     [-file <csr_file>] [-keypass <keypass>]",
					"\t     [-file <csr_file>] [-keypass <mot_passe_cl\u00e9>]" },
			{ "\t     [-keystore <keystore>] [-storepass <storepass>]",
					"\t     [-keystore <keystore>] [-storepass <mot_passe_store>]" },
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
					"\t     [-alias <alias>] [-file <fichier_cert>]" },
			/** rest is same as -certreq starting from -keystore **/

			// {"-genkey      [-v] [-protected]",
			// "-genkey      [-v] [-protected]"},
			{ "-genkeypair  [-v] [-protected]",
					"-genkeypair  [-v] [-protected]" },
			{ "\t     [-alias <alias>]", "\t     [-alias <alias>]" },
			{ "\t     [-keyalg <keyalg>] [-keysize <keysize>]",
					"\t     [-keyalg <keyalg>] [-keysize <taille_cl\u00e9>]" },
			{ "\t     [-sigalg <sigalg>] [-dname <dname>]",
					"\t     [-sigalg <sigalg>] [-dname <nomd>]" },
			{ "\t     [-validity <valDays>] [-keypass <keypass>]",
					"\t     [-validity <joursVal>] [-keypass <mot_passe_cl\u00e9>]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-genseckey   [-v] [-protected]",
					"-genkeypair  [-v] [-protected]" },
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
					"\t     [-file <fichier_cert>] [-keypass <mot_passe_cl\u00e9>]" },
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
					"\t     [-srcprovidername <srcprovidername>]\n\t     [-destprovidername <destprovidername>]", // ligne
																													// trop
																													// longue,
																													// scind\u00e9e
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
					"\t     [-keypass <ancien_mot_passe_cl\u00e9>] [-new <nouveau_mot_passe_cl\u00e9>]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-list        [-v | -rfc] [-protected]",
					"-list        [-v | -rfc] [-protected]" },
			{ "\t     [-alias <alias>]", "\t     [-alias <alias>]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-printcert   [-v] [-file <cert_file>]",
					"-printcert   [-v] [-file <fichier_cert>]" },

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
					"Avertissement\u00a0: il n'existe pas de cl\u00e9 publique pour l'alias {0}.  V\u00e9rifiez que le keystore est correctement configur\u00e9." },
			{ "Warning: Class not found: class",
					"Avertissement : Classe introuvable : {0}" },
			{
					"Warning: Invalid argument(s) for constructor: arg",
					"Avertissement\u00a0: argument(s) non valide(s) pour le constructeur\u00a0: {0}" },
			{ "Illegal Principal Type: type", "Type de mandant non admis : {0}" },
			{ "Illegal option: option", "Option non admise : {0}" },
			{ "Usage: policytool [options]", "Syntaxe : policytool [options]" },
			{ "  [-file <file>]    policy file location",
					"  [-file <fichier>]    emplacement du fichier de r\u00e8gles" },
			{ "New", "Nouveau" },
			{ "Open", "Ouvrir" },
			{ "Save", "Enregistrer" },
			{ "Save As", "Enregistrer sous" },
			{ "View Warning Log", "Afficher le journal des avertissements" },
			{ "Exit", "Quitter" },
			{ "Add Policy Entry", "Ajouter une r\u00e8gle" },
			{ "Edit Policy Entry", "Modifier une r\u00e8gle" },
			{ "Remove Policy Entry", "Supprimer une r\u00e8gle" },
			{ "Edit", "Edition" },
			{ "Retain", "Conserver" },

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

			{ "Add Public Key Alias", "Ajouter un alias de cl\u00e9 publique" },
			{ "Remove Public Key Alias",
					"Supprimer un alias de cl\u00e9 publique" },
			{ "File", "Fichier" },
			{ "KeyStore", "KeyStore" },
			{ "Policy File:", "Fichier de r\u00e8gles :" },
			{ "Could not open policy file: policyFile: e.toString()",
					"Impossible d'ouvrir le fichier de strat\u00e9gie\u00a0: {0}: {1}" },
			{ "Policy Tool", "Policy Tool" },
			{
					"Errors have occurred while opening the policy configuration.  View the Warning Log for more information.",
					"Des erreurs se sont produites \u00e0 l'ouverture de la configuration de r\u00e8gles. Consultez le journal des avertissements pour obtenir des informations." },
			{ "Error", "Erreur" },
			{ "OK", "OK" },
			{ "Status", "\u00c9tat" },
			{ "Warning", "Avertissement" },
			{
					"Permission:                                                       ",
					"Permission :                                                       " },
			{ "Principal Type:", "Type de principal :" },
			{ "Principal Name:", "Nom de principal :" },
			{
					"Target Name:                                                    ",
					"Nom de cible :                                                    " },
			{
					"Actions:                                                             ",
					"Actions :                                                             " },
			{ "OK to overwrite existing file filename?",
					"Remplacer le fichier existant {0} ?" },
			{ "Cancel", "Annuler" },
			{ "CodeBase:", "Base de code :" },
			{ "SignedBy:", "Sign\u00e9 par :" },
			{ "Add Principal", "Ajouter un principal" },
			{ "Edit Principal", "Modifier un principal" },
			{ "Remove Principal", "Supprimer un principal" },
			{ "Principals:", "Principaux :" },
			{ "  Add Permission", " Ajouter une permission" },
			{ "  Edit Permission", " Modifier une permission" },
			{ "Remove Permission", "Supprimer une permission" },
			{ "Done", "Termin\u00e9" },
			{ "KeyStore URL:", "URL du KeyStore :" },
			{ "KeyStore Type:", "Type de KeyStore :" },
			{ "KeyStore Provider:", "Fournisseur du KeyStore :" },
			{ "KeyStore Password URL:", "URL du mot de passe du KeyStore :" },
			{ "Principals", "Principaux" },
			{ "  Edit Principal:", " Modifier un principal :" },
			{ "  Add New Principal:", " Ajouter un principal :" },
			{ "Permissions", "Permissions" },
			{ "  Edit Permission:", " Modifier une permission :" },
			{ "  Add New Permission:", " Ajouter une permission :" },
			{ "Signed By:", "Sign\u00e9 par :" },
			{
					"Cannot Specify Principal with a Wildcard Class without a Wildcard Name",
					"Impossible de sp\u00e9cifier un principal avec une classe g\u00e9n\u00e9rique sans nom de g\u00e9n\u00e9rique" },
			{ "Cannot Specify Principal without a Name",
					"Impossible de sp\u00e9cifier un principal sans nom" },
			{ "Permission and Target Name must have a value",
					"La permission et le nom de cible doivent avoir une valeur" },
			{ "Remove this Policy Entry?", "Supprimer cette r\u00e8gle ?" },
			{ "Overwrite File", "Remplacer le fichier" },
			{ "Policy successfully written to filename",
					"R\u00e8gle enregistr\u00e9e dans {0}" },
			{ "null filename", "Nom Null de fichier" },
			{ "Save changes?", "Enregistrer les modifications ?" },
			{ "Yes", "Oui" },
			{ "No", "Non" },
			{ "Policy Entry", "R\u00e8gle" },
			{ "Save Changes", "Enregistrer les changements" },
			{ "No Policy Entry selected",
					"Aucune r\u00e8gle s\u00e9lectionn\u00e9e" },
			{ "Unable to open KeyStore: ex.toString()",
					"Impossible d'ouvrir le keystore\u00a0: {0}" },
			{ "No principal selected", "Aucun principal s\u00e9lectionn\u00e9" },
			{ "No permission selected",
					"Aucune permission s\u00e9lectionn\u00e9e" },
			{ "name", "nom" },
			{ "configuration type", "type de configuration" },
			{ "environment variable name", "Nom variable de l'environnement" },
			{ "library name", "nom de biblioth\u00e8que" },
			{ "package name", "nom de package" },
			{ "policy type", "type de strat\u00e9gie" },
			{ "property name", "nom de propri\u00e9t\u00e9" },
			{ "provider name", "nom de fournisseur" },
			{ "Principal List", "Liste de mandants" },
			{ "Permission List", "Liste de droits" },
			{ "Code Base", "Base de codes" },
			{ "KeyStore U R L:", "URL du KeyStore :" },
			{ "KeyStore Password U R L:", "URL du mot de passe du KeyStore :" },

			// javax.security.auth.PrivateCredentialPermission
			{ "invalid null input(s)", "Entr\u00e9e() Null non valide(s)" },
			{ "actions can only be 'read'",
					"les actions peuvent \u00eatre accessibles en 'lecture' uniquement" },
			{ "permission name [name] syntax invalid: ",
					"syntaxe de nom de permission [{0}] non valide : " },
			{ "Credential Class not followed by a Principal Class and Name",
					"Classe Credential non suivie d'une classe et d'un nom de principal" },
			{ "Principal Class not followed by a Principal Name",
					"Classe de principal non suivie d'un nom de principal" },
			{ "Principal Name must be surrounded by quotes",
					"Le nom de principal doit \u00eatre entre guillemets" },
			{ "Principal Name missing end quote",
					"Guillemet fermant manquant pour nom de principal" },
			{
					"PrivateCredentialPermission Principal Class can not be a wildcard (*) value if Principal Name is not a wildcard (*) value",
					"La classe principale PrivateCredentialPermission ne peut \u00eatre une valeur g\u00e9n\u00e9rique (*) si le nom de principal n'est pas une valeur g\u00e9n\u00e9rique (*)" },
			{ "CredOwner:\n\tPrincipal Class = class\n\tPrincipal Name = name",
					"CredOwner :\n\tClasse principale = {0}\n\tNom principal = {1}" },

			// javax.security.auth.x500
			{ "provided null name", "nom Null sp\u00e9cifi\u00e9" },
			{ "provided null keyword map",
					"Mappage des mots cl\u00e9s Null fourni" },
			{ "provided null OID map", "Mappage OID Null fourni" },

			// javax.security.auth.Subject
			{ "invalid null AccessControlContext provided",
					"AccessControlContext Null sp\u00e9cifi\u00e9 non valide" },
			{ "invalid null action provided",
					"action Null sp\u00e9cifi\u00e9e non valide" },
			{ "invalid null Class provided",
					"classe Null sp\u00e9cifi\u00e9e non valide" },
			{ "Subject:\n", "Objet :\n" },
			{ "\tPrincipal: ", "\tPrincipal : " },
			{ "\tPublic Credential: ", "\tIdentit\u00e9 publique : " },
			{ "\tPrivate Credentials inaccessible\n",
					"\tIdentit\u00e9s priv\u00e9es inaccessibles\n" },
			{ "\tPrivate Credential: ", "\tIdentit\u00e9 priv\u00e9e : " },
			{ "\tPrivate Credential inaccessible\n",
					"\tIdentit\u00e9 priv\u00e9e inaccessible\n" },
			{ "Subject is read-only", "Objet en lecture seule" },
			{
					"attempting to add an object which is not an instance of java.security.Principal to a Subject's Principal Set",
					"tentative d'ajout d'un objet qui n'est pas une instance de java.security.Principal dans un ensemble principal d'objet" },
			{ "attempting to add an object which is not an instance of class",
					"tentative d''ajout d''un objet qui n''est pas une instance de {0}" },

			// javax.security.auth.login.AppConfigurationEntry
			{ "LoginModuleControlFlag: ", "LoginModuleControlFlag : " },

			// javax.security.auth.login.LoginContext
			{ "Invalid null input: name", "Entr\u00e9e Null non valide : nom" },
			{ "No LoginModules configured for name",
					"Aucun LoginModule configur\u00e9 pour {0}" },
			{ "invalid null Subject provided",
					"sujet Null sp\u00e9cifi\u00e9 non valide" },
			{ "invalid null CallbackHandler provided",
					"CallbackHandler Null sp\u00e9cifi\u00e9 non valide" },
			{ "null subject - logout called before login",
					"sujet Null - tentative de d\u00e9connexion avant connexion" },
			{
					"unable to instantiate LoginModule, module, because it does not provide a no-argument constructor",
					"impossible d''instancier LoginModule {0} car il ne fournit pas de constructeur sans argument" },
			{ "unable to instantiate LoginModule",
					"impossible d'instancier LoginModule" },
			{ "unable to instantiate LoginModule: ",
					"impossible d'instancier LoginModule\u00a0: " },
			{ "unable to find LoginModule class: ",
					"classe LoginModule introuvable : " },
			{ "unable to access LoginModule: ",
					"impossible d'acc\u00e9der \u00e0 LoginModule : " },
			{ "Login Failure: all modules ignored",
					"Echec de connexion : tous les modules ont \u00e9t\u00e9 ignor\u00e9s" },

			// sun.security.provider.PolicyFile

			{ "java.security.policy: error parsing policy:\n\tmessage",
					"java.security.policy : erreur d''analyse de {0} :\n\t{1}" },
			{
					"java.security.policy: error adding Permission, perm:\n\tmessage",
					"java.security.policy : erreur d''ajout de permission, {0} :\n\t{1}" },
			{ "java.security.policy: error adding Entry:\n\tmessage",
					"java.security.policy : erreur d''ajout d''entr\u00e9e :\n\t{0}" },
			{ "alias name not provided (pe.name)",
					"nom d''alias non fourni ({0})" },
			{ "unable to perform substitution on alias, suffix",
					"impossible d''effectuer une substitution pour l''alias, {0}" },
			{ "substitution value, prefix, unsupported",
					"valeur de substitution, {0}, non prise en charge" },
			{ "(", "(" },
			{ ")", ")" },
			{ "type can't be null", "le type ne peut \u00eatre Null" },

			// sun.security.provider.PolicyParser
			{
					"keystorePasswordURL can not be specified without also specifying keystore",
					"Impossible de sp\u00e9cifier keystorePasswordURL sans sp\u00e9cifier aussi le keystore" },
			{ "expected keystore type", "type keystore pr\u00e9vu" },
			{ "expected keystore provider", "fournisseur keystore pr\u00e9vu" },
			{ "multiple Codebase expressions", "expressions Codebase multiples" },
			{ "multiple SignedBy expressions", "expressions SignedBy multiples" },
			{ "SignedBy has empty alias", "SignedBy poss\u00e8de un alias vide" },
			{
					"can not specify Principal with a wildcard class without a wildcard name",
					"impossible de sp\u00e9cifier Principal avec une classe g\u00e9n\u00e9rique sans nom g\u00e9n\u00e9rique" },
			{ "expected codeBase or SignedBy or Principal",
					"codeBase ou SignedBy ou Principal pr\u00e9vu" },
			{ "expected permission entry",
					"entr\u00e9e de permission pr\u00e9vue" },
			{ "number ", "nombre " },
			{ "expected [expect], read [end of file]",
					"pr\u00e9vu [{0}], lecture [fin de fichier]" },
			{ "expected [;], read [end of file]",
					"pr\u00e9vu [;], lecture [fin de fichier]" },
			{ "line number: msg", "ligne {0} : {1}" },
			{ "line number: expected [expect], found [actual]",
					"ligne {0} : pr\u00e9vu [{1}], trouv\u00e9 [{2}]" },
			{ "null principalClass or principalName",
					"principalClass ou principalName Null" },

			// sun.security.pkcs11.SunPKCS11
			{ "PKCS11 Token [providerName] Password: ",
					"Mot de passe PKCS11 Token [{0}] : " },

			/* --- DEPRECATED --- */
			// javax.security.auth.Policy
			{ "unable to instantiate Subject-based policy",
					"impossible d'instancier la strat\u00e9gie Subject" } };

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
