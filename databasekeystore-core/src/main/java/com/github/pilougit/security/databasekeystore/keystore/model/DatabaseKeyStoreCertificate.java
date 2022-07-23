/*
 *
 *
 * Copyright 2022 Pierre-Emmanuel Gros.
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package com.github.pilougit.security.databasekeystore.keystore.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Base64;

@Slf4j
@Data
@RequiredArgsConstructor
public class DatabaseKeyStoreCertificate {
    @NonNull
    protected String type;
    @NonNull
    protected String cert;

    public DatabaseKeyStoreCertificate(Certificate certificate) throws CertificateEncodingException {
        cert = Base64.getEncoder().encodeToString(certificate.getEncoded());
        type = certificate.getType();
    }

    public Certificate convertToCertificate() throws CertificateException {


        byte[] certificateByteArray = Base64.getDecoder().decode(cert);
        CertificateFactory certFactory = CertificateFactory.getInstance(type);
        return certFactory.generateCertificate(new ByteArrayInputStream(certificateByteArray));

    }

}
