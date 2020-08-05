# testjsonpatch

Generated using Luminus version "3.57"

lein new luminus +swagger +kee-frame +shadow-cljs +kibit

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

Currently this application depends on the new version of clj-json-patch of daviddpark, which I made also available for ClojureScript.
Because this library is not yet merged with the original version it is now included in this library in resources/lib/clj-json-patch-0.1.8.jar.
Once my pull request is merged I will change this test application so it will retrieve this library via project.clj - dependencies


To start a web server for the application, run:

    lein run

## License

Copyright © 2020 FIXME
