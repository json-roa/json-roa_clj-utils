
# JSON-ROA Utils for Clojure

This library provides utilities to write [JSON-ROA][] enabled APIs.

## Usage

See also the section [Usage Visualization][].

### Dependency 

    [json-roa/clj-utils "1.0.0-beta.5"]

### Code Example

```clojure
(-> your-json-handler
    (json-roa.ring-middleware.request/wrap your-json-roa-handler)
    json-roa.ring-middleware.response/wrap
    ring.middleware.json/wrap-json-response
    )
```

### Response Wrapper

The [ring middleware][] `json-roa.ring-middleware.response/wrap` is
functionally similar to `wrap-json-response` of [Ring-JSON][]. It will covert
the body of the response into JSON and the content type to
`application/json-roaٍ+json` if and only if it finds a JSON-ROA signature in the
body. 

### Request and Build Wrapper

The middleware `json-roa.ring-middleware.request/wrap` provides a mechanism to
dispatch on the accept types `application/json-roa+json` or `application/json`
and to add `JSON-ROA` data in the former case.

Note that `your-json-roa-handler` does not have the same signature compared to
a normal ring handler. It takes two arguments `[request response]`. It is
defined in this way to take the request into account when adding the JSON-ROA
extension to the response.

## Advanced Usage and Internals

### Unobtrusiveness and Content Type Negotiation 

The JSON-ROA middlware helpers are designed such that they can be used to
extend an existing pure JSON API to a JSON-ROA enabled API. Also clients are in
no way forced to use `application/json-roa+json` and can behave like normal
JSON clients if the send the appropriate accept header. 

The request wrapper takes the accept header into account to negotiate between
the content types  `application/json-roa+json` or `application/json`. It
considers also the quality factor to this end and favors
`application/json-roa+json` over `application/json` by an internal quality of
service factor. 

### JSON Generation

The response wrapper uses [data.json][] to generate the JSON response by
default. This is in contrast to [Ring-JSON][] which uses [Cheshire][]. However,
the response wrapper can be configured to use any JSON encoder desired by
giving an appropriate function to the `:json-encoder` option. 

### Usage Visualization 

![](https://rawgit.com/json-roa/json-roa_clj-utils/master/docs/json-roa-middleware.svg)



## Copyright and License 

### Copyright 

Copyright © 2015 Thomas Schank

### License 

Released to the public under the [MIT License](http://opensource.org/licenses/MIT).


  [Cheshire]: https://github.com/dakrone/cheshire
  [JSON-ROA]: http://json-roa.github.io/
  [Ring-JSON]: https://github.com/ring-clojure/ring-json
  [Usage Visualization]: #usage-visualization
  [data.json]: https://github.com/clojure/data.json
  [ring middleware]: https://github.com/ring-clojure/ring/wiki/Concepts#middleware
