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
public class Resources_es extends java.util.ListResourceBundle {

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
			{ "keytool error: ", "error de keytool: " },
			{ "Illegal option:  ", "Opci\u00f3n no permitida:  " },
			{ "Try keytool -help", "Probar keytool -help" },
			{ "Command option <flag> needs an argument.",
					"La opci\u00f3n de comando {0} necesita un argumento." },
			{
					"Warning:  Different store and key passwords not supported for PKCS12 KeyStores. Ignoring user-specified <command> value.",
					"Advertencia: Los archivos de almac\u00e9n de claves en formato PKCS12 no admiten contrase\u00f1as de clave y almacenamiento distintas. Se omite el valor especificado por el usuario {0}." },
			{ "-keystore must be NONE if -storetype is {0}",
					"-keystore debe ser NONE si -storetype es {0}" },
			{ "Too may retries, program terminated",
					"Ha habido demasiados intentos, se ha cerrado el programa" },
			{
					"-storepasswd and -keypasswd commands not supported if -storetype is {0}",
					"los comandos -storepasswd y -keypasswd no se admiten si -storetype es {0}" },
			{ "-keypasswd commands not supported if -storetype is PKCS12",
					"los comandos -keypasswd no se admiten si -storetype es PKCS12" },
			{ "-keypass and -new can not be specified if -storetype is {0}",
					"-keypass y -new no se pueden especificar si -storetype es {0}" },
			{
					"if -protected is specified, then -storepass, -keypass, and -new must not be specified",
					"si se especifica -protected, no deben especificarse -storepass, -keypass ni -new" },
			{
					"if -srcprotected is specified, then -srcstorepass and -srckeypass must not be specified",
					"si se especifica -srcprotected, no se puede especificar -srcstorepass ni -srckeypass" },
			{
					"if keystore is not password protected, then -storepass, -keypass, and -new must not be specified",
					"Si keystore no est\u00e1 protegido por contrase\u00f1a, no se deben especificar -storepass, -keypass ni -new" },
			{
					"if source keystore is not password protected, then -srcstorepass and -srckeypass must not be specified",
					"Si keystore de origen no est\u00e1 protegido por contrase\u00f1a, no se deben especificar -srcstorepass ni -srckeypass" },
			{ "Validity must be greater than zero",
					"La validez debe ser mayor que cero" },
			{ "provName not a provider", "{0} no es un proveedor" },
			{ "Usage error: no command provided",
					"Error de uso: no se ha proporcionado ning\u00fan comando" },
			{ "Usage error, <arg> is not a legal command",
					"Error de uso, {0} no es un comando legal" },
			{
					"Source keystore file exists, but is empty: ",
					"El archivo de almac\u00e9n de claves de origen existe, pero est\u00e1 vac\u00edo: " },
			{ "Please specify -srckeystore", "Especifique -srckeystore" },
			{ "Must not specify both -v and -rfc with 'list' command",
					"No se deben especificar -v y -rfc simult\u00e1neamente con el comando 'list'" },
			{ "Key password must be at least 6 characters",
					"La contrase\u00f1a clave debe tener al menos 6 caracteres" },
			{ "New password must be at least 6 characters",
					"La nueva contrase\u00f1a debe tener al menos 6 caracteres" },
			{ "Keystore file exists, but is empty: ",
					"El archivo de almac\u00e9n de claves existe, pero est\u00e1 vac\u00edo " },
			{ "Keystore file does not exist: ",
					"El archivo de almac\u00e9n de claves no existe: " },
			{ "Must specify destination alias",
					"Se debe especificar alias de destino" },
			{ "Must specify alias", "Se debe especificar alias" },
			{
					"Keystore password must be at least 6 characters",
					"La contrase\u00f1a del almac\u00e9n de claves debe tener al menos 6 caracteres" },
			{ "Enter keystore password:  ",
					"Escriba la contrase\u00f1a del almac\u00e9n de claves:  " },
			{ "Enter source keystore password:  ",
					"Escribir contrase\u00f1a de almac\u00e9n de claves de origen:  " },
			{ "Enter destination keystore password:  ",
					"Escribir contrase\u00f1a de almac\u00e9n de claves de destino:  " },
			{
					"Keystore password is too short - must be at least 6 characters",
					"La contrase\u00f1a del almac\u00e9n de claves es demasiado corta, debe tener al menos 6 caracteres" },
			{ "Unknown Entry Type", "Tipo de entrada desconocido" },
			{ "Too many failures. Alias not changed",
					"Demasiados errores. No se ha cambiado el alias" },
			{ "Entry for alias <alias> successfully imported.",
					"Las entradas del alias {0} se han importado correctamente." },
			{ "Entry for alias <alias> not imported.",
					"La entrada del alias {0} no se ha importado." },
			{
					"Problem importing entry for alias <alias>: <exception>.\nEntry for alias <alias> not imported.",
					"Problema al importar la entrada del alias {0}: {1}.\nNo se ha importado la entrada del alias {0}." },
			{
					"Import command completed:  <ok> entries successfully imported, <fail> entries failed or cancelled",
					"Comando de importaci\u00f3n completado:  {0} entradas importadas correctamente, {1} entradas incorrectas o canceladas" },
			{
					"Warning: Overwriting existing alias <alias> in destination keystore",
					"Advertencia: Sobrescribiendo el alias {0} en el almac\u00e9n de claves de destino" },
			{ "Existing entry alias <alias> exists, overwrite? [no]:  ",
					"El alias de entrada {0} ya existe, \u00bfdesea sobrescribirlo? [no]:  " },
			{ "Too many failures - try later",
					"Demasiados fallos; int\u00e9ntelo m\u00e1s adelante" },
			{ "Certification request stored in file <filename>",
					"Solicitud de certificaci\u00f3n almacenada en el archivo <{0}>" },
			{ "Submit this to your CA", "Enviar a la CA" },
			{
					"if alias not specified, destalias, srckeypass, and destkeypass must not be specified",
					"si no se especifica el alias, no se puede especificar destalias, srckeypass ni destkeypass" },
			{ "Certificate stored in file <filename>",
					"Certificado almacenado en el archivo <{0}>" },
			{ "Certificate reply was installed in keystore",
					"Se ha instalado la respuesta del certificado en el almac\u00e9n de claves" },
			{ "Certificate reply was not installed in keystore",
					"No se ha instalado la respuesta del certificado en el almac\u00e9n de claves" },
			{ "Certificate was added to keystore",
					"Se ha a\u00f1adido el certificado al almac\u00e9n de claves" },
			{ "Certificate was not added to keystore",
					"No se ha a\u00f1adido el certificado al almac\u00e9n de claves" },
			{ "[Storing ksfname]", "[Almacenando {0}]" },
			{ "alias has no public key (certificate)",
					"{0} no tiene clave p\u00fablica (certificado)" },
			{ "Cannot derive signature algorithm",
					"No se puede derivar el algoritmo de firma" },
			{ "Alias <alias> does not exist", "El alias <{0}> no existe" },
			{ "Alias <alias> has no certificate",
					"El alias <{0}> no tiene certificado" },
			{ "Key pair not generated, alias <alias> already exists",
					"No se ha generado el par de claves, el alias <{0}> ya existe" },
			{ "Cannot derive signature algorithm",
					"No se puede derivar el algoritmo de firma" },
			{
					"Generating keysize bit keyAlgName key pair and self-signed certificate (sigAlgName) with a validity of validality days\n\tfor: x500Name",
					"Generando par de claves {1} de {0} bits para certificado autofirmado ({2}) con una validez de {3} d\u00edas\n\tpara: {4}" },
			{ "Enter key password for <alias>",
					"Escriba la contrase\u00f1a clave para <{0}>" },
			{ "\t(RETURN if same as keystore password):  ",
					"\t(INTRO si es la misma contrase\u00f1a que la del almac\u00e9n de claves):  " },
			{ "Key password is too short - must be at least 6 characters",
					"La contrase\u00f1a clave es demasiado corta; debe tener al menos 6 caracteres" },
			{ "Too many failures - key not added to keystore",
					"Demasiados fallos; no se ha agregado la clave al almac\u00e9n de claves" },
			{ "Destination alias <dest> already exists",
					"El alias de destino <{0}> ya existe" },
			{ "Password is too short - must be at least 6 characters",
					"La contrase\u00f1a es demasiado corta; debe tener al menos 6 caracteres" },
			{ "Too many failures. Key entry not cloned",
					"Demasiados errores. No se ha copiado la entrada de clave" },
			{ "key password for <alias>", "contrase\u00f1a clave para <{0}>" },
			{ "Keystore entry for <id.getName()> already exists",
					"La entrada de almac\u00e9n de claves para <{0}> ya existe" },
			{ "Creating keystore entry for <id.getName()> ...",
					"Creando entrada de almac\u00e9n de claves para <{0}> ..." },
			{ "No entries from identity database added",
					"No se han agregado entradas de la base de datos de identidades" },
			{ "Alias name: alias", "Nombre de alias: {0}" },
			{ "Creation date: keyStore.getCreationDate(alias)",
					"Fecha de creaci\u00f3n: {0,date}" },
			{ "alias, keyStore.getCreationDate(alias), ", "{0}, {1,date}, " },
			{ "alias, ", "{0}, " },
			{ "Entry type: <type>", "Tipo de entrada: {0}" },
			{ "Certificate chain length: ",
					"Longitud de la cadena de certificado: " },
			{ "Certificate[(i + 1)]:", "Certificado[{0,number,integer}]:" },
			{ "Certificate fingerprint (MD5): ",
					"Huella digital de certificado (MD5): " },
			{ "Entry type: trustedCertEntry\n",
					"Tipo de entrada: trustedCertEntry\n" },
			{ "trustedCertEntry,", "trustedCertEntry," },
			{ "Keystore type: ", "Tipo de almac\u00e9n de claves: " },
			{ "Keystore provider: ", "Proveedor de almac\u00e9n de claves: " },
			{ "Your keystore contains keyStore.size() entry",
					"Su almac\u00e9n de claves contiene entrada {0,number,integer}" },
			{ "Your keystore contains keyStore.size() entries",
					"Su almac\u00e9n de claves contiene {0,number,integer} entradas" },
			{ "Failed to parse input", "Error al analizar la entrada" },
			{ "Empty input", "Entrada vac\u00eda" },
			{ "Not X.509 certificate", "No es un certificado X.509" },
			{ "Cannot derive signature algorithm",
					"No se puede derivar el algoritmo de firma" },
			{ "alias has no public key", "{0} no tiene clave p\u00fablica" },
			{ "alias has no X.509 certificate",
					"{0} no tiene certificado X.509" },
			{ "New certificate (self-signed):",
					"Nuevo certificado (autofirmado):" },
			{ "Reply has no certificates", "La respuesta no tiene certificados" },
			{ "Certificate not imported, alias <alias> already exists",
					"Certificado no importado, el alias <{0}> ya existe" },
			{ "Input not an X.509 certificate",
					"La entrada no es un certificado X.509" },
			{
					"Certificate already exists in keystore under alias <trustalias>",
					"El certificado ya existe en el almac\u00e9n de claves con el alias <{0}>" },
			{ "Do you still want to add it? [no]:  ",
					"\u00bfA\u00fan desea agregarlo? [no]:  " },
			{
					"Certificate already exists in system-wide CA keystore under alias <trustalias>",
					"El certificado ya existe en el almac\u00e9n de claves de la CA del sistema, con el alias <{0}>" },
			{ "Do you still want to add it to your own keystore? [no]:  ",
					"\u00bfA\u00fan desea agregarlo a su propio almac\u00e9n de claves? [no]:  " },
			{ "Trust this certificate? [no]:  ",
					"\u00bfConfiar en este certificado? [no]:  " },
			{ "YES", "S\u00cd" },
			{ "New prompt: ", "Nuevo {0}: " },
			{ "Passwords must differ",
					"Las contrase\u00f1as deben ser distintas" },
			{ "Re-enter new prompt: ", "Vuelva a escribir el nuevo {0}: " },
			{ "Re-enter new password: ",
					"Volver a escribir la contrase\u00f1a nueva: " },
			{ "They don't match. Try again",
					"No coinciden. Int\u00e9ntelo de nuevo" },
			{ "Enter prompt alias name:  ",
					"Escriba el nombre de alias de {0}:  " },
			{
					"Enter new alias name\t(RETURN to cancel import for this entry):  ",
					"Indique el nuevo nombre de alias\t(INTRO para cancelar la importaci\u00f3n de esta entrada):  " },
			{ "Enter alias name:  ", "Escriba el nombre de alias:  " },
			{ "\t(RETURN if same as for <otherAlias>)",
					"\t(INTRO si es el mismo que para <{0}>)" },
			{
					"*PATTERN* printX509Cert",
					"Propietario: {0}\nEmisor: {1}\nN\u00famero de serie: {2}\nV\u00e1lido desde: {3} hasta: {4}\nHuellas digitales del certificado:\n\t MD5:  {5}\n\t SHA1: {6}\n\t Nombre del algoritmo de firma: {7}\n\t Versi\u00f3n: {8}" },
			{ "What is your first and last name?",
					"\u00bfCu\u00e1les son su nombre y su apellido?" },
			{ "What is the name of your organizational unit?",
					"\u00bfCu\u00e1l es el nombre de su unidad de organizaci\u00f3n?" },
			{ "What is the name of your organization?",
					"\u00bfCu\u00e1l es el nombre de su organizaci\u00f3n?" },
			{ "What is the name of your City or Locality?",
					"\u00bfCu\u00e1l es el nombre de su ciudad o localidad?" },
			{ "What is the name of your State or Province?",
					"\u00bfCu\u00e1l es el nombre de su estado o provincia?" },
			{ "What is the two-letter country code for this unit?",
					"\u00bfCu\u00e1l es el c\u00f3digo de pa\u00eds de dos letras de la unidad?" },
			{ "Is <name> correct?", "\u00bfEs correcto {0}?" },
			{ "no", "no" },
			{ "yes", "s\u00ed" },
			{ "y", "y" },
			{ "  [defaultValue]:  ", "  [{0}]:  " },
			{ "Alias <alias> has no key", "El alias <{0}> no tiene clave" },
			{
					"Alias <alias> references an entry type that is not a private key entry.  The -keyclone command only supports cloning of private key entries",
					"El alias <{0}> hace referencia a un tipo de entrada que no es una clave privada.  El comando -keyclone s\u00f3lo permite la clonaci\u00f3n de entradas de claves privadas" },

			{ "*****************  WARNING WARNING WARNING  *****************",
					"*****************  ADVERTENCIA ADVERTENCIA ADVERTENCIA  *****************" },

			// Translators of the following 5 pairs, ATTENTION:
			// the next 5 string pairs are meant to be combined into 2
			// paragraphs,
			// 1+3+4 and 2+3+5. make sure your translation also does.
			{
					"* The integrity of the information stored in your keystore  *",
					"* La integridad de la informaci\u00f3n almacenada en su almac\u00e9n de claves  *" },
			{ "* The integrity of the information stored in the srckeystore*",
					"* La totalidad de la informaci\u00f3n almacenada en srckeystore*" },
			{ "* has NOT been verified!  In order to verify its integrity, *",
					"* NO se ha comprobado. Para comprobar dicha integridad, *" },
			{
					"* you must provide your keystore password.                  *",
					"deber\u00e1 proporcionar su contrase\u00f1a de almac\u00e9n de claves.                  *" },
			{ "* you must provide the srckeystore password.                *",
					"* se debe indicar la contrase\u00f1a de srckeystore.                *" },

			{ "Certificate reply does not contain public key for <alias>",
					"La respuesta de certificado no contiene una clave p\u00fablica para <{0}>" },
			{ "Incomplete certificate chain in reply",
					"Cadena de certificado incompleta en la respuesta" },
			{ "Certificate chain in reply does not verify: ",
					"La cadena de certificado de la respuesta no verifica: " },
			{ "Top-level certificate in reply:\n",
					"Certificado de nivel superior en la respuesta:\n" },
			{ "... is not trusted. ", "... no es de confianza. " },
			{ "Install reply anyway? [no]:  ",
					"\u00bfinstalar respuesta de todos modos? [no]:  " },
			{ "NO", "NO" },
			{
					"Public keys in reply and keystore don't match",
					"Las claves p\u00fablicas en la respuesta y en el almac\u00e9n de claves no coinciden" },
			{
					"Certificate reply and certificate in keystore are identical",
					"La respuesta del certificado y el certificado en el almac\u00e9n de claves son id\u00e9nticos" },
			{ "Failed to establish chain from reply",
					"No se ha podido establecer una cadena a partir de la respuesta" },
			{ "n", "n" },
			{ "Wrong answer, try again",
					"Respuesta incorrecta, vuelva a intentarlo" },
			{ "Secret key not generated, alias <alias> already exists",
					"No se ha generado la clave secreta, el alias <{0}> ya existe" },
			{ "Please provide -keysize for secret key generation",
					"Proporcione el valor de -keysize para la generaci\u00f3n de claves secretas" },
			{ "keytool usage:\n", "sintaxis de keytool:\n" },

			{ "Extensions: ", "Extensiones: " },

			{ "-certreq     [-v] [-protected]",
					"-certreq     [-v] [-protected]" },
			{ "\t     [-alias <alias>] [-sigalg <sigalg>]",
					"\t     [-alias <alias>] [-sigalg <algoritmo_firma>]" },
			{ "\t     [-file <csr_file>] [-keypass <keypass>]",
					"\t     [-file <archivo_csr>] [-keypass <contrase\u00f1a_clave>]" },
			{
					"\t     [-keystore <keystore>] [-storepass <storepass>]",
					"\t     [-keystore <almac\u00e9n_claves>] [-storepass <contrase\u00f1a_almac\u00e9n>]" },
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
					"\t     [-alias <alias>] [-file <archivo_cert>]" },
			/** rest is same as -certreq starting from -keystore **/

			// {"-genkey      [-v] [-protected]",
			// "-genkey      [-v] [-protected]"},
			{ "-genkeypair  [-v] [-protected]",
					"-genkeypair  [-v] [-protected]" },
			{ "\t     [-alias <alias>]", "\t     [-alias <alias>]" },
			{ "\t     [-keyalg <keyalg>] [-keysize <keysize>]",
					"\t     [-keyalg <algoritmo_clave>] [-keysize <tama\u00f1o_clave>]" },
			{ "\t     [-sigalg <sigalg>] [-dname <dname>]",
					"\t     [-sigalg <algoritmo_firma>] [-dname <nombre_d>]" },
			{ "\t     [-validity <valDays>] [-keypass <keypass>]",
					"\t     [-validity <d\u00edas_validez>] [-keypass <contrase\u00f1a_clave>]" },
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
					"\t     [-file <archivo_cert>] [-keypass <contrase\u00f1a_clave>]" },
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
					"\t     [-srcprovidername <srcprovidername>]\n\t     [-destprovidername <destprovidername>]", // l\u00ednea
																													// demasiado
																													// larga,
																													// dividir
																													// en
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
			{ "\t     [-keypass <keypass>]",
					"\t     [-keypass <contrase\u00f1a_claves>]" },

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
					"\t     [-keypass <contrase\u00f1a_clave_antigua>] [-new <nueva_contrase\u00f1a_clave>]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-list        [-v | -rfc] [-protected]",
					"-list        [-v | -rfc] [-protected]" },
			{ "\t     [-alias <alias>]", "\t     [-alias <alias>]" },
			/** rest is same as -certreq starting from -keystore **/

			{ "-printcert   [-v] [-file <cert_file>]",
					"-printcert   [-v] [-file <archivo_certif>]" },

			// {"-selfcert    [-v] [-protected]",
			// "-selfcert    [-v] [-protected]"},
			{ "\t     [-alias <alias>]", "\t     [-alias <alias>]" },
			// {"\t     [-dname <dname>] [-validity <valDays>]",
			// "\t     [-dname <dname>] [-validity <valDays>]"},
			// {"\t     [-keypass <keypass>] [-sigalg <sigalg>]",
			// "\t     [-keypass <keypass>] [-sigalg <sigalg>]"},
			/** rest is same as -certreq starting from -keystore **/

			{ "-storepasswd [-v] [-new <new_storepass>]",
					"-storepasswd [-v] [-new <nueva_contrase\u00f1a_almac\u00e9n>]" },
			/** rest is same as -certreq starting from -keystore **/

			// policytool
			{
					"Warning: A public key for alias 'signers[i]' does not exist.  Make sure a KeyStore is properly configured.",
					"Advertencia: No hay clave p\u00fablica para el alias {0}. Compruebe que se haya configurado correctamente un almac\u00e9n de claves." },
			{ "Warning: Class not found: class",
					"Advertencia: No se ha encontrado la clase: {0}" },
			{ "Warning: Invalid argument(s) for constructor: arg",
					"Advertencia: Argumento(s) no v\u00e1lido(s) para el constructor: {0}" },
			{ "Illegal Principal Type: type",
					"Tipo de principal no permitido: {0}" },
			{ "Illegal option: option", "Opci\u00f3n no permitida: {0}" },
			{ "Usage: policytool [options]", "Sintaxis: policytool [opciones]" },
			{ "  [-file <file>]    policy file location",
					"  [-file <archivo>]    ubicaci\u00f3n del archivo de normas" },
			{ "New", "Nuevo" },
			{ "Open", "Abrir" },
			{ "Save", "Guardar" },
			{ "Save As", "Guardar como" },
			{ "View Warning Log", "Ver registro de advertencias" },
			{ "Exit", "Salir" },
			{ "Add Policy Entry", "Agregar entrada de norma" },
			{ "Edit Policy Entry", "Editar entrada de norma" },
			{ "Remove Policy Entry", "Eliminar entrada de norma" },
			{ "Edit", "Editar" },
			{ "Retain", "Mantener" },

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

			{ "Add Public Key Alias", "Agregar alias de clave p\u00fablico" },
			{ "Remove Public Key Alias", "Eliminar alias de clave p\u00fablico" },
			{ "File", "Archivo" },
			{ "KeyStore", "Almac\u00e9n de claves" },
			{ "Policy File:", "Archivo de normas:" },
			{ "Could not open policy file: policyFile: e.toString()",
					"No se ha podido abrir el archivo java.policy: {0}: {1}" },
			{ "Policy Tool", "Herramienta de normas" },
			{
					"Errors have occurred while opening the policy configuration.  View the Warning Log for more information.",
					"Ha habido errores al abrir la configuraci\u00f3n de normas.  V\u00e9ase el registro de advertencias para obtener m\u00e1s informaci\u00f3n." },
			{ "Error", "Error" },
			{ "OK", "Aceptar" },
			{ "Status", "Estado" },
			{ "Warning", "Advertencia" },
			{
					"Permission:                                                       ",
					"Permiso:                                                       " },
			{ "Principal Type:", "Tipo de principal:" },
			{ "Principal Name:", "Nombre de principal:" },
			{
					"Target Name:                                                    ",
					"Nombre de destino:                                                    " },
			{
					"Actions:                                                             ",
					"Acciones:                                                             " },
			{ "OK to overwrite existing file filename?",
					"\u00bfSobrescribir el archivo existente {0}?" },
			{ "Cancel", "Cancelar" },
			{ "CodeBase:", "Base de c\u00f3digos:" },
			{ "SignedBy:", "Firmado por:" },
			{ "Add Principal", "Agregar principal" },
			{ "Edit Principal", "Editar principal" },
			{ "Remove Principal", "Eliminar principal" },
			{ "Principals:", "Principales:" },
			{ "  Add Permission", "  Agregar permiso" },
			{ "  Edit Permission", "  Editar permiso" },
			{ "Remove Permission", "Eliminar permiso" },
			{ "Done", "Terminar" },
			{ "KeyStore URL:", "URL de almac\u00e9n de claves:" },
			{ "KeyStore Type:", "Tipo de almac\u00e9n de claves:" },
			{ "KeyStore Provider:", "Proveedor de almac\u00e9n de claves:" },
			{ "KeyStore Password URL:",
					"URL de contrase\u00f1a de almac\u00e9n de claves:" },
			{ "Principals", "Principales" },
			{ "  Edit Principal:", "  Editar principal:" },
			{ "  Add New Principal:", "  Agregar nuevo principal:" },
			{ "Permissions", "Permisos" },
			{ "  Edit Permission:", "  Editar permiso:" },
			{ "  Add New Permission:", "  Agregar permiso nuevo:" },
			{ "Signed By:", "Firmado por:" },
			{
					"Cannot Specify Principal with a Wildcard Class without a Wildcard Name",
					"No se puede especificar principal con una clase de comod\u00edn sin un nombre de comod\u00edn" },
			{ "Cannot Specify Principal without a Name",
					"No se puede especificar principal sin un nombre" },
			{ "Permission and Target Name must have a value",
					"Permiso y Nombre de destino deben tener un valor" },
			{ "Remove this Policy Entry?",
					"\u00bfEliminar esta entrada de norma?" },
			{ "Overwrite File", "Sobrescribir archivo" },
			{ "Policy successfully written to filename",
					"Norma escrita satisfactoriamente en {0}" },
			{ "null filename", "nombre de archivo nulo" },
			{ "Save changes?", "\u00bfGuardar los cambios?" },
			{ "Yes", "S\u00ed" },
			{ "No", "No" },
			{ "Policy Entry", "Entrada de norma" },
			{ "Save Changes", "Guardar cambios" },
			{ "No Policy Entry selected",
					"No se ha seleccionado entrada de norma" },
			{ "Unable to open KeyStore: ex.toString()",
					"No se puede abrir el almac\u00e9n de claves: {0}" },
			{ "No principal selected", "No se ha seleccionado principal" },
			{ "No permission selected", "No se ha seleccionado un permiso" },
			{ "name", "nombre" },
			{ "configuration type", "tipo de configuraci\u00f3n" },
			{ "environment variable name", "nombre de variable de entorno" },
			{ "library name", "nombre de la biblioteca" },
			{ "package name", "nombre del paquete" },
			{ "policy type", "tipo de directiva" },
			{ "property name", "nombre de la propiedad" },
			{ "provider name", "nombre del proveedor" },
			{ "Principal List", "Lista de principales" },
			{ "Permission List", "Lista de permisos" },
			{ "Code Base", "Base de c\u00f3digo" },
			{ "KeyStore U R L:", "URL de almac\u00e9n de claves:" },
			{ "KeyStore Password U R L:",
					"URL de contrase\u00f1a de almac\u00e9n de claves:" },

			// javax.security.auth.PrivateCredentialPermission
			{ "invalid null input(s)", "entradas nulas no v\u00e1lidas" },
			{ "actions can only be 'read'",
					"las acciones s\u00f3lo pueden 'leerse'" },
			{ "permission name [name] syntax invalid: ",
					"sintaxis de nombre de permiso [{0}] no v\u00e1lida: " },
			{ "Credential Class not followed by a Principal Class and Name",
					"Clase de credencial no va seguida de una clase y nombre de principal" },
			{ "Principal Class not followed by a Principal Name",
					"La clase de principal no va seguida de un nombre de principal" },
			{ "Principal Name must be surrounded by quotes",
					"El nombre de principal debe ir entre comillas" },
			{ "Principal Name missing end quote",
					"Faltan las comillas finales en el nombre de principal" },
			{
					"PrivateCredentialPermission Principal Class can not be a wildcard (*) value if Principal Name is not a wildcard (*) value",
					"La clase de principal PrivateCredentialPermission no puede ser un valor comod\u00edn (*) si el nombre de principal no lo es tambi\u00e9n" },
			{ "CredOwner:\n\tPrincipal Class = class\n\tPrincipal Name = name",
					"CredOwner:\n\tClase de principal = {0}\n\tNombre de principal = {1}" },

			// javax.security.auth.x500
			{ "provided null name", "se ha proporcionado un nombre nulo" },
			{ "provided null keyword map",
					"mapa de palabras proporcionado nulo" },
			{ "provided null OID map", "mapa de OID proporcionado nulo" },

			// javax.security.auth.Subject
			{ "invalid null AccessControlContext provided",
					"se ha proporcionado un AccessControlContext nulo no v\u00e1lido" },
			{ "invalid null action provided",
					"se ha proporcionado una acci\u00f3n nula no v\u00e1lida" },
			{ "invalid null Class provided",
					"se ha proporcionado una clase nula no v\u00e1lida" },
			{ "Subject:\n", "Asunto:\n" },
			{ "\tPrincipal: ", "\tPrincipal: " },
			{ "\tPublic Credential: ", "\tCredencial p\u00fablica: " },
			{ "\tPrivate Credentials inaccessible\n",
					"\tCredenciales privadas inaccesibles\n" },
			{ "\tPrivate Credential: ", "\tCredencial privada: " },
			{ "\tPrivate Credential inaccessible\n",
					"\tCredencial privada inaccesible\n" },
			{ "Subject is read-only", "El asunto es de s\u00f3lo lectura" },
			{
					"attempting to add an object which is not an instance of java.security.Principal to a Subject's Principal Set",
					"intentando agregar un objeto que no es un ejemplar de java.security.Principal al conjunto principal de un asunto" },
			{ "attempting to add an object which is not an instance of class",
					"intentando agregar un objeto que no es un ejemplar de {0}" },

			// javax.security.auth.login.AppConfigurationEntry
			{ "LoginModuleControlFlag: ", "LoginModuleControlFlag: " },

			// javax.security.auth.login.LoginContext
			{ "Invalid null input: name", "Entrada nula no v\u00e1lida: nombre" },
			{ "No LoginModules configured for name",
					"No se han configurado LoginModules para {0}" },
			{ "invalid null Subject provided",
					"se ha proporcionado un asunto nulo no v\u00e1lido" },
			{ "invalid null CallbackHandler provided",
					"se ha proporcionado CallbackHandler nulo no v\u00e1lido" },
			{
					"null subject - logout called before login",
					"asunto nulo - se ha llamado a fin de sesi\u00f3n antes del inicio de sesi\u00f3n" },
			{
					"unable to instantiate LoginModule, module, because it does not provide a no-argument constructor",
					"no se puede lanzar un ejemplar de LoginModule, {0}, porque no incluye un constructor no-argumento" },
			{ "unable to instantiate LoginModule",
					"no se puede lanzar un ejemplar de LoginModule" },
			{ "unable to instantiate LoginModule: ",
					"no se puede instanciar LoginModule: " },
			{ "unable to find LoginModule class: ",
					"no se puede encontrar la clase LoginModule: " },
			{ "unable to access LoginModule: ",
					"no se puede acceder a LoginModule: " },
			{
					"Login Failure: all modules ignored",
					"Fallo en inicio de sesi\u00f3n: se ha hecho caso omiso de todos los m\u00f3dulos" },

			// sun.security.provider.PolicyFile

			{ "java.security.policy: error parsing policy:\n\tmessage",
					"java.security.policy: error de an\u00e1lisis de {0}:\n\t{1}" },
			{
					"java.security.policy: error adding Permission, perm:\n\tmessage",
					"java.security.policy: error al agregar Permiso, {0}:\n\t{1}" },
			{ "java.security.policy: error adding Entry:\n\tmessage",
					"java.security.policy: error al agregar Entrada:\n\t{0}" },
			{ "alias name not provided (pe.name)",
					"no se ha proporcionado nombre de alias ({0})" },
			{ "unable to perform substitution on alias, suffix",
					"no se puede realizar la sustituci\u00f3n en el alias, {0}" },
			{ "substitution value, prefix, unsupported",
					"valor de sustituci\u00f3n, {0}, no soportado" },
			{ "(", "(" },
			{ ")", ")" },
			{ "type can't be null", "el tipo no puede ser nulo" },

			// sun.security.provider.PolicyParser
			{
					"keystorePasswordURL can not be specified without also specifying keystore",
					"keystorePasswordURL no puede especificarse sin especificar tambi\u00e9n el almac\u00e9n de claves" },
			{ "expected keystore type",
					"se esperaba un tipo de almac\u00e9n de claves" },
			{ "expected keystore provider",
					"se esperaba un proveedor de almac\u00e9n de claves" },
			{ "multiple Codebase expressions",
					"expresiones m\u00faltiples de base de c\u00f3digos" },
			{ "multiple SignedBy expressions",
					"expresiones m\u00faltiples de SignedBy" },
			{ "SignedBy has empty alias", "SignedBy tiene un alias vac\u00edo" },
			{
					"can not specify Principal with a wildcard class without a wildcard name",
					"no se puede especificar Principal con una clase de comod\u00edn sin un nombre de comod\u00edn" },
			{ "expected codeBase or SignedBy or Principal",
					"se esperaba base de c\u00f3digos o SignedBy o Principal" },
			{ "expected permission entry", "se esperaba un permiso de entrada" },
			{ "number ", "n\u00famero " },
			{ "expected [expect], read [end of file]",
					"se esperaba [{0}], se ha le\u00eddo [end of file]" },
			{ "expected [;], read [end of file]",
					"se esperaba [;], se ha le\u00eddo [end of file]" },
			{ "line number: msg", "l\u00ednea {0}: {1}" },
			{ "line number: expected [expect], found [actual]",
					"l\u00ednea {0}: se esperaba [{1}], se ha encontrado [{2}]" },
			{ "null principalClass or principalName",
					"principalClass o principalName nulos" },

			// sun.security.pkcs11.SunPKCS11
			{ "PKCS11 Token [providerName] Password: ",
					"Contrase\u00f1a de la tarjeta de claves PKCS11 [{0}]: " },

			/* --- DEPRECATED --- */
			// javax.security.auth.Policy
			{ "unable to instantiate Subject-based policy",
					"no se puede instanciar una directiva basada en Subject" } };

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
