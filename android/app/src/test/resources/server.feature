# https://karatelabs.github.io/karate/karate-netty/

Feature: Http Server

    Scenario: pathMatches('/hello') && methodIs('GET')
        * def response = 'Hello ' + (requestParams.name ? requestParams.name[0] : 'Anonymous') + '!'

    Scenario: pathMatches('/delay') && methodIs('GET')
        * def responseDelay = parseInt(requestParams.delay) * 1000
        * def response = 'OK'

    Scenario: pathMatches('/status') && methodIs('GET')
        * def responseStatus = parseInt(requestParams.status)
        * def response = 'OK'

    Scenario: pathMatches('/redirect') && methodIs('GET')
        * def responseStatus = parseInt(requestParams.count) == 0 ? 200 : 307
        * def location = `/redirect?count=${parseInt(requestParams.count) - 1}`
        * def responseHeaders = { 'Location': #(location) }
        * def response = `OK ${parseInt(requestParams.count)}`

    Scenario: pathMatches('/headers') && methodIs('GET')
        * def responseHeaders = requestHeaders
        * def response = 'OK'

    Scenario: pathMatches('/text') && methodIs('POST')
        * def type = requestHeaders['content-type'][0]
        * def responseHeaders = { 'content-type': #(type) }
        * def response = request

    Scenario: pathMatches('/form') && methodIs('POST')
        * def response = requestParams

    Scenario: pathMatches('/multipart') && methodIs('POST')
        * def type = requestHeaders['content-type'][0]
        * def response = { params: #(requestParams), parts: #(requestParts), type: #(type) }

    Scenario: pathMatches('/raw') && methodIs('POST')
        * def response = requestBytes
