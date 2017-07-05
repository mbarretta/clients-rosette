package com.elastic.barretta.analytics.rosette

import spock.lang.Shared
import spock.lang.Specification

class RosetteApiClientSpec extends Specification {
    @Shared
    def properties = new ConfigSlurper().parse(this.class.classLoader.getResource("testProperties.groovy"))

    @Shared
    def client = new RosetteApiClient(new RosetteApiClient.Config(url: properties.url, key: properties.key))


    def "can get entities"() {
        setup:
        def text = "Bill Murray will appear in new Ghostbusters film: Dr. Peter Venkman was spotted filming a cameo in Boston this ... http://dlvr.it/BnsFfS"

        when:
        def response = client.getEntities(text)

        then:
        response[0].type == "PERSON"
        response[0].normalized == "Bill Murray"
        response[1].type == "PRODUCT"
        response[3].normalized == "Peter Venkman"

    }
}
