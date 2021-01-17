#!/bin/bash
nohup java -classpath './lib/*:./conf/' com.css.cloudkitchen.CloudKitchenApplication >run.log 2>&1 &