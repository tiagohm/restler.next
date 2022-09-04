# https://karatelabs.github.io/karate/karate-netty/

Feature: Http Server

    Background:
        * configure cors = true

    Scenario: pathMatches('/hello') && methodIs('GET')
        * print requestHeaders
        * print requestParams
        * print request
        * def response = requestParams.name[0] || 'Anonymous'
