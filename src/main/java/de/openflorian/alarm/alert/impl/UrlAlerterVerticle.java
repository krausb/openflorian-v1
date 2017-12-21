package de.openflorian.alarm.alert.impl;

/*
 * This file is part of Openflorian.
 *
 * Copyright (C) 2015 - 2018  Bastian Kraus
 *
 * Openflorian is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version)
 *
 * Openflorian is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Openflorian.  If not, see <http://www.gnu.org/licenses/>.
 */

import de.openflorian.alarm.alert.AbstractAlerter;
import de.openflorian.data.model.Operation;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.openflorian.OpenflorianGlobals.HTTP_USER_AGENT;

/**
 * URL Alerter
 *
 * Alerts / sends serialized {@link Operation} to a configured URL.
 */
public class UrlAlerterVerticle extends AbstractAlerter {

    private static final Logger log = LoggerFactory.getLogger(UrlAlerterVerticle.class);

    private final HttpClient httpClient = new DefaultHttpClient();

    private final String url;
    private final HttpPost postRequest;

    /**
     * CTOR for configuring an URL to alert {@link Operation}s to.
     *
     * @param url
     */
    public UrlAlerterVerticle(String url) {
        this.url = url;
        postRequest = new HttpPost(this.url);
        // add header
        postRequest.setHeader("User-Agent", HTTP_USER_AGENT);
    }

    @Override
    public void alert(Operation operation) throws Exception {

        log.info("Alerting incurred operation to URL: " + url);

        // Building HTTP Post Request
        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(operation);
        postRequest.setEntity(new StringEntity(payload));

        // Executing HTTP Post Request
        HttpResponse response = httpClient.execute(postRequest);
        log.info("Response status: " + url);
    }

}
