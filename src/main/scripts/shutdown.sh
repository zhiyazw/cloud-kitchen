#!/bin/bash
ps -ef | grep CloudKitchenApplication | grep java | awk '{print $2}' | xargs -l -t kill -9