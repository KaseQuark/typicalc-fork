#!/bin/sh
rg edu.kit.typicalc.model | rg --only-matching -r '$1' 'line-rate="([0-9.]+)"' | head -n 1 | awk '{ print "Test Coverage: " ($1 * 100) "%" }'
