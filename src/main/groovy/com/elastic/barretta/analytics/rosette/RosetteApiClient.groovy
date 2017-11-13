package com.elastic.barretta.analytics.rosette

import groovy.json.JsonOutput
import groovy.util.logging.Slf4j
import wslite.rest.RESTClient
import wslite.rest.RESTClientException

@Slf4j
class RosetteApiClient {
    private RESTClient client
    private Config config
    private def headers

    static class Config {
        String url
        String key
    }

    RosetteApiClient(Config config) {
        client = new RESTClient(config.url)
        client.httpClient.connectTimeout = 5000
        this.config = config
        if (this.config.key) {
            headers = ["X-RosetteAPI-Key": this.config.key]
        }
        test()
    }

    def test() throws RESTClientException {
        client.get(path: "/ping", headers: headers)
        log.info("able to connect to Rosette API [$config.url]")
    }

    List getEntities(text) {
        def returnList = []
        try {
            if (text && !text.trim().isEmpty()) {
                def response = client.post(path: "/entities", headers: headers) {
                    json content: text
                }
                returnList = response.json.entities.collect()
            }
        } catch (e) {
            log.error("error fetching entities [$e.cause]")
        }
        return returnList
    }

    Map getSentiment(text) {
        def returnMap = [:]
        try {
            if (text && !text.trim().isEmpty()) {
                def response = client.post(path: "/sentiment", headers: headers) {
                    json content: text
                }
                returnMap = response.json
            }
        } catch (RESTClientException e) {
            log.error("error getting sentiment [$e.cause]")
            if (e.response.statusCode == 400) {
                log.error("detail [\n${JsonOutput.prettyPrint(new String(e.response.data))}\n]")
            }
        }
        return returnMap
    }
}