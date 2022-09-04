# https://karatelabs.github.io/karate/karate-netty/

Feature: Http Server

    Background:
        * configure cors = true
        * def sleep = function(s) { java.lang.Thread.sleep(parseInt(s) * 1000) }

    Scenario: pathMatches('/name') && methodIs('GET')
        * def response = requestParams.name ? requestParams.name[0] : 'Anonymous'

    Scenario: pathMatches('/delay') && methodIs('GET')
        * sleep(requestParams.delay)
        * def response = 'OK'
