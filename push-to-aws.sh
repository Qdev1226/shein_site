#!/bin/bash

cd .. && aws s3 sync Shein-Site s3://sheinhtike.com/ --exclude "*" --include "/*.html" --exclude "*template.html" --include "writeups/*" --include "*.css" --include "images/*" --exclude "*.DS_Store" --include "fonts/*"
