#!/usr/bin/env python

import cantools #$ pip install cantools
import os
from pprint import pprint
import subprocess
import sys

commonDir = 'common-f7'
genDir = 'common-f7/Scripts/Gen'

genIncDir = os.path.join(genDir, 'Inc')

ScriptsDir = os.path.join(commonDir, 'Scripts')

depFile = os.path.join(genDir, 'scriptGen.d')

dataDir = 'common-f7/data'
dbFile = os.path.join(dataDir, '2018CAR.dbc')

scriptFile = os.path.join(genIncDir, '_strings.txt')

db = cantools.db.load_file(dbFile)

label = '0df2a9a'
gitClean = '0';

gitCommit = label

def fWrite(string, fileHandle):
    fileHandle.write(string + '\n')

def generateDepedencyFile(headerFile):
    with open(depFile, 'w') as depFileHandle:
        fWrite('{scriptFile}: {dir}/generateStrings.py {dbFile}'.format(scriptFile=scriptFile, dbFile=dbFile, dir=ScriptsDir), depFileHandle)



#make .h file

sys.stdout = open(scriptFile, "w")
names = []
print("//generated string containing variable names and types")
print("public static final String FESchema = \"{\"")
print("+ \"\\\"type\\\":\\\"record\\\",\"")
print("+ \"\\\"name\\\":\\\"FERecord\\\",\"")
print("+ \"\\\"type\\\":\\\"[\"")
print("\t+ \"\tname\\\":\\\state\\\", \\\"type\\\":\\\"int\\\" },")

for mes in db.messages:
    for sig in mes.signals:
        if sig.name not in names:
            print("\t+ \"\tname\\\":\\\""+sig.name+"\\\", \\\"type\\\":\\\"float\\\" },\"")
        names.append(sig.name)

print("\t+\"]}\";")


generateDepedencyFile(scriptFile)
