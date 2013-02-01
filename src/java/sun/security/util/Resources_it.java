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
public class Resources_it extends java.util.ListResourceBundle {

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
			{ "keytool error: ", "Errore keytool: " },
			{ "Illegal option:  ", "Opzione non valida:  " },
			{ "Try keytool -help", "Provare keytool -help" },
			{ "Command option <flag> needs an argument.",
					"\u00c8 necessario specificare un argomento per l'opzione di comando {0}." },
			{
					"Warning:  Different store and key passwords not supported for PKCS12 KeyStores. Ignoring user-specified <command> value.",
					"Avviso: non sono supportate password diverse di chiave e di archivio per i KeyStore PKCS12. Il valore {0} specificato dall'utente verr\u00e0 ignorato." },
			{
					"-keystore must be NONE if -storetype is {0}",
					"Se -storetype \u00e8 impostato su {0}, -keystore deve essere impostato su NONE" },
			{
					"Too may retries, program terminated",
					"Il numero dei tentativi consentiti \u00e8 stato superato. Il programma verr\u00e0 chiuso." },
			{
					"-storepasswd and -keypasswd commands not supported if -storetype is {0}",
					"Se -storetype \u00e8 impostato su {0}, i comandi -storepasswd e -keypasswd non sono supportati" },
			{
					"-keypasswd commands not supported if -storetype is PKCS12",
					"Se -storetype \u00e8 impostato su PKCS12 i comandi -keypasswd non vengono supportati" },
			{
					"-keypass and -new can not be specified if -storetype is {0}",
					"Se -storetype \u00e8 impostato su {0}, non \u00e8 possibile specificare un valore per -keypass e -new" },
			{
					"if -protected is specified, then -storepass, -keypass, and -new must not be specified",
					"Se \u00e8 specificata l'opzione -protected, le opzioni -storepass, -keypass e -new non possono essere specificate" },
			{
					"if -srcprotected is specified, then -srcstorepass and -srckeypass must not be specified",
					"Se viene specificato -srcprotected, -srcstorepass e -srckeypass non dovranno essere specificati" },
			{
					"if keystore is not password protected, then -storepass, -keypass, and -new must not be specified",
					"Se il file keystore non \u00e8 protetto da password, non deve essere specificato alcun valore per -storepass, -keypass e -new" },
			{
					"if source keystore is not password protected, then -srcstorepass and -srckeypass must not be specified",
					"Se il file keystore non \u00e8 protetto da password, non deve essere specificato alcun valore per -srcstorepass e -srckeypass" },
			{ "Validity must be greater than zero",
					"La validit\u00e0 deve essere maggiore di zero" },
			{ "provName not a provider", "{0} non \u00e8 un provider" },
			{ "Usage error: no command provided",
					"Errore di utilizzo: nessun comando specificato" },
			{ "Usage error, <arg> is not a legal command",
					"Errore di utilizzo. {0} non \u00e8 un comando valido." },
			{ "Source keystore file exists, but is empty: ",
					"Il file keystore di origine esiste, ma \u00e8 vuoto: " },
			{ "Please specify -srckeystore", "Specificare -srckeystore" },
			{ "Must not specify both -v and -rfc with 'list' command",
					"Impossibile specificare sia -v sia -rfc con il comando 'list'" },
			{ "Key password must be at least 6 characters",
					"La password della chiave deve contenere almeno 6 caratteri" },
			{ "New password must be at least 6 characters",
					"La nuova password deve contenere almeno 6 caratteri" },
			{ "Keystore file exists, but is empty: ",
					"Il file keystore esiste ma \u00e8 vuoto: " },
			{ "Keystore file does not exist: ", "Il file keystore non esiste: " },
			{ "Must specify destination alias",
					"\u00c8 necessario specificare l'alias di destinazione" },
			{ "Must specify alias", "\u00c8 necessario specificare l'alias" },
			{ "Keystore password must be at least 6 characters",
					"La password del keystore deve contenere almeno 6 caratteri" },
			{ "Enter keystore password:  ",
					"Immettere la password del keystore:  " },
			{ "Enter source keystore password:  ",
					"Immettere la password del keystore di origine:  " },
			{ "Enter destination keystore password:  ",
					"Immettere la password del keystore di destinazione:  " },
			{
					"Keystore password is too short - must be at least 6 characters",
					"La password del keystore \u00e8 troppo corta - deve contenere almeno 6 caratteri" },
			{ "Unknown Entry Type", "Tipo di voce sconosciuto" },
			{ "Too many failures. Alias not changed",
					"Si sono verificati troppi errori. L'alias non \u00e8 stato modificato." },
			{ "Entry for alias <alias> successfully imported.",
					"La voce dell'alias {0} \u00e8 stata importata." },
			{ "Entry for alias <alias> not imported.",
					"La voce dell'alias {0} non \u00e8 stata importata." },
			{
					"Problem importing entry for alias <alias>: <exception>.\nEntry for alias <alias> not imported.",
					"Si \u00e8 verificato un problema durante l'importazione della voce dell'alias {0}: {1}.\nLa voce dell'alias {0} non \u00e8 stata importata." },
			{
					"Import command completed:  <ok> entries successfully imported, <fail> entries failed or cancelled",
					"Importazione completata:  {0} voci importate, {1} voci non importate o annullate" },
			{
					"Warning: Overwriting existing alias <alias> in destination keystore",
					"Avviso: sovrascrittura in corso dell'alias {0} nel file keystore di destinazione" },
			{ "Existing entry alias <alias> exists, overwrite? [no]:  ",
					"La voce dell'alias {0} esiste gi\u00e0. Sovrascrivere? [no]:  " },
			{ "Too many failures - try later", "Troppi errori - riprovare" },
			{ "Certification request stored in file <filename>",
					"La richiesta di certificazione \u00e8 memorizzata nel file <{0}>" },
			{ "Submit this to your CA", "Inviarla alla propria CA" },
			{
					"if alias not specified, destalias, srckeypass, and destkeypass must not be specified",
					"Se l'alias non \u00e8 specificato, destalias, srckeypass e destkeypass non dovranno essere specificati" },
			{ "Certificate stored in file <filename>",
					"Il certificato \u00e8 memorizzato nel file <{0}>" },
			{ "Certificate reply was installed in keystore",
					"La risposta del certificato \u00e8 stata installata nel keystore" },
			{ "Certificate reply was not installed in keystore",
					"La risposta del certificato non \u00e8 stata installata nel keystore" },
			{ "Certificate was added to keystore",
					"Il certificato \u00e8 stato aggiunto al keystore" },
			{ "Certificate was not added to keystore",
					"Il certificato non \u00e8 stato aggiunto al keystore" },
			{ "[Storing ksfname]", "[Memorizzazione di {0}] in corso" },
			{ "alias has no public key (certificate)",
					"{0} non dispone di chiave pubblica (certificato)" },
			{ "Cannot derive signature algorithm",
					"Impossibile derivare l'algoritmo di firma" },
			{ "Alias <alias> does not exist", "L''alias <{0}> non esiste" },
			{ "Alias <alias> has no certificate",
					"L''alias <{0}> non dispone di certificato" },
			{
					"Key pair not generated, alias <alias> already exists",
					"Non \u00e8 stata generata la coppia di chiavi, l''alias <{0}> \u00e8 gi\u00e0 esistente" },
			{ "Cannot derive signature algorithm",
					"Impossibile derivare l'algoritmo di firma" },
			{
					"Generating keysize bit keyAlgName key pair and self-signed certificate (sigAlgName) with a validity of validality days\n\tfor: x500Name",
					"Generazione in corso di una coppia di chiavi {1} da {0} bit e di un certificato autofirmato ({2}) con una validit\u00e0 di {3} giorni\n\tper: {4}" },
			{ "Enter key password for <alias>",
					"Immettere la password della chiave per <{0}>" },
			{ "\t(RETURN if same as keystore password):  ",
					"\t(INVIO se corrisponde alla password del keystore):  " },
			{
					"Key password is too short - must be at least 6 characters",
					"La password della chiave \u00e8 troppo corta - deve contenere almeno 6 caratteri" },
			{ "Too many failures - key not added to keystore",
					"Troppi errori - la chiave non \u00e8 stata aggiunta al keystore" },
			{ "Destination alias <dest> already exists",
					"L''alias di destinazione <{0}> \u00e8 gi\u00e0 esistente" },
			{ "Password is too short - must be at least 6 characters",
					"La password \u00e8 troppo corta - deve contenere almeno 6 caratteri" },
			{ "Too many failures. Key entry not cloned",
					"Troppi errori. Il valore della chiave non \u00e8 stato clonato" },
			{ "key password for <alias>", "password della chiave per <{0}>" },
			{ "Keystore entry for <id.getName()> already exists",
					"L''entry nel keystore per <{0}> \u00e8 gi\u00e0 esistente" },
			{ "Creating keystore entry for <id.getName()> ...",
					"Creazione dell''entry nel keystore per <{0}> in corso..." },
			{ "No entries from identity database added",
					"Nessuna entry aggiunta dal database di identit\u00e0" },
			{ "Alias name: alias", "Nome alias: {0}" },
			{ "Creation date: keyStore.getCreationDate(alias)",
					"Data di creazione: {0,date}" },
			{ "alias, keyStore.getCreationDate(alias), ", "{0}, {1,date}, " },
			{ "alias, ", "{0}, " },
			{ "Entry type: <type>", "Tipo di voce: {0}" },
			{ "Certificate chain length: ", "Lunghezza catena certificati: " },
			{ "Certificate[(i + 1)]:", "Certificato[{0,number,integer}]:" },
			{ "Certificate fingerprint (MD5): ",
					"Impronta digitale certificato (MD5): " },
			{ "Entry type: trustedCertEntry\n",
					"Tipo entry: trustedCertEntry\n" },
			{ "trustedCertEntry,", "trustedCertEntry," },
			{ "Keystore type: ", "Tipo keystore: " },
			{ "Keystore provider: ", "Provider keystore: " },
			{ "Your keystore contains keyStore.size() entry",
					"Il keystore contiene {0,number,integer} entry" },
			{ "Your keystore contains keyStore.size() entries",
					"Il keystore contiene {0,number,integer} entry" },
			{ "Failed to parse input", "Impossibile analizzare l'input" },
			{ "Empty input", "Input vuoto" },
			{ "Not X.509 certificate", "Il certificato non \u00e8 X.509" },
			{ "Cannot derive signature algorithm",
					"Impossibile derivare l'algoritmo di firma" },
			{ "alias has no public key", "{0} non dispone di chiave pubblica" },
			{ "alias has no X.509 certificate",
					"{0} non dispone di certificato X.509" },
			{ "New certificate (self-signed):",
					"Nuovo certificato (autofirmato):" },
			{ "Reply has no certificates",
					"La risposta non dispone di certificati" },
			{
					"Certificate not imported, alias <alias> already exists",
					"Impossibile importare il certificato, l''alias <{0}> \u00e8 gi\u00e0 esistente" },
			{ "Input not an X.509 certificate",
					"L'input non \u00e8 un certificato X.509" },
			{
					"Certificate already exists in keystore under alias <trustalias>",
					"Il certificato esiste gi\u00e0 nel keystore con alias <{0}>" },
			{ "Do you still want to add it? [no]:  ",
					"Aggiungerlo ugualmente? [no]:  " },
			{
					"Certificate already exists in system-wide CA keystore under alias <trustalias>",
					"Il certificato esiste gi\u00e0 nel keystore CA con alias <{0}>" },
			{ "Do you still want to add it to your own keystore? [no]:  ",
					"Aggiungerlo al proprio keystore? [no]:  " },
			{ "Trust this certificate? [no]:  ",
					"Considerare attendibile questo certificato? [no]:  " },
			{ "YES", "S\u00ec" },
			{ "New prompt: ", "Nuova {0}: " },
			{ "Passwords must differ", "Le password non devono coincidere" },
			{ "Re-enter new prompt: ", "Reimmettere nuova {0}: " },
			{ "Re-enter new password: ",
					"Immettere nuovamente la nuova password: " },
			{ "They don't match. Try again", "Non corrispondono. Riprovare." },
			{ "Enter prompt alias name:  ", "Immettere nome alias {0}:  " },
			{
					"Enter new alias name\t(RETURN to cancel import for this entry):  ",
					"Immettere un nuovo nome alias\t(premere INVIO per annullare l'importazione della voce):  " },
			{ "Enter alias name:  ", "Immettere nome alias:  " },
			{ "\t(RETURN if same as for <otherAlias>)",
					"\t(INVIO se corrisponde al nome di <{0}>" },
			{
					"*PATTERN* printX509Cert",
					"Proprietario: {0}\nAutorit\u00e0 emittente: {1}\nNumero di serie: {2}\nValido da: {3} a: {4}\nImpronte digitali certificato:\n\t MD5:  {5}\n\t SHA1: {6}\n\t Nome algoritmo firma: {7}\n\t Versione: {8}" },
			{ "What is your first and last name?", "Specificare nome e cognome" },
			{ "What is the name of your organizational unit?",
					"Specificare il nome dell'unit\u00e0 aziendale" },
			{ "What is the name of your organization?",
					"Specificare il nome dell'azienda" },
			{ "What is the name of your City or Locality?",
					"Specificare la localit\u00e0" },
			{ "What is the name of your State or Province?",
					"Specificare la provincia" },
			{ "What is the two-letter country code for this unit?",
					"Specificare il codice a due lettere del paese in cui si trova l'unit\u00e0" },
			{ "Is <name> correct?", "Il dato {0} \u00e8 corretto?" },
			{ "no", "no" },
			{ "yes", "s\u00ec" },
			{ "y", "s" },
			{ "  [defaultValue]:  ", " [{0}]:  " },
			{ "Alias <alias> has no key",
					"All'alias <{0}> non \u00e8 associata alcuna chiave" },
			{
					"Alias <alias> references an entry type that is not a private key entry.  The -keyclone command only supports cloning of private key entries",
					"L'alias <{0}> fa riferimento a un tipo di voce che non \u00e8 una voce di chiave privata. Il comando -keyclone supporta solo la duplicazione delle voci di chiave private." },

			{ "*****************  WARNING WARNING WARNING  *****************",
					"*****************  AVVISO  AVVISO  AVVISO  *****************" },

			// Translators of the following 5 pairs, ATTENTION:
			// the next 5 string pairs are meant to be combined into 2
			// paragraphs,
			// 1+3+4 and 2+3+5. make sure your translation also does.
			{ "* The integrity of the information stored in your keystore  *",
					"* L'integrit\u00e0 delle informazioni memorizzate nel keystore    *" },
			{ "* The integrity of the information stored in the srckeystore*",
					"* L'integrit\u00e0 delle informazioni salvate nel srckeystore*" },
			{ "* has NOT been verified!  In order to verify its integrity, *",
					"* NON \u00e8 stata verificata! A tale scopo \u00e8 necessario fornire *" },
			{ "* you must provide your keystore password.                  *",
					"* la password del keystore.                                 *" },
			{ "* you must provide the srckeystore password.                *",
					"* \u00c8 necessario fornire la password per il srckeystore.                *" },

			{ "Certificate reply does not contain public key for <alias>",
					"La risposta del certificato non contiene la chiave pubblica per <{0}>" },
			{ "Incomplete certificate chain in reply",
					"Catena dei certificati incompleta nella risposta" },
			{ "Certificate chain in reply does not verify: ",
					"La catena dei certificati nella risposta non verifica: " },
			{ "Top-level certificate in reply:\n",
					"Certificato di primo livello nella risposta:\n" },
			{ "... is not trusted. ",
					"... non \u00e8 considerato attendibile. " },
			{ "Install reply anyway? [no]:  ",
					"Installare la risposta? [no]:  " },
			{ "NO", "NO" },
			{ "Public keys in reply and keystore don't match",
					"Le chiavi pubbliche nella risposta e nel keystore non corrispondono" },
			{ "Certificate reply and certificate in keystore are identical",
					"La risposta del certificato e il certificato nel keystore sono identici" },
			{ "Failed to establish chain from reply",
					"Impossibile stabilire la catena dalla risposta" },
			{ "n", "n" },
			{ "Wrong answer, try again", "Risposta errata, riprovare" },
			{ "Secret key not generated, alias <alias> already exists",
					"La chiave segreta non \u00e8 stata generata; l'alias <{0}> esiste gi\u00e0" },
			{ "Please provide -keysize for secret key generation",
					"Specificare il valore -keysize per la generazione della chiave segreta" },
			{ "keytool usage:\n", "utilizzo keytool:\n" },

			{ "Extensions: ", "Estensioni: " },

			{ "-certreq     [-v] [-protected]",
					"-certreq     [-v] [-protected]" },
			{ "\t     [-alias <alias>] [-sigalg <sigalg>]",
					"\t     [-alias <alias>] [-sigalg <algfirma>]" },
			{ "\t     [-file <csr_file>] [-keypass <keypass>]",
					"\t     [-file <file_csr>] [-keypass <keypass>]" },
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
					"\t     [-alias <alias>] [-file <file_cert>]" },
			/** rest is same as -certreq starting from -keystore **/

			// {"-genkey      [-v] [-protected]",
			// "-genkey      [-v] [-protected]"},
			{ "-genkeypair  [-v] [-protected]",
					"-genkeypair  [-v] [-protected]" },
			{ "\t     [-alias <alias>]", "\t     [-alias <alias>]" },
			{ "\t     [-keyalg <keyalg>] [-keysize <keysize>]",
					"\t     [-keyalg <algchiave>] [-keysize <dimchiave>]" },
			{ "\t     [-sigalg <sigalg>] [-dname <dname>]",
					"\t     [-sigalg <algfirma>] [-dname <nomed>]" },
			{ "\t     [-validity <valDays>] [-keypass <keypass>]",
					"\t     [-validity <Giornival>] [-keypass <keypass>]" },
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
					"\t     [-file <file_cert>] [-keypass <keypass>]" },
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
					"\t     [-srcprovidername <srcprovidername>]\n\t     [-destprovidername <destprovidername>]", // riga
																													// troppo
																													// lunga,
																													// divisa
																													// in
																													// 2
																													// parti
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
					"\t     [-keypass <keypass_vecchio>] [-new <keypass_nuovo>]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-list        [-v | -rfc] [-protected]",
					"-list        [-v | -rfc] [-protected]" },
			{ "\t     [-alias <alias>]", "\t     [-alias <alias>]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-printcert   [-v] [-file <cert_file>]",
					"-printcert   [-v] [-file <file_cert>]" },

			// {"-selfcert    [-v] [-protected]",
			// "-selfcert    [-v] [-protected]"},
			{ "\t     [-alias <alias>]", "\t     [-alias <alias>]" },
			// {"\t     [-dname <dname>] [-validity <valDays>]",
			// "\t     [-dname <dname>] [-validity <valDays>]"},
			// {"\t     [-keypass <keypass>] [-sigalg <sigalg>]",
			// "\t     [-keypass <keypass>] [-sigalg <sigalg>]"},
			/** rest is same as -certreq starting from -keystore **/

			{ "-storepasswd [-v] [-new <new_storepass>]",
					"-storepasswd [-v] [-new <storepass_nuovo>]" },
			/** rest is same as -certreq starting from -keystore **/

			// policytool
			{
					"Warning: A public key for alias 'signers[i]' does not exist.  Make sure a KeyStore is properly configured.",
					"Avviso: non esiste una chiave pubblica per l'alias {0}. Verificare che il KeyStore sia configurato correttamente." },
			{ "Warning: Class not found: class",
					"Avviso: Classe non trovata: {0}" },
			{ "Warning: Invalid argument(s) for constructor: arg",
					"Avviso: argomento o argomenti non validi per il costruttore {0}" },
			{ "Illegal Principal Type: type", "Tipo principale non valido: {0}" },
			{ "Illegal option: option", "Opzione non valida: {0}" },
			{ "Usage: policytool [options]", "Utilizzo: policytool [opzioni]" },
			{ "  [-file <file>]    policy file location",
					" [-file <file>]    posizione del policy file" },
			{ "New", "Nuovo" },
			{ "Open", "Apri" },
			{ "Save", "Salva" },
			{ "Save As", "Salva con nome" },
			{ "View Warning Log", "Visualizza registro avvisi" },
			{ "Exit", "Esci" },
			{ "Add Policy Entry", "Aggiungi policy entry" },
			{ "Edit Policy Entry", "Modifica policy entry" },
			{ "Remove Policy Entry", "Elimina policy entry" },
			{ "Edit", "Modifica" },
			{ "Retain", "Mantieni" },

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

			{ "Add Public Key Alias", "Aggiungi alias chiave pubblica" },
			{ "Remove Public Key Alias", "Elimina alias chiave pubblica" },
			{ "File", "File" },
			{ "KeyStore", "KeyStore" },
			{ "Policy File:", "Policy file:" },
			{ "Could not open policy file: policyFile: e.toString()",
					"Impossibile aprire il file di criteri {0}: {1}" },
			{ "Policy Tool", "Policy Tool" },
			{
					"Errors have occurred while opening the policy configuration.  View the Warning Log for more information.",
					"Si sono verificati errori durante l'apertura della configurazione della policy. Consultare il registro degli avvisi per ulteriori informazioni." },
			{ "Error", "Errore" },
			{ "OK", "OK" },
			{ "Status", "Stato" },
			{ "Warning", "Avviso" },
			{
					"Permission:                                                       ",
					"Permesso:                                                       " },
			{ "Principal Type:", "Tipo Principal:" },
			{ "Principal Name:", "Nome Principal:" },
			{
					"Target Name:                                                    ",
					"Nome obiettivo:                                                    " },
			{
					"Actions:                                                             ",
					"Azioni:                                                             " },
			{ "OK to overwrite existing file filename?",
					"OK per sovrascrivere il file {0}?" },
			{ "Cancel", "Annulla" },
			{ "CodeBase:", "CodeBase:" },
			{ "SignedBy:", "SignedBy:" },
			{ "Add Principal", "Aggiungi Principal" },
			{ "Edit Principal", "Modifica Principal" },
			{ "Remove Principal", "Elimina Principal" },
			{ "Principals:", "Principal:" },
			{ "  Add Permission", " Aggiungi permesso" },
			{ "  Edit Permission", " Modifica permesso" },
			{ "Remove Permission", "Elimina permesso" },
			{ "Done", "Fine" },
			{ "KeyStore URL:", "URL KeyStore:" },
			{ "KeyStore Type:", "Tipo KeyStore:" },
			{ "KeyStore Provider:", "Fornitore KeyStore:" },
			{ "KeyStore Password URL:", "URL password KeyStore:" },
			{ "Principals", "Principal:" },
			{ "  Edit Principal:", " Modifica Principal:" },
			{ "  Add New Principal:", " Aggiungi nuova Principal:" },
			{ "Permissions", "Permessi" },
			{ "  Edit Permission:", " Modifica permesso:" },
			{ "  Add New Permission:", " Aggiungi nuovo permesso:" },
			{ "Signed By:", "Firmato da:" },
			{
					"Cannot Specify Principal with a Wildcard Class without a Wildcard Name",
					"Impossibile specificare Principal con una classe wildcard senza un nome wildcard" },
			{ "Cannot Specify Principal without a Name",
					"Impossibile specificare Principal senza un nome" },
			{ "Permission and Target Name must have a value",
					"Il permesso e il nome di obiettivo non possono essere nulli" },
			{ "Remove this Policy Entry?", "Eliminare questa policy entry?" },
			{ "Overwrite File", "Sovrascrivi file" },
			{ "Policy successfully written to filename",
					"La policy \u00e8 stata scritta correttamente in {0}" },
			{ "null filename", "nome file nullo" },
			{ "Save changes?", "Salvare le modifiche?" },
			{ "Yes", "S\u00ec" },
			{ "No", "No" },
			{ "Policy Entry", "Policy entry" },
			{ "Save Changes", "Salva le modifiche" },
			{ "No Policy Entry selected", "Nessuna policy entry selezionata" },
			{ "Unable to open KeyStore: ex.toString()",
					"Impossibile aprire il KeyStore: {0}" },
			{ "No principal selected", "Nessuna Principal selezionata" },
			{ "No permission selected", "Nessun permesso selezionato" },
			{ "name", "nome" },
			{ "configuration type", "tipo di configurazione" },
			{ "environment variable name", "nome variabile ambiente" },
			{ "library name", "nome libreria" },
			{ "package name", "nome package" },
			{ "policy type", "tipo di criteri" },
			{ "property name", "nome propriet\u00e0" },
			{ "provider name", "nome provider" },
			{ "Principal List", "Elenco principale" },
			{ "Permission List", "Elenco autorizzazioni" },
			{ "Code Base", "Codebase" },
			{ "KeyStore U R L:", "URL KeyStore:" },
			{ "KeyStore Password U R L:", "URL password KeyStore:" },

			// javax.security.auth.PrivateCredentialPermission
			{ "invalid null input(s)", "input nullo/i non valido/i" },
			{ "actions can only be 'read'",
					"le azioni possono essere solamente 'lette'" },
			{ "permission name [name] syntax invalid: ",
					"sintassi non valida per il nome di permesso [{0}]: " },
			{ "Credential Class not followed by a Principal Class and Name",
					"la classe Credential non \u00e8 seguita da un nome e una classe Principal" },
			{ "Principal Class not followed by a Principal Name",
					"la classe Principal non \u00e8 seguita da un nome Principal" },
			{ "Principal Name must be surrounded by quotes",
					"il nome Principal deve essere compreso tra virgolette" },
			{ "Principal Name missing end quote",
					"virgolette di chiusura del nome Principal mancanti" },
			{
					"PrivateCredentialPermission Principal Class can not be a wildcard (*) value if Principal Name is not a wildcard (*) value",
					"la classe Principal PrivateCredentialPermission non pu\u00f2 essere un valore wildcard (*) se il nome Principal a sua volta non \u00e8 un valore wildcard (*)" },
			{ "CredOwner:\n\tPrincipal Class = class\n\tPrincipal Name = name",
					"ProprCred:\n\tclasse Principal = {0}\n\tNome Principal = {1}" },

			// javax.security.auth.x500
			{ "provided null name", "il nome fornito \u00e8 nullo" },
			{ "provided null keyword map",
					"specificata mappa parole chiave null" },
			{ "provided null OID map", "specificata mappa OID null" },

			// javax.security.auth.Subject
			{ "invalid null AccessControlContext provided",
					"fornito un valore nullo non valido per AccessControlContext" },
			{ "invalid null action provided",
					"fornita un'azione nulla non valida" },
			{ "invalid null Class provided",
					"fornita una classe nulla non valida" },
			{ "Subject:\n", "Subject:\n" },
			{ "\tPrincipal: ", "\tPrincipal: " },
			{ "\tPublic Credential: ", "\tCredenziale pubblica: " },
			{ "\tPrivate Credentials inaccessible\n",
					"\tImpossibile accedere alle credenziali private\n" },
			{ "\tPrivate Credential: ", "\tCredenziale privata: " },
			{ "\tPrivate Credential inaccessible\n",
					"\tImpossibile accedere alla credenziale privata\n" },
			{ "Subject is read-only", "Subject \u00e8 di sola lettura" },
			{
					"attempting to add an object which is not an instance of java.security.Principal to a Subject's Principal Set",
					"si \u00e8 tentato di aggiungere un oggetto che non \u00e8 un'istanza di java.security.Principal a un set Principal di Subject" },
			{ "attempting to add an object which is not an instance of class",
					"si \u00e8 tentato di aggiungere un oggetto che non \u00e8 un''istanza di {0}" },

			// javax.security.auth.login.AppConfigurationEntry
			{ "LoginModuleControlFlag: ", "LoginModuleControlFlag: " },

			// javax.security.auth.login.LoginContext
			{ "Invalid null input: name", "Input nullo non valido: nome" },
			{ "No LoginModules configured for name",
					"Nessun LoginModule configurato per {0}" },
			{ "invalid null Subject provided",
					"fornito un valore nullo non valido per Subject" },
			{ "invalid null CallbackHandler provided",
					"fornito un valore nullo non valido per CallbackHandler" },
			{ "null subject - logout called before login",
					"subject nullo - il logout \u00e8 stato chiamato prima del login" },
			{
					"unable to instantiate LoginModule, module, because it does not provide a no-argument constructor",
					"impossibile istanziare il LoginModule {0} in quando non restituisce un valore vuoto per il costruttore" },
			{ "unable to instantiate LoginModule",
					"impossibile istanziare LoginModule" },
			{ "unable to instantiate LoginModule: ",
					"Impossibile creare un'istanza di LoginModule: " },
			{ "unable to find LoginModule class: ",
					"impossibile trovare la classe LoginModule: " },
			{ "unable to access LoginModule: ",
					"impossibile accedere a LoginModule " },
			{ "Login Failure: all modules ignored",
					"Errore di login: tutti i moduli sono stati ignorati" },

			// sun.security.provider.PolicyFile

			{ "java.security.policy: error parsing policy:\n\tmessage",
					"java.security.policy: errore nell''analisi di {0}:\n\t{1}" },
			{
					"java.security.policy: error adding Permission, perm:\n\tmessage",
					"java.security.policy: errore nell''aggiunta del permesso {0}:\n\t{1}" },
			{ "java.security.policy: error adding Entry:\n\tmessage",
					"java.security.policy: errore nell''aggiunta dell''entry:\n\t{0}" },
			{ "alias name not provided (pe.name)",
					"impossibile fornire nome alias ({0})" },
			{ "unable to perform substitution on alias, suffix",
					"impossibile eseguire una sostituzione sull''alias, {0}" },
			{ "substitution value, prefix, unsupported",
					"valore sostituzione, {0}, non supportato" },
			{ "(", "(" },
			{ ")", ")" },
			{ "type can't be null", "il tipo non pu\u00f2 essere nullo" },

			// sun.security.provider.PolicyParser
			{
					"keystorePasswordURL can not be specified without also specifying keystore",
					"Impossibile specificare keystorePasswordURL senza specificare anche il keystore" },
			{ "expected keystore type", "tipo di keystore previsto" },
			{ "expected keystore provider", "provider di keystore previsto" },
			{ "multiple Codebase expressions", "espressioni Codebase multiple" },
			{ "multiple SignedBy expressions", "espressioni SignedBy multiple" },
			{ "SignedBy has empty alias", "SignedBy presenta un alias vuoto" },
			{
					"can not specify Principal with a wildcard class without a wildcard name",
					"impossibile specificare Principal con una classe wildcard senza un nome wildcard" },
			{ "expected codeBase or SignedBy or Principal",
					"previsto codeBase o SignedBy o Principal" },
			{ "expected permission entry", "prevista entry di permesso" },
			{ "number ", "numero " },
			{ "expected [expect], read [end of file]",
					"previsto [{0}], letto [end of file]" },
			{ "expected [;], read [end of file]",
					"previsto [;], letto [end of file]" },
			{ "line number: msg", "riga {0}: {1}" },
			{ "line number: expected [expect], found [actual]",
					"riga {0}: previsto [{1}], trovato [{2}]" },
			{ "null principalClass or principalName",
					"principalClass o principalName nullo" },

			// sun.security.pkcs11.SunPKCS11
			{ "PKCS11 Token [providerName] Password: ",
					"Password per token PKCS11 [{0}]: " },

			/* --- DEPRECATED --- */
			// javax.security.auth.Policy
			{ "unable to instantiate Subject-based policy",
					"impossibile creare un'istanza dei criteri basati su Subject" } };

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
