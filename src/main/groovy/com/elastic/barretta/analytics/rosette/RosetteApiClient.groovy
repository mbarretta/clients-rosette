package com.elastic.barretta.analytics.rosette

import wslite.rest.RESTClient
import wslite.rest.RESTClientException

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
        this.config = config
        if (this.config.key) {
            headers = ["X-RosetteAPI-Key": this.config.key]
        }
    }

    def test() throws RESTClientException {
        client.get(path: "/ping", headers: headers)
    }

    def getEntities(text) {
        def response = client.post(path: "/entities", headers: headers) {
            json content: text
        }
        return response.json.entities.collect()
    }
}
