import os;
import subprocess;
import shutil;

# Przycinanie

for root, dirs, files in os.walk('images/'):
    for file in files:
        prefixSize = len('images/')
        fullFilename = root + file
        relative = fullFilename[prefixSize:]
        if file.endswith('.png'):
            print '[CROP] '+fullFilename
            theproc = subprocess.Popen(['convert',fullFilename,'-crop','600x600+420+0',fullFilename], shell = True)
            theproc.communicate()


# Resize

for root, dirs, files in os.walk('images/'):
    for file in files:
        prefixSize = len('images/')
        fullFilename = root + file
        relative = fullFilename[prefixSize:]
        if file.endswith('.png'):
            print '[RESIZE] '+fullFilename
            theproc = subprocess.Popen(['convert',fullFilename,'-resize','80x80',fullFilename], shell = True)
            theproc.communicate()
