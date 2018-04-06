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

scriptFile = os.path.join(genIncDir, '_switch.txt')

db = cantools.db.load_file(dbFile)

label = '0df2a9a'
gitClean = '0';

gitCommit = label

def fWrite(string, fileHandle):
    fileHandle.write(string + '\n')

def generateDepedencyFile(headerFile):
    with open(depFile, 'w') as depFileHandle:
        fWrite('{scriptFile}: {dir}/generateSwitch.py {dbFile}'.format(scriptFile=scriptFile, dbFile=dbFile, dir=ScriptsDir), depFileHandle)



#make .h file

sys.stdout = open(scriptFile, "w")
print("//switch case for the can messages")
print("public void translate(HashMap hm, long value, int id){")
print("\tlong shift = 0;")
print("\tdouble temp = 0;")
print("\tswitch (id) {")
name = list()
done = 0
for mes in db.messages:
    print('\t\tcase '+str(mes.frame_id)+':')
    print("\t\t\t//"+mes.name)
    for sig in mes.signals:
            print("\t\t\tshift = (1<<"+str(sig.length)+")-1;")
            print("\t\t\ttemp = (value>>"+str(sig.start)+")&shift;")
            print("\t\t\ttemp = temp*"+str(sig.scale)+";")
            print("\t\t\ttemp = temp+"+str(sig.offset)+";")
            print("\t\t\thm.put("+"\""+sig.name+"\""+", temp);\n")
    print("\t\t\tbreak;")
print("\t}")
print("}")


generateDepedencyFile(scriptFile)
